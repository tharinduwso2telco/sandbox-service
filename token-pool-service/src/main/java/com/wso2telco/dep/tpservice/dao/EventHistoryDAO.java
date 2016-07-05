package com.wso2telco.dep.tpservice.dao;

import java.sql.SQLException;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wso2telco.dep.tpservice.model.TokenDTO;
import com.wso2telco.dep.tpservice.util.Constants;
import com.wso2telco.dep.tpservice.util.Constants.Tables;
import com.wso2telco.dep.tpservice.util.Event;
import com.wso2telco.dep.tpservice.util.Status;

public class EventHistoryDAO {

	private static Logger log = LoggerFactory.getLogger(EventHistoryDAO.class);

	// event related DAO layer for token invalidate
	public void invalidateToken(TokenDTO tokenDto) throws SQLException {

		DBI dbi = JDBIUtil.getInstance();
		Handle h = dbi.open();

		try {
			// for single transaction with TokenDAO
			h.getConnection().setAutoCommit(false);
			h.begin();

			StringBuilder sql_event = new StringBuilder();

			sql_event.append(" INSERT ");
			sql_event.append(" INTO ").append(Tables.TABLE_TSTEVENT.toString());
			sql_event.append(" ( jobname , text, event , status ) ");
			sql_event.append("VALUES ");
			sql_event.append("(? , ? , ? , ?)");

			h.execute(sql_event.toString(), Constants.CONTEXT_TOKEN, "TokenID "+tokenDto.getId(), Status.INVALIDATE_SUCCESS, Event.INVALIDATE_TOKEN);

			// for invalidating Token through TokenDAO layer
			TokenDAO tokenObj = new TokenDAO();
			tokenObj.invalidatingToken( tokenDto);

			// When both TokenDAO & EventDAO executed without error
			h.commit();
			log.debug("Token Invalidation Success for the token id "+ tokenDto.getId());

		} catch (Exception e) {

			h.rollback();
			log.error("EventHistoryDAO", "invalidateToken()", e);
			throw new SQLException("Could not invalidate token");

		} finally {
			h.close();
		}

	}

}
