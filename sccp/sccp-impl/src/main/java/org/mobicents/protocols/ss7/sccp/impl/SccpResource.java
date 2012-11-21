/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012. 
 * and individual contributors
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

	private static final String REMOTE_SSN = "remoteSsns";
	private static final String REMOTE_SPC = "remoteSpcs";
	private static final String CONCERNED_SPC = "concernedSpcs";

	private final TextBuilder persistFile = TextBuilder.newInstance();

	private static final SccpResourceXMLBinding binding = new SccpResourceXMLBinding();
	private static final String TAB_INDENT = "\t";
	private static final String CLASS_ATTRIBUTE = "type";

	private String persistDir = null;

	private RemoteSubSystemMap<Integer, RemoteSubSystemImpl> remoteSsns = new RemoteSubSystemMap<Integer, RemoteSubSystemImpl>();
	private RemoteSignalingPointCodeMap<Integer, RemoteSignalingPointCodeImpl> remoteSpcs = new RemoteSignalingPointCodeMap<Integer, RemoteSignalingPointCodeImpl>();
	private ConcernedSignalingPointCodeMap<Integer, ConcernedSignalingPointCodeImpl> concernedSpcs = new ConcernedSignalingPointCodeMap<Integer, ConcernedSignalingPointCodeImpl>();
	
	private final String name;

	public SccpResource(String name) {
		this.name = name;
		binding.setClassAttribute(CLASS_ATTRIBUTE);
		binding.setAlias(RemoteSubSystemImpl.class, "remoteSubSystem");
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
			persistFile.append(System.getProperty(SCCP_RESOURCE_PERSIST_DIR_KEY, System.getProperty(USER_DIR_KEY)))
					.append(File.separator).append(this.name).append("_").append(PERSIST_FILE_NAME);
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

	public void addRemoteSsn(int remoteSsnid, RemoteSubSystemImpl remoteSsn) {
		synchronized (this) {
			RemoteSubSystemMap<Integer, RemoteSubSystemImpl> newRemoteSsns = new RemoteSubSystemMap<Integer, RemoteSubSystemImpl>();
			newRemoteSsns.putAll(this.remoteSsns);
			newRemoteSsns.put(remoteSsnid, remoteSsn);
			this.remoteSsns = newRemoteSsns;
			this.store();
		}
	}

	public void removeRemoteSsn(int remoteSsnid) {
		synchronized (this) {
			RemoteSubSystemMap<Integer, RemoteSubSystemImpl> newRemoteSsns = new RemoteSubSystemMap<Integer, RemoteSubSystemImpl>();
			newRemoteSsns.putAll(this.remoteSsns);
			newRemoteSsns.remove(remoteSsnid);
			this.remoteSsns = newRemoteSsns;
			this.store();
		}
	}

	public RemoteSubSystemImpl getRemoteSsn(int remoteSsnid) {
		return this.remoteSsns.get(remoteSsnid);
	}

	public RemoteSubSystemImpl getRemoteSsn(int spc, int remoteSsn) {

		for (FastMap.Entry<Integer, RemoteSubSystemImpl> e = this.remoteSsns.head(), end = this.remoteSsns.tail(); (e = e.getNext()) != end;) {
			RemoteSubSystemImpl remoteSubSystem = e.getValue();
			if (remoteSubSystem.getRemoteSpc() == spc && remoteSsn == remoteSubSystem.getRemoteSsn()) {
				return remoteSubSystem;
			}

		}
		return null;
	}

	public FastMap<Integer, RemoteSubSystemImpl> getRemoteSsns() {
		return remoteSsns;
	}

	public void addRemoteSpc(int remoteSpcId, RemoteSignalingPointCodeImpl remoteSpc) {
		synchronized (this) {
			RemoteSignalingPointCodeMap<Integer, RemoteSignalingPointCodeImpl> newRemoteSpcs = new RemoteSignalingPointCodeMap<Integer, RemoteSignalingPointCodeImpl>(); 
			newRemoteSpcs.putAll(this.remoteSpcs);
			newRemoteSpcs.put(remoteSpcId, remoteSpc);
			this.remoteSpcs = newRemoteSpcs; 
			this.store();
		}
	}

	public void removeRemoteSpc(int remoteSpcId) {
		synchronized (this) {
			RemoteSignalingPointCodeMap<Integer, RemoteSignalingPointCodeImpl> newRemoteSpcs = new RemoteSignalingPointCodeMap<Integer, RemoteSignalingPointCodeImpl>(); 
			newRemoteSpcs.putAll(this.remoteSpcs);
			newRemoteSpcs.remove(remoteSpcId);
			this.remoteSpcs = newRemoteSpcs; 
			this.store();
		}
	}

	public RemoteSignalingPointCodeImpl getRemoteSpc(int remoteSpcId) {
		return this.remoteSpcs.get(remoteSpcId);
	}

	public RemoteSignalingPointCodeImpl getRemoteSpcByPC(int remotePC) {
		for (FastMap.Entry<Integer, RemoteSignalingPointCodeImpl> e = this.remoteSpcs.head(), end = this.remoteSpcs.tail(); (e = e
				.getNext()) != end;) {
			RemoteSignalingPointCodeImpl remoteSubSystem = e.getValue();
			if (remoteSubSystem.getRemoteSpc() == remotePC ) {
				return remoteSubSystem;
			}

		}
		return null;
	}
	
	public FastMap<Integer, RemoteSignalingPointCodeImpl> getRemoteSpcs() {
		return remoteSpcs;
	}

	public void addConcernedSpc(int concernedSpcId, ConcernedSignalingPointCodeImpl concernedSpc) {
		synchronized (this) {
			ConcernedSignalingPointCodeMap<Integer, ConcernedSignalingPointCodeImpl> newConcernedSpcs = new ConcernedSignalingPointCodeMap<Integer, ConcernedSignalingPointCodeImpl>(); 
			newConcernedSpcs.putAll(this.concernedSpcs);
			newConcernedSpcs.put(concernedSpcId, concernedSpc);
			this.concernedSpcs = newConcernedSpcs; 
			this.store();
		}
	}

	public void removeConcernedSpc(int concernedSpcId) {
		synchronized (this) {
			ConcernedSignalingPointCodeMap<Integer, ConcernedSignalingPointCodeImpl> newConcernedSpcs = new ConcernedSignalingPointCodeMap<Integer, ConcernedSignalingPointCodeImpl>(); 
			newConcernedSpcs.putAll(this.concernedSpcs);
			newConcernedSpcs.remove(concernedSpcId);
			this.concernedSpcs = newConcernedSpcs; 
			this.store();
		}
	}

	public ConcernedSignalingPointCodeImpl getConcernedSpc(int concernedSpcId) {
		return this.concernedSpcs.get(concernedSpcId);
	}

	public ConcernedSignalingPointCodeImpl getConcernedSpcByPC(int remotePC) {
		for (FastMap.Entry<Integer, ConcernedSignalingPointCodeImpl> e = this.concernedSpcs.head(), end = this.concernedSpcs.tail(); (e = e.getNext()) != end;) {
			ConcernedSignalingPointCodeImpl concernedSubSystem = e.getValue();
			if (concernedSubSystem.getRemoteSpc() == remotePC) {
				return concernedSubSystem;
			}

		}
		return null;
	}
	
	public FastMap<Integer, ConcernedSignalingPointCodeImpl> getConcernedSpcs() {
		return concernedSpcs;
	}

	public void removeAllResourses() {

		synchronized (this) {
			if (this.remoteSsns.size() == 0 && this.remoteSpcs.size() == 0 && this.concernedSpcs.size() == 0)
				// no resources allocated - nothing to do
				return;

			remoteSsns = new RemoteSubSystemMap<Integer, RemoteSubSystemImpl>();
			remoteSpcs = new RemoteSignalingPointCodeMap<Integer, RemoteSignalingPointCodeImpl>();
			concernedSpcs = new ConcernedSignalingPointCodeMap<Integer, ConcernedSignalingPointCodeImpl>();

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
			writer.write(remoteSsns, REMOTE_SSN, RemoteSubSystemMap.class);
			writer.write(remoteSpcs, REMOTE_SPC, RemoteSignalingPointCodeMap.class);
			writer.write(concernedSpcs, CONCERNED_SPC, ConcernedSignalingPointCodeMap.class);

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
			remoteSsns = reader.read(REMOTE_SSN, RemoteSubSystemMap.class);
			remoteSpcs = reader.read(REMOTE_SPC, RemoteSignalingPointCodeMap.class);
			concernedSpcs = reader.read(CONCERNED_SPC, ConcernedSignalingPointCodeMap.class);
		} catch (XMLStreamException ex) {
			// this.logger.info(
			// "Error while re-creating Linksets from persisted file", ex);
		}
	}
}
