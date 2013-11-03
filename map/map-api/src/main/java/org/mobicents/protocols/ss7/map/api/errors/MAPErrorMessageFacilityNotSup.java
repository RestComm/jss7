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

/**
 * The MAP ReturnError message: MessageFacilityNotSup with parameters
 *
 * facilityNotSupported ERROR ::= { PARAMETER FacilityNotSupParam -- optional -- FacilityNotSupParam must not be used in version
 * <3 CODE local:21 }
 *
 *
 * FacilityNotSupParam ::= SEQUENCE { extensionContainer ExtensionContainer OPTIONAL, ..., shapeOfLocationEstimateNotSupported
 * [0] NULL OPTIONAL, neededLcsCapabilityNotSupportedInServingNode [1] NULL OPTIONAL }
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface MAPErrorMessageFacilityNotSup extends MAPErrorMessage {

    MAPExtensionContainer getExtensionContainer();

    Boolean getShapeOfLocationEstimateNotSupported();

    Boolean getNeededLcsCapabilityNotSupportedInServingNode();

    void setExtensionContainer(MAPExtensionContainer extensionContainer);

    void setShapeOfLocationEstimateNotSupported(Boolean shapeOfLocationEstimateNotSupported);

    void getNeededLcsCapabilityNotSupportedInServingNode(Boolean neededLcsCapabilityNotSupportedInServingNode);

}
