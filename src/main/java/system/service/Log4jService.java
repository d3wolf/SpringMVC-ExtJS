package system.service;

import java.util.List;

import org.apache.log4j.Level;

public interface Log4jService {
	
	/**
	 * 获取当前所有的logger名字
	 * @throws ClassNotFoundException
	 */
	public List<String> getCurrentLoggerNames() ;
	
	/**
	 * 获取目标level
	 * @param target
	 * @return
	 */
	public Level getLevelByTarget(String target);
	
	/**
	 * 获取目标level字符串
	 * @param target
	 * @return
	 */
	public String getLevelStrByTarget(String target);

	/**
	 * 根据级别字符串获取到Log4j Level对象
	 * @param levelStr
	 * @return
	 * @throws Exception
	 */
	public Level getLevelByLevelStr(String levelStr) throws Exception;
	
	/**
	 * 设置日志级别
	 * @param target
	 * @param level
	 */
	public void setTargetLevel(String target, Level level);

}