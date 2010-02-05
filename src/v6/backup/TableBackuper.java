package v6.backup;

class TableBackuper implements Runnable {

	String urlBase, table;

	DbBackuper dbb;

	public TableBackuper(DbBackuper b, String url, String table) {
		urlBase = url;
		this.table = table;
		dbb = b;
	}

	public void run() {
		Thread t1 = null, t2 = null;
		// System.out.println("* "+table);
		if (dbb.backupTableData) {
			t1 = new Thread(new TableDataBackuper(dbb, urlBase, table));
			t1.start();
		}
		;
		if (dbb.backupTableDefinition) {
			t2 = new Thread(new TableDefinitionBackuper(dbb, urlBase, table));
			t2.start();
		}
		;
		try {
			if (t1 != null) {
				t1.join();
			}
			;
			if (t2 != null) {
				t2.join();
			}
			;
		} catch (InterruptedException e) {
		}
		;
	}

}
