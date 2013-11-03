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

package org.mobicents.protocols.ss7.tcap.asn;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.BitSetStrictLength;
import org.mobicents.protocols.asn.Tag;

public interface UserInformation extends Encodable {

    int _TAG = 0x1E;
    int _TAG_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
    boolean _TAG_PC_PRIMITIVE = false;

    byte[] getEncodeType() throws AsnException;

    void setEncodeType(byte[] data);

    BitSetStrictLength getEncodeBitStringType() throws AsnException;

    void setEncodeBitStringType(BitSetStrictLength data);

    /**
     * @return the oid
     */
    boolean isOid();

    /**
     * @param oid the oid to set
     */
    void setOid(boolean oid);

    /**
     * @return the integer
     */
    boolean isInteger();

    /**
     * @param integer the integer to set
     */
    void setInteger(boolean integer);

    /**
     * @return the objDescriptor
     */
    boolean isObjDescriptor();

    /**
     * @param objDescriptor the objDescriptor to set
     */
    void setObjDescriptor(boolean objDescriptor);

    /**
     * @return the oidValue
     */
    long[] getOidValue();

    /**
     * @param oidValue the oidValue to set
     */
    void setOidValue(long[] oidValue);

    /**
     * @return the integerValue
     */
    long getIndirectReference();

    /**
     * @param integerValue the integerValue to set
     */
    void setIndirectReference(long indirectReference);

    /**
     * @return the objDescriptorValue
     */
    String getObjDescriptorValue();

    /**
     * @param objDescriptorValue the objDescriptorValue to set
     */
    void setObjDescriptorValue(String objDescriptorValue);

    /**
     * @return the asn
     */
    boolean isAsn();

    /**
     * @param asn the asn to set
     */
    void setAsn(boolean asn);

    /**
     * @return the octet
     */
    boolean isOctet();

    /**
     * @param octet the octet to set
     */
    void setOctet(boolean octet);

    /**
     * @return the arbitrary
     */
    boolean isArbitrary();

    /**
     * @param arbitrary the arbitrary to set
     */
    void setArbitrary(boolean arbitrary);

}
