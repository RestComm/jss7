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

package org.mobicents.protocols.ss7.map.api.primitives;

import java.io.Serializable;
import java.nio.charset.Charset;

import org.mobicents.protocols.ss7.map.api.MAPException;

/**
 * USSD-String ::= OCTET STRING (SIZE (1..maxUSSD-StringLength)) -- The structure of the contents of the USSD-String is
 * dependent -- on the USSD-DataCodingScheme as described in TS 3GPP TS 23.038 [25].
 *
 * maxUSSD-StringLength INTEGER ::= 160
 *
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public interface USSDString extends Serializable {

    /**
     * Get the byte[] that represents encoded USSD String
     *
     * @return
     */
    byte[] getEncodedString();

    /**
     * Get the decoded USSD String
     *
     * @return
     */
    String getString(Charset gsm8Charset) throws MAPException;

}
