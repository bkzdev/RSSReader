package testswingpack;

import javax.swing.JFrame;

/**
 * https://www.javadrive.jp/tutorial/jframe/index1.html
 */
public class SwingTest1 {
	public static void main(String args[]){
		JFrame frame = new JFrame("タイトル");

		//表示
		frame.setVisible(true);

		//サイズ変更(バー含むサイズ)
		frame.setSize(100, 80);

		//フレームの位置
		frame.setBounds(200, 200, 100, 80);

		//ウインドウの位置(サイズ決めてから使う)
		frame.setLocationRelativeTo(null);		//nullで画面中央

		//バツで閉じる
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
