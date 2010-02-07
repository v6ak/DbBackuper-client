package v6.apps.clients.backup.db;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;

@Data
class TableBackuper implements Runnable {
	
	@Getter(AccessLevel.NONE)
	private final @NonNull DbBackuper dbb;

	@Getter(AccessLevel.NONE)
	private final @NonNull String urlBase;
	
	@Getter(AccessLevel.NONE)
	private final @NonNull String table;

	public void run() {
		Thread t1 = null, t2 = null;
		// System.out.println("* "+table);
		if (dbb.backupTableData) {
			t1 = new Thread(new TableDataBackuper(dbb, urlBase, table));
			t1.start();
		}
		if (dbb.backupTableDefinition) {
			t2 = new Thread(new TableDefinitionBackuper(dbb, urlBase, table));
			t2.start();
		}
		try {
			if (t1 != null) {
				t1.join();
			}
			if (t2 != null) {
				t2.join();
			}
		} catch (InterruptedException e) {
		}
	}

}
