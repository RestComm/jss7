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

package org.mobicents.protocols.ss7.tcap.concurrent;

import static org.testng.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import javolution.util.FastMap;
import junit.framework.Assert;

import org.mobicents.protocols.ss7.sccp.NetworkIdState;
import org.mobicents.protocols.ss7.sccp.SccpListener;
import org.mobicents.protocols.ss7.sccp.SccpManagementEventListener;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.SccpStack;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.mobicents.protocols.ss7.sccp.message.MessageFactory;
import org.mobicents.protocols.ss7.sccp.message.SccpDataMessage;
import org.mobicents.protocols.ss7.sccp.message.SccpNoticeMessage;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.DialogImpl;
import org.mobicents.protocols.ss7.tcap.PreviewDialogData;
import org.mobicents.protocols.ss7.tcap.PreviewDialogDataKey;
import org.mobicents.protocols.ss7.tcap.TCAPProviderImplWrapper;
import org.mobicents.protocols.ss7.tcap.TCAPStackImplWrapper;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.ss7.congestion.ExecutorCongestionMonitor;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
*
* @author jaime casero
* @author sergey vetyutnev
*
*/
public class DialogPreviewModeMngConcurrentTest {

    private SccpHarnessPreview sccpProv = new SccpHarnessPreview();
    private TCAPStackImplWrapper tcapStack1;
    private static final int MAX_DIALOGS = 100;
    private static final int DPC = 100;
    private static final int SSN = 100;
    private static final int TEST_TIMEOUT = 1000000;
    private static final String SCCP_DIGITS = "1112220000";
    private static final AtomicLong txSeq = new AtomicLong(0);

    private TCAPProviderImplWrapper provider;

    @BeforeClass
    public void setUpClass() {
        System.out.println("setUpClass");
    }

    @AfterClass
    public void tearDownClass() throws Exception {
        System.out.println("tearDownClass");
    }

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @BeforeMethod
    public void setUp() throws Exception {
        System.out.println("setUp");

        this.tcapStack1 = new TCAPStackImplWrapper(this.sccpProv, 8, "DialogMngConcurrentTest");
        this.tcapStack1.start();
        provider = (TCAPProviderImplWrapper) tcapStack1.getProvider();
    }

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    @AfterMethod
    public void tearDown() {
        System.out.println("tearDown");
        this.tcapStack1.stop();
    }

    @Test
    public void testMaxDialogConstraint() throws Exception {
        tcapStack1.setMaxDialogs(MAX_DIALOGS);

        List<Runnable> runnables = new ArrayList<Runnable>();
        for (int i = 0; i < MAX_DIALOGS + 100; i++) {
            runnables.add(new Runnable() {
                public void run() {
                    SccpAddress localAddress = new SccpAddressImpl();
                    SccpAddress remoteAddress = new SccpAddressImpl();
                    PreviewDialogDataKey ky = new PreviewDialogDataKey(DPC, SCCP_DIGITS, SSN, txSeq.incrementAndGet());
                    try {
                        Long previewDialogId = provider.getAvailableTxIdPreview();
                        PreviewDialogData pdd = new PreviewDialogData(provider, previewDialogId);
                        provider.createPreviewDialog(ky, localAddress, remoteAddress, 0);
                    } catch (Exception e) {

                    }
                }
            });
        }

        ConcurrentTestUtil.assertConcurrent("", runnables, TEST_TIMEOUT);
        Assert.assertEquals(MAX_DIALOGS, provider.getDialogPreviewList().size());

        tcapStack1.setMaxDialogs(10000);
    }

    @Test
    public void testInsertKeyConstraint() throws Exception {

        final PreviewDialogDataKey ky = new PreviewDialogDataKey(DPC, SCCP_DIGITS, SSN, txSeq.incrementAndGet());
        // Long previewDialogId = provider.getAvailableTxIdPreview();
        // final PreviewDialogData pdd = new PreviewDialogData(provider, previewDialogId);
        final SccpAddress localAddress = new SccpAddressImpl();
        final SccpAddress remoteAddress = new SccpAddressImpl();

        List<Runnable> runnables = new ArrayList<Runnable>();
        for (int i = 0; i < 100; i++) {
            runnables.add(new Runnable() {
                public void run() {
                    try {
                        provider.createPreviewDialog(ky, localAddress, remoteAddress, 0);
                    } catch (TCAPException e) {
                        Logger.getAnonymousLogger().info(e.getMessage());
                    }
                }
            });
        }

        ConcurrentTestUtil.assertConcurrent("", runnables, TEST_TIMEOUT);
        // we do not check it because this is not accurate often
//        Assert.assertEquals(0, provider.getDialogPreviewList().size());
    }

    @Test
    public void testInsertKeyConstraint2() throws Exception {
        List<Runnable> runnables = new ArrayList<Runnable>();
        for (int i = 0; i < 100; i++) {
            PreviewDialogDataKey ky = new PreviewDialogDataKey(DPC, SCCP_DIGITS, SSN, txSeq.incrementAndGet());
            runnables.add(new TestInsertKeyConstraint2Class(ky));
        }

        ConcurrentTestUtil.assertConcurrent("", runnables, TEST_TIMEOUT);
        Assert.assertEquals(100, provider.getDialogPreviewList().size());
    }

