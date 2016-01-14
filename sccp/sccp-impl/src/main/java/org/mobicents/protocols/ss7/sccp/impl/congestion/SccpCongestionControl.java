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

import java.util.concurrent.TimeUnit;

import org.mobicents.protocols.ss7.sccp.impl.SccpManagement;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SccpCongestionControl {
    protected static int N = 8;
    protected static int M = 4;
    protected static int TIMER_A = 200;
    protected static int TIMER_D = 2000;

    private SccpManagement sccpManagement;

    public SccpCongestionControl(SccpManagement sccpManagement) {
        this.sccpManagement = sccpManagement;
    }

    public void scheduleTimer(Runnable timer, int delay) {
        this.sccpManagement.getManagementExecutors().schedule(timer, delay, TimeUnit.MILLISECONDS);
    }

    public void onRestrictionLevelChange(int affectedPc, int newLevel, boolean levelEncreased) {
        sccpManagement.onRestrictionLevelChange(affectedPc, newLevel, levelEncreased);
    }

    public int getTIMER_A() {
        return TIMER_A;
    }

    public int getTIMER_D() {
        return TIMER_D;
    }

    public int getN() {
        return N;
    }

    public int getM() {
        return M;
    }

    public static int generateSccpUserCongLevel(int restrictionLevel) {
        if (restrictionLevel <= 1)
            return 0;
        if (restrictionLevel <= 3)
            return 1;
        if (restrictionLevel <= 5)
            return 2;
        return 3;
    }
}
