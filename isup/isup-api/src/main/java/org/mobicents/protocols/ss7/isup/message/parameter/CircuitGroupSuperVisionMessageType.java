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
 * Start time:12:20:48 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:12:20:48 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface CircuitGroupSuperVisionMessageType extends ISUPParameter {
    int _PARAMETER_CODE = 0x15;
    // FIXME: V->v

    /**
     * See Q.763 3.13 Circuit group supervision message type indicator maintenance oriented
     */
    int _CIRCUIT_GROUP_SMTIMO = 0;
    /**
     * See Q.763 3.13 Circuit group supervision message type indicator hardware failure oriented
     */
    int _CIRCUIT_GROUP_SMTIHFO = 1;

    int getCircuitGroupSuperVisionMessageTypeIndicator();

    void setCircuitGroupSuperVisionMessageTypeIndicator(int CircuitGroupSuperVisionMessageTypeIndicator);
}
