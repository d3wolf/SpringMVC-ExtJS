package spider.model;

import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import common.model.BaseObject;

/**
 * 基本的待爬行的url信息,只有url，title和页面id信息<br>
 * 该类并不是一个实体，待爬行url可能有更多信息，需要继承该类并扩展
 * 
 */
@Inheritance(strategy = InheritanceType.JOINED)
@MappedSuperclass
public abstract class BaseCrawlUrl extends BaseObject {
	@Column
	private String url;
	@Column
	private String dataId;
	@Column
	private String title;
	@Column
	private Boolean crawled = false;
	@Column
	private Boolean deleteAfterCrawled = false;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Boolean getCrawled() {
		return crawled;
	}

	public void setCrawled(Boolean crawled) {
		this.crawled = crawled;
	}

	public Boolean getDeleteAfterCrawled() {
		return deleteAfterCrawled;
	}

	public void setDeleteAfterCrawled(Boolean deleteAfterCrawled) {
		this.deleteAfterCrawled = deleteAfterCrawled;
	}

}
