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
import org.mobicents.protocols.ss7.map.api.MAPMessageType;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;

import org.mobicents.protocols.ss7.map.api.primitives.DiameterIdentity;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NAEAPreferredCI;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

import org.mobicents.protocols.ss7.map.api.service.lsm.ReportingPLMN;
import org.mobicents.protocols.ss7.map.api.service.mobility.MobilityMessage;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.AgeIndicator;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.TypeOfUpdate;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.AccessRestrictionData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CSAllocationRetentionPriority;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CSGSubscriptionData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.Category;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ChargingCharacteristics;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.EPSSubscriptionData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBearerServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtTeleserviceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSSubscriptionData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.InsertSubscriberDataRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.LCSInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.MCSSInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.NetworkAccessMode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SGSNCAMELSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SubscriberStatus;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.VlrCamelSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.VoiceBroadcastData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.VoiceGroupCallData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ZoneCode;
import org.mobicents.protocols.ss7.map.service.lsm.ReportingPLMNImpl;
import org.mobicents.protocols.ss7.map.service.mobility.MobilityMessageImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.CategoryImpl;
import org.mobicents.protocols.ss7.map.service.mobility.locationManagement.CancelLocationRequestImpl;

/**
 * @author daniel bichara
 * 
 */
public class InsertSubscriberDataRequestImpl extends MobilityMessageImpl implements InsertSubscriberDataRequest {

	public static final String _PrimitiveName = "InsertSubscriberDataRequest";
	
	/**
	 * TeleServiceCode
	 * 3GPP TS 29.002 V9.1.0 (2010-03) - release 9 - page 421
	 */
	public static final byte[] tscAllSpeechTransmissionServices 		= {(byte)16};	// '00010000'B
	public static final byte[] tscTelephony 							= {(byte)17};	// '00010001'B
	public static final byte[] tscEmergencyCalls						= {(byte)18};	// '00010010'B

	public static final byte[] tscAllShortMessageServices				= {(byte)32};	// '00100000'B
	public static final byte[] tscShortMessageMT_PP 					= {(byte)33};	// '00100001'B
	public static final byte[] tscShortMessageMO_PP						= {(byte)34};	// '00100010'B

	public static final byte[] tscAllFacsimileTransmissionServices		= {(byte)96};	// '01100000'B
	public static final byte[] tscFacsimileGroup3AndAlterSpeech			= {(byte)97};	// '01100001'B
	public static final byte[] tscAutomaticFacsimileGroup3				= {(byte)98};	// '01100010'B
	public static final byte[] tscFacsimileGroup4						= {(byte)99};	// '01100011'B

	public static final byte[] tscAllVoiceGroupCallServices				= {(byte)144};	// '10010000'B
	public static final byte[] tscVoiceGroupCall						= {(byte)145};	// '10010001'B
	public static final byte[] tscVoiceBroadcastCall					= {(byte)146};	// '10010010'B
	
	/**
	 * BearerServiceCode
	 * 3GPP TS 29.002 V9.1.0 (2010-03) - release 9 - page 422
	 */
	public static final byte[] bscAllBearerServices 			= {(byte)0};	// '00000000'B
	public static final byte[] bscAllDataCDAServices			= {(byte)16};	// '00010000'B
	public static final byte[] bscGeneralDataCDAServices		= {(byte)23};	// '00010111'B
	public static final byte[] bscAllDataCDSServices			= {(byte)24};	// '00011000'B
	public static final byte[] bscGeneralDataCDSServices		= {(byte)31};	// '00011111'B
	public static final byte[] bscAllPadAccessCAServices		= {(byte)32};	// '00100000'B
	public static final byte[] bscAllDataPDSServices			= {(byte)40};	// '00101000'B

	public static final byte[] bscAllAlternateSpeechDataCDA		= {(byte)48};	// '00110000'B
	public static final byte[] bscAllAlternateSpeechDataCDS		= {(byte)56};	// '00111000'B
	public static final byte[] bscAllSpeechFollowedByDataCDA	= {(byte)64};	// '01000000'B
	public static final byte[] bscAllSpeechFollowedByDataCDS	= {(byte)72};	// '01001000'B

	public static final byte[] bscAllDataCircuitAsynchronous	= {(byte)80};	// '01010000'B
	public static final byte[] bscAllDataCircuitSynchronous		= {(byte)88};	// '01011000'B
	public static final byte[] bscAllAsynchronousServices		= {(byte)96};	// '01100000'B
	public static final byte[] bscAllSynchronousServices		= {(byte)104};	// '01101000'B

	public static final byte[] bscAllPLMNspecificBS				= {(byte)208};	// '11010000'B
	
