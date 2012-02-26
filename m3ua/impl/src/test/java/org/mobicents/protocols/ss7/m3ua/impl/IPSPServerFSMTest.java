package org.mobicents.protocols.ss7.m3ua.impl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javolution.util.FastMap;

import org.mobicents.protocols.api.Association;
import org.mobicents.protocols.api.AssociationListener;
import org.mobicents.protocols.api.AssociationType;
import org.mobicents.protocols.api.IpChannelType;
import org.mobicents.protocols.api.Management;
import org.mobicents.protocols.api.PayloadData;
import org.mobicents.protocols.api.Server;
import org.mobicents.protocols.ss7.m3ua.ExchangeType;
import org.mobicents.protocols.ss7.m3ua.Functionality;
import org.mobicents.protocols.ss7.m3ua.IPSPType;
import org.mobicents.protocols.ss7.m3ua.impl.SgFSMTest.TestAssociation;
import org.mobicents.protocols.ss7.m3ua.impl.SgFSMTest.TransportManagement;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.message.M3UAMessageImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.asptm.ASPActiveImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.asptm.ASPInactiveImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.transfer.PayloadDataImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ProtocolDataImpl;
import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.mgmt.Notify;
import org.mobicents.protocols.ss7.m3ua.parameter.DestinationPointCode;
import org.mobicents.protocols.ss7.m3ua.parameter.ErrorCode;
import org.mobicents.protocols.ss7.m3ua.parameter.LocalRKIdentifier;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingKey;
import org.mobicents.protocols.ss7.m3ua.parameter.ServiceIndicators;
import org.mobicents.protocols.ss7.m3ua.parameter.Status;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test for FSM of IPSP acting as Server
 * 
 * @author abhayani
 * 
 */
public class IPSPServerFSMTest {
	private ParameterFactoryImpl parmFactory = new ParameterFactoryImpl();
	private MessageFactoryImpl messageFactory = new MessageFactoryImpl();
	private M3UAManagement serverM3UAMgmt = null;

	private TransportManagement transportManagement = null;

	public IPSPServerFSMTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@BeforeMethod
	public void setUp() throws Exception {
		this.transportManagement = new TransportManagement();
		this.serverM3UAMgmt = new M3UAManagement("SgFSMTest");
		this.serverM3UAMgmt.setTransportManagement(this.transportManagement);
		this.serverM3UAMgmt.start();

	}

	@AfterMethod
	public void tearDown() throws Exception {
		serverM3UAMgmt.getAppServers().clear();
		serverM3UAMgmt.getAspfactories().clear();
		serverM3UAMgmt.getRoute().clear();
		serverM3UAMgmt.stop();
	}

	private AspState getAspState(FSM fsm) {
		return AspState.getState(fsm.getState().getName());
	}

	private AsState getAsState(FSM fsm) {
		return AsState.getState(fsm.getState().getName());
	}

	@Test
	public void testSingleAspInAsWithRC() throws Exception {
		// 5.1.1. Single ASP in an Application Server ("1+0" sparing),
		TestAssociation testAssociation = (TestAssociation) this.transportManagement.addAssociation(null, 0, null, 0,
				"testAssoc1");

		RoutingContext rc = parmFactory.createRoutingContext(new long[] { 100 });

		// As remAs = sgw.createAppServer("testas", rc, rKey, trModType);
		As remAs = serverM3UAMgmt.createAs("testas", Functionality.IPSP, ExchangeType.SE, IPSPType.SERVER, rc, null,
				null);
		FSM asLocalFSM = remAs.getLocalFSM();

		AspFactory aspFactory = serverM3UAMgmt.createAspFactory("testasp", "testAssoc1");

		Asp remAsp = serverM3UAMgmt.assignAspToAs("testas", "testasp");
		FSM aspPeerFSM = remAsp.getPeerFSM();

		// Check for Communication UP
		testAssociation.signalCommUp();

		assertEquals(AspState.DOWN, this.getAspState(aspPeerFSM));

		// Peer sends ASP_UP
		M3UAMessageImpl message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP);
		aspFactory.read(message);

		assertEquals(AspState.INACTIVE, this.getAspState(aspPeerFSM));
		assertTrue(validateMessage(testAssociation, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK, -1, -1));

