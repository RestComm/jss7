/**
 * Start time:15:07:07 2009-07-17<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * 
 */
package org.mobicents.ss7.isup.impl.message;

import org.mobicents.ss7.isup.message.ISUPMessage;
import org.mobicents.ss7.isup.message.ResetCircuitMessage;

/**
 * Start time:15:07:07 2009-07-17<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class RSCTest extends MessageHarness{

	
	@Override
	protected byte[] getDefaultBody() {
		//FIXME: for now we strip MTP part
		byte[] message={
				
				0x0C
				,(byte) 0x0B
				,ResetCircuitMessage._MESSAGE_CODE_RSC

		};


		return message;
	}
	@Override
	protected ISUPMessage getDefaultMessage() {
		return super.messageFactory.createRSC();
	}
}
