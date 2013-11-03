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

package org.mobicents.protocols.ss7.cap.api.service.gprs.primitive;

import java.io.Serializable;

/**
 *
 ChargingRollOver ::= CHOICE { transferredVolumeRollOver [0] TransferredVolumeRollOver, elapsedTimeRollOver [1]
 * ElapsedTimeRollOver } -- transferredVolumeRollOver shall be reported if ApplyChargingReportGPRS reports volume and -- a
 * roll-over has occurred in one or more volume counters. Otherwise, it shall be absent. -- elapsedTimeRollOver shall be
 * reported if ApplyChargingReportGPRS reports duration and -- a roll-over has occurred in one or more duration counters.
 * Otherwise, it shall be absent.
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface ChargingRollOver extends Serializable {

    TransferredVolumeRollOver getTransferredVolumeRollOver();

    ElapsedTimeRollOver getElapsedTimeRollOver();

}