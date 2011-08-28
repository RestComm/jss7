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

package org.mobicents.protocols.ss7.map.service.lsm;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.lsm.AdditionalNumber;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.SupportedLCSCapabilitySets;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.LMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

/**
 * TODO : Add unit test for this
 * @author amit bhayani
 * 
 */
public class LCSLocationInfoImpl implements LCSLocationInfo, MAPAsnPrimitive {
	
	private static final int _TAG_LMSI = 0;
	private static final int _TAG_EXTENSION_CONTAINER = 1;
	private static final int _TAG_GPRS_NODE_IND = 2;
	private static final int _TAG_ADDITIONAL_NUMBER = 3;
	private static final int _TAG_SUPPORTED_LCS_CAPBILITY_SET = 4;
	private static final int _TAG_ADDITIONAL_LCS_CAPBILITY_SET = 5;

	private ISDNAddressString networkNodeNumber = null;
	private LMSI lmsi = null;
	private MAPExtensionContainer extensionContainer = null;
	private Boolean gprsNodeIndicator = null;
	private AdditionalNumber additionalNumber = null;
	private SupportedLCSCapabilitySets supportedLCSCapabilitySets = null;
	private SupportedLCSCapabilitySets additionalLCSCapabilitySets = null;

	/**
	 * 
	 */
	public LCSLocationInfoImpl() {
		super();
	}
	
