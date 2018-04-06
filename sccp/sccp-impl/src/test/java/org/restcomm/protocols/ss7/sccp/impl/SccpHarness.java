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

package org.restcomm.protocols.ss7.sccp.impl;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.FileAppender;
import org.apache.log4j.PatternLayout;
import org.restcomm.protocols.ss7.Util;
import org.restcomm.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.restcomm.protocols.ss7.sccp.Router;
import org.restcomm.protocols.ss7.sccp.SccpConnection;
import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.SccpProvider;
import org.restcomm.protocols.ss7.sccp.SccpResource;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.LocalReferenceImpl;
import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;
import org.restcomm.protocols.ss7.scheduler.Clock;
import org.restcomm.protocols.ss7.scheduler.DefaultClock;
import org.restcomm.protocols.ss7.scheduler.Scheduler;
import org.restcomm.protocols.ss7.ss7ext.Ss7ExtInterface;

import static org.testng.Assert.assertEquals;

/**
 * @author amit bhayani
 *
 */
public abstract class SccpHarness {

    protected boolean onlyOneStack;

    protected String sccpStack1Name = null;
    protected String sccpStack2Name = null;

    protected SccpStackImpl sccpStack1;
    protected SccpProvider sccpProvider1;

    protected SccpStackImpl sccpStack2;
    protected SccpProvider sccpProvider2;

    protected Mtp3UserPartImpl mtp3UserPart1 = new Mtp3UserPartImpl(this);
    protected Mtp3UserPartImpl mtp3UserPart2 = new Mtp3UserPartImpl(this);

    protected Router router1 = null;
    protected Router router2 = null;

    protected SccpResource resource1 = null;
    protected SccpResource resource2 = null;

    protected ParameterFactory parameterFactory;

    /**
	 *
	 */
    public SccpHarness() {
        mtp3UserPart1.setOtherPart(mtp3UserPart2);
        mtp3UserPart2.setOtherPart(mtp3UserPart1);
    }

    protected void createStack1() {
        sccpStack1 = createStack(sccpStack1Name, null);
    }

    protected void createStack2() {
        sccpStack2 = createStack(sccpStack2Name, null);
    }

    protected SccpStackImpl createStack(final String name, Ss7ExtInterface ss7ExtInterface) {
        Clock clock = new DefaultClock();
        Scheduler scheduler = new Scheduler();
        scheduler.setClock(clock);
        scheduler.start();

        SccpStackImpl stack = new SccpStackImpl(scheduler, name, ss7ExtInterface);
        final String dir = Util.getTmpTestDir();
        if (dir != null) {
            stack.setPersistDir(dir);
        }
        return stack;
    }

    protected void setUpStack1() throws Exception {
        createStack1();

        sccpStack1.setMtp3UserPart(1, mtp3UserPart1);
        sccpStack1.start();
        sccpStack1.removeAllResourses();
        sccpStack1.getRouter().addMtp3ServiceAccessPoint(1, 1, getStack1PC(), 2, 0, null);
        sccpStack1.getRouter().addMtp3Destination(1, 1, getStack2PC(), getStack2PC(), 0, 255, 255);

        sccpProvider1 = sccpStack1.getSccpProvider();

        router1 = sccpStack1.getRouter();

        resource1 = sccpStack1.getSccpResource();

        resource1.addRemoteSpc(1, getStack2PC(), 0, 0);
        resource1.addRemoteSsn(1, getStack2PC(), getSSN2(), 0, false);
        this.parameterFactory = this.sccpProvider1.getParameterFactory();

    }

    protected void setUpStack2() throws Exception {
        createStack2();

        sccpStack2.setMtp3UserPart(1, mtp3UserPart2);
        sccpStack2.start();
        sccpStack2.removeAllResourses();
        sccpStack2.getRouter().addMtp3ServiceAccessPoint(1, 1, getStack2PC(), 2, 0, null);
        sccpStack2.getRouter().addMtp3Destination(1, 1, getStack1PC(), getStack1PC(), 0, 255, 255);

        sccpProvider2 = sccpStack2.getSccpProvider();

        router2 = sccpStack2.getRouter();

        resource2 = sccpStack2.getSccpResource();

        resource2.addRemoteSpc(02, getStack1PC(), 0, 0);
        resource2.addRemoteSsn(1, getStack1PC(), getSSN(), 0, false);

    }

