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
 * Start time:10:27:43 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>

 */
package org.mobicents.protocols.ss7.isup.message.parameter;

import java.io.Serializable;

/**
 * Start time:10:27:43 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface NatureOfAddressIndicator extends Serializable{

    /**
     * nature of address indicator value. See Q.763 - 3.9b
     */
    int _SPARE = 0;
    /**
     * nature of address indicator value. See Q.763 - 3.9b
     */
    int _SUBSCRIBER = 1;
    /**
     * nature of address indicator value. See Q.763 - 3.9b
     */
    int _UNKNOWN = 2;
    /**
     * nature of address indicator value. See Q.763 - 3.9b
     */
    int _NATIONAL = 3;
    /**
     * nature of address indicator value. See Q.763 - 3.9b
     */
    int _INTERNATIONAL = 4;
    /**
     * nature of address indicator value. See Q.763 - 3.9b
     */
    int _NETWORK_SPECIFIC = 5;
}
