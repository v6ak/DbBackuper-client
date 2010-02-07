package v6.apps.clients.backup.db;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import lombok.AccessLevel;
import lombok.Cleanup;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;

@Data
abstract class RequestBackuper implements Runnable {
	
	@Getter(AccessLevel.NONE)
	final @NonNull DbBackuper dbb;

	@Getter(AccessLevel.NONE)
	private final @NonNull String urlBase;

	abstract protected String getUrlSuffix();

	abstract protected String getFileName();

	abstract protected void start();

	abstract protected void success();

	abstract protected void error();

	public void run() {
		// System.out.println("* def: "+table);
		start();
		try {
			@Cleanup("disconnect")
			HttpURLConnection conn = (HttpURLConnection) new URL(urlBase
					+ getUrlSuffix()).openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			dbb.modify(conn);
			@Cleanup
			final BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.forName("utf-8")));
			@Cleanup
			final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(dbb.filePrefix + getFileName())));
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
			System.err.println("(!) some error: "+e);
			e.printStackTrace();
		}
	}

}
