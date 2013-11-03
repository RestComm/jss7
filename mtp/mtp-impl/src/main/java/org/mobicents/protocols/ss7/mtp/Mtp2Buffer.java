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

package org.mobicents.protocols.ss7.mtp;

/**
 * @author baranowb
 *
 */
public class Mtp2Buffer {
    /**
     * data in this frame.
     */
    byte[] frame = new byte[272 + 7]; // +7 - 272 is max SIF part len,
    /**
     * length of actual data fram to be transmited.
     */
    int len = 0;// len pointer for this buffer, indicates how much data is there really.

    int offset = 0; // offset from beggining of buffer.
    // public boolean isFree()
}
