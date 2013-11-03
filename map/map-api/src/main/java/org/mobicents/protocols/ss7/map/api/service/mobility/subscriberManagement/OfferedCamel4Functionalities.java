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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement;

import java.io.Serializable;

/**
 *
OfferedCamel4Functionalities ::= BIT STRING {
 initiateCallAttempt (0), splitLeg (1), moveLeg (2), disconnectLeg (3),
 entityReleased (4), dfc-WithArgument (5), playTone (6), dtmf-MidCall (7),
 chargingIndicator (8), alertingDP (9), locationAtAlerting (10), changeOfPositionDP (11),
 or-Interactions (12), warningToneEnhancements (13), cf-Enhancements (14),
 subscribedEnhancedDialledServices (15), servingNetworkEnhancedDialledServices (16),
 criteriaForChangeOfPositionDP (17), serviceChangeDP (18), collectInformation (19)
} (SIZE (15..64)) -- A node supporting Camel phase 4 shall mark in the BIT STRING all CAMEL4
-- functionalities it offers.
-- Other values than listed above shall be discarded.
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface OfferedCamel4Functionalities extends Serializable {

    boolean getInitiateCallAttempt();

    boolean getSplitLeg();

    boolean getMoveLeg();

    boolean getDisconnectLeg();

    boolean getEntityReleased();

    boolean getDfcWithArgument();

    boolean getPlayTone();

    boolean getDtmfMidCall();

    boolean getChargingIndicator();

    boolean getAlertingDP();

    boolean getLocationAtAlerting();

    boolean getChangeOfPositionDP();

    boolean getOrInteractions();

    boolean getWarningToneEnhancements();

    boolean getCfEnhancements();

    boolean getSubscribedEnhancedDialledServices();

    boolean getServingNetworkEnhancedDialledServices();

    boolean getCriteriaForChangeOfPositionDP();

    boolean getServiceChangeDP();

    boolean getCollectInformation();

}
