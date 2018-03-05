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

package org.restcomm.protocols.ss7.map.service.sms;

import java.io.ByteArrayInputStream;

import org.mobicents.protocols.asn.AsnOutputStream;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_SMEA;
import org.restcomm.protocols.ss7.map.api.smstpdu.AddressField;
import org.restcomm.protocols.ss7.map.primitives.OctetStringBase;
import org.restcomm.protocols.ss7.map.smstpdu.AddressFieldImpl;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SM_RP_SMEAImpl extends OctetStringBase implements SM_RP_SMEA {

    public SM_RP_SMEAImpl() {
        super(1, 12, "SM_RP_SMEA");
    }

    public SM_RP_SMEAImpl(byte[] data) {
        super(1, 12, "SM_RP_SMEA", data);
    }

    public SM_RP_SMEAImpl(AddressField addressField) throws MAPException {
        super(1, 12, "SM_RP_SMEA");

        if (addressField == null) {
            throw new MAPException("addressField field must not be equal null");
        }

        AsnOutputStream res = new AsnOutputStream();
        addressField.encodeData(res);
        this.data = res.toByteArray();
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public AddressField getAddressField() throws MAPException {

        ByteArrayInputStream stm = new ByteArrayInputStream(data);
        AddressField res = AddressFieldImpl.createMessage(stm);
        return res;
    }
}
