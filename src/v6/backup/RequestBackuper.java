package v6.backup;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import lombok.Cleanup;

abstract class RequestBackuper implements Runnable {

	private String urlBase;

	DbBackuper dbb;

	public RequestBackuper(DbBackuper b, String urlBase) {
		this.urlBase = urlBase;
		dbb = b;
	}

	abstract protected String getUrlSuffix();

	abstract protected String getFileName();

	abstract protected void start();

	abstract protected void success();

	abstract protected void error();

	public void run() {
		// System.out.println("* def: "+table);
		start();
		try {
			@Cleanup
			BufferedReader in = null;
			@Cleanup
			PrintWriter out = null;
			@Cleanup("disconnect")
			HttpURLConnection conn = (HttpURLConnection) new URL(urlBase
					+ getUrlSuffix()).openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			dbb.connectionModifer.modify(conn);
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			out = new PrintWriter(new BufferedWriter(new FileWriter(
					dbb.filePrefix + getFileName())));// */System.out;
			String s, lastline = "";
			while ((s = in.readLine()) != null) {
				lastline = s;
				// System.out.println("---"+s );
				out.println(s);
			}
			if (lastline.equals(DbBackuper.OK_MARK)) {
				success();
			} else {
				dbb.errs++;
				error();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
