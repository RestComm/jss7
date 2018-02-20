/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.restcomm.protocols.ss7.map.api.service.lsm;

import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.SubscriberIdentity;

/**
 *
 MAP V3:
 *
 * sendRoutingInfoForLCS OPERATION ::= { --Timer m ARGUMENT RoutingInfoForLCS-Arg RESULT RoutingInfoForLCS-Res ERRORS {
 * systemFailure | dataMissing | unexpectedDataValue | facilityNotSupported | unknownSubscriber | absentSubscriber |
 * unauthorizedRequestingNetwork } CODE local:85 }
 *
 * RoutingInfoForLCS-Arg ::= SEQUENCE { mlcNumber [0] ISDN-AddressString, targetMS [1] SubscriberIdentity, extensionContainer
 * [2] ExtensionContainer OPTIONAL, ...}
 *
 *
 * @author amit bhayani
 *
 */
public interface SendRoutingInfoForLCSRequest extends LsmMessage {
    ISDNAddressString getMLCNumber();

    SubscriberIdentity getTargetMS();

    MAPExtensionContainer getExtensionContainer();

}