	/**
	 * Charging Characteristics Flags
	 * 3GPP TS 32.215 - V5.9.0 - release 5 - page 25 & Annex A
	 */
	public static final byte[] charging00 			= {(byte)0,(byte)0};	// '00000000'B '00000000'B
	public static final byte[] chargingP0 			= {(byte)1,(byte)0};	// '00000001'B '00000000'B
	public static final byte[] chargingP1 			= {(byte)2,(byte)0};	// '00000010'B '00000000'B
	public static final byte[] chargingP2 			= {(byte)4,(byte)0};	// '00000100'B '00000000'B
	public static final byte[] chargingP3 			= {(byte)8,(byte)0};	// '00001000'B '00000000'B

	public static final byte[] chargingB1 			= {(byte)16,(byte)0};	// '00010000'B '00000000'B
	public static final byte[] chargingB2 			= {(byte)32,(byte)0};	// '00100000'B '00000000'B
	public static final byte[] chargingB3 			= {(byte)64,(byte)0};	// '01000000'B '00000000'B
	public static final byte[] chargingB4 			= {(byte)128,(byte)0};	// '10000000'B '00000000'B
	public static final byte[] chargingB5 			= {(byte)0,(byte)1};	// '00000000'B '00000001'B
	public static final byte[] chargingB6 			= {(byte)0,(byte)2};	// '00000000'B '00000010'B
	public static final byte[] chargingB7 			= {(byte)0,(byte)4};	// '00000000'B '00000100'B
	public static final byte[] chargingB8 			= {(byte)0,(byte)8};	// '00000000'B '00000100'B
	public static final byte[] chargingB9 			= {(byte)0,(byte)16};	// '00000000'B '00001000'B
	public static final byte[] chargingB10 			= {(byte)0,(byte)32};	// '00000000'B '00010000'B
	public static final byte[] chargingB11			= {(byte)0,(byte)64};	// '00000000'B '00100000'B
	public static final byte[] chargingB12 			= {(byte)0,(byte)128};	// '00000000'B '10000000'B

	// MAP V2 & V3:
	protected static final int _TAG_imsi = 0;
	protected static final int _TAG_msisdn = 1;
	protected static final int _TAG_category = 2;
	protected static final int _TAG_subscriberStatus = 3;
	protected static final int _TAG_bearerServiceList = 4;
	protected static final int _TAG_teleserviceList = 6;
	protected static final int _TAG_provisionedSS = 7;
	protected static final int _TAG_roamingRestrictionDueToUnsupportedFeature = 9;

	// MAP V3:
	protected static final int _TAG_extContainer = 14;
	protected static final int _TAG_gprsSubscriptionData = 16;
	protected static final int _TAG_chargingCharacteristics = 18;
	protected static final int _TAG_roamingRestrictedInSgsnDueToUnsupportedFeature = 23;	
	protected static final int _TAG_networkAccessMode = 24;

	// TODO MAP V2 & V3:
	//protected static final int _TAG_odb-Data = 8;
	//protected static final int _TAG_regionalSubscriptionData = 10;
	//protected static final int _TAG_vbsSubscriptionData = 11;
	//protected static final int _TAG_vgcsSubscriptionData = 12;
	//protected static final int _TAG_vlrCamelSubscriptionInfo = 13;

	// TODO MAP V3:
	//protected static final int _TAG_naea-PreferredCI = 15;
	//protected static final int _TAG_lsaInformation = 25;
	//protected static final int _TAG_lmu-Indicator = 21;
	//protected static final int _TAG_lcsInformation = 22;
	//protected static final int _TAG_istAlertTimer = 26;
	//protected static final int _TAG_superChargerSupportedInHLR = 27;
	//protected static final int _TAG_mc-SS-Info = 28;
	//protected static final int _TAG_cs-AllocationRetentionPriority = 29;
	//protected static final int _TAG_sgsn-CAMEL-SubscriptionInfo = 17;
	//protected static final int _TAG_accessRestrictionData = 19;
	//protected static final int _TAG_ics-Indicator = 20;
	//protected static final int _TAG_eps-SubscriptionData = 31;
	//protected static final int _TAG_csg-SubscriptionDataList = 32;
	//protected static final int _TAG_ue-ReachabilityRequestIndicator = 33;
	//protected static final int _TAG_sgsn-Numbe = 34;
	//protected static final int _TAG_mme-Name = 35;
	//protected static final int _TAG_subscribedPeriodicRAUTAUtimer = 36;
	//protected static final int _TAG_vplmnLIPAAllowed = 37;
	//protected static final int _TAG_mdtUserConsent = 38;
	//protected static final int _TAG_subscribedPeriodicLAUtimer = 39;

