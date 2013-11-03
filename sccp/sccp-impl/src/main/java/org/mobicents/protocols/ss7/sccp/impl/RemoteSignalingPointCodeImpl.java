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

package org.mobicents.protocols.ss7.sccp.impl;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.sccp.RemoteSignalingPointCode;

/**
 * @author amit bhayani
 *
 */
public class RemoteSignalingPointCodeImpl implements XMLSerializable, RemoteSignalingPointCode {
    private static final String REMOTE_SPC = "remoteSpc";
    private static final String REMOTE_SPC_FLAG = "remoteSpcFlag";
    private static final String MASK = "mask";

    private int remoteSpc;
    private int remoteSpcFlag;
    private int mask;
    private boolean remoteSpcProhibited;
    private boolean remoteSccpProhibited;

    public RemoteSignalingPointCodeImpl() {

    }

    public RemoteSignalingPointCodeImpl(int remoteSpc, int remoteSpcFlag, int mask) {
        this.remoteSpc = remoteSpc;
        this.remoteSpcFlag = remoteSpcFlag;
        this.mask = mask;
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
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("rsp=").append(this.remoteSpc).append(" rsp-flag=").append(this.remoteSpcFlag).append(" mask=")
                .append(this.mask).append(" rsp-prohibited=").append(this.remoteSpcProhibited).append(" rsccp-prohibited=")
                .append(this.remoteSccpProhibited);
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
