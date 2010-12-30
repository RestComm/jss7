package org.mobicents.ss7.m3ua.oam;

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

    @Override
    protected void initialize() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void configure() {

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
    public void activate() throws Exception {
        if (this.state == LinksetState.AVAILABLE) {
            throw new Exception(LinkOAMMessages.LINKSET_ALREADY_ACTIVE);
        }

        // TODO Start M3ua Linkset
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

    @Override
    public void print(StringBuffer arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
        
    }
}
