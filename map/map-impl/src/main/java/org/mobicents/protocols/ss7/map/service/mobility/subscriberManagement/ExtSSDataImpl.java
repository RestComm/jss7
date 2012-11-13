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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatus;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSSubscriptionOption;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtSSStatusImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.SSCodeImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.SSSubscriptionOptionImpl;

/**
 * @author daniel bichara
 * 
 */
public class ExtSSDataImpl implements ExtSSData, MAPAsnPrimitive {

	public static final String _PrimitiveName = "ExtSSData";

	private static final int _TAG_ss_Status = 4;
	private static final int _TAG_extensionContainer = 5;

	private SSCode ssCode = null;
	private ExtSSStatus ssStatus = null;
	private SSSubscriptionOption ssSubscriptionOption = null;
	private ArrayList<ExtBasicServiceCode> basicServiceGroupList = null;
	private MAPExtensionContainer extensionContainer = null;

	public ExtSSDataImpl() {
		
	}

	/**
	 * 
	 */
	public ExtSSDataImpl(SSCode ssCode, ExtSSStatus ssStatus,
			SSSubscriptionOption ssSubscriptionOption, ArrayList<ExtBasicServiceCode> basicServiceGroupList,
		MAPExtensionContainer extensionContainer) {

		this.ssCode = ssCode;
		this.ssStatus = ssStatus;
		this.ssSubscriptionOption = ssSubscriptionOption;
		this.basicServiceGroupList = basicServiceGroupList;
		this.extensionContainer = extensionContainer;
	}

	public SSCode getSsCode() {
		return this.ssCode;
	}

	public ExtSSStatus getSsStatus() {
		return this.ssStatus;
	}

	public SSSubscriptionOption getSSSubscriptionOption() {
		return this.ssSubscriptionOption;
	}

	public ArrayList<ExtBasicServiceCode> getBasicServiceGroupList() {
		return this.basicServiceGroupList;
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
		ExtBasicServiceCode serviceItem = null;
		this.ssCode = null;
		this.ssStatus = null;
		this.ssSubscriptionOption = null;
		this.basicServiceGroupList = null;
		this.extensionContainer = null;

		AsnInputStream ais = ansIS.readSequenceStreamData(length);

		int num = 0;
		while (true) {
			if (ais.available() == 0)
				break;

			int tag = ais.readTag();

			switch (tag) {
			case _TAG_ss_Status:
				if (!ais.isTagPrimitive())
					throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".ssStatus: bad tag or tag class or not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
				this.ssStatus = new ExtSSStatusImpl();
				((ExtSSStatusImpl) this.ssStatus).decodeAll(ais);
				break;
			case _TAG_extensionContainer:
				this.extensionContainer = new MAPExtensionContainerImpl();
				((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
				break;
			default:
				if (num == 0) {
					if (!ais.isTagPrimitive())
						throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".ssCode: bad tag or tag class or not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
					this.ssCode = new SSCodeImpl();
					((SSCodeImpl) this.ssCode).decodeAll(ais);
				} else {
					if(tag == Tag.BOOLEAN) {	// ssSubscriptionOption
						this.ssSubscriptionOption = new SSSubscriptionOptionImpl();
						((SSSubscriptionOptionImpl) this.ssSubscriptionOption).decodeAll(ais);

					} else {	// basicServiceGroupList
						AsnInputStream ais2 = ais.readSequenceStream();
						while (true) {
							if (ais2.available() == 0)
								break;

							int tag2 = ais2.readTag();
							if (tag2 != Tag.SEQUENCE || ais2.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais2.isTagPrimitive())
								throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
										+ ": bad basicServiceGroupList tag or tagClass or is primitive ", MAPParsingComponentExceptionReason.MistypedParameter);

							serviceItem = new ExtBasicServiceCodeImpl();
							((ExtBasicServiceCodeImpl) serviceItem).decodeAll(ais2);
							if (this.basicServiceGroupList == null)
								this.basicServiceGroupList = new ArrayList<ExtBasicServiceCode>();
							this.basicServiceGroupList.add(serviceItem);
						}
					}
				}
			}
			num++;
		}

		if (this.ssCode == null)
			throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": ssCode required.", MAPParsingComponentExceptionReason.MistypedParameter);

		if (this.ssStatus == null)
			throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": ssStatus required.", MAPParsingComponentExceptionReason.MistypedParameter);

		if (this.basicServiceGroupList != null && this.basicServiceGroupList.size() > 32) {
			throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": Parameter basicServiceGroupList size must be from 1 to 32, found: "
					+ this.basicServiceGroupList.size(), MAPParsingComponentExceptionReason.MistypedParameter);
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

		if (this.ssStatus == null)
			throw new MAPException("Error while encoding " + _PrimitiveName + ": ssStatus required.");

		if (this.basicServiceGroupList != null && this.basicServiceGroupList.size() > 32) {
			throw new MAPException("Error while encoding " + _PrimitiveName + ": Parameter basicServiceGroupList size must be from 1 to 32, found: "
					+ this.basicServiceGroupList.size());
		}

		try {

			((SSCodeImpl) this.ssCode).encodeAll(asnOs);

			((ExtSSStatusImpl) this.ssStatus).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_ss_Status);

			if (this.ssSubscriptionOption != null) {
				((SSSubscriptionOptionImpl) this.ssSubscriptionOption).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, Tag.BOOLEAN);
			}

			if (this.basicServiceGroupList != null) {
				asnOs.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
				int pos = asnOs.StartContentDefiniteLength();
				for (ExtBasicServiceCode serviceItem: this.basicServiceGroupList) {
					((ExtBasicServiceCodeImpl) serviceItem).encodeAll(asnOs);
				}
				asnOs.FinalizeContent(pos);
			}

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

		if (this.ssStatus != null) {
			sb.append("ssStatus=");
			sb.append(this.ssStatus.toString());
			sb.append(", ");
		}

		if (this.ssSubscriptionOption != null) {
			sb.append("ssSubscriptionOption=");
			sb.append(this.ssSubscriptionOption.toString());
			sb.append(", ");
		}

		if (this.basicServiceGroupList != null) {
			sb.append("basicServiceGroupList=");
			sb.append(this.basicServiceGroupList.toString());
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
