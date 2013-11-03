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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation;

import java.io.Serializable;
import java.util.ArrayList;

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtCallBarringFeature;
import org.mobicents.protocols.ss7.map.api.service.supplementary.Password;

/**
 *
 CallBarringData ::= SEQUENCE { callBarringFeatureList Ext-CallBarFeatureList, password Password OPTIONAL,
 * wrongPasswordAttemptsCounter WrongPasswordAttemptsCounter OPTIONAL, notificationToCSE NULL OPTIONAL, extensionContainer
 * ExtensionContainer OPTIONAL, ...}
 *
 * Ext-CallBarFeatureList ::= SEQUENCE SIZE (1..32) OF Ext-CallBarringFeature
 *
 * WrongPasswordAttemptsCounter ::= INTEGER (0..4)
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface CallBarringData extends Serializable {

    ArrayList<ExtCallBarringFeature> getCallBarringFeatureList();

    Password getPassword();

    Integer getWrongPasswordAttemptsCounter();

    boolean getNotificationToCSE();

    MAPExtensionContainer getExtensionContainer();

}
