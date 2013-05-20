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
public interface SpecificCSIWithdraw {

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
