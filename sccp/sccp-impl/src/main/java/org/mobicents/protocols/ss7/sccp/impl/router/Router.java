/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.sccp.impl.router;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javolution.text.TextBuilder;
import javolution.util.FastMap;
import javolution.xml.XMLBinding;
import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;
import javolution.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * <p>
 * The default implementation for the SCCP router.
 * </p>
 * 
 * <p>
 * The SCCP router allows to add/remove/list routing rules and implements
 * persistence for the routing rules.
 * </p>
 * <p>
 * RouterImpl when {@link #start() started} looks for file
 * <tt>sccprouter.xml</tt> containing serialized information of underlying
 * {@link Rule}. Set the directory path by calling
 * {@link #setPersistDir(String)} to direct RouterImpl to look at specified
 * directory for underlying serialized file.
 * </p>
 * <p>
 * If directory path is not set, RouterImpl searches for system property
 * <tt>sccprouter.persist.dir</tt> to get the path for directory
 * </p>
 * 
 * <p>
 * Even if <tt>sccprouter.persist.dir</tt> system property is not set,
 * RouterImpl will look at property <tt>user.dir</tt>
 * </p>
 * 
 * <p>
 * Implementation of SCCP routing mechanism makes routing decisions based on
 * rules. Each rule consists of three elements:
 * <ul>
 * <li>
 * <p>
 * The <i>pattern</i> determines pattern to which destination address is
 * compared. It has complex structure which looks as follows:
 * <ul>
 * <li>
 * <p>
 * <i>translation type</i> (tt) integer numer which is used in a network to
 * indicate the preferred method of global title analysis
 * </p>
 * </li>
 * <li>
 * <p>
 * <i>numbering plan</i> (np) integer value which inidcates which numbering plan
 * will be used for the global title. Its value aids the routing system in
 * determining the correct network system to route message to.
 * </p>
 * </li>
 * <li>
 * <p>
 * <i>nature of address</i> (noa) integer value which indicates address type.,
 * Specifically it indicates the scope of the address value, such as whether it
 * is an international number (i.e. including the country code), a "national" or
 * domestic number (i.e. without country code), and other formats such as
 * "local" format.
 * </p>
 * </li>
 * <li>
 * <p>
 * <i>digits</i> (digits) actual address
 * </p>
 * </li>
 * <li>
 * <p>
 * <i>sub-system number</i> (ssn) identifies application in SCCP routing
 * network.
 * </p>
 * </li>
 * </ul>
 * </p>
 * </li>
 * <li>
 * <p>
 * The <i>translation</i> determines target for messages which destination
 * address matches pattern. It has exactly the same structure as pattern .
 * </p>
 * </li>
 * <li>
 * <p>
 * The <i>mtpinfo</i> determines mtp layer information. If translation does not
 * indicate local address, this information is used to send message through MTP
 * layer. It has following structure:
 * <ul>
 * <li>
 * <p>
 * <i>name</i> (name) identifying one of link sets used by SCCP
 * </p>
 * </li>
 * <li>
 * <p>
 * <i>originating point code</i> (opc) local point code used as originating MTP
 * address
 * </p>
 * </li>
 * <li>
 * <p>
 * <i>adjacent point code</i> (apc) remote point code used as destination MTP
 * address
 * </p>
 * </li>
 * <li>
 * <p>
 * <i>signaling link selection</i> (sls) indentifies link in set
 * </p>
 * </li>
 * </ul>
 * </p>
 * </li>
 * </ul>
 * </p>
 * <p>
 * While the <i>pattern</i> is mandatory, <i>translation</i> and <i>mtpinfo</i>
 * is optional. Following combinations are possible
 * <ul>
 * <li>
 * <p>
 * <i>pattern</i> and <i>translation</i> : specifies local routing
 * </p>
 * </li>
 * <li>
 * <p>
 * <i>pattern</i> and <i>mtpinfo</i> : specifies remote routing using specified
 * mtp routing info and no translation needed
 * </p>
 * </li>
 * <li>
 * <p>
 * <i>pattern</i>, <i>translation</i> and <i>mtpinfo</i> specifies remote
 * routing using specified mtp routing info after applying specified translation
 * </p>
 * </li>
 * </ul>
 * </p>
 * 
 * @author amit bhayani
 * @author kulikov
 */
public class Router {
	private static final Logger logger = Logger.getLogger(Router.class);

	private static final String SCCP_ROUTER_PERSIST_DIR_KEY = "sccprouter.persist.dir";
	private static final String USER_DIR_KEY = "user.dir";
	private static final String PERSIST_FILE_NAME = "sccprouter.xml";

	private static final String RULE = "rule";
	private static final String PRIMARY_ADDRESS = "primaryAddress";
	private static final String SECONDARY_ADDRESS = "secondaryAddress";

	private final TextBuilder persistFile = TextBuilder.newInstance();

	private static final XMLBinding binding = new XMLBinding();
	private static final String TAB_INDENT = "\t";
	private static final String CLASS_ATTRIBUTE = "type";

	private String persistDir = null;

	// rule list
	private FastMap<Integer, Rule> rules = new FastMap<Integer, Rule>();

	private FastMap<Integer, SccpAddress> primaryAddresses = new FastMap<Integer, SccpAddress>();

	private FastMap<Integer, SccpAddress> secondaryAddresses = new FastMap<Integer, SccpAddress>();

	public Router() {

		binding.setAlias(Rule.class, RULE);
		binding.setClassAttribute(CLASS_ATTRIBUTE);
	}

	public String getPersistDir() {
		return persistDir;
	}

	public void setPersistDir(String persistDir) {
		this.persistDir = persistDir;
	}

	public void start() {
		this.persistFile.clear();

		if (persistDir != null) {
			this.persistFile.append(persistDir).append(File.separator).append(PERSIST_FILE_NAME);
		} else {
			persistFile.append(System.getProperty(SCCP_ROUTER_PERSIST_DIR_KEY, System.getProperty(USER_DIR_KEY)))
					.append(File.separator).append(PERSIST_FILE_NAME);
		}

		logger.info(String.format("SCCP Router configuration file path %s", persistFile.toString()));

		try {
			this.load();
		} catch (FileNotFoundException e) {
			logger.warn(String.format("Failed to load the SS7 configuration file. \n%s", e.getMessage()));
		}

		logger.info("Started LinksetManager");
	}

	public void stop() {
		this.store();
	}

	/**
	 * Looks up rule for translation.
	 * 
	 * @param calledParty
	 *            called party address
	 * @return the rule with match to the called party address
	 */
	public Rule find(SccpAddress calledParty) {

		for (FastMap.Entry<Integer, Rule> e = this.rules.head(), end = this.rules.tail(); (e = e.getNext()) != end;) {
			Rule rule = e.getValue();
			if (rule.matches(calledParty)) {
				return rule;
			}
		}
		return null;
	}

	public FastMap<Integer, Rule> getRules() {
		return rules;
	}

	public FastMap<Integer, SccpAddress> getPrimaryAddresses() {
		return primaryAddresses;
	}

	public FastMap<Integer, SccpAddress> getSecondaryAddresses() {
		return secondaryAddresses;
	}

	/**
	 * Persist
	 */
	public void store() {

		// TODO : Should we keep reference to Objects rather than recreating
		// everytime?
		try {
			XMLObjectWriter writer = XMLObjectWriter.newInstance(new FileOutputStream(persistFile.toString()));
			writer.setBinding(binding);
			// Enables cross-references.
			// writer.setReferenceResolver(new XMLReferenceResolver());
			writer.setIndentation(TAB_INDENT);
			writer.write(rules, RULE, FastMap.class);
			writer.write(primaryAddresses, PRIMARY_ADDRESS, FastMap.class);
			writer.write(secondaryAddresses, SECONDARY_ADDRESS, FastMap.class);

			writer.close();
		} catch (Exception e) {
			logger.error("Error while persisting the Rule state in file", e);
		}
	}

	/**
	 * Load and create LinkSets and Link from persisted file
	 * 
	 * @throws Exception
	 */
	public void load() throws FileNotFoundException {

		XMLObjectReader reader = null;
		try {
			reader = XMLObjectReader.newInstance(new FileInputStream(persistFile.toString()));

			reader.setBinding(binding);
			rules = reader.read(RULE, FastMap.class);
			primaryAddresses = reader.read(PRIMARY_ADDRESS, FastMap.class);
			secondaryAddresses = reader.read(SECONDARY_ADDRESS, FastMap.class);
		} catch (XMLStreamException ex) {
			// this.logger.info(
			// "Error while re-creating Linksets from persisted file", ex);
		}
	}
}
