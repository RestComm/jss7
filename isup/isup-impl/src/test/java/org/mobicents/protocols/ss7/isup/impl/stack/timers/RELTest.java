/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.isup.impl.stack.timers;

import java.util.Properties;

import org.mobicents.protocols.ss7.isup.ISUPTimeoutEvent;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.mobicents.protocols.ss7.isup.message.ReleaseCompleteMessage;
import org.mobicents.protocols.ss7.isup.message.ReleaseMessage;
import org.mobicents.protocols.ss7.isup.message.ResetCircuitMessage;
import org.mobicents.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.CircuitIdentificationCode;

/**
 * @author baranowb
 *
 */
public class RELTest extends DoubleTimers {
	//thanks to magic of super class, this is whole test :)
	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.isup.impl.stack.DoubleTimers#getSmallerT()
	 */
	
	protected long getSmallerT() {
		return ISUPTimeoutEvent.T1_DEFAULT+2000;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.isup.impl.stack.DoubleTimers#getBiggerT()
	 */
	
	protected long getBiggerT() {
		return ISUPTimeoutEvent.T5_DEFAULT;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.isup.impl.stack.DoubleTimers#getSmallerT_ID()
	 */
	
	protected int getSmallerT_ID() {
		return ISUPTimeoutEvent.T1;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.isup.impl.stack.DoubleTimers#getBiggerT_ID()
	 */
	
	protected int getBiggerT_ID() {
		return ISUPTimeoutEvent.T5;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.isup.impl.stack.DoubleTimers#getRequest()
	 */
	
	protected ISUPMessage getRequest() {
		ReleaseMessage rel = super.provider.getMessageFactory().createREL(1);
		CauseIndicators ci = super.provider.getParameterFactory().createCauseIndicators();
		ci.setCauseValue(0);
		ci.setCodingStandard(ci._CODING_STANDARD_NATIONAL);
		rel.setCauseIndicators(ci);
		return rel; 
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.isup.impl.stack.DoubleTimers#getAnswer()
	 */
	
	protected ISUPMessage getAnswer() {
		ReleaseCompleteMessage rlc = super.provider.getMessageFactory().createRLC();
		CircuitIdentificationCode cic = super.provider.getParameterFactory().createCircuitIdentificationCode();
		cic.setCIC(1);
		rlc.setCircuitIdentificationCode(cic);
		return rlc; //asnwer to RSC is... RLC :P
	}

	protected ISUPMessage getAfterBigTRequest()
	{
		ResetCircuitMessage rsc = super.provider.getMessageFactory().createRSC(1);
		return rsc;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.isup.impl.stack.EventTestHarness#getSpecificConfig()
	 */
	
	protected Properties getSpecificConfig() {
		//ensure proper values;
		Properties p = new Properties();
		p.put("t1", getSmallerT()+"");
		p.put("t5" ,  getBiggerT()+"");
		return p;
	}	


}
