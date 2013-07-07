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

    public ParseException(PAbortCause pAbortCauseType, RejectProblem problem) {
        this.pAbortCauseType = pAbortCauseType;
        this.problem = problem;
    }

    public ParseException(PAbortCause pAbortCauseType, RejectProblem problem, String message) {
        super(message);
        this.pAbortCauseType = pAbortCauseType;
        this.problem = problem;
    }

    public ParseException(PAbortCause pAbortCauseType, RejectProblem problem, Throwable cause) {
        super(cause);
        this.pAbortCauseType = pAbortCauseType;
        this.problem = problem;
    }

    public ParseException(PAbortCause pAbortCauseType, RejectProblem problem, String message, Throwable cause) {
        super(message, cause);
        this.pAbortCauseType = pAbortCauseType;
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
