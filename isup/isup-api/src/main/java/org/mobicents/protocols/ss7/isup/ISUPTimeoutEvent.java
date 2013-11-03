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

package org.mobicents.protocols.ss7.isup;

import java.util.EventObject;

import org.mobicents.protocols.ss7.isup.message.ISUPMessage;

/**
 * Event indicating timeout of some sort, ie. T7
 *
 * @author baranowb
 *
 */
public class ISUPTimeoutEvent extends EventObject implements ISUPTimeout {

    protected final ISUPMessage message;
    protected final int timerId;
    protected final int dpc;

    /**
     * @param message
     * @param circuit
     */
    public ISUPTimeoutEvent(Object source, ISUPMessage message, int timerId, int dpc) {
        super(source);
        this.message = message;
        this.timerId = timerId;
        this.dpc = dpc;
    }

    public ISUPMessage getMessage() {
        return message;
    }

    /**
     * Returns ID
     *
     * @return
     */
    public int getTimerId() {
        return timerId;
    }

    public int getDpc() {
        return dpc;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        if (message != null) {
            result = prime * result + message.getMessageType().getCode();
            result = prime * result + message.getCircuitIdentificationCode().getCIC();
        }

        result = prime * result + timerId;
        result = prime * result + dpc;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ISUPTimeoutEvent other = (ISUPTimeoutEvent) obj;
        if (this.message != null && other.message == null) {
            return false;
        }

        if (this.message == null && other.message != null) {
            return false;
        }

        if (this.message.getCircuitIdentificationCode().getCIC() != other.message.getCircuitIdentificationCode().getCIC())
            return false;

        if (this.message.getMessageType().getCode() != other.message.getMessageType().getCode())
            return false;

        if (dpc != other.dpc)
            return false;

        if (timerId != other.timerId)
            return false;

        return true;
    }

    @Override
    public String toString() {
        return "ISUPTimeoutEvent [message=" + message + ", timerId=" + timerId + "]";
    }
}
