/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.mobicents.protocols.ss7.sccp.impl.congestion;

import org.mobicents.protocols.ss7.sccp.NetworkIdState;

/**
*
* @author sergey vetyutnev
*
*/
public class NetworkIdStateImpl implements NetworkIdState {
    private boolean available;
    private int congLevel;
    private boolean affectedByPc;

    public NetworkIdStateImpl(boolean affectedByPc) {
        this.affectedByPc = affectedByPc;
    }

    public NetworkIdStateImpl(boolean available, boolean affectedByPc) {
        this.available = available;
        this.affectedByPc = affectedByPc;
    }

    public NetworkIdStateImpl(int congLevel, boolean affectedByPc) {
        this.congLevel = congLevel;
        this.affectedByPc = affectedByPc;
    }

    public boolean isAvailavle() {
        return this.available;
    }

    public int getCongLevel() {
        return this.congLevel;
    }

    public boolean isAffectedByPc() {
        return this.affectedByPc;
    }

    public void setAffectedByPc(boolean val) {
        this.affectedByPc = val;
    }

//    public boolean isWorseLevel(NetworkIdStateImpl comp) {
//        if (comp == null)
//            return false;
//        if (this.available != comp.available) {
//            if (!comp.available)
//                return true;
//            else
//                return false;
//        }
//        if (this.congLevel != comp.congLevel) {
//            if (this.congLevel < comp.congLevel)
//                return true;
//            else
//                return false;
//        }
//        return false;
//    }

}
