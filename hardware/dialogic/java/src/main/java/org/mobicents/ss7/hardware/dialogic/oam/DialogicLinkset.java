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

package org.mobicents.ss7.hardware.dialogic.oam;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;
import org.mobicents.ss7.hardware.dialogic.InterProcessCommunicator;
import org.mobicents.ss7.linkset.oam.FormatterHelp;
import org.mobicents.ss7.linkset.oam.LinkOAMMessages;
import org.mobicents.ss7.linkset.oam.Linkset;
import org.mobicents.ss7.linkset.oam.LinksetMode;
import org.mobicents.ss7.linkset.oam.LinksetState;

/**
 * <p>
 * Linkset for <tt>dialogic</tt> based hardware. <tt>dialogic</tt> boards have MTP2 and MTP3 support on board.
 * </p>
 *
 * @author amit bhayani
 *
 */
public class DialogicLinkset extends Linkset {

    private static final Logger logger = Logger.getLogger(DialogicLinkset.class);

    private static final String SRC_MODULE = "srcMod";
    private static final String DEST_MODULE = "destMod";

    private InterProcessCommunicator ipc;

    private int sourceModule;
    private int destModule;

    public DialogicLinkset() {
        super();
    }

    public DialogicLinkset(String linksetName, int opc, int dpc, int ni, int srcMod, int dstMod) {
        super(linksetName, opc, dpc, ni);
        this.sourceModule = srcMod;
        this.destModule = dstMod;
    }

    public int getSourceModule() {
        return sourceModule;
    }

    public void setSourceModule(int sourceModule) {
        this.sourceModule = sourceModule;
    }

    public int getDestModule() {
        return destModule;
    }

    public void setDestModule(int destModule) {
        this.destModule = destModule;
    }

    @Override
    protected void initialize() {
        // this.linksetStream = new LinksetStreamImpl();
    }

    @Override
    protected void configure() throws Exception {
        if (this.mode == LinksetMode.CONFIGURED) {
            ipc = new InterProcessCommunicator(this.sourceModule, this.destModule);
        }

    }

    @Override
    public void activate() throws Exception {
        if (this.state == LinksetState.AVAILABLE) {
            throw new Exception(LinkOAMMessages.LINKSET_ALREADY_ACTIVE);
        }

        this.mode = LinksetMode.CONFIGURED;
        this.configure();
    }

    @Override
    public void deactivate() throws Exception {
        throw new Exception(LinkOAMMessages.NOT_IMPLEMENTED);
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
        throw new Exception(LinkOAMMessages.NOT_IMPLEMENTED);
    }

    @Override
    public void deleteLink(String arg0) throws Exception {
        throw new Exception(LinkOAMMessages.NOT_IMPLEMENTED);
    }

    protected static final XMLFormat<DialogicLinkset> DAHDI_LINK_XML = new XMLFormat<DialogicLinkset>(DialogicLinkset.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, DialogicLinkset linkset) throws XMLStreamException {

            LINKSET_XML.read(xml, linkset);

            linkset.sourceModule = xml.getAttribute(SRC_MODULE, -1);
            linkset.destModule = xml.getAttribute(DEST_MODULE, -1);

            try {
                linkset.configure();
            } catch (Exception e) {
                logger.error("Failed to initialize dialogic card", e);
            }
        }

        @Override
        public void write(DialogicLinkset linkset, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {

            LINKSET_XML.write(linkset, xml);

            xml.setAttribute(SRC_MODULE, linkset.sourceModule);
            xml.setAttribute(DEST_MODULE, linkset.destModule);
        }
    };

    // private class LinksetStreamImpl extends LinksetStream {
    // ByteBuffer rxData = null;
    //
    // @Override
    // public boolean poll(int arg0, int arg1) {
    // rxData = null;
    // try {
    // if (ipc != null) {
    // rxData = ipc.read(null);
    // return true;
    // }
    // } catch (IOException ex) {
    // logger.error("IO error while receiving data from InterProcessCommunicator", ex);
    // }
    // return false;
    // }
    //
    // @Override
    // public String getName() {
    // return linksetName;
    // }
    //
    // public void close() {
    // // TODO Auto-generated method stub
    //
    // }
    //
    // public SelectorProvider provider() {
    // throw new UnsupportedOperationException("Not supported yet.");
    // }
    //
    // public int read(byte[] paramArrayOfByte) throws IOException {
    // if (rxData != null) {
    // System.arraycopy(rxData, 0, paramArrayOfByte, 0, rxData.length);
    // return rxData.length;
    // }
    //
    // return 0;
    // }
    //
    // public SelectorKey register(StreamSelector selector) throws IOException {
    // return ((LinksetSelector) selector).register(this);
    // }
    //
    // public int write(byte[] paramArrayOfByte) throws IOException {
    // ipc.send(paramArrayOfByte);
    // return paramArrayOfByte.length;
    // }
    //
    // @Override
    // public int read(ByteBuffer arg0) throws IOException {
    // // TODO Auto-generated method stub
    // return 0;
    // }
    //
    // @Override
    // public int write(ByteBuffer arg0) throws IOException {
    // // TODO Auto-generated method stub
    // return 0;
    // }
    // }

    @Override
    public void print(StringBuffer sb, int leftPad, int descPad) {

        // left pad
        FormatterHelp.createPad(sb, leftPad);

        // Add name
        sb.append(this.linksetName);

        // check if length is less than Link.NAME_SIZE, add padding
        if (this.linksetName.length() < Linkset.NAME_SIZE) {
            FormatterHelp.createPad(sb, Linkset.NAME_SIZE - this.linksetName.length());
        }

        // add desc padding
        FormatterHelp.createPad(sb, descPad);

        // type is dahdi
        sb.append("dialogic");

        // add desc padding
        FormatterHelp.createPad(sb, descPad);

        // add opc
        sb.append(LINKSET_OPC).append(FormatterHelp.EQUAL_SIGN).append(this.opc);

        // opc can be max 8 (ANSI is max 24bits) digits. Add padding if its not
        int length = (Integer.toString(this.opc).length());
        if (length < 8) {
            FormatterHelp.createPad(sb, 8 - length);
        }

        // add desc padding
        FormatterHelp.createPad(sb, descPad);

        // add apc
        sb.append(LINKSET_APC).append(FormatterHelp.EQUAL_SIGN).append(this.apc);

        // opc can be max 8 (ANSI is max 24bits) digits. Add padding if its not
        length = (Integer.toString(this.apc).length());
        if (length < 8) {
            FormatterHelp.createPad(sb, 8 - length);
        }

        // add desc padding
        FormatterHelp.createPad(sb, descPad);

        // add NI
        sb.append(LINKSET_NI).append(FormatterHelp.EQUAL_SIGN).append(this.ni);

        // add desc padding
        FormatterHelp.createPad(sb, descPad);

        // add Source Module
        sb.append(SRC_MODULE).append(FormatterHelp.EQUAL_SIGN).append(this.sourceModule);

        // add desc padding
        FormatterHelp.createPad(sb, descPad);

        // add dest Module
        sb.append(DEST_MODULE).append(FormatterHelp.EQUAL_SIGN).append(this.destModule);

        // add desc padding
        FormatterHelp.createPad(sb, descPad);

        // add state
        sb.append(LINKSET_STATE).append(FormatterHelp.EQUAL_SIGN).append(FormatterHelp.getLinksetState(this.state));

        sb.append(FormatterHelp.NEW_LINE);

    }

}
