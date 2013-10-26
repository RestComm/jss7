/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
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

package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.MessageCompatibilityInstructionIndicator;

/**
 * @author baranowb
 *
 */
public class MessageCompatibilityInstructionIndicatorImpl implements MessageCompatibilityInstructionIndicator {
    private static final int _TURN_ON = 1;
    private static final int _TURN_OFF = 0;

    private boolean transitAtIntermediateExchangeIndicator;
    private boolean releaseCallindicator;
    private boolean sendNotificationIndicator;
    private boolean discardMessageIndicator;
    private boolean passOnNotPossibleIndicator;
    private int bandInterworkingIndicator;

    public MessageCompatibilityInstructionIndicatorImpl(byte[] b) throws ParameterException {
        super();
        decode(b);
    }

    public MessageCompatibilityInstructionIndicatorImpl() {
        super();

    }

    @Override
    public boolean isTransitAtIntermediateExchangeIndicator() {
        return this.transitAtIntermediateExchangeIndicator;
    }

    @Override
    public void setTransitAtIntermediateExchangeIndicator(boolean transitAtIntermediateExchangeIndicator) {
        this.transitAtIntermediateExchangeIndicator = transitAtIntermediateExchangeIndicator;
    }

    @Override
    public boolean isReleaseCallIndicator() {
        return this.releaseCallindicator;
    }

    @Override
    public void setReleaseCallIndicator(boolean releaseCallindicator) {
        this.releaseCallindicator = releaseCallindicator;
    }

    @Override
    public boolean isSendNotificationIndicator() {
        return this.sendNotificationIndicator;
    }

    @Override
    public void setSendNotificationIndicator(boolean sendNotificationIndicator) {
        this.sendNotificationIndicator = sendNotificationIndicator;
    }

    @Override
    public boolean isDiscardMessageIndicator() {
        return this.discardMessageIndicator;
    }

    @Override
    public void setDiscardMessageIndicator(boolean discardMessageIndicator) {
        this.discardMessageIndicator = discardMessageIndicator;
    }

    @Override
    public boolean isPassOnNotPossibleIndicator() {
        return this.passOnNotPossibleIndicator;
    }

    @Override
    public void setPassOnNotPossibleIndicator(boolean passOnNotPossibleIndicator) {
        this.passOnNotPossibleIndicator = passOnNotPossibleIndicator;
    }

    @Override
    public int getBandInterworkingIndicator() {
        return this.bandInterworkingIndicator;
    }

    @Override
    public void setBandInterworkingIndicator(int bandInterworkingIndicator) {
        this.bandInterworkingIndicator = bandInterworkingIndicator & 0x03;
    }

    public int decode(byte[] b) throws ParameterException {
        if (b == null || b.length < 1) {
            throw new ParameterException("byte[] must  not be null and length must  be greater than  0");
        }
        if(b.length >1){
            throw new ParameterException("Too many bytes: "+b.length);
        }

        int v = b[0];
        this.transitAtIntermediateExchangeIndicator = (v & 0x01) == _TURN_ON;
        this.releaseCallindicator = ((v >> 1) & 0x01) == _TURN_ON;
        this.sendNotificationIndicator = ((v >> 2) & 0x01) == _TURN_ON;
        this.discardMessageIndicator = ((v >> 3) & 0x01) == _TURN_ON;
        this.passOnNotPossibleIndicator = ((v >> 4) & 0x01) == _TURN_ON;
        this.bandInterworkingIndicator = ((v >> 5) & 0x03);
        return 0;
    }

    public byte[] encode() throws ParameterException {
        byte[] b = new byte[1];
        b[0] |= (this.transitAtIntermediateExchangeIndicator ? _TURN_ON : _TURN_OFF);
        b[0] |= (this.releaseCallindicator ? _TURN_ON : _TURN_OFF) << 1;
        b[0] |= (this.sendNotificationIndicator ? _TURN_ON : _TURN_OFF) << 2;
        b[0] |= (this.discardMessageIndicator ? _TURN_ON : _TURN_OFF) << 3;
        b[0] |= (this.passOnNotPossibleIndicator ? _TURN_ON : _TURN_OFF) << 4;
        b[0] |= this.bandInterworkingIndicator << 5;
        return b;
    }

}
