/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
import org.mobicents.protocols.asn.Tag;

public interface UserInformation extends Encodable {

	public static final int _TAG = 0x1E;
	public static final int _TAG_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
	public static final boolean _TAG_PC_PRIMITIVE = false;

	public byte[] getEncodeType() throws AsnException;

	public void setEncodeType(byte[] data);

	public BitSet getEncodeBitStringType() throws AsnException;

	public void setEncodeBitStringType(BitSet data);


	/**
	 * @return the oid
	 */
	public boolean isOid();

	/**
	 * @param oid
	 *            the oid to set
	 */
	public void setOid(boolean oid);

	/**
	 * @return the integer
	 */
	public boolean isInteger();

	/**
	 * @param integer
	 *            the integer to set
	 */
	public void setInteger(boolean integer);

	/**
	 * @return the objDescriptor
	 */
	public boolean isObjDescriptor();

	/**
	 * @param objDescriptor
	 *            the objDescriptor to set
	 */
	public void setObjDescriptor(boolean objDescriptor);

	/**
	 * @return the oidValue
	 */
	public long[] getOidValue();

	/**
	 * @param oidValue
	 *            the oidValue to set
	 */
	public void setOidValue(long[] oidValue);

	/**
	 * @return the integerValue
	 */
	public long getIndirectReference();

	/**
	 * @param integerValue
	 *            the integerValue to set
	 */
	public void setIndirectReference(long indirectReference);

	/**
	 * @return the objDescriptorValue
	 */
	public Object getObjDescriptorValue();

	/**
	 * @param objDescriptorValue
	 *            the objDescriptorValue to set
	 */
	public void setObjDescriptorValue(Object objDescriptorValue);

	/**
	 * @return the asn
	 */
	public boolean isAsn();

	/**
	 * @param asn
	 *            the asn to set
	 */
	public void setAsn(boolean asn);

	/**
	 * @return the octet
	 */
	public boolean isOctet();

	/**
	 * @param octet
	 *            the octet to set
	 */
	public void setOctet(boolean octet);

	/**
	 * @return the arbitrary
	 */
	public boolean isArbitrary();

	/**
	 * @param arbitrary
	 *            the arbitrary to set
	 */
	public void setArbitrary(boolean arbitrary);

}
