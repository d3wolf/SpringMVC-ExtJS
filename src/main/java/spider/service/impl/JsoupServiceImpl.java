package spider.service.impl;

import java.io.IOException;
import java.util.Map;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import spider.model.FetchedPage;
import spider.service.JsoupService;

@Service
public class JsoupServiceImpl implements JsoupService {
	
	public Document getDocumentFromUrl(String url) throws IOException {
		Document doc = Jsoup.connect(url).data("query", "Java") // 请求参数
				.userAgent("Mozilla") // 设置 User-Agent
				.cookie("auth", "token") // 设置 cookie
				.timeout(7000) // 设置连接超时时间
				.get(); // 使用 POST 方法访问 URL

		return doc;
	}

	public Elements getMainContentSet(Document doc, String selector) {
		Elements itemSet = doc.select(selector);
		
		return itemSet;
	}
	
	public FetchedPage fetchUrl(String url) throws IOException {
		Response response = Jsoup.connect(url)
					.ignoreContentType(true)//unhandled content type
					.userAgent("Mozilla/5.0 (Windows NT 6.2; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0")
					.timeout(15000)
					.execute();

		Document doc = response.parse();
		
		int statusCode = response.statusCode();
		Map<String, String> headers = response.headers();
		String contentType = headers.get("Content-Type");
		
		FetchedPage fetchedPage = new FetchedPage(url, doc, statusCode,contentType);
		
		return fetchedPage;
	}

}
