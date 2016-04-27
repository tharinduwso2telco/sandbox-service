package com.wso2telco.note.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.wso2telco.note.exception.NoteException;

public class NoteDBUtils {

	private static final Log log = LogFactory.getLog(NoteDBUtils.class);
	private static Connection connection;

	public static synchronized Connection getNoteDBConnection() throws NoteException {

		try {
			if (connection == null || connection.isClosed()) {

				Class.forName(ConfigurationLoader.getDbDriver()).newInstance();
				String databaseConnectionURL = ConfigurationLoader.getDbConnectionURL() + ""
						+ ConfigurationLoader.getDbHost() + ":" + ConfigurationLoader.getDbPort() + "/"
						+ ConfigurationLoader.getDbName() + "?autoReconnect=" + ConfigurationLoader.getAutoReconnect();

				log.debug("DBUtils getNoteDBConnection -> connection_url : " + databaseConnectionURL);

				connection = DriverManager.getConnection(databaseConnectionURL, ConfigurationLoader.getDbUsername(),
						ConfigurationLoader.getDbPassword());
			}
		} catch (InstantiationException e) {

			log.error("Error in NoteDBUtils getNoteDBConnection -> failed to instantiate database connection : ", e);
			throw new NoteException(ErrorCodes.ERROR_DATABASE_CONNECTION_INSTANTIATION, e);
		} catch (IllegalAccessException e) {

			log.error("Error in NoteDBUtils getNoteDBConnection -> illegal access of database : ", e);
			throw new NoteException(ErrorCodes.ERROR_DATABASE_ACCESS, e);
		} catch (ClassNotFoundException e) {

			log.error("Error in NoteDBUtils getNoteDBConnection -> database class not found : ", e);
			throw new NoteException(ErrorCodes.ERROR_DATABASE_CLASS_NOT_FOUND, e);
		} catch (SQLException e) {

			log.error("Error in NoteDBUtils getNoteDBConnection -> failed to initialize database connection : ", e);
			throw new NoteException(ErrorCodes.ERROR_DATABASE_CONNECTION_INITIALIZATION, e);
		}

		return connection;
	}

	public static synchronized void closeAllConnections(Connection connection, PreparedStatement preparedStatement) {

		closeConnection(connection);
		closeStatement(preparedStatement);
	}

	public static synchronized void closeAllConnections(Connection connection, PreparedStatement preparedStatement,
			ResultSet resultSet) {

		closeConnection(connection);
		closeStatement(preparedStatement);
		closeResultSet(resultSet);
	}

	public static synchronized void closeAllConnections(Connection connection, Statement statement) {

		closeConnection(connection);
		closeStatement(statement);
	}

	public static synchronized void closeAllConnections(Connection connection, Statement statement,
			ResultSet resultSet) {

		closeConnection(connection);
		closeStatement(statement);
		closeResultSet(resultSet);
	}

	private static synchronized void closeConnection(Connection dbConnection) {

		if (dbConnection != null) {

			try {

				dbConnection.close();
			} catch (Exception e) {

				log.error("Error in NoteDBUtils closeConnection -> failed to close database connection : ", e);
			}
		}
	}

	private static synchronized void closeResultSet(ResultSet resultSet) {

		if (resultSet != null) {

			try {

				resultSet.close();
			} catch (Exception e) {

				log.error("Error in NoteDBUtils closeResultSet -> failed to close resultset : ", e);
			}
		}
	}

	private static synchronized void closeStatement(PreparedStatement preparedStatement) {

		if (preparedStatement != null) {

			try {

				preparedStatement.close();
			} catch (Exception e) {

				log.error("Error in NoteDBUtils closeStatement -> failed to close prepared statement : ", e);
			}
		}
	}

	private static synchronized void closeStatement(Statement statement) {

		if (statement != null) {

			try {

				statement.close();
			} catch (Exception e) {

				log.error("Error in NoteDBUtils closeStatement -> failed to close statement : ", e);
			}
		}
	}
}
