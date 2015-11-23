package demo.service;


/**
 * 省份Province-城市City-地区District匹配关系
 * @author Wolf
 *
 */
public interface PCDMappingService {

	public Object getAllProvinces() throws Exception;
	
	public Object getCitiesByProvince(String pid)throws Exception;
	
	public Object getDistrictsByCity(String cid)throws Exception;
}
