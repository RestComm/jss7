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

package org.mobicents.protocols.ss7.map.api.service.lsm;

import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 * @author amit bhayani
 * 
 */
public interface MAPDialogLsm extends MAPDialog {

	public Long addProvideSubscriberLocationRequest(LocationType locationType, ISDNAddressString mlcNumber, LCSClientID lcsClientID, Boolean privacyOverride,
			IMSI imsi, ISDNAddressString msisdn, LMSI lmsi, IMEI imei, Integer lcsPriority, LCSQoS lcsQoS, MAPExtensionContainer extensionContainer,
			SupportedGADShapes supportedGADShapes, Byte lcsReferenceNumber, Integer lcsServiceTypeID, LCSCodeword lcsCodeword, LCSPrivacyCheck lcsPrivacyCheck,
			AreaEventInfo areaEventInfo, byte[] hgmlcAddress) throws MAPException;

	public void addProvideSubscriberLocationResponse(long invokeId, byte[] locationEstimate, byte[] geranPositioningData, byte[] utranPositioningData,
			Integer ageOfLocationEstimate, byte[] additionalLocationEstimate, MAPExtensionContainer extensionContainer, Boolean deferredMTLRResponseIndicator,
			CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI, Boolean saiPresent, AccuracyFulfilmentIndicator accuracyFulfilmentIndicator)
			throws MAPException;

	public Long addSubscriberLocationReportRequest(LCSEvent lcsEvent, LCSClientID lcsClientID, LCSLocationInfo lcsLocationInfo, ISDNAddressString msisdn,
			IMSI imsi, IMEI imei, ISDNAddressString naEsrd, ISDNAddressString naEsrk, byte[] locationEstimate, Integer ageOfLocationEstimate,
			SLRArgExtensionContainer slrArgExtensionContainer, byte[] addLocationEstimate, DeferredmtlrData deferredmtlrData, Byte lcsReferenceNumber,
			byte[] geranPositioningData, byte[] utranPositioningData, CellGlobalIdOrServiceAreaIdOrLAI cellIdOrSai, byte[] hgmlcAddress,
			Integer lcsServiceTypeID, Boolean saiPresent, Boolean pseudonymIndicator, AccuracyFulfilmentIndicator accuracyFulfilmentIndicator)
			throws MAPException;

	public void addSubscriberLocationReportResponse(long invokeId, ISDNAddressString naEsrd, ISDNAddressString naEsrk, MAPExtensionContainer extensionContainer)
			throws MAPException;

	public Long addSendRoutingInfoForLCSRequest(ISDNAddressString mlcNumber, SubscriberIdentity targetMS, MAPExtensionContainer extensionContainer)
			throws MAPException;

	public void addSendRoutingInfoForLCSResponse(long invokeId, SubscriberIdentity targetMS, LCSLocationInfo lcsLocationInfo,
			MAPExtensionContainer extensionContainer, byte[] vgmlcAddress, byte[] hGmlcAddress, byte[] pprAddress, byte[] additionalVGmlcAddress)
			throws MAPException;

}
