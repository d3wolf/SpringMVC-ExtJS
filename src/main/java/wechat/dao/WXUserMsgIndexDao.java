package wechat.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import wechat.model.WXUser;
import wechat.model.WXUserMsgIndex;

public interface WXUserMsgIndexDao extends CrudRepository<WXUserMsgIndex, Integer>{

	public List<WXUserMsgIndex> getWXUserMsgIndexByUser(WXUser user);
}
