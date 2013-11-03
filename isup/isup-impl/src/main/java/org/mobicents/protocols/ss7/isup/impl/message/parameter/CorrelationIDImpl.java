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
 * Start time:12:48:19 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.CorrelationID;

/**
 * Start time:12:48:19 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 *
 */
public class CorrelationIDImpl extends GenericDigitsImpl implements CorrelationID {

    public CorrelationIDImpl() {
        super();

    }

    public CorrelationIDImpl(byte[] b) throws ParameterException {
        super(b);

    }

    public CorrelationIDImpl(int encodignScheme, int typeOfDigits, byte[] digits) {
        super(encodignScheme, typeOfDigits, digits);

    }

    // FIXME: Q.1218 -- weird document.... Oleg is this correct? or should it be
    // mix of GenericNumber and Generic digits?

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

    public int getCode() {

        return CorrelationID._PARAMETER_CODE;
    }
}
