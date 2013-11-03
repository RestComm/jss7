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
import org.mobicents.protocols.ss7.isup.message.CircuitGroupResetAckMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupResetMessage;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.mobicents.protocols.ss7.isup.message.parameter.CircuitIdentificationCode;
import org.mobicents.protocols.ss7.isup.message.parameter.RangeAndStatus;

/**
 * @author baranowb
 *
 */
public class GRSTest extends DoubleTimers {
    // thanks to magic of super class, this is whole test :)
    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.impl.stack.DoubleTimers#getSmallerT()
     */

    protected long getSmallerT() {
        return ISUPTimeoutEvent.T22_DEFAULT;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.impl.stack.DoubleTimers#getBiggerT()
     */

    protected long getBiggerT() {
        return ISUPTimeoutEvent.T23_DEFAULT;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.impl.stack.DoubleTimers#getSmallerT_ID()
     */

    protected int getSmallerT_ID() {
        return ISUPTimeoutEvent.T22;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.impl.stack.DoubleTimers#getBiggerT_ID()
     */

    protected int getBiggerT_ID() {
        return ISUPTimeoutEvent.T23;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.impl.stack.DoubleTimers#getRequest()
     */

    protected ISUPMessage getRequest() {
        RangeAndStatus ras = super.provider.getParameterFactory().createRangeAndStatus();
        ras.setRange((byte) 7, true);
        ras.setAffected((byte) 1, true);
        ras.setAffected((byte) 0, true);
        CircuitGroupResetMessage grs = super.provider.getMessageFactory().createGRS(1);
        grs.setRangeAndStatus(ras);
        return grs;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.impl.stack.DoubleTimers#getAnswer()
     */

    protected ISUPMessage getAnswer() {
        CircuitGroupResetAckMessage gra = super.provider.getMessageFactory().createGRA();
        CircuitIdentificationCode cic = super.provider.getParameterFactory().createCircuitIdentificationCode();
        cic.setCIC(1);
        gra.setCircuitIdentificationCode(cic);
        RangeAndStatus ras = super.provider.getParameterFactory().createRangeAndStatus();
        ras.setRange((byte) 7, true);
        ras.setAffected((byte) 1, true);
        ras.setAffected((byte) 0, true);
        gra.setRangeAndStatus(ras);

        return gra;
    }
}