    private void tearDownStack1() {
        sccpStack1.removeAllResourses();
        sccpStack1.stop();
    }

    private void tearDownStack2() {
        sccpStack2.removeAllResourses();
        sccpStack2.stop();
    }

    protected int getStack1PC() {
        if (sccpStack1.getSccpProtocolVersion() == SccpProtocolVersion.ANSI)
            return 8000001;
        else
            return 1;
    }

    protected int getStack2PC() {
        if (onlyOneStack) {
            return getStack1PC();
        }

        if (sccpStack1.getSccpProtocolVersion() == SccpProtocolVersion.ANSI)
            return 8000002;
        else
        return 2;
    }

    protected int getSSN() {
        return 8;
    }

    protected int ssn2 = 8;

    protected int getSSN2() {
        return ssn2;
    }

    public void setUp() throws Exception {
        this.setUpStack1();
        if (!onlyOneStack) {
            this.setUpStack2();
        }
    }

    public void tearDown() {
        this.tearDownStack1();
        if (!onlyOneStack) {
            this.tearDownStack2();
        }
    }

    protected int tsnNum = (new Random()).nextInt(100000);
    protected boolean saveTrafficInFile = false;

    /**
     * After this method invoking all MTP traffic will be save into the file "MsgLog.txt" file format:
     * [message][message]...[message] [message] ::= { byte-length low byte, byte-length high byte, byte[] message }
     */
    public synchronized void saveTrafficInFile() {
        this.saveTrafficInFile = true;
//        ((Mtp3UserPartImpl) this.mtp3UserPart1).saveTrafficInFile = true;
//        ((Mtp3UserPartImpl) this.mtp3UserPart2).saveTrafficInFile = true;

        try {
//            String tmpDir = Util.getTmpTestDir();
//            if (tmpDir != null)
//                tmpDir = tmpDir + File.separator;
//            else
//                tmpDir = "";
//            FileOutputStream fs = new FileOutputStream(tmpDir + "MsgLog.pcap", false);
            FileOutputStream fs = new FileOutputStream("MsgLog.pcap", false);

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

    protected synchronized void saveTrafficFile(Mtp3TransferPrimitive msg) {
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

            stm2.write(0); // TSN
            stm2.write(0);
            stm2.write(tsnNum / 256);
            stm2.write(tsnNum % 256);

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

    public synchronized void saveLogFile(String fileName) {
        try {
            PatternLayout pattern = new PatternLayout();
            pattern.setConversionPattern("%d %-5p [%c] (%t) %m%n");
            FileAppender fileAppender = new FileAppender(pattern, fileName);
            BasicConfigurator.configure(fileAppender);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void assertBothConnectionsExist() {
        if (sccpStack1 != sccpStack2) {
            assertEquals(sccpStack1.getConnectionsNumber(), 1);
            assertEquals(sccpStack2.getConnectionsNumber(), 1);
        } else {
            assertEquals(sccpStack1.getConnectionsNumber(), 2);
        }
    }

    public boolean isBothConnectionsExist() {
        if (sccpStack1 != sccpStack2) {
            return sccpStack1.getConnectionsNumber() == 1 && sccpStack2.getConnectionsNumber() == 1;
        } else {
            return sccpStack1.getConnectionsNumber() == 2;
        }
    }

    public SccpConnection getConn2() {
        if (sccpStack1 != sccpStack2) {
            return sccpProvider2.getConnections().values().iterator().next();
        } else {
            return sccpProvider2.getConnections().get(new LocalReferenceImpl(sccpStack2.referenceNumberCounter));
        }
    }
}
