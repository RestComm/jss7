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

package org.mobicents.protocols.ss7.tools.traceparser;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class TraceReaderDriverPcap extends TraceReaderDriverBase implements TraceReaderDriver {

	public TraceReaderDriverPcap(ProcessControl processControl, String fileName) {
		super(processControl, fileName);
	}

	@Override
	public void startTraceFile() throws TraceReaderException {
		
		if (this.listeners.size() == 0)
			throw new TraceReaderException("TraceReaderListener list is empty");
		
		this.isStarted = true;
		
		FileInputStream fis = null;

		try {
			if( this.processControl.checkNeedInterrupt() )
				return;
			
			fis = new FileInputStream(fileName);

			// Global header
			// typedef struct pcap_hdr_s {
			// 		guint32 magic_number; /* magic number */
			// 		guint16 version_major; /* major version number */
			// 		guint16 version_minor; /* minor version number */
			// 		gint32 thiszone; /* GMT to local correction */
			// 		guint32 sigfigs; /* accuracy of timestamps */
			// 		guint32 snaplen; /* max length of captured packets, in octets */
			// 		guint32 network; /* data link type */
			// } pcap_hdr_t;
			
			byte[] globHeader = new byte[24];
			if (fis.read(globHeader) < 24)
				throw new Exception("Not enouph data for a global header");
			
			int recCnt = 0;
			while( fis.available() > 0 ) {
				if( this.processControl.checkNeedInterrupt() )
					return;

				// Packet Header 
				// typedef struct pcaprec_hdr_s {
				// 		guint32 ts_sec; /* timestamp seconds */
				// 		guint32 ts_usec; /* timestamp microseconds */
				// 		guint32 incl_len; /* number of octets of packet saved in file */
				// 		guint32 orig_len; /* actual length of packet */
				// } pcaprec_hdr_t;				
				byte[] packetHeader = new byte[16];
				if (fis.read(packetHeader) < 16)
					throw new Exception("Not enouph data for a packet header");
				int ts_sec = (packetHeader[0] & 0xFF) + (((int)packetHeader[1] & 0xFF) << 8) + (((int)packetHeader[2] & 0xFF) << 16) + (((int)packetHeader[3] & 0xFF) << 24);				
				int ts_usec = (packetHeader[4] & 0xFF) + (((int)packetHeader[5] & 0xFF) << 8) + (((int)packetHeader[6] & 0xFF) << 16) + (((int)packetHeader[7] & 0xFF) << 24);				
				int incl_len = (packetHeader[8] & 0xFF) + (((int)packetHeader[9] & 0xFF) << 8) + (((int)packetHeader[10] & 0xFF) << 16) + (((int)packetHeader[11] & 0xFF) << 24);				
				int orig_len = (packetHeader[12] & 0xFF) + (((int)packetHeader[13] & 0xFF) << 8) + (((int)packetHeader[14] & 0xFF) << 16) + (((int)packetHeader[15] & 0xFF) << 24);				
				
				byte[] data = new byte[incl_len];
				if (fis.read(data) < incl_len)
					throw new Exception("Not enouph data for a packet data");
				recCnt++;

				this.parsePacket(data);
			}
			
		} catch (Throwable e) {
			this.loger.error("General exception: " + e.getMessage());
			e.printStackTrace();
			throw new TraceReaderException("General exception: " + e.getMessage(), e);
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void parsePacket(byte[] data) throws TraceReaderException {
		
		int startPos = 74;
		
		byte[] bufMsg = new byte[data.length - startPos + 3];
		bufMsg[0] = 0;
		bufMsg[1] = 0;
		bufMsg[2] = 63;
		System.arraycopy(data, startPos, bufMsg, 3, data.length - startPos);
		
		for (TraceReaderListener ls : this.listeners) {
			ls.ss7Message(bufMsg);
		}		
	}
}
