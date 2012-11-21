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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement;

import java.util.ArrayList;

import org.mobicents.protocols.ss7.map.api.primitives.DiameterIdentity;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NAEAPreferredCI;
import org.mobicents.protocols.ss7.map.api.service.mobility.MobilityMessage;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.AgeIndicator;

/**
 * 

MAP V1-2-3:

MAP V3:
insertSubscriberData  OPERATION ::= {				--Timer m
	ARGUMENT
		InsertSubscriberDataArg
	RESULT
		InsertSubscriberDataRes
		-- optional
	ERRORS {
		dataMissing |
		unexpectedDataValue |
		unidentifiedSubscriber}
	CODE	local:7 }


MAP V2:
InsertSubscriberData ::= OPERATION--Timer m
ARGUMENT
	insertSubscriberDataArg		InsertSubscriberDataArg
RESULT
	insertSubscriberDataRes		InsertSubscriberDataRes
	-- optional
	-- insertSubscriberDataRes must be absent in version 1
ERRORS {
	DataMissing,
	UnexpectedDataValue,
	UnidentifiedSubscriber}

MAP V3:
InsertSubscriberDataArg ::= SEQUENCE {
	imsi			[0] IMSI		OPTIONAL,
	COMPONENTS OF	SubscriberData,
	extensionContainer	[14] ExtensionContainer	OPTIONAL,
	... ,	
	naea-PreferredCI	[15] NAEA-PreferredCI	OPTIONAL,
	-- naea-PreferredCI is included at the discretion of the HLR operator.
	gprsSubscriptionData	[16] GPRSSubscriptionData	OPTIONAL,
	roamingRestrictedInSgsnDueToUnsupportedFeature [23] 	NULL	
							OPTIONAL, 
	networkAccessMode	[24] NetworkAccessMode	OPTIONAL,
	lsaInformation	[25] LSAInformation	OPTIONAL,
	lmu-Indicator	[21]	NULL		OPTIONAL,
	lcsInformation	[22]	LCSInformation	OPTIONAL,
	istAlertTimer	[26] IST-AlertTimerValue	OPTIONAL,
	superChargerSupportedInHLR	[27] AgeIndicator	OPTIONAL,
	mc-SS-Info	[28] MC-SS-Info	OPTIONAL,
	cs-AllocationRetentionPriority	[29] CS-AllocationRetentionPriority		OPTIONAL,
	sgsn-CAMEL-SubscriptionInfo	[17] SGSN-CAMEL-SubscriptionInfo	OPTIONAL,
	chargingCharacteristics 	[18]	ChargingCharacteristics 	OPTIONAL,
	accessRestrictionData	[19] AccessRestrictionData	OPTIONAL,
	ics-Indicator	[20]	BOOLEAN	OPTIONAL,
	eps-SubscriptionData	[31]	EPS-SubscriptionData	OPTIONAL,
	csg-SubscriptionDataList	[32] CSG-SubscriptionDataList	OPTIONAL,
	ue-ReachabilityRequestIndicator	[33]	NULL		OPTIONAL,
	sgsn-Number	[34]	ISDN-AddressString	OPTIONAL,
	mme-Name		[35]	DiameterIdentity	OPTIONAL,
	subscribedPeriodicRAUTAUtimer	[36]	SubscribedPeriodicRAUTAUtimer	OPTIONAL,
	vplmnLIPAAllowed	[37]	NULL		OPTIONAL,
	mdtUserConsent	[38]	BOOLEAN	OPTIONAL,
	subscribedPeriodicLAUtimer	[39]	SubscribedPeriodicLAUtimer	OPTIONAL }
	-- If the Network Access Mode parameter is sent, it shall be present only in 
	-- the first sequence if seqmentation is used

MAP V2:
InsertSubscriberDataArg ::= SEQUENCE {
	imsi[0] 		IMSI	OPTIONAL,
	COMPONENTS OF	SubscriberData,
...}

SubscriberData ::= SEQUENCE {
	msisdn		[1] ISDN-AddressString	OPTIONAL,
	category		[2] Category	OPTIONAL,
	subscriberStatus	[3] SubscriberStatus	OPTIONAL,
	bearerServiceList	[4] BearerServiceList	OPTIONAL,
	-- The exception handling for reception of unsupported / not allocated
	-- bearerServiceCodes is defined in section 8.8.1
	teleserviceList	[6] TeleserviceList	OPTIONAL,
	-- The exception handling for reception of unsupported / not allocated
	-- teleserviceCodes is defined in section 8.8.1
	provisionedSS	[7] Ext-SS-InfoList	OPTIONAL,
	odb-Data		[8] ODB-Data	OPTIONAL,
	roamingRestrictionDueToUnsupportedFeature  [9] NULL	OPTIONAL,
	regionalSubscriptionData	[10] ZoneCodeList	OPTIONAL,
	vbsSubscriptionData	[11] VBSDataList	OPTIONAL,
	vgcsSubscriptionData	[12] VGCSDataList	OPTIONAL,
	vlrCamelSubscriptionInfo	[13] VlrCamelSubscriptionInfo	OPTIONAL
	}

BearerServiceList ::= SEQUENCE SIZE (1..50) OF Ext-BearerServiceCode

TeleserviceList ::= SEQUENCE SIZE (1..20) OF Ext-TeleserviceCode

Ext-SS-InfoList ::= SEQUENCE SIZE (1..30) OF Ext-SS-Info

ZoneCodeList ::= SEQUENCE SIZE (1..10) OF ZoneCode

VBSDataList ::= SEQUENCE SIZE (1..50) OF VoiceBroadcastData

VGCSDataList ::= SEQUENCE SIZE (1..50) OF VoiceGroupCallData

IST-AlertTimerValue ::= INTEGER (15..255)

CSG-SubscriptionDataList ::= SEQUENCE SIZE (1..50) OF CSG-SubscriptionData

SubscribedPeriodicRAUTAUtimer ::= INTEGER (0..4294967295)
	-- This parameter carries the subscribed periodic TAU/RAU timer value in seconds.

SubscribedPeriodicLAUtimer ::= INTEGER (0..4294967295)
	-- This parameter carries the subscribed periodic LAU timer value in seconds.

 * 
 * 
 * @author sergey vetyutnev
 * 
 */