    private class TestInsertKeyConstraint2Class implements Runnable {
        final PreviewDialogDataKey ky;
        final SccpAddress localAddress = new SccpAddressImpl();
        final SccpAddress remoteAddress = new SccpAddressImpl();

        public TestInsertKeyConstraint2Class(PreviewDialogDataKey ky) {
            this.ky = ky;
        }

        @Override
        public void run() {
            try {
                provider.createPreviewDialog(ky, localAddress, remoteAddress, 0);
            } catch (TCAPException e) {
                ConcurrentTestUtil.exceptions.add(e);
            }
        }
    }

    @Test
    public void testInsertKeyConstraint3() throws Exception {
        List<Runnable> runnables = new ArrayList<Runnable>();
        for (int i = 0; i < 100; i++) {
            PreviewDialogDataKey ky = new PreviewDialogDataKey(DPC, SCCP_DIGITS, SSN, txSeq.incrementAndGet());
            runnables.add(new TestInsertKeyConstraint3Class(ky));
            runnables.add(new TestInsertKeyConstraint3Class(ky));
        }

        ConcurrentTestUtil.assertConcurrent("", runnables, TEST_TIMEOUT);
        Assert.assertEquals(0, provider.getDialogPreviewList().size());
    }

    private class TestInsertKeyConstraint3Class implements Runnable {
        final PreviewDialogDataKey ky;
        final SccpAddress localAddress = new SccpAddressImpl();
        final SccpAddress remoteAddress = new SccpAddressImpl();

        public TestInsertKeyConstraint3Class(PreviewDialogDataKey ky) {
            this.ky = ky;
        }

        @Override
        public void run() {
            try {
                provider.createPreviewDialog(ky, localAddress, remoteAddress, 0);
            } catch (TCAPException e) {
                Logger.getAnonymousLogger().info(e.getMessage());
            }
        }
    }

    @Test
    public void testUpdatePreviewDialogKey2() throws Exception {

        final PreviewDialogDataKey ky = new PreviewDialogDataKey(DPC, SCCP_DIGITS, SSN, txSeq.incrementAndGet());
        // Long previewDialogId = provider.getAvailableTxIdPreview();
        // final PreviewDialogData pdd = new PreviewDialogData(provider, previewDialogId);
        final SccpAddress localAddress = new SccpAddressImpl();
        final SccpAddress remoteAddress = new SccpAddressImpl();
        provider.createPreviewDialog(ky, localAddress, remoteAddress, 0);

        final PreviewDialogDataKey ky2 = new PreviewDialogDataKey(DPC, SCCP_DIGITS, SSN, txSeq.incrementAndGet());

        List<Runnable> runnables = new ArrayList<Runnable>();
        for (int i = 0; i < 100; i++) {
            runnables.add(new Runnable() {
                public void run() {
                    provider.getPreviewDialog(ky, ky2, localAddress, remoteAddress, 0);
                }
            });
        }

        ConcurrentTestUtil.assertConcurrent("", runnables, TEST_TIMEOUT);
        Assert.assertEquals(2, provider.getDialogPreviewList().size());
        Assert.assertNotNull(provider.getPreviewDialog(ky, null, localAddress, remoteAddress, 0));
        Assert.assertNotNull(provider.getPreviewDialog(ky2, null, localAddress, remoteAddress, 0));
    }

    @Test
    public void testUpdatePreviewDialogKey3() throws Exception {
        // Long previewDialogId = provider.getAvailableTxIdPreview();
        // final PreviewDialogData pdd = new PreviewDialogData(provider, previewDialogId);

        List<Runnable> runnables = new ArrayList<Runnable>();
        List<Runnable> runnables2 = new ArrayList<Runnable>();
        for (int i = 0; i < 100; i++) {
            PreviewDialogDataKey ky = new PreviewDialogDataKey(DPC, SCCP_DIGITS, SSN, txSeq.incrementAndGet());
            PreviewDialogDataKey ky2 = new PreviewDialogDataKey(DPC, SCCP_DIGITS, SSN, txSeq.incrementAndGet());
            runnables.add(new TestUpdatePreviewDialogKey3Class(ky, ky2));
            runnables2.add(new TestUpdatePreviewDialogKey3aClass(ky, ky2));
        }

        Assert.assertEquals(0, provider.getDialogPreviewList().size());
        ConcurrentTestUtil.assertConcurrent("", runnables, TEST_TIMEOUT);
        Assert.assertEquals(200, provider.getDialogPreviewList().size());

        ConcurrentTestUtil.assertConcurrent("", runnables2, TEST_TIMEOUT);
        Assert.assertEquals(0, provider.getDialogPreviewList().size());
    }

    private class TestUpdatePreviewDialogKey3Class implements Runnable {
        final PreviewDialogDataKey ky;
        final PreviewDialogDataKey ky2;
        final SccpAddress localAddress = new SccpAddressImpl();
        final SccpAddress remoteAddress = new SccpAddressImpl();

