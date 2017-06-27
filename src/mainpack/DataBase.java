package mainpack;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataBase {

	//ログ
	private static final Logger LOG = LogManager.getLogger(DataBase.class);

	public static void db(ArrayList<RSS> rssList){
		Connection conn = null;
		String url = "jdbc:sqlserver://localhost\\SQLEXPRESS;database=rssdb;";
		String user = "rssuser";
		String password = "rssuser";

		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
			conn = DriverManager.getConnection(url, user, password);

			// SELECTのSQL
			// TODO: SELECTは1回にしたい
			String selectSql = "select link from rssTable where link = ?";
			PreparedStatement selectPstmt = conn.prepareStatement(selectSql);

			// INSERTのSQL
			String insertSql = "insert into rssTable (title, description, link, date, updatedate) values (?, ?, ?, ?, ?)";
			PreparedStatement insertPstmt = conn.prepareStatement(insertSql);

			// 現在の時刻を取得
			String nowDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Calendar.getInstance().getTime());

			// RSSの数だけ回す
			for(RSS rss : rssList){
				selectPstmt.setString(1, rss.getLink());
				ResultSet rs = selectPstmt.executeQuery();

				if(!rs.next()){
					insertPstmt.setString(1, rss.getTitle());
					insertPstmt.setString(2, rss.getDescription());
					insertPstmt.setString(3, rss.getLink());
					insertPstmt.setString(4, rss.getDate());
					insertPstmt.setString(5, nowDate);

					insertPstmt.executeUpdate();

					LOG.debug("add:" + rss.getLink());

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
