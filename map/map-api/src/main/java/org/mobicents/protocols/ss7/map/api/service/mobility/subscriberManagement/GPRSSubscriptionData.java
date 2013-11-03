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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.PDPContext;

/**
 *
 GPRSSubscriptionData ::= SEQUENCE {
   completeDataListIncluded NULL OPTIONAL,
   -- If segmentation is used, completeDataListIncluded may only be present in the
   -- first segment of GPRSSubscriptionData.
   gprsDataList [1] GPRSDataList,
   extensionContainer [2] ExtensionContainer OPTIONAL,
   ...,
   apn-oi-Replacement [3] APN-OI-Replacement OPTIONAL
   -- this apn-oi-Replacement refers to the UE level apn-oi-Replacement.
}
 *
 * GPRSDataList ::= SEQUENCE SIZE (1..50) OF PDP-Context
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface GPRSSubscriptionData extends Serializable {

    boolean getCompleteDataListIncluded();

    ArrayList<PDPContext> getGPRSDataList();

    MAPExtensionContainer getExtensionContainer();

    APNOIReplacement getApnOiReplacement();

}