		// also the AS should be INACTIVE now an no Notify should have been sent
		assertEquals(AsState.INACTIVE, this.getAsState(asLocalFSM));

		// Peer sends ASP_ACTIVE
		message = messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE);
		((ASPActiveImpl) message).setRoutingContext(rc);

		aspFactory.read(message);
		assertEquals(AspState.ACTIVE, this.getAspState(aspPeerFSM));
		assertTrue(validateMessage(testAssociation, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE_ACK,
				-1, -1));
		// also the AS should be ACTIVE now
		assertEquals(AsState.ACTIVE, this.getAsState(asLocalFSM));

		// Since we didn't set the Traffic Mode while creating AS, it should now
		// be set to loadshare as default
		assertEquals(TrafficModeType.Loadshare, remAs.getTrafficModeType().getMode());

		// Check for ASP_INACTIVE
		message = messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_INACTIVE);
		((ASPInactiveImpl) message).setRoutingContext(rc);

		aspFactory.read(message);
		assertEquals(AspState.INACTIVE, this.getAspState(aspPeerFSM));
		assertTrue(validateMessage(testAssociation, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_INACTIVE_ACK,
				-1, -1));
		// also the AS should be PENDING now
		assertEquals(AsState.PENDING, this.getAsState(asLocalFSM));

		// Check for ASP_DOWN
		message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN);
		aspFactory.read(message);
		assertEquals(AspState.DOWN, this.getAspState(aspPeerFSM));
		assertTrue(validateMessage(testAssociation, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN_ACK, -1,
				-1));

		// Make sure we don't have any more
		assertNull(testAssociation.txPoll());
	}

	@Test
	public void testSingleAspInAsWithoutRC() throws Exception {
		// 5.1.1. Single ASP in an Application Server ("1+0" sparing),
		TestAssociation testAssociation = (TestAssociation) this.transportManagement.addAssociation(null, 0, null, 0,
				"testAssoc1");

		As remAs = serverM3UAMgmt.createAs("testas", Functionality.IPSP, ExchangeType.SE, IPSPType.SERVER, null, null,
				null);
		FSM asLocalFSM = remAs.getLocalFSM();

		AspFactory aspFactory = serverM3UAMgmt.createAspFactory("testasp", "testAssoc1");

		Asp remAsp = serverM3UAMgmt.assignAspToAs("testas", "testasp");
		FSM aspPeerFSM = remAsp.getPeerFSM();

		// Check for Communication UP
		testAssociation.signalCommUp();

		assertEquals(AspState.DOWN, this.getAspState(aspPeerFSM));

		// Peer sends ASP_UP
		M3UAMessageImpl message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP);
		aspFactory.read(message);

		assertEquals(AspState.INACTIVE, this.getAspState(aspPeerFSM));
		assertTrue(validateMessage(testAssociation, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK, -1, -1));
		// also the AS should be INACTIVE now
		assertEquals(AsState.INACTIVE, this.getAsState(asLocalFSM));

		// Check for ASP_ACTIVE
		message = messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE);

		aspFactory.read(message);
		assertEquals(AspState.ACTIVE, this.getAspState(aspPeerFSM));
		assertTrue(validateMessage(testAssociation, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE_ACK,
				-1, -1));
		// also the AS should be ACTIVE now
		assertEquals(AsState.ACTIVE, this.getAsState(asLocalFSM));

		// Since we didn't set the Traffic Mode while creating AS, it should now
		// be set to loadshare as default
		assertEquals(TrafficModeType.Loadshare, remAs.getTrafficModeType().getMode());

		// Peer sends ASP_INACTIVE
		message = messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_INACTIVE);
		aspFactory.read(message);
		assertEquals(AspState.INACTIVE, this.getAspState(aspPeerFSM));
		assertTrue(validateMessage(testAssociation, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_INACTIVE_ACK,
				-1, -1));
		// also the AS should be PENDING now
		assertEquals(AsState.PENDING, this.getAsState(asLocalFSM));

		// Check for ASP_DOWN
		message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN);
		aspFactory.read(message);
		assertEquals(AspState.DOWN, this.getAspState(aspPeerFSM));
		assertTrue(validateMessage(testAssociation, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN_ACK, -1,
				-1));

		// Make sure we don't have any more
		assertNull(testAssociation.txPoll());
	}

	@Test
	public void testAspUpReceivedWhileASPIsAlreadyUp() throws Exception {
		// Test bug http://code.google.com/p/mobicents/issues/detail?id=2436

		TestAssociation testAssociation1 = (TestAssociation) this.transportManagement.addAssociation(null, 0, null, 0,
				"testAssoc1");

		// 4.3.4.1. ASP Up Procedures from http://tools.ietf.org/html/rfc4666
		RoutingContext rc = parmFactory.createRoutingContext(new long[] { 100 });

		DestinationPointCode[] dpc = new DestinationPointCode[] { parmFactory
				.createDestinationPointCode(123, (short) 0) };

		ServiceIndicators[] servInds = new ServiceIndicators[] { parmFactory.createServiceIndicators(new short[] { 3 }) };

		TrafficModeType trModType = parmFactory.createTrafficModeType(TrafficModeType.Override);
		LocalRKIdentifier lRkId = parmFactory.createLocalRKIdentifier(1);
		RoutingKey rKey = parmFactory.createRoutingKey(lRkId, rc, null, null, dpc, servInds, null);

		// As remAs = sgw.createAppServer("testas", rc, rKey, trModType);

		As remAs = serverM3UAMgmt.createAs("testas", Functionality.IPSP, ExchangeType.SE, IPSPType.SERVER, rc,
				trModType, null);

		AspFactory aspFactory = serverM3UAMgmt.createAspFactory("testasp", "testAssoc1");

		Asp remAsp = serverM3UAMgmt.assignAspToAs("testas", "testasp");
		FSM aspPeerFSM = remAsp.getPeerFSM();

		FSM asLocalFSM = remAs.getLocalFSM();

		// Check for Communication UP
		testAssociation1.signalCommUp();

		assertEquals(AspState.DOWN, this.getAspState(aspPeerFSM));

		// Peer sends ASP_UP
		M3UAMessageImpl message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP);
		aspFactory.read(message);

		assertEquals(AspState.INACTIVE, this.getAspState(aspPeerFSM));
		assertTrue(validateMessage(testAssociation1, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK, -1, -1));
		// also the AS should be INACTIVE now
		assertEquals(AsState.INACTIVE, this.getAsState(asLocalFSM));

		// Check for ASP_ACTIVE
		message = messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE);
		((ASPActiveImpl) message).setRoutingContext(rc);
		aspFactory.read(message);
		assertEquals(AspState.ACTIVE, this.getAspState(aspPeerFSM));
		assertTrue(validateMessage(testAssociation1, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE_ACK,
				-1, -1));
		// also the AS should be ACTIVE now
		assertEquals(AsState.ACTIVE, this.getAsState(asLocalFSM));

		// Check for ASP_UP received while ASP is already UP
		message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP);
		aspFactory.read(message);
		// The ASP Transitions to INACTIVE
		assertEquals(AspState.INACTIVE, this.getAspState(aspPeerFSM));
		// Receives ASP_UP Ack messages
		assertTrue(validateMessage(testAssociation1, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK, -1, -1));
		// As well as receives Error message
		assertTrue(validateMessage(testAssociation1, MessageClass.MANAGEMENT, MessageType.ERROR,
				ErrorCode.Unexpected_Message, 100));

		// also the AS should be PENDING now
		assertEquals(AsState.PENDING, this.getAsState(asLocalFSM));

		// Make sure we don't have any more
		assertNull(testAssociation1.txPoll());

	}

	@Test
	public void testPendingQueue() throws Exception {

		TestAssociation testAssociation1 = (TestAssociation) this.transportManagement.addAssociation(null, 0, null, 0,
				"testAssoc1");

		RoutingContext rc = parmFactory.createRoutingContext(new long[] { 100 });

		TrafficModeType trModType = parmFactory.createTrafficModeType(TrafficModeType.Override);

		As remAs = serverM3UAMgmt.createAs("testas", Functionality.IPSP, ExchangeType.SE, IPSPType.SERVER, rc,
				trModType, null);
		FSM asLocalFSM = remAs.getLocalFSM();

		AspFactory aspFactory = serverM3UAMgmt.createAspFactory("testasp", "testAssoc1");

		Asp remAsp = serverM3UAMgmt.assignAspToAs("testas", "testasp");
		FSM aspPeerFSM = remAsp.getPeerFSM();

		// Check for Communication UP
		testAssociation1.signalCommUp();

		assertEquals(AspState.DOWN, this.getAspState(aspPeerFSM));

		// Peer sends ASP_UP
		M3UAMessageImpl message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP);
		aspFactory.read(message);

		assertEquals(AspState.INACTIVE, this.getAspState(aspPeerFSM));
		assertTrue(validateMessage(testAssociation1, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK, -1, -1));
		// also the AS should be INACTIVE now
		assertEquals(AsState.INACTIVE, this.getAsState(asLocalFSM));

		// Peer sends ASP_ACTIVE
		message = messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE);
		((ASPActiveImpl) message).setRoutingContext(rc);
		aspFactory.read(message);
		assertEquals(AspState.ACTIVE, this.getAspState(aspPeerFSM));
		assertTrue(validateMessage(testAssociation1, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE_ACK,
				-1, -1));
		// also the AS should be ACTIVE now
		assertEquals(AsState.ACTIVE, this.getAsState(asLocalFSM));

		// Check for ASP_INACTIVE
		message = messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_INACTIVE);
		((ASPInactiveImpl) message).setRoutingContext(rc);
		aspFactory.read(message);
		assertEquals(AspState.INACTIVE, this.getAspState(aspPeerFSM));
		assertTrue(validateMessage(testAssociation1, MessageClass.ASP_TRAFFIC_MAINTENANCE,
				MessageType.ASP_INACTIVE_ACK, -1, -1));
		// also the AS should be PENDING now
		assertEquals(AsState.PENDING, this.getAsState(asLocalFSM));

		// Add PayloadData
		PayloadDataImpl payload = (PayloadDataImpl) messageFactory.createMessage(MessageClass.TRANSFER_MESSAGES,
				MessageType.PAYLOAD);
		ProtocolDataImpl p1 = (ProtocolDataImpl) parmFactory.createProtocolData(1408, 123, 3, 1, 0, 1, new byte[] { 1,
				2, 3, 4 });
		payload.setRoutingContext(rc);
		payload.setData(p1);

		remAs.write(payload);

		// Now bring UP the ASP
		message = messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE);
		((ASPActiveImpl) message).setRoutingContext(rc);
		aspFactory.read(message);

		assertTrue(validateMessage(testAssociation1, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE_ACK,
				-1, -1));
		// also the AS should be ACTIVE now
		assertEquals(AsState.ACTIVE, this.getAsState(asLocalFSM));

		// Also we should have PayloadData
		M3UAMessage payLoadTemp = testAssociation1.txPoll();
		assertNotNull(payLoadTemp);
		assertEquals(MessageClass.TRANSFER_MESSAGES, payLoadTemp.getMessageClass());
		assertEquals(MessageType.PAYLOAD, payLoadTemp.getMessageType());

		// Make sure we don't have any more
		assertNull(testAssociation1.txPoll());
	}

	/**
	 * 
	 * @param factory
	 * @param msgClass
	 * @param msgType
	 * @param type
	 *            The type for Notify message Or Error Code for Error Messages
	 * @param info
	 *            The Info for Notify message Or RoutingContext for Error
	 *            Message
	 * @return
	 */
	private boolean validateMessage(TestAssociation testAssociation, int msgClass, int msgType, int type, int info) {
		M3UAMessage message = testAssociation.txPoll();
		if (message == null) {
			return false;
		}

		if (message.getMessageClass() != msgClass || message.getMessageType() != msgType) {
			return false;
		}

		if (message.getMessageClass() == MessageClass.MANAGEMENT) {
			if (message.getMessageType() == MessageType.NOTIFY) {
				Status s = ((Notify) message).getStatus();
				if (s.getType() != type || s.getInfo() != info) {
					return false;
				} else {
					return true;
				}
			} else if (message.getMessageType() == MessageType.ERROR) {
				ErrorCode errCode = ((org.mobicents.protocols.ss7.m3ua.message.mgmt.Error) message).getErrorCode();
				if (errCode.getCode() != type) {
					return false;
				}

				RoutingContext rc = ((org.mobicents.protocols.ss7.m3ua.message.mgmt.Error) message).getRoutingContext();
				if (rc == null || rc.getRoutingContexts()[0] != info) {
					return false;
				}

				return true;

			}
			return false;
		} else {
			return true;
		}

	}

	class TestAssociation implements Association {

		private AssociationListener associationListener = null;
		private String name = null;
		private LinkedList<M3UAMessage> messageRxFromUserPart = new LinkedList<M3UAMessage>();

		TestAssociation(String name) {
			this.name = name;
		}

		M3UAMessage txPoll() {
			return messageRxFromUserPart.poll();
		}

		@Override
		public AssociationListener getAssociationListener() {
			return this.associationListener;
		}

		@Override
		public String getHostAddress() {
			return null;
		}

		@Override
		public int getHostPort() {
			return 0;
		}

		@Override
		public String getName() {
			return null;
		}

		@Override
		public String getPeerAddress() {
			return null;
		}

		@Override
		public int getPeerPort() {
			return 0;
		}

		@Override
		public String getServerName() {
			return null;
		}

		@Override
		public boolean isStarted() {
			return false;
		}

		@Override
		public void send(PayloadData payloadData) throws Exception {
			M3UAMessage m3uaMessage = messageFactory.createSctpMessage(payloadData.getData());
			this.messageRxFromUserPart.add(m3uaMessage);
		}

		@Override
		public void setAssociationListener(AssociationListener associationListener) {
			this.associationListener = associationListener;
		}

		public void signalCommUp() {
			this.associationListener.onCommunicationUp(this);
		}

		public void signalCommLost() {
			this.associationListener.onCommunicationLost(this);
		}

		@Override
		public IpChannelType getIpChannelType() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public AssociationType getAssociationType() {
			// TODO Auto-generated method stub
			return null;
		}

	}

	class TransportManagement implements Management {

		private FastMap<String, Association> associations = new FastMap<String, Association>();

		@Override
		public Association addAssociation(String hostAddress, int hostPort, String peerAddress, int peerPort,
				String assocName) throws Exception {
			TestAssociation testAssociation = new TestAssociation(assocName);
			this.associations.put(assocName, testAssociation);
			return testAssociation;
		}

		@Override
		public Server addServer(String serverName, String hostAddress, int port) throws Exception {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Association addServerAssociation(String peerAddress, int peerPort, String serverName, String assocName)
				throws Exception {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Association getAssociation(String assocName) throws Exception {
			return this.associations.get(assocName);
		}

		@Override
		public Map<String, Association> getAssociations() {
			return associations.unmodifiable();
		}

		@Override
		public int getConnectDelay() {
			return 0;
		}

		@Override
		public String getName() {
			return null;
		}

		@Override
		public List<Server> getServers() {
			return null;
		}

		@Override
		public int getWorkerThreads() {
			return 0;
		}

		@Override
		public boolean isSingleThread() {
			return false;
		}

		@Override
		public void removeAssociation(String assocName) throws Exception {

		}

		@Override
		public void removeServer(String serverName) throws Exception {

		}

		@Override
		public void setConnectDelay(int connectDelay) {

		}

		@Override
		public void setSingleThread(boolean arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setWorkerThreads(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void start() throws Exception {
			// TODO Auto-generated method stub

		}

		@Override
		public void startAssociation(String arg0) throws Exception {
			// TODO Auto-generated method stub

		}

		@Override
		public void startServer(String arg0) throws Exception {
			// TODO Auto-generated method stub

		}

		@Override
		public void stop() throws Exception {
			// TODO Auto-generated method stub

		}

		@Override
		public void stopAssociation(String arg0) throws Exception {
			// TODO Auto-generated method stub

		}

		@Override
		public void stopServer(String arg0) throws Exception {
			// TODO Auto-generated method stub

		}

		@Override
		public String getPersistDir() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setPersistDir(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public Association addAssociation(String arg0, int arg1, String arg2, int arg3, String arg4, IpChannelType arg5)
				throws Exception {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Server addServer(String arg0, String arg1, int arg2, IpChannelType arg3) throws Exception {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Association addServerAssociation(String arg0, int arg1, String arg2, String arg3, IpChannelType arg4)
				throws Exception {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void removeAllResourses() throws Exception {
			// TODO Auto-generated method stub

		}

	}
}
