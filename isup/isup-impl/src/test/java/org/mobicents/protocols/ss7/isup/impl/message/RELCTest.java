package org.mobicents.protocols.ss7.isup.impl.message;


import org.mobicents.protocols.ss7.isup.message.ISUPMessage;

/**
 * Start time:15:07:07 2009-07-17<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class RELCTest extends MessageHarness{

	
	@Override
	protected byte[] getDefaultBody() {
		//FIXME: for now we strip MTP part
		byte[] message={

				0x0C
				,(byte) 0x0B
				,0x10,
				0x00


		};


		return message;
	}
	@Override
	protected ISUPMessage getDefaultMessage() {
		return super.messageFactory.createRLC();
	}
}
