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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import java.io.IOException;
import java.util.ArrayList;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBearerServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtCallBarringFeature;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwFeature;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtForwFeatureImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.SSCodeImpl;

/**
 * @author daniel bichara
 * 
 */
public class ExtForwInfoImpl implements ExtForwInfo, MAPAsnPrimitive {

	public static final String _PrimitiveName = "ExtForwInfo";

	private static final int _TAG_extensionContainer = 0;

	private SSCode ssCode = null;
	private ArrayList<ExtForwFeature> forwardingFeatureList = null;
	private MAPExtensionContainer extensionContainer = null;

	public ExtForwInfoImpl() {
		
	}

	/**
	 * 
	 */
	public ExtForwInfoImpl(SSCode ssCode, ArrayList<ExtForwFeature> forwardingFeatureList,
		MAPExtensionContainer extensionContainer) {

		this.ssCode = ssCode;
		this.forwardingFeatureList = forwardingFeatureList;
		this.extensionContainer = extensionContainer;
	}

	public SSCode getSsCode() {
		return this.ssCode;
	}

	public ArrayList<ExtForwFeature> getForwardingFeatureList() {
		return this.forwardingFeatureList;
	}

	public MAPExtensionContainer getExtensionContainer() {
		return this.extensionContainer;
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
			int length = ansIS.readLength();
			this._decode(ansIS, length);
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
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
		ExtForwFeature featureItem = null;
		this.ssCode = null;
		this.forwardingFeatureList = null;
		this.extensionContainer = null;

		AsnInputStream ais = ansIS.readSequenceStreamData(length);

		int num = 0;
		while (true) {
			if (ais.available() == 0)
				break;

			int tag = ais.readTag();

			switch (num) {
			case 0:	// ssCode
				if (!ais.isTagPrimitive())
					throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".ssCode: bad tag or tag class or not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
				this.ssCode = new SSCodeImpl();
				((ExtSSStatusImpl) this.ssCode).decodeAll(ais);
				break;
			case 1:	// forwardingFeatureList
				if (ais.isTagPrimitive())
					throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
							+ ".forwardingFeatureList: Parameter is primitive", MAPParsingComponentExceptionReason.MistypedParameter);

				AsnInputStream ais2 = ais.readSequenceStream();
				while (true) {
					if (ais2.available() == 0)
						break;

					int tag2 = ais2.readTag();
					if (tag2 != Tag.SEQUENCE || ais2.getTagClass() != Tag.CLASS_UNIVERSAL || ais2.isTagPrimitive())
						throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
								+ ": bad forwardingFeatureList tag or tagClass or is primitive ", MAPParsingComponentExceptionReason.MistypedParameter);

					featureItem = new ExtForwFeatureImpl();
					((ExtForwFeatureImpl) featureItem).decodeAll(ais2);
					if (this.forwardingFeatureList == null)
						this.forwardingFeatureList = new ArrayList<ExtForwFeature>();
					this.forwardingFeatureList.add(featureItem);
				}
				break;
			default:
				switch (tag) {
				case _TAG_extensionContainer:
					this.extensionContainer = new MAPExtensionContainerImpl();
					((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
					break;
				default:
					ais.advanceElement();
					break;
				}
			}
			num++;
		}

		if (this.ssCode == null)
			throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": ssCode required.", MAPParsingComponentExceptionReason.MistypedParameter);

		if (this.forwardingFeatureList == null)
			throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": forwardingFeatureList required.", MAPParsingComponentExceptionReason.MistypedParameter);

		if (this.forwardingFeatureList.size() > 32) {
			throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": Parameter forwardingFeatureList size must be from 1 to 32, found: "
					+ this.forwardingFeatureList.size(), MAPParsingComponentExceptionReason.MistypedParameter);
		}
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
		try {
			asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeData
	 * (org.mobicents.protocols.asn.AsnOutputStream)
	 */
	public void encodeData(AsnOutputStream asnOs) throws MAPException {

		if (this.ssCode == null)
			throw new MAPException("Error while encoding " + _PrimitiveName + ": ssCode required.");

		if (this.forwardingFeatureList == null)
			throw new MAPException("Error while encoding " + _PrimitiveName + ": forwardingFeatureList required.");

		if (this.forwardingFeatureList.size() < 1 || this.forwardingFeatureList.size() > 32) {
			throw new MAPException("Error while encoding " + _PrimitiveName + ": Parameter forwardingFeatureList size must be from 1 to 32, found: "
					+ this.forwardingFeatureList.size());
		}

		try {

			((SSCodeImpl) this.ssCode).encodeAll(asnOs);

			asnOs.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
			int pos = asnOs.StartContentDefiniteLength();
			for (ExtForwFeature featureItem: this.forwardingFeatureList) {
				((ExtForwFeatureImpl) featureItem).encodeAll(asnOs);
			}
			asnOs.FinalizeContent(pos);

			if (this.extensionContainer != null)
				((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_extensionContainer);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
		}
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(_PrimitiveName + " [");

		if (this.ssCode != null) {
			sb.append("ssCode=");
			sb.append(this.ssCode.toString());
			sb.append(", ");
		}

		if (this.forwardingFeatureList != null) {
			sb.append("forwardingFeatureList=");
			sb.append(this.forwardingFeatureList.toString());
			sb.append(", ");
		}

		if (this.extensionContainer != null) {
			sb.append("extensionContainer=");
			sb.append(this.extensionContainer.toString());
		}

		sb.append("]");

		return sb.toString();
	}

}
