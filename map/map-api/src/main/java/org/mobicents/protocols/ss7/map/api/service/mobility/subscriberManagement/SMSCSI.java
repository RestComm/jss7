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
 SMS-CSI ::= SEQUENCE { sms-CAMEL-TDP-DataList [0] SMS-CAMEL-TDP-DataList OPTIONAL, camelCapabilityHandling [1]
 * CamelCapabilityHandling OPTIONAL, extensionContainer [2] ExtensionContainer OPTIONAL, notificationToCSE [3] NULL OPTIONAL,
 * csi-Active [4] NULL OPTIONAL, ...} -- notificationToCSE and csi-Active shall not be present -- when MO-SMS-CSI or MT-SMS-CSI
 * is sent to VLR or SGSN. -- They may only be included in ATSI/ATM ack/NSDC message. -- SMS-CAMEL-TDP-Data and
 * camelCapabilityHandling shall be present in -- the SMS-CSI sequence. -- If SMS-CSI is segmented, sms-CAMEL-TDP-DataList and
 * camelCapabilityHandling shall be -- present in the first segment
 *
 * SMS-CAMEL-TDP-DataList ::= SEQUENCE SIZE (1..1-) OF SMS-CAMEL-TDP-Data -- SMS-CAMEL-TDP-DataList shall not contain more than
 * one instance of -- SMS-CAMEL-TDP-Data containing the same value for sms-TriggerDetectionPoint.
 *
 * CamelCapabilityHandling ::= INTEGER(1..16) -- value 1 = CAMEL phase 1, -- value 2 = CAMEL phase 2, -- value 3 = CAMEL Phase
 * 3, -- value 4 = CAMEL phase 4: -- reception of values greater than 4 shall be treated as CAMEL phase 4.
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface SMSCSI extends Serializable {

    ArrayList<SMSCAMELTDPData> getSmsCamelTdpDataList();

    Integer getCamelCapabilityHandling();

    MAPExtensionContainer getExtensionContainer();

    boolean getNotificationToCSE();

    boolean getCsiActive();

}
