package mainpack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * https://www.javadrive.jp/tutorial/jframe/index1.html
 * https://www.javadrive.jp/tutorial/jframe/index2.html
 * https://www.javadrive.jp/tutorial/jframe/index3.html
 * https://www.javadrive.jp/tutorial/jframe/index4.html
 * https://www.javadrive.jp/tutorial/jframe/index5.html
 */
class Swing extends JFrame{
	//ログ
	private static final Logger LOG = LogManager.getLogger(Swing.class);

	private final int FRAME_WIDTH = 800;
	private final int FRAME_HEIGHT = 600;
	private final int SCROLL_WIDTH = FRAME_WIDTH - 50;
	private final int SCROLL_HEIGHT = FRAME_HEIGHT - 100;
	private final String COLUMN_NUM = "記事番号";
	private final String COLUMN_TITLE = "記事タイトル";
	private final String COLUMN_DATE = "配信日時";

	private Map<String, String> linkMap;

	private DefaultTableModel tableModel;

	private int lastNum = 0;

	private String[] columnNames = {COLUMN_NUM, COLUMN_TITLE, COLUMN_DATE};

	//コンストラクタ
	//TODO: 幅とか調整する
	Swing(String title){
		try {
			setTitle(title);

			//フレームの位置とサイズ
			setBounds(200, 200, FRAME_WIDTH, FRAME_HEIGHT);

			//バツでフレームを隠して破棄する
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

			//アイコンの指定
			setIconImage(ImageIO.read(
					getClass().getResourceAsStream("/trayicon.png")));

			//謎の帯パネル
			JPanel panel1 = new JPanel();
			panel1.setBackground(Color.blue);

			JPanel panel2 = new JPanel();
			panel2.setBackground(Color.orange);

			add(panel1,BorderLayout.NORTH);
			add(panel2,BorderLayout.SOUTH);

			linkMap = new HashMap<>();

			//テーブルの作成
			tableModel = new DefaultTableModel(columnNames, 0);
			JTable table = new JTable(tableModel);

			//テーブルの設定
			table.setDefaultEditor(Object.class, null);		//編集不可
//			table.setAutoCreateRowSorter(true);					// TODO: 新着を一番上に表示したい
			database();	//初期値を取得

			//テーブルのイベント
			table.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if(e.getClickCount() == 2){		//ダブルクリック時
						// 選択行の行番号を取得します
						String title = (String) table.getValueAt(table.getSelectedRow(), 1);

						String link = (String) linkMap.get(title);
						Desktop desktop = Desktop.getDesktop();

						try {
							URI uri = new URI(link);
							desktop.browse(uri);
						} catch (IOException | URISyntaxException e1) {
							LOG.error(e1);
						}

						LOG.info("clicked:" + link);
					}
				}
			});

			//スクロールペイン
			JScrollPane sp = new JScrollPane(table);
			sp.setPreferredSize(new Dimension(SCROLL_WIDTH, SCROLL_HEIGHT));

			JPanel panel3 = new JPanel();
			panel3.add(sp);

			this.add(panel3, BorderLayout.CENTER);

		} catch (IOException e) {
			LOG.error(e);
		}

	}

	public void database(){
		Connection conn = null;
		String url = "jdbc:sqlserver://localhost\\SQLEXPRESS;database=rssdb;";
		String user = "rssuser";
		String password = "rssuser";

		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
			conn = DriverManager.getConnection(url, user, password);

			// SELECTのSQL
			String selectSql = "select top (100) rssTable.num, rssTable.title, rssTable.link, rssTable.date from rssdb.dbo.rssTable where num >= ? order by rssTable.num desc;";
			PreparedStatement selectPstmt = conn.prepareStatement(selectSql);
			selectPstmt.setInt(1, lastNum + 1);
			ResultSet rs = selectPstmt.executeQuery();

			//テーブル全行削除
//			tableModel.setRowCount(0);
			rs.next();
			lastNum = rs.getInt("num");

			// Resultの数だけ回す
			do{
				String num   = rs.getString("num");
				String title = rs.getString("title");
				String link  = rs.getString("link");
				String date  = rs.getString("date");

				//タイトルからリンクを取得できるよう登録
				linkMap.put(title, link);

				//1行追加
				String[] s = {num, title, date};
				tableModel.addRow(s);

			} while(rs.next());

			rs.close();
			selectPstmt.close();

		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			LOG.error(e);
		}

	}

	public static void main(String args[]){
		Swing frame = new Swing("タイトル");

		//表示
		frame.setVisible(true);
	}

}
