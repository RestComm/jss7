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

package org.mobicents.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive;

import java.io.Serializable;

/**
*
USIInformation {PARAMETERS-BOUND : bound} ::= OCTET STRING (SIZE (
bound.&minUSIInformationLength..bound.&maxUSIInformationLength))
-- Indicates the length of the USIInformation element, maxUSIInformationlength will depend on
-- the constraints imposed by the network signalling used to transport the USI information.
-- Its content is network signalling/operator specific.
-- The internal structure of this parameter can be defined using ASN.1 and the related Basic
-- Encoding Rules (BER). In such a case the value of this paramter (after the first tag and length
-- information) is the BER encoding of the defined ASN.1 internal structure.
-- The tag of this parameter as defined by ETSI is never replaced.

*
* @author sergey vetyutnev
*
*/
public interface USIInformation extends Serializable {

    byte[] getData();

}
