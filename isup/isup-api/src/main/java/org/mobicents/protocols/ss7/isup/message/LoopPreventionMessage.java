package org.mobicents.protocols.ss7.isup.message;


/**
 * Start time:09:54:07 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface LoopPreventionMessage extends ISUPMessage {
	/**
	 * Loopback Prevention Message, Q.763 reference table 50 <br>
	 * {@link LoopPreventionMessage}
	 */
	public static final int MESSAGE_CODE = 0x40;
}
