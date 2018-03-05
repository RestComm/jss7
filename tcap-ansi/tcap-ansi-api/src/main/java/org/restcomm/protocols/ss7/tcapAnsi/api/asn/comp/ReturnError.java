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

/**
 *
 */

package org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp;

/**
 * @author baranowb
 * @author amit bhayani
 *

ReturnError{ERROR: Errors} ::= SEQUENCE {
    componentID     [PRIVATE 15] IMPLICIT OCTET STRING SIZE(1)
        (CONSTRAINED BY {--must be that of an outstanding operation --}
            ! RejectProblem : returnError-unrecognisedCorrelationId)
        (CONSTRAINED BY {--which returns an error --}
            ! RejectProblem : returnError-unexpectedReturnError),
    errorCode ERROR.&errorCode
        ({Errors}
            ! RejectProblem : returnError-unrecognisedError)
        (CONSTRAINED BY {--must be in the &Errors field of the associated operation --}
            ! RejectProblem : returnError-unexpectedError),
    parameter ERROR.&ParameterType
        ({Errors}{@errorcode}
            ! RejectProblem : returnError-incorrectParameter) OPTIONAL
}
(CONSTRAINED BY {--must conform to the above definition --}
    ! RejectProblem : general-incorrectComponentPortion)
(CONSTRAINED BY {--must have consistent encoding --}
    ! RejectProblem : general-badlyStructuredCompPortion)
(CONSTRAINED BY {--must conform to ANSI T1.114.3 encoding rules --}
    ! RejectProblem : general-incorrectComponentCoding)

 */
public interface ReturnError extends Component {
    int _TAG_RETURN_ERROR = 11;

    void setErrorCode(ErrorCode ec);

    ErrorCode getErrorCode();

    void setParameter(Parameter p);

    Parameter getParameter();

}
