package org.mobicents.ss7.hardware.dahdi.oam;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import javolution.util.FastMap;
import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.mtp.Mtp3;
import org.mobicents.protocols.ss7.mtp.Mtp3Listener;
import org.mobicents.protocols.stream.api.SelectorKey;
import org.mobicents.protocols.stream.api.SelectorProvider;
import org.mobicents.protocols.stream.api.StreamSelector;
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
 * Linkset for <tt>dahdi</tt> based hardware. <tt>dahdi</tt> boards usually
 * have no MTP support and depends on external software to provide MTP2/MTP3
 * support.
 * </p>
 * <p>
 * DahdiLinkset encapsulates the {@link Mtp3 Mtp3} and {@link Mtp2 Mtp2}
 * protocols.
 * </p>
 * <p>
 * Well known <tt>dahdi</tt> based SS7 cards are <tt>Diguim</tt> and
 * <tt>Sangoma</tt>
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
                this.mtp3 = new Mtp3(this.linksetName);
            }

            this.mtp3.clearLinks();
            this.mtp3.addMtp3Listener(this);

            for (FastMap.Entry<String, Link> e = this.links.head(), end = this.links
                    .tail(); (e = e.getNext()) != end;) {
                Link value = e.getValue();
                if (value.getMode() == LinkMode.CONFIGURED) {
                    this.mtp3.addLink(((DahdiLink) value).getMtp2());
                }
            }

            if (this.state != LinksetState.SHUTDOWN) {
                this.mtp3.start();
            }
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
        for (FastMap.Entry<String, Link> e = this.links.head(), end = this.links
                .tail(); (e = e.getNext()) != end;) {
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
    protected static final XMLFormat<DahdiLinkset> DAHDI_LINKSET_XML = new XMLFormat<DahdiLinkset>(
            DahdiLinkset.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml,
                DahdiLinkset linkSet) throws XMLStreamException {
            LINKSET_XML.read(xml, linkSet);

            try {
                linkSet.configure();
            } catch (Exception e) {
                logger.error("Error while initializing dahdi linkset", e);
            }
        }

        @Override
        public void write(DahdiLinkset linkSet,
                javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
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
            paramArrayOfByte = queue.poll();
            return paramArrayOfByte == null ? 0 : paramArrayOfByte.length;
        }

        public SelectorKey register(StreamSelector selector) throws IOException {
            return ((LinksetSelector) selector).register(this);
        }

        public int write(byte[] paramArrayOfByte) throws IOException {
            mtp3.send(paramArrayOfByte, paramArrayOfByte.length);
            return paramArrayOfByte.length;
        }
    }
}
