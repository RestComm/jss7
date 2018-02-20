/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

/**
 * Start time:11:25:13 2009-03-31<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.EventInformation;

/**
 * Start time:11:25:13 2009-03-31<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class EventInformationImpl extends AbstractISUPParameter implements EventInformation {

    private static final int _TURN_ON = 1;
    private static final int _TURN_OFF = 0;

    private int eventIndicator;
    private boolean eventPresentationRestrictedIndicator;

    public EventInformationImpl(byte[] b) throws ParameterException {
        super();
        decode(b);
    }

    public EventInformationImpl() {
        super();

    }

    public EventInformationImpl(int eventIndicator) {
        super();
        this.eventIndicator = eventIndicator;
    }

    public int decode(byte[] b) throws ParameterException {
        if (b == null || b.length != 1) {
            throw new ParameterException("byte[] must not be null or have different size than 1");
        }

        this.eventIndicator = b[0] & 0x7F;
        this.eventPresentationRestrictedIndicator = ((b[0] >> 7) & 0x01) == _TURN_ON;

        return 1;
    }

    public byte[] encode() throws ParameterException {
        byte[] b = new byte[] { (byte) (this.eventIndicator & 0x7F) };

        b[0] |= (byte) ((this.eventPresentationRestrictedIndicator ? _TURN_ON : _TURN_OFF) << 7);
        return b;
    }

    public int getEventIndicator() {
        return eventIndicator;
    }

    public void setEventIndicator(int eventIndicator) {
        this.eventIndicator = eventIndicator;
    }

    public boolean isEventPresentationRestrictedIndicator() {
        return eventPresentationRestrictedIndicator;
    }

    public void setEventPresentationRestrictedIndicator(boolean eventPresentationRestrictedIndicator) {
        this.eventPresentationRestrictedIndicator = eventPresentationRestrictedIndicator;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("EventInformation [");

        sb.append("eventIndicator=");
        sb.append(eventIndicator);
        sb.append(", ");
        sb.append("eventPresentationRestrictedIndicator=");
        sb.append(eventPresentationRestrictedIndicator);

        sb.append("]");
        return sb.toString();
    }
}
