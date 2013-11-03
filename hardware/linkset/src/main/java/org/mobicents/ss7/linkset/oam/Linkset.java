/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.ss7.linkset.oam;

import javolution.util.FastMap;
import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.scheduler.Scheduler;

/**
 * Instance of this class represents the logical group of links between two SP
 *
 * @author amit bhayani
 */
public abstract class Linkset implements XMLSerializable {

    // Name of link can be max 10 characters
    protected static final int NAME_SIZE = 10;

    protected static final String LINKSET_NAME = "name";
    protected static final String LINKSET_STATE = "state";
    protected static final String LINKSET_MODE = "mode";
    protected static final String LINKSET_OPC = "opc";
    protected static final String LINKSET_APC = "apc";
    protected static final String LINKSET_NI = "ni";
    protected static final String LINKS = "links";
    protected static final String LINK = "link";

    protected String linksetName = null;
    protected int apc;
    protected int opc;
    protected int ni = 2;

    protected int state = LinksetState.UNAVAILABLE;
    protected int mode = LinksetMode.UNCONFIGURED;

    protected LinksetSelectorKey selectorKey = null;

    protected LinksetStream linksetStream = null;

    // Hold Links here. Link name as key and actual Link as Object
    protected FastMap<String, Link> links = new FastMap<String, Link>();
    protected FastMap<String, Link> loadedLinks = new FastMap<String, Link>();

    protected Scheduler scheduler;

    public Linkset() {
        this.initialize();
    }

    public Linkset(String linksetName, int opc, int apc, int ni) {
        this();
        this.linksetName = linksetName;
        this.opc = opc;
        this.apc = apc;
        this.ni = ni;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;

        FastMap.Entry<String, Link> e = this.links.head();
        FastMap.Entry<String, Link> end = this.links.tail();
        for (; (e = e.getNext()) != end;) {
            Link link = e.getValue();
            link.setScheduler(scheduler);
        }

        e = this.loadedLinks.head();
        end = this.loadedLinks.tail();
        for (; (e = e.getNext()) != end;) {
            Link link = e.getValue();
            link.setScheduler(scheduler);
        }
    }

    /**
     * Initialize this linkset after creating a new instance
     */
    protected abstract void initialize();

    /**
     * Configure this linkset
     *
     * @throws Exception
     */
    protected abstract void configure() throws Exception;

    /**
     * Get handle to underlying stream
     *
     * @return
     */
    public LinksetStream getLinksetStream() {
        return this.linksetStream;
    }

    /**
     * Get adjacent point code
     *
     * @return
     */
    public int getApc() {
        return apc;
    }

    /**
     * Set adjacent point code
     *
     * @param dpc
     */
    public void setApc(int dpc) {
        this.apc = dpc;
    }

    /**
     * Get originating point code
     *
     * @return
     */
    public int getOpc() {
        return opc;
    }

    /**
     * Set originating point code
     *
     * @param opc
     */
    public void setOpc(int opc) {
        this.opc = opc;
    }

    /**
     * Get network-indicator
     *
     * @return
     */
    public int getNi() {
        return ni;
    }

    /**
     * Set network-indicator
     *
     * @param ni
     */
    public void setNi(int ni) {
        this.ni = ni;
    }

    /**
     * Get linkset name
     *
     * @return
     */
    public String getName() {
        return linksetName;
    }

    /**
     * Get linkset state
     *
     * @return
     */
    public int getState() {
        return state;
    }

    /**
     * Set linkset state
     *
     * @param state
     */
    public void setState(int state) {
        this.state = state;
    }

    /**
     * Get linkset Mode
     *
     * @return
     */
    public int getMode() {
        return mode;
    }

    /**
     * Get the map of link name vs {@link Link}
     *
     * @return
     */
    public FastMap<String, Link> getLinks() {
        return links;
    }

    /**
     * Set links
     *
     * @param links
     */
    public void setLinks(FastMap<String, Link> links) {
        this.links = links;
    }

    /**
     * Get the link corresponding to passed linkName. If no link corresponding to this linkname is present null is returned
     *
     * @param linkName
     * @return
     */
    public Link getLink(String linkName) {
        return this.links.get(linkName);
    }

    /**
     * Operations
     */

    /**
     * Create new {@link Link}
     */
    public abstract void createLink(String[] options) throws Exception;

    /**
     * Delete existing {@link Link}
     *
     * @param linkName
     * @throws Exception
     */
    public abstract void deleteLink(String linkName) throws Exception;

    /**
     * Activate this linkset
     *
     * @throws Exception
     */
    public abstract void activate() throws Exception;

    /**
     * Deactivate this linkset
     *
     * @throws Exception
     */
    public abstract void deactivate() throws Exception;

    /**
     * Activate link
     *
     * @param linkName
     * @throws Exception
     */
    public abstract void activateLink(String linkName) throws Exception;

    public void activateLinks() {
        this.links.putAll(this.loadedLinks);
    }

    /**
     * deactivate link
     *
     * @param linkName
     * @throws Exception
     */
    public abstract void deactivateLink(String linkName) throws Exception;

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<Linkset> LINKSET_XML = new XMLFormat<Linkset>(Linkset.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, Linkset linkSet) throws XMLStreamException {
            linkSet.linksetName = xml.getAttribute(LINKSET_NAME).toString();
            linkSet.state = xml.getAttribute(LINKSET_STATE, LinksetState.UNAVAILABLE);
            linkSet.mode = xml.getAttribute(LINKSET_MODE, LinksetMode.UNCONFIGURED);
            linkSet.opc = xml.getAttribute(LINKSET_OPC, -1);
            linkSet.apc = xml.getAttribute(LINKSET_APC, -1);
            linkSet.ni = xml.getAttribute(LINKSET_NI, 2);
            int linksCount = xml.getAttribute(LINKS, 0);

            for (int i = 0; i < linksCount; i++) {
                Link link = xml.get(LINK);
                link.setLinkSet(linkSet);
                link.setScheduler(linkSet.getScheduler());
                linkSet.loadedLinks.put(link.getName(), link);
            }
        }

        @Override
        public void write(Linkset linkSet, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            xml.setAttribute(LINKSET_NAME, linkSet.linksetName);
            xml.setAttribute(LINKSET_STATE, LinksetState.UNAVAILABLE);
            xml.setAttribute(LINKSET_MODE, linkSet.mode);
            xml.setAttribute(LINKSET_OPC, linkSet.opc);
            xml.setAttribute(LINKSET_APC, linkSet.apc);
            xml.setAttribute(LINKSET_NI, linkSet.ni);
            xml.setAttribute(LINKS, linkSet.links.size());

            for (FastMap.Entry<String, Link> e = linkSet.getLinks().head(), end = linkSet.getLinks().tail(); (e = e.getNext()) != end;) {
                Link value = e.getValue();
                xml.add(value, LINK);
            }

        }
    };

    /**
     * Add the details of this linkset in passed {@link StringBuffer}. This is for printing the linkset state.
     *
     * @param sb Add the information in this buffer
     * @param leftPad number of spaces from left side
     * @param descPad number of spaces between each word
     */
    public abstract void print(StringBuffer sb, int leftPad, int descPad);

}