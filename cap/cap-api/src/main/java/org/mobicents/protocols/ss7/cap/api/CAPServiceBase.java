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

package org.mobicents.protocols.ss7.cap.api;

import org.mobicents.protocols.ss7.cap.api.dialog.ServingCheckData;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 *
 * @author sergey vetyutnev
 *
 */
public interface CAPServiceBase {

    CAPProvider getCAPProvider();

    /**
     * Creates a new Dialog.
     *
     * @param applicationCntx This parameter identifies the type of application context being established. If the dialogue is
     *        accepted the received application context name shall be echoed. In case of refusal of dialogue this parameter
     *        shall indicate the highest version supported.
     *
     * @param destAddress A valid SCCP address identifying the destination peer entity. As an implementation option, this
     *        parameter may also, in the indication, be implicitly associated with the service access point at which the
     *        primitive is issued.
     *
     * @param origAddress A valid SCCP address identifying the requestor of a CAP dialogue. As an implementation option, this
     *        parameter may also, in the request, be implicitly associated with the service access point at which the primitive
     *        is issued.
     *
     * @return
     */
    CAPDialog createNewDialog(CAPApplicationContext appCntx, SccpAddress origAddress, SccpAddress destAddress)
            throws CAPException;

    /**
     * Create new structured dialog with predefined local TransactionId.
     * We do not normally invoke this method. Use it only when you need this and only this local TransactionId
     * (for example if we need of recreating a Dialog for which a peer already has in memory)
     * If a Dialog with local TransactionId is already present there will be CAPException
     */
    CAPDialog createNewDialog(CAPApplicationContext appCntx, SccpAddress origAddress, SccpAddress destAddress, Long localTrId) throws CAPException;

    /**
     * Returns true if the service can perform dialogs with given ApplicationContext
     */
    ServingCheckData isServingService(CAPApplicationContext dialogApplicationContext);

    boolean isActivated();

    void acivate();

    void deactivate();
}
