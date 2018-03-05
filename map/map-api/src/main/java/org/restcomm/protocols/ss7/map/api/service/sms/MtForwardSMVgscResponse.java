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

package org.restcomm.protocols.ss7.map.api.service.sms;

import java.util.ArrayList;

import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 *
 MT-ForwardSM-VGCS-Res ::= SEQUENCE { sm-RP-UI [0] SignalInfo OPTIONAL, dispatcherList [1] DispatcherList OPTIONAL,
 * ongoingCall NULL OPTIONAL, extensionContainer [2] ExtensionContainer OPTIONAL, ...}
 *
 * DispatcherList ::= SEQUENCE SIZE (1..5) OF ISDN-AddressString
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface MtForwardSMVgscResponse extends SmsMessage {

    SmsSignalInfo getSM_RP_UI();

    ArrayList<ISDNAddressString> getDispatcherList();

    boolean getOngoingCall();

    MAPExtensionContainer getExtensionContainer();

}
