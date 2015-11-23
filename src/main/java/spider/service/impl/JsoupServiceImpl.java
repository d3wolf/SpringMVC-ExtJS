package spider.service.impl;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import spider.service.JsoupService;

@Service
public class JsoupServiceImpl implements JsoupService {

	public Document getDocumentFromUrl(String url) throws IOException{
		 Document doc = Jsoup.connect(url) 
				  .data("query", "Java")   // 请求参数
				  .userAgent("Mozilla") // 设置 User-Agent 
				  .cookie("auth", "token") // 设置 cookie 
				  .timeout(7000)           // 设置连接超时时间
				  .get();                 // 使用 POST 方法访问 URL 
	
		 return doc;
	}
	
	public Elements getMainContentSet(Document doc, String selector){
		Elements itemSet = doc.select(selector);
		
		return itemSet;
	}
}
