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

package org.mobicents.protocols.ss7.tcap;

import static org.testng.Assert.*;

import java.io.IOException;

import javolution.util.FastMap;

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
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.ss7.congestion.ExecutorCongestionMonitor;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
*
*/
public class CreateDialogTest {

    private SccpHarnessPreview sccpProv = new SccpHarnessPreview();
    private TCAPStackImplWrapper tcapStack1;

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

        this.tcapStack1 = new TCAPStackImplWrapper(this.sccpProv, 8, "CreateDialogTest");

        this.tcapStack1.start();
    }

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#tearDown()
     */
    @AfterMethod
    public void tearDown() {
        this.tcapStack1.stop();
    }

    @Test(groups = { "functional.flow" })
    public void createDialogTest() throws Exception {

        SccpAddress localAddress = new SccpAddressImpl();
        SccpAddress remoteAddress = new SccpAddressImpl();

        Dialog dlg1 = this.tcapStack1.getProvider().getNewDialog(localAddress, remoteAddress);
        assertEquals((long) dlg1.getLocalDialogId(), 1L);

        try {
            Dialog dlg2 = this.tcapStack1.getProvider().getNewDialog(localAddress, remoteAddress, 1L);
            fail("Must be failure because dialogID==1 is busy");
        } catch (Exception e) {
        }

        Dialog dlg3 = this.tcapStack1.getProvider().getNewDialog(localAddress, remoteAddress, 2L);
        assertEquals((long) dlg3.getLocalDialogId(), 2L);

        Dialog dlg4 = this.tcapStack1.getProvider().getNewDialog(localAddress, remoteAddress);
        assertEquals((long) dlg4.getLocalDialogId(), 3L);
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
            // TODO Auto-generated method stub
            
        }

        @Override
        public FastMap<Integer, NetworkIdState> getNetworkIdStateList() {
            return new FastMap<Integer, NetworkIdState>();
        }

        @Override
        public ExecutorCongestionMonitor[] getExecutorCongestionMonitorList() {
            // TODO Auto-generated method stub
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
