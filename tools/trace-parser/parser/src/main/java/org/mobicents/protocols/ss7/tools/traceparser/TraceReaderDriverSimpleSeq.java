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
public class TraceReaderDriverSimpleSeq extends TraceReaderDriverBase implements TraceReaderDriver {

	public TraceReaderDriverSimpleSeq(ProcessControl processControl, String fileName) {
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
			
			while( fis.available() > 0 ) {
				if( this.processControl.checkNeedInterrupt() )
					return;
				
				int b1 = fis.read();
				int b2 = fis.read();
				int length = b1 + (b2 << 8);
				
				byte[] bb = new byte[length];
				int rb = fis.read(bb);
				if (rb < length)
					throw new TraceReaderException("Not enouph data in the file for reading a message");

				byte[] bufMsg = new byte[length + 3];
				System.arraycopy(bb, 0, bufMsg, 3, length);
				bufMsg[0] = 0;
				bufMsg[1] = 0;
				bufMsg[2] = 63;
				
				for( TraceReaderListener ls : this.listeners ) {
					ls.ss7Message(bufMsg);
				}
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
}

