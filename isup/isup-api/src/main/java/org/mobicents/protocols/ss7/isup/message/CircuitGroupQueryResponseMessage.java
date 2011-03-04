package org.mobicents.protocols.ss7.isup.message;

import org.mobicents.protocols.ss7.isup.message.parameter.CircuitStateIndicator;
import org.mobicents.protocols.ss7.isup.message.parameter.RangeAndStatus;

/**
 * Start time:09:54:07 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface CircuitGroupQueryResponseMessage extends ISUPMessage {

	/**
	 * Circuit Group Query Response Message, Q.763 reference table 24 <br>
	 * {@link CircuitGroupQueryResponseMessage}
	 */
	public static final int MESSAGE_CODE = 0x2B;

	public void setRangeAndStatus(RangeAndStatus ras);

	public RangeAndStatus getRangeAndStatus();

	public void setCircuitStateIndicator(CircuitStateIndicator ras);

	public CircuitStateIndicator getCircuitStateIndicator();
}
