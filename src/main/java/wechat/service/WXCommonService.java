package wechat.service;

import wechat.model.WXUser;
import wechat.model.WXUserMsgIndex;

public interface WXCommonService {

	/**
	 * 获取或保存用户
	 * 
	 * @param name
	 * @return
	 */
	public WXUser getOrCreateWXUser(String name);

	/**
	 * 获取消息索引
	 * 
	 * @param user
	 * @return
	 */
	public WXUserMsgIndex getOrCreateUserMsgIndex(WXUser user);

	/**
	 * 更新消息索引
	 * 
	 * @param msgIndex
	 * @return
	 */
	public void updateUserMsgIndex(WXUserMsgIndex msgIndex);
	
	/**
	 * 获取用户的下一个Joke
	 * @param name
	 * @return
	 */
	public String getNextJokeByUserName(String name);

}
