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

package org.mobicents.protocols.ss7.map.api.datacoding;

/**
 * 3GPP TS 23.038 - 5. CBS Data Coding Scheme
 *
 *
 * @author sergey vetyutnev
 *
 */
public enum CBSDataCodingGroup {
    // Coding Group Bits == 0000 or 0010
    GeneralGsm7,
    // Coding Group Bits == 0001
    GeneralWithLanguageIndication,

    // Coding Group Bits == 01xx
    GeneralDataCodingIndication,
    // Coding Group Bits == 1001
    MessageWithUserDataHeader,
    // Coding Group Bits == 1101
    I1ProtocolMessage,
    // Coding Group Bits == 1110
    DefinedByTheWAPForum,
    // Coding Group Bits == 1111
    DataCodingMessageClass, Reserved;
}
