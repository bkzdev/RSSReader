package mainpack;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class RSS_Test {

	private static final String RSS_URL= "http://www.4gamer.net/rss/index.xml";

	public ArrayList<RSS> readRSS() {

		//RSSリスト作成
		ArrayList<RSS> rssList = new ArrayList<RSS>();

		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(RSS_URL);

			// ドキュメントのルートを取得
			Element root = document.getDocumentElement();

			// ルート直下の"channel"に含まれるノードリストを取得
			NodeList channel = root.getElementsByTagName("channel");

			// "channel"直下の"title"に含まれるノードリストを取得
			NodeList title = ((Element)channel.item(0)).getElementsByTagName("title");

			// とりあえず出力する
//			System.out.println("タイトル：\r\n" + title.item(0).getFirstChild().getNodeValue() + "\r\n");

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
		return rssList;

	}
	public static void main(String[] args){
		RSS_Test rss_test = new RSS_Test();
		DataBase db = new DataBase();
		ArrayList<RSS> rssList = rss_test.readRSS();
		db.db(rssList);
	}

}