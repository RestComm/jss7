/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * 
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free 
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
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
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.GPRSMSClass;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.LocationInformation;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.LocationInformationGPRS;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.MNPInfoRes;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.MSClassmark2;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.PSSubscriberState;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.SubscriberInfo;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.SubscriberState;
import org.mobicents.protocols.ss7.map.primitives.IMEIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

/**
 * @author amit bhayani
 * 
 */
public class SubscriberInfoImpl implements SubscriberInfo, MAPAsnPrimitive {

	public static final int _ID_locationInformation = 0;
	public static final int _ID_subscriberState = 1;
	public static final int _ID_extensionContainer = 2;
	public static final int _ID_locationInformationGPRS = 3;
	public static final int _ID_psSubscriberState = 4;
	public static final int _ID_imei = 5;
	public static final int _ID_msclassmark2 = 6;
	public static final int _ID_gprsMSClass = 7;
	public static final int _ID_mnpInfoRes = 8;

	private LocationInformation locationInformation = null;
	private SubscriberState subscriberState = null;
	private MAPExtensionContainer extensionContainer = null;
	private LocationInformationGPRS locationInformationGPRS = null;
	private PSSubscriberState psSubscriberState = null;
	private IMEI imei = null;
	private MSClassmark2 msClassmark2 = null;
	private GPRSMSClass gprsMSClass = null;
	private MNPInfoRes mnpInfoRes = null;

	/**
	 * 
	 */
	public SubscriberInfoImpl() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.
	 * SubscriberInfo#getLocationInformation()
	 */
	public LocationInformation getLocationInformation() {
		return this.locationInformation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.
	 * SubscriberInfo#getSubscriberState()
	 */
	public SubscriberState getSubscriberState() {
		return this.subscriberState;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.
	 * SubscriberInfo#getExtensionContainer()
	 */
	public MAPExtensionContainer getExtensionContainer() {
		return this.extensionContainer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.
	 * SubscriberInfo#getLocationInformationGPRS()
	 */
	public LocationInformationGPRS getLocationInformationGPRS() {
		return this.locationInformationGPRS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.
	 * SubscriberInfo#getPSSubscriberState()
	 */
	public PSSubscriberState getPSSubscriberState() {
		return this.psSubscriberState;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.
	 * SubscriberInfo#getIMEI()
	 */
	public IMEI getIMEI() {
		return this.imei;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.
	 * SubscriberInfo#getMSClassmark2()
	 */
	public MSClassmark2 getMSClassmark2() {
		return this.msClassmark2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.
	 * SubscriberInfo#getGPRSMSClass()
	 */
	public GPRSMSClass getGPRSMSClass() {
		return this.gprsMSClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.
	 * SubscriberInfo#getMNPInfoRes()
	 */
	public MNPInfoRes getMNPInfoRes() {
		return this.mnpInfoRes;
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
			throw new MAPParsingComponentException("IOException when decoding SubscriberInfo: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding SubscriberInfo: " + e.getMessage(), e,
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
			throw new MAPParsingComponentException("IOException when decoding SubscriberInfo: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding SubscriberInfo: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
		AsnInputStream ais = ansIS.readSequenceStreamData(length);

		int num = 0;
		while (true) {
			if (ais.available() == 0)
				break;

			int tag = ais.readTag();
			switch (tag) {
			case _ID_locationInformation:
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive())
					throw new MAPParsingComponentException("Error while decoding locationInformation: Parameter 0 bad tag class or not primitive",
							MAPParsingComponentExceptionReason.MistypedParameter);
				this.locationInformation = new LocationInformationImpl();
				((LocationInformationImpl) this.locationInformation).decodeAll(ais);
				break;
			case _ID_subscriberState:
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive())
					throw new MAPParsingComponentException("Error while decoding SubscriberState: Parameter 1 bad tag class or not primitive",
							MAPParsingComponentExceptionReason.MistypedParameter);
				int length1 = ais.readLength();
				tag = ais.readTag();

				this.subscriberState = new SubscriberStateImpl();
				((SubscriberStateImpl) this.subscriberState).decodeAll(ais);
				break;
			case _ID_extensionContainer:
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive())
					throw new MAPParsingComponentException("Error while decoding MAPExtensionContainer: Parameter 2 bad tag class or not primitive",
							MAPParsingComponentExceptionReason.MistypedParameter);
				extensionContainer = new MAPExtensionContainerImpl();
				((MAPExtensionContainerImpl) extensionContainer).decodeAll(ais);
				break;

			case _ID_locationInformationGPRS:
				((LocationInformationGPRSImpl) locationInformationGPRS).decodeAll(ais);
				break;
			case _ID_psSubscriberState:
				((PSSubscriberStateImpl) psSubscriberState).decodeAll(ais);
				break;
			case _ID_imei:
				((IMEIImpl) imei).decodeAll(ais);
				break;
			case _ID_msclassmark2:
				((MSClassmark2Impl) msClassmark2).decodeAll(ais);
				break;
			case _ID_gprsMSClass:
				((GPRSMSClassImpl) gprsMSClass).decodeAll(ais);
				break;
			case _ID_mnpInfoRes:
				((MNPInfoResImpl) mnpInfoRes).decodeAll(ais);
				break;

			default:
				ais.advanceElement();
				break;

			}
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
		this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, this.getTag());
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
			asnOs.writeTag(tagClass, true, tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding SubscriberInfo : " + e.getMessage(), e);
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
		if (this.locationInformation != null)
			((LocationInformationImpl) this.locationInformation).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_locationInformation);

		if (this.subscriberState != null)
			((SubscriberStateImpl) this.subscriberState).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_subscriberState);

		if (this.extensionContainer != null)
			((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_extensionContainer);

		if (this.locationInformationGPRS != null)
			((LocationInformationGPRSImpl) this.locationInformationGPRS).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_locationInformationGPRS);

		if (this.psSubscriberState != null)
			((PSSubscriberStateImpl) this.psSubscriberState).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_psSubscriberState);

		if (this.imei != null)
			((IMEIImpl) this.imei).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_imei);

		if (this.msClassmark2 != null)
			((MSClassmark2Impl) this.msClassmark2).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_msclassmark2);

		if (this.gprsMSClass != null)
			((GPRSMSClassImpl) this.gprsMSClass).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_gprsMSClass);

		if (this.mnpInfoRes != null)
			((MNPInfoResImpl) this.mnpInfoRes).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_mnpInfoRes);
	}

}
