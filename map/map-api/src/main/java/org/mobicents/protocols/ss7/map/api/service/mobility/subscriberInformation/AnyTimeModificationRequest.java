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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation;

import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.mobicents.protocols.ss7.map.api.service.mobility.MobilityMessage;

/**	
 * 

anyTimeModification  OPERATION ::= {				--Timer m
	ARGUMENT
		AnyTimeModificationArg
	RESULT
		AnyTimeModificationRes
	ERRORS {
		atm-NotAllowed |
		dataMissing |
		unexpectedDataValue |
		unknownSubscriber |
		bearerServiceNotProvisioned |
		teleserviceNotProvisioned |
		callBarred |
		illegalSS-Operation |
		ss-SubscriptionViolation |
		ss-ErrorStatus |
		ss-Incompatibility |
		informationNotAvailable}
	CODE	local:65 }

AnyTimeModificationArg ::= SEQUENCE {
	subscriberIdentity	[0]	SubscriberIdentity,
	gsmSCF-Address	[1]	ISDN-AddressString,
	modificationRequestFor-CF-Info	[2]	ModificationRequestFor-CF-Info OPTIONAL,
	modificationRequestFor-CB-Info	[3]	ModificationRequestFor-CB-Info OPTIONAL,
	modificationRequestFor-CSI	[4]	ModificationRequestFor-CSI	OPTIONAL,
	extensionContainer	[5]	ExtensionContainer	OPTIONAL,
	longFTN-Supported	[6]	NULL		OPTIONAL,
	...,
	modificationRequestFor-ODB-data	[7]	ModificationRequestFor-ODB-data OPTIONAL,
	modificationRequestFor-IP-SM-GW-Data	[8]	ModificationRequestFor-IP-SM-GW-Data OPTIONAL,
	activationRequestForUE-reachability	[9]	RequestedServingNode	OPTIONAL,
	modificationRequestFor-CSG	[10]	ModificationRequestFor-CSG	OPTIONAL,
	modificationRequestFor-CW-Data	[11] ModificationRequestFor-CW-Info	OPTIONAL,
	modificationRequestFor-CLIP-Data	[12] ModificationRequestFor-CLIP-Info	OPTIONAL,
	modificationRequestFor-CLIR-Data	[13] ModificationRequestFor-CLIR-Info 	OPTIONAL,
	modificationRequestFor-HOLD-Data	[14] ModificationRequestFor-CH-Info	OPTIONAL,
	modificationRequestFor-ECT-Data	[15] ModificationRequestFor-ECT-Info	OPTIONAL }

 * 
 * @author sergey vetyutnev
 *
 */
public interface AnyTimeModificationRequest extends MobilityMessage {

	public SubscriberIdentity getSubscriberIdentity();

	public ISDNAddressString getGsmSCFAddress();

	public ModificationRequestForCFInfo getModificationRequestForCfInfo();

	public ModificationRequestForCBInfo getModificationRequestForCbInfo();

	public ModificationRequestForCSI getModificationRequestForCSI();

	public MAPExtensionContainer getExtensionContainer();

	public boolean getLongFTNSupported();

	public ModificationRequestForODBdata getModificationRequestForODBdata();

	public ModificationRequestForIPSMGWData getModificationRequestForIpSmGwData();

	public RequestedServingNode getActivationRequestForUEReachability();

	public ModificationRequestForCSG getModificationRequestForCSG();

	public ModificationRequestForCWInfo getModificationRequestForCwData();

	public ModificationRequestForCLIPInfo getModificationRequestForClipData();

	public ModificationRequestForCLIRInfo getModificationRequestForClirData();

	public ModificationRequestForCHInfo getModificationRequestForHoldData();

	public ModificationRequestForECTInfo getModificationRequestForEctData();

}
