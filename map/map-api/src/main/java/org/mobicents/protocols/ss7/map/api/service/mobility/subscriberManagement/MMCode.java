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

import java.io.Serializable;

/**
 *
<code>
MM-Code ::= OCTET STRING (SIZE (1))
-- This type is used to indicate a Mobility Management event.
-- Actions for the following MM-Code values are defined in CAMEL Phase 4:
-- -- CS domain MM events:
-- Location-update-in-same-VLR MM-Code ::= '00000000'B
-- Location-update-to-other-VLR MM-Code ::= '00000001'B
-- IMSI-Attach MM-Code ::= '00000010'B
-- MS-initiated-IMSI-Detach MM-Code ::= '00000011'B
-- Network-initiated-IMSI-Detach MM-Code ::= '00000100'B
-- -- PS domain MM events:
-- Routeing-Area-update-in-same-SGSN MM-Code ::= '10000000'B
-- Routeing-Area-update-to-other-SGSN-update-from-new-SGSN
-- MM-Code ::= '10000001'B
-- Routeing-Area-update-to-other-SGSN-disconnect-by-detach
-- MM-Code ::= '10000010'B
-- GPRS-Attach MM-Code ::= '10000011'B
-- MS-initiated-GPRS-Detach MM-Code ::= '10000100'B
-- Network-initiated-GPRS-Detach MM-Code ::= '10000101'B
-- Network-initiated-transfer-to-MS-not-reachable-for-paging
-- MM-Code ::= '10000110'B
-- -- If the MSC receives any other MM-code than the ones listed above for the
-- CS domain, then the MSC shall ignore that MM-code.
-- If the SGSN receives any other MM-code than the ones listed above for the
-- PS domain, then the SGSN shall ignore that MM-code.
</code>
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface MMCode extends Serializable {

    MMCodeValue getMMCodeValue();

}
