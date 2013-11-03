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

/**
 * Start time:13:44:52 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:13:44:52 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface PivotCapability extends ISUPParameter {

    int _PARAMETER_CODE = 0x7B;

    // FIXME: add C defs
    /**
     * See Q.763 3.84 Pivot possible indicator : no indication
     */
    int _PPI_NO_INDICATION = 0;

    /**
     * See Q.763 3.84 Pivot possible indicator : pivot routing possible before ACM
     */
    int _PPI_PRPB_ACM = 1;

    /**
     * See Q.763 3.84 Pivot possible indicator : pivot routing possible before ANM
     */
    int _PPI_PRPB_ANM = 2;

    /**
     * See Q.763 3.84 Pivot possible indicator : pivot routing possible any time during the call
     */
    int _PPI_PRPB_ANY = 3;

    /**
     * See Q.763 3.84 Interworking to redirection indicator (national use)
     */
    boolean _ITRI_ALLOWED = false;

    /**
     * See Q.763 3.84 Interworking to redirection indicator (national use)
     */
    boolean _ITRI_NOT_ALLOWED = true;

    byte[] getPivotCapabilities();

    void setPivotCapabilities(byte[] pivotCapabilities);

    byte createPivotCapabilityByte(boolean itriNotAllowed, int pivotPossibility);

    boolean getITRINotAllowed(byte b);

    int getPivotCapability(byte b);
}