        public TestUpdatePreviewDialogKey3Class(PreviewDialogDataKey ky, PreviewDialogDataKey ky2) {
            this.ky = ky;
            this.ky2 = ky2;
        }

        @Override
        public void run() {
            try {
                provider.createPreviewDialog(ky, localAddress, remoteAddress, 0);
                provider.getPreviewDialog(ky, ky2, localAddress, remoteAddress, 0);
                provider.getPreviewDialog(ky2, ky, localAddress, remoteAddress, 0);
                provider.getPreviewDialog(ky, ky2, localAddress, remoteAddress, 0);
                provider.getPreviewDialog(ky2, ky, localAddress, remoteAddress, 0);
                provider.getPreviewDialog(ky, ky2, localAddress, remoteAddress, 0);
                provider.getPreviewDialog(ky2, ky, localAddress, remoteAddress, 0);
                provider.getPreviewDialog(ky, ky2, localAddress, remoteAddress, 0);
                provider.getPreviewDialog(ky2, ky, localAddress, remoteAddress, 0);
            } catch (TCAPException e) {
                Logger.getAnonymousLogger().info(e.getMessage());
            }
        }
    }

    private class TestUpdatePreviewDialogKey3aClass implements Runnable {
        final PreviewDialogDataKey ky;
        final PreviewDialogDataKey ky2;
        final SccpAddress localAddress = new SccpAddressImpl();
        final SccpAddress remoteAddress = new SccpAddressImpl();

        public TestUpdatePreviewDialogKey3aClass(PreviewDialogDataKey ky, PreviewDialogDataKey ky2) {
            this.ky = ky;
            this.ky2 = ky2;
        }

        @Override
        public void run() {
            DialogImpl d = (DialogImpl) provider.getPreviewDialog(ky, null, localAddress, remoteAddress, 0);
            DialogImpl d2 = (DialogImpl) provider.getPreviewDialog(ky2, null, localAddress, remoteAddress, 0);

            provider.removePreviewDialog(d);
            provider.removePreviewDialog(d2);
        }
    }

    @Test
    public void testRemove() throws Exception {

        final PreviewDialogDataKey ky = new PreviewDialogDataKey(DPC, SCCP_DIGITS, SSN, txSeq.incrementAndGet());
        Long previewDialogId = provider.getAvailableTxIdPreview();
        final PreviewDialogData pdd = new PreviewDialogData(provider, previewDialogId);
        final SccpAddress localAddress = new SccpAddressImpl();
        final SccpAddress remoteAddress = new SccpAddressImpl();
        final DialogImpl createPreviewDialog = (DialogImpl) provider.createPreviewDialog(ky, localAddress, remoteAddress, 0);

        final PreviewDialogDataKey ky2 = new PreviewDialogDataKey(DPC, SCCP_DIGITS, SSN, txSeq.incrementAndGet());
        provider.getPreviewDialog(ky, ky2, localAddress, remoteAddress, 0);

        List<Runnable> runnables = new ArrayList();
        for (int i = 0; i < 100; i++) {
            runnables.add(new Runnable() {
                public void run() {
                    provider.removePreviewDialog(createPreviewDialog);
                }
            });
        }

        ConcurrentTestUtil.assertConcurrent("", runnables, TEST_TIMEOUT);
        Assert.assertEquals(0, provider.getDialogPreviewList().size());
    }

    private class SccpHarnessPreview implements SccpProvider {

        @Override
        public void deregisterSccpListener(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public int getMaxUserDataLength(SccpAddress arg0, SccpAddress arg1, int networkId) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public MessageFactory getMessageFactory() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public ParameterFactory getParameterFactory() {
            // TODO Auto-generated method stub
            return null;
        }

        protected SccpListener sccpListener;

        @Override
        public void registerSccpListener(int arg0, SccpListener listener) {
            sccpListener = listener;
        }

        @Override
        public void send(SccpDataMessage msg) throws IOException {
            // we check here that no messages go from TCAP previewMode

            fail("No message must go from TCAP previewMode");
        }

        @Override
        public void registerManagementEventListener(SccpManagementEventListener listener) {
            // TODO Auto-generated method stub

        }

        @Override
        public void deregisterManagementEventListener(SccpManagementEventListener listener) {
            // TODO Auto-generated method stub

        }

        @Override
        public void coordRequest(int ssn) {

        }

        @Override
        public FastMap<Integer, NetworkIdState> getNetworkIdStateList() {
            return new FastMap<Integer, NetworkIdState>();
        }

        @Override
        public ExecutorCongestionMonitor[] getExecutorCongestionMonitorList() {
            return null;
        }

        @Override
        public void send(SccpNoticeMessage message) throws IOException {
            // TODO Auto-generated method stub
            
        }

		@Override
		public SccpStack getSccpStack() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void updateSPCongestion(Integer ssn, Integer congestionLevel) {
			// TODO Auto-generated method stub
			
		}
    }

}
