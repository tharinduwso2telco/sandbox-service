package com.wso2telco.note.service.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DBUtils {

	private static final Log log = LogFactory.getLog(DBUtils.class);
	private static Connection connection;

	public static synchronized Connection getNoteDBConnection() {

		try {
			
			if (connection == null || connection.isClosed()) {

				Class.forName(ConfigurationLoader.getDbDriver()).newInstance();
				String connection_url = ConfigurationLoader.getDbConnectionURL() + "" + ConfigurationLoader.getDbHost() + ":" + ConfigurationLoader.getDbPort() + "/"
						+ ConfigurationLoader.getDbName() + "?autoReconnect=true";
				
				log.debug("DBUtils getNoteDBConnection -> connection_url : " + connection_url);

				connection = DriverManager.getConnection(connection_url, ConfigurationLoader.getDbUsername(), ConfigurationLoader.getDbPassword());
			}
		} catch (Exception e) {

			System.out.println("getNoteDBConnection : " + e);
			log.error("Error in DBUtils getNoteDBConnection : " + e.getMessage());
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
			} catch (SQLException e) {

				log.error("Error in DBUtils closeConnection : " + e.getMessage());
			}
		}
	}

	private static synchronized void closeResultSet(ResultSet resultSet) {

		if (resultSet != null) {

			try {

				resultSet.close();
			} catch (SQLException e) {

				log.error("Error in DBUtils closeResultSet : " + e.getMessage());
			}
		}
	}

	private static synchronized void closeStatement(PreparedStatement preparedStatement) {

		if (preparedStatement != null) {

			try {

				preparedStatement.close();
			} catch (SQLException e) {

				log.error("Error in DBUtils closeStatement : " + e.getMessage());
			}
		}
	}

	private static synchronized void closeStatement(Statement statement) {

		if (statement != null) {

			try {

				statement.close();
			} catch (SQLException e) {

				log.error("Error in DBUtils closeStatement : " + e.getMessage());
			}
		}
	}
}
