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
package org.mobicents.ss7.hardware.dialogic;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPart;

/**
 * @author amit bhayani
 * 
 */
public class DialogicMtp3UserPart implements Mtp3UserPart {
	private static final Logger logger = Logger.getLogger(DialogicMtp3UserPart.class);

	private int sourceModuleId;
	private int destinationModuleId;

	private InterProcessCommunicator ipc = null;

	public DialogicMtp3UserPart() {

	}

	public int getSourceModuleId() {
		return sourceModuleId;
	}

	public void setSourceModuleId(int sourceModuleId) {
		this.sourceModuleId = sourceModuleId;
	}

	public int getDestinationModuleId() {
		return destinationModuleId;
	}

	public void setDestinationModuleId(int destinationModuleId) {
		this.destinationModuleId = destinationModuleId;
	}

	public void start() {
		ipc = new InterProcessCommunicator(sourceModuleId, destinationModuleId);
	}

	public void stop() {

	}

	/**
	 * Mtp3UserPart methods
	 */

	@Override
	public int read(ByteBuffer b) throws IOException {
		return ipc.read(b);
	}

	@Override
	public int write(ByteBuffer b) throws IOException {
		return ipc.write(b);
	}

	@Override
	public void execute() throws IOException {
		// Dialogic doesn't care for this and read can be directly from native
		// code and write can be directly to native code

	}

}
