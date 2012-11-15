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

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGInterlock;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGSubscription;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.IntraCUGOptions;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;

/**
 * @author daniel bichara
 * 
 */
public class CUGSubscriptionImpl extends SequenceBase implements CUGSubscription, MAPAsnPrimitive {

	private static final int _TAG_extensionContainer = 0;

	private Integer cugIndex = null;
	private CUGInterlock cugInterlock = null;
	private IntraCUGOptions intraCugOptions = null;
	private ExtBasicServiceCode basicService = null;
	private MAPExtensionContainer extensionContainer = null;

	public CUGSubscriptionImpl() {
		super("CUGSubscription");
	}

	/**
	 * 
	 */
	public CUGSubscriptionImpl(Integer cugIndex, CUGInterlock cugInterlock, IntraCUGOptions intraCugOptions, ExtBasicServiceCode basicService,
			MAPExtensionContainer extensionContainer) {
		super("CUGSubscription");

		this.cugIndex = cugIndex;
		this.cugInterlock = cugInterlock;
		this.intraCugOptions = intraCugOptions;
		this.basicService = basicService;
		this.extensionContainer = extensionContainer;
	}

	public int getCUGIndex() {
		return (int)this.cugIndex;
	}

	public CUGInterlock getCugInterlock() {
		return this.cugInterlock;
	}

	// TODO: implement IntraCUGOptions
	//public IntraCUGOptions getIntraCugOptions();
	public IntraCUGOptions getIntraCugOptions() {
		return this.intraCugOptions;
	}

	public ExtBasicServiceCode getBasicServiceGroupList() {
		return this.basicService;
	}

	public MAPExtensionContainer getExtensionContainer() {
		return this.extensionContainer;
	}

	protected void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
		this.cugIndex = null;
		this.cugInterlock = null;
		this.intraCugOptions = null;
		this.basicService = null;
		this.extensionContainer = null;

		AsnInputStream ais = ansIS.readSequenceStreamData(length);

		int num = 0;
		while (true) {
			if (ais.available() == 0)
				break;

			int tag = ais.readTag();

			switch (num) {
			case 0:
				if (!ais.isTagPrimitive() || tag != Tag.INTEGER)
					throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".cugIndex: bad tag or tag class or not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
				this.cugIndex = (int) ais.readInteger();
				break;
			case 1:
				if (!ais.isTagPrimitive() || tag != Tag.STRING_OCTET)
					throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".cugInterlock: bad tag or tag class or not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
				this.cugInterlock = new CUGInterlockImpl();
				((CUGInterlockImpl) this.cugInterlock).decodeAll(ais);
				break;
			case 2:
				if (!ais.isTagPrimitive())
					throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".intraCugOptions: bad tag or tag class or not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
				int i1 = (int) ais.readInteger();
				this.intraCugOptions = IntraCUGOptions.getInstance(i1);
				break;
			default:
				if (tag == _TAG_extensionContainer) {
					this.extensionContainer = new MAPExtensionContainerImpl();
					((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
				} else {
					if (!ais.isTagPrimitive())
						throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".basicService: bad tag or tag class or not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
					this.basicService = new ExtBasicServiceCodeImpl();
					((ExtBasicServiceCodeImpl) this.basicService).decodeAll(ais);					
				}
			}
			num++;
		}

		if (this.cugIndex == null)
			throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": cugIndex required.", MAPParsingComponentExceptionReason.MistypedParameter);

		if (this.cugInterlock == null)
			throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": cugInterlock required.", MAPParsingComponentExceptionReason.MistypedParameter);

		if (this.intraCugOptions == null)
			throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": intraCugOptions required.", MAPParsingComponentExceptionReason.MistypedParameter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeData
	 * (org.mobicents.protocols.asn.AsnOutputStream)
	 */
	public void encodeData(AsnOutputStream asnOs) throws MAPException {

		if (this.cugIndex == null)
			throw new MAPException("Error while encoding " + _PrimitiveName + ": cugIndex required.");

		if (this.cugInterlock == null)
			throw new MAPException("Error while encoding " + _PrimitiveName + ": cugInterlock required.");

		if (this.intraCugOptions == null)
			throw new MAPException("Error while encoding " + _PrimitiveName + ": intraCugOptions required.");

		try {

			asnOs.writeInteger(this.cugIndex);

			((CUGInterlockImpl) this.cugInterlock).encodeAll(asnOs);

			asnOs.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, this.intraCugOptions.getCode());

			if (this.basicService != null)
				((ExtBasicServiceCodeImpl) this.basicService).encodeAll(asnOs);

			if (this.extensionContainer != null)
				((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs);
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

		if (this.cugIndex != null) {
			sb.append("cugIndex=");
			sb.append(this.cugIndex.toString());
			sb.append(", ");
		}

		if (this.cugInterlock != null) {
			sb.append("cugInterlock=");
			sb.append(this.cugInterlock.toString());
			sb.append(", ");
		}

		if (this.intraCugOptions != null) {
			sb.append("intraCugOptions=");
			sb.append(this.intraCugOptions.toString());
			sb.append(", ");
		}

		if (this.basicService != null) {
			sb.append("basicService=");
			sb.append(this.basicService.toString());
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
