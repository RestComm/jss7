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

package org.mobicents.protocols.ss7.sccp.impl;

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

/**
 * 
 * @author amit bhayani
 * 
 */
public class SccpResource {
	private static final Logger logger = Logger.getLogger(SccpResource.class);

	private static final String SCCP_RESOURCE_PERSIST_DIR_KEY = "sccpresource.persist.dir";
	private static final String USER_DIR_KEY = "user.dir";
	private static final String PERSIST_FILE_NAME = "sccpresource.xml";

	private static final String REMOTE_SSN = "remoteSsn";
	private static final String REMOTE_SPC = "remoteSpc";

	private final TextBuilder persistFile = TextBuilder.newInstance();

	private static final XMLBinding binding = new XMLBinding();
	private static final String TAB_INDENT = "\t";
	private static final String CLASS_ATTRIBUTE = "type";

	private String persistDir = null;

	private FastMap<Integer, RemoteSubSystem> remoteSsns = new FastMap<Integer, RemoteSubSystem>();

	private FastMap<Integer, RemoteSignalingPointCode> remoteSpcs = new FastMap<Integer, RemoteSignalingPointCode>();

	public SccpResource() {
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
			persistFile.append(System.getProperty(SCCP_RESOURCE_PERSIST_DIR_KEY, System.getProperty(USER_DIR_KEY)))
					.append(File.separator).append(PERSIST_FILE_NAME);
		}

		logger.info(String.format("SCCP Resource configuration file path %s", persistFile.toString()));

		try {
			this.load();
		} catch (FileNotFoundException e) {
			logger.warn(String.format("Failed to load the SS7 configuration file. \n%s", e.getMessage()));
		}

		logger.info("Started Sccp Resource");
	}

	public void stop() {
		this.store();
	}

	public void addRemoteSsn(int remoteSsnid, RemoteSubSystem remoteSsn) {
		this.remoteSsns.put(remoteSsnid, remoteSsn);
	}

	public RemoteSubSystem getRemoteSsn(int remoteSsnid) {
		return this.remoteSsns.get(remoteSsnid);
	}

	public RemoteSubSystem getRemoteSsn(int spc, int remoteSsn) {

		for (FastMap.Entry<Integer, RemoteSubSystem> e = this.remoteSsns.head(), end = this.remoteSsns.tail(); (e = e
				.getNext()) != end;) {
			RemoteSubSystem remoteSubSystem = e.getValue();
			if (remoteSubSystem.getRemoteSpc() == spc && remoteSsn == remoteSubSystem.getRemoteSsn()) {
				return remoteSubSystem;
			}

		}
		return null;
	}

	public FastMap<Integer, RemoteSubSystem> getRemoteSsns() {
		return remoteSsns;
	}

	public void addRemoteSpc(int remoteSpcId, RemoteSignalingPointCode remoteSpc) {
		this.remoteSpcs.put(remoteSpcId, remoteSpc);
	}

	public RemoteSignalingPointCode getRemoteSpc(int remoteSpcId) {
		return this.remoteSpcs.get(remoteSpcId);
	}

	public RemoteSignalingPointCode getRemoteSpcByPC(int remotePC) {
		for (FastMap.Entry<Integer, RemoteSignalingPointCode> e = this.remoteSpcs.head(), end = this.remoteSpcs.tail(); (e = e
				.getNext()) != end;) {
			RemoteSignalingPointCode remoteSubSystem = e.getValue();
			if (remoteSubSystem.getRemoteSpc() == remotePC ) {
				return remoteSubSystem;
			}

		}
		return null;
	}
	
	public FastMap<Integer, RemoteSignalingPointCode> getRemoteSpcs() {
		return remoteSpcs;
	}

	/**
	 * Persist
	 */
	private void store() {

		// TODO : Should we keep reference to Objects rather than recreating
		// everytime?
		try {
			XMLObjectWriter writer = XMLObjectWriter.newInstance(new FileOutputStream(persistFile.toString()));
			writer.setBinding(binding);
			// Enables cross-references.
			// writer.setReferenceResolver(new XMLReferenceResolver());
			writer.setIndentation(TAB_INDENT);
			writer.write(remoteSsns, REMOTE_SSN, FastMap.class);
			writer.write(remoteSpcs, REMOTE_SPC, FastMap.class);

			writer.close();
		} catch (Exception e) {
			this.logger.error("Error while persisting the Sccp Resource state in file", e);
		}
	}

	/**
	 * Load and create LinkSets and Link from persisted file
	 * 
	 * @throws Exception
	 */
	private void load() throws FileNotFoundException {

		XMLObjectReader reader = null;
		try {
			reader = XMLObjectReader.newInstance(new FileInputStream(persistFile.toString()));

			reader.setBinding(binding);
			remoteSsns = reader.read(REMOTE_SSN, FastMap.class);
			remoteSpcs = reader.read(REMOTE_SPC, FastMap.class);
		} catch (XMLStreamException ex) {
			// this.logger.info(
			// "Error while re-creating Linksets from persisted file", ex);
		}
	}
}
