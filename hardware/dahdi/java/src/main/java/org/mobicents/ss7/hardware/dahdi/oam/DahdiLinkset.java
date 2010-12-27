package org.mobicents.ss7.hardware.dahdi.oam;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

import javolution.util.FastMap;
import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.ss7.linkset.oam.LinkState;
import org.mobicents.protocols.ss7.mtp.Mtp3;
import org.mobicents.protocols.ss7.mtp.Mtp3Listener;
import org.mobicents.ss7.linkset.oam.Link;
import org.mobicents.ss7.linkset.oam.LinkMode;
import org.mobicents.ss7.linkset.oam.LinkOAMMessages;
import org.mobicents.ss7.linkset.oam.Linkset;
import org.mobicents.ss7.linkset.oam.LinksetMode;
import org.mobicents.ss7.linkset.oam.LinksetState;

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
        if(link == null){
            throw new Exception(LinkOAMMessages.LINK_DOESNT_EXIST);
        }
        
        if(link.getState() == LinkState.AVAILABLE){
            throw new Exception(LinkOAMMessages.CANT_DELETE_LINK);
        }
        
        this.links.remove(linkName);
    }

    @Override
    public boolean noShutdown(ByteBuffer byteBuffer) {
        if (this.state == LinksetState.AVAILABLE) {
            //byteBuffer.put(LinkOAMMessages.LINKSET_ALREADY_ACTIVE);
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

        //byteBuffer.put(LinkOAMMessages.LINKSET_NO_LINKS_CONFIGURED);
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
