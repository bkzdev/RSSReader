package mainpack;

/**
 * RSSクラス
 */
public class RSS {

	private String title;
	private String description;
	private String link;
	private String date;
	private boolean newTitle = false;

	/**
	 * @param title
	 * @param description
	 * @param link
	 * @param date
	 */
	public RSS(String title, String description, String link, String date){
		this.title = title;
		this.description = description;
		this.link = link;
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getLink() {
		return link;
	}

	public String getDate() {
		return date;
	}

	public boolean isNewTitle() {
		return newTitle;
	}

	public void setNewTitle(boolean newTitle) {
		this.newTitle = newTitle;
	}

}
