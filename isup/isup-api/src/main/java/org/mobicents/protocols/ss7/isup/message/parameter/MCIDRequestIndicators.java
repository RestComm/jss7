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
 * Start time:13:26:06 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:13:26:06 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface MCIDRequestIndicators extends ISUPParameter {
    int _PARAMETER_CODE = 0x3B;

    // FIXME: its byte[], there may be more indicators than one byte

    /**
     * Flag that indicates that information is requested
     */
    boolean _INDICATOR_REQUESTED = true;

    /**
     * Flag that indicates that information is not requested
     */
    boolean _INDICATOR_NOT_REQUESTED = false;

    boolean isMcidRequestIndicator();

    void setMcidRequestIndicator(boolean mcidRequestIndicator);

    boolean isHoldingIndicator();

    void setHoldingIndicator(boolean holdingIndicator);

}
