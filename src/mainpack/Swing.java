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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

/**
 * https://www.javadrive.jp/tutorial/jframe/index1.html
 * https://www.javadrive.jp/tutorial/jframe/index2.html
 * https://www.javadrive.jp/tutorial/jframe/index3.html
 * https://www.javadrive.jp/tutorial/jframe/index4.html
 * https://www.javadrive.jp/tutorial/jframe/index5.html
 */
class Swing extends JFrame{

	private String[] columnNames = {"記事タイトル", "配信日時"};

	private List<Map<String, String>> rsList;

	//コンストラクタ
	Swing(String title){
		try {
			setTitle(title);

			//サイズ変更(バー含むサイズ)
			//frame.setSize(100, 80);

			//フレームの位置
			setBounds(200, 200, 400, 320);

			//バツで閉じる
			//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//java自体を終了する
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);	//フレームを隠して破棄する

			//ContentPaneの取得
			//Container contentPane = getContentPane();
			//add(temporaryLostComponent, defaultCloseOperation);

			//アイコンの指定
			setIconImage(ImageIO.read(
					getClass().getResourceAsStream("/trayicon.png")));

			//パネルの追加
			JPanel panel1 = new JPanel();
			panel1.setBackground(Color.blue);

			JPanel panel2 = new JPanel();
			panel2.setBackground(Color.orange);

			add(panel1,BorderLayout.NORTH);
			add(panel2,BorderLayout.SOUTH);

			DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

			//テーブルの設定
			JTable table = new JTable(tableModel);
			table.setDefaultEditor(Object.class, null);		//編集不可
			database(tableModel);
			//テーブルのイベント
			table.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if(e.getClickCount() == 2){		//ダブルクリック時
					// 選択行の行番号を取得します
					int row = table.getSelectedRow();
					String link = (String) rsList.get(row).get("link");
					Desktop desktop = Desktop.getDesktop();
					try {
						URI uri = new URI(link);
						desktop.browse(uri);
					} catch (URISyntaxException e1) {
						// TODO 自動生成された catch ブロック
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO 自動生成された catch ブロック
						e1.printStackTrace();
					}

					System.out.println("行" + row + "::" + "列" + link);
					}
				}
			});

			//スクロールペイン
			JScrollPane sp = new JScrollPane(table);
			sp.setPreferredSize(new Dimension(250, 70));

			JPanel panel3 = new JPanel();
			panel3.add(sp);

			add(panel3, BorderLayout.CENTER);

		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	}

	private void database(DefaultTableModel tableModel){
		Connection conn = null;
		String url = "jdbc:sqlserver://localhost\\SQLEXPRESS;database=rssdb;";
		String user = "rssuser";
		String password = "rssuser";

		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
			conn = DriverManager.getConnection(url, user, password);

			// SELECTのSQL
			// TODO: SELECTは1回にしたい
			String selectSql = "select testview.タイトル, testview.URL, testview.配信日時 from rssdb.dbo.testview order by testview.配信日時 desc";
			PreparedStatement selectPstmt = conn.prepareStatement(selectSql);
			ResultSet rs = selectPstmt.executeQuery();

			rsList = new ArrayList<Map<String, String>>();

			// Resultの数だけ回す
			while(rs.next()){

				String title = rs.getString("タイトル");
				String link = rs.getString("url");
				String date = rs.getString("配信日時");

				Map<String, String> map = new HashMap<>();
				map.put("title", title);
				map.put("link", link);
				map.put("date", date);
				rsList.add(map);

				String[] s = {title, date};
				tableModel.addRow(s);

			}

			rs.close();
			selectPstmt.close();

		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	}

	public static void main(String args[]){
		Swing frame = new Swing("タイトル");

		//表示
		frame.setVisible(true);
	}

}
