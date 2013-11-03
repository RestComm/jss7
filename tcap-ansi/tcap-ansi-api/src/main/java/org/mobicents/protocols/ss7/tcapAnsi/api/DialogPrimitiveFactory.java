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

/**
 *
 */

package org.mobicents.protocols.ss7.tcapAnsi.api;

import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.UserInformation;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.UserInformationElement;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCQueryRequest;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCConversationRequest;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCResponseRequest;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUniRequest;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUserAbortRequest;

/**
 *
 * * @author baranowb
 *
 * @author amit bhayani
 *
 */
public interface DialogPrimitiveFactory {

    TCQueryRequest createQuery(Dialog d, boolean dialogTermitationPermission);

    TCConversationRequest createConversation(Dialog d, boolean dialogTermitationPermission);

    TCResponseRequest createResponse(Dialog d);

    TCUserAbortRequest createUAbort(Dialog d);

    TCUniRequest createUni(Dialog d);

    ApplicationContext createApplicationContext(long[] val);

    ApplicationContext createApplicationContext(long val);

    UserInformation createUserInformation();

    UserInformationElement createUserInformationElement();

}