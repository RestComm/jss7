package org.mobicents.ss7.linkset.oam;

import javolution.text.TextBuilder;
import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;

/**
 * 
 * @author amit bhayani
 * 
 */
public abstract class Link implements XMLSerializable {
    
    public static final String SPACE = " ";

	protected String linkName = null;

	protected int state = LinkState.UNAVAILABLE;
	protected int mode = LinkMode.UNCONFIGURED;

	protected Linkset linkSet;

	/**
     * Define attributes of xml
     */
	private static final String LINK_NAME = "name";
	private static final String LINK_STATE = "state";
	private static final String LINK_MODE = "mode";
	
	protected static final boolean TRUE = true;
	protected static final boolean FALSE = false;

	public Link() {
	}

	public Link(String linkName) {
			this.linkName = linkName;
	}

	protected abstract void init() throws Exception;

	public String getName() {
		return linkName;
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

	public void setMode(int mode) {
		this.mode = mode;
	}

	public Linkset getLinkSet() {
		return linkSet;
	}

	public void setLinkSet(Linkset linkSet) {
		this.linkSet = linkSet;
	}

	/**
     * Operation
     */
	public abstract void deactivate() throws Exception;
	
	public abstract void activate() throws Exception;

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
	
	public abstract void print(StringBuffer sb, int leftPad, int descPad);
	
    protected void createPad(StringBuffer sb, int pad) {
        for (int i = 0; i < pad; i++) {
            sb.append(' ');
        }
    }
}
