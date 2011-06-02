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
import org.mobicents.protocols.ss7.isup.message.CircuitGroupQueryMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupQueryResponseMessage;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.mobicents.protocols.ss7.isup.message.parameter.CircuitIdentificationCode;
import org.mobicents.protocols.ss7.isup.message.parameter.CircuitStateIndicator;
import org.mobicents.protocols.ss7.isup.message.parameter.RangeAndStatus;

/**
 * @author baranowb
 * 
 */
public class CQMTest extends SingleTimers {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.isup.impl.stack.timers.SingleTimers#getT()
	 */

	protected long getT() {
		return ISUPTimeoutEvent.T28_DEFAULT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.isup.impl.stack.timers.SingleTimers#getT_ID()
	 */

	protected int getT_ID() {
		return ISUPTimeoutEvent.T28;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.impl.stack.timers.EventTestHarness#
	 * getRequest()
	 */

	protected ISUPMessage getRequest() {
		CircuitGroupQueryMessage msg = super.provider.getMessageFactory().createCQM(1);
		RangeAndStatus ras = super.provider.getParameterFactory().createRangeAndStatus();
		ras.setRange((byte) 7, true);
		ras.setAffected((byte) 1, true);
		ras.setAffected((byte) 0, true);
		msg.setRangeAndStatus(ras);
		return msg;
	}

	protected ISUPMessage getAfterTRequest() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.isup.impl.stack.timers.EventTestHarness#getAnswer
	 * ()
	 */
	protected ISUPMessage getAnswer() {
		CircuitGroupQueryResponseMessage ans = super.provider.getMessageFactory().createCQR();
		CircuitIdentificationCode cic = super.provider.getParameterFactory().createCircuitIdentificationCode();
		cic.setCIC(1);
		ans.setCircuitIdentificationCode(cic);

		RangeAndStatus ras = super.provider.getParameterFactory().createRangeAndStatus();
		ras.setRange((byte) 7, true);
		ras.setAffected((byte) 1, true);
		ras.setAffected((byte) 0, true);
		ans.setRangeAndStatus(ras);

		CircuitStateIndicator ci = super.provider.getParameterFactory().createCircuitStateIndicator();
		byte[] state = new byte[2];
		state[0] = ci.createCircuitState(ci._MBS_LAR_BLOCKED, ci._CPS_CIB, ci._HBS_LAR_BLOCKED);
		state[1] = ci.createCircuitState(ci._MBS_LAR_BLOCKED, ci._CPS_COB, ci._HBS_LAR_BLOCKED);
		ci.setCircuitState(state);
		ans.setCircuitStateIndicator(ci);
		return ans;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.impl.stack.timers.EventTestHarness#
	 * getSpecificConfig()
	 */

	protected Properties getSpecificConfig() {
		Properties p = new Properties();
		p.put("t28", getT() + "");
		p.put("ni", "2");
		p.put("localspc", "2");
		return p;
	}

}
