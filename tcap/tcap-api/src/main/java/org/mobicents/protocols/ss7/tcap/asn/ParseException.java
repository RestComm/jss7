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

package org.mobicents.protocols.ss7.tcap.asn;

import org.mobicents.protocols.ss7.tcap.asn.comp.GeneralProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.PAbortCauseType;

/**
 * Thrown to indicate problems at parse time.
 *
 * @author baranowb
 * @author sergey vetyutnev
 *
 */
public class ParseException extends Exception {

    private GeneralProblemType problem;
    private PAbortCauseType pAbortCauseType;
    private Long invokeId;

    public ParseException(PAbortCauseType pAbortCauseType, GeneralProblemType problem) {
        this.problem = problem;
        this.pAbortCauseType = pAbortCauseType;
    }

    public ParseException(PAbortCauseType pAbortCauseType, GeneralProblemType problem, String message) {
        super(message);
        this.problem = problem;
        this.pAbortCauseType = pAbortCauseType;
    }

    public ParseException(PAbortCauseType pAbortCauseType, GeneralProblemType problem, Throwable cause) {
        super(cause);
        this.problem = problem;
        this.pAbortCauseType = pAbortCauseType;
    }

    public ParseException(PAbortCauseType pAbortCauseType, GeneralProblemType problem, String message, Throwable cause) {
        super(message, cause);
        this.problem = problem;
        this.pAbortCauseType = pAbortCauseType;
    }

    public GeneralProblemType getProblem() {
        return problem;
    }

    public PAbortCauseType getPAbortCauseType() {
        return pAbortCauseType;
    }

    public Long getInvokeId() {
        return invokeId;
    }

    public void setInvokeId(Long val) {
        invokeId = val;
    }

}
