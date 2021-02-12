package org.lattice.spectrum_backend_final.dao.util;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.Location;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.manager.DbConnectionManager;
import org.lattice.spectrum_backend_final.property.ResourceManager;
import org.sqlite.SQLiteDataSource;

import com.lattice.spectrum.ComLibrary.utility.sLog;

public final class DatabaseMigration {

	private static DatabaseMigration databaseMigration;

	public static DatabaseMigration getInstance() {

		synchronized (DatabaseMigration.class) {
			if (databaseMigration == null) {
				databaseMigration = new DatabaseMigration();
			}
		}
		return databaseMigration;
	}

	private static final String KFCOMM2C_SQL_SCRIPT_LOCATION = "./jre/migration/KFComm2C";
	private static final String KFCOMM2_SQL_SCRIPT_LOCATION = "./jre/migration/KFComm2";
	private static final String DB_MIGRATE_UP_TO_DATE_MESSAGE = "Database already up to date.";
	private static final String DB_MIGRATE_SUCCESS_MESSAGE = "Database migrated successfully, Total migrations: ";
	private static final String DB_MIGRATE_LOCATION_ERROR_MESSAGE = "Unable to found migration scripts. Please check and try again.";
	private static final String CURRENT_VERSION_SCRIPT = "CURRENT_VERSION_SCRIPT: ";

	/**
	 * This is the starting point for db migration process. This method is used to
	 * get the DB url, pass it to the getScriptLocation method to get the location
	 * of the migration folder, creates a datasource instance and finally pass the
	 * location and datasource instances to the flywayMigrate method for further
	 * implementation.
	 */
	public final void migrate() {
		Logger.setMigrationLogToFile(true);
		sLog.d(this, "inside migrate method...");
		try {
			String dbUrl = DbConnectionManager.getInstance().getDB_URL();
			final Location location = getScriptLocation(dbUrl);
			if (location == null || !location.isFileSystem()) {
				throw new RuntimeException(DB_MIGRATE_LOCATION_ERROR_MESSAGE);
			}
			final SQLiteDataSource dataSource = new SQLiteDataSource();
			dataSource.setUrl(dbUrl);
			flywayMigrate(location, dataSource);
			dataSource.getConnection().close();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex.getCause().getLocalizedMessage());
		}
	}

	/**
	 * This method is used to get the location of folder which contains the
	 * migration scripts based on database.
	 * 
	 * @implNote DB filename should be either kfcomm2c.db or kfcomm2.db
	 * @implNote migration folder should be present in jre folder along with
	 *           migration scripts.
	 * 
	 * @param dbUrl - URL of the database;
	 * @return either new location instance or null
	 */
	private final Location getScriptLocation(String dbUrl) {
		if (ApiConstant.KFCOMM2C_DB.equalsIgnoreCase(ResourceManager.getProperty(ApiConstant.DEFAULT_DB_NAME))) {
			return new Location(Location.FILESYSTEM_PREFIX.concat(KFCOMM2C_SQL_SCRIPT_LOCATION));
		} else if (ApiConstant.KFCOMM2_DB.equalsIgnoreCase(ResourceManager.getProperty(ApiConstant.DEFAULT_DB_NAME))) {
			return new Location(Location.FILESYSTEM_PREFIX.concat(KFCOMM2_SQL_SCRIPT_LOCATION));
		} else {
			return null;
		}
	}

	/**
	 * This method creates a configuration which can be customized to your needs
	 * before being loaded into a new Flyway instance using the load() method. In
	 * its simplest form, this is how you configure Flyway with all defaults to get
	 * started. After that you have a fully-configured Flyway instance at your
	 * disposal which can be used to invoke Flyway functionality such as migrate()
	 * or clean() or repair().
	 *
	 * @param location   - Location of migration folder.
	 * @param dataSource
	 */
	private final void flywayMigrate(final Location location, final SQLiteDataSource dataSource) {
		final Flyway flyway = Flyway.configure().baselineOnMigrate(true).baselineVersion("0").dataSource(dataSource)
				.locations(location).validateOnMigrate(true).cleanDisabled(true).connectRetries(2).load();
		flyway.repair();
		final int totalMigrations = flyway.migrate();
		sLog.d(this,
				totalMigrations == 0 ? DB_MIGRATE_UP_TO_DATE_MESSAGE : DB_MIGRATE_SUCCESS_MESSAGE + totalMigrations);
		sLog.d(this, CURRENT_VERSION_SCRIPT.concat(flyway.info().current().getScript()));
	}

}
