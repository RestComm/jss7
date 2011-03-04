/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * 
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free 
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.protocols.ss7.isup.impl.stack.timers;

import java.util.Properties;

import org.mobicents.protocols.ss7.isup.ISUPTimeoutEvent;
import org.mobicents.protocols.ss7.isup.message.AddressCompleteMessage;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.mobicents.protocols.ss7.isup.message.SubsequentAddressMessage;
import org.mobicents.protocols.ss7.isup.message.parameter.BackwardCallIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.CircuitIdentificationCode;
import org.mobicents.protocols.ss7.isup.message.parameter.SubsequentNumber;

/**
 * @author baranowb
 *
 */
public class SAM_ACMTest extends SingleTimers {

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.isup.impl.stack.timers.SingleTimers#getT()
	 */
	
	protected long getT() {
		return ISUPTimeoutEvent.T7_DEFAULT;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.isup.impl.stack.timers.SingleTimers#getT_ID()
	 */
	
	protected int getT_ID() {
		return ISUPTimeoutEvent.T7;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.isup.impl.stack.timers.EventTestHarness#getRequest()
	 */
	
	protected ISUPMessage getRequest() {
		SubsequentAddressMessage msg = super.provider.getMessageFactory().createSAM(1);
		
		SubsequentNumber sn = super.provider.getParameterFactory().createSubsequentNumber();
		sn.setAddress("11");
		msg.setSubsequentNumber(sn);
		
		return msg;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.isup.impl.stack.timers.EventTestHarness#getAnswer()
	 */
	
	protected ISUPMessage getAnswer() {
		AddressCompleteMessage ans = super.provider.getMessageFactory().createACM();
		BackwardCallIndicators bci = super.provider.getParameterFactory().createBackwardCallIndicators();
		ans.setBackwardCallIndicators(bci);
		CircuitIdentificationCode cic = super.provider.getParameterFactory().createCircuitIdentificationCode();
		cic.setCIC(1);
		ans.setCircuitIdentificationCode(cic);
		return ans;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.isup.impl.stack.timers.EventTestHarness#getSpecificConfig()
	 */
	
	protected Properties getSpecificConfig() {
		Properties p = new Properties();
		p.put("t7", getT()+"");
		
		return p;
	}

}
