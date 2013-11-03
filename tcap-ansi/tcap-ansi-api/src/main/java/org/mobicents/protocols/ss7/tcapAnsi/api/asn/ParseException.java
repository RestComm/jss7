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

package org.mobicents.protocols.ss7.tcapAnsi.api.asn;

import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.PAbortCause;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.RejectProblem;

/**
 * Thrown to indicate problems at parse time.
 *
 * @author baranowb
 * @author sergey vetyutnev
 *
 */
public class ParseException extends Exception {

    private PAbortCause pAbortCauseType;
    private RejectProblem problem;
    private Long invokeId;

    public ParseException(PAbortCause pAbortCauseType) {
        this.pAbortCauseType = pAbortCauseType;
    }

    public ParseException(RejectProblem problem) {
        this.problem = problem;
    }

    public ParseException(PAbortCause pAbortCauseType, String message) {
        super(message);
        this.pAbortCauseType = pAbortCauseType;
    }

    public ParseException(RejectProblem problem, String message) {
        super(message);
        this.problem = problem;
    }

    public ParseException(PAbortCause pAbortCauseType, Throwable cause) {
        super(cause);
        this.pAbortCauseType = pAbortCauseType;
    }

    public ParseException(RejectProblem problem, Throwable cause) {
        super(cause);
        this.problem = problem;
    }

    public ParseException(PAbortCause pAbortCauseType, String message, Throwable cause) {
        super(message, cause);
        this.pAbortCauseType = pAbortCauseType;
    }

    public ParseException(RejectProblem problem, String message, Throwable cause) {
        super(message, cause);
        this.problem = problem;
    }

    public PAbortCause getPAbortCauseType() {
        return pAbortCauseType;
    }

    public RejectProblem getProblem() {
        return problem;
    }

    public Long getInvokeId() {
        return invokeId;
    }

    public void setInvokeId(Long val) {
        invokeId = val;
    }

}
