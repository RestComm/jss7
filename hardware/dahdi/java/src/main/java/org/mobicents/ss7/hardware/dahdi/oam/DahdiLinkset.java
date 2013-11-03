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

package org.mobicents.ss7.hardware.dahdi.oam;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

import javolution.util.FastMap;
import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.mtp.Mtp3;
import org.mobicents.protocols.ss7.mtp.Mtp3Listener;
import org.mobicents.protocols.ss7.scheduler.Scheduler;
import org.mobicents.protocols.stream.api.SelectorKey;
import org.mobicents.protocols.stream.api.SelectorProvider;
import org.mobicents.protocols.stream.api.StreamSelector;
import org.mobicents.ss7.linkset.oam.FormatterHelp;
import org.mobicents.ss7.linkset.oam.Link;
import org.mobicents.ss7.linkset.oam.LinkMode;
import org.mobicents.ss7.linkset.oam.LinkOAMMessages;
import org.mobicents.ss7.linkset.oam.LinkState;
import org.mobicents.ss7.linkset.oam.Linkset;
import org.mobicents.ss7.linkset.oam.LinksetMode;
import org.mobicents.ss7.linkset.oam.LinksetSelector;
import org.mobicents.ss7.linkset.oam.LinksetState;
import org.mobicents.ss7.linkset.oam.LinksetStream;

/**
 * <p>
 * Linkset for <tt>dahdi</tt> based hardware. <tt>dahdi</tt> boards usually have no MTP support and depends on external software
 * to provide MTP2/MTP3 support.
 * </p>
 * <p>
 * DahdiLinkset encapsulates the {@link Mtp3 Mtp3} and {@link Mtp2 Mtp2} protocols.
 * </p>
 * <p>
 * Well known <tt>dahdi</tt> based SS7 cards are <tt>Diguim</tt> and <tt>Sangoma</tt>
 * </p>
 *
 * @author amit bhayani
 *
 */
public class DahdiLinkset extends Linkset implements Mtp3Listener {

    private static final Logger logger = Logger.getLogger(DahdiLinkset.class);

    private Mtp3 mtp3 = null;
    private ConcurrentLinkedQueue<byte[]> queue = new ConcurrentLinkedQueue<byte[]>();

    public DahdiLinkset() {
        super();
    }

    public DahdiLinkset(String linksetName, int opc, int dpc, int ni) {
        super(linksetName, opc, dpc, ni);
    }

    @Override
    protected void initialize() {
        this.linksetStream = new LinksetStreamImpl();
    }

    @Override
    protected void configure() throws Exception {

        if (this.mode == LinksetMode.CONFIGURED) {

            if (this.mtp3 == null) {
                // TODO fix String to TextBuilder
                this.mtp3 = new Mtp3(this.linksetName, scheduler);
            }

            this.mtp3.clearLinks();

            try {
                this.mtp3.addMtp3Listener(this);
            } catch (Exception e) {
                // can not be another listener , this only
            }

            for (FastMap.Entry<String, Link> e = this.links.head(), end = this.links.tail(); (e = e.getNext()) != end;) {
                Link value = e.getValue();
                if (value.getMode() == LinkMode.CONFIGURED) {
                    this.mtp3.addLink(((DahdiLink) value).getMtp2());
                }
            }

            this.mtp3.setDpc(this.apc);
            this.mtp3.setOpc(this.opc);
            this.mtp3.setNetworkIndicator(this.ni);

            if (this.state != LinksetState.SHUTDOWN) {
                this.mtp3.start();
            }
        }
    }

    @Override
    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
        if (this.mtp3 != null)
            this.mtp3.setScheduler(scheduler);

        FastMap.Entry<String, Link> e = this.links.head();
        FastMap.Entry<String, Link> end = this.links.tail();
        for (; (e = e.getNext()) != end;) {
            Link link = e.getValue();
            link.setScheduler(scheduler);
        }

