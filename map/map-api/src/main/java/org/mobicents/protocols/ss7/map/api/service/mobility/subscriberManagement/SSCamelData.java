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

import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;

/**
 *
 SS-CamelData ::= SEQUENCE { ss-EventList SS-EventList, gsmSCF-Address ISDN-AddressString, extensionContainer [0]
 * ExtensionContainer OPTIONAL, ...}
 *
 * SS-EventList ::= SEQUENCE SIZE (1..10) OF SS-Code -- Actions for the following SS-Code values are defined in CAMEL Phase 3:
 * -- ect SS-Code ::= '00110001'B -- multiPTY SS-Code ::= '01010001'B -- cd SS-Code ::= '00100100'B -- ccbs SS-Code ::=
 * '01000100'B -- all other SS codes shall be ignored -- When SS-CSI is sent to the VLR, it shall not contain a marking for
 * ccbs. -- If the VLR receives SS-CSI containing a marking for ccbs, the VLR shall discard the -- ccbs marking in SS-CSI.
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface SSCamelData {

    ArrayList<SSCode> getSsEventList();

    ISDNAddressString getGsmSCFAddress();

    MAPExtensionContainer getExtensionContainer();

}
