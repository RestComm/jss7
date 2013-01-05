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

package org.mobicents.protocols.ss7.tcap.asn;

import java.util.BitSet;

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
	public static final int _TAG_CLASS = Tag.CLASS_APPLICATION;
	public static final boolean _TAG_PC_PRIMITIVE = false;
	public static final int _TAG = 0x0B;

	/**
	 * @return the dialogAPDU
	 */
	public DialogAPDU getDialogAPDU();

	/**
	 * @param dialogAPDU
	 *            the dialogAPDU to set
	 */
	public void setDialogAPDU(DialogAPDU dialogAPDU);

	public void setUnidirectional(boolean flag);

	public boolean isUnidirectional();

	// From External

	public boolean isOid();

	public void setOid(boolean oid);

	public long[] getOidValue();

	public void setOidValue(long[] oidValue);

	public boolean isInteger();

	public void setInteger(boolean integer);

	public long getIndirectReference();

	public void setIndirectReference(long indirectReference);

	public boolean isObjDescriptor();

	public void setObjDescriptor(boolean objDescriptor);

	public boolean isAsn();

	public void setAsn(boolean asn);

	public byte[] getEncodeType() throws AsnException;

	public void setEncodeType(byte[] data);

	public boolean isOctet();

	public void setOctet(boolean octet);

	public boolean isArbitrary();

	public void setArbitrary(boolean arbitrary);

	public BitSetStrictLength getEncodeBitStringType() throws AsnException;

	public void setEncodeBitStringType(BitSetStrictLength data);

}
