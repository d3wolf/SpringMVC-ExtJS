package wechat.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import spider.model.Joke;
import spider.service.JokeService;
import wechat.model.WXUser;
import wechat.model.WXUserMsgIndex;
import wechat.service.WXCommonService;
import common.dao.BaseDao;

@Service
@Transactional(readOnly = false,propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class WXCommonServiceImpl implements WXCommonService {
	
	@Autowired
	@Qualifier("baseDaoImpl")
	private BaseDao<WXUser> userDao;
	
	@Autowired
	@Qualifier("baseDaoImpl")
	private BaseDao<WXUserMsgIndex> indexDao;

	public WXUser getOrCreateWXUser(String name) {
		WXUser user = null;
		
		List<WXUser> users = userDao.find("from WXUser where name=?", new Object[]{name});
		if(users != null && users.size() > 0){
			user = users.get(0);
		}else{
			user = WXUser.newWXUser(name);
			userDao.save(user);
		}
		return user;
	}

	public WXUserMsgIndex getOrCreateUserMsgIndex(WXUser user) {
		WXUserMsgIndex index = null;
		
		List<WXUserMsgIndex> indexes = indexDao.find("from WXUserMsgIndex where user=?", new Object[]{user});
		if(indexes != null && indexes.size() > 0){
			index = indexes.get(0);
		}else{
			index = WXUserMsgIndex.newWXUserMsgIndex(user);
			indexDao.save(index);
		}
		return index;
	}

	public void updateUserMsgIndex(WXUserMsgIndex msgIndex) {
		indexDao.update(msgIndex);
	}

	@Autowired
	private JokeService jokeService;
	
	public String getNextJokeByUserName(String name) {
		WXUser user = getOrCreateWXUser(name);
		WXUserMsgIndex msgIndex = getOrCreateUserMsgIndex(user);
		Joke joke = jokeService.getNextJoke(msgIndex.getJoke());
		msgIndex.setJoke(joke);
		updateUserMsgIndex(msgIndex);
		String jokeMsg = String.format("[%s][%s]%s", joke.getId(), joke.getTitle(), joke.getText());
		
		return jokeMsg;
	}

}
