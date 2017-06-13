package mainpack;

import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TimerTask extends java.util.TimerTask {

	//ログ
	private static final Logger LOG = LogManager.getLogger(TimerTask.class);

	private TrayIcon icon;

	@SuppressWarnings("unused")
	private TimerTask(){

	}

	public TimerTask(TrayIcon icon){
		this.icon = icon;
	}

	@Override
	public void run() {
		RSS_Test rss_test = new RSS_Test();
		DataBase db = new DataBase();

		//RSS読み込み
		ArrayList<RSS> rssList = rss_test.readRSS();

		//DB接続
		db.db(rssList);

		//新着表示
		String mes = "";
		boolean flag = true;

		for(RSS rss:rssList){
			if(rss.isNewTitle()){
				flag = false;
				mes = rss.getTitle();
				icon.displayMessage("タイマー",
						mes, MessageType.INFO);
				LOG.info("update:" + mes);

			}
		}

		if(flag){
			LOG.info("no update.");
		}

	}

}
