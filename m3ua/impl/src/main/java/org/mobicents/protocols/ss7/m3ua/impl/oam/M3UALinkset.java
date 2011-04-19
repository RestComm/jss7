/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.mobicents.protocols.ss7.m3ua.impl.oam;

import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.m3ua.impl.AsState;
import org.mobicents.protocols.ss7.m3ua.impl.as.AsImpl;
import org.mobicents.protocols.ss7.m3ua.message.transfer.PayloadData;
import org.mobicents.protocols.stream.api.SelectorKey;
import org.mobicents.protocols.stream.api.SelectorProvider;
import org.mobicents.protocols.stream.api.StreamSelector;
import org.mobicents.ss7.linkset.oam.LinkOAMMessages;
import org.mobicents.ss7.linkset.oam.Linkset;
import org.mobicents.ss7.linkset.oam.LinksetSelector;
import org.mobicents.ss7.linkset.oam.LinksetStream;

/**
 * 
 * @author amit bhayani
 * 
 */
public class M3UALinkset extends Linkset {

    private static final String AS_NAME = "asname";

    private String asName;
    private AsImpl asImpl;

    public M3UALinkset(String linksetName, int opc, int dpc, int ni, AsImpl asImpl) {
        super(linksetName, opc, dpc, ni);
        this.asImpl = asImpl;
        this.asName = asImpl.getName();
    }

    @Override
    protected void initialize() {
        this.linksetStream = new LinksetStreamImpl();
    }

    @Override
    protected void configure() {

    }

    public String getAspName() {
        return asName;
    }

    /**
     * Operations
     */
    @Override
    public void activate() throws Exception {
        throw new Exception(LinkOAMMessages.OPERATION_NOT_SUPPORTED);
    }

    @Override
    public void deactivate() throws Exception {
        throw new Exception(LinkOAMMessages.OPERATION_NOT_SUPPORTED);
    }

    @Override
    public void activateLink(String linkName) throws Exception {
        throw new Exception(LinkOAMMessages.OPERATION_NOT_SUPPORTED);
    }

    @Override
    public void deactivateLink(String linkName) throws Exception {
        throw new Exception(LinkOAMMessages.OPERATION_NOT_SUPPORTED);
    }

    @Override
    public void createLink(String[] arg0) throws Exception {
        throw new Exception(LinkOAMMessages.OPERATION_NOT_SUPPORTED);
    }

    @Override
    public void deleteLink(String arg0) throws Exception {
        throw new Exception(LinkOAMMessages.OPERATION_NOT_SUPPORTED);
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<M3UALinkset> M3UA_LINKSET_XML = new XMLFormat<M3UALinkset>(M3UALinkset.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, M3UALinkset linkSet) throws XMLStreamException {
            linkSet.asName = xml.getAttribute(AS_NAME).toString();
            LINKSET_XML.read(xml, linkSet);
        }

        @Override
        public void write(M3UALinkset linkSet, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            xml.setAttribute(AS_NAME, linkSet.asName.toString());
            LINKSET_XML.write(linkSet, xml);
        }
    };

    @Override
    public void print(StringBuffer arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    private class LinksetStreamImpl extends LinksetStream {

        @Override
        public String getName() {
            return linksetName;
        }

        @Override
        public boolean poll(int arg0, int arg1) {
            if (asImpl != null && asImpl.getState() == AsState.ACTIVE) {
                return true;
            }
            return false;
        }

        public void close() {
            // TODO Auto-generated method stub

        }

        public SelectorProvider provider() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public int read(byte[] paramArrayOfByte) throws IOException {
            PayloadData payload = asImpl.poll();
            if (payload != null) {
                byte[] data = payload.getData().getMsu();
                System.arraycopy(data, 0, paramArrayOfByte, 0, data.length);
                return paramArrayOfByte.length;
            }
            return 0;
        }

        public SelectorKey register(StreamSelector selector) throws IOException {
            return ((LinksetSelector) selector).register(this);
        }

        public int write(byte[] msu) throws IOException {
            asImpl.write(msu);
            return msu.length;
        }

    }
}
