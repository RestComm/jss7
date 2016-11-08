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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.bind.DatatypeConverter;

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

            // 1 - LIB PCAP Global header
            // typedef struct pcap_hdr_s {
            // guint32 magic_number; /* magic number */
            // guint16 version_major; /* major version number */
            // guint16 version_minor; /* minor version number */
            // gint32 thiszone; /* GMT to local correction */
            // guint32 sigfigs; /* accuracy of timestamps */
            // guint32 snaplen; /* max length of captured packets, in octets */
            // guint32 network; /* data link type */
            // } pcap_hdr_t;

            // 2 - PCAP NG Section Header Block

            FileEncodingType fileEncodingType;
            LittleBigEndianFormat littleBigEndianFormat;
            byte[] fileSignature = new byte[4];
            if (fis.read(fileSignature) < 4)
                throw new Exception("Not enouph data for a file signature");
            byte[] libPcapSign = new byte[] { (byte) 0xd4, (byte) 0xc3, (byte) 0xb2, (byte) 0xa1 };
            byte[] pcapNgSign = new byte[] { 0x0A, 0x0D, 0x0D, 0x0A };
            if (Arrays.equals(fileSignature, libPcapSign)) {
                fileEncodingType = FileEncodingType.LIB_PCAP;
            } else if (Arrays.equals(fileSignature, pcapNgSign)) {
                fileEncodingType = FileEncodingType.PCAP_NG;
            } else {
                throw new Exception("A file signature does not match to LIBPCAP or PCAPNG file formats");
            }

            switch (fileEncodingType) {
                case LIB_PCAP:
                    littleBigEndianFormat = LittleBigEndianFormat.LITTLE_ENDIAN;
                    byte[] globHeader = new byte[24];
                    System.arraycopy(fileSignature, 0, globHeader, 0, 4);
                    if (fis.read(globHeader, 4, 20) < 20){
                        throw new Exception("Not enough data for a global header LIB PCAP");
                    }
//                    if (fis.read(globHeader) < 24)
//                        throw new Exception("Not enough data for a global header");

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
                            throw new Exception("Not enough data for a packet header LIB PCAP");
                        int ts_sec = parseIntValue(packetHeader, 0, littleBigEndianFormat);
                        int ts_usec = parseIntValue(packetHeader, 4, littleBigEndianFormat);
                        int incl_len = parseIntValue(packetHeader, 8, littleBigEndianFormat);
                        int orig_len = parseIntValue(packetHeader, 12, littleBigEndianFormat);

                        byte[] data = new byte[incl_len];
                        if (fis.read(data) < incl_len)
                            throw new Exception("Not enough data for a packet data");

                        recCnt++;
                        this.parsePacket(data, network);
                    }
                    break;

                case PCAP_NG:
                    byte[] sectionHeaderBlockGlobHeader = new byte[24];
                    System.arraycopy(fileSignature, 0, sectionHeaderBlockGlobHeader, 0, 4);
                    if (fis.read(sectionHeaderBlockGlobHeader, 4, 20) < 20) {
                        throw new Exception("Not enough data for a sectionHeaderBlock Header PCAP NG");
                    }

                    byte[] bigEndianSign = new byte[] { 0x1A, 0x2B, 0x3C, 0x4D };
                    byte[] bigLittleEndianFld = new byte[4];
                    System.arraycopy(sectionHeaderBlockGlobHeader, 8, bigLittleEndianFld, 0, 4);
                    if (Arrays.equals(bigLittleEndianFld, bigEndianSign))
                        littleBigEndianFormat = LittleBigEndianFormat.BIG_ENDIAN;
                    else
                        littleBigEndianFormat = LittleBigEndianFormat.LITTLE_ENDIAN;

                    int blockTotalLength = parseIntValue(sectionHeaderBlockGlobHeader, 4, littleBigEndianFormat);
                    int sectLen = parseIntValue(sectionHeaderBlockGlobHeader, 16, littleBigEndianFormat);
                    byte[] sectionHeaderBlockglobOptions = new byte[blockTotalLength - 24 - 4];
                    if (fis.read(sectionHeaderBlockglobOptions) < sectionHeaderBlockglobOptions.length) {
                        throw new Exception("Not enough data for a sectionHeaderBlock Options PCAP NG");
                    }
                    byte[] blockTotalLength2 = new byte[4];
                    if (fis.read(blockTotalLength2) < 4) {
                        throw new Exception("Not enough data for a blockTotalLength2 PCAP NG");
                    }
                    PcapNgOption[] options = parsePcapNgOptions(sectionHeaderBlockglobOptions, 0, littleBigEndianFormat);

                    int linkType = 0;
                    int snapLen = 0;

                    recCnt = 0;
                    while (fis.available() > 0) {
                        if (this.processControl.checkNeedInterrupt())
                            return;

                        // Packet Header
                        byte[] packetHeader = new byte[8];
                        if (fis.read(packetHeader) < 8)
                            throw new Exception("Not enough data for a packet header PCAP NG");

                        int blockType = parseIntValue(packetHeader, 0, littleBigEndianFormat);
                        blockTotalLength = parseIntValue(packetHeader, 4, littleBigEndianFormat);

                        byte[] data = new byte[blockTotalLength - 12];
                        if (fis.read(data) < blockTotalLength - 12)
                            throw new Exception("Not enough data for a block data PCAP NG");
                        blockTotalLength2 = new byte[4];
                        if (fis.read(blockTotalLength2) < 4) {
                            throw new Exception("Not enough data for a blockTotalLength2 PCAP NG");
                        }

                        switch (blockType) {
                            case 1: // 0x00000001 Interface description block
                                linkType = parseShortValue(data, 0, littleBigEndianFormat);
                                snapLen = parseIntValue(data, 4, littleBigEndianFormat);
                                options = parsePcapNgOptions(data, 8, littleBigEndianFormat);
                                break;

                            case 6: // 0x00000006 Enhanced packet block
                                int _interface = parseIntValue(data, 0, littleBigEndianFormat);
                                long timestamp = parseLongValue(data, 4, littleBigEndianFormat);
                                int capturedLen = parseIntValue(data, 12, littleBigEndianFormat);
                                int packetLen = parseIntValue(data, 16, littleBigEndianFormat);
                                byte[] data2 = new byte[capturedLen];
                                System.arraycopy(data, 20, data2, 0, capturedLen);

                                int cl = ((capturedLen + 3) / 4) * 4;
                                options = parsePcapNgOptions(data, 20 + cl, littleBigEndianFormat);

                                recCnt++;
                                this.parsePacket(data2, linkType);
                                break;
                        }
                    }
                    break;
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

    private int parseIntValue(byte[] buf, int offset, LittleBigEndianFormat littleBigEndianFormat) {
        int res;
        if (littleBigEndianFormat == LittleBigEndianFormat.LITTLE_ENDIAN)
            res = (buf[offset + 0] & 0xFF) + (((int) buf[offset + 1] & 0xFF) << 8) + (((int) buf[offset + 2] & 0xFF) << 16)
                    + (((int) buf[offset + 3] & 0xFF) << 24);
        else
            res = (buf[offset + 3] & 0xFF) + (((int) buf[offset + 2] & 0xFF) << 8) + (((int) buf[offset + 1] & 0xFF) << 16)
                    + (((int) buf[offset + 0] & 0xFF) << 24);
        return res;
    }

    private long parseLongValue(byte[] buf, int offset, LittleBigEndianFormat littleBigEndianFormat) {
        long res;
        if (littleBigEndianFormat == LittleBigEndianFormat.LITTLE_ENDIAN)
            res = (buf[offset + 0] & 0xFF) + (((int) buf[offset + 1] & 0xFF) << 8) + (((int) buf[offset + 2] & 0xFF) << 16)
                    + (((int) buf[offset + 3] & 0xFF) << 24) + ((buf[offset + 4] & 0xFF) << 32)
                    + (((int) buf[offset + 5] & 0xFF) << 40) + (((int) buf[offset + 6] & 0xFF) << 48)
                    + (((int) buf[offset + 7] & 0xFF) << 56);
        else
            res = (buf[offset + 7] & 0xFF) + (((int) buf[offset + 6] & 0xFF) << 8) + (((int) buf[offset + 5] & 0xFF) << 16)
                    + (((int) buf[offset + 4] & 0xFF) << 24) + ((buf[offset + 3] & 0xFF) << 32)
                    + (((int) buf[offset + 2] & 0xFF) << 40) + (((int) buf[offset + 1] & 0xFF) << 48)
                    + (((int) buf[offset + 0] & 0xFF) << 56);
        return res;
    }

    private int parseShortValue(byte[] buf, int offset, LittleBigEndianFormat littleBigEndianFormat) {
        int res;
        if (littleBigEndianFormat == LittleBigEndianFormat.LITTLE_ENDIAN)
            res = (buf[offset + 0] & 0xFF) + (((int) buf[offset + 1] & 0xFF) << 8);
        else
            res = (buf[offset + 1] & 0xFF) + (((int) buf[offset + 0] & 0xFF) << 8);
        return res;
    }

    public PcapNgOption[] parsePcapNgOptions(byte[] data, int ind, LittleBigEndianFormat littleBigEndianFormat) {
        ArrayList<PcapNgOption> res = new ArrayList<PcapNgOption>();
        while (data.length - ind >= 4) {
            int optionCode = this.parseShortValue(data, ind, littleBigEndianFormat);
            int optionLength = this.parseShortValue(data, ind + 2, littleBigEndianFormat);

            if (optionCode == 0) {
                break;
            }

            byte[] buf = new byte[optionLength];
            System.arraycopy(data, ind + 4, buf, 0, optionLength);
            PcapNgOption opt = new PcapNgOption();
            opt.optionCode = optionCode;
            opt.value = buf;
            res.add(opt);

            int ol = ((optionLength + 3) / 4) * 4;
            ind += 4 + ol;
        }

        PcapNgOption[] ress = new PcapNgOption[res.size()];
        res.toArray(ress);
        return ress;
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

        case 141: // DLT_MTP3
            // check the min possible length
            if (data == null || data.length < 5) {
                return;
            }
            byte[] bufMsg = new byte[data.length + 3];
            bufMsg[2] = 63;
            System.arraycopy(data, 0, bufMsg, 3, data.length);
            for (TraceReaderListener ls : this.listeners) {
                TraceParserUtil.parceLegacyMtp3(bufMsg, this.listeners);
            }

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

                int parLen2 = ((parLen - 1) / 4) * 4 + 4;
                pos += parLen2;
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
                TraceParserUtil.parceLegacyMtp3(protocolData, this.listeners);
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

        byte[] bufMsg = new byte[data.length - 12];
        System.arraycopy(data, 12, bufMsg, 0, data.length - 12);

        for (TraceReaderListener ls : this.listeners) {
            ls.ss7Message(si, ni, 0, opc, dpc, sls, bufMsg);
        }
    }

    public enum FileEncodingType {
        LIB_PCAP, PCAP_NG,
    }

    public enum LittleBigEndianFormat {
        BIG_ENDIAN, LITTLE_ENDIAN,
    }

    public class PcapNgOption {
        public int optionCode;
        public byte[] value;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            sb.append("PcapNgOption=[optionCode=");
            sb.append(optionCode);
            sb.append(", len=");
            sb.append(value.length);
            sb.append(", bytes=");
            sb.append(DatatypeConverter.printHexBinary(value));
            sb.append(", UTF8 String=");
            try {
                String decoded = new String(value, "UTF-8");
                sb.append(decoded);
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            sb.append("]");

            return sb.toString();
        }
    }
}
