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
package org.mobicents.protocols.ss7.m3ua.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javolution.text.TextBuilder;
import javolution.util.FastList;
import javolution.xml.XMLBinding;
import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;
import javolution.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.M3UAProvider;
import org.mobicents.protocols.ss7.m3ua.impl.oam.M3UAOAMMessages;
import org.mobicents.protocols.ss7.m3ua.impl.scheduler.M3UAScheduler;
import org.mobicents.protocols.ss7.m3ua.impl.sctp.SctpProvider;

/**
 * @author amit bhayani
 * 
 */
public abstract class M3UAManagement {
	private static final Logger logger = Logger.getLogger(M3UAManagement.class);
	private static final String AS_LIST = "asList";
	private static final String ASP_FACTORY_LIST = "aspFactoryList";

	protected FastList<As> appServers = new FastList<As>();
	protected FastList<AspFactory> aspfactories = new FastList<AspFactory>();

	protected M3UAScheduler m3uaScheduler = new M3UAScheduler();
	protected final M3UAProvider m3uaProvider = SctpProvider.provider();

	protected static final String M3UA_PERSIST_DIR_KEY = "m3ua.persist.dir";
	protected static final String USER_DIR_KEY = "user.dir";
	protected String PERSIST_FILE_NAME;

	private final TextBuilder persistFile = TextBuilder.newInstance();

	protected static final XMLBinding binding = new XMLBinding();
	protected static final String TAB_INDENT = "\t";
	private static final String CLASS_ATTRIBUTE = "type";

	protected String persistDir = null;

	public M3UAManagement() {
		binding.setClassAttribute(CLASS_ATTRIBUTE);
	}

	public String getPersistDir() {
		return persistDir;
	}

	public void setPersistDir(String persistDir) {
		this.persistDir = persistDir;
	}

	public M3UAProvider getM3uaProvider() {
		return m3uaProvider;
	}

	public void start() throws IOException {
		this.persistFile.clear();

		if (persistDir != null) {
			this.persistFile.append(persistDir).append(File.separator).append(PERSIST_FILE_NAME);
		} else {
			persistFile.append(System.getProperty(M3UA_PERSIST_DIR_KEY, System.getProperty(USER_DIR_KEY)))
					.append(File.separator).append(PERSIST_FILE_NAME);
		}

		logger.info(String.format("M3UA configuration file path %s", persistFile.toString()));

		try {
			this.load();
		} catch (FileNotFoundException e) {
			logger.warn(String.format("Failed to load the SS7 configuration file. \n%s", e.getMessage()));
		}

		logger.info("Started M3UAManagement");
	}

	public void stop() throws IOException {
		this.store();
	}

	public FastList<As> getAppServers() {
		return appServers;
	}

	public FastList<AspFactory> getAspfactories() {
		return aspfactories;
	}

	public M3UAScheduler getM3uaScheduler() {
		return m3uaScheduler;
	}

	/**
	 * Create new {@link As}
	 * 
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public abstract As createAppServer(String args[]) throws Exception;

	/**
	 * Create new {@link AspFactory}
	 * 
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public abstract AspFactory createAspFactory(String[] args) throws Exception;

	/**
	 * Associate {@link Asp} to {@link As}
	 * 
	 * @param asName
	 * @param aspName
	 * @return
	 * @throws Exception
	 */
	public Asp assignAspToAs(String asName, String aspName) throws Exception {
		// check ASP and AS exist with given name
		As as = null;
		for (FastList.Node<As> n = appServers.head(), end = appServers.tail(); (n = n.getNext()) != end;) {
			if (n.getValue().getName().compareTo(asName) == 0) {
				as = n.getValue();
				break;
			}
		}

		if (as == null) {
			throw new Exception(String.format(M3UAOAMMessages.ADD_ASP_TO_AS_FAIL_NO_AS, asName));
		}

		AspFactory aspFactroy = null;
		for (FastList.Node<AspFactory> n = aspfactories.head(), end = aspfactories.tail(); (n = n.getNext()) != end;) {
			if (n.getValue().getName().compareTo(aspName) == 0) {
				aspFactroy = n.getValue();
				break;
			}
		}

		if (aspFactroy == null) {
			throw new Exception(String.format(M3UAOAMMessages.ADD_ASP_TO_AS_FAIL_NO_ASP, aspName));
		}

		Asp asp = aspFactroy.createAsp();
		m3uaScheduler.execute(asp.getFSM());
		as.addAppServerProcess(asp);

		this.store();

		return asp;
	}

	public abstract void startAsp(String aspName) throws Exception;

	public abstract void stopAsp(String aspName) throws Exception;

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
			writer.write(aspfactories, ASP_FACTORY_LIST, FastList.class);
			writer.write(appServers, AS_LIST, FastList.class);

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
			aspfactories = reader.read(ASP_FACTORY_LIST, FastList.class);
			appServers = reader.read(AS_LIST, FastList.class);

			for (FastList.Node<AspFactory> n = aspfactories.head(), end = aspfactories.tail(); (n = n.getNext()) != end;) {
				AspFactory factory = n.getValue();
				factory.setM3UAProvider(m3uaProvider);
			}

			// Create Asp's and assign to each of the AS
			for (FastList.Node<As> n = appServers.head(), end = appServers.tail(); (n = n.getNext()) != end;) {
				As as = n.getValue();
				as.setM3UAProvider(m3uaProvider);
				m3uaScheduler.execute(as.getFSM());

				// All the Asp's for this As added in temp list
				FastList<Asp> tempAsp = new FastList<Asp>();
				tempAsp.addAll(as.getAspList());

				// Claer Asp's from this As
				as.getAspList().clear();

				for (FastList.Node<Asp> n1 = tempAsp.head(), end1 = tempAsp.tail(); (n1 = n1.getNext()) != end1;) {
					Asp asp = n1.getValue();

					try {
						// Now let the Asp's be created from respective
						// AspFactory and added to As
						this.assignAspToAs(as.getName(), asp.getName());
					} catch (Exception e) {
						logger.error("Error while assigning Asp to As on loading from xml file", e);
					}
				}
			}

			// Start the ASP's if it was originally started
			for (FastList.Node<AspFactory> n = aspfactories.head(), end = aspfactories.tail(); (n = n.getNext()) != end;) {
				AspFactory factory = n.getValue();
				if (factory.getStatus()) {
					try {
						startAsp(factory.getName());
					} catch (Exception e) {
						logger.error(
								String.format("Error starting the AspFactory=%s while loading from XML",
										factory.getName()), e);
					}
				}
			}

		} catch (XMLStreamException ex) {
			// this.logger.info(
			// "Error while re-creating Linksets from persisted file", ex);
		}
	}
}
