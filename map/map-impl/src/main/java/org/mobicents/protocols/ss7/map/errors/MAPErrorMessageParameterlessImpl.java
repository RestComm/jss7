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

package org.mobicents.protocols.ss7.map.errors;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageParameterless;

/**
 * The MAP ReturnError message without any parameters
 *
 * @author sergey vetyutnev
 *
 */
public class MAPErrorMessageParameterlessImpl extends MAPErrorMessageImpl implements MAPErrorMessageParameterless {

    public MAPErrorMessageParameterlessImpl(Long errorCode) {
        super(errorCode);
    }

    public boolean isEmParameterless() {
        return true;
    }

    public MAPErrorMessageParameterless getEmParameterless() {
        return this;
    }

    public int getTag() throws MAPException {
        throw new MAPException("MAPErrorMessageParameterless does not support encoding");
    }

    public int getTagClass() {
        return 0;
    }

    public boolean getIsPrimitive() {
        return false;
    }

    public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
    }

    public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
    }

    public void encodeAll(AsnOutputStream asnOs) throws MAPException {
    }

    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
    }

    public void encodeData(AsnOutputStream asnOs) throws MAPException {
    }

    @Override
    public String toString() {
        return "MAPErrorMessageParameterless [errorCode=" + this.errorCode + "]";
    }
}
