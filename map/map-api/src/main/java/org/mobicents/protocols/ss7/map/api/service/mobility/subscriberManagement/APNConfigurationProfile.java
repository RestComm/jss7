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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement;

import java.io.Serializable;
import java.util.ArrayList;

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 *
 APN-ConfigurationProfile ::= SEQUENCE { defaultContext ContextId, completeDataListIncluded NULL OPTIONAL, -- If segmentation
 * is used, completeDataListIncluded may only be present in the -- first segment of APN-ConfigurationProfile. epsDataList [1]
 * EPS-DataList, extensionContainer [2] ExtensionContainer OPTIONAL, ... }
 *
 * ContextId ::= INTEGER (1..50)
 *
 * EPS-DataList ::= SEQUENCE SIZE (1..50) OF APN-Configuration
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface APNConfigurationProfile extends Serializable {

    int getDefaultContext();

    boolean getCompleteDataListIncluded();

    ArrayList<APNConfiguration> getEPSDataList();

    MAPExtensionContainer getExtensionContainer();

}
