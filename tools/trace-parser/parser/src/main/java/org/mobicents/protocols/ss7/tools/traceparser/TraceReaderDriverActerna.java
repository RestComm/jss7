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

import java.io.FileInputStream;
import java.io.IOException;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class TraceReaderDriverActerna extends TraceReaderDriverBase implements TraceReaderDriver {

    public TraceReaderDriverActerna(ProcessControl processControl, String fileName) {
        super(processControl, fileName);
    }

    public void startTraceFile() throws TraceReaderException {

        if (this.listeners.size() == 0)
            throw new TraceReaderException("TraceReaderListener list is empty");

        this.isStarted = true;

        FileInputStream fis = null;

        try {
            if (this.processControl.checkNeedInterrupt())
                return;

            fis = new FileInputStream(fileName);

            int ind = 0;
            int b = 0;
            int recCnt = 0;
            int step = 4;
            int stepPos = 0;
            int blockLen = 0;
            int sufLen = 0;
            int[] buf = new int[5];
            // byte[] bufTag;
            while (fis.available() > 0) {
                if (this.processControl.checkNeedInterrupt())
                    return;

                b = fis.read();

                switch (step) {
                    case 1:
                        buf[stepPos] = b;
                        if (stepPos == 16) {
                            blockLen = b;
                        }
                        if (stepPos == 15 && b != 0 || stepPos == 17 && b != 0 && b != 1) {
                            this.loger.error("Error #1, recCnt=" + recCnt);
                        }

                        if (stepPos == 17) {
                            blockLen += b * 256;

                            step = 2;
                            stepPos = 0;
                            buf = new int[blockLen];
                        } else
                            stepPos++;
                        break;

                    case 2:
                        buf[stepPos] = b;

                        if (stepPos == blockLen - 1) {

                            byte[] bufMsg = new byte[blockLen];
                            for (int i = 0; i < blockLen; i++) {
                                bufMsg[i] = (byte) buf[i];
                            }
                            for (TraceReaderListener ls : this.listeners) {
                                ls.ss7Message(bufMsg);
                            }

                            step = 3;
                            stepPos = 0;
                            buf = new int[2];
                        } else
                            stepPos++;
                        break;

                    case 3:
                        buf[stepPos] = b;

                        if (stepPos == 1) {

                            step = 4;
                            stepPos = 0;
                            buf = new int[5];
                        } else
                            stepPos++;
                        break;

                    case 4:
                        buf[stepPos] = b;

                        if (b == 2) {
                            int[] buf2 = new int[stepPos + 1];
                            for (int i1 = 0; i1 <= stepPos; i1++)
                                buf2[i1] = buf[i1];

                            recCnt++;
                            step = 1;
                            stepPos = 0;
                            buf = new int[18];
                        } else
                            stepPos++;
                        break;
                }

                ind++;
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
