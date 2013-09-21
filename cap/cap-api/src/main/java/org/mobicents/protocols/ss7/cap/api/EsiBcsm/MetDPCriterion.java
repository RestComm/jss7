/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
