package spider.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import common.model.BaseObject;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Joke extends BaseObject {

	@Column
	private Integer dataId;
	@Column(columnDefinition="TEXT")
	private String text;
	@Column
	private String title;
	
	public static Joke newJoke(Integer dataId, String title, String text){
		Joke joke = new Joke();
		joke.setDataId(dataId);
		joke.setTitle(title);
		joke.setText(text);
		
		return joke;
	}

	public Integer getDataId() {
		return dataId;
	}

	public void setDataId(Integer dataId) {
		this.dataId = dataId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
