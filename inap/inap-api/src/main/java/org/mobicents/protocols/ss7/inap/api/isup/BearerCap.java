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

package org.mobicents.protocols.ss7.inap.api.isup;

import java.io.Serializable;

import org.mobicents.protocols.ss7.inap.api.INAPException;
import org.mobicents.protocols.ss7.isup.message.parameter.UserServiceInformation;

/**
*

bearerCapability:
This parameter indicates the type of the bearer capability connection or the transmission medium requirements to
the user. See IN CS2 Signalling Interworking Requirements [48].
It is a network option to select one of the two parameters to be used:

- bearerCap:
This parameter contains the value of the DSS1 Bearer Capability parameter (EN 300 403-1 [10]) in case the
SSF is at local exchange level or the value of the ISUP User Service Information parameter
(ITU-T Recommendation Q.763 [20]) in case the SSF is at transit exchange level.
ETSI EN 301 140-1 V1.3.4 (1999-06)

*
* @author sergey vetyutnev
*
*/
public interface BearerCap extends Serializable {

    byte[] getData();

    UserServiceInformation getUserServiceInformation() throws INAPException;

    // TODO: special "DSS1 Bearer Capability parameter" case - Bearer Capability
    // from Q.321 - from ISDN prptocol
    byte[] getBearerCapability();

}
