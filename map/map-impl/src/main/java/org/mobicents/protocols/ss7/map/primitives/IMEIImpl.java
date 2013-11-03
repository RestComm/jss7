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

package org.mobicents.protocols.ss7.map.primitives;

import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class IMEIImpl extends TbcdString implements IMEI {

    public IMEIImpl() {
        // There are some fake mobiles that IMEI length != 15
        super(1, 8, "IMEI");
    }

    public IMEIImpl(String data) {
        // There are some fake mobiles that IMEI length != 15
        super(1, 8, "IMEI", data);
    }

    public String getIMEI() {
        return this.data;
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (this.data == null)
            throw new MAPException("Error while encoding the IMEI: IMEI must not be null");

        super.encodeData(asnOs);
    }
}
