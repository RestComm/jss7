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
 * OfferedCamel4CSIs ::= BIT STRING { o-csi (0), d-csi (1), vt-csi (2), t-csi (3), mt-sms-csi (4), mg-csi (5), psi-enhancements
 * (6) } (SIZE (7..16)) -- A node supporting Camel phase 4 shall mark in the BIT STRING all Camel4 CSIs -- it offers. -- Other
 * values than listed above shall be discarded.
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface OfferedCamel4CSIs extends Serializable {

    boolean getOCsi();

    boolean getDCsi();

    boolean getVtCsi();

    boolean getTCsi();

    boolean getMtSmsCsi();

    boolean getMgCsi();

    boolean getPsiEnhancements();

}
