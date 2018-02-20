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

package org.restcomm.protocols.ss7.map.smstpdu;

import org.restcomm.protocols.ss7.map.api.smstpdu.ApplicationPortAddressing16BitAddress;
import org.restcomm.protocols.ss7.map.api.smstpdu.UserDataHeader;

/**
*
* @author sergey vetyutnev
*
*/
public class ApplicationPortAddressing16BitAddressImpl implements ApplicationPortAddressing16BitAddress {

    private int destinationPort;
    private int originatorPort;

    /**
     * @param destinationPort
     *            These octets contain a number indicating the receiving port,
     *            i.e. application, in the receiving device.
     * @param originatorPort
     *            These octets contain a number indicating the sending port,
     *            i.e. application, in the sending device.
     */
    public ApplicationPortAddressing16BitAddressImpl(int destinationPort, int originatorPort) {
        this.destinationPort = destinationPort;
        this.originatorPort = originatorPort;
    }

    public ApplicationPortAddressing16BitAddressImpl(byte[] encodedInformationElementData) {

        if (encodedInformationElementData == null || encodedInformationElementData.length != 4)
            return;

        this.destinationPort = ((encodedInformationElementData[0] & 0xFF) << 8) + (encodedInformationElementData[1] & 0xFF);
        this.originatorPort = ((encodedInformationElementData[2] & 0xFF) << 8) + (encodedInformationElementData[3] & 0xFF);
    }

    @Override
    public int getDestinationPort() {
        return destinationPort;
    }

    @Override
    public int getOriginatorPort() {
        return originatorPort;
    }

    @Override
    public int getEncodedInformationElementIdentifier() {
        return UserDataHeader._InformationElementIdentifier_ApplicationPortAddressingScheme16BitAddress;
    }

    @Override
    public byte[] getEncodedInformationElementData() {
        byte[] res = new byte[4];
        res[0] = (byte) ((this.destinationPort & 0xFF00) >> 8);
        res[1] = (byte) (this.destinationPort & 0x00FF);
        res[2] = (byte) ((this.originatorPort & 0xFF00) >> 8);
        res[3] = (byte) (this.originatorPort & 0x00FF);
        return res;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ApplicationPortAddressing16BitAddress [");
        sb.append("destinationPort=");
        sb.append(this.destinationPort);
        sb.append(", originatorPort=");
        sb.append(this.originatorPort);
        sb.append("]");

        return sb.toString();
    }

}
