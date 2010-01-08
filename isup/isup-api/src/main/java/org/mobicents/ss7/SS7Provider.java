package org.mobicents.ss7;
/**
 * Start time:08:51:48 2009-08-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */

/**
 * Start time:08:51:48 2009-08-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public interface SS7Provider {
	/**
	 * @param encoded
	 */
	void sendData(byte[] encoded);
	
	//Other methods which expose info about configuration, like SPC
	void addSS7PayloadListener(SS7PayloadListener listener);
	void removeSS7PayloadListener(SS7PayloadListener listener);
}
