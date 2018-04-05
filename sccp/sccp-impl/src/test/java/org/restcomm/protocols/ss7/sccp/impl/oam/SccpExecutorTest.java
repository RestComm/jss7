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

package org.restcomm.protocols.ss7.sccp.impl.oam;

import javolution.util.FastMap;

import org.restcomm.protocols.ss7.Util;
import org.restcomm.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3TransferPrimitiveFactory;
import org.restcomm.protocols.ss7.mtp.Mtp3UserPart;
import org.restcomm.protocols.ss7.mtp.Mtp3UserPartListener;
import org.restcomm.protocols.ss7.mtp.RoutingLabelFormat;
import org.restcomm.protocols.ss7.sccp.ConcernedSignalingPointCode;
import org.restcomm.protocols.ss7.sccp.LongMessageRule;
import org.restcomm.protocols.ss7.sccp.LongMessageRuleType;
import org.restcomm.protocols.ss7.sccp.Mtp3Destination;
import org.restcomm.protocols.ss7.sccp.Mtp3ServiceAccessPoint;
import org.restcomm.protocols.ss7.sccp.RemoteSignalingPointCode;
import org.restcomm.protocols.ss7.sccp.RemoteSubSystem;
import org.restcomm.protocols.ss7.sccp.Router;
import org.restcomm.protocols.ss7.sccp.SccpResource;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.restcomm.protocols.ss7.sccp.impl.oam.SccpExecutor;
import org.restcomm.protocols.ss7.sccp.impl.oam.SccpOAMMessage;
import org.restcomm.ss7.congestion.ExecutorCongestionMonitor;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class SccpExecutorTest {

    private Router router = null;
    private SccpResource sccpResource = null;
    private SccpStackImpl sccpStack = null;

    private SccpExecutor sccpExecutor = null;

    /**
	 *
	 */
    public SccpExecutorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUp() throws IllegalStateException {
        Mtp3UserPartImpl mtp3UserPartImpl = new Mtp3UserPartImpl();

        this.sccpStack = new SccpStackImpl("SccpExecutorTest", null);
        this.sccpStack.setPersistDir(Util.getTmpTestDir());
        this.sccpStack.setMtp3UserPart(1, mtp3UserPartImpl);
        this.sccpStack.start();
        this.sccpStack.removeAllResourses();
        this.router = this.sccpStack.getRouter();
        this.sccpResource = this.sccpStack.getSccpResource();

        sccpExecutor = new SccpExecutor();
        
        FastMap<String, SccpStackImpl> sccpStacks = new FastMap<String, SccpStackImpl>();
        sccpStacks.put(this.sccpStack.getName(), this.sccpStack);
        
        sccpExecutor.setSccpStacks(sccpStacks);
    }

    @AfterMethod
    public void tearDown() {
        this.sccpStack.stop();
    }

    @Test(groups = { "oam", "functional.mgmt" })
    public void testLmr() {

        String rspCmd = "sccp lmr create 1 11 12 udt";
        String res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.router.getLongMessageRules().size(), 1);
        LongMessageRule lmr = this.router.getLongMessageRule(1);
        assertEquals(lmr.getFirstSpc(), 11);
        assertEquals(lmr.getLastSpc(), 12);
        assertEquals(lmr.getLongMessageRuleType(), LongMessageRuleType.LONG_MESSAGE_FORBBIDEN);

        // Test Show
        rspCmd = "sccp lmr show";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(res, "key=1  firstSpc=11, lastSpc=12, ruleType=LONG_MESSAGE_FORBBIDEN\n");

        rspCmd = "sccp lmr create 2 13 14 xudt";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.router.getLongMessageRules().size(), 2);
        lmr = this.router.getLongMessageRule(2);
        assertEquals(lmr.getFirstSpc(), 13);
        assertEquals(lmr.getLastSpc(), 14);
        assertEquals(lmr.getLongMessageRuleType(), LongMessageRuleType.XUDT_ENABLED);

        rspCmd = "sccp lmr create 3 15 16 ludt";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.router.getLongMessageRules().size(), 3);
        lmr = this.router.getLongMessageRule(3);
        assertEquals(lmr.getFirstSpc(), 15);
        assertEquals(lmr.getLastSpc(), 16);
        assertEquals(lmr.getLongMessageRuleType(), LongMessageRuleType.LUDT_ENABLED);

        rspCmd = "sccp lmr create 4 17 18 ludt_segm";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.router.getLongMessageRules().size(), 4);
        lmr = this.router.getLongMessageRule(4);
        assertEquals(lmr.getFirstSpc(), 17);
        assertEquals(lmr.getLastSpc(), 18);
        assertEquals(lmr.getLongMessageRuleType(), LongMessageRuleType.LUDT_ENABLED_WITH_SEGMENTATION);

        rspCmd = "sccp lmr create 4 19 20 ludt_segm";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(SccpOAMMessage.LMR_ALREADY_EXIST));
        assertEquals(this.router.getLongMessageRules().size(), 4);
        lmr = this.router.getLongMessageRule(4);

        rspCmd = "sccp lmr modify 4 21 22 udt";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.router.getLongMessageRules().size(), 4);
        lmr = this.router.getLongMessageRule(4);
        assertEquals(lmr.getFirstSpc(), 21);
        assertEquals(lmr.getLastSpc(), 22);
        assertEquals(lmr.getLongMessageRuleType(), LongMessageRuleType.LONG_MESSAGE_FORBBIDEN);

        rspCmd = "sccp lmr modify 4 21 22 xudt";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.router.getLongMessageRules().size(), 4);
        lmr = this.router.getLongMessageRule(4);
        assertEquals(lmr.getFirstSpc(), 21);
        assertEquals(lmr.getLastSpc(), 22);
        assertEquals(lmr.getLongMessageRuleType(), LongMessageRuleType.XUDT_ENABLED);

        rspCmd = "sccp lmr modify 4 21 22 ludt";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.router.getLongMessageRules().size(), 4);
        lmr = this.router.getLongMessageRule(4);
        assertEquals(lmr.getFirstSpc(), 21);
        assertEquals(lmr.getLastSpc(), 22);
        assertEquals(lmr.getLongMessageRuleType(), LongMessageRuleType.LUDT_ENABLED);

        rspCmd = "sccp lmr modify 4 21 22 ludt_segm";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.router.getLongMessageRules().size(), 4);
        lmr = this.router.getLongMessageRule(4);
        assertEquals(lmr.getFirstSpc(), 21);
        assertEquals(lmr.getLastSpc(), 22);
        assertEquals(lmr.getLongMessageRuleType(), LongMessageRuleType.LUDT_ENABLED_WITH_SEGMENTATION);

        rspCmd = "sccp lmr modify 5 23 24 udt";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(String.format(SccpOAMMessage.LMR_DOESNT_EXIST,this.sccpStack.getName() )));
        assertEquals(this.router.getLongMessageRules().size(), 4);
        lmr = this.router.getLongMessageRule(4);
        assertEquals(lmr.getFirstSpc(), 21);
        assertEquals(lmr.getLastSpc(), 22);
        assertEquals(lmr.getLongMessageRuleType(), LongMessageRuleType.LUDT_ENABLED_WITH_SEGMENTATION);

        rspCmd = "sccp lmr show 1";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

        rspCmd = "sccp lmr show";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

        rspCmd = "sccp lmr delete 10";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(String.format(SccpOAMMessage.LMR_DOESNT_EXIST,this.sccpStack.getName() )));
        assertEquals(this.router.getLongMessageRules().size(), 4);

        rspCmd = "sccp lmr delete 4";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.router.getLongMessageRules().size(), 3);
    }
    
    @Test(groups = { "oam", "functional.mgmt" })
    public void testSap() {

        String rspCmd = "sccp sap create 5 101 11 2";
        String res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(SccpOAMMessage.MUP_DOESNT_EXIST));
        assertEquals(this.router.getMtp3ServiceAccessPoints().size(), 0);

        rspCmd = "sccp sap create 5 1 11 2";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.router.getMtp3ServiceAccessPoints().size(), 1);
        Mtp3ServiceAccessPoint sap = this.router.getMtp3ServiceAccessPoint(5);
        assertEquals(sap.getOpc(), 11);
        assertEquals(sap.getMtp3Destinations().size(), 0);

        // test show
        rspCmd = "sccp sap show";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(res, "key=5  mtp3Id=1, opc=11, ni=2, networkId=0, localGtDigits=null, dpcList=[]\n");

        rspCmd = "sccp sap create 5 1 11 2";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(SccpOAMMessage.SAP_ALREADY_EXIST));
        assertEquals(this.router.getMtp3ServiceAccessPoints().size(), 1);
        sap = this.router.getMtp3ServiceAccessPoint(5);
        assertEquals(sap.getOpc(), 11);
        assertEquals(sap.getMtp3Destinations().size(), 0);

        rspCmd = "sccp sap modify 5 2 12 2";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(SccpOAMMessage.MUP_DOESNT_EXIST));
        assertEquals(this.router.getMtp3ServiceAccessPoints().size(), 1);
        sap = this.router.getMtp3ServiceAccessPoint(5);
        assertEquals(sap.getOpc(), 11);
        assertEquals(sap.getMtp3Destinations().size(), 0);

        rspCmd = "sccp sap modify 5 1 13 2";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.router.getMtp3ServiceAccessPoints().size(), 1);
        sap = this.router.getMtp3ServiceAccessPoint(5);
        assertEquals(sap.getOpc(), 13);
        assertEquals(sap.getMtp3Destinations().size(), 0);

        rspCmd = "sccp sap modify 6 2 14 2";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(String.format(SccpOAMMessage.SAP_DOESNT_EXIST, this.sccpStack.getName())));
        assertEquals(this.router.getMtp3ServiceAccessPoints().size(), 1);
        sap = this.router.getMtp3ServiceAccessPoint(5);
        assertEquals(sap.getOpc(), 13);
        assertEquals(sap.getMtp3Destinations().size(), 0);

        rspCmd = "sccp dest create 1 7 31 32 3 4 255";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(String.format(SccpOAMMessage.SAP_DOESNT_EXIST,this.sccpStack.getName())));
        assertEquals(this.router.getMtp3ServiceAccessPoints().size(), 1);
        sap = this.router.getMtp3ServiceAccessPoint(5);
        assertEquals(sap.getOpc(), 13);
        assertEquals(sap.getMtp3Destinations().size(), 0);

        rspCmd = "sccp dest create 5 7 31 32 3 4 255";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.router.getMtp3ServiceAccessPoints().size(), 1);
        sap = this.router.getMtp3ServiceAccessPoint(5);
        assertEquals(sap.getOpc(), 13);
        assertEquals(sap.getMtp3Destinations().size(), 1);
        Mtp3Destination dest = sap.getMtp3Destination(7);
        assertEquals(dest.getFirstDpc(), 31);
        assertEquals(dest.getLastDpc(), 32);
        assertEquals(dest.getFirstSls(), 3);
        assertEquals(dest.getLastSls(), 4);
        assertEquals(dest.getSlsMask(), 255);

        rspCmd = "sccp dest create 5 7 33 34 3 4 255";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(SccpOAMMessage.DEST_ALREADY_EXIST));
        assertEquals(this.router.getMtp3ServiceAccessPoints().size(), 1);
        sap = this.router.getMtp3ServiceAccessPoint(5);
        assertEquals(sap.getOpc(), 13);
        assertEquals(sap.getMtp3Destinations().size(), 1);
        dest = sap.getMtp3Destination(7);
        assertEquals(dest.getFirstDpc(), 31);

        rspCmd = "sccp dest modify 1 7 35 36 3 4 15";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(String.format(SccpOAMMessage.SAP_DOESNT_EXIST, this.sccpStack.getName())));
        assertEquals(this.router.getMtp3ServiceAccessPoints().size(), 1);
        sap = this.router.getMtp3ServiceAccessPoint(5);
        assertEquals(sap.getOpc(), 13);
        assertEquals(sap.getMtp3Destinations().size(), 1);
        dest = sap.getMtp3Destination(7);
        assertEquals(dest.getFirstDpc(), 31);

        rspCmd = "sccp dest modify 5 9 38 39 3 4 15";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(String.format(SccpOAMMessage.DEST_DOESNT_EXIST,this.sccpStack.getName())));
        assertEquals(this.router.getMtp3ServiceAccessPoints().size(), 1);
        sap = this.router.getMtp3ServiceAccessPoint(5);
        assertEquals(sap.getOpc(), 13);
        assertEquals(sap.getMtp3Destinations().size(), 1);
        dest = sap.getMtp3Destination(7);
        assertEquals(dest.getFirstDpc(), 31);

        rspCmd = "sccp dest modify 5 7 40 41 3 4 15";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.router.getMtp3ServiceAccessPoints().size(), 1);
        sap = this.router.getMtp3ServiceAccessPoint(5);
        assertEquals(sap.getOpc(), 13);
        assertEquals(sap.getMtp3Destinations().size(), 1);
        dest = sap.getMtp3Destination(7);
        assertEquals(dest.getFirstDpc(), 40);
        assertEquals(dest.getSlsMask(), 15);

        rspCmd = "sccp dest show 5 7";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

        rspCmd = "sccp dest show 5";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

        rspCmd = "sccp sap show 5";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

        rspCmd = "sccp sap show";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

        rspCmd = "sccp dest delete 1 7";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(String.format(SccpOAMMessage.SAP_DOESNT_EXIST, this.sccpStack.getName())));
        assertEquals(this.router.getMtp3ServiceAccessPoints().size(), 1);
        sap = this.router.getMtp3ServiceAccessPoint(5);
        assertEquals(sap.getOpc(), 13);
        assertEquals(sap.getMtp3Destinations().size(), 1);
        dest = sap.getMtp3Destination(7);
        assertEquals(dest.getFirstDpc(), 40);

        rspCmd = "sccp dest delete 5 9";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(String.format(SccpOAMMessage.DEST_DOESNT_EXIST, this.sccpStack.getName())));
        assertEquals(this.router.getMtp3ServiceAccessPoints().size(), 1);
        sap = this.router.getMtp3ServiceAccessPoint(5);
        assertEquals(sap.getOpc(), 13);
        assertEquals(sap.getMtp3Destinations().size(), 1);
        dest = sap.getMtp3Destination(7);
        assertEquals(dest.getFirstDpc(), 40);

        rspCmd = "sccp dest delete 5 7";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.router.getMtp3ServiceAccessPoints().size(), 1);
        sap = this.router.getMtp3ServiceAccessPoint(5);
        assertEquals(sap.getOpc(), 13);
        assertEquals(sap.getMtp3Destinations().size(), 0);

        rspCmd = "sccp sap delete 1";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(String.format(SccpOAMMessage.SAP_DOESNT_EXIST, this.sccpStack.getName())));
        assertEquals(this.router.getMtp3ServiceAccessPoints().size(), 1);
        sap = this.router.getMtp3ServiceAccessPoint(5);
        assertEquals(sap.getOpc(), 13);
        assertEquals(sap.getMtp3Destinations().size(), 0);

        rspCmd = "sccp sap delete 5";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.router.getMtp3ServiceAccessPoints().size(), 0);
    }
    
    @Test(groups = { "oam", "functional.mgmt" })
    public void testRsp() {

        String rspCmd = "sccp rsp create 1 11 0 0";
        String res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.sccpResource.getRemoteSpcs().size(), 1);
        RemoteSignalingPointCode spc = this.sccpResource.getRemoteSpc(1);
        assertEquals(spc.getRemoteSpc(), 11);

        // test show
        rspCmd = "sccp rsp show";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(res, "key=1  rsp=11 rsp-flag=0 mask=0 rsp-prohibited=false rsccp-prohibited=false rl=0 rsl=0\n");

        rspCmd = "sccp rsp create 1 12 0 0";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(SccpOAMMessage.RSPC_ALREADY_EXIST));
        assertEquals(this.sccpResource.getRemoteSpcs().size(), 1);
        spc = this.sccpResource.getRemoteSpc(1);
        assertEquals(spc.getRemoteSpc(), 11);

        rspCmd = "sccp rsp modify 2 12 0 0";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(String.format(SccpOAMMessage.RSPC_DOESNT_EXIST, this.sccpStack.getName())));
        assertEquals(this.sccpResource.getRemoteSpcs().size(), 1);
        spc = this.sccpResource.getRemoteSpc(1);
        assertEquals(spc.getRemoteSpc(), 11);

        rspCmd = "sccp rsp modify 1 12 0 0";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.sccpResource.getRemoteSpcs().size(), 1);
        spc = this.sccpResource.getRemoteSpc(1);
        assertEquals(spc.getRemoteSpc(), 12);

        rspCmd = "sccp rsp show 1";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

        rspCmd = "sccp rsp show";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

        rspCmd = "sccp rsp delete 5";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(String.format(SccpOAMMessage.RSPC_DOESNT_EXIST, this.sccpStack.getName())));
        assertEquals(this.sccpResource.getRemoteSpcs().size(), 1);
        spc = this.sccpResource.getRemoteSpc(1);
        assertEquals(spc.getRemoteSpc(), 12);

        rspCmd = "sccp rsp delete 1";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.sccpResource.getRemoteSpcs().size(), 0);
    }
    
    @Test(groups = { "oam", "functional.mgmt" })
    public void testRss() {

        String rspCmd = "sccp rss create 2 11 8 0";
        String res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.sccpResource.getRemoteSsns().size(), 1);
        RemoteSubSystem rss = this.sccpResource.getRemoteSsn(2);
        assertEquals(rss.getRemoteSpc(), 11);
        assertEquals(rss.getRemoteSsn(), 8);
        assertFalse(rss.getMarkProhibitedWhenSpcResuming());

        // Test show
        rspCmd = "sccp rss show";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(res, "key=2  rsp=11 rss=8 rss-flag=0 rss-prohibited=false\n");

        rspCmd = "sccp rss delete 5";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(res, String.format(SccpOAMMessage.RSS_DOESNT_EXIST, this.sccpStack.getName()));
        assertEquals(this.sccpResource.getRemoteSsns().size(), 1);
        rss = this.sccpResource.getRemoteSsn(2);
        assertEquals(rss.getRemoteSpc(), 11);

        rspCmd = "sccp rss delete 2";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.sccpResource.getRemoteSsns().size(), 0);

        rspCmd = "sccp rss create 2 12 8 0 prohibitedwhenspcresuming true";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(res, String.format(SccpOAMMessage.RSS_SUCCESSFULLY_ADDED, this.sccpStack.getName()));
        assertEquals(this.sccpResource.getRemoteSsns().size(), 1);
        rss = this.sccpResource.getRemoteSsn(2);
        assertEquals(rss.getRemoteSpc(), 12);
        assertEquals(rss.getRemoteSsn(), 8);
        assertTrue(rss.getMarkProhibitedWhenSpcResuming());

        rspCmd = "sccp rss create 2 12 8 0";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(SccpOAMMessage.RSS_ALREADY_EXIST));
        assertEquals(this.sccpResource.getRemoteSsns().size(), 1);
        rss = this.sccpResource.getRemoteSsn(2);
        assertEquals(rss.getRemoteSpc(), 12);
        assertEquals(rss.getRemoteSsn(), 8);
        assertTrue(rss.getMarkProhibitedWhenSpcResuming());

        rspCmd = "sccp rss modify 2 13 18 0";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.sccpResource.getRemoteSsns().size(), 1);
        rss = this.sccpResource.getRemoteSsn(2);
        assertEquals(rss.getRemoteSpc(), 13);
        assertEquals(rss.getRemoteSsn(), 18);
        assertFalse(rss.getMarkProhibitedWhenSpcResuming());

        rspCmd = "sccp rss modify 2 14 19 0 prohibitedwhenspcresuming true";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(res, String.format(SccpOAMMessage.RSS_SUCCESSFULLY_MODIFIED, this.sccpStack.getName()));
        assertEquals(this.sccpResource.getRemoteSsns().size(), 1);
        rss = this.sccpResource.getRemoteSsn(2);
        assertEquals(rss.getRemoteSpc(), 14);
        assertEquals(rss.getRemoteSsn(), 19);
        assertTrue(rss.getMarkProhibitedWhenSpcResuming());

        rspCmd = "sccp rss modify 3 15 19 0 prohibitedwhenspcresuming true";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(res, String.format(SccpOAMMessage.RSS_DOESNT_EXIST, this.sccpStack.getName()));
        assertEquals(this.sccpResource.getRemoteSsns().size(), 1);
        rss = this.sccpResource.getRemoteSsn(2);
        assertEquals(rss.getRemoteSpc(), 14);
        assertEquals(rss.getRemoteSsn(), 19);
        assertTrue(rss.getMarkProhibitedWhenSpcResuming());

        rspCmd = "sccp rss show 1";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

        rspCmd = "sccp rss show 2";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

        rspCmd = "sccp rss show";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
    } 

    @Test(groups = { "oam", "functional.mgmt" })
    public void testConcernedSpc() {

        String rspCmd = "sccp csp create 3 21";
        String res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.sccpResource.getConcernedSpcs().size(), 1);
        ConcernedSignalingPointCode cspc = this.sccpResource.getConcernedSpc(3);
        assertEquals(cspc.getRemoteSpc(), 21);

        // Show test
        rspCmd = "sccp csp show";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(res, "key=3  rsp=21\n");

        rspCmd = "sccp csp create 3 22";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(SccpOAMMessage.CS_ALREADY_EXIST),res);
        assertEquals(this.sccpResource.getConcernedSpcs().size(), 1);
        cspc = this.sccpResource.getConcernedSpc(3);
        assertEquals(cspc.getRemoteSpc(), 21);

        rspCmd = "sccp csp modify 3 23";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.sccpResource.getConcernedSpcs().size(), 1);
        cspc = this.sccpResource.getConcernedSpc(3);
        assertEquals(cspc.getRemoteSpc(), 23);

        rspCmd = "sccp csp modify 33 24";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(String.format(SccpOAMMessage.CS_DOESNT_EXIST, this.sccpStack.getName())));
        assertEquals(this.sccpResource.getConcernedSpcs().size(), 1);
        cspc = this.sccpResource.getConcernedSpc(3);
        assertEquals(cspc.getRemoteSpc(), 23);

        rspCmd = "sccp csp show 3";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

        rspCmd = "sccp csp show";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

        rspCmd = "sccp csp delete 33";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(String.format(SccpOAMMessage.CS_DOESNT_EXIST, this.sccpStack.getName())));
        assertEquals(this.sccpResource.getConcernedSpcs().size(), 1);
        cspc = this.sccpResource.getConcernedSpc(3);
        assertEquals(cspc.getRemoteSpc(), 23);

        rspCmd = "sccp csp delete 3";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.sccpResource.getConcernedSpcs().size(), 0);
    }    

    @Test(groups = { "oam", "functional.mgmt" })
    public void testParameters() {

        String rspCmd = "sccp set xxx 200";
        String res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(res, SccpOAMMessage.INVALID_COMMAND);

        rspCmd = "sccp set zMarginXudtMessage 200 stackname SccpExecutorTest";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(res, String.format(SccpOAMMessage.PARAMETER_SUCCESSFULLY_SET, this.sccpStack.getName()));
        
        assertEquals(this.sccpStack.getZMarginXudtMessage(), 200);

        rspCmd = "sccp set reassemblyTimerDelay 10000";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.sccpStack.getReassemblyTimerDelay(), 10000);

        rspCmd = "sccp set maxDataMessage 3000";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.sccpStack.getMaxDataMessage(), 3000);

        rspCmd = "sccp set removeSpc false";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.sccpStack.isRemoveSpc(), false);

        rspCmd = "sccp set sstTimerDuration_Min 6000";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.sccpStack.getSstTimerDuration_Min(), 6000);

        rspCmd = "sccp set sstTimerDuration_Max 1000000";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.sccpStack.getSstTimerDuration_Max(), 1000000);

        rspCmd = "sccp set sstTimerDuration_IncreaseFactor 2.55";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.sccpStack.getSstTimerDuration_IncreaseFactor(), 2.55);

        rspCmd = "sccp set canrelay true";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.sccpStack.isCanRelay(), true);

        rspCmd = "sccp set connesttimerdelay 60001";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.sccpStack.getConnEstTimerDelay(), 60001);

        rspCmd = "sccp set iastimerdelay 300001";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.sccpStack.getIasTimerDelay(), 300001);

        rspCmd = "sccp set iartimerdelay 660001";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.sccpStack.getIarTimerDelay(), 660001);

        rspCmd = "sccp set reltimerdelay 10001";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.sccpStack.getRelTimerDelay(), 10001);

        rspCmd = "sccp set repeatreltimerdelay 10001";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.sccpStack.getRepeatRelTimerDelay(), 10001);

        rspCmd = "sccp set inttimerdelay 1";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.sccpStack.getIntTimerDelay(), 1);

        rspCmd = "sccp set guardtimerdelay 1380001";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.sccpStack.getGuardTimerDelay(), 1380001);

        rspCmd = "sccp set resettimerdelay 10001";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.sccpStack.getResetTimerDelay(), 10001);

        rspCmd = "sccp get zMarginXudtMessage";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

        rspCmd = "sccp get reassemblyTimerDelay";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

        rspCmd = "sccp get maxDataMessage";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

        rspCmd = "sccp get removeSpc";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

        rspCmd = "sccp get sstTimerDuration_Min";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

        rspCmd = "sccp get sstTimerDuration_Max";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

        rspCmd = "sccp get sstTimerDuration_IncreaseFactor";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

        rspCmd = "sccp get";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

    }

    class Mtp3UserPartImpl implements Mtp3UserPart {

        public void addMtp3UserPartListener(Mtp3UserPartListener arg0) {
            // TODO Auto-generated method stub

        }

        public int getMaxUserDataLength(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        public void removeMtp3UserPartListener(Mtp3UserPartListener arg0) {
            // TODO Auto-generated method stub

        }

        public void sendMessage(Mtp3TransferPrimitive arg0) throws IOException {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         *
         * @see org.restcomm.protocols.ss7.mtp.Mtp3UserPart# getMtp3TransferPrimitiveFactory()
         */
        @Override
        public Mtp3TransferPrimitiveFactory getMtp3TransferPrimitiveFactory() {
            // TODO Auto-generated method stub
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.restcomm.protocols.ss7.mtp.Mtp3UserPart#getRoutingLabelFormat()
         */
        @Override
        public RoutingLabelFormat getRoutingLabelFormat() {
            // TODO Auto-generated method stub
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.restcomm.protocols.ss7.mtp.Mtp3UserPart#setRoutingLabelFormat
         * (org.restcomm.protocols.ss7.mtp.RoutingLabelFormat)
         */
        @Override
        public void setRoutingLabelFormat(RoutingLabelFormat arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean isUseLsbForLinksetSelection() {
            return false;
        }

        @Override
        public void setUseLsbForLinksetSelection(boolean arg0) {

        }

        @Override
        public int getDeliveryMessageThreadCount() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public void setDeliveryMessageThreadCount(int deliveryMessageThreadCount) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public ExecutorCongestionMonitor getExecutorCongestionMonitor() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void start() throws Exception {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void stop() throws Exception {
            // TODO Auto-generated method stub
            
        }
    }
}
