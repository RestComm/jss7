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

package org.mobicents.protocols.ss7.cap.api.gap;

import java.io.Serializable;

/**
 *
<code>
GapIndicators ::= SEQUENCE {
  duration    [0] Duration,
  gapInterval [1] Interval,
  ...
}
-- Indicates the call gapping characteristics.
-- No call gapping when gapInterval equals 0

Duration ::= INTEGER (-2..86400)
Interval ::= INTEGER (-1..60000)
</code>
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface GapIndicators extends Serializable {

    int getDuration();

    int getGapInterval();

}