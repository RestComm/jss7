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

import java.util.ArrayList;
import java.util.List;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.PivotStatus;
import org.mobicents.protocols.ss7.isup.message.parameter.Status;

/**
 * @author baranowb
 *
 */
public class PivotStatusImpl extends AbstractISUPParameter implements PivotStatus {
    private List<Status> statusList = new ArrayList<Status>();

    public PivotStatusImpl() {
        // TODO Auto-generated constructor stub
    }

    public PivotStatusImpl(byte[] data) throws ParameterException {
        decode(data);
    }

    @Override
    public int getCode() {
        return _PARAMETER_CODE;
    }

    @Override
    public int decode(byte[] b) throws ParameterException {
        //FIXME: ? this does not take into account case when extension bit is used.
        for (byte v : b) {
            Status s = new StatusImpl();
            s.setStatus(v);
            this.statusList.add(s);
        }
        return b.length;
    }

    @Override
    public byte[] encode() throws ParameterException {
        byte[] data = new byte[this.statusList.size()];
        for (int index = 0; index < data.length; index++) {
            data[index] = this.statusList.get(index).getStatus();
        }
        return data;
    }

    @Override
    public void setStatus(Status... status) {
        this.statusList.clear();
        for (Status s : status) {
            if (s != null) {
                this.statusList.add(s);
            }
        }
    }

    @Override
    public Status[] getStatus() {
        return this.statusList.toArray(new Status[] {});
    }

}
