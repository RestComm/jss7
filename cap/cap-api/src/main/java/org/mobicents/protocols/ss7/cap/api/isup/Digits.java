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

package org.mobicents.protocols.ss7.cap.api.isup;

import java.io.Serializable;

import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericDigits;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericNumber;

/**
 *
 ISUP GenericNumber & GenericDigits wrapper
 *
 * Digits {PARAMETERS-BOUND : bound} ::= OCTET STRING (SIZE( bound.&minDigitsLength .. bound.&maxDigitsLength)) SIZE: 2..16 --
 * Indicates the address signalling digits. -- Refer to ETSI EN 300 356-1 [23] Generic Number & Generic Digits parameters for
 * encoding. -- The coding of the subfields 'NumberQualifier' in Generic Number and 'TypeOfDigits' in -- Generic Digits are
 * irrelevant to the CAP; -- the ASN.1 tags are sufficient to identify the parameter. -- The ISUP format does not allow to
 * exclude these subfields, -- therefore the value is network operator specific. -- -- The following parameters shall use
 * Generic Number: -- - AdditionalCallingPartyNumber for InitialDP -- - AssistingSSPIPRoutingAddress for
 * EstablishTemporaryConnection -- - CorrelationID for AssistRequestInstructions -- - CalledAddressValue for all occurrences,
 * CallingAddressValue for all occurrences. -- -- The following parameters shall use Generic Digits: -- - CorrelationID in
 * EstablishTemporaryConnection -- - number in VariablePart -- - digitsResponse in ReceivedInformationArg -- - midCallEvents in
 * oMidCallSpecificInfo and tMidCallSpecificInfo -- -- In the digitsResponse and midCallevents, the digits may also include the
 * '*', '#', -- a, b, c and d digits by using the IA5 character encoding scheme. If the BCD even or -- BCD odd encoding scheme
 * is used, then the following encoding shall be applied for the -- non-decimal characters: 1011 (*), 1100 (#). -- --
 * AssistingSSPIPRoutingAddress in EstablishTemporaryConnection and CorrelationID in -- AssistRequestInstructions may contain a
 * Hex B digit as address signal. Refer to -- Annex A.6 for the usage of the Hex B digit. -- -- Note that when CorrelationID is
 * transported in Generic Digits, then the digits shall -- always be BCD encoded.
 *
 * @author sergey vetyutnev
 *
 */
public interface Digits extends Serializable {

    byte[] getData();

    GenericDigits getGenericDigits() throws CAPException;

    GenericNumber getGenericNumber() throws CAPException;

    void setData(byte[] data);

    void setGenericDigits(GenericDigits genericDigits) throws CAPException;

    void setGenericNumber(GenericNumber genericNumber) throws CAPException;

    boolean getIsGenericDigits();

    boolean getIsGenericNumber();

    /**
     * Set that Digits carries GenericDigits element Attention: this value must be set after primitive decoding !!!!
     */
    void setIsGenericDigits();

    /**
     * Set that Digits carries GenericNumber element Attention: this value must be set after primitive decoding !!!!
     */
    void setIsGenericNumber();

}