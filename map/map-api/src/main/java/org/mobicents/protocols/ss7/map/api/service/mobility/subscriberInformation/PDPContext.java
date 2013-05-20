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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation;

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.APN;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.APNOIReplacement;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ChargingCharacteristics;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext2QoSSubscribed;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext3QoSSubscribed;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext4QoSSubscribed;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtPDPType;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPAddress;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPType;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.QoSSubscribed;

/**
 * PDP-Context ::= SEQUENCE { pdp-ContextId ContextId, pdp-Type [16] PDP-Type, pdp-Address [17] PDP-Address OPTIONAL,
 * qos-Subscribed [18] QoS-Subscribed, vplmnAddressAllowed [19] NULL OPTIONAL, apn [20] APN, extensionContainer [21]
 * ExtensionContainer OPTIONAL, ... , ext-QoS-Subscribed [0] Ext-QoS-Subscribed OPTIONAL, pdp-ChargingCharacteristics [1]
 * ChargingCharacteristics OPTIONAL, ext2-QoS-Subscribed [2] Ext2-QoS-Subscribed OPTIONAL, -- ext2-QoS-Subscribed may be present
 * only if ext-QoS-Subscribed is present. ext3-QoS-Subscribed [3] Ext3-QoS-Subscribed OPTIONAL, -- ext3-QoS-Subscribed may be
 * present only if ext2-QoS-Subscribed is present. ext4-QoS-Subscribed [4] Ext4-QoS-Subscribed OPTIONAL, -- ext4-QoS-Subscribed
 * may be present only if ext3-QoS-Subscribed is present. apn-oi-Replacement [5] APN-OI-Replacement OPTIONAL, -- this
 * apn-oi-Replacement refers to the APN level apn-oi-Replacement and has -- higher priority than UE level apn-oi-Replacement.
 * ext-pdp-Type [6] Ext-PDP-Type OPTIONAL, -- contains the value IPv4v6 defined in 3GPP TS 29.060 [105], if the PDP can be --
 * accessed by dual-stack UEs ext-pdp-Address [7] PDP-Address OPTIONAL, -- contains an additional IP address in case of
 * dual-stack static IP address assignment -- for the UE. -- it may contain an IPv4 or an IPv6 address/prefix, and it may be
 * present -- only if pdp-Address is present; if both are present, each parameter shall -- contain a different type of address
 * (IPv4 or IPv6). sipto-Permission [8] SIPTO-Permission OPTIONAL, lipa-Permission [9] LIPA-Permission OPTIONAL }
 *
 * ContextId ::= INTEGER (1..maxNumOfPDP-Contexts) maxNumOfPDP-Contexts INTEGER ::= 50
 *
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public interface PDPContext {

    int getPDPContextId();

    PDPType getPDPType();

    PDPAddress getPDPAddress();

    QoSSubscribed getQoSSubscribed();

    boolean isVPLMNAddressAllowed();

    APN getAPN();

    MAPExtensionContainer getExtensionContainer();

    ExtQoSSubscribed getExtQoSSubscribed();

    ChargingCharacteristics getChargingCharacteristics();

    Ext2QoSSubscribed getExt2QoSSubscribed();

    Ext3QoSSubscribed getExt3QoSSubscribed();

    Ext4QoSSubscribed getExt4QoSSubscribed();

    APNOIReplacement getAPNOIReplacement();

    ExtPDPType getExtPDPType();

    PDPAddress getExtPDPAddress();

    SIPTOPermission getSIPTOPermission();

    LIPAPermission getLIPAPermission();
}
