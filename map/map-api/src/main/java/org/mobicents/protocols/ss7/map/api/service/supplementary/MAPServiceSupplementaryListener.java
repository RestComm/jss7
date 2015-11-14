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

package org.mobicents.protocols.ss7.map.api.service.supplementary;

import org.mobicents.protocols.ss7.map.api.MAPServiceListener;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public interface MAPServiceSupplementaryListener extends MAPServiceListener {

    void onRegisterSSRequest(RegisterSSRequest request);

    void onRegisterSSResponse(RegisterSSResponse response);

    void onEraseSSRequest(EraseSSRequest request);

    void onEraseSSResponse(EraseSSResponse response);

    void onActivateSSRequest(ActivateSSRequest request);

    void onActivateSSResponse(ActivateSSResponse response);

    void onDeactivateSSRequest(DeactivateSSRequest request);

    void onDeactivateSSResponse(DeactivateSSResponse response);

    void onInterrogateSSRequest(InterrogateSSRequest request);

    void onInterrogateSSResponse(InterrogateSSResponse response);

    void onGetPasswordRequest(GetPasswordRequest request);

    void onGetPasswordResponse(GetPasswordResponse response);

    void onRegisterPasswordRequest(RegisterPasswordRequest request);

    void onRegisterPasswordResponse(RegisterPasswordResponse response);


    void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd);

    void onProcessUnstructuredSSResponse(ProcessUnstructuredSSResponse procUnstrResInd);

    void onUnstructuredSSRequest(UnstructuredSSRequest unstrReqInd);

    void onUnstructuredSSResponse(UnstructuredSSResponse unstrResInd);

    void onUnstructuredSSNotifyRequest(UnstructuredSSNotifyRequest unstrNotifyInd);

    void onUnstructuredSSNotifyResponse(UnstructuredSSNotifyResponse unstrNotifyInd);
}
