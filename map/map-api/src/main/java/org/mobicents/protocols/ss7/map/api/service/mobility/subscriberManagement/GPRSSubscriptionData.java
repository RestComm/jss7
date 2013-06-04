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
public interface GPRSSubscriptionData {

    boolean getCompleteDataListIncluded();

    ArrayList<PDPContext> getGPRSDataList();

    MAPExtensionContainer getExtensionContainer();

    APNOIReplacement getApnOiReplacement();

}
