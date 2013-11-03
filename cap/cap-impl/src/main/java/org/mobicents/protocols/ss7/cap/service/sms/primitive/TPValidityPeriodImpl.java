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

package org.mobicents.protocols.ss7.cap.service.sms.primitive;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.TPValidityPeriod;
import org.mobicents.protocols.ss7.cap.primitives.OctetStringBase;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.smstpdu.AbsoluteTimeStamp;
import org.mobicents.protocols.ss7.map.api.smstpdu.ValidityPeriod;
import org.mobicents.protocols.ss7.map.smstpdu.AbsoluteTimeStampImpl;
import org.mobicents.protocols.ss7.map.smstpdu.ValidityPeriodImpl;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class TPValidityPeriodImpl extends OctetStringBase implements TPValidityPeriod {

    public TPValidityPeriodImpl() {
        super(1, 7, "TPValidityPeriod");
    }

    public TPValidityPeriodImpl(byte[] data) {
        super(1, 7, "TPValidityPeriod", data);
    }

    public TPValidityPeriodImpl(int relativeFormat) {
        super(1, 7, "TPValidityPeriod", new byte[] { (byte) relativeFormat });
    }

    public TPValidityPeriodImpl(AbsoluteTimeStamp absoluteFormatValue) {
        super(1, 7, "TPValidityPeriod");

        ByteArrayOutputStream res = new ByteArrayOutputStream();
        try {
            absoluteFormatValue.encodeData(res);
        } catch (MAPException e) {
            // This can not occur
        }
        this.data = res.toByteArray();
    }

    @Override
    public byte[] getData() {
        return data;
    }

    @Override
    public ValidityPeriod getValidityPeriod() throws CAPException {
        if (this.data == null)
            throw new CAPException("Error when getting ValidityPeriod: data must not be null");

        // we support RelativeFormat and AbsoluteFormat
        if (this.data.length != 1 && this.data.length != 7)
            throw new CAPException("Error when getting ValidityPeriod: data must has length 1 or 7, found " + data.length);

        ValidityPeriodImpl vp;
        if (this.data.length == 1) {
            vp = new ValidityPeriodImpl(this.data[0]);
        } else {
            AbsoluteTimeStamp absoluteFormatValue;
            try {
                InputStream stm = new ByteArrayInputStream(data);
                absoluteFormatValue = AbsoluteTimeStampImpl.createMessage(stm);
            } catch (MAPException e) {
                throw new CAPException("MAPException when AbsoluteTimeStampImpl creating: " + e.getMessage(), e);
            }
            vp = new ValidityPeriodImpl(absoluteFormatValue);
        }
        return vp;
    }

    @Override
    public String toString() {

        try {
            ValidityPeriod vp = this.getValidityPeriod();
            return _PrimitiveName + " [" + vp + "]";
        } catch (CAPException e) {
            return super.toString();
        }
    }

}
