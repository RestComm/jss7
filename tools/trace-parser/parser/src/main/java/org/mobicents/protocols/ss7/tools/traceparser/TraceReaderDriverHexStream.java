/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.protocols.ss7.tools.traceparser;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class TraceReaderDriverHexStream extends TraceReaderDriverBase implements TraceReaderDriver {

    public TraceReaderDriverHexStream(ProcessControl processControl, String fileName) {
        super(processControl, fileName);
    }

    @Override
    public void startTraceFile() throws TraceReaderException {

        if (this.listeners.size() == 0)
            throw new TraceReaderException("TraceReaderListener list is empty");

        this.isStarted = true;

        FileInputStream fis = null;
        DataInputStream in = null;

        try {
            if (this.processControl.checkNeedInterrupt())
                return;

            fis = new FileInputStream(fileName);
            in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String strLine;
            while ((strLine = br.readLine()) != null) {
                if (strLine.length() > 0) {
                    if ((strLine.length() % 2) != 0)
                        throw new TraceReaderException("Odd characters count in a string");
                    byte[] buf = this.hexToBytes(strLine);

                    byte[] bufMsg = new byte[buf.length + 8];
                    System.arraycopy(buf, 0, bufMsg, 8, buf.length);
                    bufMsg[0] = 0;
                    bufMsg[1] = 0;
                    bufMsg[2] = 63;
                    bufMsg[3] = 3;
                    bufMsg[4] = 0;
                    bufMsg[5] = 0;
                    bufMsg[6] = 0;
                    bufMsg[7] = 0;

                    for (TraceReaderListener ls : this.listeners) {
                        ls.ss7Message(bufMsg);
                    }
                }
            }

        } catch (Throwable e) {
            this.loger.error("General exception: " + e.getMessage());
            e.printStackTrace();
            throw new TraceReaderException("General exception: " + e.getMessage(), e);
        } finally {
            try {
                fis.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public byte[] hexToBytes(String hexString) {
        HexBinaryAdapter adapter = new HexBinaryAdapter();
        byte[] bytes = adapter.unmarshal(hexString);
        return bytes;
    }
}
