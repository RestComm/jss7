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

package org.restcomm.protocols.ss7.cap.api.primitives;

import java.io.Serializable;

/**
 *
<code>
Burst ::= SEQUENCE {
  numberOfBursts   [0] INTEGER (1..3) DEFAULT 1,
  -- 3GPP Release 7 32 3GPP TS 29.078 7.0.0 (2005-06)
  burstInterval    [1] INTEGER (1..1200) DEFAULT 2,
  numberOfTonesInBurst [2] INTEGER (1..3) DEFAULT 3,
  toneDuration     [3] INTEGER (1..20) DEFAULT 2,
  toneInterval     [4] INTEGER (1..20) DEFAULT 2,
  ...
}
</code>
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface Burst extends Serializable {

    Integer getNumberOfBursts();

    Integer getBurstInterval();

    Integer getNumberOfTonesInBurst();

    Integer getToneDuration();

    Integer getToneInterval();

}