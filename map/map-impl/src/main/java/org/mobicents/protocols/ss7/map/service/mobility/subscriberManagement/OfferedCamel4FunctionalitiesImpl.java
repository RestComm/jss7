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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4Functionalities;
import org.mobicents.protocols.ss7.map.primitives.BitStringBase;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class OfferedCamel4FunctionalitiesImpl extends BitStringBase implements OfferedCamel4Functionalities {

    private static final int _ID_initiateCallAttempt = 0;
    private static final int _ID_splitLeg = 1;
    private static final int _ID_moveLeg = 2;
    private static final int _ID_disconnectLeg = 3;
    private static final int _ID_entityReleased = 4;
    private static final int _ID_dfcWithArgument = 5;
    private static final int _ID_playTone = 6;
    private static final int _ID_dtmfMidCall = 7;
    private static final int _ID_chargingIndicator = 8;
    private static final int _ID_alertingDP = 9;
    private static final int _ID_locationAtAlerting = 10;
    private static final int _ID_changeOfPositionDP = 11;
    private static final int _ID_orInteractions = 12;
    private static final int _ID_warningToneEnhancements = 13;
    private static final int _ID_cfEnhancements = 14;
    private static final int _ID_subscribedEnhancedDialledServices = 15;
    private static final int _ID_servingNetworkEnhancedDialledServices = 16;
    private static final int _ID_criteriaForChangeOfPositionDP = 17;
    private static final int _ID_serviceChangeDP = 18;
    private static final int _ID_collectInformation = 19;

    public OfferedCamel4FunctionalitiesImpl() {
        super(15, 64, 20, "OfferedCamel4FunctionalitiesImpl");
    }

    public OfferedCamel4FunctionalitiesImpl(boolean initiateCallAttempt,
            boolean splitLeg, boolean moveLeg, boolean disconnectLeg,
            boolean entityReleased, boolean dfcWithArgument, boolean playTone,
            boolean dtmfMidCall, boolean chargingIndicator, boolean alertingDP,
            boolean locationAtAlerting, boolean changeOfPositionDP,
            boolean orInteractions, boolean warningToneEnhancements,
            boolean cfEnhancements, boolean subscribedEnhancedDialledServices,
            boolean servingNetworkEnhancedDialledServices,
            boolean criteriaForChangeOfPositionDP, boolean serviceChangeDP,
            boolean collectInformation) {
        super(15, 64, 20, "OfferedCamel4FunctionalitiesImpl");

        if (initiateCallAttempt)
            this.bitString.set(_ID_initiateCallAttempt);
        if (splitLeg)
            this.bitString.set(_ID_splitLeg);
        if (moveLeg)
            this.bitString.set(_ID_moveLeg);
        if (disconnectLeg)
            this.bitString.set(_ID_disconnectLeg);
        if (entityReleased)
            this.bitString.set(_ID_entityReleased);
        if (dfcWithArgument)
            this.bitString.set(_ID_dfcWithArgument);
        if (playTone)
            this.bitString.set(_ID_playTone);
        if (dtmfMidCall)
            this.bitString.set(_ID_dtmfMidCall);
        if (chargingIndicator)
            this.bitString.set(_ID_chargingIndicator);
        if (alertingDP)
            this.bitString.set(_ID_alertingDP);
        if (locationAtAlerting)
            this.bitString.set(_ID_locationAtAlerting);
        if (changeOfPositionDP)
            this.bitString.set(_ID_changeOfPositionDP);
        if (orInteractions)
            this.bitString.set(_ID_orInteractions);
        if (warningToneEnhancements)
            this.bitString.set(_ID_warningToneEnhancements);
        if (cfEnhancements)
            this.bitString.set(_ID_cfEnhancements);
        if (subscribedEnhancedDialledServices)
            this.bitString.set(_ID_subscribedEnhancedDialledServices);
        if (servingNetworkEnhancedDialledServices)
            this.bitString.set(_ID_servingNetworkEnhancedDialledServices);
        if (criteriaForChangeOfPositionDP)
            this.bitString.set(_ID_criteriaForChangeOfPositionDP);
        if (serviceChangeDP)
            this.bitString.set(_ID_serviceChangeDP);
        if (collectInformation)
            this.bitString.set(_ID_collectInformation);
    }

    @Override
    public boolean getInitiateCallAttempt() {
        return this.bitString.get(_ID_initiateCallAttempt);
    }

    @Override
    public boolean getSplitLeg() {
        return this.bitString.get(_ID_splitLeg);
    }

    @Override
    public boolean getMoveLeg() {
        return this.bitString.get(_ID_moveLeg);
    }

    @Override
    public boolean getDisconnectLeg() {
        return this.bitString.get(_ID_disconnectLeg);
    }

    @Override
    public boolean getEntityReleased() {
        return this.bitString.get(_ID_entityReleased);
    }

    @Override
    public boolean getDfcWithArgument() {
        return this.bitString.get(_ID_dfcWithArgument);
    }

    @Override
    public boolean getPlayTone() {
        return this.bitString.get(_ID_playTone);
    }

    @Override
    public boolean getDtmfMidCall() {
        return this.bitString.get(_ID_dtmfMidCall);
    }

    @Override
    public boolean getChargingIndicator() {
        return this.bitString.get(_ID_chargingIndicator);
    }

    @Override
    public boolean getAlertingDP() {
        return this.bitString.get(_ID_alertingDP);
    }

    @Override
    public boolean getLocationAtAlerting() {
        return this.bitString.get(_ID_locationAtAlerting);
    }

    @Override
    public boolean getChangeOfPositionDP() {
        return this.bitString.get(_ID_changeOfPositionDP);
    }

    @Override
    public boolean getOrInteractions() {
        return this.bitString.get(_ID_orInteractions);
    }

    @Override
    public boolean getWarningToneEnhancements() {
        return this.bitString.get(_ID_warningToneEnhancements);
    }

    @Override
    public boolean getCfEnhancements() {
        return this.bitString.get(_ID_cfEnhancements);
    }

    @Override
    public boolean getSubscribedEnhancedDialledServices() {
        return this.bitString.get(_ID_subscribedEnhancedDialledServices);
    }

    @Override
    public boolean getServingNetworkEnhancedDialledServices() {
        return this.bitString.get(_ID_servingNetworkEnhancedDialledServices);
    }

    @Override
    public boolean getCriteriaForChangeOfPositionDP() {
        return this.bitString.get(_ID_criteriaForChangeOfPositionDP);
    }

    @Override
    public boolean getServiceChangeDP() {
        return this.bitString.get(_ID_serviceChangeDP);
    }

    @Override
    public boolean getCollectInformation() {
        return this.bitString.get(_ID_collectInformation);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OfferedCamel4FunctionalitiesImpl [");

        if (getInitiateCallAttempt())
            sb.append("initiateCallAttempt, ");
        if (getSplitLeg())
            sb.append("splitLeg, ");
        if (getMoveLeg())
            sb.append("moveLeg, ");
        if (getDisconnectLeg())
            sb.append("disconnectLeg, ");
        if (getEntityReleased())
            sb.append("entityReleased, ");
        if (getDfcWithArgument())
            sb.append("dfcWithArgument, ");
        if (getPlayTone())
            sb.append("playTone, ");
        if (getDtmfMidCall())
            sb.append("dtmfMidCall, ");
        if (getChargingIndicator())
            sb.append("chargingIndicator, ");
        if (getAlertingDP())
            sb.append("alertingDP, ");
        if (getLocationAtAlerting())
            sb.append("locationAtAlerting, ");
        if (getChangeOfPositionDP())
            sb.append("changeOfPositionDP, ");
        if (getOrInteractions())
            sb.append("orInteractions, ");
        if (getWarningToneEnhancements())
            sb.append("warningToneEnhancements, ");
        if (getCfEnhancements())
            sb.append("cfEnhancements, ");
        if (getSubscribedEnhancedDialledServices())
            sb.append("subscribedEnhancedDialledServices, ");
        if (getServingNetworkEnhancedDialledServices())
            sb.append("servingNetworkEnhancedDialledServices, ");
        if (getCriteriaForChangeOfPositionDP())
            sb.append("criteriaForChangeOfPositionDP, ");
        if (getServiceChangeDP())
            sb.append("serviceChangeDP, ");
        if (getCollectInformation())
            sb.append("collectInformation, ");

        sb.append("]");

        return sb.toString();
    }
}
