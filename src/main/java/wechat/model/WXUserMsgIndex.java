package wechat.model;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import common.model.BaseObject;
import spider.model.Joke;

/**
 * 微信用户消息索引
 * @author Wolf
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class WXUserMsgIndex extends BaseObject {

	@ManyToOne(targetEntity = WXUser.class)
	@JoinColumn
	private WXUser user;

	@ManyToOne(targetEntity = Joke.class)
	@JoinColumn
	private Joke joke;
	
	public static WXUserMsgIndex newWXUserMsgIndex(WXUser user){
		WXUserMsgIndex index = new WXUserMsgIndex();
		index.setUser(user);
		
		return index;
	}

	public WXUser getUser() {
		return user;
	}

	public void setUser(WXUser user) {
		this.user = user;
	}

	public Joke getJoke() {
		return joke;
	}

	public void setJoke(Joke joke) {
		this.joke = joke;
	}
}