	private IMSI imsi = null;
	private ISDNAddressString msisdn = null;
	private Category category = null;
	private SubscriberStatus subscriberStatus = null;
	private ArrayList<ExtBearerServiceCode> bearerServiceList = null;
	private ArrayList<ExtTeleserviceCode> teleserviceList = null;
	private ArrayList<ExtSSInfo> provisionedSS = null;
	private ODBData odbData = null;
	private boolean roamingRestrictionDueToUnsupportedFeature = false;
	private ArrayList<ZoneCode> regionalSubscriptionData = null;
	private ArrayList<VoiceBroadcastData> vbsSubscriptionData = null;
	private ArrayList<VoiceGroupCallData> vgcsSubscriptionData = null;
	private VlrCamelSubscriptionInfo vlrCamelSubscriptionInfo = null;
	private MAPExtensionContainer extensionContainer = null;
	private NAEAPreferredCI naeaPreferredCI = null;
	private GPRSSubscriptionData gprsSubscriptionData = null;
	private boolean roamingRestrictedInSgsnDueToUnsupportedFeature = false;
	private NetworkAccessMode networkAccessMode = null;
	private LSAInformation lsaInformation = null;
	private boolean lmuIndicator = false;
	private LCSInformation lcsInformation = null;
	private Integer istAlertTimer = null;
	private AgeIndicator superChargerSupportedInHLR = null;
	private MCSSInfo mcSsInfo = null;
	private CSAllocationRetentionPriority csAllocationRetentionPriority = null;
	private SGSNCAMELSubscriptionInfo sgsnCamelSubscriptionInfo = null;
	private ChargingCharacteristics chargingCharacteristics = null;
	private AccessRestrictionData accessRestrictionData = null;
	private Boolean icsIndicator = false;
	private EPSSubscriptionData epsSubscriptionData = null;
	private ArrayList<CSGSubscriptionData> csgSubscriptionDataList = null;
	private boolean ueReachabilityRequestIndicator = false;
	private ISDNAddressString sgsnNumber = null;
	private DiameterIdentity mmeName = null;
	private Long subscribedPeriodicRAUTAUtimer = null;
	private boolean vplmnLIPAAllowed = false;
	private Boolean mdtUserConsent = false;
	private Long subscribedPeriodicLAUtimer = null;

	private long mapProtocolVersion;
	
	// For incoming messages
	public InsertSubscriberDataRequestImpl(long mapProtocolVersion) {
		this.mapProtocolVersion = mapProtocolVersion;
	}
	
	// For outgoing messages - MAP V2
	public InsertSubscriberDataRequestImpl(long mapProtocolVersion, IMSI imsi, ISDNAddressString msisdn,
			Category category, SubscriberStatus subscriberStatus, ArrayList<ExtBearerServiceCode> bearerServiceList,
			ArrayList<ExtTeleserviceCode> teleserviceList, ArrayList<ExtSSInfo> provisionedSS,
			ODBData odbData, boolean roamingRestrictionDueToUnsupportedFeature,
			ArrayList<ZoneCode> regionalSubscriptionData, ArrayList<VoiceBroadcastData> vbsSubscriptionData,
			ArrayList<VoiceGroupCallData> vgcsSubscriptionData, VlrCamelSubscriptionInfo vlrCamelSubscriptionInfo) {
		this.mapProtocolVersion = mapProtocolVersion;
		this.imsi = imsi;
		this.msisdn = msisdn;
		this.category = category;
		this.subscriberStatus = subscriberStatus;
		this.bearerServiceList = bearerServiceList;
		this.teleserviceList = teleserviceList;
		this.provisionedSS = provisionedSS;
		this.odbData = odbData;
		this.roamingRestrictionDueToUnsupportedFeature = roamingRestrictionDueToUnsupportedFeature;
		this.regionalSubscriptionData = regionalSubscriptionData;
		this.vbsSubscriptionData = vbsSubscriptionData;
		this.vgcsSubscriptionData = vgcsSubscriptionData;
		this.vlrCamelSubscriptionInfo = vlrCamelSubscriptionInfo;
	}

