package mainpack;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataBase {

	//ログ
	private static final Logger LOG = LogManager.getLogger(DataBase.class);

	public void db(ArrayList<RSS> rssList){
		Connection conn = null;
		String url = "jdbc:sqlserver://localhost\\SQLEXPRESS;database=rssdb;";
		String user = "sa";
		String password = "sdd@0913";

		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
			conn = DriverManager.getConnection(url, user, password);

			String selectSql = "select link from rsstable where link = ?";
			String insertSql = "insert into rsstable (title, description, link, date) values (?, ?, ?, ?)";
			PreparedStatement selectPstmt = conn.prepareStatement(selectSql);
			PreparedStatement insertPstmt = conn.prepareStatement(insertSql);

			//SQLの数だけ回す
			for(RSS rss : rssList){
				selectPstmt.setString(1, rss.getLink());
				ResultSet rs = selectPstmt.executeQuery();

				if(!rs.next()){
					insertPstmt.setString(1, rss.getTitle());
					insertPstmt.setString(2, rss.getDescription());
					insertPstmt.setString(3, rss.getLink());
					insertPstmt.setString(4, rss.getDate());

					insertPstmt.executeUpdate();

					LOG.info("add:" + rss.getLink());

					rss.setNewTitle(true);
				}
				rs.close();
			}
			selectPstmt.close();
			insertPstmt.close();

		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	}
}
