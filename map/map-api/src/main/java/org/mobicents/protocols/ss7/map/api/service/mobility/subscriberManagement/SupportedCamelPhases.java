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

/**
 *
 * SupportedCamelPhases ::= BIT STRING { phase1 (0), phase2 (1), phase3 (2), phase4 (3)} (SIZE (1..16)) -- A node shall mark in
 * the BIT STRING all CAMEL Phases it supports. -- Other values than listed above shall be discarded.
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface SupportedCamelPhases extends Serializable {

    boolean getPhase1Supported();

    boolean getPhase2Supported();

    boolean getPhase3Supported();

    boolean getPhase4Supported();

}
