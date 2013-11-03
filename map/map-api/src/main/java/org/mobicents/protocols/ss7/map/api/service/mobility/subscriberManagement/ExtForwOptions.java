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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement;

import java.io.Serializable;

/**
 *
 Ext-ForwOptions ::= OCTET STRING (SIZE (1..5))
 *
 * -- OCTET 1:
 *
 * -- bit 8: notification to forwarding party -- 0 no notification -- 1 notification
 *
 * -- bit 7: redirecting presentation -- 0 no presentation -- 1 presentation
 *
 * -- bit 6: notification to calling party -- 0 no notification -- 1 notification
 *
 * -- bit 5: 0 (unused)
 *
 * -- bits 43: forwarding reason -- 00 ms not reachable -- 01 ms busy -- 10 no reply -- 11 unconditional
 *
 * -- bits 21: 00 (unused)
 *
 * -- OCTETS 2-5: reserved for future use. They shall be discarded if -- received and not understood.
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface ExtForwOptions extends Serializable {

    byte[] getData();

    boolean getNotificationToForwardingParty();

    boolean getRedirectingPresentation();

    boolean getNotificationToCallingParty();

    ExtForwOptionsForwardingReason getExtForwOptionsForwardingReason();

}
