package system.service.impl;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.springframework.stereotype.Service;

import system.service.Log4jConfig;

@Service
public class Log4jConfigImpl implements Log4jConfig {

	public void enableInfo(String target) {
		LogManager.getLogger(target).setLevel(Level.INFO);
	}

	public void enableWarn(String target) {
		LogManager.getLogger(target).setLevel(Level.WARN);
	}

	public void enableError(String target) {
		LogManager.getLogger(target).setLevel(Level.ERROR);
	}

	public void enableDebug(String target) {
		LogManager.getLogger(target).setLevel(Level.DEBUG);
	}

	public void enableTrack(String target) {
		LogManager.getLogger(target).setLevel(Level.TRACE);
	}

	public void enableOff(String target) {
		LogManager.getLogger(target).setLevel(Level.OFF);
	}
}
