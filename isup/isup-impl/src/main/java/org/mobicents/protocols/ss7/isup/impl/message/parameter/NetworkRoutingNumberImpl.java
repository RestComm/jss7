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
 * Start time:18:44:18 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.NetworkRoutingNumber;

/**
 * Start time:18:44:18 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class NetworkRoutingNumberImpl extends AbstractNumber implements NetworkRoutingNumber {

    private int numberingPlanIndicator;
    private int natureOfAddressIndicator;

    public NetworkRoutingNumberImpl(String address) {
        super(address);

    }

    public NetworkRoutingNumberImpl(String address, int numberingPlanIndicator, int natureOfAddressIndicator) {
        super(address);
        this.numberingPlanIndicator = numberingPlanIndicator;
        this.natureOfAddressIndicator = natureOfAddressIndicator;
    }

    public NetworkRoutingNumberImpl() {
        super();

    }

    public NetworkRoutingNumberImpl(byte[] representation) throws ParameterException {
        super(representation);

    }

    public NetworkRoutingNumberImpl(ByteArrayInputStream bis) throws ParameterException {
        super(bis);

    }

    public int decodeBody(ByteArrayInputStream bis) throws IllegalArgumentException {

        return 0;
    }

    public int encodeBody(ByteArrayOutputStream bos) {

        return 0;
    }

    public int decodeHeader(ByteArrayInputStream bis) throws IllegalArgumentException {

        int b = bis.read() & 0xff;

        this.oddFlag = (b & 0x80) >> 7;
        this.numberingPlanIndicator = (b & 0x70) >> 4;
        this.natureOfAddressIndicator = b & 0x0F;
        return 1;
    }

    public int encodeHeader(ByteArrayOutputStream bos) {
        int b = 0;
        // Even is 000000000 == 0
        boolean isOdd = this.oddFlag == _FLAG_ODD;
        if (isOdd)
            b |= 0x80;

        b |= (this.numberingPlanIndicator & 0x07) << 4;
        b |= this.natureOfAddressIndicator & 0x0F;
        bos.write(b);

        return 1;
    }

    public int getNumberingPlanIndicator() {
        return numberingPlanIndicator;
    }

    public void setNumberingPlanIndicator(int numberingPlanIndicator) {
        this.numberingPlanIndicator = numberingPlanIndicator;
    }

    public int getNatureOfAddressIndicator() {
        return natureOfAddressIndicator;
    }

    public void setNatureOfAddressIndicator(int natureOfAddressIndicator) {
        this.natureOfAddressIndicator = natureOfAddressIndicator;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
