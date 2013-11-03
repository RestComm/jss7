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

/**
 * Start time:12:42:55 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.ParameterCompatibilityInstructionIndicators;

/**
 * Start time:12:42:55 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class ParameterCompatibilityInstructionIndicatorsImpl implements ParameterCompatibilityInstructionIndicators, Encodable {

    private static final int _TURN_ON = 1;
    private static final int _TURN_OFF = 0;

    private byte parameterCode;
    // FIXME: decide how to use this.
    private boolean transitAtIntermediateExchangeIndicator;
    private boolean releaseCallindicator;
    private boolean sendNotificationIndicator;
    private boolean discardMessageIndicator;
    private boolean discardParameterIndicator;
    private int passOnNotPossibleIndicator;
    private int bandInterworkingIndicator;

    private boolean secondOctetPresenet;

    private byte[] raw;
    private boolean useAsRaw;

    public ParameterCompatibilityInstructionIndicatorsImpl(final byte code, final byte[] b) throws ParameterException {
        super();
        this.parameterCode = code;
        decode(b);
    }

    public ParameterCompatibilityInstructionIndicatorsImpl() {
        super();

    }

    /**
     * This constructor shall be used in cases more octets are defined, User needs to manipulate and encode them properly.
     *
     * @param b
     * @param userAsRaw
     * @throws ParameterException
     */
    public ParameterCompatibilityInstructionIndicatorsImpl(final byte code, final byte[] b, final boolean userAsRaw) throws ParameterException {
        super();
        this.parameterCode = code;
        this.raw = b;
        this.useAsRaw = userAsRaw;
        if (!userAsRaw)
            decode(b);
    }

    public ParameterCompatibilityInstructionIndicatorsImpl(boolean transitAtIntermediateExchangeIndicator, boolean releaseCallindicator,
            boolean sendNotificationIndicator, boolean discardMessageIndicator, boolean discardParameterIndicator,
            int passOnNotPossibleIndicator, boolean secondOctetPresenet) {
        super();
        this.transitAtIntermediateExchangeIndicator = transitAtIntermediateExchangeIndicator;
        this.releaseCallindicator = releaseCallindicator;
        this.sendNotificationIndicator = sendNotificationIndicator;
        this.discardMessageIndicator = discardMessageIndicator;
        this.discardParameterIndicator = discardParameterIndicator;
        this.setPassOnNotPossibleIndicator(passOnNotPossibleIndicator);
        this.secondOctetPresenet = secondOctetPresenet;

    }

    public ParameterCompatibilityInstructionIndicatorsImpl(boolean transitAtIntermediateExchangeIndicator, boolean releaseCallindicator,
            boolean sendNotificationIndicator, boolean discardMessageIndicator, boolean discardParameterIndicator,
            int passOnNotPossibleIndicator, int bandInterworkingIndicator) {
        super();
        this.transitAtIntermediateExchangeIndicator = transitAtIntermediateExchangeIndicator;
        this.releaseCallindicator = releaseCallindicator;
        this.sendNotificationIndicator = sendNotificationIndicator;
        this.discardMessageIndicator = discardMessageIndicator;
        this.discardParameterIndicator = discardParameterIndicator;
        this.setPassOnNotPossibleIndicator(passOnNotPossibleIndicator);
        this.setBandInterworkingIndicator(bandInterworkingIndicator);
    }

    public int decode(byte[] b) throws ParameterException {
        if (b == null || b.length < 1) {
            throw new ParameterException("byte[] must  not be null and length must  be greater than  0");
        }

        // XXX: Cheat, we read only defined in Q763 2 octets, rest we ignore...
        int index = 0;
        int v = b[index];

        try {
            // watch extension byte
            do {
                v = b[index];
                if (index == 0) {
                    this.transitAtIntermediateExchangeIndicator = (v & 0x01) == _TURN_ON;
                    this.releaseCallindicator = ((v >> 1) & 0x01) == _TURN_ON;
                    this.sendNotificationIndicator = ((v >> 2) & 0x01) == _TURN_ON;
                    this.discardMessageIndicator = ((v >> 3) & 0x01) == _TURN_ON;
                    this.discardParameterIndicator = ((v >> 4) & 0x01) == _TURN_ON;
                    this.passOnNotPossibleIndicator = ((v >> 5) & 0x03);
                } else if (index == 1) {
                    this.setBandInterworkingIndicator((v & 0x03));
                } else {
                    // if (logger.isLoggable(Level.FINEST)) {
                    // logger.finest("Skipping octets with index[" + index + "] in " + this.getClass().getName() +
                    // ". This should not be called for us .... Instead one should use raw");
                    // }
                    break;
                }
                index++;

            } while ((((v >> 7) & 0x01) != 0));
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            aioobe.printStackTrace();
            throw new ParameterException("Failed to parse passed value due to wrong encoding.", aioobe);
        }
        return b.length;
    }

    public byte[] encode() throws ParameterException {
        if (this.useAsRaw) {
            // FIXME: make sure we properly encode ext bit?
            return this.raw;
        }
        byte[] b = null;
        if (this.secondOctetPresenet) {
            b = new byte[2];
            b[1] = (byte) ((this.bandInterworkingIndicator & 0x03));
            b[0] = (byte) 0x80;
        } else {
            b = new byte[1];
        }

        b[0] |= (this.transitAtIntermediateExchangeIndicator ? _TURN_ON : _TURN_OFF);
        b[0] |= (this.releaseCallindicator ? _TURN_ON : _TURN_OFF) << 1;
        b[0] |= (this.sendNotificationIndicator ? _TURN_ON : _TURN_OFF) << 2;
        b[0] |= (this.discardMessageIndicator ? _TURN_ON : _TURN_OFF) << 3;
        b[0] |= (this.discardParameterIndicator ? _TURN_ON : _TURN_OFF) << 4;
        b[0] |= this.passOnNotPossibleIndicator << 5;

        return b;
    }

    @Override
    public void setParamerterCode(byte code) {
        this.parameterCode = code;
    }

    @Override
    public byte getParameterCode() {
        return this.parameterCode;
    }

    public boolean isTransitAtIntermediateExchangeIndicator() {
        return transitAtIntermediateExchangeIndicator;
    }

    public void setTransitAtIntermediateExchangeIndicator(boolean transitAtIntermediateExchangeIndicator) {
        this.transitAtIntermediateExchangeIndicator = transitAtIntermediateExchangeIndicator;
    }

    public boolean isReleaseCallIndicator() {
        return releaseCallindicator;
    }

    public void setReleaseCallIndicator(boolean releaseCallindicator) {
        this.releaseCallindicator = releaseCallindicator;
    }

    public boolean isSendNotificationIndicator() {
        return sendNotificationIndicator;
    }

    public void setSendNotificationIndicator(boolean sendNotificationIndicator) {
        this.sendNotificationIndicator = sendNotificationIndicator;
    }

    public boolean isDiscardMessageIndicator() {
        return discardMessageIndicator;
    }

    public void setDiscardMessageIndicator(boolean discardMessageIndicator) {
        this.discardMessageIndicator = discardMessageIndicator;
    }

    public boolean isDiscardParameterIndicator() {
        return discardParameterIndicator;
    }

    public void setDiscardParameterIndicator(boolean discardParameterIndicator) {
        this.discardParameterIndicator = discardParameterIndicator;
    }

    public int getPassOnNotPossibleIndicator() {
        return passOnNotPossibleIndicator;
    }

    public void setPassOnNotPossibleIndicator(int passOnNotPossibleIndicator2) {
        this.passOnNotPossibleIndicator = passOnNotPossibleIndicator2;
    }

    public int getBandInterworkingIndicator() {
        return bandInterworkingIndicator;
    }

    public void setBandInterworkingIndicator(int bandInterworkingIndicator) {
        this.bandInterworkingIndicator = bandInterworkingIndicator;
        this.secondOctetPresenet = true;
    }

    public boolean isSecondOctetPresent() {
        return secondOctetPresenet;
    }

    public void setSecondOctetPresent(boolean secondOctetPresenet) {
        this.secondOctetPresenet = secondOctetPresenet;
    }

    public byte[] getRaw() {
        return raw;
    }

    public void setRaw(byte[] raw) {
        this.raw = raw;
    }

    public boolean isUseAsRaw() {
        return useAsRaw;
    }

    public void setUseAsRaw(boolean useAsRaw) {
        this.useAsRaw = useAsRaw;
    }
//
//    public int getCode() {
//
//        return _PARAMETER_CODE;
//    }
}
