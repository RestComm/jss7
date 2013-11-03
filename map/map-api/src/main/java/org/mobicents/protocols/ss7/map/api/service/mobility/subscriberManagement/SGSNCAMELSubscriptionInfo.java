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
public interface SGSNCAMELSubscriptionInfo extends Serializable {

    GPRSCSI getGprsCsi();

    SMSCSI getMoSmsCsi();

    MAPExtensionContainer getExtensionContainer();

    SMSCSI getMtSmsCsi();

    ArrayList<MTsmsCAMELTDPCriteria> getMtSmsCamelTdpCriteriaList();

    MGCSI getMgCsi();

}
