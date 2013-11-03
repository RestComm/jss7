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

package org.mobicents.protocols.ss7.tools.simulator.level3;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CapConfigurationData {

    // protected static final String LOCAL_SSN = "localSsn";
    // protected static final String REMOTE_SSN = "remoteSsn";
    protected static final String REMOTE_ADDRESS_DIGITS = "remoteAddressDigits";

    // private int localSsn;
    // private int remoteSsn;
    private String remoteAddressDigits;

    // public int getLocalSsn() {
    // return localSsn;
    // }
    //
    // public void setLocalSsn(int localSsn) {
    // this.localSsn = localSsn;
    // }
    //
    // public int getRemoteSsn() {
    // return remoteSsn;
    // }
    //
    // public void setRemoteSsn(int remoteSsn) {
    // this.remoteSsn = remoteSsn;
    // }

    public String getRemoteAddressDigits() {
        return remoteAddressDigits;
    }

    public void setRemoteAddressDigits(String remoteAddressDigits) {
        this.remoteAddressDigits = remoteAddressDigits;
    }

    protected static final XMLFormat<CapConfigurationData> XML = new XMLFormat<CapConfigurationData>(CapConfigurationData.class) {

        public void write(CapConfigurationData cap, OutputElement xml) throws XMLStreamException {
            // xml.setAttribute(LOCAL_SSN, cap.localSsn);
            // xml.setAttribute(REMOTE_SSN, cap.remoteSsn);

            xml.add(cap.remoteAddressDigits, REMOTE_ADDRESS_DIGITS, String.class);
        }

        public void read(InputElement xml, CapConfigurationData cap) throws XMLStreamException {
            // cap.localSsn = xml.getAttribute(LOCAL_SSN).toInt();
            // cap.remoteSsn = xml.getAttribute(REMOTE_SSN).toInt();

            cap.remoteAddressDigits = (String) xml.get(REMOTE_ADDRESS_DIGITS, String.class);
        }
    };

}
