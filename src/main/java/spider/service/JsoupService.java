package spider.service;

import java.io.IOException;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import spider.model.FetchedPage;

public interface JsoupService {
	/**
	 * 根据url得到整个页面doc
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public Document getDocumentFromUrl(String url) throws IOException;
	
	/**
	 * 获取待爬行页面信息
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public FetchedPage fetchUrl(String url) throws IOException;
	
	/**
	 * 根据整个页面doc获取到主要内容的set,这里认为主内容set是一个集合，根据selector获取
	 * @param doc
	 * @param selector
	 * @return
	 */
	public Elements getMainContentSet(Document doc, String selector);
}
