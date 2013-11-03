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

package org.mobicents.protocols.ss7.map.api.service.callhandling;

import java.io.Serializable;
import java.util.ArrayList;

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmCamelTdpCriteria;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmCamelTdpCriteria;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TCSI;

/**
 *
 GmscCamelSubscriptionInfo ::= SEQUENCE { t-CSI [0] T-CSI OPTIONAL, o-CSI [1] O-CSI OPTIONAL, extensionContainer [2]
 * ExtensionContainer OPTIONAL, ..., o-BcsmCamelTDP-CriteriaList [3] O-BcsmCamelTDPCriteriaList OPTIONAL,
 * t-BCSM-CAMEL-TDP-CriteriaList [4] T-BCSM-CAMEL-TDP-CriteriaList OPTIONAL, d-csi [5] D-CSI OPTIONAL}
 *
 * O-BcsmCamelTDPCriteriaList ::= SEQUENCE SIZE (1..10) OF O-BcsmCamelTDP-Criteria
 *
 * T-BCSM-CAMEL-TDP-CriteriaList ::= SEQUENCE SIZE (1..10) OF T-BCSM-CAMEL-TDP-Criteria
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface GmscCamelSubscriptionInfo extends Serializable {

     TCSI getTCsi();

     OCSI getOCsi();

     MAPExtensionContainer getMAPExtensionContainer();

     ArrayList<OBcsmCamelTdpCriteria> getOBcsmCamelTdpCriteriaList();

     ArrayList<TBcsmCamelTdpCriteria> getTBcsmCamelTdpCriteriaList();

     DCSI getDCsi();

}
