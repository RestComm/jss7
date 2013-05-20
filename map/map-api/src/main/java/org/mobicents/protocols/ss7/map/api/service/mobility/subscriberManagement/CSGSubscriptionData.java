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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement;

import java.util.ArrayList;

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.Time;

/**
 *
 CSG-SubscriptionData ::= SEQUENCE { csg-Id CSG-Id, expirationDate Time OPTIONAL, extensionContainer ExtensionContainer
 * OPTIONAL, ..., lipa-AllowedAPNList [0] LIPA-AllowedAPNList OPTIONAL }
 *
 * LIPA-AllowedAPNList ::= SEQUENCE SIZE (1..50) OF APN
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface CSGSubscriptionData {

    CSGId getCsgId();

    Time getExpirationDate();

    MAPExtensionContainer getExtensionContainer();

    ArrayList<APN> getLipaAllowedAPNList();

}
