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
 * Start time:13:49:29 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:13:49:29 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface PivotRoutingIndicators extends ISUPParameter {
    int _PARAMETER_CODE = 0x7C;

    // FIXME: add C defs

    /**
     * See Q.763 3.85 Pivot routing indicators : no indication
     */
    int _PRI_NO_INDICATION = 0;
    /**
     * See Q.763 3.85 Pivot routing indicators : pivote request
     */
    int _PRI_PIVOT_REQUEST = 1;
    /**
     * See Q.763 3.85 Pivot routing indicators : cancel pivot request
     */
    int _PRI_C_PR = 2;
    /**
     * See Q.763 3.85 Pivot routing indicators : pivot request failure
     */
    int _PRI_PRF = 3;
    /**
     * See Q.763 3.85 Pivot routing indicators : interworking to redirection prohibited (backward) (national use)
     */
    int _PRI_ITRP = 4;

    byte[] getPivotRoutingIndicators();

    void setPivotRoutingIndicators(byte[] pivotRoutingIndicators);
}
