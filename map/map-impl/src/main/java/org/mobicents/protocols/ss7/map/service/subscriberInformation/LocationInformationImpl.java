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

package org.mobicents.protocols.ss7.map.service.subscriberInformation;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.GeodeticInformation;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.GeographicalInformation;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.LSAIdentity;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.LocationInformation;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.LocationInformationEPS;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.LocationNumberMap;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.UserCSGInformation;
import org.mobicents.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class LocationInformationImpl implements LocationInformation, MAPAsnPrimitive {

	public static final int _ID_geographicalInformation = 0;
	public static final int _ID_vlr_number = 1;
	public static final int _ID_locationNumber = 2;
	public static final int _ID_cellGlobalIdOrServiceAreaIdOrLAI = 3;
	public static final int _ID_extensionContainer = 4;
	public static final int _ID_selectedLSA_Id = 5;
	public static final int _ID_msc_Number = 6;
	public static final int _ID_geodeticInformation = 7;
	public static final int _ID_currentLocationRetrieved = 8;
	public static final int _ID_sai_Present = 9;
	public static final int _ID_locationInformationEPS = 10;
	public static final int _ID_userCSGInformation = 11;	

	public static final String _PrimitiveName = "LocationInformation";
	
	private Integer ageOfLocationInformation;
	private GeographicalInformation geographicalInformation;
	private ISDNAddressString vlrNumber;
	private LocationNumberMap locationNumber;
	private CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI;
	private MAPExtensionContainer extensionContainer;
	private LSAIdentity selectedLSAId;
	private ISDNAddressString mscNumber;
	private GeodeticInformation geodeticInformation;
	private boolean currentLocationRetrieved;
	private boolean saiPresent;
	private LocationInformationEPS locationInformationEPS;
	private UserCSGInformation userCSGInformation;

	
	public LocationInformationImpl() {
	}

	public LocationInformationImpl(Integer ageOfLocationInformation, GeographicalInformation geographicalInformation, ISDNAddressString vlrNumber,
			LocationNumberMap locationNumber, CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI, MAPExtensionContainer extensionContainer,
			LSAIdentity selectedLSAId, ISDNAddressString mscNumber, GeodeticInformation geodeticInformation, boolean currentLocationRetrieved,
			boolean saiPresent, LocationInformationEPS locationInformationEPS, UserCSGInformation userCSGInformation) {
		this.ageOfLocationInformation = ageOfLocationInformation;
		this.geographicalInformation = geographicalInformation;
		this.vlrNumber = vlrNumber;
		this.locationNumber = locationNumber;
		this.cellGlobalIdOrServiceAreaIdOrLAI = cellGlobalIdOrServiceAreaIdOrLAI;
		this.extensionContainer = extensionContainer;
		this.selectedLSAId = selectedLSAId;
		this.mscNumber = mscNumber;
		this.geodeticInformation = geodeticInformation;
		this.currentLocationRetrieved = currentLocationRetrieved;
		this.saiPresent = saiPresent;
		this.locationInformationEPS = locationInformationEPS;
		this.userCSGInformation = userCSGInformation;
	}
	
	@Override
	public Integer getAgeOfLocationInformation() {
		return this.ageOfLocationInformation;
	}

	@Override
	public GeographicalInformation getGeographicalInformation() {
		return this.geographicalInformation;
	}

	@Override
	public ISDNAddressString getVlrNumber() {
		return this.vlrNumber;
	}

	@Override
	public LocationNumberMap getLocationNumber() {
		return locationNumber;
	}

	@Override
	public CellGlobalIdOrServiceAreaIdOrLAI getCellGlobalIdOrServiceAreaIdOrLAI() {
		return cellGlobalIdOrServiceAreaIdOrLAI;
	}

	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return extensionContainer;
	}

	@Override
	public LSAIdentity getSelectedLSAId() {
		return selectedLSAId;
	}

	@Override
	public ISDNAddressString getMscNumber() {
		return mscNumber;
	}

	@Override
	public GeodeticInformation getGeodeticInformation() {
		return geodeticInformation;
	}

	@Override
	public boolean getCurrentLocationRetrieved() {
		return currentLocationRetrieved;
	}

	@Override
	public boolean getSaiPresent() {
		return saiPresent;
	}

	@Override
	public LocationInformationEPS getLocationInformationEPS() {
		return locationInformationEPS;
	}

	@Override
	public UserCSGInformation getUserCSGInformation() {
		return userCSGInformation;
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
		
		this.ageOfLocationInformation = null;
		this.geographicalInformation = null;
		this.vlrNumber = null;
		this.locationNumber = null;
		this.cellGlobalIdOrServiceAreaIdOrLAI = null;
		this.extensionContainer = null;
		this.selectedLSAId = null;
		this.mscNumber = null;
		this.geodeticInformation = null;
		this.currentLocationRetrieved = false;
		this.saiPresent = false;
		this.locationInformationEPS = null;
		this.userCSGInformation = null;
		
		AsnInputStream ais = ansIS.readSequenceStreamData(length);
		
		while( true ) {
			if( ais.available()==0 )
				break;
			
			int tag = ais.readTag();
			
			// optional parameters
			if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {

				switch (tag) {
				case Tag.INTEGER: // AgeOfLocationInformation
					this.ageOfLocationInformation = (int) ais.readInteger();
					break;

				default:
					ais.advanceElement();
					break;
				}
			} else if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {

				switch (tag) {
				case _ID_geographicalInformation:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_vlr_number:
					this.vlrNumber = new ISDNAddressStringImpl();
					((ISDNAddressStringImpl) this.vlrNumber).decodeAll(ais);
					break;
				case _ID_locationNumber:
					this.locationNumber = new LocationNumberMapImpl();
					((LocationNumberMapImpl) this.locationNumber).decodeAll(ais);
					break;
				case _ID_cellGlobalIdOrServiceAreaIdOrLAI:
					this.cellGlobalIdOrServiceAreaIdOrLAI = new CellGlobalIdOrServiceAreaIdOrLAIImpl();
					AsnInputStream ais2 = ais.readSequenceStream();
					ais2.readTag();
					((CellGlobalIdOrServiceAreaIdOrLAIImpl) this.cellGlobalIdOrServiceAreaIdOrLAI).decodeAll(ais2);
					break;
				case _ID_extensionContainer:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_selectedLSA_Id:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_msc_Number:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_geodeticInformation:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_currentLocationRetrieved:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_sai_Present:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_locationInformationEPS:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_userCSGInformation:
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
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {

		this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, this.getTag());
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
		
		try {
			asnOs.writeTag(tagClass, true, tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
		}
	}

	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {

		try {
			if (ageOfLocationInformation != null)
				asnOs.writeInteger((int) ageOfLocationInformation);

			// TODO: implement unimplemented members
			// private GeographicalInformation geographicalInformation;
			
			if (vlrNumber != null)
				((ISDNAddressStringImpl) vlrNumber).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_vlr_number);
			if (locationNumber != null)
				((LocationNumberMapImpl) locationNumber).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_locationNumber);
			if (cellGlobalIdOrServiceAreaIdOrLAI != null) {
				try {
					asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _ID_cellGlobalIdOrServiceAreaIdOrLAI);
					int pos = asnOs.StartContentDefiniteLength();
					((CellGlobalIdOrServiceAreaIdOrLAIImpl) this.cellGlobalIdOrServiceAreaIdOrLAI).encodeAll(asnOs);
					asnOs.FinalizeContent(pos);
				} catch (AsnException e) {
					throw new MAPException("AsnException while encoding parameter cellGlobalIdOrServiceAreaIdOrLAI", e);
				}
			}

			// TODO: implement unimplemented members
			// private MAPExtensionContainer extensionContainer;
			// private LSAIdentity selectedLSAId;
			// private ISDNAddressString mscNumber;
			// private GeodeticInformation geodeticInformation;
			// private boolean currentLocationRetrieved;
			// private boolean saiPresent;
			// private LocationInformationEPS locationInformationEPS;
			// private UserCSGInformation userCSGInformation;
			
		} catch (IOException e) {
			throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("LocationInformation [");

		if (this.ageOfLocationInformation != null) {
			sb.append("ageOfLocationInformation=");
			sb.append(this.ageOfLocationInformation);
		}

		// TODO: implement unimplemented members
		// private GeographicalInformation geographicalInformation;

		if (this.vlrNumber != null) {
			sb.append(", vlrNumber=");
			sb.append(this.vlrNumber.toString());
		}
		if (this.locationNumber != null) {
			sb.append(", locationNumber=");
			sb.append(this.locationNumber.toString());
		}
		if (this.cellGlobalIdOrServiceAreaIdOrLAI != null) {
			sb.append(", cellGlobalIdOrServiceAreaIdOrLAI=[");
			sb.append(this.cellGlobalIdOrServiceAreaIdOrLAI.toString());
			sb.append("]");
		}
		if (this.extensionContainer != null) {
			sb.append(", extensionContainer=");
			sb.append(this.extensionContainer.toString());
		}

		// TODO: implement unimplemented members
		// private MAPExtensionContainer extensionContainer;
		// private LSAIdentity selectedLSAId;
		// private ISDNAddressString mscNumber;
		// private GeodeticInformation geodeticInformation;
		// private boolean currentLocationRetrieved;
		// private boolean saiPresent;
		// private LocationInformationEPS locationInformationEPS;
		// private UserCSGInformation userCSGInformation;

		sb.append("]");

		return sb.toString();
	}
}
