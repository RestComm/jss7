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
 * Start time:12:47:23 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:12:47:23 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface EventInformation extends ISUPParameter {
    int _PARAMETER_CODE = 0x24;

    /**
     * See Q.763 3.21 Event indicator : ALERTING
     */
    int _EVENT_INDICATOR_ALERTING = 1;

    /**
     * See Q.763 3.21 Event indicator : PROGRESS
     */
    int _EVENT_INDICATOR_PROGRESS = 2;

    /**
     * See Q.763 3.21 Event indicator : in-band information or an appropriate pattern is now available
     */
    int _EVENT_INDICATOR_IIIOPA = 3;

    /**
     * See Q.763 3.21 Event indicator : call forwarded on busy (national use)
     */
    int _EVENT_INDICATOR_CFOB = 4;

    /**
     * See Q.763 3.21 Event indicator : call forwarded on no reply (national use)
     */
    int _EVENT_INDICATOR_CFONNR = 5;

    /**
     * See Q.763 3.21 Event indicator : call forwarded unconditional (national use)
     */
    int _EVENT_INDICATOR_CFOU = 6;

    /**
     * See Q.763 3.21 Event presentation restricted indicator (national use) : no indication
     */
    boolean _EVENT_PRESENTATION_INI = false;

    /**
     * See Q.763 3.21 Event presentation restricted indicator (national use) : presentation restricted
     */
    boolean _EVENT_PRESENTATION_IPR = true;

    int getEventIndicator();

    void setEventIndicator(int eventIndicator);

    boolean isEventPresentationRestrictedIndicator();

    void setEventPresentationRestrictedIndicator(boolean eventPresentationRestrictedIndicator);

}
