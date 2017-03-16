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

package org.mobicents.protocols.ss7.sccp.impl;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.sccp.RemoteSignalingPointCode;
import org.mobicents.protocols.ss7.sccp.SccpCongestionControlAlgo;
import org.mobicents.protocols.ss7.sccp.impl.congestion.CongStateTimerA;
import org.mobicents.protocols.ss7.sccp.impl.congestion.CongStateTimerD;
import org.mobicents.protocols.ss7.sccp.impl.congestion.SccpCongestionControl;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class RemoteSignalingPointCodeImpl implements XMLSerializable, RemoteSignalingPointCode {
    private static final String REMOTE_SPC = "remoteSpc";
    private static final String REMOTE_SPC_FLAG = "remoteSpcFlag";
    private static final String MASK = "mask";

    private Logger logger = Logger.getLogger(RemoteSignalingPointCodeImpl.class);

    private int remoteSpc;
    private int remoteSpcFlag;
    private int mask;
    protected boolean remoteSpcProhibited;
    protected boolean remoteSccpProhibited;

    protected int rl;
    protected int rsl;
    private CongStateTimerA timerA;
    private CongStateTimerD timerD;

    private SccpCongestionControl sccpCongestionControl;

    public RemoteSignalingPointCodeImpl() {
    }

    public RemoteSignalingPointCodeImpl(int remoteSpc, int remoteSpcFlag, int mask, boolean isProhibited) {
        this.remoteSpc = remoteSpc;
        this.remoteSpcFlag = remoteSpcFlag;
        this.mask = mask;
        this.remoteSccpProhibited = isProhibited;
        this.remoteSpcProhibited = isProhibited;
    }

    public int getRemoteSpc() {
        return remoteSpc;
    }

    public int getRemoteSpcFlag() {
        return remoteSpcFlag;
    }

    public int getMask() {
        return mask;
    }

    public boolean isRemoteSpcProhibited() {
        return remoteSpcProhibited;
    }

    public boolean isRemoteSccpProhibited() {
        return remoteSccpProhibited;
    }

    protected void setProhibitedState(boolean remoteSpcProhibited, boolean remoteSccpProhibited) {
        this.remoteSpcProhibited = remoteSpcProhibited;
        this.remoteSccpProhibited = remoteSccpProhibited;
    }

    protected void setRemoteSpcProhibited(boolean remoteSpcProhibited) {
        this.remoteSpcProhibited = remoteSpcProhibited;
    }

    protected void setRemoteSccpProhibited(boolean remoteSccpProhibited) {
        this.remoteSccpProhibited = remoteSccpProhibited;
    }

    /**
     * @param remoteSpc the remoteSpc to set
     */
    protected void setRemoteSpc(int remoteSpc) {
        this.remoteSpc = remoteSpc;
    }

    /**
     * @param remoteSpcFlag the remoteSpcFlag to set
     */
    protected void setRemoteSpcFlag(int remoteSpcFlag) {
        this.remoteSpcFlag = remoteSpcFlag;
    }

    /**
     * @param mask the mask to set
     */
    protected void setMask(int mask) {
        this.mask = mask;
    }

    @Override
    public int getCurrentRestrictionLevel() {
        return rl;
    }

    public int getCurrentRestrictionSubLevel() {
        return rsl;
    }

    public void clearCongLevel(SccpCongestionControl sccpCongestionControl) {
        this.sccpCongestionControl = sccpCongestionControl;

        this.rl = 0;
        this.rsl = 0;

        this.sccpCongestionControl.onRestrictionLevelChange(remoteSpc, rl, false);
    }

    public void increaseCongLevel(SccpCongestionControl sccpCongestionControl, int level) {
        this.sccpCongestionControl = sccpCongestionControl;

        if (this.timerA != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("SCCP cong increaseCongLevel - no actions because of timerA is not over: " + this);
            }
            return;
        }

        timerA = new CongStateTimerA(this);
        this.sccpCongestionControl.scheduleTimer(timerA, sccpCongestionControl.getCongControlTIMER_A());
        CongStateTimerD _timerD = timerD;
        if (_timerD != null) {
            _timerD.cancel();
        }
        timerD = new CongStateTimerD(this);
        this.sccpCongestionControl.scheduleTimer(timerD, sccpCongestionControl.getCongControlTIMER_D());

        if (rl >= sccpCongestionControl.getCongControlN()) {
            if (logger.isDebugEnabled()) {
                logger.debug("SCCP cong increaseCongLevel - no actions because rl has its max level: " + this);
            }
            return;
        }
        if (sccpCongestionControl.getCongControl_Algo() == SccpCongestionControlAlgo.levelDepended
                && rl >= SccpCongestionControl.getMaxRestrictionLevelForMtp3Level(level)) {
            return;
        }

        rsl++;
        if (rsl >= sccpCongestionControl.getCongControlM()) {
            rsl = 0;
            rl++;
            if (logger.isDebugEnabled()) {
                logger.debug("SCCP cong increaseCongLevel - rl has increased: " + this);
            }

            this.sccpCongestionControl.onRestrictionLevelChange(remoteSpc, rl, true);
        }
    }

    public void clearTimerA() {
        timerA = null;
    }

    public void decreaseCongLevel() {
        if (rl == 0 && rsl == 0)
            return;

        rsl--;
        if (rsl < 0) {
            rsl = sccpCongestionControl.getCongControlM() - 1;
            rl--;
            if (logger.isDebugEnabled()) {
                logger.debug("SCCP cong increaseCongLevel - rl has decreased: " + this);
            }

            this.sccpCongestionControl.onRestrictionLevelChange(remoteSpc, rl, false);
        }

        timerD = new CongStateTimerD(this);
        this.sccpCongestionControl.scheduleTimer(timerD, sccpCongestionControl.getCongControlTIMER_D());
    }

    /**
     * Do not use this method directly except of debugging. Use clearCongLevel(), increaseCongLevel(), decreaseCongLevel()
     *
     * @param value
     */
    protected void setCurrentRestrictionLevel(int value) {
        this.rl = value;
        this.rsl = 0;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("rsp=").append(this.remoteSpc).append(" rsp-flag=").append(this.remoteSpcFlag).append(" mask=")
                .append(this.mask).append(" rsp-prohibited=").append(this.remoteSpcProhibited).append(" rsccp-prohibited=")
                .append(this.remoteSccpProhibited).append(" rl=").append(rl).append(" rsl=").append(rsl);
        return sb.toString();
    }

    protected static final XMLFormat<RemoteSignalingPointCodeImpl> XML = new XMLFormat<RemoteSignalingPointCodeImpl>(
            RemoteSignalingPointCodeImpl.class) {

        public void write(RemoteSignalingPointCodeImpl ai, OutputElement xml) throws XMLStreamException {
            xml.setAttribute(REMOTE_SPC, ai.remoteSpc);
            xml.setAttribute(REMOTE_SPC_FLAG, ai.remoteSpcFlag);
            xml.setAttribute(MASK, ai.mask);

        }

        public void read(InputElement xml, RemoteSignalingPointCodeImpl ai) throws XMLStreamException {
            ai.remoteSpc = xml.getAttribute(REMOTE_SPC).toInt();
            ai.remoteSpcFlag = xml.getAttribute(REMOTE_SPC_FLAG).toInt();
            ai.mask = xml.getAttribute(MASK).toInt();
        }
    };

}