public interface InsertSubscriberDataRequest extends MobilityMessage {

	public IMSI getImsi();

	public ISDNAddressString getMsisdn();

	public Category getCategory();

	public SubscriberStatus getSubscriberStatus();

	public ArrayList<ExtBearerServiceCode> getBearerServiceList();

	public ArrayList<ExtTeleserviceCode> getTeleserviceList();

	public ArrayList<ExtSSInfo> getProvisionedSS();

	public ODBData getODBData();

	public boolean getRoamingRestrictionDueToUnsupportedFeature();

	public ArrayList<ZoneCode> getRegionalSubscriptionData();

	public ArrayList<VoiceBroadcastData> getVbsSubscriptionData();

	public ArrayList<VoiceGroupCallData> getVgcsSubscriptionData();

	public VlrCamelSubscriptionInfo getVlrCamelSubscriptionInfo();

	public MAPExtensionContainer getExtensionContainer();

	public NAEAPreferredCI getNAEAPreferredCI();

	public GPRSSubscriptionData getGPRSSubscriptionData();

	public boolean getRoamingRestrictedInSgsnDueToUnsupportedFeature();

	public NetworkAccessMode getNetworkAccessMode();

	public LSAInformation getLSAInformation();

	public boolean getLmuIndicator();

	public LCSInformation getLCSInformation();

	public Integer getIstAlertTimer();

	public AgeIndicator getSuperChargerSupportedInHLR();

	public MCSSInfo getMcSsInfo();

	public CSAllocationRetentionPriority getCSAllocationRetentionPriority();

	public SGSNCAMELSubscriptionInfo getSgsnCamelSubscriptionInfo();

	public ChargingCharacteristics getChargingCharacteristics();

	public AccessRestrictionData getAccessRestrictionData();

	public Boolean getIcsIndicator();

	public EPSSubscriptionData getEpsSubscriptionData();

	public ArrayList<CSGSubscriptionData> getCsgSubscriptionDataList();

	public boolean getUeReachabilityRequestIndicator();

	public ISDNAddressString getSgsnNumber();

	public DiameterIdentity getMmeName();

	public Long getSubscribedPeriodicRAUTAUtimer();

	public boolean getVplmnLIPAAllowed();

	public Boolean getMdtUserConsent();

	public Long getSubscribedPeriodicLAUtimer();

}
