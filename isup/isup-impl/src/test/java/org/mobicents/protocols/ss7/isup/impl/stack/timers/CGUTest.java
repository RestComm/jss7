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
import org.mobicents.protocols.ss7.isup.message.CircuitGroupUnblockingAckMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupUnblockingMessage;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.mobicents.protocols.ss7.isup.message.parameter.CircuitGroupSuperVisionMessageType;
import org.mobicents.protocols.ss7.isup.message.parameter.CircuitIdentificationCode;
import org.mobicents.protocols.ss7.isup.message.parameter.RangeAndStatus;

/**
 * @author baranowb
 * 
 */
public class CGUTest extends DoubleTimers {
	// thanks to magic of super class, this is whole test :)
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.isup.impl.stack.DoubleTimers#getSmallerT()
	 */
	
	protected long getSmallerT() {
		return ISUPTimeoutEvent.T20_DEFAULT + 3000;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.isup.impl.stack.DoubleTimers#getBiggerT()
	 */
	
	protected long getBiggerT() {
		return ISUPTimeoutEvent.T21_DEFAULT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.isup.impl.stack.DoubleTimers#getSmallerT_ID()
	 */
	
	protected int getSmallerT_ID() {
		return ISUPTimeoutEvent.T20;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.isup.impl.stack.DoubleTimers#getBiggerT_ID()
	 */
	
	protected int getBiggerT_ID() {
		return ISUPTimeoutEvent.T21;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.isup.impl.stack.DoubleTimers#getRequest()
	 */
	
	protected ISUPMessage getRequest() {
		CircuitGroupUnblockingMessage cgu = super.provider.getMessageFactory().createCGU(1);

		RangeAndStatus ras = super.provider.getParameterFactory().createRangeAndStatus();
		ras.setRange((byte) 7,true);
		ras.setAffected((byte) 1, true);
		ras.setAffected((byte) 0, true);
		cgu.setRangeAndStatus(ras);

		CircuitGroupSuperVisionMessageType cgsvmt = super.provider.getParameterFactory().createCircuitGroupSuperVisionMessageType();
		cgsvmt.setCircuitGroupSuperVisionMessageTypeIndicator(cgsvmt._CIRCUIT_GROUP_SMTIHFO);
		cgu.setSupervisionType(cgsvmt);


		return cgu;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.impl.stack.DoubleTimers#getAnswer()
	 */
	
	protected ISUPMessage getAnswer() {
		CircuitGroupUnblockingAckMessage cgua = super.provider.getMessageFactory().createCGUA();
		CircuitIdentificationCode cic = super.provider.getParameterFactory().createCircuitIdentificationCode();
		cic.setCIC(1);
		cgua.setCircuitIdentificationCode(cic);
		
		RangeAndStatus ras = super.provider.getParameterFactory().createRangeAndStatus();
		ras.setRange((byte) 7,true);
		ras.setAffected((byte) 1, true);
		ras.setAffected((byte) 0, true);
		cgua.setRangeAndStatus(ras);

		CircuitGroupSuperVisionMessageType cgsvmt = super.provider.getParameterFactory().createCircuitGroupSuperVisionMessageType();
		cgsvmt.setCircuitGroupSuperVisionMessageTypeIndicator(cgsvmt._CIRCUIT_GROUP_SMTIHFO);
		cgua.setSupervisionType(cgsvmt);
		
		return cgua;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.impl.stack.EventTestHarness#
	 * getSpecificConfig()
	 */
	
	protected Properties getSpecificConfig() {
		// ensure proper values;
		Properties p = new Properties();
		p.put("t20", getSmallerT()+"");
		p.put("t21" ,  getBiggerT()+"");
		return p;
	}

}
