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
 * Start time:16:41:45 2009-04-02<br>
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
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectionNumber;

/**
 * Start time:16:41:45 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class RedirectionNumberImpl extends AbstractNAINumber implements RedirectionNumber {

    protected int numberingPlanIndicator;

    protected int internalNetworkNumberIndicator;

    public RedirectionNumberImpl() {
        super();

    }

    public RedirectionNumberImpl(byte[] representation) throws ParameterException {
        super(representation);

    }

    public RedirectionNumberImpl(ByteArrayInputStream bis) throws ParameterException {
        super(bis);

    }

    public RedirectionNumberImpl(int natureOfAddresIndicator, String address, int numberingPlanIndicator,
            int internalNetworkNumberIndicator) {
        super(natureOfAddresIndicator, address);
        this.numberingPlanIndicator = numberingPlanIndicator;
        this.internalNetworkNumberIndicator = internalNetworkNumberIndicator;
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.mobicents.isup.parameters.AbstractNumber#decodeBody(java.io. ByteArrayInputStream)
     */

    public int decodeBody(ByteArrayInputStream bis) throws IllegalArgumentException {
        int b = bis.read() & 0xff;

        this.internalNetworkNumberIndicator = (b & 0x80) >> 7;
        this.numberingPlanIndicator = (b & 0x70) >> 4;
        return 1;
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.mobicents.isup.parameters.AbstractNumber#encodeBody(java.io. ByteArrayOutputStream)
     */

    public int encodeBody(ByteArrayOutputStream bos) {
        int c = this.natureOfAddresIndicator << 4;
        c |= (this.internalNetworkNumberIndicator << 7);
        bos.write(c);
        return 1;
    }

    public int getNumberingPlanIndicator() {
        return numberingPlanIndicator;
    }

    public void setNumberingPlanIndicator(int numberingPlanIndicator) {
        this.numberingPlanIndicator = numberingPlanIndicator;
    }

    public int getInternalNetworkNumberIndicator() {
        return internalNetworkNumberIndicator;
    }

    public void setInternalNetworkNumberIndicator(int internalNetworkNumberIndicator) {
        this.internalNetworkNumberIndicator = internalNetworkNumberIndicator;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
