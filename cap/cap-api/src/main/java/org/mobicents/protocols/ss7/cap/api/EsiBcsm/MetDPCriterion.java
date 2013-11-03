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

package org.mobicents.protocols.ss7.cap.api.EsiBcsm;

import java.io.Serializable;

import org.mobicents.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdFixedLength;
import org.mobicents.protocols.ss7.map.api.primitives.LAIFixedLength;

/**
 *
 MetDPCriteriaList {PARAMETERS-BOUND : bound} ::= SEQUENCE SIZE(1..10) OF MetDPCriterion {bound}
 *
 * MetDPCriterion {PARAMETERS-BOUND : bound} ::= CHOICE { enteringCellGlobalId [0] CellGlobalIdOrServiceAreaIdFixedLength,
 * leavingCellGlobalId [1] CellGlobalIdOrServiceAreaIdFixedLength, enteringServiceAreaId [2]
 * CellGlobalIdOrServiceAreaIdFixedLength, leavingServiceAreaId [3] CellGlobalIdOrServiceAreaIdFixedLength,
 * enteringLocationAreaId [4] LAIFixedLength, leavingLocationAreaId [5] LAIFixedLength, inter-SystemHandOverToUMTS [6] NULL,
 * inter-SystemHandOverToGSM [7] NULL, inter-PLMNHandOver [8] NULL, inter-MSCHandOver [9] NULL, metDPCriterionAlt [10]
 * MetDPCriterionAlt {bound} } -- The enteringCellGlobalId and leavingCellGlobalId shall contain a Cell Global Identification.
 * -- The enteringServiceAreaId and leavingServiceAreaId shall contain a Service Area Identification.
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface MetDPCriterion extends Serializable {

    CellGlobalIdOrServiceAreaIdFixedLength getEnteringCellGlobalId();

    CellGlobalIdOrServiceAreaIdFixedLength getLeavingCellGlobalId();

    CellGlobalIdOrServiceAreaIdFixedLength getEnteringServiceAreaId();

    CellGlobalIdOrServiceAreaIdFixedLength getLeavingServiceAreaId();

    LAIFixedLength getEnteringLocationAreaId();

    LAIFixedLength getLeavingLocationAreaId();

    boolean getInterSystemHandOverToUMTS();

    boolean getInterSystemHandOverToGSM();

    boolean getInterPLMNHandOver();

    boolean getInterMSCHandOver();

    MetDPCriterionAlt getMetDPCriterionAlt();

}
