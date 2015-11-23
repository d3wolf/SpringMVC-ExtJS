package system.service.impl;

import java.lang.reflect.Field;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import system.service.Log4jService;

@Service
public class Log4jServiceImpl implements Log4jService {

	public Level getLevelByLevelStr(String levelStr) throws Exception {
		Class<?> levelClass = Class.forName("org.apache.log4j.Level");
		Field field = levelClass.getField(levelStr.toUpperCase());
		Object obj = field.get(levelClass);
		if(obj instanceof Level){
			return (Level)obj;
		}
		return null;
	}

	public void setTargetLevel(String target, Level level) {
		LogManager.getLogger(target).setLevel(level);
	}

	public Level getLevelByTarget(String target) {
		Logger logger = LogManager.getLogger(target);
		if(logger != null){
			Level level = logger.getLevel();
			if(level != null){
				return level;
			}
		}
		return null;
	}

	public String getLevelStrByTarget(String target) {
		Level level = getLevelByTarget(target);
		if(level != null){
			return level.toString();
		}
		return "";
	}
}
