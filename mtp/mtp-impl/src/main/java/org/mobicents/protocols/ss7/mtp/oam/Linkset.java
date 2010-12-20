package org.mobicents.protocols.ss7.mtp.oam;

import java.io.IOException;
import java.nio.ByteBuffer;

import javolution.text.TextBuilder;
import javolution.util.FastMap;
import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.stream.api.SelectorKey;
import org.mobicents.protocols.stream.api.SelectorProvider;
import org.mobicents.protocols.stream.api.Stream;
import org.mobicents.protocols.stream.api.StreamSelector;

/**
 * 
 * @author amit bhayani
 */
public abstract class Linkset implements XMLSerializable, Stream {

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

	private int type;

	protected TextBuilder linkSetName = TextBuilder.newInstance();
	protected int dpc;
	protected int opc;
	protected int ni = 2;

	protected int state = LinksetState.UNAVAILABLE;
	protected int mode = LinksetMode.UNCONFIGURED;

	protected LinksetSelectorKey selectorKey = null;

	// Hold Links here. Link name as key and actual Link as Object
	protected FastMap<TextBuilder, Link> links = new FastMap<TextBuilder, Link>();

	public Linkset() {

	}

	public Linkset(TextBuilder linkSetName, int type) {
		for (int i = 0; i < linkSetName.length(); i++) {
			this.linkSetName.append(linkSetName.charAt(i));
		}
		this.type = type;
	}

	// Initialize the Link. Depends on Type of Link
	protected abstract void init();

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

	public TextBuilder getLinkSetName() {
		return linkSetName;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public FastMap<TextBuilder, Link> getLinks() {
		return links;
	}

	public void setLinks(FastMap<TextBuilder, Link> links) {
		this.links = links;
	}
	
	public int getType() {
		return type;
	}

	/**
	 * Operations
	 */
	public abstract boolean addLink(TextBuilder linkName, ByteBuffer byteBuffer);

	public Link getLink(TextBuilder linkName) {
		return this.links.get(linkName);
	}

	public abstract boolean noShutdown(ByteBuffer byteBuffer);

	// Poll the LinkSets for readiness
	protected abstract boolean poll(int operation, int timeout);

	/**
	 * XML Serialization/Deserialization
	 */
	protected static final XMLFormat<Linkset> LINKSET_XML = new XMLFormat<Linkset>(
			Linkset.class) {

		@Override
		public void read(javolution.xml.XMLFormat.InputElement xml,
				Linkset linkSet) throws XMLStreamException {
			linkSet.linkSetName.append(xml.getAttribute(LINKSET_NAME));
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
				linkSet.links.put(link.getLinkName(), link);
			}
		}

		@Override
		public void write(Linkset linkSet,
				javolution.xml.XMLFormat.OutputElement xml)
				throws XMLStreamException {
			xml.setAttribute(LINKSET_NAME, linkSet.linkSetName.toString());
			xml.setAttribute(LINKSET_STATE, linkSet.state);
			xml.setAttribute(LINKSET_MODE, linkSet.mode);
			xml.setAttribute(LINKSET_OPC, linkSet.opc);
			xml.setAttribute(LINKSET_DPC, linkSet.dpc);
			xml.setAttribute(LINKSET_NI, linkSet.ni);
			xml.setAttribute(LINKS, linkSet.links.size());

			for (FastMap.Entry<TextBuilder, Link> e = linkSet.getLinks().head(), end = linkSet
					.getLinks().tail(); (e = e.getNext()) != end;) {
				Link value = e.getValue();
				xml.add(value, LINK, value.getClass().getName());
			}

		}
	};

	/**
	 * Stream Impl
	 */
	public SelectorKey register(StreamSelector selector) throws IOException {
		return ((LinksetSelector) selector).register(this);
	}
	
    public abstract int read(byte[] b) throws IOException;

    public abstract int write(byte[] d) throws IOException;

	/**
	 * Closes this streamer implementation. After closing stream its selectors
	 * are invalidated!
	 */
	public void close() {

	}

	/**
	 * Returns the provider that created this stream.
	 * 
	 * @return
	 */
	public SelectorProvider provider() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
