package mainpack;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Timer;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * タスクトレイ常駐アプリサンプル
 */
public class App {
	//ログ
	private static final Logger LOG = LogManager.getLogger(App.class);

	/** コンストラクタ */
	public App() throws IOException, AWTException {
		LOG.info("App startup start.");
		Swing frame = new Swing("タイトル");

		// タスクトレイアイコン
		Image image = ImageIO.read(
				getClass().getResourceAsStream("/trayicon.png"));
		final TrayIcon icon = new TrayIcon(image);
		icon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(true);
			}
		});


		// ポップアップメニュー
		PopupMenu menu = new PopupMenu();
		// メニューの例
		MenuItem aItem = new MenuItem("メニューの例");
		aItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(true);
			}
		});
		// 終了メニュー
		MenuItem exitItem = new MenuItem("終了");
		exitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		// メニューにメニューアイテムを追加
		menu.add(aItem);
		menu.add(exitItem);
		icon.setPopupMenu(menu);

		// タスクトレイに格納
		SystemTray.getSystemTray().add(icon);

		// タイマータスク作成
		TimerTask task = new TimerTask(icon);

		// タイマー起動
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(task, 0, 1 * 60 * 1000); // 1分ごと

		LOG.info("App startup fin.");
	}

	/** メインメソッド */
	public static void main(String[] args) throws Exception {
		new App();
	}
}