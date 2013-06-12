package org.mobicents.protocols.ss7.tools.simulator.level1;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.mobicents.protocols.ss7.m3ua.impl.M3UAManagementImpl;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitive;

public class M3UAManagementProxyImpl extends M3UAManagementImpl {

    private String pcapTraceName;

    public M3UAManagementProxyImpl(String name) {
        super(name);
    }

    public void startPcapTrace(String fileName) {
        this.pcapTraceName = fileName;
        this.startPcapTrace();
    }

    @Override
    public void sendMessage(Mtp3TransferPrimitive mtp3TransferPrimitive) throws IOException {
        super.sendMessage(mtp3TransferPrimitive);

        this.storeTransferMessageToPcap(mtp3TransferPrimitive, true);
    }

    @Override
    public void sendTransferMessageToLocalUser(Mtp3TransferPrimitive msg, int seqControl) {
        super.sendTransferMessageToLocalUser(msg, seqControl);

        this.storeTransferMessageToPcap(msg, false);
    }

    private synchronized void startPcapTrace() {
        try {
            FileOutputStream fs = new FileOutputStream(this.pcapTraceName, false);

            // pcap global header
            fs.write(0xd4);
            fs.write(0xc3);
            fs.write(0xb2);
            fs.write(0xa1);

            fs.write(2);
            fs.write(0);
            fs.write(4);
            fs.write(0);

            fs.write(0);
            fs.write(0);
            fs.write(0);
            fs.write(0);
            fs.write(0);
            fs.write(0);
            fs.write(0);
            fs.write(0);

            fs.write(0xFF);
            fs.write(0xFF);
            fs.write(0);
            fs.write(0);
            fs.write(1);
            fs.write(0);
            fs.write(0);
            fs.write(0);

            fs.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void storeTransferMessageToPcap(Mtp3TransferPrimitive msg, boolean localOriginated) {
        if (this.pcapTraceName == null)
            return;

        try {
            // pcap version
            // m3ua part
            byte[] sccpPart = msg.getData();
            ByteArrayOutputStream stm1 = new ByteArrayOutputStream();
            stm1.write(1); // m3ua version - release 1
            stm1.write(0); // m3ua reserved
            stm1.write(1); // m3ua message class - Transfer message
            stm1.write(1); // m3ua Message type - Payload data

            int lenb = sccpPart.length + 16;
            int lena = lenb + 8;
            int m3uaPad = lena % 4;
            if (m3uaPad > 0)
                m3uaPad = 4 - m3uaPad;
            lena += m3uaPad;

            stm1.write(0); // m3ua Payload data length
            stm1.write(0);
            stm1.write(lena / 256);
            stm1.write(lena % 256);

            stm1.write(2); // Parameter tag: protocol data
            stm1.write(0x10);

            stm1.write(lenb / 256); // protocol data length
            stm1.write(lenb % 256);

            stm1.write(0); // m3ua opc
            stm1.write(0);
            stm1.write(msg.getOpc() / 256);
            stm1.write(msg.getOpc() % 256);

            stm1.write(0); // m3ua dpc
            stm1.write(0);
            stm1.write(msg.getDpc() / 256);
            stm1.write(msg.getDpc() % 256);

            stm1.write(msg.getSi());
            stm1.write(msg.getNi());
            stm1.write(msg.getMp());
            stm1.write(msg.getSls());

            stm1.write(sccpPart);

            for (int i1 = 0; i1 < m3uaPad; i1++) {
                stm1.write(0);
            }

            byte[] m3uaPart = stm1.toByteArray();

            // sctp part
            ByteArrayOutputStream stm2 = new ByteArrayOutputStream();
            stm2.write(1); // source port
            stm2.write(1);
            stm2.write(2); // dest port
            stm2.write(2);
            stm2.write(3); // verification tag
            stm2.write(3);
            stm2.write(3);
            stm2.write(3);
            stm2.write(4); // checksum - not really calculated
            stm2.write(4);
            stm2.write(4);
            stm2.write(4);

            int lenc = m3uaPart.length + 16;
            stm2.write(0); // chunk type - data
            stm2.write(3); // chunk flags
            stm2.write(lenc / 256); // chunk length
            stm2.write(lenc % 256);

            stm2.write(0); // TSN
            stm2.write(0);
            stm2.write(0);
            stm2.write(0);

            stm2.write(0); // stream identifier
            stm2.write(3);

            stm2.write(11); // stream sequence number
            stm2.write(11);

            stm2.write(0); // protocol identifier - m3ua
            stm2.write(0);
            stm2.write(0);
            stm2.write(3);

            stm2.write(m3uaPart);

            byte[] sctpPart = stm2.toByteArray();

            // ip V4 part
            ByteArrayOutputStream stm3 = new ByteArrayOutputStream();
            int lend = sctpPart.length + 20;
            stm3.write(0x45); // version 4 + headerLen=20
            stm3.write(0xe0); // dscp + ecn
            stm3.write(lend / 256); // total length
            stm3.write(lend % 256);

            stm3.write(10); // identification
            stm3.write(11);
            stm3.write(0); // flags + fragment offset
            stm3.write(0);

            stm3.write(0x40); // time to live
            stm3.write(0x84); // protocol: sctp
            stm3.write(0); // checksum: not really checked
            stm3.write(0);

            stm3.write(1); // source ip address
            stm3.write(2);
            stm3.write(3);
            stm3.write(4);

            stm3.write(4); // dest ip address
            stm3.write(3);
            stm3.write(2);
            stm3.write(1);

            stm3.write(sctpPart);

            byte[] ipPart = stm3.toByteArray();

            // calculating ip header checksum
            int ix = 0;
            for (int i1 = 0; i1 < 10; i1++) {
                int ii1 = ipPart[i1 * 2] & 0xFF;
                int ii2 = ipPart[i1 * 2 + 1] & 0xFF;
                ix += (ii1 << 8) + ii2;
            }
            int iy = ix & 0xFFFF;
            int iz = iy ^ 0xFFFF;
            ipPart[10] = (byte) (iz / 256);
            ipPart[11] = (byte) (iz % 256);

            // Ethernet part
            ByteArrayOutputStream stm4 = new ByteArrayOutputStream();
            stm4.write(8); // source
            stm4.write(8);
            stm4.write(8);
            stm4.write(8);
            stm4.write(8);
            stm4.write(8);

            stm4.write(6); // destination
            stm4.write(6);
            stm4.write(6);
            stm4.write(6);
            stm4.write(6);
            stm4.write(6);

            stm4.write(8); // type: ip
            stm4.write(0);

            stm4.write(ipPart);

            byte[] ethPart = stm4.toByteArray();

            // pcap header
            FileOutputStream fs = new FileOutputStream(this.pcapTraceName, true);
            int ln = ethPart.length;

            fs.write(0);
            fs.write(0);
            fs.write(0);
            fs.write(0);

            fs.write(0);
            fs.write(0);
            fs.write(0);
            fs.write(0);

            fs.write(ln % 256);
            fs.write(ln / 256);
            fs.write(0);
            fs.write(0);

            fs.write(ln % 256);
            fs.write(ln / 256);
            fs.write(0);
            fs.write(0);

            // pcap data
            fs.write(ethPart);
            fs.close();

            // MsgLog.txt version
            // byte[] txData = msg.encodeMtp3();
            // FileOutputStream fs = new FileOutputStream("MsgLog.txt", true);
            // int ln = txData.length;
            // fs.write(ln & 0xFF);
            // fs.write(ln >> 8);
            // fs.write(txData);
            // fs.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
