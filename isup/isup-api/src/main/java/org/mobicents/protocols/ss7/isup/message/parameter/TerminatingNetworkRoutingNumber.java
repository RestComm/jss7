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
 * Start time:13:01:51 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:13:01:51 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface TerminatingNetworkRoutingNumber extends Number, ISUPParameter {
    int _PARAMETER_CODE = 0;

    // FIXME: Add C defs

    /**
     * see Q.763 3.66 c4 : subscriber number (national use)
     */
    int _NAI_SN = 1;
    /**
     * see Q.763 3.66 c4 : unknown (national use)
     */
    int _NAI_UNKNOWN = 2;
    /**
     * see Q.763 3.66 c4 : national (significant) number
     */
    int _NAI_NATIONAL_SN = 3;
    /**
     * see Q.763 3.66 c4 : international number
     */
    int _NAI_IN = 4;
    /**
     * see Q.763 3.66 c4 : network specific number
     */
    int _NAI_NETWORK_SN = 5;

    /**
     * numbering plan indicator indicator value. See Q.763 - 3.9d
     */
    int _NPI_ISDN = 1;
    /**
     * numbering plan indicator indicator value. See Q.763 - 3.9d
     */
    int _NPI_DATA = 3;
    /**
     * numbering plan indicator indicator value. See Q.763 - 3.9d
     */
    int _NPI_TELEX = 4;

    int getNumberingPlanIndicator();

    void setNumberingPlanIndicator(int numberingPlanIndicator);

    int getNatureOfAddressIndicator();

    void setNatureOfAddressIndicator(int natureOfAddressIndicator);

    int getTnrnLengthIndicator();
}
