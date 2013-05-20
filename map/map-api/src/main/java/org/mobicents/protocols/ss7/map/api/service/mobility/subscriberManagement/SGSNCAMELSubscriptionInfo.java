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

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 *
 SGSN-CAMEL-SubscriptionInfo ::= SEQUENCE { gprs-CSI [0] GPRS-CSI OPTIONAL, mo-sms-CSI [1] SMS-CSI OPTIONAL,
 * extensionContainer [2] ExtensionContainer OPTIONAL, ..., mt-sms-CSI [3] SMS-CSI OPTIONAL, mt-smsCAMELTDP-CriteriaList [4]
 * MT-smsCAMELTDP-CriteriaList OPTIONAL, mg-csi [5] MG-CSI OPTIONAL }
 *
 * MT-smsCAMELTDP-CriteriaList ::= SEQUENCE SIZE (1.. 10) OF MT-smsCAMELTDP-Criteria
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface SGSNCAMELSubscriptionInfo {

    GPRSCSI getGprsCsi();

    SMSCSI getMoSmsCsi();

    MAPExtensionContainer getExtensionContainer();

    SMSCSI getMtSmsCsi();

    ArrayList<MTsmsCAMELTDPCriteria> getMtSmsCamelTdpCriteriaList();

    MGCSI getMgCsi();

}
