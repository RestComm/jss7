package org.mobicents.ss7.hardware.dialogic.oam;

import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;
import org.mobicents.protocols.stream.api.SelectorKey;
import org.mobicents.protocols.stream.api.SelectorProvider;
import org.mobicents.protocols.stream.api.StreamSelector;
import org.mobicents.ss7.hardware.dialogic.InterProcessCommunicator;
import org.mobicents.ss7.linkset.oam.LinkOAMMessages;
import org.mobicents.ss7.linkset.oam.Linkset;
import org.mobicents.ss7.linkset.oam.LinksetMode;
import org.mobicents.ss7.linkset.oam.LinksetSelector;
import org.mobicents.ss7.linkset.oam.LinksetState;
import org.mobicents.ss7.linkset.oam.LinksetStream;

/**
 * <p>
 * Linkset for <tt>dialogic</tt> based hardware. <tt>dialogic</tt> boards
 * have MTP2 and MTP3 support on board.
 * </p>
 * 
 * @author amit bhayani
 * 
 */
public class DialogicLinkset extends Linkset {

    private static final Logger logger = Logger
            .getLogger(DialogicLinkset.class);

    private static final String SRC_MODULE = "srcMod";
    private static final String DEST_MODULE = "destMod";

    private InterProcessCommunicator ipc;

    private int sourceModule;
    private int destModule;

    public DialogicLinkset() {
        super();
    }

    public DialogicLinkset(String linksetName, int opc, int dpc, int ni,
            int srcMod, int dstMod) {
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
        this.linksetStream = new LinksetStreamImpl();
    }

    @Override
    protected void configure() throws Exception {
        if (this.mode == LinksetMode.CONFIGURED) {
            ipc = new InterProcessCommunicator(this.sourceModule,
                    this.destModule);
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

    protected static final XMLFormat<DialogicLinkset> DAHDI_LINK_XML = new XMLFormat<DialogicLinkset>(
            DialogicLinkset.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml,
                DialogicLinkset linkset) throws XMLStreamException {

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
        public void write(DialogicLinkset linkset,
                javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {

            LINKSET_XML.write(linkset, xml);

            xml.setAttribute(SRC_MODULE, linkset.sourceModule);
            xml.setAttribute(DEST_MODULE, linkset.destModule);
        }
    };

    private class LinksetStreamImpl extends LinksetStream {
        byte[] rxData = null;

        @Override
        public boolean poll(int arg0, int arg1) {
            try {
                if (ipc != null) {
                    rxData = ipc.receive();
                    return true;
                }
            } catch (IOException ex) {
                logger
                        .error(
                                "IO error while receiving data from InterProcessCommunicator",
                                ex);
            }
            return false;
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
            paramArrayOfByte = rxData;
            return paramArrayOfByte == null ? 0 : paramArrayOfByte.length;
        }

        public SelectorKey register(StreamSelector selector) throws IOException {
            return ((LinksetSelector) selector).register(this);
        }

        public int write(byte[] paramArrayOfByte) throws IOException {
            ipc.send(paramArrayOfByte);
            return paramArrayOfByte.length;
        }
    }

}
