package wechat.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import wechat.model.WXUser;

public interface WXUserDao  extends CrudRepository<WXUser, Integer>{

	public List<WXUser> getWXUserByName(String name);
}
