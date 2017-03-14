package org.mobicents.protocols.ss7.map.load.mapp;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.SimpleLayout;
import org.mobicents.protocols.ss7.map.api.MAPStack;
import org.mobicents.protocols.ss7.map.load.TestHarness;
import org.mobicents.protocols.ss7.sccp.NetworkIdState;

public class MAPp {

    private static final Logger logger = Logger.getLogger("mapp");

    private final String LOG_FILE_NAME = "log.file.name";
    private String logFileName = "maplog.txt";

    static final String MAP_DIALOG_LISTENER = "MAP_DIALOG_LISTENER";
    static final String MAP_SUPPL_LISTENER = "MAP_SUPPL_LISTENER";

    private List<StackInitializer> initializersList = new ArrayList();

    private MAPpContext ctx = new MAPpContext();

    ScheduledFuture<?> csvFuture;
    ScheduledFuture<?> congestionFuture;
    ScheduledFuture<?> trafficFuture;

    public MAPp(Properties props) throws Exception {
        try {

            InputStream inStreamLog4j = TestHarness.class.getResourceAsStream("/log4j.properties");

            System.out.println("Input Stream = " + inStreamLog4j);

            Properties propertiesLog4j = new Properties();
            try {
                propertiesLog4j.load(inStreamLog4j);
                PropertyConfigurator.configure(propertiesLog4j);
            } catch (Exception e) {
                e.printStackTrace();
                BasicConfigurator.configure();
            }

            logger.debug("log4j configured");

            String lf = System.getProperties().getProperty(LOG_FILE_NAME);
            if (lf != null) {
                logFileName = lf;
            }

            // If already created a print writer then just use it.
            try {
                logger.addAppender(new FileAppender(new SimpleLayout(), logFileName));
            } catch (FileNotFoundException fnfe) {

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }

        ctx.scenario = new USSDScenario();
        //order is important
        initializersList.add(new SCCPInitializer());
        initializersList.add(new M3UAInitializer());
        initializersList.add(new SCCPInitializer());
        initializersList.add(new MAPInitializer());
        for (int i = 0; i < initializersList.size(); i++) {
            initializersList.get(i).init(ctx);
        }
        csvFuture = ctx.executor.scheduleAtFixedRate(new StatsPrinter(), 0, 15, TimeUnit.SECONDS);
        congestionFuture = ctx.executor.scheduleAtFixedRate(new CongestionControl(), 0, 15, TimeUnit.SECONDS);
        String scenarioClassName = ctx.props.getProperty("mapp.scenarioClassName");
        Class scenarioClass = this.getClass().getClassLoader().loadClass(scenarioClassName);
        ctx.scenario = (MAPScenario) scenarioClass.newInstance();
        ctx.scenario.init(ctx);
        ctx.switchToState(MAPpState.INITIATED);
    }

    private static final String CSV_SEPARATOR = ";";

    class StatsPrinter implements Runnable {

        @Override
        public void run() {
            StringBuilder csvLine = new StringBuilder();
            Map<String, AtomicLong> retrieveAndResetCurrentCounters = ctx.retrieveAndResetCurrentCounters();
            for (String counterKey : retrieveAndResetCurrentCounters.keySet()) {
                csvLine.append(retrieveAndResetCurrentCounters.get(counterKey));
                csvLine.append(CSV_SEPARATOR);
            }
            System.out.println(csvLine.toString());
        }

    }

    //http://tldp.org/LDP/abs/html/exitcodes.html
    private static final int STACK_ERROR_STATUS = 64;

    public static void main(String[] args) {
        Properties props = new Properties();
        //TODO props.load(inStream);
        MAPp mapp;
        try {
            mapp = new MAPp(props);
            mapp.generateTraffic();
            mapp.waitForTrafficToComplete();
        } catch (Exception ex) {
            logger.error("unable to init", ex);
            System.exit(STACK_ERROR_STATUS);
        }

    }

    public void generateTraffic() {
        if (trafficFuture != null) {
            trafficFuture.cancel(false);
        }
        trafficFuture = ctx.executor.scheduleAtFixedRate(new CreateDialog(), 0, 100, TimeUnit.SECONDS);
    }

    public void waitForTrafficToComplete() throws InterruptedException {
        while (!ctx.getCurrentState().equals(MAPpState.COMPLETED) ) {
            Thread.sleep(5000);
        }
        ctx.switchToState(MAPpState.STOPPING);
        trafficFuture.cancel(false);
        csvFuture.cancel(false);
        congestionFuture.cancel(false);
        //wait for all dialogs to complete
        ctx.executor.awaitTermination(120, TimeUnit.SECONDS);
        ctx.switchToState(MAPpState.STOPPED);
    }

    class CongestionControl implements Runnable {

        @Override
        public void run() {
            MAPStack mapStack = (MAPStack) ctx.data.get(MAPInitializer.STACK_ID);
            NetworkIdState networkIdState = mapStack.getMAPProvider().getNetworkIdState(0);
            if (!(networkIdState == null || networkIdState.isAvailavle() && networkIdState.getCongLevel() == 0)) {
                // congestion or unavailable
                ctx.logger.warn("Outgoing congestion control: MAP load test client: networkIdState=" + networkIdState);
                //TODO adjust CAPS;
            }
        }

    }

    class CreateDialog implements Runnable {

        @Override
        public void run() {
            try {
                if (ctx.getCurrentCounter("CreateDialogCounter").get() < ctx.NDIALOGS) {
                    ctx.scenario.createDialog(ctx);
                    ctx.incrementCounter("CreateDialogCounter");
                    if (ctx.getCurrentCounter("CreateDialogCounter").get() >= ctx.NDIALOGS) {
                        ctx.switchToState(MAPpState.COMPLETED);
                    }
                }
            } catch (Exception ex) {
                logger.info("failed ot create dialog", ex);
                ctx.incrementCounter("DialogCreateFailure");
            }
        }

    }

}
