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
            if (this.processControl.checkNeedInterrupt())
                return;

            fis = new FileInputStream(fileName);

            // Global header
            // typedef struct pcap_hdr_s {
            // guint32 magic_number; /* magic number */
            // guint16 version_major; /* major version number */
            // guint16 version_minor; /* minor version number */
            // gint32 thiszone; /* GMT to local correction */
            // guint32 sigfigs; /* accuracy of timestamps */
            // guint32 snaplen; /* max length of captured packets, in octets */
            // guint32 network; /* data link type */
            // } pcap_hdr_t;

            byte[] globHeader = new byte[24];
            if (fis.read(globHeader) < 24)
                throw new Exception("Not enouph data for a global header");

            int network = ((globHeader[20] & 0xFF) << 0) + ((globHeader[21] & 0xFF) << 8) + ((globHeader[22] & 0xFF) << 16) + ((globHeader[23] & 0xFF) << 24);

            int recCnt = 0;
            while (fis.available() > 0) {

                if (recCnt == 509) {
                    int gggg = 0;
                    gggg++;
                }

                if (this.processControl.checkNeedInterrupt())
                    return;

                // Packet Header
                // typedef struct pcaprec_hdr_s {
                // guint32 ts_sec; /* timestamp seconds */
                // guint32 ts_usec; /* timestamp microseconds */
                // guint32 incl_len; /* number of octets of packet saved in file */
                // guint32 orig_len; /* actual length of packet */
                // } pcaprec_hdr_t;
                byte[] packetHeader = new byte[16];
                if (fis.read(packetHeader) < 16)
                    throw new Exception("Not enouph data for a packet header");
                int ts_sec = (packetHeader[0] & 0xFF) + (((int) packetHeader[1] & 0xFF) << 8)
                        + (((int) packetHeader[2] & 0xFF) << 16) + (((int) packetHeader[3] & 0xFF) << 24);
                int ts_usec = (packetHeader[4] & 0xFF) + (((int) packetHeader[5] & 0xFF) << 8)
                        + (((int) packetHeader[6] & 0xFF) << 16) + (((int) packetHeader[7] & 0xFF) << 24);
                int incl_len = (packetHeader[8] & 0xFF) + (((int) packetHeader[9] & 0xFF) << 8)
                        + (((int) packetHeader[10] & 0xFF) << 16) + (((int) packetHeader[11] & 0xFF) << 24);
                int orig_len = (packetHeader[12] & 0xFF) + (((int) packetHeader[13] & 0xFF) << 8)
                        + (((int) packetHeader[14] & 0xFF) << 16) + (((int) packetHeader[15] & 0xFF) << 24);

                byte[] data = new byte[incl_len];
                if (fis.read(data) < incl_len)
                    throw new Exception("Not enouph data for a packet data");
                recCnt++;

                this.parsePacket(data, network);
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

    private void parsePacket(byte[] data, int network) throws TraceReaderException {

        switch (network) {
        case 1: // DLT_EN10MB
            // check the min possible length
            if (data == null || data.length < 34) {
                return;
            }

            // Ethernet II level
            if (data[12] != 8 || data[13] != 0) {
                // this is not IP protocol - return
                return;
            }

            byte[] ipData = new byte[data.length - 14];
            System.arraycopy(data, 14, ipData, 0, data.length - 14);
            this.parseIpV4Packet(ipData);
            break;

        case 113: // DLT_LINUX_SLL
            // check the min possible length
            if (data == null || data.length < 36) {
                return;
            }

            // Ethernet II level
            if (data[14] != 8 || data[15] != 0) {
                // this is not IP protocol - return
                return;
            }

            ipData = new byte[data.length - 16];
            System.arraycopy(data, 16, ipData, 0, data.length - 16);
            this.parseIpV4Packet(ipData);
            break;
        }
    }

    private void parseIpV4Packet(byte[] data) throws TraceReaderException {

        // IP protocol level
        int version = (data[0] & 0xF0) >> 4; // 14
        int ipHeaderLen = (data[0] & 0x0F) * 4;
        if (version != 4) {
            // TODO: add support for IP V6
            return;
        }
        int ipProtocolId = data[9] & 0xFF; // 23
        if (ipProtocolId != 132) { // 132 == SCTP protocol
            // TODO: add support for TCP protocol
            return;
        }
        int startSctpBlock = ipHeaderLen; // int startSctpBlock = 14 + ipHeaderLen

        // SCTP
        // skip SCTP header
        startSctpBlock += 12;

        while (true) {
            // check if else sctp block exists
            if (data.length < startSctpBlock + 4) // data.length < startSctpBlock + 4
                break;

            int blockType = data[startSctpBlock] & 0xFF;
            int blockLen = ((data[startSctpBlock + 2] & 0xFF) << 8) + (data[startSctpBlock + 3] & 0xFF);
            if (blockLen == 0)
                break;
            if (data.length < startSctpBlock + blockLen)
                break;

            if (blockType == 0 && blockLen > 16) {
                // for m3ua blockType==0
                byte[] bufM3ua = new byte[blockLen - 16];
                System.arraycopy(data, startSctpBlock + 16, bufM3ua, 0, blockLen - 16);
                this.parseM3uaPacket(bufM3ua);
            }

            int suff = blockLen % 4;
            if (suff > 0)
                blockLen += 4 - suff;
            startSctpBlock += blockLen;
        }
    }

    private void parseM3uaPacket(byte[] data) throws TraceReaderException {

        if (data.length < 8)
            return;

        int version = data[0] & 0xFF;
        int messageClass = data[2] & 0xFF;
        int messageType = data[3] & 0xFF;

        int msgLen = ((data[4] & 0xFF) << 24) + ((data[5] & 0xFF) << 16) + ((data[6] & 0xFF) << 8) + (data[7] & 0xFF);
        if (data.length < msgLen)
            return;

        if (messageClass == 1 && messageType == 1) { // parse only transfer message - payload data

            int pos = 8;
            long networkAppearance = -1;
            long routingContext = -1;
            long correlationId = -1;
            byte[] protocolData = null;
            while (true) {
                if (pos + 4 > msgLen)
                    break;
                int parLen = ((data[pos + 2] & 0xFF) << 8) + (data[pos + 3] & 0xFF);
                if (pos + parLen > msgLen)
                    break;

                if (data[pos] == 0x02 && data[pos + 1] == 0x00) {
                    // Network Appearance
                    networkAppearance = ((data[pos + 4] & 0xFF) << 24) + ((data[pos + 5] & 0xFF) << 16)
                            + ((data[pos + 6] & 0xFF) << 8) + (data[pos + 7] & 0xFF);
                }

                if (data[pos] == 0x00 && data[pos + 1] == 0x06) {
                    // Routing Context
                    routingContext = ((data[pos + 4] & 0xFF) << 24) + ((data[pos + 5] & 0xFF) << 16)
                            + ((data[pos + 6] & 0xFF) << 8) + (data[pos + 7] & 0xFF);
                }

                if (data[pos] == 0x02 && data[pos + 1] == 0x10) {
                    // Protocol Data
                    protocolData = new byte[parLen - 4];
                    System.arraycopy(data, pos + 4, protocolData, 0, parLen - 4);
                }

                if (data[pos] == 0x00 && data[pos + 1] == 0x13) {
                    // Correlation Id
                    correlationId = ((data[pos + 4] & 0xFF) << 24) + ((data[pos + 5] & 0xFF) << 16)
                            + ((data[pos + 6] & 0xFF) << 8) + (data[pos + 7] & 0xFF);
                }

                pos += parLen;
            }

            if (protocolData != null) {
                this.parseM3uaProtocolData(networkAppearance, routingContext, correlationId, protocolData);
            }
        } else if (messageClass == 6 && messageType == 1) {
            int len2 = ((data[18] & 0xFF) << 8) + data[19] & 0xFF;
            byte[] protocolData = new byte[len2 - 4 + 3];
            protocolData[2] = 63;
            System.arraycopy(data, 20, protocolData, 3, protocolData.length - 3);

            for (TraceReaderListener ls : this.listeners) {
                ls.ss7Message(protocolData);
            }
        }
    }

    private void parseM3uaProtocolData(long networkAppearance, long routingContext, long correlationId, byte[] data)
            throws TraceReaderException {

        if (data.length < 14) {
            return;
        }

        int opc = ((data[0] & 0xFF) << 24) + ((data[1] & 0xFF) << 16) + ((data[2] & 0xFF) << 8) + (data[3] & 0xFF);
        int dpc = ((data[4] & 0xFF) << 24) + ((data[5] & 0xFF) << 16) + ((data[6] & 0xFF) << 8) + (data[7] & 0xFF);
        int si = data[8] & 0xFF;
        int ni = data[9] & 0xFF;
        int mp = data[10] & 0xFF;
        int sls = data[11] & 0xFF;

        // int startPos = 74;

        byte[] bufMsg = new byte[data.length - 12 + 3 + 1 + 4];
        bufMsg[0] = 0;
        bufMsg[1] = 0;
        bufMsg[2] = 63; // li
        bufMsg[3] = (byte) ((ni << 6) + si); // sio

        // routing label
        // int dpc = ((b2 & 0x3f) << 8) | (b1 & 0xff);
        // int opc = ((b4 & 0x0f) << 10) | ((b3 & 0xff) << 2) | ((b2 & 0xc0) >> 6);
        // int sls = ((b4 & 0xf0) >> 4);
        bufMsg[4] = (byte) (dpc & 0xFF);
        bufMsg[5] = (byte) (((dpc & 0x3F00) >> 8) | (opc & 0x03) << 6);
        bufMsg[6] = (byte) ((opc & 0x03FC) >> 2);
        bufMsg[7] = (byte) (((opc & 0x3C00) >> 10) | (sls & 0x0F) << 4);

        System.arraycopy(data, 12, bufMsg, 8, data.length - 12);

        for (TraceReaderListener ls : this.listeners) {
            ls.ss7Message(bufMsg);
        }

        // byte[] bufMsg = new byte[data.length - 12 + 3];
        // bufMsg[0] = 0;
        // bufMsg[1] = 0;
        // bufMsg[2] = 63;
        // System.arraycopy(data, 12, bufMsg, 3, data.length - 12);
        //
        // for (TraceReaderListener ls : this.listeners) {
        // ls.ss7Message(bufMsg);
        // }
    }
}
