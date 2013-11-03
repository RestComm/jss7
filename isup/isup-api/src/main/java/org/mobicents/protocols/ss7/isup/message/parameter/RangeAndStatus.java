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
 * Start time:13:52:59 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:13:52:59 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * This RangeAndStatus indiactes whcih CICs, starting from one present in message are affected. Range indicates how many CICs
 * are potentially affected. Status contains bits indicating CIC affected(1 - affected, 0 - not affected) <br>
 * For content interpretation refer to Q.763 3.43
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface RangeAndStatus extends ISUPParameter {

    int _PARAMETER_CODE = 0x16;

    /**
     * Fetches range.
     *
     * @return
     */
    byte getRange();

    /**
     * Sets range.
     *
     * @param range
     * @param addStatus - flag indicates if implementation should create proper status
     */
    void setRange(byte range, boolean addStatus);

    /**
     * Sets range.
     *
     * @param range
     */
    void setRange(byte range);

    /**
     * Gets raw status part.
     *
     * @return
     */
    byte[] getStatus();

    /**
     * Gets raw status part.
     *
     * @return
     */
    void setStatus(byte[] status);

    void setAffected(byte subrange, boolean v) throws IllegalArgumentException;

    boolean isAffected(byte b) throws IllegalArgumentException;
}
