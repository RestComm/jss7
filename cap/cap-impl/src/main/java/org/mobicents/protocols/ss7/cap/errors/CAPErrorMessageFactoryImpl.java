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

package org.mobicents.protocols.ss7.cap.errors;

import org.mobicents.protocols.ss7.cap.api.errors.CAPErrorCode;
import org.mobicents.protocols.ss7.cap.api.errors.CAPErrorMessage;
import org.mobicents.protocols.ss7.cap.api.errors.CAPErrorMessageCancelFailed;
import org.mobicents.protocols.ss7.cap.api.errors.CAPErrorMessageFactory;
import org.mobicents.protocols.ss7.cap.api.errors.CAPErrorMessageParameterless;
import org.mobicents.protocols.ss7.cap.api.errors.CAPErrorMessageRequestedInfoError;
import org.mobicents.protocols.ss7.cap.api.errors.CAPErrorMessageSystemFailure;
import org.mobicents.protocols.ss7.cap.api.errors.CAPErrorMessageTaskRefused;
import org.mobicents.protocols.ss7.cap.api.errors.CancelProblem;
import org.mobicents.protocols.ss7.cap.api.errors.RequestedInfoErrorParameter;
import org.mobicents.protocols.ss7.cap.api.errors.TaskRefusedParameter;
import org.mobicents.protocols.ss7.cap.api.errors.UnavailableNetworkResource;

/**
 * The factory of CAP ReturnError messages
 *
 * @author sergey vetyutnev
 *
 */
public class CAPErrorMessageFactoryImpl implements CAPErrorMessageFactory {

    @Override
    public CAPErrorMessage createMessageFromErrorCode(Long errorCode) {
        int ec = (int) (long) errorCode;
        switch (ec) {
            case CAPErrorCode.cancelFailed:
                CAPErrorMessageCancelFailedImpl emCancelFailed = new CAPErrorMessageCancelFailedImpl();
                return emCancelFailed;
            case CAPErrorCode.requestedInfoError:
                CAPErrorMessageRequestedInfoErrorImpl emRequestedInfoError = new CAPErrorMessageRequestedInfoErrorImpl();
                return emRequestedInfoError;
            case CAPErrorCode.systemFailure:
                CAPErrorMessageSystemFailureImpl emSystemFailure = new CAPErrorMessageSystemFailureImpl();
                return emSystemFailure;
            case CAPErrorCode.taskRefused:
                CAPErrorMessageTaskRefusedImpl emTaskRefused = new CAPErrorMessageTaskRefusedImpl();
                return emTaskRefused;
            default:
                return new CAPErrorMessageParameterlessImpl(errorCode);
        }
    }

    @Override
    public CAPErrorMessageParameterless createCAPErrorMessageParameterless(Long errorCode) {
        return new CAPErrorMessageParameterlessImpl(errorCode);
    }

    @Override
    public CAPErrorMessageCancelFailed createCAPErrorMessageCancelFailed(CancelProblem cancelProblem) {
        return new CAPErrorMessageCancelFailedImpl(cancelProblem);
    }

    @Override
    public CAPErrorMessageRequestedInfoError createCAPErrorMessageRequestedInfoError(
            RequestedInfoErrorParameter requestedInfoErrorParameter) {
        return new CAPErrorMessageRequestedInfoErrorImpl(requestedInfoErrorParameter);
    }

    @Override
    public CAPErrorMessageSystemFailure createCAPErrorMessageSystemFailure(UnavailableNetworkResource unavailableNetworkResource) {
        return new CAPErrorMessageSystemFailureImpl(unavailableNetworkResource);
    }

    @Override
    public CAPErrorMessageTaskRefused createCAPErrorMessageTaskRefused(TaskRefusedParameter taskRefusedParameter) {
        return new CAPErrorMessageTaskRefusedImpl(taskRefusedParameter);
    }
}
