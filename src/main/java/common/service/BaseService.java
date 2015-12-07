package common.service;

import common.BaseException;


public interface BaseService {
	
	/**
	 * 根据oid(&ltClassName>:&ltid>, e.g. common.model.BaseObject:1)获取对象
	 * 
	 * @param oid
	 * @return
	 * @throws BaseException
	 */
	public Object getObjectByOid(String oid) throws BaseException;
}
