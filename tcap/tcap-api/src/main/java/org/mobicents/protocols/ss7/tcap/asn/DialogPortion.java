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

/**
 *
 * @author baranowb
 * @author amit bhayani
 *
 */
public interface DialogPortion extends Encodable {

    // Dialog portion is actually type of EXTERNAL, this sucks....
    int _TAG_CLASS = Tag.CLASS_APPLICATION;
    boolean _TAG_PC_PRIMITIVE = false;
    int _TAG = 0x0B;

    /**
     * @return the dialogAPDU
     */
    DialogAPDU getDialogAPDU();

    /**
     * @param dialogAPDU the dialogAPDU to set
     */
    void setDialogAPDU(DialogAPDU dialogAPDU);

    void setUnidirectional(boolean flag);

    boolean isUnidirectional();

    // From External

    boolean isOid();

    void setOid(boolean oid);

    long[] getOidValue();

    void setOidValue(long[] oidValue);

    boolean isInteger();

    void setInteger(boolean integer);

    long getIndirectReference();

    void setIndirectReference(long indirectReference);

    boolean isObjDescriptor();

    void setObjDescriptor(boolean objDescriptor);

    boolean isAsn();

    void setAsn(boolean asn);

    byte[] getEncodeType() throws AsnException;

    void setEncodeType(byte[] data);

    boolean isOctet();

    void setOctet(boolean octet);

    boolean isArbitrary();

    void setArbitrary(boolean arbitrary);

    BitSetStrictLength getEncodeBitStringType() throws AsnException;

    void setEncodeBitStringType(BitSetStrictLength data);

}
