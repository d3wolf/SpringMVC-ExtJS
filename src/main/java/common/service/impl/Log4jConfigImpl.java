package common.service.impl;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.springframework.stereotype.Service;

import common.service.Log4jConfig;

@Service
public class Log4jConfigImpl implements Log4jConfig {
	/* (non-Javadoc)
	 * @see common.service.impl.Log4jConfig#enableInfo(java.lang.String)
	 */
	public void enableInfo(String target) {
		LogManager.getLogger(target).setLevel(Level.INFO);
	}

	/* (non-Javadoc)
	 * @see common.service.impl.Log4jConfig#enableWarn(java.lang.String)
	 */
	public void enableWarn(String target) {
		LogManager.getLogger(target).setLevel(Level.WARN);
	}

	/* (non-Javadoc)
	 * @see common.service.impl.Log4jConfig#enableError(java.lang.String)
	 */
	public void enableError(String target) {
		LogManager.getLogger(target).setLevel(Level.ERROR);
	}

	/* (non-Javadoc)
	 * @see common.service.impl.Log4jConfig#enableDebug(java.lang.String)
	 */
	public void enableDebug(String target) {
		LogManager.getLogger(target).setLevel(Level.DEBUG);
	}

	/* (non-Javadoc)
	 * @see common.service.impl.Log4jConfig#enableTrack(java.lang.String)
	 */
	public void enableTrack(String target) {
		LogManager.getLogger(target).setLevel(Level.TRACE);
	}

	/* (non-Javadoc)
	 * @see common.service.impl.Log4jConfig#enableOff(java.lang.String)
	 */
	public void enableOff(String target) {
		LogManager.getLogger(target).setLevel(Level.OFF);
	}
}