        e = this.loadedLinks.head();
        end = this.loadedLinks.tail();
        for (; (e = e.getNext()) != end;) {
            Link link = e.getValue();
            link.setScheduler(scheduler);
        }
    }

    /**
     * Operations
     */
    @Override
    public void createLink(String[] options) throws Exception {

        // the command looks like "linkset link create span 1 code 1 channel 1
        // linkset1 link1"
        if (options.length != 11) {
            throw new Exception(LinkOAMMessages.INVALID_COMMAND);
        }

        String option = options[3];

        if (option == null || option.compareTo("span") != 0) {
            throw new Exception(LinkOAMMessages.INVALID_COMMAND);
        }

        int span = Integer.parseInt(options[4]);

        option = options[5];
        if (option == null || option.compareTo("code") != 0) {
            throw new Exception(LinkOAMMessages.INVALID_COMMAND);
        }

        int code = Integer.parseInt(options[6]);

        option = options[7];
        if (option == null || option.compareTo("channel") != 0) {
            throw new Exception(LinkOAMMessages.INVALID_COMMAND);
        }

        int channel = Integer.parseInt(options[8]);

        option = options[10]; // Link name
        if (option == null) {
            throw new Exception(LinkOAMMessages.INVALID_COMMAND);
        }

        if (links.containsKey(option)) {
            throw new Exception(LinkOAMMessages.LINK_ALREADY_EXIST);
        }

        DahdiLink link = new DahdiLink(option, span, channel, code);
        link.setScheduler(scheduler);
        link.setLinkSet(this);

        this.links.put(option, link);

    }

    @Override
    public void deleteLink(String linkName) throws Exception {
        Link link = this.links.get(linkName);
        if (link == null) {
            throw new Exception(LinkOAMMessages.LINK_DOESNT_EXIST);
        }

        if (link.getState() == LinkState.AVAILABLE) {
            throw new Exception(LinkOAMMessages.CANT_DELETE_LINK);
        }

        this.links.remove(linkName);
    }

    @Override
    public void activate() throws Exception {
        if (this.state == LinksetState.AVAILABLE) {
            throw new Exception(LinkOAMMessages.LINKSET_ALREADY_ACTIVE);
        }

        // Check if atleast one Link is in Configured mode
        for (FastMap.Entry<String, Link> e = this.links.head(), end = this.links.tail(); (e = e.getNext()) != end;) {
            Link value = e.getValue();
            if (value.getMode() == LinkMode.CONFIGURED) {
                this.mode = LinksetMode.CONFIGURED;
                this.configure();
                this.state = LinksetState.UNAVAILABLE;
                return;
            }

        }

        // If at least 1 Link is not configured
        throw new Exception(LinkOAMMessages.LINKSET_NO_LINKS_CONFIGURED);
    }

    @Override
    public void deactivate() throws Exception {
        throw new Exception(LinkOAMMessages.NOT_IMPLEMENTED);
    }

    @Override
    public void activateLink(String linkName) throws Exception {
        Link link = this.links.get(linkName);

        if (link == null) {
            throw new Exception(LinkOAMMessages.LINK_DOESNT_EXIST);
        }

        link.activate();
    }

    @Override
    public void deactivateLink(String linkName) throws Exception {
        Link link = this.links.get(linkName);

        if (link == null) {
            throw new Exception(LinkOAMMessages.LINK_DOESNT_EXIST);
        }

        link.deactivate();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<DahdiLinkset> DAHDI_LINKSET_XML = new XMLFormat<DahdiLinkset>(DahdiLinkset.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, DahdiLinkset linkSet) throws XMLStreamException {
            LINKSET_XML.read(xml, linkSet);

            try {
                linkSet.configure();
            } catch (Exception e) {
                logger.error("Error while initializing dahdi linkset", e);
            }
        }

        @Override
        public void write(DahdiLinkset linkSet, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            LINKSET_XML.write(linkSet, xml);
        }
    };

    // TODO : We still working with Listener model. rather change MTP3 to give
    // back byte[] and caller calls .read(byte[] data) to read data
    /**
     * Mtp3Listener Methods
     */
    public void linkDown() {
        this.state = LinksetState.UNAVAILABLE;

    }

    public void linkUp() {
        queue.clear();
        this.state = LinksetState.AVAILABLE;
    }

    public void receive(byte[] msgBuff) {
        queue.offer(msgBuff);
    }

    private class LinksetStreamImpl extends LinksetStream {

        @Override
        public boolean poll(int arg0, int arg1) {
            if (mtp3 != null) {
                mtp3.run();
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String getName() {
            return linksetName;
        }

        public void close() {
            // TODO Auto-generated method stub

        }

        public SelectorProvider provider() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public int read(byte[] paramArrayOfByte) throws IOException {
            if (queue.isEmpty()) {
                return 0;
            }

            byte[] data = queue.poll();
            if (data != null) {
                System.arraycopy(data, 0, paramArrayOfByte, 0, data.length);
            }

            return paramArrayOfByte == null ? 0 : data.length;
        }

        public SelectorKey register(StreamSelector selector) throws IOException {
            return ((LinksetSelector) selector).register(this);
        }

        public int write(byte[] paramArrayOfByte) throws IOException {
            mtp3.send(paramArrayOfByte, paramArrayOfByte.length);
            return paramArrayOfByte.length;
        }

        public int read(ByteBuffer arg0) throws IOException {
            // TODO Auto-generated method stub
            return 0;
        }

        public int write(ByteBuffer arg0) throws IOException {
            // TODO Auto-generated method stub
            return 0;
        }
    }

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
        sb.append("dahdi");

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

        // add state
        sb.append(LINKSET_STATE).append(FormatterHelp.EQUAL_SIGN).append(FormatterHelp.getLinksetState(this.state));

        sb.append(FormatterHelp.NEW_LINE);

        for (FastMap.Entry<String, Link> e = this.links.head(), end = this.links.tail(); (e = e.getNext()) != end;) {
            Link link = e.getValue();
            link.print(sb, 4, 2);
            sb.append(FormatterHelp.NEW_LINE);
        }
    }
}
