package org.mobicents.ss7.hardware.dahdi.oam;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

import javolution.util.FastMap;
import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.mtp.Mtp3;
import org.mobicents.protocols.ss7.mtp.Mtp3Listener;
import org.mobicents.ss7.linkset.oam.Link;
import org.mobicents.ss7.linkset.oam.LinkMode;
import org.mobicents.ss7.linkset.oam.Linkset;
import org.mobicents.ss7.linkset.oam.LinksetMode;
import org.mobicents.ss7.linkset.oam.LinksetState;
import org.mobicents.ss7.linkset.oam.LinkOAMMessages;

/**
 * 
 * @author amit bhayani
 * 
 */
public class DahdiLinkset extends Linkset implements Mtp3Listener {

    private Mtp3 mtp3 = null;
    private ConcurrentLinkedQueue<byte[]> queue = new ConcurrentLinkedQueue();

    public DahdiLinkset() {
        super();
    }

    public DahdiLinkset(String linksetName, int opc, int dpc, int ni) {
        super(linksetName, opc, dpc, ni);
    }

    @Override
    protected void init() {

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
                try {
                    this.mtp3.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Operations
     */
    @Override
    public boolean addLink(String linkName, ByteBuffer byteBuffer) {

        if (links.containsKey(linkName)) {
            byteBuffer.put(LinkOAMMessages.LINK_ALREADY_EXIST);
            return FALSE;
        }

        DahdiLink link = new DahdiLink(linkName);
        link.setLinkSet(this);

        this.links.put(linkName, link);

        // Send back success message
        byteBuffer.put(LinkOAMMessages.LINK_SUCCESSFULLY_ADDED);
        return TRUE;

    }

    @Override
    public boolean noShutdown(ByteBuffer byteBuffer) {
        if (this.state == LinksetState.AVAILABLE) {
            byteBuffer.put(LinkOAMMessages.LINKSET_ALREADY_ACTIVE);
            return FALSE;
        }

        // Check if atleast one Link is in Configured mode
        for (FastMap.Entry<String, Link> e = this.links.head(), end = this.links
                .tail(); (e = e.getNext()) != end;) {
            Link value = e.getValue();
            if (value.getMode() == LinkMode.CONFIGURED) {
                this.mode = LinksetMode.CONFIGURED;
                break;
            }

        }

        // If at least 1 Link is configured, lets start Mtp3
        if (this.mode == LinksetMode.CONFIGURED) {
            this.init();
            return TRUE;
        }

        byteBuffer.put(LinkOAMMessages.LINKSET_NO_LINKS_CONFIGURED);
        return FALSE;

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

            linkSet.init();
        }

        @Override
        public void write(DahdiLinkset linkSet,
                javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            LINKSET_XML.write(linkSet, xml);
        }
    };

    /**
     * Stream implementation methods
     */

    @Override
    protected boolean poll(int operation, int timeout) {
        if (mtp3 != null) {
            mtp3.run();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int read(byte[] paramArrayOfByte) throws IOException {
        if (queue.isEmpty()) {
            return 0;
        }
        paramArrayOfByte = queue.poll();
        return paramArrayOfByte == null ? 0 : paramArrayOfByte.length;
    }

    @Override
    public int write(byte[] paramArrayOfByte) throws IOException {
        mtp3.send(paramArrayOfByte, paramArrayOfByte.length);
        return paramArrayOfByte.length;
    }

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

}
