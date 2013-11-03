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
 * Start time:12:38:16 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:12:38:16 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface CorrelationID extends ISUPParameter, GenericDigits {
    int _PARAMETER_CODE = 0x65;

    // TODO: CorrelationID should extends GenericNumber (not GenericDigits): from Q.1218:
    // Digits ::= OCTET STRING (SIZE (minDigitsLength .. maxDigitsLength))
    // -- Indicates the address signalling digits. Refer to the ETS 300 356-1 [8] Generic Number and
    // -- Generic Digits parameters for encoding. The coding of the subfields "NumberQualifier" in
    // -- Generic Number and "Type Of Digits" in Generic Digits are irrelevant to the INAP, the ASN.1
    // -- tags are sufficient to identify the parameter. The ISUP format does not allow to exclude these
    // -- subfields, therefor the value is network operator specific.
    // -- The following parameter should use Generic Number :
    // -- CorrelationID for AssistRequestInstructions,
    // -- AdditionalCallingPartyNumber for InitialDP,
    // -- AssistingSSPIPRoutingAddress for EstablishTemporaryConnection,
    // -- calledAddressValue for all occurrences,
    // -- callingAddressValue for all occurrences
    // -- The following parameters should use Generic Digits :
    // -- all other CorrelationID occurrences,
    // -- number VariablePart,
    // -- digitsResponse ReceivedInformationArg
}
