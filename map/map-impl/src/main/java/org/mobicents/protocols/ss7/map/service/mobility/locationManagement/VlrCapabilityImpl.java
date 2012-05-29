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

package org.mobicents.protocols.ss7.map.service.mobility.locationManagement;

import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.IstSupportIndicator;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SuperChargerInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SupportedLCSCapabilitySets;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SupportedRATTypes;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.VlrCapability;
import org.mobicents.protocols.ss7.map.api.service.subscriberManagement.OfferedCamel4CSIs;
import org.mobicents.protocols.ss7.map.api.service.subscriberManagement.SupportedCamelPhases;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.service.subscriberManagement.OfferedCamel4CSIsImpl;
import org.mobicents.protocols.ss7.map.service.subscriberManagement.SupportedCamelPhasesImpl;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class VlrCapabilityImpl implements VlrCapability, MAPAsnPrimitive {

	public static final int _TAG_supportedCamelPhases = 0;
	public static final int _TAG_solsaSupportIndicator = 2;
	public static final int _TAG_istSupportIndicator = 1;
	public static final int _TAG_superChargerSupportedInServingNetworkEntity = 3;
	public static final int _TAG_longFTNSupported = 4;
	public static final int _TAG_supportedLCSCapabilitySets = 5;
	public static final int _TAG_offeredCamel4CSIs = 6;
	public static final int _TAG_supportedRATTypesIndicator = 7;
	public static final int _TAG_longGroupIDSupported = 8;
	public static final int _TAG_mtRoamingForwardingSupported = 9;

	public static final String _PrimitiveName = "VlrCapability";

	private SupportedCamelPhases supportedCamelPhases;
	private MAPExtensionContainer extensionContainer;
	private boolean solsaSupportIndicator;
	private IstSupportIndicator istSupportIndicator;
	private SuperChargerInfo superChargerSupportedInServingNetworkEntity;
	private boolean longFtnSupported;
	private SupportedLCSCapabilitySets supportedLCSCapabilitySets;
	private OfferedCamel4CSIs offeredCamel4CSIs;
	private SupportedRATTypes supportedRATTypesIndicator;
	private boolean longGroupIDSupported;
	private boolean mtRoamingForwardingSupported;	

	public VlrCapabilityImpl() {
	}	

	public VlrCapabilityImpl(SupportedCamelPhases supportedCamelPhases, MAPExtensionContainer extensionContainer, boolean solsaSupportIndicator,
			IstSupportIndicator istSupportIndicator, SuperChargerInfo superChargerSupportedInServingNetworkEntity, boolean longFtnSupported,
			SupportedLCSCapabilitySets supportedLCSCapabilitySets, OfferedCamel4CSIs offeredCamel4CSIs, SupportedRATTypes supportedRATTypesIndicator,
			boolean longGroupIDSupported, boolean mtRoamingForwardingSupported) {
		this.supportedCamelPhases = supportedCamelPhases;
		this.extensionContainer = extensionContainer;
		this.solsaSupportIndicator = solsaSupportIndicator;
		this.istSupportIndicator = istSupportIndicator;
		this.superChargerSupportedInServingNetworkEntity = superChargerSupportedInServingNetworkEntity;
		this.longFtnSupported = longFtnSupported;
		this.supportedLCSCapabilitySets = supportedLCSCapabilitySets;
		this.offeredCamel4CSIs = offeredCamel4CSIs;
		this.supportedRATTypesIndicator = supportedRATTypesIndicator;
		this.longGroupIDSupported = longGroupIDSupported;
		this.mtRoamingForwardingSupported = mtRoamingForwardingSupported;
	}	


	@Override
	public SupportedCamelPhases getSupportedCamelPhases() {
		return supportedCamelPhases;
	}

	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return extensionContainer;
	}

	@Override
	public boolean getSolsaSupportIndicator() {
		return solsaSupportIndicator;
	}

	@Override
	public IstSupportIndicator getIstSupportIndicator() {
		return istSupportIndicator;
	}

	@Override
	public SuperChargerInfo getSuperChargerSupportedInServingNetworkEntity() {
		return superChargerSupportedInServingNetworkEntity;
	}

	@Override
	public boolean getLongFtnSupported() {
		return longFtnSupported;
	}

	@Override
	public SupportedLCSCapabilitySets getSupportedLCSCapabilitySets() {
		return supportedLCSCapabilitySets;
	}

	@Override
	public OfferedCamel4CSIs getOfferedCamel4CSIs() {
		return offeredCamel4CSIs;
	}

	@Override
	public SupportedRATTypes getSupportedRATTypesIndicator() {
		return supportedRATTypesIndicator;
	}

	@Override
	public boolean getLongGroupIDSupported() {
		return longGroupIDSupported;
	}

	@Override
	public boolean getMtRoamingForwardingSupported() {
		return mtRoamingForwardingSupported;
	}


	@Override
	public int getTag() throws MAPException {
		return Tag.SEQUENCE;
	}

	@Override
	public int getTagClass() {
		return Tag.CLASS_UNIVERSAL;
	}

	@Override
	public boolean getIsPrimitive() {
		return false;
	}

	@Override
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

	@Override
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

		supportedCamelPhases = null;
		extensionContainer = null;
		solsaSupportIndicator = false;
		istSupportIndicator = null;
		superChargerSupportedInServingNetworkEntity = null;
		longFtnSupported = false;
		supportedLCSCapabilitySets = null;
		offeredCamel4CSIs = null;
		supportedRATTypesIndicator = null;
		longGroupIDSupported = false;
		mtRoamingForwardingSupported = false;

		AsnInputStream ais = ansIS.readSequenceStreamData(length);
		while (true) {
			if (ais.available() == 0)
				break;

			int tag = ais.readTag();

			if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
				switch (tag) {
				case _TAG_supportedCamelPhases: // supportedCamelPhases
					if (!ais.isTagPrimitive())
						throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".supportedCamelPhases: Parameter is not primitive",
								MAPParsingComponentExceptionReason.MistypedParameter);
					this.supportedCamelPhases = new SupportedCamelPhasesImpl();
					((SupportedCamelPhasesImpl) this.supportedCamelPhases).decodeAll(ais);
					break;
				case _TAG_solsaSupportIndicator:
					// solsaSupportIndicator
					if (!ais.isTagPrimitive())
						throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
								+ ".solsaSupportIndicator: Parameter is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
					ais.readNull();
					this.solsaSupportIndicator = true;
					break;
				case _TAG_istSupportIndicator:
					// istSupportIndicator
					if (!ais.isTagPrimitive())
						throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
								+ ".istSupportIndicator: Parameter is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
					int i1 = (int) ais.readInteger();
					this.istSupportIndicator = IstSupportIndicator.getInstance(i1);
					break;
				case _TAG_superChargerSupportedInServingNetworkEntity: // superChargerSupportedInServingNetworkEntity
					if (ais.isTagPrimitive())
						throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".superChargerSupportedInServingNetworkEntity: Parameter is primitive",
								MAPParsingComponentExceptionReason.MistypedParameter);
					AsnInputStream ais2 = ais.readSequenceStream();
					ais2.readTag();
					this.superChargerSupportedInServingNetworkEntity = new SuperChargerInfoImpl();
					((SuperChargerInfoImpl)this.superChargerSupportedInServingNetworkEntity).decodeAll(ais2);
					break;
				case _TAG_longFTNSupported:
					// longFtnSupported
					if (!ais.isTagPrimitive())
						throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
								+ ".longFTNSupported: Parameter is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
					ais.readNull();
					this.longFtnSupported = true;
					break;
				case _TAG_supportedLCSCapabilitySets: // supportedLCSCapabilitySets
					if (!ais.isTagPrimitive())
						throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".supportedLCSCapabilitySets: Parameter is not primitive",
								MAPParsingComponentExceptionReason.MistypedParameter);
					this.supportedLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl();
					((SupportedLCSCapabilitySetsImpl) this.supportedLCSCapabilitySets).decodeAll(ais);
					break;
				case _TAG_offeredCamel4CSIs: // offeredCamel4CSIs
					if (!ais.isTagPrimitive())
						throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".offeredCamel4CSIs: Parameter is not primitive",
								MAPParsingComponentExceptionReason.MistypedParameter);
					this.offeredCamel4CSIs = new OfferedCamel4CSIsImpl();
					((OfferedCamel4CSIsImpl) this.offeredCamel4CSIs).decodeAll(ais);
					break;
				case _TAG_supportedRATTypesIndicator: // supportedRATTypesIndicator
					if (!ais.isTagPrimitive())
						throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".supportedRATTypesIndicator: Parameter is not primitive",
								MAPParsingComponentExceptionReason.MistypedParameter);
					// TODO: implement it
					ais.advanceElement();
					break;
				case _TAG_longGroupIDSupported:
					// longGroupIDSupported
					if (!ais.isTagPrimitive())
						throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
								+ ".longGroupIDSupported: Parameter is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
					ais.readNull();
					this.longGroupIDSupported = true;
					break;
				case _TAG_mtRoamingForwardingSupported:
					// mtRoamingForwardingSupported
					if (!ais.isTagPrimitive())
						throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
								+ ".longFTNSupported: Parameter is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
					ais.readNull();
					this.mtRoamingForwardingSupported = true;
					break;

				default:
					ais.advanceElement();
					break;
				}
			} else if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {

				switch (tag) {
				case Tag.SEQUENCE:
					// extensionContainer
					if (ais.isTagPrimitive())
						throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
								+ ".extensionContainer: Parameter extensionContainer is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
					this.extensionContainer = new MAPExtensionContainerImpl();
					((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
					break;
				}
			} else {

				ais.advanceElement();
			}
		}
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {
		// TODO Auto-generated method stub
		
	}
}

