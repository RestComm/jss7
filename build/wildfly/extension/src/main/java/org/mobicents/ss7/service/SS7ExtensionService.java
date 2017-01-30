package org.mobicents.ss7.service;

import javolution.util.FastList;
import javolution.util.FastMap;
import org.jboss.logging.Logger;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;
import org.mobicents.protocols.api.Management;
import org.mobicents.protocols.sctp.netty.NettySctpManagementImpl;
import org.mobicents.protocols.ss7.cap.CAPStackImpl;
import org.mobicents.protocols.ss7.isup.impl.CircuitManagerImpl;
import org.mobicents.protocols.ss7.isup.impl.ISUPStackImpl;
import org.mobicents.protocols.ss7.m3ua.impl.M3UAManagementImpl;
import org.mobicents.protocols.ss7.m3ua.impl.oam.M3UAShellExecutor;
import org.mobicents.protocols.ss7.m3ua.impl.oam.SCTPShellExecutor;
import org.mobicents.protocols.ss7.map.MAPStackImpl;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPart;
import org.mobicents.protocols.ss7.mtp.RoutingLabelFormat;
import org.mobicents.protocols.ss7.oam.common.alarm.AlarmProvider;
import org.mobicents.protocols.ss7.oam.common.jmxss7.Ss7Management;
import org.mobicents.protocols.ss7.oam.common.m3ua.M3uaManagementJmx;
import org.mobicents.protocols.ss7.oam.common.sccp.SccpManagementJmx;
import org.mobicents.protocols.ss7.oam.common.sctp.SctpManagementJmx;
import org.mobicents.protocols.ss7.oam.common.statistics.CounterProviderManagement;
import org.mobicents.protocols.ss7.oam.common.tcap.TcapManagementJmx;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.oam.SccpExecutor;
import org.mobicents.protocols.ss7.scheduler.DefaultClock;
import org.mobicents.protocols.ss7.scheduler.Scheduler;
import org.mobicents.protocols.ss7.tcap.TCAPStackImpl;
import org.mobicents.protocols.ss7.tcap.oam.TCAPExecutor;
import org.mobicents.ss7.SS7Service;
import org.mobicents.ss7.management.console.ShellExecutor;
import org.mobicents.ss7.management.console.ShellServer;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class SS7ExtensionService implements Service<SS7ExtensionService> {

  Map<String, String> mbeanProp = new HashMap<String, String>();

  public void addMbean(String mbeanName) {
    log.info("addProperty: "+mbeanName);
  }

  public void addProperty(String mbeanName, String propertyName, String propertyValue) {
    log.info("addProperty: "+propertyName);
    this.mbeanProp.put(mbeanName+"::"+propertyName, propertyValue);
  }

  private String getPropertyString(String mbeanName, String propertyName, String defaultValue) {
    String result = mbeanProp.get(mbeanName+"::"+propertyName);
    return (result == null) ? defaultValue : result;
  }

  private int getPropertyInt(String mbeanName, String propertyName, int defaultValue) {
    String result = mbeanProp.get(mbeanName+"::"+propertyName);
    return (result == null) ? defaultValue : Integer.parseInt(result);
  }

  public static final SS7ExtensionService INSTANCE = new SS7ExtensionService();

  private final Logger log = Logger.getLogger(SS7ExtensionService.class);

  public static ServiceName getServiceName() {
    return ServiceName.of("restcomm","ss7");
  }

  private final InjectedValue<MBeanServer> mbeanServer = new InjectedValue<MBeanServer>();
  public InjectedValue<MBeanServer> getMbeanServer() {
    return mbeanServer;
  }
  private final LinkedList<String> registeredMBeans = new LinkedList<String>();


  @Override
  public SS7ExtensionService getValue() throws IllegalStateException, IllegalArgumentException {
    return this;
  }

  @Override
  public void start(StartContext context) throws StartException {
    log.info("Starting SS7ExtensionService");

    //
    NettySctpManagementImpl sctpManagement = null;
    StandardMBean sctpManagementMBean = null;
    try {
      sctpManagement = new NettySctpManagementImpl("SCTPManagement");
      sctpManagement.setPersistDir(getPropertyString("SCTPManagement", "persistDir", "DEFAULT"));

      sctpManagementMBean = new StandardMBean(sctpManagement, org.mobicents.protocols.api.Management.class);
    } catch (Exception e) {
      log.warn("SCTPManagement MBean creating is failed: "+e);
    }

    //
    SCTPShellExecutor sctpShellExecutor = new SCTPShellExecutor();
    StandardMBean sctpShellExecutorMBean = null;
    try {
      Map<String, Management> sctpManagements = new FastMap<String, Management>();
      sctpManagements.put("SCTPManagement", sctpManagement);
      sctpShellExecutor.setSctpManagements(sctpManagements);

      sctpShellExecutorMBean = new StandardMBean(sctpShellExecutor, org.mobicents.ss7.management.console.ShellExecutor.class);
    } catch (Exception e) {
      log.warn("SCTPShellExecutor MBean creating is failed: "+e);
    }

    //
    RoutingLabelFormat routingLabelFormat = RoutingLabelFormat.getInstance("ITU");

    //
    M3UAManagementImpl m3uaManagement = null;
    StandardMBean m3uaManagementMBean = null;
    try {
      m3uaManagement = new M3UAManagementImpl("Mtp3UserPart", "Restcomm-jSS7");
      m3uaManagement.setPersistDir(getPropertyString("Mtp3UserPart", "persistDir", "DEFAULT"));
      m3uaManagement.setRoutingLabelFormat(routingLabelFormat);
      m3uaManagement.setTransportManagement(sctpManagement);
      m3uaManagement.setMaxSequenceNumber(getPropertyInt("Mtp3UserPart", "maxSequenceNumber", 256));
      m3uaManagement.setMaxAsForRoute(getPropertyInt("Mtp3UserPart", "maxAsForRoute", 2));
      m3uaManagement.setDeliveryMessageThreadCount(getPropertyInt("Mtp3UserPart", "deliveryMessageThreadCount", 1));

      m3uaManagementMBean = new StandardMBean(m3uaManagement, org.mobicents.protocols.ss7.m3ua.M3UAManagement.class);
    } catch (Exception e) {
      log.warn("Mtp3UserPart MBean creating is failed: "+e);
    }

    //
    M3UAShellExecutor m3uaShellExecutor = null;
    StandardMBean m3uaShellExecutorMBean = null;
    try {
      Map<String, M3UAManagementImpl> m3uaManagements = new FastMap<String, M3UAManagementImpl>();
      m3uaManagements.put("Mtp3UserPart", m3uaManagement);
      m3uaShellExecutor = new M3UAShellExecutor();
      m3uaShellExecutor.setM3uaManagements(m3uaManagements);

      m3uaShellExecutorMBean = new StandardMBean(m3uaShellExecutor, org.mobicents.ss7.management.console.ShellExecutor.class);
    } catch (Exception e) {
      log.warn("M3UAShellExecutor MBean creating is failed: "+e);
    }

    //
    DefaultClock ss7Clock = null;
    StandardMBean ss7ClockMBean = null;
    try {
      ss7Clock = new DefaultClock();

      ss7ClockMBean = new StandardMBean(ss7Clock, org.mobicents.protocols.ss7.scheduler.Clock.class);
    } catch (Exception e) {
      log.warn("SS7Clock MBean creating is failed: "+e);
    }

    //
    Scheduler schedulerMBean = null;
    try {
      schedulerMBean = new Scheduler();
      schedulerMBean.setClock(ss7Clock);
    } catch (Exception e) {
      log.warn("SS7Scheduler MBean creating is failed: "+e);
    }

    //
    SccpStackImpl sccpStack = null;
    StandardMBean sccpStackMBean = null;
    try {
      Map<Integer, Mtp3UserPart> mtp3UserParts = new FastMap<Integer, Mtp3UserPart>();
      mtp3UserParts.put(1, m3uaManagement);
      sccpStack = new SccpStackImpl("SccpStack");
      sccpStack.setPersistDir(getPropertyString("SccpStack", "persistDir", "DEFAULT"));
      sccpStack.setMtp3UserParts(mtp3UserParts);

      sccpStackMBean = new StandardMBean(sccpStack, org.mobicents.protocols.ss7.sccp.SccpStack.class);
    } catch (Exception e) {
      log.warn("SccpStack MBean creating is failed: "+e);
    }

    //
    SccpExecutor sccpExecutor = null;
    StandardMBean sccpExecutorMBean = null;
    try {
      Map<String, SccpStackImpl> sccpStacks = new FastMap<String, SccpStackImpl>();
      sccpStacks.put("SccpStack", sccpStack);
      sccpExecutor = new SccpExecutor();
      sccpExecutor.setSccpStacks(sccpStacks);

      sccpExecutorMBean = new StandardMBean(sccpExecutor, org.mobicents.ss7.management.console.ShellExecutor.class);
    } catch (Exception e) {
      log.warn("SccpExecutor MBean creating is failed: "+e);
    }

    //
    TCAPStackImpl tcapStackMap = null;
    StandardMBean tcapStackMapMBean = null;
    try {
      tcapStackMap = new TCAPStackImpl("TcapStackMap", sccpStack.getSccpProvider(), 8);
      tcapStackMap.setPersistDir(getPropertyString("TcapStackMap", "persistDir", "DEFAULT"));
      tcapStackMap.setPreviewMode(false);

      tcapStackMapMBean = new StandardMBean(tcapStackMap, org.mobicents.protocols.ss7.tcap.api.TCAPStack.class);
    } catch (Exception e) {
      log.warn("TcapStackMap MBean creating is failed: "+e);
    }

    //
    TCAPStackImpl tcapStackCap = null;
    StandardMBean tcapStackCapMBean = null;
    try {
      tcapStackCap = new TCAPStackImpl("TcapStackCap", sccpStack.getSccpProvider(), 146);
      tcapStackCap.setPersistDir(getPropertyString("TcapStackCap", "persistDir", "DEFAULT"));
      tcapStackCap.setPreviewMode(false);

      tcapStackCapMBean = new StandardMBean(tcapStackCap, org.mobicents.protocols.ss7.tcap.api.TCAPStack.class);
    } catch (Exception e) {
      log.warn("TcapStackCap MBean creating is failed: "+e);
    }

    //
    TCAPStackImpl tcapStack = null;
    StandardMBean tcapStackMBean = null;
    try {
      tcapStack = new TCAPStackImpl("TcapStackCap", sccpStack.getSccpProvider(), 9);
      tcapStack.setPersistDir(getPropertyString("TcapStack", "persistDir", "DEFAULT"));
      tcapStack.setPreviewMode(false);

      tcapStackMBean = new StandardMBean(tcapStack, org.mobicents.protocols.ss7.tcap.api.TCAPStack.class);
    } catch (Exception e) {
      log.warn("TcapStack MBean creating is failed: "+e);
    }

    //
    TCAPExecutor tcapExecutor = null;
    StandardMBean tcapExecutorMBean = null;
    try {
      Map<String, TCAPStackImpl> tcapStacks = new FastMap<String, TCAPStackImpl>();
      tcapStacks.put("TcapStackMap", tcapStackMap);
      tcapStacks.put("TcapStackCap", tcapStackCap);
      tcapStacks.put("TcapStack", tcapStack);
      tcapExecutor = new TCAPExecutor();
      tcapExecutor.setTcapStacks(tcapStacks);

      tcapExecutorMBean = new StandardMBean(tcapExecutor, org.mobicents.ss7.management.console.ShellExecutor.class);
    } catch (Exception e) {
      log.warn("TcapExecutor MBean creating is failed: "+e);
    }

    //
    ShellServer shellExecutorMBean = null;
    try {
      FastList<ShellExecutor> shellExecutors = new FastList<ShellExecutor>();
      shellExecutors.add(sccpExecutor);
      shellExecutors.add(m3uaShellExecutor);
      shellExecutors.add(sctpShellExecutor);
      shellExecutors.add(tcapExecutor);
      shellExecutorMBean = new ShellServer(schedulerMBean, shellExecutors);
      shellExecutorMBean.setAddress(getPropertyString("ShellExecutor", "address", "${jboss.bind.address}"));
      shellExecutorMBean.setPort(getPropertyInt("ShellExecutor", "port", 3435));
      shellExecutorMBean.setSecurityDomain(getPropertyString("ShellExecutor", "securityDomain", "java:/jaas/jmx-console"));
    } catch (Exception e) {
      log.warn("ShellExecutor MBean creating is failed: "+e);
    }

    //
    CAPStackImpl capStack = null;
    StandardMBean capStackMBean = null;
    try {
      capStack = new CAPStackImpl("CapStack", tcapStackCap.getProvider());
      capStackMBean = new StandardMBean(capStack, org.mobicents.protocols.ss7.cap.api.CAPStack.class);
    } catch (Exception e) {
      log.warn("CapStack MBean creating is failed: "+e);
    }

    //
    MAPStackImpl mapStack = null;
    StandardMBean mapStackMBean = null;
    try {
      mapStack = new MAPStackImpl("MapStack", tcapStackMap.getProvider());
      mapStackMBean = new StandardMBean(mapStack, org.mobicents.protocols.ss7.map.api.MAPStack.class);
    } catch (Exception e) {
      log.warn("MapStack MBean creating is failed: "+e);
    }

    //
    CircuitManagerImpl circuitManager = null;
    StandardMBean circuitManagerMBean = null;
    try {
      circuitManager = new CircuitManagerImpl();
      circuitManagerMBean = new StandardMBean(circuitManager, org.mobicents.protocols.ss7.isup.CircuitManager.class);
    } catch (Exception e) {
      log.warn("CircuitManager MBean creating is failed: "+e);
    }

    //
    ISUPStackImpl isupStack = null;
    StandardMBean isupStackMBean = null;
    try {
      isupStack = new ISUPStackImpl(schedulerMBean, 22234, 2);
      isupStack.setMtp3UserPart(m3uaManagement);
      isupStack.setCircuitManager(circuitManager);
      isupStackMBean = new StandardMBean(isupStack, org.mobicents.protocols.ss7.isup.ISUPStack.class);
    } catch (Exception e) {
      log.warn("IsupStack MBean creating is failed: "+e);
    }

    //
    SS7Service tcapSS7Service = new SS7Service("TCAP");
    tcapSS7Service.setJndiName("java:/restcomm/ss7/tcap");
    tcapSS7Service.setStack(tcapStack.getProvider());

    //
    SS7Service capSS7Service = new SS7Service("CAP");
    capSS7Service.setJndiName("java:/restcomm/ss7/cap");
    capSS7Service.setStack(capStack.getCAPProvider());

    //
    SS7Service mapSS7Service = new SS7Service("MAP");
    mapSS7Service.setJndiName("java:/restcomm/ss7/map");
    mapSS7Service.setStack(mapStack.getMAPProvider());

    //
    SS7Service isupSS7Service = new SS7Service("ISUP");
    isupSS7Service.setJndiName("java:/restcomm/ss7/isup");
    isupSS7Service.setStack(isupStack.getIsupProvider());


    //
    Ss7Management ss7ManagementMBean = null;
    ss7ManagementMBean = new Ss7Management();
    ss7ManagementMBean.setAgentId(getPropertyString("Ss7Management", "agentId", "jboss"));

    //
    AlarmProvider restcommAlarmManagementMBean = null;
    restcommAlarmManagementMBean = new AlarmProvider(ss7ManagementMBean, ss7ManagementMBean);

    //
    CounterProviderManagement restcommStatisticManagementMBean = null;
    restcommStatisticManagementMBean = new CounterProviderManagement(ss7ManagementMBean);
    restcommStatisticManagementMBean.setPersistDir(getPropertyString("RestcommStatisticManagement", "persistDir", "DEFAULT"));

    //
    SctpManagementJmx restcommSctpManagementMBean = null;
    restcommSctpManagementMBean = new SctpManagementJmx(ss7ManagementMBean, sctpManagement);

    //
    M3uaManagementJmx restcommM3uaManagementMBean = null;
    restcommM3uaManagementMBean = new M3uaManagementJmx(ss7ManagementMBean, m3uaManagement);

    //
    SccpManagementJmx restcommSccpManagementMBean = null;
    restcommSccpManagementMBean = new SccpManagementJmx(ss7ManagementMBean, sccpStack);

    //
    TcapManagementJmx restcommTcapManagementMBean = null;
    restcommTcapManagementMBean = new TcapManagementJmx(ss7ManagementMBean, tcapStack);

    //
    TcapManagementJmx restcommTcapCapManagementMBean = null;
    restcommTcapCapManagementMBean = new TcapManagementJmx(ss7ManagementMBean, tcapStackCap);

    //
    TcapManagementJmx restcommTcapMapManagementMBean = null;
    restcommTcapMapManagementMBean = new TcapManagementJmx(ss7ManagementMBean, tcapStackMap);


    ////

    //// register MBeans
    registerMBean(sctpManagementMBean, "org.mobicents.ss7:name=SCTPManagement");
    registerMBean(sctpShellExecutorMBean, "org.mobicents.ss7:name=SCTPShellExecutor");

    registerMBean(m3uaManagementMBean, "org.mobicents.ss7:name=Mtp3UserPart");
    registerMBean(m3uaShellExecutorMBean, "org.mobicents.ss7:name=M3UAShellExecutor");

    registerMBean(ss7ClockMBean, "org.mobicents.ss7:name=SS7Clock");
    registerMBean(schedulerMBean, "org.mobicents.ss7:name=SS7Scheduler");

    registerMBean(sccpStackMBean, "org.mobicents.ss7:name=SccpStack");
    registerMBean(sccpExecutorMBean, "org.mobicents.ss7:name=SccpExecutor");

    registerMBean(tcapStackMapMBean, "org.mobicents.ss7:name=TcapStackMap");
    registerMBean(tcapStackCapMBean, "org.mobicents.ss7:name=TcapStackCap");
    registerMBean(tcapStackMBean, "org.mobicents.ss7:name=TcapStack");
    registerMBean(tcapExecutorMBean, "org.mobicents.ss7:name=TcapExecutor");

    registerMBean(shellExecutorMBean, "org.mobicents.ss7:name=ShellExecutor");

    registerMBean(circuitManagerMBean, "org.mobicents.ss7:name=CircuitManager");
    registerMBean(capStackMBean, "org.mobicents.ss7:name=CapStack");
    registerMBean(mapStackMBean, "org.mobicents.ss7:name=MapStack");
    registerMBean(isupStackMBean, "org.mobicents.ss7:name=IsupStack");

    registerMBean(tcapSS7Service, "org.mobicents.ss7:service=TCAPSS7Service");
    registerMBean(capSS7Service, "org.mobicents.ss7:service=CAPSS7Service");
    registerMBean(mapSS7Service, "org.mobicents.ss7:service=MAPSS7Service");
    registerMBean(isupSS7Service, "org.mobicents.ss7:service=ISUPSS7Service");

    registerMBean(ss7ManagementMBean, "org.mobicents.ss7:service=Ss7Management");

    registerMBean(restcommAlarmManagementMBean, "org.mobicents.ss7:name=RestcommAlarmManagement");
    registerMBean(restcommStatisticManagementMBean, "org.mobicents.ss7:name=RestcommStatisticManagement");
    registerMBean(restcommSctpManagementMBean, "org.mobicents.ss7:name=RestcommSctpManagement");
    registerMBean(restcommM3uaManagementMBean, "org.mobicents.ss7:name=RestcommM3uaManagement");
    registerMBean(restcommSccpManagementMBean, "org.mobicents.ss7:name=RestcommSccpManagement");

    registerMBean(restcommTcapManagementMBean, "org.mobicents.ss7:name=RestcommTcapManagement");
    registerMBean(restcommTcapCapManagementMBean, "org.mobicents.ss7:name=RestcommTcapCapManagement");
    registerMBean(restcommTcapMapManagementMBean, "org.mobicents.ss7:name=RestcommTcapMapManagement");


    ////
    //
    try {
      tcapSS7Service.create();
      tcapSS7Service.start();
    } catch (Exception e) {
      e.printStackTrace();
    }

    //
    try {
      capSS7Service.create();
      capSS7Service.start();
    } catch (Exception e) {
      e.printStackTrace();
    }

    //
    try {
      mapSS7Service.create();
      mapSS7Service.start();
    } catch (Exception e) {
      e.printStackTrace();
    }

    //
    try {
      isupSS7Service.create();
      isupSS7Service.start();
    } catch (Exception e) {
      e.printStackTrace();
    }

    //
    try {
      ss7ManagementMBean.start();
    } catch (Exception e) {
      e.printStackTrace();
    }

    //
    try {
      restcommAlarmManagementMBean.start();
    } catch (Exception e) {
      e.printStackTrace();
    }

    //
    try {
      restcommStatisticManagementMBean.start();
    } catch (Exception e) {
      e.printStackTrace();
    }

    //
    try {
      restcommSctpManagementMBean.start();
    } catch (Exception e) {
      e.printStackTrace();
    }

    //
    try {
      restcommM3uaManagementMBean.start();
    } catch (Exception e) {
      e.printStackTrace();
    }

    //
    try {
      restcommSccpManagementMBean.start();
    } catch (Exception e) {
      e.printStackTrace();
    }

    //
    try {
      restcommTcapManagementMBean.start();
    } catch (Exception e) {
      e.printStackTrace();
    }

    //
    try {
      restcommTcapCapManagementMBean.start();
    } catch (Exception e) {
      e.printStackTrace();
    }

    //
    try {
      restcommTcapMapManagementMBean.start();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  @Override
  public void stop(StopContext context) {
    log.info("Stopping SS7ExtensionService");

    while(!registeredMBeans.isEmpty()) {
      unregisterMBean(registeredMBeans.pop());
    }
  }

  //private void registerStandardMBean(Object mBean, Class<Object> mInterface, String name) throws StartException {
  //  try {
  //    StandardMBean standardMBean = new StandardMBean(mBean, mInterface);
  //    getMbeanServer().getValue().registerMBean(standardMBean, new ObjectName(name));
  //  } catch (Throwable e) {
  //    throw new StartException(e);
  //  }
  //  registeredMBeans.push(name);
  //}

  private void registerMBean(Object mBean, String name) throws StartException {
    if (mBean == null) {
      return;
    }

    try {
      getMbeanServer().getValue().registerMBean(mBean, new ObjectName(name));
    } catch (Throwable e) {
      throw new StartException(e);
    }
    registeredMBeans.push(name);
  }

  private void unregisterMBean(String name) {
    try {
      getMbeanServer().getValue().unregisterMBean(new ObjectName(name));
    } catch (Throwable e) {
      log.error("failed to unregister mbean", e);
    }
  }
}
