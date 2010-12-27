package org.mobicents.ss7.m3ua.oam;

import java.io.IOException;
import java.nio.ByteBuffer;

import javolution.text.TextBuilder;
import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.ss7.linkset.oam.LinkOAMMessages;
import org.mobicents.ss7.linkset.oam.Linkset;
import org.mobicents.ss7.linkset.oam.LinksetState;

/**
 * 
 * @author amit bhayani
 * 
 */
public class M3UALinkset extends Linkset {

    private static final String LINKSET_LOCAL_ADDRESS = "addr";
    private static final String LINKSET_LOCAL_PORT = "port";

    private TextBuilder localAddress = TextBuilder.newInstance();
    private int localPort;

    public M3UALinkset() {

    }

    public M3UALinkset(String linkSetName, String type) {
        super(linkSetName, type);
    }

    @Override
    protected void init() {
        // TODO Auto-generated method stub

    }

    public TextBuilder getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(TextBuilder localAddress) {
        for (int i = 0; i < localAddress.length(); i++) {
            this.localAddress.append(localAddress.charAt(i));
        }
    }

    public int getLocalPort() {
        return localPort;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    /**
     * Operations
     */

    @Override
    public boolean addLink(String linkName, ByteBuffer byteBuffer) {
        return FALSE;
    }

    @Override
    public boolean noShutdown(ByteBuffer byteBuffer) {
        if (this.state == LinksetState.AVAILABLE) {
            byteBuffer.put(LinkOAMMessages.LINKSET_ALREADY_ACTIVE);
            return FALSE;
        }

        // TODO : Add check that all parameters are set before starting the
        // Link. Else send error message
        return FALSE;
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<M3UALinkset> M3UA_LINKSET_XML = new XMLFormat<M3UALinkset>(
            M3UALinkset.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml,
                M3UALinkset linkSet) throws XMLStreamException {
            linkSet.localAddress
                    .append(xml.getAttribute(LINKSET_LOCAL_ADDRESS));
            linkSet.localPort = xml.getAttribute(LINKSET_LOCAL_PORT, 0);
            LINKSET_XML.read(xml, linkSet);
        }

        @Override
        public void write(M3UALinkset linkSet,
                javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            xml.setAttribute(LINKSET_LOCAL_ADDRESS, linkSet.localAddress
                    .toString());
            xml.setAttribute(LINKSET_LOCAL_PORT, linkSet.localPort);
            LINKSET_XML.write(linkSet, xml);
        }
    };

    /**
     * Stream implementation methods
     */

    @Override
    protected boolean poll(int operation, int timeout) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int read(byte[] paramArrayOfByte) throws IOException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int write(byte[] paramArrayOfByte) throws IOException {
        // TODO Auto-generated method stub
        return 0;
    }

}
