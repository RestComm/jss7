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

package org.mobicents.protocols.ss7.cap.api.gap;

import java.io.Serializable;

import org.mobicents.protocols.ss7.cap.api.isup.CauseCap;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InformationToSend;

/**
 *
 GapTreatment {PARAMETERS-BOUND : bound} ::= CHOICE { informationToSend [0] InformationToSend {bound}, releaseCause [1] Cause
 * {bound} } -- The default value for Cause is the same as in ISUP.
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface GapTreatment extends Serializable {

    InformationToSend getInformationToSend();

    CauseCap getCause();

}