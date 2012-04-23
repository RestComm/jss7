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

package org.mobicents.protocols.ss7.inap.primitives;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.inap.api.INAPException;
import org.mobicents.protocols.ss7.inap.api.INAPParsingComponentException;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public interface INAPAsnPrimitive {

	public int getTag() throws INAPException;

	public int getTagClass();

	public boolean getIsPrimitive();

	/**
	 * Decoding the length and the content of the primitive (the tag has already
	 * read)
	 * 
	 * @param ansIS
	 *            The AsnInputStream that contains the length and the content of
	 *            the primitive
	 * @throws CAPParsingComponentException
	 */
	public void decodeAll(AsnInputStream ansIS) throws INAPParsingComponentException;

	/**
	 * Decoding the content of the primitive (the tag and the length have
	 * already read)
	 * 
	 * @param ansIS
	 *            The AsnInputStream that contains the content of the primitive
	 * @param length
	 *            The length of the content
	 * @throws CAPParsingComponentException
	 */
	public void decodeData(AsnInputStream ansIS, int length) throws INAPParsingComponentException;

	/**
	 * Encoding the tag, the length and the content. Tag and tag class are universal
	 * 
	 * @param asnOs
	 * @throws CAPException
	 */
	public void encodeAll(AsnOutputStream asnOs) throws INAPException;

	/**
	 * Encoding the tag, the length and the content. Tag and tag class are defined
	 * 
	 * @param asnOs
	 * @param tagClass
	 * @param tag
	 * @throws CAPException
	 */
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws INAPException;

	/**
	 * Encoding the content.
	 * 
	 * @param asnOs
	 * @throws CAPException
	 */
	public void encodeData(AsnOutputStream asnOs) throws INAPException;


}
