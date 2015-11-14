/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.mobicents.protocols.ss7.map.primitives;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class OidContainer {

    private long[] data;

    public OidContainer() {
    }

    public OidContainer(long[] val) {
        this.data = val;
    }

    public void parseSerializedData(String val) throws NumberFormatException {
        if (val == null || val.length() == 0) {
            this.data = new long[0];
            return;
        }

        String[] ss = val.split("\\.");
        this.data = new long[ss.length];
        for (int i1 = 0; i1 < ss.length; i1++) {
            data[i1] = Long.parseLong(ss[i1]);
        }
    }

    public long[] getData() {
        return data;
    }

    public void setData(long[] val) {
        data = val;
    }

    public String getSerializedData() {
        if (this.data == null)
            return "";
        else {
            boolean isFirst = true;
            StringBuilder sb = new StringBuilder();
            for (long l : data) {
                if (isFirst)
                    isFirst = false;
                else
                    sb.append(".");
                sb.append(l);
            }
            return sb.toString();
        }
    }
}
