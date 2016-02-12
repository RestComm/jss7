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

import org.mobicents.protocols.ss7.sccp.SccpCongestionControlAlgo;
import org.mobicents.protocols.ss7.sccp.SccpStack;
import org.mobicents.protocols.ss7.sccp.impl.SccpManagement;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SccpCongestionControl {

    private SccpManagement sccpManagement;
    private SccpStack sccpStack;

    public SccpCongestionControl(SccpManagement sccpManagement, SccpStack sccpStack) {
        this.sccpManagement = sccpManagement;
        this.sccpStack = sccpStack;
    }

    public void scheduleTimer(Runnable timer, int delay) {
        this.sccpManagement.getManagementExecutors().schedule(timer, delay, TimeUnit.MILLISECONDS);
    }

    public void onRestrictionLevelChange(int affectedPc, int newLevel, boolean levelEncreased) {
        sccpManagement.onRestrictionLevelChange(affectedPc, newLevel, levelEncreased);
    }

    public int getCongControlTIMER_A() {
        return sccpStack.getCongControlTIMER_A();
    }

    public int getCongControlTIMER_D() {
        return sccpStack.getCongControlTIMER_D();
    }

    public int getCongControlN() {
        return ((SccpStackImpl) sccpStack).getCongControlN();
    }

    public int getCongControlM() {
        return ((SccpStackImpl) sccpStack).getCongControlM();
    }

    public SccpCongestionControlAlgo getCongControl_Algo() {
        return sccpStack.getCongControl_Algo();
    }

    public boolean isCongControl_blockingOutgoungScpMessages() {
        return sccpStack.isCongControl_blockingOutgoungSccpMessages();
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

    public static int getMaxRestrictionLevelForMtp3Level(int mtp3Level) {
        if (mtp3Level == 1)
            return 3;
        if (mtp3Level == 2)
            return 5;
        return 8;
    }
}
