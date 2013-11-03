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

package org.mobicents.protocols.ss7.cap.api.service.sms.primitive;

import java.io.Serializable;

/**
 *
 RPCause ::= OCTET STRING (SIZE (1)) -- RP cause according to 3GPP TS 24.011 [10] or 3GPP TS 29.002 [11]. -- GsmSCF shall send
 * this cause in the ReleaseSMS operation. -- For a MO-SMS service, the MSC or SGSN shall send the RP Cause to the originating
 * MS. -- It shall be used to overwrite the RP-Cause element in the RP-ERROR RPDU. -- For a MT-SMS service, the MSC or SGSN
 * shall send the RP Cause to the sending SMS-GMSC. -- It shall be used to overwrite the RP-Cause element in the RP-ERROR RPDU.
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface RPCause extends Serializable {

    int getData();

}