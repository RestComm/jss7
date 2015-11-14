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

package org.mobicents.protocols.ss7.inap.api.charging;

import java.io.Serializable;

/**
*
<code>
TariffSwitchoverTime ::= OCTET STRING (SIZE(1))
-- This time is the absolute time at which the next tariff has to become active. It is represented in steps of 15 minutes.
-- The coding is the following:
-- 0 : spare
-- 1 : 0 hour 15 minutes
-- 2 : 0 hour 30 minutes
-- 3 : 0 hour 45 minutes
-- 4 : 1 hour 0 minutes
-- ..
-- 96 : 24 hours 0 minutes
-- 97-255 : spare
</code>
*
* @author sergey vetyutnev
*
*/
public interface TariffSwitchoverTime extends Serializable {

    int getData();

}
