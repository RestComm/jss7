package org.mobicents.ss7.linkset.oam;

import javolution.util.FastMap;
import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;

/**
 * 
 * @author amit bhayani
 */
public abstract class Linkset implements XMLSerializable {

    private static final String LINKSET_NAME = "name";
    private static final String LINKSET_STATE = "state";
    private static final String LINKSET_MODE = "mode";
    private static final String LINKSET_OPC = "opc";
    private static final String LINKSET_DPC = "dpc";
    private static final String LINKSET_NI = "ni";
    private static final String LINKS = "links";
    private static final String LINK = "link";

    protected static final boolean TRUE = true;
    protected static final boolean FALSE = false;

    protected String linksetName = null;
    protected int dpc;
    protected int opc;
    protected int ni = 2;

    protected int state = LinksetState.UNAVAILABLE;
    protected int mode = LinksetMode.UNCONFIGURED;

    protected LinksetSelectorKey selectorKey = null;

    protected LinksetStream linksetStream = null;

    // Hold Links here. Link name as key and actual Link as Object
    protected FastMap<String, Link> links = new FastMap<String, Link>();

    public Linkset() {
        this.initialize();
    }

    public Linkset(String linksetName, int opc, int dpc, int ni) {
        this();
        this.linksetName = linksetName;
        this.opc = opc;
        this.dpc = dpc;
        this.ni = ni;
    }

    protected abstract void initialize();

    // Configure the Linkset. Depends on Type of Link
    protected abstract void configure() throws Exception;

    public LinksetStream getLinksetStream() {
        return this.linksetStream;
    }

    public int getDpc() {
        return dpc;
    }

    public void setDpc(int dpc) {
        this.dpc = dpc;
    }

    public int getOpc() {
        return opc;
    }

    public void setOpc(int opc) {
        this.opc = opc;
    }

    public int getNi() {
        return ni;
    }

    public void setNi(int ni) {
        this.ni = ni;
    }

    public String getName() {
        return linksetName;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getMode() {
        return mode;
    }

    public FastMap<String, Link> getLinks() {
        return links;
    }

    public void setLinks(FastMap<String, Link> links) {
        this.links = links;
    }

    public Link getLink(String linkName) {
        return this.links.get(linkName);
    }

    /**
     * Operations
     */
    public abstract void createLink(String[] options) throws Exception;

    public abstract void deleteLink(String linkName) throws Exception;

    public abstract void activate() throws Exception;

    public abstract void deactivate() throws Exception;

    public abstract void activateLink(String linkName) throws Exception;

    public abstract void deactivateLink(String linkName) throws Exception;

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<Linkset> LINKSET_XML = new XMLFormat<Linkset>(
            Linkset.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml,
                Linkset linkSet) throws XMLStreamException {
            linkSet.linksetName = xml.getAttribute(LINKSET_NAME).toString();
            linkSet.state = xml.getAttribute(LINKSET_STATE,
                    LinksetState.UNAVAILABLE);
            linkSet.mode = xml.getAttribute(LINKSET_MODE,
                    LinksetMode.UNCONFIGURED);
            linkSet.opc = xml.getAttribute(LINKSET_OPC, -1);
            linkSet.dpc = xml.getAttribute(LINKSET_DPC, -1);
            linkSet.ni = xml.getAttribute(LINKSET_NI, 2);
            int linksCount = xml.getAttribute(LINKS, 0);

            for (int i = 0; i < linksCount; i++) {
                Link link = xml.get(LINK);
                link.setLinkSet(linkSet);
                linkSet.links.put(link.getName(), link);
            }
        }

        @Override
        public void write(Linkset linkSet,
                javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            xml.setAttribute(LINKSET_NAME, linkSet.linksetName);
            xml.setAttribute(LINKSET_STATE, linkSet.state);
            xml.setAttribute(LINKSET_MODE, linkSet.mode);
            xml.setAttribute(LINKSET_OPC, linkSet.opc);
            xml.setAttribute(LINKSET_DPC, linkSet.dpc);
            xml.setAttribute(LINKSET_NI, linkSet.ni);
            xml.setAttribute(LINKS, linkSet.links.size());

            for (FastMap.Entry<String, Link> e = linkSet.getLinks().head(), end = linkSet
                    .getLinks().tail(); (e = e.getNext()) != end;) {
                Link value = e.getValue();
                xml.add(value, LINK, value.getClass().getName());
            }

        }
    };
}
