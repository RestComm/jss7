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
import java.util.ArrayList;
import java.util.List;

import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.SccpListener;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpDataMessageImpl;
import org.mobicents.protocols.ss7.sccp.message.MessageFactory;
import org.mobicents.protocols.ss7.sccp.message.SccpDataMessage;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.TCListener;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCContinueIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCEndIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCNoticeIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCPAbortIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUniIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortIndication;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
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
public class PreviewModeFunctionalTest {
    private SccpHarnessPreview sccpProv = new SccpHarnessPreview();
    private TCAPStackImpl tcapStack1;
    private TCAPListenerHarness tcapListener;
	protected List<TestEvent> observerdEvents;
	protected int sequence;

	public PreviewModeFunctionalTest() {
	}
    
	@BeforeClass
	public void setUpClass() {
//		this.sccpStack1Name = "TCAPFunctionalTestSccpStack1";
//		this.sccpStack2Name = "TCAPFunctionalTestSccpStack2";
		System.out.println("setUpClass");
	}

	@AfterClass
	public void tearDownClass() throws Exception {
		System.out.println("tearDownClass");
	}

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
	@BeforeMethod
	public void setUp() throws Exception {
		System.out.println("setUp");

        this.tcapStack1 = new TCAPStackImpl(this.sccpProv, 8);
       
//        this.tcapStack1.setInvokeTimeout(0);
        this.tcapStack1.setPreviewMode(true);
       
       
        this.tcapStack1.start();

		tcapListener = new TCAPListenerHarness();
		this.tcapStack1.getProvider().addTCListener(tcapListener);

		observerdEvents = new ArrayList<TestEvent>();
		sequence = 0;
    }
    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
	@AfterMethod
	public void tearDown() {
		this.tcapStack1.getProvider().addTCListener(tcapListener);

		this.tcapStack1.stop();
    }

	/**
	 * TC-BEGIN + addProcessUnstructuredSSRequest
	 * TC-END + ReturnError(systemFailure)
	 */
	@Test(groups = { "functional.flow"})
	public void beginEndTest() throws Exception {
		
		MessageFactory fact = new MessageFactoryImpl(null);

		byte[] m1 = new byte[] { 98, -127, -109, 72, 4, 0, 0, 0, 1, 107, 108, 40, 106, 6, 7, 0, 17, -122, 5, 1, 1, 1, -96, 95, 96, 93, -128, 2, 7, -128, -95,
				9, 6, 7, 4, 0, 0, 1, 0, 19, 2, -66, 76, 40, 74, 6, 7, 4, 0, 0, 1, 1, 1, 1, -96, 63, -96, 61, -128, 9, -106, 2, 36, -128, 3, 0, -128, 0, -14,
				-127, 7, -111, 19, 38, -104, -122, 3, -16, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3,
				42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, 108, 29, -95, 27, 2, 1, 1, 2, 1, 59, 48, 19, 4, 1, 15, 4, 5, -86, -40, 108, 54, 2, -128,
				7, -111, 19, 38, -120, -125, 0, -14 };
		byte[] m2 = new byte[] { 100, 60, 73, 4, 0, 0, 0, 1, 107, 42, 40, 40, 6, 7, 0, 17, -122, 5, 1, 1, 1, -96, 29, 97, 27, -128, 2, 7, -128, -95, 9, 6, 7,
				4, 0, 0, 1, 0, 19, 2, -94, 3, 2, 1, 0, -93, 5, -95, 3, 2, 1, 0, 108, 8, -93, 6, 2, 1, 1, 2, 1, 34 };

		SccpAddress addr1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 101, null, 6);
		SccpAddress addr2 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 102, null, 6);
		// RoutingIndicator ri, int dpc, GlobalTitle gt, int ssn

		SccpDataMessageImpl msg = (SccpDataMessageImpl)fact.createDataMessageClass1(addr2, addr1, m1, 0, 0, false, null, null);
		msg.setIncomingOpc(101);
		msg.setIncomingDpc(102);
		this.sccpProv.sccpListener.onMessage(msg);
		msg = (SccpDataMessageImpl)fact.createDataMessageClass1(addr1, addr2, m2, 0, 0, false, null, null);
		msg.setIncomingOpc(102);
		msg.setIncomingDpc(101);
		this.sccpProv.sccpListener.onMessage(msg);

		int i1 = 0;
		i1++;
	}

	private class SccpHarnessPreview implements SccpProvider {

		@Override
		public void deregisterSccpListener(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public int getMaxUserDataLength(SccpAddress arg0, SccpAddress arg1) {
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
	}

	private class TCAPListenerHarness implements TCListener {

		private void opComponents(Component[] comList) {
			if (comList == null)
				return;

			for (Component comp : comList) {
				EventType et = null;
				switch (comp.getType()) {
				case Invoke:
					et = EventType.Invoke;
					break;
				case ReturnResult:
					et = EventType.ReturnResult;
					break;
				case ReturnResultLast:
					et = EventType.ReturnResultLast;
					break;
				case ReturnError:
					et = EventType.ReturnError;
					break;
				case Reject:
					et = EventType.Reject;
					break;
				}

				if (et != null) {
					TestEvent te = TestEvent.createReceivedEvent(et, comp, sequence++);
					observerdEvents.add(te);
				}
			}
		}

		@Override
		public void onTCUni(TCUniIndication ind) {
			TestEvent te = TestEvent.createReceivedEvent(EventType.Uni, ind, sequence++);
			observerdEvents.add(te);

			opComponents(ind.getComponents());
		}

		@Override
		public void onTCBegin(TCBeginIndication ind) {
			TestEvent te = TestEvent.createReceivedEvent(EventType.Begin, ind, sequence++);
			observerdEvents.add(te);

			opComponents(ind.getComponents());
		}

		@Override
		public void onTCContinue(TCContinueIndication ind) {
			TestEvent te = TestEvent.createReceivedEvent(EventType.Continue, ind, sequence++);
			observerdEvents.add(te);

			opComponents(ind.getComponents());
		}

		@Override
		public void onTCEnd(TCEndIndication ind) {
			TestEvent te = TestEvent.createReceivedEvent(EventType.End, ind, sequence++);
			observerdEvents.add(te);

			opComponents(ind.getComponents());
		}

		@Override
		public void onTCUserAbort(TCUserAbortIndication ind) {
			TestEvent te = TestEvent.createReceivedEvent(EventType.UAbort, ind, sequence++);
			observerdEvents.add(te);
		}

		@Override
		public void onTCPAbort(TCPAbortIndication ind) {
			TestEvent te = TestEvent.createReceivedEvent(EventType.PAbort, ind, sequence++);
			observerdEvents.add(te);
		}

		@Override
		public void onTCNotice(TCNoticeIndication ind) {
			TestEvent te = TestEvent.createReceivedEvent(EventType.Notice, ind, sequence++);
			observerdEvents.add(te);
		}

		@Override
		public void onDialogReleased(Dialog d) {
			TestEvent te = TestEvent.createReceivedEvent(EventType.DialogRelease, d, sequence++);
			observerdEvents.add(te);
		}

		@Override
		public void onInvokeTimeout(Invoke tcInvokeRequest) {
			TestEvent te = TestEvent.createReceivedEvent(EventType.InvokeTimeout, tcInvokeRequest, sequence++);
			observerdEvents.add(te);
		}

		@Override
		public void onDialogTimeout(Dialog d) {
			TestEvent te = TestEvent.createReceivedEvent(EventType.DialogTimeout, d, sequence++);
			observerdEvents.add(te);
		}
		
	}
}
