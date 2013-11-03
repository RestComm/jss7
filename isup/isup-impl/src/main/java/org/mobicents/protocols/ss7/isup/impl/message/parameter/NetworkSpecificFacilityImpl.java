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
 * Start time:09:37:50 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.NetworkSpecificFacility;

/**
 * Start time:09:37:50 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class NetworkSpecificFacilityImpl extends AbstractISUPParameter implements NetworkSpecificFacility {

    /**
     * This tells us to include byte 1a - sets lengthOfNetworkIdentification to 1+networkdIdentification.length
     */
    private boolean includeNetworkIdentification;

    private int lengthOfNetworkIdentification;
    private int typeOfNetworkIdentification;
    private int networkIdentificationPlan;
    // FIXME: ext bit: indicated as to be used as in 3.25 but on specs id
    // different...
    private byte[] networkIdentification;
    private byte[] networkSpecificaFacilityIndicator;

    public NetworkSpecificFacilityImpl(byte[] b) throws ParameterException {
        super();
        decode(b);
    }

    public NetworkSpecificFacilityImpl() {
        super();

    }

    public NetworkSpecificFacilityImpl(boolean includeNetworkIdentification, byte typeOfNetworkIdentification,
            byte networkdIdentificationPlan, byte[] networkdIdentification, byte[] networkSpecificaFacilityIndicator) {
        super();
        this.includeNetworkIdentification = includeNetworkIdentification;
        this.typeOfNetworkIdentification = typeOfNetworkIdentification;
        this.networkIdentificationPlan = networkdIdentificationPlan;
        this.networkIdentification = networkdIdentification;
        this.networkSpecificaFacilityIndicator = networkSpecificaFacilityIndicator;
    }

    public int decode(byte[] b) throws ParameterException {
        if (b == null || b.length < 1) {
            throw new ParameterException("byte[] must nto be null or have length greater than 1");
        }
        // try {
        int shift = 0;
        this.lengthOfNetworkIdentification = b[shift++];

        // FIXME: We ignore ext bit, we dont need it ? ?????
        this.typeOfNetworkIdentification = (byte) ((b[shift] >> 4) & 0x07);
        this.networkIdentificationPlan = (byte) (b[shift] & 0x0F);
        shift++;
        if (this.lengthOfNetworkIdentification > 0) {

            byte[] _networkId = new byte[this.lengthOfNetworkIdentification];
            for (int i = 0; i < this.lengthOfNetworkIdentification; i++, shift++) {

                _networkId[i] = (byte) (b[shift] | 0x80);
            }

            // now lets set it.
            if (_networkId.length > 0) {

                _networkId[_networkId.length - 1] = (byte) (_networkId[_networkId.length - 1] & 0x7F);
            }

            this.setNetworkIdentification(_networkId);
        }

        if (shift + 1 == b.length) {
            throw new ParameterException("There is no facility indicator. This part is mandatory!!!");
        }
        byte[] _facility = new byte[b.length - shift - 1];
        // -1 cause shift counts from 0
        System.arraycopy(b, shift, _facility, 0, b.length - shift - 1);
        this.setNetworkSpecificaFacilityIndicator(_facility);
        return b.length;
        // } catch (ArrayIndexOutOfBoundsException aioobe) {
        // throw new IllegalArgumentException("Failed to parse due to: ",
        // aioobe);
        // }
    }

    public byte[] encode() throws ParameterException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        bos.write(this.lengthOfNetworkIdentification);
        // This should always be set to true if there is network ID
        if (this.includeNetworkIdentification) {
            int b1 = 0;
            b1 = ((this.typeOfNetworkIdentification & 0x07) << 4);
            b1 |= (this.networkIdentificationPlan & 0x0F);

            if (this.networkIdentification != null && this.networkIdentification.length > 0) {
                b1 |= 0x80;
                bos.write(b1);
                for (int index = 0; index < this.networkIdentification.length; index++) {
                    if (index == this.networkIdentification.length - 1) {

                        bos.write(this.networkIdentification[index] & 0x7F);

                    } else {
                        bos.write(this.networkIdentification[index] | (0x01 << 7));

                    }
                }
            } else {
                bos.write(b1 & 0x7F);
            }
        }

        if (this.networkSpecificaFacilityIndicator == null) {
            throw new IllegalArgumentException("Network Specific Facility must not be null");
        }
        try {
            bos.write(this.networkSpecificaFacilityIndicator);
        } catch (IOException e) {
            throw new ParameterException(e);
        }

        return bos.toByteArray();
    }

    public boolean isIncludeNetworkIdentification() {
        return includeNetworkIdentification;
    }

    public int getLengthOfNetworkIdentification() {
        return lengthOfNetworkIdentification;
    }

    public int getTypeOfNetworkIdentification() {
        return typeOfNetworkIdentification;
    }

    public void setTypeOfNetworkIdentification(byte typeOfNetworkIdentification) {
        this.typeOfNetworkIdentification = typeOfNetworkIdentification;
    }

    public int getNetworkIdentificationPlan() {
        return networkIdentificationPlan;
    }

    public void setNetworkIdentificationPlan(byte networkdIdentificationPlan) {
        this.networkIdentificationPlan = networkdIdentificationPlan;
    }

    public byte[] getNetworkIdentification() {
        return networkIdentification;
    }

    public void setNetworkIdentification(byte[] networkdIdentification) {

        if (networkdIdentification != null && networkdIdentification.length > Byte.MAX_VALUE * 2 - 1) {
            throw new IllegalArgumentException("Length of Network Identification part must not be greater than: "
                    + (Byte.MAX_VALUE * 2 - 1));
        }

        this.networkIdentification = networkdIdentification;
        this.includeNetworkIdentification = true;

    }

    public byte[] getNetworkSpecificaFacilityIndicator() {
        return networkSpecificaFacilityIndicator;
    }

    public void setNetworkSpecificaFacilityIndicator(byte[] networkSpecificaFacilityIndicator) {
        this.networkSpecificaFacilityIndicator = networkSpecificaFacilityIndicator;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
