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

package org.mobicents.protocols.ss7.tcap.api;

import org.mobicents.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.mobicents.protocols.ss7.tcap.asn.comp.ErrorCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.protocols.ss7.tcap.asn.comp.ProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Reject;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnError;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResult;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;

/**
 *
 * @author amit bhayani
 * @author baranowb
 */
public interface ComponentPrimitiveFactory {

    /**
     * Create a new Invoke. Class of this Invoke will be 1
     *
     * @return return new instance of ({@link Invoke}
     */
    Invoke createTCInvokeRequest();

    /**
     * <p>
     * Create a new {@link Invoke}. Set the {@link InvokeClass} as per bellow consideration
     * </p>
     * <ul>
     * <li>Class 1 – Both success and failure are reported.</li>
     * <li>Class 2 – Only failure is reported.</li>
     * <li>Class 3 – Only success is reported.</li>
     * <li>Class 4 – Neither success, nor failure is reported.</li>
     * </ul>
     *
     * @param invokeClass The Class of Operation
     * @return new instance of ({@link Invoke}
     */
    Invoke createTCInvokeRequest(InvokeClass invokeClass);

    Reject createTCRejectRequest();

    ReturnResultLast createTCResultLastRequest();

    ReturnResult createTCResultRequest();

    ReturnError createTCReturnErrorRequest();

    OperationCode createOperationCode();

    ErrorCode createErrorCode();

    Parameter createParameter();

    Parameter createParameter(int tag, int tagClass, boolean isPrimitive);

    Problem createProblem(ProblemType pt);
}
