package com.wso2telco.note.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.wso2telco.note.dao.model.NoteDTO;
import com.wso2telco.note.exception.NoteException;
import com.wso2telco.note.util.NoteDBUtils;
import com.wso2telco.note.util.ErrorCodes;
import com.wso2telco.note.util.NoteTables;

public class NoteServiceDAO {

	private static final Log log = LogFactory.getLog(NoteServiceDAO.class);

	public boolean addNote(NoteDTO note) throws NoteException {

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = NoteDBUtils.getNoteDBConnection();

			StringBuilder str = new StringBuilder("INSERT INTO ");
			str.append(NoteTables.NSNOTES.getTableName());
			str.append(" (clientref, note, reslutiontypedid, description, notetypedid)");
			str.append(" VALUES ");
			str.append(" (?, ?, ?, ?, ?)");

			ps = con.prepareStatement(str.toString());

			ps.setString(1, note.getClientRef());
			ps.setString(2, note.getNote());
			ps.setInt(3, Integer.parseInt(note.getReslutionTypeDid()));
			ps.setString(4, note.getDescription());
			ps.setInt(5, Integer.parseInt(note.getNoteTypeDid()));

			log.debug("NoteServiceDAO add note -> query : " + ps);

			ps.execute();
		} catch (SQLException e) {

			log.error("Error in NoteServiceDAO addNote -> failed to insert note : ", e);
			throw new NoteException(ErrorCodes.ERROR_DATABASE_OPERATION, e);
		} catch (NoteException e) {

			throw e;
		} finally {

			try {

				NoteDBUtils.closeAllConnections(con, ps);
			} catch (Exception e) {

				log.error("Error in NoteServiceDAO addNote -> failed to close database connection : ", e);
			}
		}

		return true;
	}

	public NoteDTO getNote(String clientRef, int noteTypeDid) throws NoteException {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		NoteDTO noteDTO = null;

		try {

			con = NoteDBUtils.getNoteDBConnection();

			StringBuilder str = new StringBuilder("SELECT * FROM ");
			str.append(NoteTables.NSNOTES.getTableName());
			str.append(" WHERE ");
			str.append(" clientref = ? and notetypedid = ?");

			ps = con.prepareStatement(str.toString());

			ps.setString(1, clientRef);
			ps.setInt(2, noteTypeDid);

			log.debug("NoteServiceDAO get note -> query : " + ps);

			rs = ps.executeQuery();

			while (rs.next()) {

				noteDTO = new NoteDTO();
				noteDTO.setNoteDid(Integer.toString(rs.getInt("notedid")));
				noteDTO.setClientRef(rs.getString("clientref"));
				noteDTO.setNote(rs.getString("note"));
				noteDTO.setReslutionTypeDid(Integer.toString(rs.getInt("reslutiontypedid")));
				noteDTO.setDescription(rs.getString("description"));
				noteDTO.setNoteTypeDid(Integer.toString(rs.getInt("notetypedid")));
			}
		} catch (SQLException e) {

			log.error("Error in NoteServiceDAO getNote -> failed to retrieve note : ", e);
			throw new NoteException(ErrorCodes.ERROR_DATABASE_OPERATION, e);
		} catch (NoteException e) {

			throw e;
		} finally {

			try {

				NoteDBUtils.closeAllConnections(con, ps, rs);
			} catch (Exception e) {

				log.error("Error in NoteServiceDAO getNote -> failed to retrieve note : ", e);
			}
		}

		return noteDTO;
	}
}
