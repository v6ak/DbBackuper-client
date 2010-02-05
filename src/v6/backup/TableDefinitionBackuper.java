package v6.backup;

import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;

final class TableDefinitionBackuper extends RequestBackuper {

	private String table;

	public TableDefinitionBackuper(DbBackuper b, String url, String table) {
		super(b, url);
		this.table = table;
	}

	protected String getUrlSuffix() {
		String r = null;
		try {
			r = "?action=create+table&table="
					+ URLEncoder.encode(table, "utf-8");
		} catch (UnsupportedEncodingException e) {
			r = "error";
		}
		;
		return r;
	}

	protected String getFileName() {
		return table + ".def.tbl";
	}

	protected void error() {
		System.out.println("(!) Tabulka " + table
				+ ": Nepodarilo se zalohovat definici tabulky!");
	}

	protected void success() {
		System.out.println("(+) Tabulka " + table
				+ ": Definice tabulky uspesne zalohovana.");
	}

	protected void start() {
		System.out.println("(i) Tabulka " + table
				+ ": Zalohuji definici tabulky.");
	}

}
