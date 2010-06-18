/**
 * 
 */
package org.mobicents.protocols.ss7.stream;

/**
 * 
 * Simple interface used to intercept Mtp3 MSU. It is used to indicate whether msu should be proxied or has it been already intercepted by local resource.
 * 
 * @author baranowb
 *
 */
public interface InterceptorHook {

	/**
	 * Method called once proper MSU is received. Note that this method should not perform long operations.
	 * 
	 * @param mtp3MSU
	 * @return <ul>
	 * <li><b>true</b> -  if MSU has been intercepted, this indicates that streamer MUST NOT proxy this message.</li>
	 * <li><b>false</b> -  if MSU has not been itnercepted, this indicates that streamer MUST proxy this message as soon as possible.</li>
	 * </ul>
	 */
	public boolean intercepted(byte[] mtp3MSU);
	/**
	 * Back reference set by streamer.
	 * @param sf
	 */
	public void setStreamForwarder(StreamForwarder sf);
	
}
