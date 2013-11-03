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

package org.mobicents.protocols.ss7.map.api.service.sms;

import java.util.ArrayList;

import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

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
