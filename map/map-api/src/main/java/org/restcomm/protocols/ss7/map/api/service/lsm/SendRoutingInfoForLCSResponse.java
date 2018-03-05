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

import org.restcomm.protocols.ss7.map.api.primitives.GSNAddress;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.SubscriberIdentity;

/**
 * RoutingInfoForLCS-Res ::= SEQUENCE { targetMS [0] SubscriberIdentity, lcsLocationInfo [1] LCSLocationInfo, extensionContainer
 * [2] ExtensionContainer OPTIONAL, ..., v-gmlc-Address [3] GSN-Address OPTIONAL, h-gmlc-Address [4] GSN-Address OPTIONAL,
 * ppr-Address [5] GSN-Address OPTIONAL, additional-v-gmlc-Address [6] GSN-Address OPTIONAL }
 *
 *
 * @author amit bhayani
 *
 */
public interface SendRoutingInfoForLCSResponse extends LsmMessage {

    SubscriberIdentity getTargetMS();

    LCSLocationInfo getLCSLocationInfo();

    MAPExtensionContainer getExtensionContainer();

    GSNAddress getVgmlcAddress();

    GSNAddress getHGmlcAddress();

    GSNAddress getPprAddress();

    GSNAddress getAdditionalVGmlcAddress();

}
