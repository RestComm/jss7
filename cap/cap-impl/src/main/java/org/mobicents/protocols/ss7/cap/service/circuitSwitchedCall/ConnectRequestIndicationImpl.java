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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall;

import java.io.IOException;
import java.util.ArrayList;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.isup.GenericNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.LocationNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.OriginalCalledNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.RedirectingPartyIDCap;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ConnectRequestIndication;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AlertingPatternCap;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.Carrier;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.DestinationRoutingAddress;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.NAOliInfo;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwo;
import org.mobicents.protocols.ss7.cap.isup.GenericNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.OriginalCalledNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.RedirectingPartyIDCapImpl;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.AlertingPatternCapImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.DestinationRoutingAddressImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.NAOliInfoImpl;
import org.mobicents.protocols.ss7.inap.api.INAPException;
import org.mobicents.protocols.ss7.inap.api.INAPParsingComponentException;
import org.mobicents.protocols.ss7.inap.api.isup.CallingPartysCategoryInap;
import org.mobicents.protocols.ss7.inap.api.isup.RedirectionInformationInap;
import org.mobicents.protocols.ss7.inap.api.primitives.LegID;
import org.mobicents.protocols.ss7.inap.isup.CallingPartysCategoryInapImpl;
import org.mobicents.protocols.ss7.inap.isup.RedirectionInformationInapImpl;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.service.subscriberManagement.CUGInterlock;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class ConnectRequestIndicationImpl extends CircuitSwitchedCallMessageImpl implements ConnectRequestIndication {

	public static final int _ID_destinationRoutingAddress = 0;
	public static final int _ID_alertingPattern = 1;
	public static final int _ID_originalCalledPartyID = 6;
	public static final int _ID_extensions = 10;
	public static final int _ID_carrier = 11;
	public static final int _ID_callingPartysCategory = 28;
	public static final int _ID_redirectingPartyID = 29;
	public static final int _ID_redirectionInformation = 30;
	public static final int _ID_genericNumbers = 14;
	public static final int _ID_serviceInteractionIndicatorsTwo = 15;
	public static final int _ID_chargeNumber = 19;
	public static final int _ID_legToBeConnected = 21;
	public static final int _ID_cug_Interlock = 31;
	public static final int _ID_cug_OutgoingAccess = 32;
	public static final int _ID_suppressionOfAnnouncement = 55;
	public static final int _ID_oCSIApplicable = 56;
	public static final int _ID_naOliInfo = 57;
	public static final int _ID_bor_InterrogationRequested = 58;

	public static final String _PrimitiveName = "ConnectRequestIndication";
	
	private DestinationRoutingAddress destinationRoutingAddress;
	private AlertingPatternCap alertingPattern;
	private OriginalCalledNumberCap originalCalledPartyID;
	private CAPExtensions extensions;
	private Carrier carrier;
	private CallingPartysCategoryInap callingPartysCategory;
	private RedirectingPartyIDCap redirectingPartyID;
	private RedirectionInformationInap redirectionInformation;
	private ArrayList<GenericNumberCap> genericNumbers;
	private ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo;
	private LocationNumberCap chargeNumber;
	private LegID legToBeConnected;
	private CUGInterlock cugInterlock;
	private boolean cugOutgoingAccess;
	private boolean suppressionOfAnnouncement;
	private boolean ocsIApplicable;
	private NAOliInfo naoliInfo;
	private boolean borInterrogationRequested;

	
	public ConnectRequestIndicationImpl() {
	}
	
	public ConnectRequestIndicationImpl(DestinationRoutingAddress destinationRoutingAddress, AlertingPatternCap alertingPattern,
			OriginalCalledNumberCap originalCalledPartyID, CAPExtensions extensions, Carrier carrier, CallingPartysCategoryInap callingPartysCategory,
			RedirectingPartyIDCap redirectingPartyID, RedirectionInformationInap redirectionInformation, ArrayList<GenericNumberCap> genericNumbers,
			ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, LocationNumberCap chargeNumber, LegID legToBeConnected, CUGInterlock cugInterlock,
			boolean cugOutgoingAccess, boolean suppressionOfAnnouncement, boolean ocsIApplicable, NAOliInfo naoliInfo, boolean borInterrogationRequested) {
		this.destinationRoutingAddress = destinationRoutingAddress;
		this.alertingPattern = alertingPattern;
		this.originalCalledPartyID = originalCalledPartyID;
		this.extensions = extensions;
		this.carrier = carrier;
		this.callingPartysCategory = callingPartysCategory;
		this.redirectingPartyID = redirectingPartyID;
		this.redirectionInformation = redirectionInformation;
		this.genericNumbers = genericNumbers;
		this.serviceInteractionIndicatorsTwo = serviceInteractionIndicatorsTwo;
		this.chargeNumber = chargeNumber;
		this.legToBeConnected = legToBeConnected;
		this.cugInterlock = cugInterlock;
		this.cugOutgoingAccess = cugOutgoingAccess;
		this.suppressionOfAnnouncement = suppressionOfAnnouncement;
		this.ocsIApplicable = ocsIApplicable;
		this.naoliInfo = naoliInfo;
		this.borInterrogationRequested = borInterrogationRequested;
	}

	@Override
	public DestinationRoutingAddress getDestinationRoutingAddress() {
		return destinationRoutingAddress;
	}

	@Override
	public AlertingPatternCap getAlertingPattern() {
		return alertingPattern;
	}

	@Override
	public OriginalCalledNumberCap getOriginalCalledPartyID() {
		return originalCalledPartyID;
	}

	@Override
	public CAPExtensions getExtensions() {
		return extensions;
	}

	@Override
	public Carrier getCarrier() {
		return carrier;
	}

	@Override
	public CallingPartysCategoryInap getCallingPartysCategory() {
		return callingPartysCategory;
	}

	@Override
	public RedirectingPartyIDCap getRedirectingPartyID() {
		return redirectingPartyID;
	}

	@Override
	public RedirectionInformationInap getRedirectionInformation() {
		return redirectionInformation;
	}

	@Override
	public ArrayList<GenericNumberCap> getGenericNumbers() {
		return genericNumbers;
	}

	@Override
	public ServiceInteractionIndicatorsTwo getServiceInteractionIndicatorsTwo() {
		return serviceInteractionIndicatorsTwo;
	}

	@Override
	public LocationNumberCap getChargeNumber() {
		return chargeNumber;
	}

	@Override
	public LegID getLegToBeConnected() {
		return legToBeConnected;
	}

	@Override
	public CUGInterlock getCUGInterlock() {
		return cugInterlock;
	}

	@Override
	public boolean getCugOutgoingAccess() {
		return cugOutgoingAccess;
	}

	@Override
	public boolean getSuppressionOfAnnouncement() {
		return suppressionOfAnnouncement;
	}

	@Override
	public boolean getOCSIApplicable() {
		return ocsIApplicable;
	}

	@Override
	public NAOliInfo getNAOliInfo() {
		return naoliInfo;
	}

	@Override
	public boolean getBorInterrogationRequested() {
		return borInterrogationRequested;
	}
	
	
	@Override
	public int getTag() throws CAPException {
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
	public void decodeAll(AsnInputStream ansIS) throws CAPParsingComponentException {

		try {
			int length = ansIS.readLength();
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new CAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					CAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new CAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					CAPParsingComponentExceptionReason.MistypedParameter);
		} catch (MAPParsingComponentException e) {
			throw new CAPParsingComponentException("MAPParsingComponentException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					CAPParsingComponentExceptionReason.MistypedParameter);
		} catch (INAPParsingComponentException e) {
			throw new CAPParsingComponentException("INAPParsingComponentException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					CAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws CAPParsingComponentException {

		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new CAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					CAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new CAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					CAPParsingComponentExceptionReason.MistypedParameter);
		} catch (MAPParsingComponentException e) {
			throw new CAPParsingComponentException("MAPParsingComponentException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					CAPParsingComponentExceptionReason.MistypedParameter);
		} catch (INAPParsingComponentException e) {
			throw new CAPParsingComponentException("INAPParsingComponentException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					CAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	private void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException, MAPParsingComponentException, INAPParsingComponentException,
			IOException, AsnException {

		this.destinationRoutingAddress = null;
		this.alertingPattern = null;
		this.originalCalledPartyID = null;
		this.extensions = null;
		this.carrier = null;
		this.callingPartysCategory = null;
		this.redirectingPartyID = null;
		this.redirectionInformation = null;
		this.genericNumbers = null;
		this.serviceInteractionIndicatorsTwo = null;
		this.chargeNumber = null;
		this.legToBeConnected = null;
		this.cugInterlock = null;
		this.cugOutgoingAccess = false;
		this.suppressionOfAnnouncement = false;
		this.ocsIApplicable = false;
		this.naoliInfo = null;
		this.borInterrogationRequested = false;

		AsnInputStream ais = ansIS.readSequenceStreamData(length);
		while (true) {
			if (ais.available() == 0)
				break;

			int tag = ais.readTag();

			if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
				switch (tag) {
				case _ID_destinationRoutingAddress:
					this.destinationRoutingAddress = new DestinationRoutingAddressImpl();
					((DestinationRoutingAddressImpl) this.destinationRoutingAddress).decodeAll(ais);
					break;
				case _ID_alertingPattern:
					this.alertingPattern = new AlertingPatternCapImpl();
					((AlertingPatternCapImpl) this.alertingPattern).decodeAll(ais);
					break;
				case _ID_originalCalledPartyID:
					this.originalCalledPartyID = new OriginalCalledNumberCapImpl();
					((OriginalCalledNumberCapImpl) this.originalCalledPartyID).decodeAll(ais);
					break;
				case _ID_extensions:
					this.extensions = new CAPExtensionsImpl();
					((CAPExtensionsImpl) this.extensions).decodeAll(ais);
					break;
				case _ID_carrier:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_callingPartysCategory:
					this.callingPartysCategory = new CallingPartysCategoryInapImpl();
					((CallingPartysCategoryInapImpl) this.callingPartysCategory).decodeAll(ais);
					break;
				case _ID_redirectingPartyID:
					this.redirectingPartyID = new RedirectingPartyIDCapImpl();
					((RedirectingPartyIDCapImpl) this.redirectingPartyID).decodeAll(ais);
					break;
				case _ID_redirectionInformation:
					this.redirectionInformation = new RedirectionInformationInapImpl();
					((RedirectionInformationInapImpl) this.redirectionInformation).decodeAll(ais);
					break;
				case _ID_genericNumbers:
					this.genericNumbers = new ArrayList<GenericNumberCap>();
					AsnInputStream ais2 = ais.readSequenceStream();
					while (true) {
						if (ais2.available() == 0)
							break;

						int tag2 = ais2.readTag();
						if (ais2.getTagClass() != Tag.CLASS_UNIVERSAL && tag2 != Tag.STRING_OCTET)
							throw new CAPParsingComponentException("Error when decoding " + _PrimitiveName
									+ " genericNumbers parameter SET must consist of OCTET_STRING elements",
									CAPParsingComponentExceptionReason.MistypedParameter);

						GenericNumberCapImpl elem = new GenericNumberCapImpl();
						elem.decodeAll(ais2);
						this.genericNumbers.add(elem);
					}
					break;
				case _ID_serviceInteractionIndicatorsTwo:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_chargeNumber:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_legToBeConnected:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_cug_Interlock:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_cug_OutgoingAccess:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_suppressionOfAnnouncement:
					ais.readNull();
					this.suppressionOfAnnouncement = true;
					break;
				case _ID_oCSIApplicable:
					ais.readNull();
					this.ocsIApplicable = true;
					break;
				case _ID_naOliInfo:
					this.naoliInfo = new NAOliInfoImpl();
					((NAOliInfoImpl) this.naoliInfo).decodeAll(ais);
					break;
				case _ID_bor_InterrogationRequested:
					ais.advanceElement(); // TODO: implement it
					break;

				default:
					ais.advanceElement();
					break;
				}
			} else {
				ais.advanceElement();
			}
		}

		if (this.destinationRoutingAddress == null)
			throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName + ": destinationRoutingAddress is mandatory but not found ",
					CAPParsingComponentExceptionReason.MistypedParameter);
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs) throws CAPException {
		this.encodeAll(asnOs, this.getTagClass(), this.getTag());
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws CAPException {

		try {
			asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
		}
	}

	@Override
	public void encodeData(AsnOutputStream aos) throws CAPException {

		if (this.destinationRoutingAddress == null)
			throw new CAPException("Error while encoding " + _PrimitiveName + ": destinationRoutingAddress must not be null");

		try {
			((DestinationRoutingAddressImpl) this.destinationRoutingAddress).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_destinationRoutingAddress);

			if (this.alertingPattern != null)
				((AlertingPatternCapImpl) this.alertingPattern).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_alertingPattern);
			if (this.originalCalledPartyID != null)
				((OriginalCalledNumberCapImpl) this.originalCalledPartyID).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_originalCalledPartyID);
			if (this.extensions != null)
				((CAPExtensionsImpl) this.extensions).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_extensions);
			if (this.carrier != null) {
				// TODO: implement it - _ID_carrier
			}
			if (this.callingPartysCategory != null)
				((CallingPartysCategoryInapImpl) this.callingPartysCategory).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_callingPartysCategory);
			if (this.redirectingPartyID != null)
				((RedirectingPartyIDCapImpl) this.redirectingPartyID).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_redirectingPartyID);
			if (this.redirectionInformation != null)
				((RedirectionInformationInapImpl) this.redirectionInformation).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_redirectionInformation);

			if (this.genericNumbers != null) {
				if (this.genericNumbers.size() < 1 || this.genericNumbers.size() > 5)
					throw new CAPException("Error while encoding " + _PrimitiveName + ": genericNumbers size must be from 1 to 5");

				aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _ID_genericNumbers);
				int pos = aos.StartContentDefiniteLength();
				for (GenericNumberCap gnc : this.genericNumbers) {
					GenericNumberCapImpl gncc = (GenericNumberCapImpl) gnc;
					gncc.encodeAll(aos);
				}
				aos.FinalizeContent(pos);
			}
			if (this.serviceInteractionIndicatorsTwo != null) {
				// TODO: implement it - _ID_serviceInteractionIndicatorsTwo
			}
			if (this.chargeNumber != null) {
				// TODO: implement it - _ID_chargeNumber
			}
			if (this.legToBeConnected != null) {
				// TODO: implement it - _ID_legToBeConnected
			}
			if (this.cugInterlock != null) {
				// TODO: implement it - _ID_cug_Interlock
			}
			if (this.cugOutgoingAccess) {
				// TODO: implement it - _ID_cug_OutgoingAccess
			}
			if (this.suppressionOfAnnouncement)
				aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_suppressionOfAnnouncement);
			if (this.ocsIApplicable)
				aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_oCSIApplicable);
			if (this.naoliInfo != null)
				((NAOliInfoImpl) this.naoliInfo).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_naOliInfo);
			if (this.borInterrogationRequested) {
				// TODO: implement it - _ID_bor_InterrogationRequested
			}

		} catch (IOException e) {
			throw new CAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
		} catch (AsnException e) {
			throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
		} catch (INAPException e) {
			throw new CAPException("INAPException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
		}
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append(_PrimitiveName);
		sb.append(" [");

		if (this.destinationRoutingAddress != null) {
			sb.append("destinationRoutingAddress=");
			sb.append(destinationRoutingAddress.toString());
		}
		if (this.alertingPattern != null) {
			sb.append(", alertingPattern=");
			sb.append(alertingPattern.toString());
		}
		if (this.originalCalledPartyID != null) {
			sb.append(", originalCalledPartyID=");
			sb.append(originalCalledPartyID.toString());
		}
		if (this.extensions != null) {
			sb.append(", extensions=");
			sb.append(extensions.toString());
		}
		if (this.carrier != null) {
			sb.append(", carrier=");
			sb.append(carrier.toString());
		}
		if (this.callingPartysCategory != null) {
			sb.append(", callingPartysCategory=");
			sb.append(callingPartysCategory.toString());
		}
		if (this.redirectingPartyID != null) {
			sb.append(", redirectingPartyID=");
			sb.append(redirectingPartyID.toString());
		}
		if (this.redirectionInformation != null) {
			sb.append(", redirectionInformation=");
			sb.append(redirectionInformation.toString());
		}
		if (this.genericNumbers != null) {
			sb.append(", genericNumbers=[");
			boolean isFirst = true;
			for (GenericNumberCap gnc : this.genericNumbers) {
				if (isFirst)
					isFirst = false;
				else
					sb.append(", ");
				sb.append(gnc.toString());
			}
			sb.append("]");
		}
		if (this.serviceInteractionIndicatorsTwo != null) {
			sb.append(", serviceInteractionIndicatorsTwo=");
			sb.append(serviceInteractionIndicatorsTwo.toString());
		}
		if (this.chargeNumber != null) {
			sb.append(", chargeNumber=");
			sb.append(chargeNumber.toString());
		}
		if (this.legToBeConnected != null) {
			sb.append(", legToBeConnected=");
			sb.append(legToBeConnected.toString());
		}
		if (this.cugInterlock != null) {
			sb.append(", cugInterlock=");
			sb.append(cugInterlock.toString());
		}
		if (this.cugOutgoingAccess) {
			sb.append(", cugOutgoingAccess");
		}
		if (suppressionOfAnnouncement) {
			sb.append(", suppressionOfAnnouncement");
		}
		if (ocsIApplicable) {
			sb.append(", ocsIApplicable");
		}
		if (this.naoliInfo != null) {
			sb.append(", naoliInfo=");
			sb.append(naoliInfo.toString());
		}
		if (this.borInterrogationRequested) {
			sb.append(", borInterrogationRequested");
		}
		
		sb.append("]");

		return sb.toString();
	}
}

