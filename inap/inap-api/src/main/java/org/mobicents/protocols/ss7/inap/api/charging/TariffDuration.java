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
TariffDuration ::= INTEGER (0..36000)
-- TariffDuration identifies with 0 unlimited duration and else in seconds unit.
-- 0 = unlimited
-- 1 = 1 second
-- 2 = 2 seconds
-- ...
-- 36000 = 10 hours
--
-- The duration indicates for how long time the communication charge component is valid. Expiration of the tariff duration
-- timer leads to the activation of the next communication charge (if present).
-- In the case where there is no next communication charge in the communication charge sequence, the action to be performed
-- is indicated by the tariffControlIndicators.
</code>
*
* @author sergey vetyutnev
*
*/
public interface TariffDuration extends Serializable {

    int getData();

}
