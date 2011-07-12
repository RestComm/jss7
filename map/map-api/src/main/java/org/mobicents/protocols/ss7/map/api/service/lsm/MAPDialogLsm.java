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
package org.mobicents.protocols.ss7.map.api.service.lsm;

import java.util.BitSet;

import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 * @author amit bhayani
 * 
 */
public interface MAPDialogLsm extends MAPDialog {

	public Long addProvideSubscriberLocationRequest(LocationType locationType, AddressString mLCNumber, LCSClientID lCSClientId, boolean privacyOverride,
			IMSI iMSI, AddressString mSISDN, LMSI lmsi, LCSQoS lCSQos, String iMEI, MAPExtensionContainer extensionContainer, BitSet supportedGADShapes,
			String lCSReferenceNumber, LCSCodeword lCSCodeword, int lCSServiceTypeID, LCSPrivacyCheck lCSPrivacyCheck, AreaEventInfo areaEventInfo,
			String hGMLCAddress);

	public void addProvideSubscriberLocationResponse(long invokeId, String locationEstimate, String geranPositioningData, String utranPositioningData,
			int ageOfLocationEstimate, String additionalLocationEstimate, MAPExtensionContainer extensionContainer, boolean deferredMTLRResponseIndicator,
			CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI, boolean saiPresent, AccuracyFulfilmentIndicator accuracyFulfilmentIndicator);

	public void addSubscriberLocationReportRequestIndication(LCSEvent lCSEvent, LCSClientID lCSClientID, LCSLocationInfo lCSLocationInfo, IMSI iMSI,
			AddressString mSISDN, AddressString naESRD, AddressString naESRK, String iMEI, String locationEstimate, String gERANPositioningData,
			String uTRANPositioningData, int ageOfLocationEstimate, String additionalLocationEstimate, DeferredmtlrData deferredmtlrData,
			String lCSReferenceNumber, CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI, String hGMLCAddress, int lCSServiceTypeID,
			boolean pseudonymIndicator, AccuracyFulfilmentIndicator accuracyFulfilmentIndicator);

	public void addSubscriberLocationReportResponseIndication(long invokeId, MAPExtensionContainer extensionContainer, AddressString naESRK,
			AddressString naESRD);

}
