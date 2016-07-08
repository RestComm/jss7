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

package org.mobicents.protocols.ss7.tools.simulator.tests.lcs;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;


/**
 *
 * @author falonso@csc.com
 *
 */
public class TestMapLcsServerConfigurationData {

    protected static final String NETWORK_NODE_NUMBER_ADDRESS = "network_node_number_address";
    protected static final String NA_ESRD_ADDRESS = "na_esrd_address";

    private String networkNodeNumberAddress = "5555544444";
    private String naESRDAddress = "11114444";

    public String getNaESRDAddress() {
        return naESRDAddress;
    }

    public void setNaESRDAddress(String naESRDAddress) {
        this.naESRDAddress = naESRDAddress;
    }

    public void setNetworkNodeNumberAddress(String data) {
        this.networkNodeNumberAddress=data;
    }

    public String getNetworkNodeNumberAddress() {
        return networkNodeNumberAddress;
    }


    protected static final XMLFormat<TestMapLcsServerConfigurationData> XML = new XMLFormat<TestMapLcsServerConfigurationData>(
            TestMapLcsServerConfigurationData.class) {

        public void write(TestMapLcsServerConfigurationData srv, OutputElement xml) throws XMLStreamException {
            xml.setAttribute(NETWORK_NODE_NUMBER_ADDRESS, srv.networkNodeNumberAddress);
            xml.add(srv.naESRDAddress.toString(), NA_ESRD_ADDRESS, String.class);
        }

        public void read(InputElement xml, TestMapLcsServerConfigurationData srv) throws XMLStreamException {
            srv.networkNodeNumberAddress = xml.getAttribute(NETWORK_NODE_NUMBER_ADDRESS).toString();
            String naESRDAddress = (String) xml.get(NA_ESRD_ADDRESS, String.class);
            srv.naESRDAddress = naESRDAddress;
        }
    };

}
