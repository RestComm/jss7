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
import java.util.Arrays;

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
	private static final String BACKUP_ADDRESS = "backupAddress";
	private static final String LONG_MESSAGE_RULE = "longMessageRule";
	private static final String MTP3_SERVICE_ACCESS_POINT = "sap";

	private final TextBuilder persistFile = TextBuilder.newInstance();

	private static final XMLBinding binding = new XMLBinding();
	private static final String TAB_INDENT = "\t";
	private static final String CLASS_ATTRIBUTE = "type";

	private String persistDir = null;

	private final RuleComparator ruleComparator = new RuleComparator();
	// rule list
	private FastMap<Integer, Rule> rulesMap = new FastMap<Integer, Rule>();
	private FastMap<Integer, SccpAddress> primaryAddresses = new FastMap<Integer, SccpAddress>();
	private FastMap<Integer, SccpAddress> backupAddresses = new FastMap<Integer, SccpAddress>();
	private FastMap<Integer, LongMessageRule> longMessageRules = new FastMap<Integer, LongMessageRule>();
	private FastMap<Integer, Mtp3ServiceAccessPoint> saps = new FastMap<Integer, Mtp3ServiceAccessPoint>();

	private final String name;

	public Router(String name) {
		this.name = name;

		binding.setAlias(Rule.class, RULE);
		binding.setClassAttribute(CLASS_ATTRIBUTE);
	}

	public String getName() {
		return name;
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
			this.persistFile.append(persistDir).append(File.separator).append(this.name).append("_").append(PERSIST_FILE_NAME);
		} else {
			persistFile.append(System.getProperty(SCCP_ROUTER_PERSIST_DIR_KEY, System.getProperty(USER_DIR_KEY))).append(File.separator).append(this.name)
					.append("_").append(PERSIST_FILE_NAME);
		}

		logger.info(String.format("SCCP Router configuration file path %s", persistFile.toString()));

		try {
			this.load();
		} catch (FileNotFoundException e) {
			logger.warn(String.format("Failed to load the SS7 configuration file. \n%s", e.getMessage()));
		}

		logger.info("Started SCCP Router");
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
	public Rule findRule(SccpAddress calledParty) {

		for (FastMap.Entry<Integer, Rule> e = this.rulesMap.head(), end = this.rulesMap.tail(); (e = e.getNext()) != end;) {
			Rule rule = e.getValue();
			if (rule.matches(calledParty)) {
				return rule;
			}
		}
		return null;
	}

	public LongMessageRule findLongMessageRule(int dpc) {
		for (FastMap.Entry<Integer, LongMessageRule> e = this.longMessageRules.head(), end = this.longMessageRules.tail(); (e = e.getNext()) != end;) {
			LongMessageRule rule = e.getValue();
			if (rule.matches(dpc)) {
				return rule;
			}
		}
		return null;
	}

	public Mtp3ServiceAccessPoint findMtp3ServiceAccessPoint(int dpc, int sls) {
		for (FastMap.Entry<Integer, Mtp3ServiceAccessPoint> e = this.saps.head(), end = this.saps.tail(); (e = e.getNext()) != end;) {
			Mtp3ServiceAccessPoint sap = e.getValue();
			if (sap.matches(dpc, sls)) {
				return sap;
			}
		}
		return null;
	}

	public Rule getRule(int id) {
		return this.rulesMap.get(id);
	}

	public SccpAddress getPrimaryAddress(int id) {
		return this.primaryAddresses.get(id);
	}

	public SccpAddress getBackupAddress(int id) {
		return this.backupAddresses.get(id);
	}

	public LongMessageRule getLongMessageRule(int id) {
		return this.longMessageRules.get(id);
	}

	public Mtp3ServiceAccessPoint getMtp3ServiceAccessPoint(int id) {
		return this.saps.get(id);
	}

	public boolean spcIsLocal(int spc) {
		for (FastMap.Entry<Integer, Mtp3ServiceAccessPoint> e = this.saps.head(), end = this.saps.tail(); (e = e.getNext()) != end;) {
			Mtp3ServiceAccessPoint sap = e.getValue();
			if (sap.getOpc() == spc) {
				return true;
			}
		}
		return false;
	}

	public FastMap<Integer, Rule> getRules() {
		return rulesMap;
	}

	public FastMap<Integer, SccpAddress> getPrimaryAddresses() {
		return primaryAddresses;
	}

	public FastMap<Integer, SccpAddress> getBackupAddresses() {
		return backupAddresses;
	}

	public FastMap<Integer, LongMessageRule> getLongMessageRules() {
		return longMessageRules;
	}

	public FastMap<Integer, Mtp3ServiceAccessPoint> getMtp3ServiceAccessPoints() {
		return saps;
	}

	public void addRule(int id, Rule rule) {
		synchronized (this) {
			rule.setRuleId(id);
			Rule[] rulesArray = new Rule[(this.rulesMap.size() + 1)];
			int count = 0;

			for (FastMap.Entry<Integer, Rule> e = this.rulesMap.head(), end = this.rulesMap.tail(); (e = e.getNext()) != end;) {
				Integer ruleId = e.getKey();
				Rule ruleTemp = e.getValue();
				ruleTemp.setRuleId(ruleId);
				rulesArray[count++] = ruleTemp;
			}
			
			//add latest rule
			rulesArray[count++] = rule;

			// Sort
			Arrays.sort(rulesArray, ruleComparator);

			FastMap<Integer, Rule> newRule = new FastMap<Integer, Rule>();
			for (int i = 0; i < rulesArray.length; i++) {
				Rule ruleTemp = rulesArray[i]; 
				newRule.put(ruleTemp.getRuleId(), ruleTemp);
			}
			 this.rulesMap = newRule;
			this.store();
		}
	}

	public void removeRule(int id) {
		synchronized (this) {
			FastMap<Integer, Rule> newRule = new FastMap<Integer, Rule>();
			newRule.putAll(this.rulesMap);
			newRule.remove(id);
			this.rulesMap = newRule;
			this.store();
		}
	}

	public void addPrimaryAddress(int id, SccpAddress primaryAddress) {
		synchronized (this) {
			FastMap<Integer, SccpAddress> newPrimaryAddress = new FastMap<Integer, SccpAddress>();
			newPrimaryAddress.putAll(this.primaryAddresses);
			newPrimaryAddress.put(id, primaryAddress);
			this.primaryAddresses = newPrimaryAddress;
			this.store();
		}
	}

	public void removePrimaryAddress(int id) {
		synchronized (this) {
			FastMap<Integer, SccpAddress> newPrimaryAddress = new FastMap<Integer, SccpAddress>();
			newPrimaryAddress.putAll(this.primaryAddresses);
			newPrimaryAddress.remove(id);
			this.primaryAddresses = newPrimaryAddress;
			this.store();
		}
	}

	public void addBackupAddress(int id, SccpAddress backupAddress) {
		synchronized (this) {
			FastMap<Integer, SccpAddress> newBackupAddress = new FastMap<Integer, SccpAddress>();
			newBackupAddress.putAll(this.backupAddresses);
			newBackupAddress.put(id, backupAddress);
			this.backupAddresses = newBackupAddress;
			this.store();
		}
	}

	public void removeBackupAddress(int id) {
		synchronized (this) {
			FastMap<Integer, SccpAddress> newBackupAddress = new FastMap<Integer, SccpAddress>();
			newBackupAddress.putAll(this.backupAddresses);
			newBackupAddress.remove(id);
			this.backupAddresses = newBackupAddress;
			this.store();
		}
	}

	public void addLongMessageRule(int id, LongMessageRule longMessageRule) {
		synchronized (this) {
			FastMap<Integer, LongMessageRule> newLongMessageRule = new FastMap<Integer, LongMessageRule>();
			newLongMessageRule.putAll(this.longMessageRules);
			newLongMessageRule.put(id, longMessageRule);
			this.longMessageRules = newLongMessageRule;
			this.store();
		}
	}

	public void removeLongMessageRule(int id) {
		synchronized (this) {
			FastMap<Integer, LongMessageRule> newLongMessageRule = new FastMap<Integer, LongMessageRule>();
			newLongMessageRule.putAll(this.longMessageRules);
			newLongMessageRule.remove(id);
			this.longMessageRules = newLongMessageRule;
			this.store();
		}
	}

	public void addMtp3ServiceAccessPoint(int id, Mtp3ServiceAccessPoint sap) {
		synchronized (this) {
			FastMap<Integer, Mtp3ServiceAccessPoint> newSap = new FastMap<Integer, Mtp3ServiceAccessPoint>();
			newSap.putAll(this.saps);
			newSap.put(id, sap);
			this.saps = newSap;
			this.store();
		}
	}

	public void removeMtp3ServiceAccessPoint(int id) {
		synchronized (this) {
			FastMap<Integer, Mtp3ServiceAccessPoint> newSap = new FastMap<Integer, Mtp3ServiceAccessPoint>();
			newSap.putAll(this.saps);
			newSap.remove(id);
			this.saps = newSap;
			this.store();
		}
	}

	public void addMtp3Destination(int sapId, int dpcId, Mtp3Destination dest) {
		synchronized (this) {
			Mtp3ServiceAccessPoint sap = this.getMtp3ServiceAccessPoint(sapId);
			if (sap != null) {
				sap.addMtp3Destination(dpcId, dest);
			}
			this.store();
		}
	}

	public void removeMtp3Destination(int sapId, int dpcId) {
		synchronized (this) {
			Mtp3ServiceAccessPoint sap = this.getMtp3ServiceAccessPoint(sapId);
			if (sap != null) {
				sap.removeMtp3Destination(dpcId);
			}
			this.store();
		}
	}

	public void removeAllResourses() {

		synchronized (this) {
			if (this.rulesMap.size() == 0 && this.primaryAddresses.size() == 0 && this.backupAddresses.size() == 0 && this.longMessageRules.size() == 0
					&& this.saps.size() == 0)
				// no resources allocated - nothing to do
				return;

			rulesMap = new FastMap<Integer, Rule>();
			primaryAddresses = new FastMap<Integer, SccpAddress>();
			backupAddresses = new FastMap<Integer, SccpAddress>();
			longMessageRules = new FastMap<Integer, LongMessageRule>();
			saps = new FastMap<Integer, Mtp3ServiceAccessPoint>();

			// We store the cleared state
			this.store();
		}
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
			writer.write(rulesMap, RULE, FastMap.class);
			writer.write(primaryAddresses, PRIMARY_ADDRESS, FastMap.class);
			writer.write(backupAddresses, BACKUP_ADDRESS, FastMap.class);

			writer.write(longMessageRules, LONG_MESSAGE_RULE, FastMap.class);
			writer.write(saps, MTP3_SERVICE_ACCESS_POINT, FastMap.class);

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
			rulesMap = reader.read(RULE, FastMap.class);
			primaryAddresses = reader.read(PRIMARY_ADDRESS, FastMap.class);
			backupAddresses = reader.read(BACKUP_ADDRESS, FastMap.class);

			longMessageRules = reader.read(LONG_MESSAGE_RULE, FastMap.class);
			saps = reader.read(MTP3_SERVICE_ACCESS_POINT, FastMap.class);
		} catch (XMLStreamException ex) {
			// this.logger.info(
			// "Error while re-creating Linksets from persisted file", ex);
		}
	}
}
