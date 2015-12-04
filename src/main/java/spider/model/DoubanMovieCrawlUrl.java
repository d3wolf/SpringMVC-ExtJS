package spider.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class DoubanMovieCrawlUrl extends BaseCrawlUrl {
	@Column
	private String coverUrl;
	@Column
	private Float rate;

	public static DoubanMovieCrawlUrl newDoubanMovieCrawlUrl(String url, String dataId, String title, String coverUrl, Float rate){
		DoubanMovieCrawlUrl urlInfo = new DoubanMovieCrawlUrl();
		urlInfo.setUrl(url);
		urlInfo.setDataId(dataId);
		urlInfo.setTitle(title);
		urlInfo.setCoverUrl(coverUrl);
		urlInfo.setRate(rate);
		
		return urlInfo;
	}
	
	public String getCoverUrl() {
		return coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public Float getRate() {
		return rate;
	}

	public void setRate(Float rate) {
		this.rate = rate;
	}

}
