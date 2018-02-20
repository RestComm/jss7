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

package org.restcomm.protocols.ss7.map.api.service.oam;

import java.io.Serializable;

/**
 *
<code>
TraceType ::= INTEGER (0..255)
-- Trace types are fully defined in 3GPP TS 52.008. [61]

MSC/BSS Trace Type:
8 - Priority Indication
7 - For future expansion (Set to 0)
6, 5 - BSS Record Type
  0 - Basic
  1 - Handover
  2 - Radio
  3 - No BSS Trace
4, 3 - MSC Record Type
  0 - Basic
  1 - Detailed (Optional)
  2 - Spare
  3 - No MSC Trace
2, 1 - Invoking Event
  0 - MOC, MTC, SMS MO, SMS MT, PDS MO, PDS MT, SS, Location Updates, IMSI attach, IMSI detach
  1 - MOC, MTC, SMS_MO, SMS_MT, PDS MO, PDS MT, SS only
  2 - Location updates, IMSI attach IMSI detach only
  3 - Operator definable

HLR Trace Type:
8 - Priority Indication
7, 6, 5 - For future expansion (Set to 0)
4, 3 - HLR Record Type
  0 - Basic
  1 - Detailed
  2 - Spare
  3 - No HLR Trace
2, 1 - Invoking Event
  0 - All HLR Interactions
  1 - Spare
  2 - Spare
  3 - Operator definable

</code>
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface TraceType extends Serializable {

    int getData();

    BssRecordType getBssRecordType();

    MscRecordType getMscRecordType();

    HlrRecordType getHlrRecordType();

    TraceTypeInvokingEvent getTraceTypeInvokingEvent();

    boolean isPriorityIndication();

}
