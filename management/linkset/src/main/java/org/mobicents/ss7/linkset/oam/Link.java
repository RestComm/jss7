package org.mobicents.ss7.linkset.oam;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;

/**
 * The instance of this class represents the signaling link over which SS7
 * signaling takes place. A link is in {@link LinkState#UNAVAILABLE UNAVAILABLE}
 * state and mode is {@link LinkMode#UNCONFIGURED UNCONFIGURED} by default.
 * 
 * @author amit bhayani
 * 
 */
public abstract class Link implements XMLSerializable {
    
    //Name of link can be max 10 characters
    protected static final int NAME_SIZE = 10;

    protected String linkName = null;

    protected int state = LinkState.UNAVAILABLE;
    protected int mode = LinkMode.UNCONFIGURED;

    protected Linkset linkSet;

    /**
     * Define attributes of xml
     */
    protected static final String LINK_NAME = "name";
    protected static final String LINK_STATE = "state";
    protected static final String LINK_MODE = "mode";

    protected static final boolean TRUE = true;
    protected static final boolean FALSE = false;

    public Link() {
    }

    public Link(String linkName) {
        this.linkName = linkName;
    }

    /**
     * Configure this link.
     * 
     * @throws Exception
     */
    protected abstract void configure() throws Exception;

    /**
     * Get link name
     * 
     * @return
     */
    public String getName() {
        return linkName;
    }

    /**
     * Get link state
     * 
     * @return
     */
    public int getState() {
        return state;
    }

    /**
     * Set link state
     * 
     * @param state
     */
    public void setState(int state) {
        this.state = state;
    }

    /**
     * Get link mode
     * 
     * @return
     */
    public int getMode() {
        return mode;
    }

    /**
     * Set link mode
     * 
     * @param mode
     */
    public void setMode(int mode) {
        this.mode = mode;
    }

    /**
     * Get the {@link Linkset} that this link belongs to
     * 
     * @return
     */
    public Linkset getLinkSet() {
        return linkSet;
    }

    /**
     * Set the {@link Linkset}
     * 
     * @param linkSet
     */
    public void setLinkSet(Linkset linkSet) {
        this.linkSet = linkSet;
    }

    /**
     * Operation
     */

    /**
     * Deactivate this link. If its not {@link LinkState#AVAILABLE AVAILABLE},
     * exception is thrown.
     */
    public abstract void deactivate() throws Exception;

    /**
     * Activate this link. If its already {@link LinkState#AVAILABLE AVAILABLE}
     * exception is thrown
     * 
     * @throws Exception
     */
    public abstract void activate() throws Exception;

    /**
     * Serialization code
     */
    protected static final XMLFormat<Link> LINK_XML = new XMLFormat<Link>(
            Link.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, Link link)
                throws XMLStreamException {
            link.linkName = xml.getAttribute(LINK_NAME).toString();
            link.state = xml.getAttribute(LINK_STATE, LinkState.UNAVAILABLE);
            link.mode = xml.getAttribute(LINK_MODE, LinkMode.UNCONFIGURED);
        }

        @Override
        public void write(Link link, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            xml.setAttribute(LINK_NAME, link.linkName);
            xml.setAttribute(LINK_STATE, link.state);
            xml.setAttribute(LINK_MODE, link.mode);

        }
    };

    /**
     * Add the details of this link in passed {@link StringBuffer}. This is for
     * printing the link state.
     * 
     * @param sb
     *            Add the information in this buffer
     * @param leftPad
     *            number of spaces from left side
     * @param descPad
     *            number of spaces between each word
     */
    public abstract void print(StringBuffer sb, int leftPad, int descPad);

}