	// For outgoing messages - MAP V3
	public InsertSubscriberDataRequestImpl(long mapProtocolVersion, IMSI imsi, ISDNAddressString msisdn,
			Category category, SubscriberStatus subscriberStatus, ArrayList<ExtBearerServiceCode> bearerServiceList,
			ArrayList<ExtTeleserviceCode> teleserviceList, ArrayList<ExtSSInfo> provisionedSS,
			ODBData odbData, boolean roamingRestrictionDueToUnsupportedFeature,
			ArrayList<ZoneCode> regionalSubscriptionData, ArrayList<VoiceBroadcastData> vbsSubscriptionData,
			ArrayList<VoiceGroupCallData> vgcsSubscriptionData, VlrCamelSubscriptionInfo vlrCamelSubscriptionInfo,
			MAPExtensionContainer extensionContainer, NAEAPreferredCI naeaPreferredCI, GPRSSubscriptionData gprsSubscriptionData,
			boolean roamingRestrictedInSgsnDueToUnsupportedFeature, NetworkAccessMode networkAccessMode,
			LSAInformation lsaInformation, boolean lmuIndicator, LCSInformation lcsInformation, Integer istAlertTimer,
			AgeIndicator superChargerSupportedInHLR, MCSSInfo mcSsInfo, CSAllocationRetentionPriority csAllocationRetentionPriority,
			SGSNCAMELSubscriptionInfo sgsnCamelSubscriptionInfo, ChargingCharacteristics chargingCharacteristics,
			AccessRestrictionData accessRestrictionData, Boolean icsIndicator, EPSSubscriptionData epsSubscriptionData,
			ArrayList<CSGSubscriptionData> csgSubscriptionDataList, boolean ueReachabilityRequestIndicator,
			ISDNAddressString sgsnNumber, DiameterIdentity mmeName, Long subscribedPeriodicRAUTAUtimer,
			boolean vplmnLIPAAllowed, Boolean mdtUserConsent, Long subscribedPeriodicLAUtimer) {

		this.mapProtocolVersion = mapProtocolVersion;
		this.imsi = imsi;
		this.msisdn = msisdn;
		this.category = category;
		this.subscriberStatus = subscriberStatus;
		this.bearerServiceList = bearerServiceList;
		this.teleserviceList = teleserviceList;
		this.provisionedSS = provisionedSS;
		this.odbData = odbData;
		this.roamingRestrictionDueToUnsupportedFeature = roamingRestrictionDueToUnsupportedFeature;
		this.regionalSubscriptionData = regionalSubscriptionData;
		this.vbsSubscriptionData = vbsSubscriptionData;
		this.vgcsSubscriptionData = vgcsSubscriptionData;
		this.vlrCamelSubscriptionInfo = vlrCamelSubscriptionInfo;

		if (mapProtocolVersion >= 3) {
			this.extensionContainer = extensionContainer;
			this.naeaPreferredCI = naeaPreferredCI;
			this.gprsSubscriptionData = gprsSubscriptionData;
			this.roamingRestrictedInSgsnDueToUnsupportedFeature = roamingRestrictedInSgsnDueToUnsupportedFeature;
			this.networkAccessMode = networkAccessMode;
			this.lsaInformation = lsaInformation;
			this.lmuIndicator = lmuIndicator;
			this.lcsInformation = lcsInformation;
			this.istAlertTimer = istAlertTimer;
			this.superChargerSupportedInHLR = superChargerSupportedInHLR;
			this.mcSsInfo = mcSsInfo;
			this.csAllocationRetentionPriority = csAllocationRetentionPriority;
			this.sgsnCamelSubscriptionInfo = sgsnCamelSubscriptionInfo;
			this.chargingCharacteristics = chargingCharacteristics;
			this.accessRestrictionData = accessRestrictionData;
			this.icsIndicator = icsIndicator;
			this.epsSubscriptionData = epsSubscriptionData;
			this.csgSubscriptionDataList = csgSubscriptionDataList;
			this.ueReachabilityRequestIndicator = ueReachabilityRequestIndicator;
			this.sgsnNumber = sgsnNumber;
			this.mmeName = mmeName;
			this.subscribedPeriodicRAUTAUtimer = subscribedPeriodicRAUTAUtimer;
			this.vplmnLIPAAllowed = vplmnLIPAAllowed;
			this.mdtUserConsent = mdtUserConsent;
			this.subscribedPeriodicLAUtimer = subscribedPeriodicLAUtimer;
		}
	}
	
	public long getMapProtocolVersion() {
		return this.mapProtocolVersion;
	}

	@Override
	public int getTag() throws MAPException {
		if (this.mapProtocolVersion >= 2) {
			return Tag.SEQUENCE;
		} else {
			return Tag.STRING_OCTET;
		}
	}

	@Override
	public int getTagClass() {
		return Tag.CLASS_UNIVERSAL;
	}

