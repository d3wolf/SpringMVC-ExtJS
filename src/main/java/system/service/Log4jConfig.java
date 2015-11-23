package system.service;

public interface Log4jConfig {

	public abstract void enableInfo(String target);

	public abstract void enableWarn(String target);

	public abstract void enableError(String target);

	public abstract void enableDebug(String target);

	public abstract void enableTrack(String target);

	public abstract void enableOff(String target);

}