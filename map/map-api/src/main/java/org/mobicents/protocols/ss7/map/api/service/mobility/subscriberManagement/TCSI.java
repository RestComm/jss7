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
 T-CSI ::= SEQUENCE { t-BcsmCamelTDPDataList T-BcsmCamelTDPDataList, extensionContainer ExtensionContainer OPTIONAL, ...,
 * camelCapabilityHandling [0] CamelCapabilityHandling OPTIONAL, notificationToCSE [1] NULL OPTIONAL, csi-Active [2] NULL
 * OPTIONAL} -- notificationToCSE and csi-Active shall not be present when VT-CSI/T-CSI is sent -- to VLR/GMSC. -- They may only
 * be included in ATSI/ATM ack/NSDC message. -- T-CSI shall not be segmented.
 *
 * T-BcsmCamelTDPDataList ::= SEQUENCE SIZE (1..10) OF T-BcsmCamelTDPData --- T-BcsmCamelTDPDataList shall not contain more than
 * one instance of --- T-BcsmCamelTDPData containing the same value for t-BcsmTriggerDetectionPoint. --- For CAMEL Phase 2, this
 * means that only one instance of T-BcsmCamelTDPData is allowed --- with t-BcsmTriggerDetectionPoint being equal to DP12. ---
 * For CAMEL Phase 3, more TDPs are allowed.
 *
 * CamelCapabilityHandling ::= INTEGER(1..16) -- value 1 = CAMEL phase 1, -- value 2 = CAMEL phase 2, -- value 3 = CAMEL Phase
 * 3, -- value 4 = CAMEL phase 4: -- reception of values greater than 4 shall be treated as CAMEL phase 4.
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface TCSI extends Serializable {

    ArrayList<TBcsmCamelTDPData> getTBcsmCamelTDPDataList();

    MAPExtensionContainer getExtensionContainer();

    Integer getCamelCapabilityHandling();

    boolean getNotificationToCSE();

    boolean getCsiActive();

}
