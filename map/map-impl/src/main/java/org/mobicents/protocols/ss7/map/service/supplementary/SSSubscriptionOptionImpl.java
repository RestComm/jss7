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

package org.mobicents.protocols.ss7.map.service.supplementary;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSSubscriptionOption;
import org.mobicents.protocols.ss7.map.api.service.supplementary.CliRestrictionOption;
import org.mobicents.protocols.ss7.map.api.service.supplementary.OverrideCategory;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/**
 * @author daniel bichara
 * 
 */
public class SSSubscriptionOptionImpl implements SSSubscriptionOption, MAPAsnPrimitive {

	public static final String _PrimitiveName = "SSSubscriptionOption";

	private static final int _TAG_cliRestrictionOption = 2;
	private static final int _TAG_overrideCategory = 1;

	private CliRestrictionOption cliRestrictionOption = null;
	private OverrideCategory overrideCategory = null;

	public SSSubscriptionOptionImpl() {
		
	}

	/**
	 * 
	 */
	public SSSubscriptionOptionImpl(CliRestrictionOption cliRestrictionOption, 
			OverrideCategory overrideCategory) {

		this.cliRestrictionOption = cliRestrictionOption;
		this.overrideCategory = overrideCategory;
	}

	public CliRestrictionOption getCliRestrictionOption() {
		return this.cliRestrictionOption;
	}

	public OverrideCategory getOverrideCategory() {
		return this.overrideCategory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getTag()
	 */
	public int getTag() throws MAPException {
		return Tag.SEQUENCE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getTagClass()
	 */
	public int getTagClass() {
		return Tag.CLASS_UNIVERSAL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getIsPrimitive
	 * ()
	 */
	public boolean getIsPrimitive() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#decodeAll(
	 * org.mobicents.protocols.asn.AsnInputStream)
	 */
	public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
		try {
			// It is a CHOICE, ignore length
			this._decode(ansIS, 0);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#decodeData
	 * (org.mobicents.protocols.asn.AsnInputStream, int)
	 */
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
		try {
			// It is a CHOICE, ignore length
			this._decode(ansIS, 0);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	private void _decode(AsnInputStream ais, int l) throws MAPParsingComponentException, IOException, AsnException {
		this.cliRestrictionOption = null;
		this.overrideCategory = null;

		int tag = ais.readTag();
		// don't read length here. readInteger below will do it.
		
		switch(tag) {
		case _TAG_cliRestrictionOption:
			this.cliRestrictionOption = CliRestrictionOption.getInstance((int) ais.readInteger());
			return;
		case _TAG_overrideCategory:
			this.overrideCategory = OverrideCategory.getInstance((int) ais.readInteger());
			return;
		}

		throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": missing cliRestrictionOption and overrideCategory.", MAPParsingComponentExceptionReason.MistypedParameter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeAll(
	 * org.mobicents.protocols.asn.AsnOutputStream)
	 */
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {
		this.encodeAll(asnOs, this.getTagClass(), this.getTag());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeAll(
	 * org.mobicents.protocols.asn.AsnOutputStream, int, int)
	 */
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
		// It is a CHOICE, ignore tagClass and tag
		this.encodeData(asnOs);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeData
	 * (org.mobicents.protocols.asn.AsnOutputStream)
	 */
	public void encodeData(AsnOutputStream asnOs) throws MAPException {


		if (this.cliRestrictionOption == null && this.overrideCategory == null)
			throw new MAPException("Error while encoding " + _PrimitiveName + ": missing cliRestrictionOption and overrideCategory.");

		try {

			if (this.cliRestrictionOption != null) {
				asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_cliRestrictionOption, this.cliRestrictionOption.getCode());
			} else {
				asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_overrideCategory, this.overrideCategory.getCode());
			}
		} catch (IOException e) {
			throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
		}
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(_PrimitiveName + " [");

		if (this.cliRestrictionOption != null) {
			sb.append("cliRestrictionOption=");
			sb.append(this.cliRestrictionOption.toString());
			sb.append(", ");
		}

		if (this.overrideCategory != null) {
			sb.append("overrideCategory=");
			sb.append(this.overrideCategory.toString());
		}

		sb.append("]");

		return sb.toString();
	}

}
