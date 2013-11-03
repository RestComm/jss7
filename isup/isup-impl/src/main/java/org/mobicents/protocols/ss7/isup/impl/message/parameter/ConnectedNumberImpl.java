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
 * Start time:16:36:21 2009-03-29<br>
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
import org.mobicents.protocols.ss7.isup.message.parameter.ConnectedNumber;

/**
 * Start time:16:36:21 2009-03-29<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class ConnectedNumberImpl extends AbstractNAINumber implements ConnectedNumber {

    protected int numberingPlanIndicator;

    protected int addressRepresentationRestrictedIndicator;

    protected int screeningIndicator;

    /**
     *
     * @param representation
     * @throws ParameterException
     */
    public ConnectedNumberImpl(byte[] representation) throws ParameterException {
        super(representation);
    }

    public ConnectedNumberImpl() {
        super();

    }

    /**
     * tttttt
     *
     * @param bis
     * @throws ParameterException
     */
    public ConnectedNumberImpl(ByteArrayInputStream bis) throws ParameterException {
        super(bis);

    }

    public ConnectedNumberImpl(int natureOfAddresIndicator, String address, int numberingPlanIndicator,
            int addressRepresentationREstrictedIndicator, int screeningIndicator) {
        super(natureOfAddresIndicator, address);
        this.numberingPlanIndicator = numberingPlanIndicator;
        this.addressRepresentationRestrictedIndicator = addressRepresentationREstrictedIndicator;
        this.screeningIndicator = screeningIndicator;
    }

    public int encodeHeader(ByteArrayOutputStream bos) {
        doAddressPresentationRestricted();
        return super.encodeHeader(bos);
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.mobicents.isup.parameters.AbstractNumber#decodeBody(java.io. ByteArrayInputStream)
     */

    public int decodeBody(ByteArrayInputStream bis) throws IllegalArgumentException {
        int b = bis.read() & 0xff;

        this.numberingPlanIndicator = (b & 0x70) >> 4;
        this.addressRepresentationRestrictedIndicator = (b & 0x0c) >> 2;
        this.screeningIndicator = (b & 0x03);
        return 1;
    }

    /**
     * makes checks on APRI - see NOTE to APRI in Q.763, p 23
     */
    protected void doAddressPresentationRestricted() {

        if (this.addressRepresentationRestrictedIndicator == _APRI_NOT_AVAILABLE) {

            // NOTE 1 If the parameter is included and the address presentation
            // restricted indicator indicates
            // address not available, octets 3 to n( this are digits.) are omitted,
            // the subfields in items a - odd/evem, b -nai , c - ni and d -npi, are
            // coded with
            // 0's, and the subfield f - filler, is coded with 11.
            this.oddFlag = 0;
            this.natureOfAddresIndicator = 0;
            this.numberingPlanIndicator = 0;
            // 11
            this.screeningIndicator = _SI_NETWORK_PROVIDED;
            this.setAddress("");
        }
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.mobicents.isup.parameters.AbstractNumber#encodeBody(java.io. ByteArrayOutputStream)
     */

    public int encodeBody(ByteArrayOutputStream bos) {
        int c = this.numberingPlanIndicator << 4;

        c |= (this.addressRepresentationRestrictedIndicator << 2);
        c |= (this.screeningIndicator);
        bos.write(c & 0x7F);
        return 1;
    }

    protected boolean skipDigits() {

        if (this.addressRepresentationRestrictedIndicator == _APRI_NOT_AVAILABLE) {
            return true;
        } else {
            return false;
        }

    }

    public int getNumberingPlanIndicator() {
        return numberingPlanIndicator;
    }

    public void setNumberingPlanIndicator(int numberingPlanIndicator) {
        this.numberingPlanIndicator = numberingPlanIndicator;
    }

    public int getAddressRepresentationRestrictedIndicator() {
        return addressRepresentationRestrictedIndicator;
    }

    public void setAddressRepresentationRestrictedIndicator(int addressRepresentationREstrictedIndicator) {
        this.addressRepresentationRestrictedIndicator = addressRepresentationREstrictedIndicator;
    }

    public int getScreeningIndicator() {
        return screeningIndicator;
    }

    public void setScreeningIndicator(int screeningIndicator) {
        this.screeningIndicator = screeningIndicator;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
