package spider.model;

import org.jsoup.nodes.Document;

public class FetchedPage {
	private String url;
	private Document document;
	private int statusCode;
	private String contentType;// 记录content type

	public FetchedPage() {

	}

	public FetchedPage(String url, Document content, int statusCode, String contentType) {
		this.url = url;
		this.document = content;
		this.statusCode = statusCode;
		this.contentType = contentType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Document getContent() {
		return document;
	}

	public void setContent(Document content) {
		this.document = content;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

}
