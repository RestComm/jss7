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

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.mobicents.protocols.ss7.mtp.Mtp3;
import org.mobicents.protocols.ss7.mtp.Mtp3EndCongestionPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3PausePrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3ResumePrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3StatusCause;
import org.mobicents.protocols.ss7.mtp.Mtp3StatusPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitiveFactory;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPartBaseImpl;

/**
 * @author abhayani
 * @author baranowb
 * @author sergey vetyutnev
 */
public class Mtp3UserPartImpl extends Mtp3UserPartBaseImpl {

    // protected ConcurrentLinkedQueue<byte[]> readFrom;
    // protected ConcurrentLinkedQueue<byte[]> writeTo;

    private Mtp3UserPartImpl otherPart;
    private ArrayList<Mtp3TransferPrimitive> messages = new ArrayList<Mtp3TransferPrimitive>();

    protected boolean saveTrafficInFile = false;

    public Mtp3UserPartImpl() {
        super(null);
        try {
            this.start();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setOtherPart(Mtp3UserPartImpl otherPart) {
        this.otherPart = otherPart;
    }

    private int tsnNum = (new Random()).nextInt(1000);

    public void sendMessage(Mtp3TransferPrimitive msg) throws IOException {
        if (saveTrafficInFile) {
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

                tsnNum++;

                stm2.write(tsnNum); // TSN
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
                FileOutputStream fs = new FileOutputStream("MsgLog.pcap", true);
//                FileOutputStream fs = new FileOutputStream(Util.getTmpTestDir() + File.separator + "MsgLog.pcap", true);
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

        if (this.otherPart != null)
            this.otherPart.sendTransferMessageToLocalUser(msg, msg.getSls());
        else
            this.messages.add(msg);
    }

    public void sendTransferMessageToLocalUser(int opc, int dpc, byte[] data) {
        int si = Mtp3._SI_SERVICE_SCCP;
        int ni = 2;
        int mp = 0;
        int sls = 0;
        Mtp3TransferPrimitiveFactory factory = this.getMtp3TransferPrimitiveFactory();
        Mtp3TransferPrimitive msg = factory.createMtp3TransferPrimitive(si, ni, mp, opc, dpc, sls, data);
        int seqControl = 0;
        this.sendTransferMessageToLocalUser(msg, seqControl);
    }

    public void sendPauseMessageToLocalUser(int affectedDpc) {
        Mtp3PausePrimitive msg = new Mtp3PausePrimitive(affectedDpc);
        this.sendPauseMessageToLocalUser(msg);
    }

    public void sendResumeMessageToLocalUser(int affectedDpc) {
        Mtp3ResumePrimitive msg = new Mtp3ResumePrimitive(affectedDpc);
        this.sendResumeMessageToLocalUser(msg);
    }

    public void sendStatusMessageToLocalUser(int affectedDpc, Mtp3StatusCause cause, int congestionLevel, int userPartIdentity) {
        Mtp3StatusPrimitive msg = new Mtp3StatusPrimitive(affectedDpc, cause, congestionLevel, userPartIdentity);
        this.sendStatusMessageToLocalUser(msg);
    }

    public void sendEndCongestionMessageToLocalUser(int affectedDpc) {
        Mtp3EndCongestionPrimitive msg = new Mtp3EndCongestionPrimitive(affectedDpc);
        this.sendEndCongestionMessageToLocalUser(msg);
    }

    public List<Mtp3TransferPrimitive> getMessages() {
        return messages;
    }

    @Override
    public int getMaxUserDataLength(int dpc) {
        return 1000;
    }
}
