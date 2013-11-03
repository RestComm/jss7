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

import org.mobicents.protocols.ss7.sccp.ConcernedSignalingPointCode;

/**
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 *
 */
public class ConcernedSignalingPointCodeImpl implements ConcernedSignalingPointCode, XMLSerializable {
    private static final String REMOTE_SPC = "remoteSpc";

    private int remoteSpc;

    public ConcernedSignalingPointCodeImpl() {
    }

    public ConcernedSignalingPointCodeImpl(int remoteSpc) {
        this.remoteSpc = remoteSpc;
    }

    public int getRemoteSpc() {
        return remoteSpc;
    }

    /**
     * @param remoteSpc the remoteSpc to set
     */
    protected void setRemoteSpc(int remoteSpc) {
        this.remoteSpc = remoteSpc;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("rsp=").append(this.remoteSpc);
        return sb.toString();
    }

    protected static final XMLFormat<ConcernedSignalingPointCodeImpl> XML = new XMLFormat<ConcernedSignalingPointCodeImpl>(
            ConcernedSignalingPointCodeImpl.class) {

        public void write(ConcernedSignalingPointCodeImpl ai, OutputElement xml) throws XMLStreamException {
            xml.setAttribute(REMOTE_SPC, ai.remoteSpc);

        }

        public void read(InputElement xml, ConcernedSignalingPointCodeImpl ai) throws XMLStreamException {
            ai.remoteSpc = xml.getAttribute(REMOTE_SPC).toInt();
        }
    };
}
