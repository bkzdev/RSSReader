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

	//前回のRSSの先頭の内容を保存しておく
	private static String oldLink = "";

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

		//前回取得したRSSと先頭の内容が変わってなければ中止
		String newLink = rssList.get(rssList.size()-1).getLink();
		if(oldLink.equals(newLink)){
			LOG.debug("no update.");
			return;
		}

		//DB接続
		DataBase.db(rssList);

		//TODO: 新着があればすぐにswingの更新をする

		oldLink = newLink;

		for(RSS rss:rssList){
			if(rss.isNewTitle()){
				String mes = rss.getTitle();
				icon.displayMessage(rssTitle, mes, MessageType.INFO);
				LOG.info("update:" + mes);

			}
		}

		return;
	}

	public static String getOldLink(){
		return oldLink;

	}

}
