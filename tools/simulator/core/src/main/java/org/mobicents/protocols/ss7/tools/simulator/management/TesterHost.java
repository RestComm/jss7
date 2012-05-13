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

package org.mobicents.protocols.ss7.tools.simulator.management;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPart;
import org.mobicents.protocols.ss7.sccp.SccpStack;
import org.mobicents.protocols.ss7.sccp.impl.router.Router;
import org.mobicents.protocols.ss7.tools.simulator.Stoppable;
import org.mobicents.protocols.ss7.tools.simulator.level1.M3uaMan;
import org.mobicents.protocols.ss7.tools.simulator.level2.SccpMan;
import org.mobicents.protocols.ss7.tools.simulator.level3.MapMan;
import org.mobicents.protocols.ss7.tools.simulator.testsussd.ProcessSsRequestAction;
import org.mobicents.protocols.ss7.tools.simulator.testsussd.TestUssdClientMan;
import org.mobicents.protocols.ss7.tools.simulator.testsussd.TestUssdServerMan;

import javolution.text.TextBuilder;
import javolution.xml.XMLBinding;
import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class TesterHost extends NotificationBroadcasterSupport implements TesterHostMBean, Stoppable {
	private static final Logger logger = Logger.getLogger(Router.class);

	private static final String TESTER_HOST_PERSIST_DIR_KEY = "testerhost.persist.dir";
	private static final String USER_DIR_KEY = "user.dir";
	public static String SOURCE_NAME = "HOST";
	public static String SS7_EVENT = "SS7Event";

	private static final String CLASS_ATTRIBUTE = "type";
	private static final String TAB_INDENT = "\t";
	private static final String PERSIST_FILE_NAME = "simulator.xml";

	private static final String INSTANCE_L1 = "instance_L1";
	private static final String INSTANCE_L2 = "instance_L2";
	private static final String INSTANCE_L3 = "instance_L3";
	private static final String INSTANCE_TESTTASK = "instance_TestTask";
	private static final String M3UA = "m3ua";
	private static final String SCCP = "sccp";
	private static final String MAP = "map";
	private static final String TEST_USSD_CLIENT = "testUssdClient";
	private static final String TEST_USSD_SERVER = "testUssdServer";

	private final String appName;
	private String persistDir = null;
	private final TextBuilder persistFile = TextBuilder.newInstance();
	private static final XMLBinding binding = new XMLBinding();

	// SETTINGS
	private boolean isStarted = false;
	private boolean needQuit = false;
	private boolean needStore = false;
	private Instance_L1 instance_L1 = new Instance_L1(Instance_L1.VAL_NO);
	private Instance_L2 instance_L2 = new Instance_L2(Instance_L2.VAL_NO);
	private Instance_L3 instance_L3 = new Instance_L3(Instance_L3.VAL_NO);
	private Instance_TestTask instance_TestTask = new Instance_TestTask(Instance_TestTask.VAL_NO);
	private long sequenceNumber = 0;

	// Layers
	private Stoppable instance_L1_B = null;
	private Stoppable instance_L2_B = null;
	private Stoppable instance_L3_B = null;
	private Stoppable instance_TestTask_B = null;

	// levels
	M3uaMan m3ua;
	SccpMan sccp;
	MapMan map;
	TestUssdClientMan testUssdClientMan;
	TestUssdServerMan testUssdServerMan;
	
	// testers

	public TesterHost(String appName) {
		this.appName = appName;

		this.m3ua = new M3uaMan(appName);
		this.m3ua.setTesterHost(this);

		this.sccp = new SccpMan(appName);
		this.sccp.setTesterHost(this);

		this.map = new MapMan(appName);
		this.map.setTesterHost(this);

		this.testUssdClientMan = new TestUssdClientMan(appName);
		this.testUssdClientMan.setTesterHost(this);

		this.testUssdServerMan = new TestUssdServerMan(appName);
		this.testUssdServerMan.setTesterHost(this);
		
		this.setupLog4j(appName);

		binding.setClassAttribute(CLASS_ATTRIBUTE);

		this.persistFile.clear();

		if (persistDir != null) {
			this.persistFile.append(persistDir).append(File.separator).append(this.appName).append("_").append(PERSIST_FILE_NAME);
		} else {
			this.persistFile.append(System.getProperty(TESTER_HOST_PERSIST_DIR_KEY, System.getProperty(USER_DIR_KEY))).append(File.separator).append(this.appName)
					.append("_").append(PERSIST_FILE_NAME);
		}

		try {
			this.load();
		} catch (FileNotFoundException e) {
			this.sendNotif(SOURCE_NAME, "Failed to load the Host state in file", e, true);
		}
	}

	public M3uaMan getM3uaMan() {
		return this.m3ua;
	}

	public SccpMan getSccpMan() {
		return this.sccp;
	}

	public MapMan getMapMan() {
		return this.map;
	}

	public TestUssdClientMan getTestUssdClientMan() {
		return this.testUssdClientMan;
	}

	public TestUssdServerMan getTestUssdServerMan() {
		return this.testUssdServerMan;
	}

	private void setupLog4j(String appName) {

//		InputStream inStreamLog4j = getClass().getResourceAsStream("/log4j.properties");

		String propFileName = appName + ".log4.properties";
		File f = new File(propFileName);
		if (f.exists()) {

			try {
				InputStream inStreamLog4j = new FileInputStream(f);
				Properties propertiesLog4j = new Properties();

				propertiesLog4j.load(inStreamLog4j);
				PropertyConfigurator.configure(propertiesLog4j);
			} catch (Exception e) {
				e.printStackTrace();
				BasicConfigurator.configure();
			}
		} else {
			BasicConfigurator.configure();
		}

//		logger.setLevel(Level.WARN);
		logger.debug("log4j configured");

	}

	public void sendNotif(String source, String msg, Throwable e, boolean showInConsole) {
		StringBuilder sb = new StringBuilder();
		for (StackTraceElement st : e.getStackTrace()) {
			if (sb.length() > 0)
				sb.append("\n");
			sb.append(st.toString());
		}
		this.doSendNotif(source, msg + " - " + e.toString(), sb.toString());

		if (showInConsole) {
			logger.error(msg, e);
		} else {
			logger.debug(msg, e);
		}
	}

	public void sendNotif(String source, String msg, String userData, boolean showInConsole) {

		this.doSendNotif(source, msg, userData);

		if (showInConsole) {
			logger.warn(msg);
		} else {
			logger.debug(msg);
		}
	}

	synchronized private void doSendNotif(String source, String msg, String userData) {
		Notification notif = new Notification(SS7_EVENT + "-" + source, "TesterHost", ++sequenceNumber, System.currentTimeMillis(), msg);
		notif.setUserData(userData);
		this.sendNotification(notif);
	}

	public boolean isNeedQuit() {
		return needQuit;
	}

	@Override
	public boolean isStarted() {
		return isStarted;
	}

	@Override
	public Instance_L1 getInstance_L1() {
		return instance_L1;
	}

	@Override
	public void setInstance_L1(Instance_L1 val) {
		instance_L1 = val;
		this.markStore();
	}

	@Override
	public Instance_L2 getInstance_L2() {
		return instance_L2;
	}

	@Override
	public void setInstance_L2(Instance_L2 val) {
		instance_L2 = val;
		this.markStore();
	}

	@Override
	public Instance_L3 getInstance_L3() {
		return instance_L3;
	}

	@Override
	public void setInstance_L3(Instance_L3 val) {
		instance_L3 = val;
		this.markStore();
	}

	@Override
	public Instance_TestTask getInstance_TestTask() {
		return instance_TestTask;
	}

	@Override
	public void setInstance_TestTask(Instance_TestTask val) {
		instance_TestTask = val;
		this.markStore();
	}

	@Override
	public String getInstance_L1_Value() {
		return instance_L1.toString();
	}

	@Override
	public String getInstance_L2_Value() {
		return instance_L2.toString();
	}

	@Override
	public String getInstance_L3_Value() {
		return instance_L3.toString();
	}

	@Override
	public String getInstance_TestTask_Value() {
		return instance_TestTask.toString();
	}

	@Override
	public String getState() {
		return TesterHost.SOURCE_NAME + ": " + (this.isStarted() ? "Started" : "Stopped");
	}

	@Override
	public String getL1State() {
		if (this.instance_L1_B != null)
			return this.instance_L1_B.getState();
		else
			return "";
	}

	@Override
	public String getL2State() {
		if (this.instance_L2_B != null)
			return this.instance_L2_B.getState();
		else
			return "";
	}

	@Override
	public String getL3State() {
		if (this.instance_L3_B != null)
			return this.instance_L3_B.getState();
		else
			return "";
	}

	@Override
	public String getTestTaskState() {
		if (this.instance_TestTask_B != null)
			return this.instance_TestTask_B.getState();
		else
			return "";
	}


	@Override
	public void start() {

		this.store();
		this.stop();

		// L1
		boolean started = false;
		Mtp3UserPart mtp3UserPart = null; 
		switch(this.instance_L1.intValue()){
		case Instance_L1.VAL_M3UA:
			this.instance_L1_B = this.m3ua;
			started = this.m3ua.start();
			mtp3UserPart = this.m3ua.getMtp3UserPart();
			break;
		case Instance_L1.VAL_DIALOGIC:
			// TODO Implement L1 = DialogicCard
			this.sendNotif(TesterHost.SOURCE_NAME, "Instance_L1.VAL_DIALOGIC has not been implemented yet", "", true);
			break;

		default:
			// TODO: implement others test tasks ...
			this.sendNotif(TesterHost.SOURCE_NAME, "Instance_L1." + this.instance_L1.toString() + " has not been implemented yet", "", true);
			break;
		}
		if (!started) {
			this.sendNotif(TesterHost.SOURCE_NAME, "Layer 1 has not started", "", true);
			this.stop();
			return;
		}

		// L2
		started = false;
		SccpStack sccpStack = null; 
		switch(this.instance_L2.intValue()){
		case Instance_L2.VAL_SCCP:
			if (mtp3UserPart == null) {
				this.sendNotif(TesterHost.SOURCE_NAME, "Error initializing SCCP: No Mtp3UserPart is defined at L1", "", true);
			} else {
				this.instance_L2_B = this.sccp;
				this.sccp.setMtp3UserPart(mtp3UserPart);
				started = this.sccp.start();
				sccpStack = this.sccp.getSccpStack();
			}
			break;
		case Instance_L2.VAL_ISUP:
			// TODO Implement L2 = ISUP
			this.sendNotif(TesterHost.SOURCE_NAME, "Instance_L2.VAL_ISUP has not been implemented yet", "", true);
			break;

		default:
			// TODO: implement others test tasks ...
			this.sendNotif(TesterHost.SOURCE_NAME, "Instance_L2." + this.instance_L2.toString() + " has not been implemented yet", "", true);
			break;
		}
		if (!started) {
			this.sendNotif(TesterHost.SOURCE_NAME, "Layer 2 has not started", "", true);
			this.stop();
			return;
		}

		// L3
		started = false;
		MapMan curMap = null;
		switch(this.instance_L3.intValue()){
		case Instance_L3.VAL_MAP:
			if (sccpStack == null) {
				this.sendNotif(TesterHost.SOURCE_NAME, "Error initializing TCAP+MAP: No SccpStack is defined at L2", "", true);
			} else {
				this.instance_L3_B = this.map;
				this.map.setSccpStack(sccpStack);
				started = this.map.start();
				curMap = this.map;
			}
			break;
		case Instance_L3.VAL_CAP:
			// TODO: implement CAP .......
			this.sendNotif(TesterHost.SOURCE_NAME, "Instance_L3.VAL_CAP has not been implemented yet", "", true);
			break;
		case Instance_L3.VAL_INAP:
			// TODO: implement INAP .......
			this.sendNotif(TesterHost.SOURCE_NAME, "Instance_L3.VAL_INAP has not been implemented yet", "", true);
			break;

		default:
			// TODO: implement others test tasks ...
			this.sendNotif(TesterHost.SOURCE_NAME, "Instance_L3." + this.instance_L3.toString() + " has not been implemented yet", "", true);
			break;
		}
		if (!started) {
			this.sendNotif(TesterHost.SOURCE_NAME, "Layer 3 has not started", "", true);
			this.stop();
			return;
		}

		// Testers
		started = false;
		switch(this.instance_TestTask.intValue()){
		case Instance_TestTask.VAL_USSD_TEST_CLIENT:
			if (curMap == null) {
				this.sendNotif(TesterHost.SOURCE_NAME, "Error initializing USSD_TEST_CLIENT: No MAP stack is defined at L3", "", true);
			} else {
				this.instance_TestTask_B = this.testUssdClientMan;
				this.testUssdClientMan.setMapMan(curMap);
				started = this.testUssdClientMan.start();
			}
			break;

		case Instance_TestTask.VAL_USSD_TEST_SERVER:
			if (curMap == null) {
				this.sendNotif(TesterHost.SOURCE_NAME, "Error initializing USSD_TEST_SERVER: No MAP stack is defined at L3", "", true);
			} else {
				this.instance_TestTask_B = this.testUssdServerMan;
				this.testUssdServerMan.setMapMan(curMap);
				started = this.testUssdServerMan.start();
			}
			break;

		default:
			// TODO: implement others test tasks ...
			this.sendNotif(TesterHost.SOURCE_NAME, "Instance_TestTask." + this.instance_TestTask.toString() + " has not been implemented yet", "", true);
			break;
		}
		if (!started) {
			this.sendNotif(TesterHost.SOURCE_NAME, "Testing task has not started", "", true);
			this.stop();
			return;
		}

		this.isStarted = true;
	}

	@Override
	public void stop() {

		this.isStarted = false;

		// L3
		if (this.instance_L3_B != null) {
			this.instance_L3_B.stop();
			this.instance_L3_B = null;
		}

		// L2
		if (this.instance_L2_B != null) {
			this.instance_L2_B.stop();
			this.instance_L2_B = null;
		}

		// L1
		if (this.instance_L1_B != null) {
			this.instance_L1_B.stop();
			this.instance_L1_B = null;
		}
	}

	@Override
	public void execute() {
		if (this.instance_L1_B != null) {
			this.instance_L1_B.execute();
		}
		if (this.instance_L2_B != null) {
			this.instance_L2_B.execute();
		}
	}

	@Override
	public void quit() {
		this.stop();
		this.store();
		this.needQuit = true;
	}

	@Override
	public void putInstance_L1Value(String val) {
		Instance_L1 x = Instance_L1.createInstance(val);
		if (x != null)
			this.setInstance_L1(x);
	}

	@Override
	public void putInstance_L2Value(String val) {
		Instance_L2 x = Instance_L2.createInstance(val);
		if (x != null)
			this.setInstance_L2(x);
	}

	@Override
	public void putInstance_L3Value(String val) {
		Instance_L3 x = Instance_L3.createInstance(val);
		if (x != null)
			this.setInstance_L3(x);
	}

	@Override
	public void putInstance_TestTaskValue(String val) {
		Instance_TestTask x = Instance_TestTask.createInstance(val);
		if (x != null)
			this.setInstance_TestTask(x);
	}

	public String getName() {
		return appName;
	}

	public String getPersistDir() {
		return persistDir;
	}

	public void setPersistDir(String persistDir) {
		this.persistDir = persistDir;
	}

	public void markStore() {
		needStore = true;
	}

	public void checkStore() {
		if (needStore) {
			needStore = false;
			this.store();
		}
	}

	synchronized public void store() {

		try {
			XMLObjectWriter writer = XMLObjectWriter.newInstance(new FileOutputStream(persistFile.toString()));
			writer.setBinding(binding);
			// Enables cross-references.
			// writer.setReferenceResolver(new XMLReferenceResolver());
			writer.setIndentation(TAB_INDENT);
			writer.write(this.instance_L1.toString(), INSTANCE_L1, String.class);
			writer.write(this.instance_L2.toString(), INSTANCE_L2, String.class);
			writer.write(this.instance_L3.toString(), INSTANCE_L3, String.class);
			writer.write(this.instance_TestTask.toString(), INSTANCE_TESTTASK, String.class);

			writer.write(this.m3ua, M3UA, M3uaMan.class);
			writer.write(this.sccp, SCCP, SccpMan.class);
			writer.write(this.map, MAP, MapMan.class);
			writer.write(this.testUssdClientMan, TEST_USSD_CLIENT, TestUssdClientMan.class);
			writer.write(this.testUssdServerMan, TEST_USSD_SERVER, TestUssdServerMan.class);

//			writer.write(remoteSpcs, REMOTE_SPC, FastMap.class);
//			writer.write(concernedSpcs, CONCERNED_SPC, FastMap.class);

			writer.close();
		} catch (Exception e) {
			this.sendNotif(SOURCE_NAME, "Error while persisting the Host state in file", e, true);
		}
	}

	private void load() throws FileNotFoundException {

		XMLObjectReader reader = null;
		try {
			reader = XMLObjectReader.newInstance(new FileInputStream(persistFile.toString()));

			reader.setBinding(binding);
			this.instance_L1 = Instance_L1.createInstance(reader.read(INSTANCE_L1, String.class));
			this.instance_L2 = Instance_L2.createInstance(reader.read(INSTANCE_L2, String.class));
			this.instance_L3 = Instance_L3.createInstance(reader.read(INSTANCE_L3, String.class));
			this.instance_TestTask = Instance_TestTask.createInstance(reader.read(INSTANCE_TESTTASK, String.class));

			M3uaMan _m3ua = reader.read(M3UA, M3uaMan.class);
			this.m3ua.setSctpLocalHost(_m3ua.getSctpLocalHost());
			this.m3ua.setSctpLocalPort(_m3ua.getSctpLocalPort());
			this.m3ua.setSctpRemoteHost(_m3ua.getSctpRemoteHost());
			this.m3ua.setSctpRemotePort(_m3ua.getSctpRemotePort());
			this.m3ua.setSctpIPChannelType(_m3ua.getSctpIPChannelType());
			this.m3ua.setSctpIsServer(_m3ua.isSctpIsServer());
			this.m3ua.doSetExtraHostAddresses(_m3ua.getSctpExtraHostAddresses());
			this.m3ua.setM3uaFunctionality(_m3ua.getM3uaFunctionality());
			this.m3ua.setM3uaIPSPType(_m3ua.getM3uaIPSPType());
			this.m3ua.setM3uaExchangeType(_m3ua.getM3uaExchangeType());
			this.m3ua.setM3uaDpc(_m3ua.getM3uaDpc());
			this.m3ua.setM3uaOpc(_m3ua.getM3uaOpc());
			this.m3ua.setM3uaSi(_m3ua.getM3uaSi());

			SccpMan _sccp = reader.read(SCCP, SccpMan.class);
			this.sccp.setDpc(_sccp.getDpc());
			this.sccp.setOpc(_sccp.getOpc());
			this.sccp.setNi(_sccp.getNi());
			this.sccp.setRemoteSsn(_sccp.getRemoteSsn());

			MapMan _tcap = reader.read(MAP, MapMan.class);
			this.map.setLocalPc(_tcap.getLocalPc());
			this.map.setRemotePc(_tcap.getRemotePc());
			this.map.setLocalSsn(_tcap.getLocalSsn());
			this.map.setRemoteSsn(_tcap.getRemoteSsn());
			this.map.setOrigReference(_tcap.getOrigReference());
			this.map.setDestReference(_tcap.getDestReference());

			TestUssdClientMan _TestUssdClientMan = reader.read(TEST_USSD_CLIENT, TestUssdClientMan.class);
			this.testUssdClientMan.setMsisdnAddress(_TestUssdClientMan.getMsisdnAddress());
			this.testUssdClientMan.setMsisdnAddressNature(_TestUssdClientMan.getMsisdnAddressNature());
			this.testUssdClientMan.setMsisdnNumberingPlan(_TestUssdClientMan.getMsisdnNumberingPlan());
			this.testUssdClientMan.setDataCodingScheme(_TestUssdClientMan.getDataCodingScheme());
			this.testUssdClientMan.setAlertingPattern(_TestUssdClientMan.getAlertingPattern());

			TestUssdServerMan _TestUssdServerMan = reader.read(TEST_USSD_SERVER, TestUssdServerMan.class);
			this.testUssdServerMan.setMsisdnAddress(_TestUssdServerMan.getMsisdnAddress());
			this.testUssdServerMan.setMsisdnAddressNature(_TestUssdServerMan.getMsisdnAddressNature());
			this.testUssdServerMan.setMsisdnNumberingPlan(_TestUssdServerMan.getMsisdnNumberingPlan());
			this.testUssdServerMan.setDataCodingScheme(_TestUssdServerMan.getDataCodingScheme());
			this.testUssdServerMan.setAlertingPattern(_TestUssdServerMan.getAlertingPattern());
			this.testUssdServerMan.setProcessSsRequestAction(_TestUssdServerMan.getProcessSsRequestAction());
			this.testUssdServerMan.setAutoResponseString(_TestUssdServerMan.getAutoResponseString());
			this.testUssdServerMan.setAutoUnstructured_SS_RequestString(_TestUssdServerMan.getAutoUnstructured_SS_RequestString());

//			remoteSsns = reader.read(REMOTE_SSN, FastMap.class);
//			remoteSpcs = reader.read(REMOTE_SPC, FastMap.class);
//			concernedSpcs = reader.read(CONCERNED_SPC, FastMap.class);
		} catch (Exception ex) {
			this.sendNotif(SOURCE_NAME, "Error while reading the Host state in file", ex, true);
		}
	}
}


