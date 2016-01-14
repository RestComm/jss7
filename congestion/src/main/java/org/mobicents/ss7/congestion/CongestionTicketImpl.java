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

package org.mobicents.ss7.congestion;

import java.util.Map.Entry;

import javolution.util.FastMap;

/**
 * The Congestion Ticket interface contains info for the source of congestion and the congestion level
 *
 * @author sergey vetyutnev
 *
 */
public class CongestionTicketImpl implements CongestionTicket {

    private String source;
    private int level;
    private FastMap<String, Object> attributeList;

    public CongestionTicketImpl(String source, int level) {
        this.source = source;
        this.level = level;
    }

    public void setAttribute(String key, Object value) {
        if (attributeList == null)
            attributeList = new FastMap<String, Object>();
        attributeList.put(key, value);
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public Object getAttribute(String key) {
        if (attributeList != null)
            return attributeList.get(key);
        else
            return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("CongestionTicket=[source=");
        sb.append(source);
        sb.append(", level=");
        sb.append(level);

        if (attributeList != null) {
            sb.append(", attributes=[");
            int i1 = 0;
            for (Entry<String, Object> el : attributeList.entrySet()) {
                if (i1 == 0)
                    i1 = 1;
                else
                    sb.append(", ");

                sb.append(el.getKey());
                sb.append("=");
                sb.append(el.getValue());
            }
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }

}
