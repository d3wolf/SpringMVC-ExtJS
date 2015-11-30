package wechat.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import spider.model.Joke;
import spider.service.JokeService;
import wechat.dao.WXUserDao;
import wechat.dao.WXUserMsgIndexDao;
import wechat.model.WXUser;
import wechat.model.WXUserMsgIndex;
import wechat.service.WXCommonService;

@Service
@Transactional(readOnly = false,propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class WXCommonServiceImpl implements WXCommonService {
	
	@Autowired
	private WXUserDao userJpaDao;
	
	@Autowired
	private WXUserMsgIndexDao indexJpaDao;

	public WXUser getOrCreateWXUser(String name) {
		WXUser user = null;
		
		List<WXUser> users = userJpaDao.getWXUserByName(name);
		if(users != null && users.size() > 0){
			user = users.get(0);
		}else{
			user = WXUser.newWXUser(name);
			userJpaDao.save(user);
		}
		return user;
	}

	public WXUserMsgIndex getOrCreateUserMsgIndex(WXUser user) {
		WXUserMsgIndex index = null;
		
		List<WXUserMsgIndex> indexes = indexJpaDao.getWXUserMsgIndexByUser(user);
		if(indexes != null && indexes.size() > 0){
			index = indexes.get(0);
		}else{
			index = WXUserMsgIndex.newWXUserMsgIndex(user);
			indexJpaDao.save(index);
		}
		return index;
	}


	@Autowired
	private JokeService jokeService;
	
	public String getNextJokeByUserName(String name) {
		WXUser user = getOrCreateWXUser(name);
		WXUserMsgIndex msgIndex = getOrCreateUserMsgIndex(user);
		Joke joke = jokeService.getNextJoke(msgIndex.getJoke());
		msgIndex.setJoke(joke);
		indexJpaDao.save(msgIndex);
		String jokeMsg = String.format("[%s][%s]%s", joke.getId(), joke.getTitle(), joke.getText());
		
		return jokeMsg;
	}

}
