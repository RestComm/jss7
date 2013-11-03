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

package org.mobicents.protocols.ss7.map.api.errors;

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NetworkResource;

/**
 * The MAP ReturnError message: SystemFailure
 *
 * systemFailure ERROR ::= { PARAMETER SystemFailureParam -- optional CODE local:34 }
 *
 *
 * SystemFailureParam ::= CHOICE { networkResource NetworkResource, -- networkResource must not be used in version 3
 * extensibleSystemFailureParam ExtensibleSystemFailureParam -- extensibleSystemFailureParam must not be used in version <3 }
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface MAPErrorMessageSystemFailure extends MAPErrorMessage {

    NetworkResource getNetworkResource();

    AdditionalNetworkResource getAdditionalNetworkResource();

    MAPExtensionContainer getExtensionContainer();

    long getMapProtocolVersion();

    void setNetworkResource(NetworkResource networkResource);

    void setAdditionalNetworkResource(AdditionalNetworkResource additionalNetworkResource);

    void setExtensionContainer(MAPExtensionContainer extensionContainer);

    void setMapProtocolVersion(long mapProtocolVersion);
}
