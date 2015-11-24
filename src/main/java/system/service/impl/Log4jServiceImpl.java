package system.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import system.service.Log4jService;

@Service
public class Log4jServiceImpl implements Log4jService {

	public List<String> getCurrentLoggerNames() {
		List<String> list = new ArrayList<String>();

		@SuppressWarnings("unchecked")
		Enumeration<Logger> enums = LogManager.getCurrentLoggers();
		while (enums.hasMoreElements()) {
			Logger obj = enums.nextElement();
			list.add(obj.getName());
		}
		
		Collections.sort(list);

		return list;
	}

	public Level getLevelByLevelStr(String levelStr) throws Exception {
		Class<?> levelClass = Class.forName("org.apache.log4j.Level");
		Field field = levelClass.getField(levelStr.toUpperCase());
		Object obj = field.get(levelClass);
		if (obj instanceof Level) {
			return (Level) obj;
		}
		return null;
	}

	public void setTargetLevel(String target, Level level) {
		LogManager.getLogger(target).setLevel(level);
	}

	public Level getLevelByTarget(String target) {
		Logger logger = LogManager.getLogger(target);
		if (logger != null) {
			Level level = logger.getLevel();
			if (level != null) {
				return level;
			}else{
				Logger rootLogger = Logger.getRootLogger();
				return rootLogger.getLevel();
			}
		}
		return null;
	}

	public String getLevelStrByTarget(String target) {
		Level level = getLevelByTarget(target);
		if (level != null) {
			return level.toString();
		}
		return "";
	}
}
