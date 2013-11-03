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
 SpecificCSI-Withdraw ::= BIT STRING { o-csi (0), ss-csi (1), tif-csi (2), d-csi (3), vt-csi (4), mo-sms-csi (5), m-csi (6),
 * gprs-csi (7), t-csi (8), mt-sms-csi (9), mg-csi (10), o-IM-CSI (11), d-IM-CSI (12), vt-IM-CSI (13) } (SIZE(8..32)) --
 * exception handling: -- bits 11 to 31 shall be ignored if received by a non-IP Multimedia Core Network entity. -- bits 0-10
 * and 14-31 shall be ignored if received by an IP Multimedia Core Network entity. -- bits 11-13 are only applicable in an IP
 * Multimedia Core Network. -- Bit 8 and bits 11-13 are only applicable for the NoteSubscriberDataModified operation.
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface SpecificCSIWithdraw extends Serializable {

    boolean getOCsi();

    boolean getSsCsi();

    boolean getTifCsi();

    boolean getDCsi();

    boolean getVtCsi();

    boolean getMoSmsCsi();

    boolean getMCsi();

    boolean getGprsCsi();

    boolean getTCsi();

    boolean getMtSmsCsi();

    boolean getMgCsi();

    boolean getOImCsi();

    boolean getDImCsi();

    boolean getVtImCsi();

}
