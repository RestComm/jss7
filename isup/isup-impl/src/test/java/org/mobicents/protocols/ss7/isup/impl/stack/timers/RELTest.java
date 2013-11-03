/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.protocols.ss7.isup.impl.stack.timers;

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
    // thanks to magic of super class, this is whole test :)
    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.impl.stack.DoubleTimers#getSmallerT()
     */

    protected long getSmallerT() {
        return ISUPTimeoutEvent.T1_DEFAULT;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.impl.stack.DoubleTimers#getBiggerT()
     */

    protected long getBiggerT() {
        return ISUPTimeoutEvent.T5_DEFAULT;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.impl.stack.DoubleTimers#getSmallerT_ID()
     */

    protected int getSmallerT_ID() {
        return ISUPTimeoutEvent.T1;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.impl.stack.DoubleTimers#getBiggerT_ID()
     */

    protected int getBiggerT_ID() {
        return ISUPTimeoutEvent.T5;
    }

    /*
     * (non-Javadoc)
     *
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

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.impl.stack.DoubleTimers#getAnswer()
     */

    protected ISUPMessage getAnswer() {
        ReleaseCompleteMessage rlc = super.provider.getMessageFactory().createRLC();
        CircuitIdentificationCode cic = super.provider.getParameterFactory().createCircuitIdentificationCode();
        cic.setCIC(1);
        rlc.setCircuitIdentificationCode(cic);
        return rlc; // asnwer to RSC is... RLC :P
    }

    protected ISUPMessage getAfterBigTRequest() {
        ResetCircuitMessage rsc = super.provider.getMessageFactory().createRSC(1);
        return rsc;
    }
}
