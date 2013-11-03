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
 * Start time:13:29:53 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:13:29:53 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface MLPPPrecedence extends ISUPParameter {
    int _PARAMETER_CODE = 0x3A;

    /**
     * See Q.763 3.34 LFB (Look ahead for busy) : LFB allowed
     */
    int _LFB_INDICATOR_ALLOWED = 0;
    /**
     * See Q.763 3.34 LFB (Look ahead for busy) : path reserved (national use)
     */
    int _LFB_INDICATOR_PATH_RESERVED = 1;
    /**
     * See Q.763 3.34 LFB (Look ahead for busy) : LFB not allowed
     */
    int _LFB_INDICATOR_NOT_ALLOWED = 2;

    /**
     * See Q.763 3.34 Precedence level : flash override
     */
    int _PLI_FLASH_OVERRIDE = 0;

    /**
     * See Q.763 3.34 Precedence level : flash
     */
    int _PLI_FLASH = 1;
    /**
     * See Q.763 3.34 Precedence level : immediate
     */
    int _PLI_IMMEDIATE = 2;
    /**
     * See Q.763 3.34 Precedence level : priority
     */
    int _PLI_PRIORITY = 3;

    /**
     * See Q.763 3.34 Precedence level : routine
     */
    int _PLI_ROUTINE = 4;

    byte getLfb();

    void setLfb(byte lfb);

    byte getPrecedenceLevel();

    void setPrecedenceLevel(byte precedenceLevel);

    int getMllpServiceDomain();

    void setMllpServiceDomain(int mllpServiceDomain);

    byte[] getNiDigits();

    void setNiDigits(byte[] niDigits);
}
