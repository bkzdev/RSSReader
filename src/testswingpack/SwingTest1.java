package testswingpack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * https://www.javadrive.jp/tutorial/jframe/index1.html
 * https://www.javadrive.jp/tutorial/jframe/index2.html
 * https://www.javadrive.jp/tutorial/jframe/index3.html
 * https://www.javadrive.jp/tutorial/jframe/index4.html
 * https://www.javadrive.jp/tutorial/jframe/index5.html
 */
class SwingTest1 extends JFrame{

	private String[][] tabledata = {
			{"1-1","1-2","1-3","1-4"},
			{"2-1","2-2","2-3","2-4"},
			{"3-1","3-2","3-3","3-4"},
			{"4-1","4-2","4-3","4-4"}
	};

	private String[] columnNames = {"A", "B", "C", "D"};

	//コンストラクタ
	SwingTest1(String title){
		try {
			setTitle(title);

			//サイズ変更(バー含むサイズ)
			//frame.setSize(100, 80);

			//フレームの位置
			setBounds(200, 200, 400, 320);

			//バツで閉じる
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

			JTable table = new JTable(tabledata,columnNames);
			JScrollPane sp = new JScrollPane(table);
			sp.setPreferredSize(new Dimension(250, 70));

			JPanel panel3 = new JPanel();
			panel3.add(sp);

			add(panel3, BorderLayout.CENTER);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String args[]){
		SwingTest1 frame = new SwingTest1("タイトル");

		//表示
		frame.setVisible(true);
	}

}
