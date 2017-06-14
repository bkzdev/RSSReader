package mainpack;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class RSSRead {

	//ログ
	private static final Logger LOG = LogManager.getLogger(RSSRead.class);

	private ArrayList<RSS> rssList = null;;
	private String rssUrl  = null;
	private String title = null;

	public RSSRead(String rssUrl) {

		this.rssUrl = rssUrl;
		rssList = new ArrayList<RSS>();

		try {

			// RSSを読み込む
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(this.rssUrl);

			// ドキュメントのルートを取得
			Element root = document.getDocumentElement();

			// ルート直下の"channel"に含まれるノードリストを取得
			NodeList channel = root.getElementsByTagName("channel");

			// "channel"直下の"title"に含まれるノードリストを取得
			title = ((Element)channel.item(0)).getElementsByTagName("title").item(0).getFirstChild().getNodeValue();
			LOG.info("[" + title + "]読込開始");

			// 各"item"とその中の"title"と"description"を取得する。
			NodeList item_list = root.getElementsByTagName("item");

			// item分ループする
			for(int i = 0; i < item_list.getLength(); i++) {

				Element element = (Element)item_list.item(i);

				// title を取得する
				String item_title        = element.getElementsByTagName("title").item(0).getFirstChild().getNodeValue();
				// description を取得する
				String item_description = element.getElementsByTagName("description").item(0).getFirstChild().getNodeValue();
				// link を取得する
				String item_link         = element.getElementsByTagName("link").item(0).getFirstChild().getNodeValue();
				// date を取得する
				String item_date         = element.getElementsByTagName("dc:date").item(0).getFirstChild().getNodeValue();

				//リストに追加する
				rssList.add(new RSS(item_title, item_description, item_link, item_date));

			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<RSS> getRssList() {
		return rssList;
	}

	public String getTitle() {
		return title;
	}

}