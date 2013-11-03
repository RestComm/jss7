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

/**
 *
 */
package org.mobicents.protocols.ss7.tcap.api.tc.dialog.events;

import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.UserInformation;

/**
 * See Q.771 3.1.2.2.1
 *
 * @author baranowb
 *
 */
public interface TCUniRequest extends DialogRequest {

    void setReturnMessageOnError(boolean val);

    boolean getReturnMessageOnError();

    /**
     * Destination address. If this address is different than one in dialog, this value will overwrite dialog value.
     */
    SccpAddress getDestinationAddress();

    void setDestinationAddress(SccpAddress dest);

    /**
     * Origin address. If this address is different than one in dialog, this value will overwrite dialog value.
     */
    SccpAddress getOriginatingAddress();

    void setOriginatingAddress(SccpAddress dest);

    /**
     * Application context name for this dialog.
     *
     * @return
     */
    ApplicationContextName getApplicationContextName();

    void setApplicationContextName(ApplicationContextName acn);

    /**
     * User information for this dialog.
     *
     * @return
     */
    UserInformation getUserInformation();

    void setUserInformation(UserInformation acn);

}
