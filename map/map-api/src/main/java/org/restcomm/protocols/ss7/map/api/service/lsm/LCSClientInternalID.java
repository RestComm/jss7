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

package org.restcomm.protocols.ss7.map.api.service.lsm;

/**
<code>
LCSClientInternalID ::= ENUMERATED {
  broadcastService (0),
  o-andM-HPLMN (1),
  o-andM-VPLMN (2),
  anonymousLocation (3),
  targetMSsubscribedService (4),
  ...
}
-- for a CAMEL phase 3 PLMN operator client, the value targetMSsubscribedService shall be used
</code>
 *
 * @author amit bhayani
 *
 */
public enum LCSClientInternalID {

    broadcastService(0), oandMHPLMN(1), oandMVPLMN(2), anonymousLocation(3), targetMSsubscribedService(4);

    private final int id;

    private LCSClientInternalID(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static LCSClientInternalID getLCSClientInternalID(int type) {
        switch (type) {
            case 0:
                return broadcastService;
            case 1:
                return oandMHPLMN;
            case 2:
                return oandMVPLMN;
            case 3:
                return anonymousLocation;
            case 4:
                return targetMSsubscribedService;
            default:
                return null;
        }
    }

}
