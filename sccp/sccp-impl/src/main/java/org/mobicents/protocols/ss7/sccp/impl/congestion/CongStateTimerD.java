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

import org.mobicents.protocols.ss7.sccp.impl.RemoteSignalingPointCodeImpl;
import org.apache.log4j.Logger;

/**
*
* @author sergey vetyutnev
*
*/
public class CongStateTimerD implements Runnable {
    private RemoteSignalingPointCodeImpl remoteSignalingPointCodeImpl;
    private boolean isCancelled;
    private Logger logger = Logger.getLogger(CongStateTimerD.class);

    public CongStateTimerD(RemoteSignalingPointCodeImpl remoteSignalingPointCodeImpl) {
        if (logger.isDebugEnabled()) {
            logger.debug("SCCP cong TimerD has started: " + remoteSignalingPointCodeImpl);
        }
        this.remoteSignalingPointCodeImpl = remoteSignalingPointCodeImpl;
    }

    @Override
    public void run() {
        if (!isCancelled) {
            if (logger.isDebugEnabled()) {
                logger.debug("SCCP cong TimerD decreasing cong level: " + remoteSignalingPointCodeImpl);
            }
            remoteSignalingPointCodeImpl.decreaseCongLevel();
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("SCCP cong TimerD was concelled: " + remoteSignalingPointCodeImpl);
            }
        }
    }

    public void cancel() {
        if (logger.isDebugEnabled()) {
            logger.debug("SCCP cong TimerD is cancelling: " + remoteSignalingPointCodeImpl);
        }
        isCancelled = true;
    }
}
