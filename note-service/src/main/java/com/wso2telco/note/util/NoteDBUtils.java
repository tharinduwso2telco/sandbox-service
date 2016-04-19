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

			log.error("Error in NoteDBUtils getNoteDBConnection -> failed to instantiate database connection : "
					+ e.getMessage());
			throw new NoteException(ErrorCodes.ERROR_DATABASE_CONNECTION_INSTANTIATION, e);
		} catch (IllegalAccessException e) {

			log.error("Error in NoteDBUtils getNoteDBConnection -> illegal access of database : " + e.getMessage());
			throw new NoteException(ErrorCodes.ERROR_DATABASE_ACCESS, e);
		} catch (ClassNotFoundException e) {

			log.error("Error in NoteDBUtils getNoteDBConnection -> database class not found : " + e.getMessage());
			throw new NoteException(ErrorCodes.ERROR_DATABASE_CLASS_NOT_FOUND, e);
		} catch (SQLException e) {

			log.error("Error in NoteDBUtils getNoteDBConnection -> failed to initialize database connection : "
					+ e.getMessage());
			throw new NoteException(ErrorCodes.ERROR_DATABASE_CONNECTION_INITIALIZATION, e);
		}

		return connection;
	}

	public static synchronized void closeAllConnections(Connection connection, PreparedStatement preparedStatement)
			throws NoteException {

		try {

			closeConnection(connection);
			closeStatement(preparedStatement);
		} catch (NoteException e) {

			throw e;
		}
	}

	public static synchronized void closeAllConnections(Connection connection, PreparedStatement preparedStatement,
			ResultSet resultSet) throws NoteException {

		try {
			closeConnection(connection);
			closeStatement(preparedStatement);
			closeResultSet(resultSet);
		} catch (NoteException e) {

			throw e;
		}
	}

	public static synchronized void closeAllConnections(Connection connection, Statement statement)
			throws NoteException {

		try {

			closeConnection(connection);
			closeStatement(statement);
		} catch (NoteException e) {

			throw e;
		}
	}

	public static synchronized void closeAllConnections(Connection connection, Statement statement, ResultSet resultSet)
			throws NoteException {

		try {

			closeConnection(connection);
			closeStatement(statement);
			closeResultSet(resultSet);
		} catch (NoteException e) {

			throw e;
		}
	}

	private static synchronized void closeConnection(Connection dbConnection) throws NoteException {

		if (dbConnection != null) {

			try {

				dbConnection.close();
			} catch (SQLException e) {

				log.error("Error in NoteDBUtils closeConnection -> failed to close database connection : "
						+ e.getMessage());
				throw new NoteException(ErrorCodes.ERROR_DATABASE_CONNECTION_CLOSE, e);
			}
		}
	}

	private static synchronized void closeResultSet(ResultSet resultSet) throws NoteException {

		if (resultSet != null) {

			try {

				resultSet.close();
			} catch (SQLException e) {

				log.error("Error in NoteDBUtils closeResultSet -> failed to close resultset : " + e.getMessage());
				throw new NoteException(ErrorCodes.ERROR_RESULTSET_CLOSE, e);
			}
		}
	}

	private static synchronized void closeStatement(PreparedStatement preparedStatement) throws NoteException {

		if (preparedStatement != null) {

			try {

				preparedStatement.close();
			} catch (SQLException e) {

				log.error("Error in NoteDBUtils closeStatement -> failed to close prepared statement : "
						+ e.getMessage());
				throw new NoteException(ErrorCodes.ERROR_STATEMENT_CLOSE, e);
			}
		}
	}

	private static synchronized void closeStatement(Statement statement) throws NoteException {

		if (statement != null) {

			try {

				statement.close();
			} catch (SQLException e) {

				log.error("Error in NoteDBUtils closeStatement -> failed to close statement : " + e.getMessage());
				throw new NoteException(ErrorCodes.ERROR_STATEMENT_CLOSE, e);
			}
		}
	}
}
