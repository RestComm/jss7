/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.protocols.ss7.map.api.service.lsm;

import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.SubscriberIdentity;

/**
 * @author amit bhayani
 *
 */
public interface MAPDialogLsm extends MAPDialog {

    Long addProvideSubscriberLocationRequest(LocationType locationType, ISDNAddressString mlcNumber,
            LCSClientID lcsClientID, boolean privacyOverride, IMSI imsi, ISDNAddressString msisdn, LMSI lmsi, IMEI imei,
            LCSPriority lcsPriority, LCSQoS lcsQoS, MAPExtensionContainer extensionContainer,
            SupportedGADShapes supportedGADShapes, Integer lcsReferenceNumber, Integer lcsServiceTypeID,
            LCSCodeword lcsCodeword, LCSPrivacyCheck lcsPrivacyCheck, AreaEventInfo areaEventInfo, GSNAddress hgmlcAddress,
            boolean moLrShortCircuitIndicator, PeriodicLDRInfo periodicLDRInfo, ReportingPLMNList reportingPLMNList)
            throws MAPException;

    Long addProvideSubscriberLocationRequest(int customInvokeTimeout, LocationType locationType,
            ISDNAddressString mlcNumber, LCSClientID lcsClientID, boolean privacyOverride, IMSI imsi, ISDNAddressString msisdn,
            LMSI lmsi, IMEI imei, LCSPriority lcsPriority, LCSQoS lcsQoS, MAPExtensionContainer extensionContainer,
            SupportedGADShapes supportedGADShapes, Integer lcsReferenceNumber, Integer lcsServiceTypeID,
            LCSCodeword lcsCodeword, LCSPrivacyCheck lcsPrivacyCheck, AreaEventInfo areaEventInfo, GSNAddress hgmlcAddress,
            boolean moLrShortCircuitIndicator, PeriodicLDRInfo periodicLDRInfo, ReportingPLMNList reportingPLMNList)
            throws MAPException;

    void addProvideSubscriberLocationResponse(long invokeId, ExtGeographicalInformation locationEstimate,
            PositioningDataInformation geranPositioningData, UtranPositioningDataInfo utranPositioningData,
            Integer ageOfLocationEstimate, AddGeographicalInformation additionalLocationEstimate,
            MAPExtensionContainer extensionContainer, boolean deferredMTLRResponseIndicator,
            CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI, boolean saiPresent,
            AccuracyFulfilmentIndicator accuracyFulfilmentIndicator, VelocityEstimate velocityEstimate,
            boolean moLrShortCircuitIndicator, GeranGANSSpositioningData geranGANSSpositioningData,
            UtranGANSSpositioningData utranGANSSpositioningData, ServingNodeAddress targetServingNodeForHandover)
            throws MAPException;

    Long addSubscriberLocationReportRequest(LCSEvent lcsEvent, LCSClientID lcsClientID, LCSLocationInfo lcsLocationInfo,
            ISDNAddressString msisdn, IMSI imsi, IMEI imei, ISDNAddressString naEsrd, ISDNAddressString naEsrk,
            ExtGeographicalInformation locationEstimate, Integer ageOfLocationEstimate,
            SLRArgExtensionContainer slrArgExtensionContainer, AddGeographicalInformation addLocationEstimate,
            DeferredmtlrData deferredmtlrData, Integer lcsReferenceNumber, PositioningDataInformation geranPositioningData,
            UtranPositioningDataInfo utranPositioningData, CellGlobalIdOrServiceAreaIdOrLAI cellIdOrSai,
            GSNAddress hgmlcAddress, Integer lcsServiceTypeID, boolean saiPresent, boolean pseudonymIndicator,
            AccuracyFulfilmentIndicator accuracyFulfilmentIndicator, VelocityEstimate velocityEstimate, Integer sequenceNumber,
            PeriodicLDRInfo periodicLDRInfo, boolean moLrShortCircuitIndicator,
            GeranGANSSpositioningData geranGANSSpositioningData, UtranGANSSpositioningData utranGANSSpositioningData,
            ServingNodeAddress targetServingNodeForHandover) throws MAPException;

    Long addSubscriberLocationReportRequest(int customInvokeTimeout, LCSEvent lcsEvent, LCSClientID lcsClientID,
            LCSLocationInfo lcsLocationInfo, ISDNAddressString msisdn, IMSI imsi, IMEI imei, ISDNAddressString naEsrd,
            ISDNAddressString naEsrk, ExtGeographicalInformation locationEstimate, Integer ageOfLocationEstimate,
            SLRArgExtensionContainer slrArgExtensionContainer, AddGeographicalInformation addLocationEstimate,
            DeferredmtlrData deferredmtlrData, Integer lcsReferenceNumber, PositioningDataInformation geranPositioningData,
            UtranPositioningDataInfo utranPositioningData, CellGlobalIdOrServiceAreaIdOrLAI cellIdOrSai,
            GSNAddress hgmlcAddress, Integer lcsServiceTypeID, boolean saiPresent, boolean pseudonymIndicator,
            AccuracyFulfilmentIndicator accuracyFulfilmentIndicator, VelocityEstimate velocityEstimate, Integer sequenceNumber,
            PeriodicLDRInfo periodicLDRInfo, boolean moLrShortCircuitIndicator,
            GeranGANSSpositioningData geranGANSSpositioningData, UtranGANSSpositioningData utranGANSSpositioningData,
            ServingNodeAddress targetServingNodeForHandover) throws MAPException;

    void addSubscriberLocationReportResponse(long invokeId, ISDNAddressString naEsrd, ISDNAddressString naEsrk,
            MAPExtensionContainer extensionContainer) throws MAPException;

    Long addSendRoutingInfoForLCSRequest(ISDNAddressString mlcNumber, SubscriberIdentity targetMS,
            MAPExtensionContainer extensionContainer) throws MAPException;

    Long addSendRoutingInfoForLCSRequest(int customInvokeTimeout, ISDNAddressString mlcNumber,
            SubscriberIdentity targetMS, MAPExtensionContainer extensionContainer) throws MAPException;

    void addSendRoutingInfoForLCSResponse(long invokeId, SubscriberIdentity targetMS, LCSLocationInfo lcsLocationInfo,
            MAPExtensionContainer extensionContainer, GSNAddress vgmlcAddress, GSNAddress hGmlcAddress, GSNAddress pprAddress,
            GSNAddress additionalVGmlcAddress) throws MAPException;

}
