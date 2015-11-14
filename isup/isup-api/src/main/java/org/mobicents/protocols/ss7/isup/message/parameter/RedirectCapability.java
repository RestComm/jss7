/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

/**
 * Start time:13:54:36 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:13:54:36 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface RedirectCapability extends ISUPParameter {

    int _PARAMETER_CODE = 0x4E;

    /**
     * See Q.763 3.96 Redirect possible indicator : not used
     */
    int _RPI_NOT_USED = 0;
    /**
     * See Q.763 3.96 Redirect possible indicator : redirect possible before ACM use)
     */
    int _RPI_RPB_ACM = 1;
    /**
     * See Q.763 3.96 Redirect possible indicator : redirect possible before ANM
     */
    int _RPI_RPB_ANM = 2;
    /**
     * See Q.763 3.96 Redirect possible indicator : redirect possible at any time during the call
     */
    int _RPI_RPANTDC = 3;

    byte[] getCapabilities();

    void setCapabilities(byte[] capabilities);

    int getCapability(byte b);
}
