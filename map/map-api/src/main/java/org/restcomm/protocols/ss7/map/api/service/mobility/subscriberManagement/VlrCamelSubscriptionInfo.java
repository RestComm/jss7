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

package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import java.io.Serializable;
import java.util.ArrayList;

import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 *
 VlrCamelSubscriptionInfo ::= SEQUENCE { o-CSI [0] O-CSI OPTIONAL, extensionContainer [1] ExtensionContainer OPTIONAL, ...,
 * ss-CSI [2] SS-CSI OPTIONAL, o-BcsmCamelTDP-CriteriaList [4] O-BcsmCamelTDPCriteriaList OPTIONAL, tif-CSI [3] NULL OPTIONAL,
 * m-CSI [5] M-CSI OPTIONAL, mo-sms-CSI [6] SMS-CSI OPTIONAL, vt-CSI [7] T-CSI OPTIONAL, t-BCSM-CAMEL-TDP-CriteriaList [8]
 * T-BCSM-CAMEL-TDP-CriteriaList OPTIONAL, d-CSI [9] D-CSI OPTIONAL, mt-sms-CSI [10] SMS-CSI OPTIONAL,
 * mt-smsCAMELTDP-CriteriaList [11] MT-smsCAMELTDP-CriteriaList OPTIONAL }
 *
 * O-BcsmCamelTDPCriteriaList ::= SEQUENCE SIZE (1..10) OF O-BcsmCamelTDP-Criteria
 *
 * T-BCSM-CAMEL-TDP-CriteriaList ::= SEQUENCE SIZE (1..10) OF T-BCSM-CAMEL-TDP-Criteria
 *
 * MT-smsCAMELTDP-CriteriaList ::= SEQUENCE SIZE (1.. 10) OF MT-smsCAMELTDP-Criteria
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface VlrCamelSubscriptionInfo extends Serializable {

    OCSI getOCsi();

    MAPExtensionContainer getExtensionContainer();

    SSCSI getSsCsi();

    ArrayList<OBcsmCamelTdpCriteria> getOBcsmCamelTDPCriteriaList();

    boolean getTifCsi();

    MCSI getMCsi();

    SMSCSI getSmsCsi();

    TCSI getVtCsi();

    ArrayList<TBcsmCamelTdpCriteria> getTBcsmCamelTdpCriteriaList();

    DCSI getDCsi();

    SMSCSI getMtSmsCSI();

    ArrayList<MTsmsCAMELTDPCriteria> getMtSmsCamelTdpCriteriaList();

}
