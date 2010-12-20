package org.mobicents.protocols.ss7.mtp.oam;

import java.nio.ByteBuffer;

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

	protected TextBuilder linkName = TextBuilder.newInstance();

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

	/**
	 * Pre defined messages
	 */
	public static final byte[] LINK_ALREADY_ACTIVE = "Link already active"
			.getBytes();
	public static final byte[] LINK_NOT_CONFIGURED = "Not all mandatory parameters are set"
			.getBytes();

	public Link() {
	}

	public Link(TextBuilder linkName) {
		for (int i = 0; i < linkName.length(); i++) {
			this.linkName.append(linkName.charAt(i));
		}

	}

	protected abstract void init();

	public TextBuilder getLinkName() {
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
	public abstract boolean noShutdown(ByteBuffer byteBuffer);

	protected static final XMLFormat<Link> LINK_XML = new XMLFormat<Link>(
			Link.class) {

		@Override
		public void read(javolution.xml.XMLFormat.InputElement xml, Link link)
				throws XMLStreamException {
			link.linkName.append(xml.getAttribute(LINK_NAME));
			link.state = xml.getAttribute(LINK_STATE, LinkState.UNAVAILABLE);
			link.mode = xml.getAttribute(LINK_MODE, LinkMode.UNCONFIGURED);

			link.init();
		}

		@Override
		public void write(Link link, javolution.xml.XMLFormat.OutputElement xml)
				throws XMLStreamException {
			xml.setAttribute(LINK_NAME, link.linkName.toString());
			xml.setAttribute(LINK_STATE, link.state);
			xml.setAttribute(LINK_MODE, link.mode);

		}
	};
}
