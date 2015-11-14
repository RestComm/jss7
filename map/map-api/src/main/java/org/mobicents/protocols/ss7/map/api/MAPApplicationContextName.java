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

package org.mobicents.protocols.ss7.map.api;

import java.util.EnumSet;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public enum MAPApplicationContextName {
    /**
     * Look at http://www.oid-info.com/get/0.4.0.0.1.0.19.2
     */

    // -- Mobility Services
    // --- Location management services
    networkLocUpContext(1), locationCancellationContext(2), interVlrInfoRetrievalContext(15), msPurgingContext(27), gprsLocationUpdateContext(
            32), mmEventReportingContext(42),

    // --- Handover services
    handoverControlContext(11),

    // --- Authentication management services
    infoRetrievalContext(14), authenticationFailureReportContext(39),

    // --- IMEI management services
    equipmentMngtContext(13),

    // --- Subscriber management services
    subscriberDataMngtContext(16),

    // --- Fault recovery services
    resetContext(10),
    // networkLocUpContext(1), doubled in Location management services

    // --- Subscriber Information services
    anyTimeEnquiryContext(29), subscriberInfoEnquiryContext(28), anyTimeInfoHandlingContext(43), subscriberDataModificationNotificationContext(
            22),

    // -- oam
    tracingContext(17),
    // handoverControlContext(11), doubled in Handover services
    imsiRetrievalContext(26),

    // -- Call Handling Services
    locationInfoRetrievalContext(5), roamingNumberEnquiryContext(3), callControlTransferContext(6), groupCallControlContext(31), groupCallInfoRetrievalContext(
            45), reportingContext(7), istAlertingContext(4), ServiceTerminationContext(9), resourceManagementContext(44),

    // -- Supplementary services
    networkFunctionalSsContext(18), networkUnstructuredSsContext(19), ssInvocationNotificationContext(36), callCompletionContext(
            8),

    // -- short message service
    shortMsgGatewayContext(20), shortMsgMORelayContext(21), shortMsgMTRelayContext(25), shortMsgMTVgcsRelayContext(41), shortMsgAlertContext(
            23), mwdMngtContext(24),

    // -- Network-Requested PDP Context Activation services
    gprsLocationInfoRetrievalContext(33), failureReportContext(34), gprsNotifyContext(35),

    // -- Location Service (lms)
    locationSvcEnquiryContext(38), locationSvcGatewayContext(37);

    private int code;

    private MAPApplicationContextName(int code) {
        this.code = code;
    }

    public int getApplicationContextCode() {
        return this.code;
    }

    public static MAPApplicationContextName getInstance(Long code) {

        EnumSet<MAPApplicationContextName> lst = EnumSet.allOf(MAPApplicationContextName.class);
        for (MAPApplicationContextName el : lst) {
            if (el.code == code)
                return el;
        }

        return null;
    }

}
