/*
 * Mobicents, Communications Middleware
 * 
 * Copyright (c) 2008, Red Hat Middleware LLC or third-party
 * contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Middleware LLC.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 *
 * Boston, MA  02110-1301  USA
 */
package org.mobicents.protocols.ss7.mtp;

import java.io.IOException;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Represent the entire Message Transfer Part
 * 
 * @author kulikov
 */
public class MTP {

	/** The name of the link set */
	private String name;

	/** Originated point code */
	private int opc;

	/** Destination point code */
	private int dpc;

	private SelectorFactory selectorFactory;
	/** physical channels */
	private List<Mtp1> channels;

	/** MTP layer 3 */
	private Mtp3 mtp3;

	/** Represent MTP user part */
	private Mtp3Listener mtp3Listener;

	private Logger logger = Logger.getLogger(MTP.class);

	/**
	 * Gets the name of the linkset.
	 * 
	 * @return the name of the linkset
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the linkset.
	 * 
	 * @param name
	 *            the alhanumeric name of the linkset.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Assigns originated point code
	 * 
	 * @param opc
	 *            the value of the originated point code in decimal format
	 */
	public void setOpc(int opc) {
		this.opc = opc;
	}

	/**
	 * Assigns destination point code.
	 * 
	 * @param dpc
	 *            the destination point code value in decimal format.
	 */
	public void setDpc(int dpc) {
		this.dpc = dpc;
	}

	/**
	 * @return the opc
	 */
	public int getOpc() {
		return opc;
	}

	/**
	 * @return the dpc
	 */
	public int getDpc() {
		return dpc;
	}

	/**
	 * Assigns signalling channels.
	 * 
	 * @param channels
	 *            the list of available physical channels.
	 */
	public void setChannels(List<Mtp1> channels) {
		this.channels = channels;
	}

	public void setSelectorFactory(SelectorFactory selectorFactory) {
		this.selectorFactory = selectorFactory;
	}

	public Mtp3 getMtp3() {
		return this.mtp3;
	}

	/**
	 * Activates link set.
	 */
	public void start() throws IOException {
		try {
			// create mtp layer 3 instance

			logger.info("Created MTP layer 3");

			List<Mtp2> linkset = Mtp2Factory.getInstance().createMtpLinkSet(channels, name);
			// assigning physical channel
			mtp3 = Mtp3Factory.getInstance().createMtp(linkset, name, opc, dpc, selectorFactory);
			logger.info("Point codes are configured");

			// set user part
			if (mtp3Listener != null) {
				mtp3.addMtp3Listener(mtp3Listener);
			}

			// starting layer 3
			mtp3.start();
		} catch (RuntimeException re) {
			re.printStackTrace();
			throw re;
		}
	}

	/**
	 * Deactivates link set.
	 */
	public void stop() throws IOException {
		mtp3.stop();
	}
}
