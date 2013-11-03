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

package org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp;

/**
 * @author baranowb
 * @author sergey vetyutnev

Reject ::= SEQUENCE {
    componentID     [PRIVATE 15] IMPLICIT OCTET STRING SIZE(0..1),
    rejectProblem       [PRIVATE 21] IMPLICIT Problem,
    parameter CHOICE {
        paramSequence       [PRIVATE 16] IMPLICIT SEQUENCE { },
        paramSet        [PRIVATE 18] IMPLICIT SET { }
        --The choice between paramSequence and paramSet is implementation
        --dependent, however paramSequence is preferred.
    }
}
(CONSTRAINED BY {--must conform to the above definition --}
    ! RejectProblem : general-incorrectComponentPortion)
(CONSTRAINED BY {--must have consistent encoding --}
    ! RejectProblem : general-badlyStructuredCompPortion)
(CONSTRAINED BY {--must conform to ANSI T1.114.3 encoding rules --}
    ! RejectProblem : general-incorrectComponentCoding)

 *
 */
public interface Reject extends Component {

    int _TAG_REJECT = 12;
    int _TAG_REJECT_PROBLEM = 21;

    void setProblem(RejectProblem p);

    RejectProblem getProblem();

    /**
     * @return true: local originated Reject (rejecting a bad incoming primitive by a local side) false: remote originated
     *         Reject (rejecting a bad outgoing primitive by a peer)
     */
    boolean isLocalOriginated();

    void setLocalOriginated(boolean p);

}
