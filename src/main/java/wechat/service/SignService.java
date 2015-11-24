package wechat.service;

public interface SignService {

	/**
	 * 验证签名
	 * 
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	public abstract boolean checkSignature(String signature, String timestamp, String nonce);

}