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

import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 *
 M-CSI ::= SEQUENCE { mobilityTriggers MobilityTriggers, serviceKey ServiceKey, gsmSCF-Address [0] ISDN-AddressString,
 * extensionContainer [1] ExtensionContainer OPTIONAL, notificationToCSE [2] NULL OPTIONAL, csi-Active [3] NULL OPTIONAL, ...}
 * -- notificationToCSE and csi-Active shall not be present when M-CSI is sent to VLR. -- They may only be included in ATSI/ATM
 * ack/NSDC message.
 *
 * MobilityTriggers ::= SEQUENCE SIZE (1..10) OF MM-Code
 *
 * ServiceKey ::= INTEGER (0..2147483647)
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface MCSI extends Serializable {

    ArrayList<MMCode> getMobilityTriggers();

    long getServiceKey();

    ISDNAddressString getGsmSCFAddress();

    MAPExtensionContainer getExtensionContainer();

    boolean getNotificationToCSE();

    boolean getCsiActive();

}
