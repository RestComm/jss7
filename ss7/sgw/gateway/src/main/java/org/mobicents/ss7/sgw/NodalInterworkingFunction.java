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

package org.mobicents.ss7.sgw;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.impl.sg.ServerM3UAProcess;
import org.mobicents.protocols.ss7.m3ua.message.transfer.PayloadData;
import org.mobicents.protocols.stream.api.SelectorKey;
import org.mobicents.ss7.linkset.oam.Layer4;
import org.mobicents.ss7.linkset.oam.Linkset;
import org.mobicents.ss7.linkset.oam.LinksetManager;
import org.mobicents.ss7.linkset.oam.LinksetSelector;
import org.mobicents.ss7.linkset.oam.LinksetStream;

/** */
public class NodalInterworkingFunction implements Layer4 {

	private static Logger logger = Logger.getLogger(NodalInterworkingFunction.class);

	private LinksetSelector linkSetSelector = new LinksetSelector();
	private LinksetStream linksetStream = null;

	private LinksetManager linksetManager = null;

	private ServerM3UAProcess serverM3UAProcess = null;

	private boolean started = false;

	private int OP_READ_WRITE = 3;

	private byte[] rxBuffer;

	private ConcurrentLinkedQueue<byte[]> mtpqueue = new ConcurrentLinkedQueue<byte[]>();
	private ConcurrentLinkedQueue<PayloadData> m3uaqueue = new ConcurrentLinkedQueue<PayloadData>();

	public NodalInterworkingFunction() {

	}

	public LinksetManager getLinksetManager() {
		return linksetManager;
	}

	public void setLinksetManager(LinksetManager linksetManager) {
		this.linksetManager = linksetManager;
	}


	public ServerM3UAProcess getServerM3UAProcess() {
		return serverM3UAProcess;
	}

	public void setServerM3UAProcess(ServerM3UAProcess serverM3UAProcess) {
		this.serverM3UAProcess = serverM3UAProcess;
	}

	// Layer4 methods
	public void add(Linkset linkset) {
		try {
			linksetStream = linkset.getLinksetStream();
			linksetStream.register(this.linkSetSelector);
		} catch (IOException ex) {
			logger.error(String.format("Registration for %s LinksetStream failed", linkset.getName()), ex);
		}
	}

	public void remove(Linkset arg0) {
		// TODO Auto-generated method stub

	}

	// Life cycle methods
	public void start() throws Exception {

		// Linkset
		this.linksetManager.setLayer4(this);

		// Add all linkset stream
		FastMap<String, Linkset> map = this.linksetManager.getLinksets();
		for (FastMap.Entry<String, Linkset> e = map.head(), end = map.tail(); (e = e.getNext()) != end;) {
			Linkset value = e.getValue();
			this.add(value);
		}

		this.started = true;

	}

	public void stop() throws Exception {
		this.started = false;
	}

	protected void perform() throws IOException {

		if (!started) {
			return;
		}
		FastList<SelectorKey> selected = linkSetSelector.selectNow(OP_READ_WRITE, 1);

		for (FastList.Node<SelectorKey> n = selected.head(), end = selected.tail(); (n = n.getNext()) != end;) {
			SelectorKey key = n.getValue();
			// ((LinksetStream) key.getStream()).read(rxBuffer);

			// Read data
			if (rxBuffer != null) {
				mtpqueue.offer(rxBuffer);
			}

			// write to stream
			if (!m3uaqueue.isEmpty()) {
				PayloadData txBuffer = m3uaqueue.poll();
				// TODO : Select appropriate LinkSet to send this data
				if (txBuffer != null) {
					// linksetStream.write(txBuffer.getData().getMsu());
				}
			}
		}// for

		// Sig Gateway will poll all its AspFactory for data
		this.serverM3UAProcess.execute();

		// TODO
		// PayloadData payload = null;
		// while ((payload = this.sgpImpl.poll()) != null) {
		// m3uaqueue.add(payload);
		// }
		//
		// byte[] msu = null;
		// while ((msu = mtpqueue.poll()) != null) {
		// this.sgpImpl.send(msu);
		// }
	}
}