	@Override
	public boolean getIsPrimitive() {
		if (this.mapProtocolVersion >= 2) {
			return false;
		} else {
			return true;
		}
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
		ExtBearerServiceCode bearerItem = null;
		ExtTeleserviceCode teleserviceItem = null;
		ExtSSInfo serviceItem = null;
		this.imsi = null;
		this.msisdn = null;
		this.category = null;
		this.subscriberStatus = null;
		this.bearerServiceList = null;
		this.teleserviceList = null;
		this.provisionedSS = null;
		this.roamingRestrictionDueToUnsupportedFeature = false;
		this.extensionContainer = null;
		this.gprsSubscriptionData = null;
		this.chargingCharacteristics = null;
		this.roamingRestrictedInSgsnDueToUnsupportedFeature = false;
		this.networkAccessMode = null;

		if (mapProtocolVersion >= 2) {
			AsnInputStream ais = ansIS.readSequenceStreamData(length);
			int num = 0;
			while (true) {
				if (ais.available() == 0) {
					break;
				}
				
				int tag = ais.readTag(); 

				switch (tag) {
				case _TAG_imsi:
					if (!ais.isTagPrimitive() || tag != Tag.STRING_OCTET)
						throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
								+ ".imsi: bad tag or tag class or not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
					this.imsi = new IMSIImpl();
					((IMSIImpl) this.imsi).decodeAll(ais);
					break;
				case _TAG_msisdn:
					if (!ais.isTagPrimitive() || tag != Tag.STRING_OCTET)
						throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
								+ ".msisdn: bad tag or tag class or not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
					this.msisdn = new ISDNAddressStringImpl();
					((ISDNAddressStringImpl) this.msisdn).decodeAll(ais);
					break;
				case _TAG_category:
					if (!ais.isTagPrimitive() || tag != Tag.STRING_OCTET)
						throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
								+ ".category: bad tag or tag class or not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
					this.category = new CategoryImpl();
					((CategoryImpl) this.category).decodeAll(ais);
					break;
				case _TAG_subscriberStatus:
					if (!ais.isTagPrimitive()) {
						throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".subscriberStatus: is not primitive",
								MAPParsingComponentExceptionReason.MistypedParameter);
					}
					this.subscriberStatus = SubscriberStatus.getInstance((int) ais.readInteger());
					break;
				case _TAG_bearerServiceList:
					if (ais.isTagPrimitive())
						throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
								+ ".bearerServiceList: Parameter is primitive", MAPParsingComponentExceptionReason.MistypedParameter);

					AsnInputStream ais2 = ais.readSequenceStream();
					while (true) {
						if (ais2.available() == 0)
							break;

						int tag2 = ais2.readTag();
						if (tag2 != Tag.SEQUENCE || ais2.getTagClass() != Tag.CLASS_UNIVERSAL || ais2.isTagPrimitive())
							throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
									+ ": bad bearerServiceCode tag or tagClass or is primitive ", MAPParsingComponentExceptionReason.MistypedParameter);

						bearerItem = new ExtBearerServiceCodeImpl();
						((ExtBearerServiceCodeImpl) bearerItem).decodeAll(ais2);
						if (this.bearerServiceList == null)
							this.bearerServiceList = new ArrayList<ExtBearerServiceCode>();
						this.bearerServiceList.add(bearerItem);
					}
					break;
				case _TAG_teleserviceList:
					if (ais.isTagPrimitive())
						throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
								+ ".teleserviceList: Parameter is primitive", MAPParsingComponentExceptionReason.MistypedParameter);

					AsnInputStream ais3 = ais.readSequenceStream();
					while (true) {
						if (ais3.available() == 0)
							break;

						int tag3 = ais3.readTag();
						if (tag3 != Tag.SEQUENCE || ais3.getTagClass() != Tag.CLASS_UNIVERSAL || ais3.isTagPrimitive())
							throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
									+ ": bad teleserviceCode tag or tagClass or is primitive ", MAPParsingComponentExceptionReason.MistypedParameter);