	/**
	 * @param networkNodeNumber
	 * @param lmsi
	 * @param extensionContainer
	 * @param gprsNodeIndicator
	 * @param additionalNumber
	 * @param supportedLCSCapabilitySets
	 * @param additionalLCSCapabilitySets
	 */
	public LCSLocationInfoImpl(ISDNAddressString networkNodeNumber, LMSI lmsi, MAPExtensionContainer extensionContainer, Boolean gprsNodeIndicator,
			AdditionalNumber additionalNumber, SupportedLCSCapabilitySets supportedLCSCapabilitySets, SupportedLCSCapabilitySets additionalLCSCapabilitySets) {
		super();
		this.networkNodeNumber = networkNodeNumber;
		this.lmsi = lmsi;
		this.extensionContainer = extensionContainer;
		this.gprsNodeIndicator = gprsNodeIndicator;
		this.additionalNumber = additionalNumber;
		this.supportedLCSCapabilitySets = supportedLCSCapabilitySets;
		this.additionalLCSCapabilitySets = additionalLCSCapabilitySets;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo#
	 * getNetworkNodeNumber()
	 */
	@Override
	public ISDNAddressString getNetworkNodeNumber() {
		return this.networkNodeNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo#getLMSI()
	 */
	@Override
	public LMSI getLMSI() {
		return this.lmsi;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo#
	 * getExtensionContainer()
	 */
	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return this.extensionContainer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo#
	 * getGprsNodeIndicator()
	 */
	@Override
	public Boolean getGprsNodeIndicator() {
		return this.gprsNodeIndicator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo#
	 * getAdditionalNumber()
	 */
	@Override
	public AdditionalNumber getAdditionalNumber() {
		return this.additionalNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo#
	 * getSupportedLCSCapabilitySets()
	 */
	@Override
	public SupportedLCSCapabilitySets getSupportedLCSCapabilitySets() {
		return this.supportedLCSCapabilitySets;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo#
	 * getadditionalLCSCapabilitySets()
	 */
	@Override
	public SupportedLCSCapabilitySets getAdditionalLCSCapabilitySets() {
		return this.additionalLCSCapabilitySets;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getTag()
	 */
	@Override
	public int getTag() throws MAPException {
		return Tag.SEQUENCE;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getTagClass()
	 */
	@Override
	public int getTagClass() {
		return Tag.CLASS_UNIVERSAL;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getIsPrimitive()
	 */
	@Override
	public boolean getIsPrimitive() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#decodeAll(org.mobicents.protocols.asn.AsnInputStream)
	 */
	@Override
	public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
		try {
			int length = ansIS.readLength();
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding SM_RP_DA: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding SM_RP_DA: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#decodeData(org.mobicents.protocols.asn.AsnInputStream, int)
	 */
	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding SM_RP_DA: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding SM_RP_DA: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}		
	}
	
	private void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {
		
		AsnInputStream ais = asnIS.readSequenceStreamData(length);
		
		int tag = ais.readTag();
		
		if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.STRING_OCTET) {
			throw new MAPParsingComponentException(
					"Error while decoding LCSLocationInfo: Parameter [networkNode-Number ISDN-AddressString] bad tag class, tag or not primitive",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
		
		this.networkNodeNumber = new ISDNAddressStringImpl();
		((ISDNAddressStringImpl)this.networkNodeNumber).decodeAll(ais);
		
		while(true){
			if (ais.available() == 0)
				break;
			
			tag = ais.readTag();
			switch(tag){
			case _TAG_LMSI:
				// lmsi [0] LMSI OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding LCSLocationInfo: Parameter [lmsi [0] LMSI ] bad tag class, tag or not primitive",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.lmsi = new LMSIImpl();
				((LMSIImpl)this.lmsi).decodeAll(ais);
				
				break;
			case _TAG_EXTENSION_CONTAINER:
				// extensionContainer [1] ExtensionContainer
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding LCSLocationInfo: Parameter [extensionContainer [1] ExtensionContainer ] bad tag class, tag or not primitive",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.extensionContainer = new MAPExtensionContainerImpl();
				((MAPExtensionContainerImpl)this.extensionContainer).decodeAll(ais);
				break;
			case _TAG_GPRS_NODE_IND:
				// gprsNodeIndicator [2] NULL
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding LCSLocationInfo: Parameter [gprsNodeIndicator [2] NULL ] bad tag class, tag or not primitive",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.gprsNodeIndicator = true;
				break;
			case _TAG_ADDITIONAL_NUMBER:
				// additional-Number [3] Additional-Number OPTIONAL
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding LCSLocationInfo: Parameter [additional-Number [3] Additional-Number] bad tag class, tag or not primitive",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				int lenhth1 = ais.readLength();
				
				tag = ais.readTag();
				
				this.additionalNumber = new AdditionalNumberImpl();
				((AdditionalNumberImpl)this.additionalNumber).decodeAll(ais);
				break;
			case _TAG_SUPPORTED_LCS_CAPBILITY_SET:
				// supportedLCS-CapabilitySets [4] SupportedLCS-CapabilitySets
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding LCSLocationInfo: Parameter [supportedLCS-CapabilitySets [4] SupportedLCS-CapabilitySets] bad tag class, tag or not primitive",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.supportedLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl();
				((SupportedLCSCapabilitySetsImpl)this.supportedLCSCapabilitySets).decodeAll(ais);
				break;
			case _TAG_ADDITIONAL_LCS_CAPBILITY_SET:
				//additional-LCS-CapabilitySets [5] SupportedLCS-CapabilitySets OPTIONAL
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding LCSLocationInfo: Parameter [additional-LCS-CapabilitySets [5] SupportedLCS-CapabilitySets] bad tag class, tag or not primitive",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.additionalLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl();
				((SupportedLCSCapabilitySetsImpl)this.additionalLCSCapabilitySets).decodeAll(ais);
				break;
			default:
//				throw new MAPParsingComponentException("Error while decoding LCSLocationInfo: Expected tags 0 - 5 but found" + p.getTag(),
//						MAPParsingComponentExceptionReason.MistypedParameter);
			}
		}

	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeAll(org.mobicents.protocols.asn.AsnOutputStream)
	 */
	@Override
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {
		this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.SEQUENCE);		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeAll(org.mobicents.protocols.asn.AsnOutputStream, int, int)
	 */
	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
		try {
			asnOs.writeTag(tagClass, false, tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding InformServiceCentreRequest: " + e.getMessage(), e);
		}		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeData(org.mobicents.protocols.asn.AsnOutputStream)
	 */
	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {

		if (this.networkNodeNumber == null) {
			throw new MAPException("Error while encoding LCSLocationInfo the mandatory parameter networkNode-Number ISDN-AddressString is not defined");
		}
		
		((ISDNAddressStringImpl)this.networkNodeNumber).encodeAll(asnOs);
		
		if (this.lmsi != null) {
			// lmsi [0] LMSI OPTIONAL,
			((LMSIImpl)this.lmsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LMSI);
		}
		
		if (this.extensionContainer != null) {
			// extensionContainer [1] ExtensionContainer OPTIONAL,
			((MAPExtensionContainerImpl)this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_EXTENSION_CONTAINER);
		}
		
		if (this.gprsNodeIndicator != null && this.gprsNodeIndicator) {
			// gprsNodeIndicator [2] NULL OPTIONAL,
			try {
				asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_GPRS_NODE_IND);
			} catch (IOException e) {
				throw new MAPException("Error while encoding LCSLocationInfo the optional parameter gprsNodeIndicator encoding failed ", e);
			} catch (AsnException e) {
				throw new MAPException("Error while encoding LCSLocationInfo the optional parameter gprsNodeIndicator encoding failed ", e);
			}
		}
		
		if (this.additionalNumber != null) {
			// additional-Number [3] Additional-Number OPTIONAL,
			try {
				asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_ADDITIONAL_NUMBER);
			} catch (AsnException e) {
				throw new MAPException("AsnException while encoding parameter additional-Number");
			}

			int pos = asnOs.StartContentDefiniteLength();
			((AdditionalNumberImpl)this.additionalNumber).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, ((AdditionalNumberImpl)this.additionalNumber).getTag());
			asnOs.FinalizeContent(pos);
		}
		
		if (this.supportedLCSCapabilitySets != null) {
			// supportedLCS-CapabilitySets [4] SupportedLCS-CapabilitySets
			// OPTIONAL,
			((SupportedLCSCapabilitySetsImpl)this.supportedLCSCapabilitySets).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_SUPPORTED_LCS_CAPBILITY_SET);
		}
		
		if (this.additionalLCSCapabilitySets != null) {
			// additional-LCS-CapabilitySets [5] SupportedLCS-CapabilitySets
			// OPTIONAL
			((SupportedLCSCapabilitySetsImpl)this.additionalLCSCapabilitySets).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_ADDITIONAL_LCS_CAPBILITY_SET);
		}
		
	}


}
