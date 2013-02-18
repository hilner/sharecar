package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.inject.Inject;

import br.gov.frameworkdemoiselle.lifecycle.Startup;
import br.gov.frameworkdemoiselle.transaction.Transactional;

public class DDL {

	@Inject
	private Connection connection;

	@Startup
	//@Transactional
	public void dropAndCreate() throws Exception {
		dropTableIfExists("routes");
		createTableRoutes();
	}

	private void dropTableIfExists(String tableName) throws Exception {
		PreparedStatement pstmt;

		String sql = "select (count(*) > 0) from information_schema.tables where table_name = ?; ";
		pstmt = connection.prepareStatement(sql.toString());
		pstmt.setString(1, tableName);

		ResultSet rs = pstmt.executeQuery();
		rs.next();
		boolean exists = rs.getBoolean(1);
		rs.close();
		pstmt.close();

		if (exists) {
			pstmt = connection.prepareStatement("DROP TABLE " + tableName + "; ");
			pstmt.execute();
			pstmt.close();
		}
	}

	private void createTableRoutes() throws Exception {
		StringBuffer sql = new StringBuffer();

		sql.append("CREATE TABLE routes ( ");
		sql.append("	id serial NOT NULL, ");
		sql.append("	description character varying(255) NOT NULL, ");
		sql.append("	username character varying(11) NOT NULL, ");
		sql.append("	geom geometry NOT NULL, ");
		sql.append("CONSTRAINT routes_pk PRIMARY KEY (id) ");
		sql.append("); ");

		PreparedStatement pstmt = connection.prepareStatement(sql.toString());
		pstmt.execute();
		pstmt.close();
	}
}
