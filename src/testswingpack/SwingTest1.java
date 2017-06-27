package testswingpack;

import javax.swing.JFrame;

/**
 * https://www.javadrive.jp/tutorial/jframe/index1.html
 */
class SwingTest1 extends JFrame{

	//コンストラクタ
	SwingTest1(String title){
		setTitle(title);

		//サイズ変更(バー含むサイズ)
		//frame.setSize(100, 80);

		//フレームの位置
		setBounds(200, 200, 400, 320);

		//バツで閉じる
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String args[]){
		SwingTest1 frame = new SwingTest1("タイトル");

		//表示
		frame.setVisible(true);
	}

}
