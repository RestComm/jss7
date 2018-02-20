/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.restcomm.protocols.ss7.cap.service.sms.primitive;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.restcomm.protocols.ss7.cap.api.CAPException;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.TPValidityPeriod;
import org.restcomm.protocols.ss7.cap.primitives.OctetStringBase;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.smstpdu.AbsoluteTimeStamp;
import org.restcomm.protocols.ss7.map.api.smstpdu.ValidityPeriod;
import org.restcomm.protocols.ss7.map.smstpdu.AbsoluteTimeStampImpl;
import org.restcomm.protocols.ss7.map.smstpdu.ValidityPeriodImpl;

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
