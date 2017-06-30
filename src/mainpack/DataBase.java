package mainpack;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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

			// INSERTのSQL
			String insertSql = "insert into rssTable (title, description, link, date, updatedate, num) values (?, ?, ?, ?, ?, (select max(rssTable.num)+1 from rssdb.dbo.rssTable))";
			PreparedStatement insertPstmt = conn.prepareStatement(insertSql);

			// 現在の時刻を取得
			String nowDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Calendar.getInstance().getTime());

			int i = 0;

			//前回の最終Linkまで空回りさせる
			String oldLink = TimerTask.getOldLink();
			if(!oldLink.equals("")){
				while((i < rssList.size()) && !(oldLink.equals( rssList.get(i).getLink()))){
					i++;
				}
				i++;
			}

			// RSSの数だけ回す
			while(i < rssList.size()){
				RSS rss = rssList.get(i);

				insertPstmt.setString(1, rss.getTitle());
				insertPstmt.setString(2, rss.getDescription());
				insertPstmt.setString(3, rss.getLink());
				insertPstmt.setString(4, rss.getDate());
				insertPstmt.setString(5, nowDate);

				int result = -1;
				try{
					result = insertPstmt.executeUpdate();
				}catch (SQLException e1) {
					LOG.error(e1);
				}

				LOG.debug("add:" + rss.getLink());
				LOG.debug("result:" + result);
				if(result > 0){
					rss.setNewTitle(true);
				}
				i++;
			}

			insertPstmt.close();

		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			LOG.error(e);
		}

	}
}
