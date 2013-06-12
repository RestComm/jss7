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

package org.mobicents.protocols.ss7.map.api.service.supplementary;

/**
 * ForwardingOptions ::= OCTET STRING (SIZE (1)) -- bit 8: notification to forwarding party -- 0 no notification -- 1
 * notification -- bit 7: redirecting presentation -- 0 no presentation -- 1 presentation -- bit 6: notification to calling
 * party -- 0 no notification -- 1 notification -- bit 5: 0 (unused) -- bits 43: forwarding reason -- 00 ms not reachable -- 01
 * ms busy -- 10 no reply -- 11 unconditional when used in a SRI Result, -- or call deflection when used in a RCH Argument --
 * bits 21: 00 (unused)
 *
 * @author cristian veliscu
 *
 */
public interface ForwardingOptions {
    int getData();

    boolean isNotificationToForwardingParty();

    boolean isRedirectingPresentation();

    boolean isNotificationToCallingParty();

    ForwardingReason getForwardingReason();
}
