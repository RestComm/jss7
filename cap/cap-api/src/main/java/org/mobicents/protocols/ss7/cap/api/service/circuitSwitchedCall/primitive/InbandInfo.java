/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive;

import java.io.Serializable;

/**
 *
<code>
InbandInfo {PARAMETERS-BOUND : bound} ::= SEQUENCE {
  messageID            [0] MessageID {bound},
  numberOfRepetitions  [1] INTEGER (1..127) OPTIONAL,
  duration             [2] INTEGER (0..32767) OPTIONAL,
  interval             [3] INTEGER (0..32767) OPTIONAL,
  ...
}
-- Interval is the time in seconds between each repeated announcement. Duration is the total
-- amount of time in seconds, including repetitions and intervals.
-- The end of announcement is either the end of duration or numberOfRepetitions,
-- whatever comes first.
-- duration with value 0 indicates infinite duration
</code>
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface InbandInfo extends Serializable {

    MessageID getMessageID();

    Integer getNumberOfRepetitions();

    Integer getDuration();

    Integer getInterval();

}
