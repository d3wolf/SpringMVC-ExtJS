package spider.service.impl;

import spider.model.FetchedPage;


public class ContentHandler {
	/**
	 * 如果能正常爬取-true
	 * @param fetchedPage
	 * @return
	 */
	public boolean check(FetchedPage fetchedPage){
		if(isAntiScratch(fetchedPage)){
			return false;
		}
		
		return true;
	}
	
	private boolean isStatusValid(int statusCode){
		if(statusCode >= 200 && statusCode < 400){
			return true;
		}
		return false;
	}
	
	private boolean isAntiScratch(FetchedPage fetchedPage){
		// 403 forbidden
		if((!isStatusValid(fetchedPage.getStatusCode())) && fetchedPage.getStatusCode() == 403){
			return true;
		}
		
		// 页面内容包含的反爬取内容
		if(fetchedPage.getContent().toString().contains("<div>禁止访问</div>")){
			return true;
		}
		
		return false;
	}
}
