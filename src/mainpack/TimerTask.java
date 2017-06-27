package mainpack;

import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TimerTask extends java.util.TimerTask {

	private static final String RSS_URL= "http://www.4gamer.net/rss/index.xml";

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
		RSSRead rssRead = new RSSRead(RSS_URL);

		//RSS読み込み
		String rssTitle = rssRead.getTitle();
		ArrayList<RSS> rssList = rssRead.getRssList();

		//DB接続
		DataBase.db(rssList);

		//新着表示
		boolean flag = true;

		for(RSS rss:rssList){
			if(rss.isNewTitle()){
				flag = false;
				String mes = rss.getTitle();
				icon.displayMessage(rssTitle, mes, MessageType.INFO);
				LOG.info("update:" + mes);

			}
		}

		if(flag){
//			icon.displayMessage("タイマー", "no update.", MessageType.INFO);

			LOG.debug("no update.");
		}

	}

}