						teleserviceItem = new ExtTeleserviceCodeImpl();
						((ExtTeleserviceCodeImpl) teleserviceItem).decodeAll(ais3);
						if (this.teleserviceList == null)
							this.teleserviceList = new ArrayList<ExtTeleserviceCode>();
						this.teleserviceList.add(teleserviceItem);
					}
					break;
				case _TAG_provisionedSS:
					if (ais.isTagPrimitive())
						throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
								+ ".provisionedSS: Parameter is primitive", MAPParsingComponentExceptionReason.MistypedParameter);

					AsnInputStream ais4 = ais.readSequenceStream();
					while (true) {
						if (ais4.available() == 0)
							break;

						int tag4 = ais4.readTag();
						if (tag4 != Tag.SEQUENCE || ais4.getTagClass() != Tag.CLASS_UNIVERSAL || ais4.isTagPrimitive())
							throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
									+ ": bad provisionedSS tag or tagClass or is primitive ", MAPParsingComponentExceptionReason.MistypedParameter);

						serviceItem = new ExtSSInfoImpl();
						((ExtSSInfoImpl) serviceItem).decodeAll(ais4);
						if (this.provisionedSS == null)
							this.provisionedSS = new ArrayList<ExtSSInfo>();
						this.provisionedSS.add(serviceItem);
					}
					break;
				case _TAG_roamingRestrictionDueToUnsupportedFeature:
					if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
						throw new MAPParsingComponentException(
								"Error while decoding " + _PrimitiveName + ".roamingRestrictionDueToUnsupportedFeature: bad tag class, tag or not primitive",
								MAPParsingComponentExceptionReason.MistypedParameter);
					}
					ais.readNull();
					this.roamingRestrictionDueToUnsupportedFeature = true;
					break;
				case _TAG_extContainer:
					if (ais.isTagPrimitive())
						throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
								+ ".extensionContainer: Parameter extensionContainer is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
					this.extensionContainer = new MAPExtensionContainerImpl();
					((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
					break;
				case _TAG_gprsSubscriptionData:
					if (ais.isTagPrimitive())
						throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
								+ ".gprsSubscriptionData: bad tag or tag class or primitive", MAPParsingComponentExceptionReason.MistypedParameter);
					this.gprsSubscriptionData = new GPRSSubscriptionDataImpl();
					((GPRSSubscriptionDataImpl) this.gprsSubscriptionData).decodeAll(ais);
					break;
				case _TAG_chargingCharacteristics:
					if (!ais.isTagPrimitive() || tag != Tag.STRING_OCTET)
						throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
								+ ".chargingCharacteristics: bad tag or tag class or not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
					this.chargingCharacteristics = new ChargingCharacteristicsImpl();
					((ChargingCharacteristicsImpl) this.chargingCharacteristics).decodeAll(ais);
					break;
				case _TAG_roamingRestrictedInSgsnDueToUnsupportedFeature:
					if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
						throw new MAPParsingComponentException(
								"Error while decoding " + _PrimitiveName + ".roamingRestrictedInSgsnDueToUnsupportedFeature: bad tag class, tag or not primitive",
								MAPParsingComponentExceptionReason.MistypedParameter);
					}
					ais.readNull();
					this.roamingRestrictedInSgsnDueToUnsupportedFeature = true;
					break;
				case _TAG_networkAccessMode:
					if (!ais.isTagPrimitive()) {
						throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".networkAccessMode: is not primitive",
								MAPParsingComponentExceptionReason.MistypedParameter);
					}
					this.networkAccessMode = NetworkAccessMode.getInstance((int) ais.readInteger());
					break;
				default:
					ais.advanceElement();
					break;
				}
				
				num++;
			}
			
			if (num == 0)
				throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": Needs at least 1 parameter, found "
						+ num, MAPParsingComponentExceptionReason.MistypedParameter);

			if (this.bearerServiceList != null && this.bearerServiceList.size() > 50){
				throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": Parameter bearerServiceList size must be from 1 to 50, found: "
						+ this.bearerServiceList.size(), MAPParsingComponentExceptionReason.MistypedParameter);
			}

			if (this.teleserviceList != null && this.teleserviceList.size() > 20){
				throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": Parameter teleserviceList size must be from 1 to 20, found: "
						+ this.teleserviceList.size(), MAPParsingComponentExceptionReason.MistypedParameter);
			}

			if (this.provisionedSS != null && this.provisionedSS.size() > 30){
				throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": Parameter provisionedSS size must be from 1 to 30, found: "
						+ this.provisionedSS.size(), MAPParsingComponentExceptionReason.MistypedParameter);
			}
		}
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {
		this.encodeAll(asnOs, this.getTagClass(), this.getTag());
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
		try {
			asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
			int posk = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(posk);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
		}
	}

	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {

		if (this.bearerServiceList != null && this.bearerServiceList.size() > 50)
			throw new MAPException("bearerServiceList size must be from 1 to 50, found: " + this.bearerServiceList.size());

		if (this.teleserviceList != null && this.teleserviceList.size() > 20)
			throw new MAPException("teleserviceList size must be from 1 to 20, found: " + this.teleserviceList.size());

		if (this.provisionedSS != null && this.provisionedSS.size() > 30)
			throw new MAPException("provisionedSS size must be from 1 to 30, found: " + this.provisionedSS.size());
 
		if (this.imsi != null) 
			((IMSIImpl) this.imsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_imsi);

		if (this.msisdn != null) 
			((ISDNAddressStringImpl) this.msisdn).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_msisdn);

		if (this.category != null) 
			((CategoryImpl) this.category).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_category);

		if (this.subscriberStatus != null) {
			try {
				asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_subscriberStatus, this.subscriberStatus.getCode());
			} catch (IOException e) {
				throw new MAPException("IOException while encoding " + _PrimitiveName + " parameter subscriberStatus", e);
			} catch (AsnException e) {
				throw new MAPException("IOException while encoding " + _PrimitiveName + " parameter subscriberStatus", e);
			}
		}

		if (this.bearerServiceList != null) {
			try {
				asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_bearerServiceList);
				int pos = asnOs.StartContentDefiniteLength();
				for (ExtBearerServiceCode bearerItem: this.bearerServiceList) {
					((ExtBearerServiceCodeImpl) bearerItem).encodeAll(asnOs);
				}
				asnOs.FinalizeContent(pos);
			} catch (AsnException e) {
				throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
			}
		}

		if (this.teleserviceList != null) {
			try {
				asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_teleserviceList);
				int pos = asnOs.StartContentDefiniteLength();
				for (ExtTeleserviceCode teleserviceItem: this.teleserviceList) {
					((ExtTeleserviceCodeImpl) teleserviceItem).encodeAll(asnOs);
				}
				asnOs.FinalizeContent(pos);
			} catch (AsnException e) {
				throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
			}
		}

		if (this.provisionedSS != null) {
			try {
				asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_provisionedSS);
				int pos = asnOs.StartContentDefiniteLength();
				for (ExtSSInfo serviceItem: this.provisionedSS) {
					((ExtSSInfoImpl) serviceItem).encodeAll(asnOs);
				}
				asnOs.FinalizeContent(pos);
			} catch (AsnException e) {
				throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
			}
		}

		if (this.roamingRestrictionDueToUnsupportedFeature) {
			try {
				asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_roamingRestrictionDueToUnsupportedFeature);					
			} catch (IOException e) {
				throw new MAPException("IOException when encoding " + _PrimitiveName +  " parameter roamingRestrictionDueToUnsupportedFeature: " + e.getMessage(), e);
			} catch (AsnException e) {
				throw new MAPException("AsnException when encoding " + _PrimitiveName + " parameter roamingRestrictionDueToUnsupportedFeature: " + e.getMessage(), e);
			}
		}

		if (mapProtocolVersion >= 3) {
			if (this.extensionContainer != null)
				((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_extContainer);

			if (this.gprsSubscriptionData != null)
				((GPRSSubscriptionDataImpl) this.gprsSubscriptionData).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_gprsSubscriptionData);

			if (this.chargingCharacteristics != null) 
				((ChargingCharacteristicsImpl) this.chargingCharacteristics).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_chargingCharacteristics);

			if (this.roamingRestrictedInSgsnDueToUnsupportedFeature) {
				try {
					asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_roamingRestrictedInSgsnDueToUnsupportedFeature);					
				} catch (IOException e) {
					throw new MAPException("IOException when encoding " + _PrimitiveName +  " parameter roamingRestrictedInSgsnDueToUnsupportedFeature: " + e.getMessage(), e);
				} catch (AsnException e) {
					throw new MAPException("AsnException when encoding " + _PrimitiveName + " parameter roamingRestrictedInSgsnDueToUnsupportedFeature: " + e.getMessage(), e);
				}
			}

			if (this.networkAccessMode != null) {
				try {
					asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_networkAccessMode, this.networkAccessMode.getCode());
				} catch (IOException e) {
					throw new MAPException("IOException while encoding " + _PrimitiveName + " parameter networkAccessMode", e);
				} catch (AsnException e) {
					throw new MAPException("IOException while encoding " + _PrimitiveName + " parameter networkAccessMode", e);
				}
			}
		}
	}

	@Override
	public MAPMessageType getMessageType() {
		return MAPMessageType.insertSubscriberData_Request;
	}

	@Override
	public int getOperationCode() {
		return MAPOperationCode.insertSubscriberData;
	}

	@Override
	public IMSI getImsi() {
		return this.imsi;
	}

	@Override
	public ISDNAddressString getMsisdn() {
		return this.msisdn;
	}

	@Override
	public Category getCategory() {
		return this.category;
	}

	@Override
	public SubscriberStatus getSubscriberStatus() {
		return this.subscriberStatus;
	}

	@Override
	public ArrayList<ExtBearerServiceCode> getBearerServiceList() {
		return this.bearerServiceList;
	}

	@Override
	public ArrayList<ExtTeleserviceCode> getTeleserviceList() {
		return this.teleserviceList;
	}

	@Override
	public ArrayList<ExtSSInfo> getProvisionedSS() {
		return this.provisionedSS;
	}

	@Override
	public ODBData getODBData() {
		return this.odbData;
	}

	@Override
	public boolean getRoamingRestrictionDueToUnsupportedFeature() {
		return this.roamingRestrictionDueToUnsupportedFeature;
	}

	@Override
	public ArrayList<ZoneCode> getRegionalSubscriptionData() {
		return this.regionalSubscriptionData;
	}

	@Override
	public ArrayList<VoiceBroadcastData> getVbsSubscriptionData() {
		return this.vbsSubscriptionData;
	}

	@Override
	public ArrayList<VoiceGroupCallData> getVgcsSubscriptionData() {
		return this.vgcsSubscriptionData;
	}

	@Override
	public VlrCamelSubscriptionInfo getVlrCamelSubscriptionInfo() {
		return this.vlrCamelSubscriptionInfo;
	}

	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return this.extensionContainer;
	}

	@Override
	public NAEAPreferredCI getNAEAPreferredCI() {
		return this.naeaPreferredCI;
	}

	@Override
	public GPRSSubscriptionData getGPRSSubscriptionData() {
		return this.gprsSubscriptionData;
	}

	@Override
	public boolean getRoamingRestrictedInSgsnDueToUnsupportedFeature() {
		return this.roamingRestrictedInSgsnDueToUnsupportedFeature;
	}

	@Override
	public NetworkAccessMode getNetworkAccessMode() {
		return this.networkAccessMode;
	}

	@Override
	public LSAInformation getLSAInformation() {
		return this.lsaInformation;
	}

	@Override
	public boolean getLmuIndicator() {
		return this.lmuIndicator;
	}

	@Override
	public LCSInformation getLCSInformation() {
		return this.lcsInformation;
	}

	@Override
	public Integer getIstAlertTimer() {
		return this.istAlertTimer;
	}

	@Override
	public AgeIndicator getSuperChargerSupportedInHLR() {
		return this.superChargerSupportedInHLR;
	}

	@Override
	public MCSSInfo getMcSsInfo() {
		return this.mcSsInfo;
	}

	@Override
	public CSAllocationRetentionPriority getCSAllocationRetentionPriority() {
		return this.csAllocationRetentionPriority;
	}

	@Override
	public SGSNCAMELSubscriptionInfo getSgsnCamelSubscriptionInfo() {
		return this.sgsnCamelSubscriptionInfo;
	}

	@Override
	public ChargingCharacteristics getChargingCharacteristics() {
		return this.chargingCharacteristics;
	}

	@Override
	public AccessRestrictionData getAccessRestrictionData() {
		return this.accessRestrictionData;
	}

	@Override
	public Boolean getIcsIndicator() {
		return this.icsIndicator;
	}

	@Override
	public EPSSubscriptionData getEpsSubscriptionData() {
		return this.epsSubscriptionData;
	}

	@Override
	public ArrayList<CSGSubscriptionData> getCsgSubscriptionDataList() {
		return this.csgSubscriptionDataList;
	}

	@Override
	public boolean getUeReachabilityRequestIndicator() {
		return this.ueReachabilityRequestIndicator;
	}

	@Override
	public ISDNAddressString getSgsnNumber() {
		return this.sgsnNumber;
	}

	@Override
	public DiameterIdentity getMmeName() {
		return this.mmeName;
	}

	@Override
	public Long getSubscribedPeriodicRAUTAUtimer() {
		return this.subscribedPeriodicRAUTAUtimer;
	}

	@Override
	public boolean getVplmnLIPAAllowed() {
		return this.vplmnLIPAAllowed;
	}

	@Override
	public Boolean getMdtUserConsent() {
		return this.mdtUserConsent;
	}

	@Override
	public Long getSubscribedPeriodicLAUtimer() {
		return this.subscribedPeriodicLAUtimer;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(_PrimitiveName);
		sb.append(" [");

		if (this.imsi != null) {
			sb.append("imsi=");
			sb.append(this.imsi.toString());
			sb.append(", ");
		}

		if (this.msisdn != null) {
			sb.append("msisdn=");
			sb.append(this.msisdn.toString());
			sb.append(", ");
		}

		if (this.category != null) {
			sb.append("category=");
			sb.append(this.category.toString());
			sb.append(", ");
		}

		if (this.subscriberStatus != null) {
			sb.append("subscriberStatus=");
			sb.append(this.subscriberStatus.getCode());
			sb.append(", ");
		}

		if (this.bearerServiceList != null) {
			sb.append("bearerServiceList=");
			sb.append(this.bearerServiceList.toString());
			sb.append(", ");
		}

		if (this.teleserviceList != null) {
			sb.append("teleserviceList=");
			sb.append(this.teleserviceList.toString());
			sb.append(", ");
		}

		if (this.provisionedSS != null) {
			sb.append("provisionedSS=");
			sb.append(this.provisionedSS.toString());
			sb.append(", ");
		}

		if (this.roamingRestrictionDueToUnsupportedFeature) {
			sb.append("roamingRestrictionDueToUnsupportedFeature, ");
		}

		if (this.extensionContainer != null) {
			sb.append("extensionContainer=");
			sb.append(this.extensionContainer.toString());
			sb.append(", ");
		}
		
		if (this.chargingCharacteristics != null) {
			sb.append("chargingCharacteristics=");
			sb.append(this.chargingCharacteristics.toString());
			sb.append(", ");			
		}

		if (this.roamingRestrictedInSgsnDueToUnsupportedFeature) {
			sb.append("roamingRestrictedInSgsnDueToUnsupportedFeature, ");
		}

		if (this.networkAccessMode != null) {
			sb.append("networkAccessMode=");
			sb.append(this.networkAccessMode.toString());
			sb.append(", ");			
		}

		sb.append("mapProtocolVersion=");
		sb.append(this.mapProtocolVersion);

		sb.append("]");

		return sb.toString();
	}

}
