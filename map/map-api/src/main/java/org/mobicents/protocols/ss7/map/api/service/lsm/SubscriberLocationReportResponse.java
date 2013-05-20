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

package org.mobicents.protocols.ss7.map.api.service.lsm;

import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 * SubscriberLocationReport-Res ::= SEQUENCE { extensionContainer ExtensionContainer OPTIONAL, ..., na-ESRK [0]
 * ISDN-AddressString OPTIONAL, na-ESRD [1] ISDN-AddressString OPTIONAL } -- na-ESRK and na-ESRD are mutually exclusive -- --
 * exception handling -- receipt of both na-ESRK and na-ESRD shall be treated the same as a return error
 *
 *
 * @author amit bhayani
 *
 */
public interface SubscriberLocationReportResponse extends LsmMessage {

    MAPExtensionContainer getExtensionContainer();

    ISDNAddressString getNaESRK();

    ISDNAddressString getNaESRD();
}
