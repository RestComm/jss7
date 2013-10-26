/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
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

package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import org.mobicents.protocols.ss7.isup.message.parameter.PivotReason;

/**
 * @author baranowb
 *
 */
public class PivotReasonImpl implements PivotReason {

    private byte pivotReason;
    //we need to know if that one was set.
    private Byte pivotPossibleAtPerformingExchange;
    public PivotReasonImpl() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public byte getPivotReason() {
        return this.pivotReason;
    }

    @Override
    public void setPivotReason(byte b) {
        this.pivotReason = (byte) (b & 0x7F);
    }

    @Override
    public byte getPivotPossibleAtPerformingExchange() {
        if(this.pivotPossibleAtPerformingExchange == null)
            return 0;
        return this.pivotPossibleAtPerformingExchange;
    }

    @Override
    public void setPivotPossibleAtPerformingExchange(byte b) {
        this.pivotPossibleAtPerformingExchange = (byte) (b & 0x07);
    }

    public byte[] encode(){
        byte[] data = null;
        if(this.pivotPossibleAtPerformingExchange == null){
            data = new byte[1];
            data[0] = (byte) (0x80 | this.pivotReason);
        } else {
            data = new byte[2];
            data[0] = this.pivotReason;
            data[1] = this.pivotPossibleAtPerformingExchange;
        }
        return data;
    }

    /**
     * Remove pivotPossbileAtPerformingExchange.
     */
    void trim(){
        this.pivotPossibleAtPerformingExchange = null;
    }
}
