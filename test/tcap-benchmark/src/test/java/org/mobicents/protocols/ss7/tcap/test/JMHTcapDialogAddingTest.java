package org.mobicents.protocols.ss7.tcap.test;

import java.util.concurrent.TimeUnit;

import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.TCAPStackImpl;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.TCAPProvider;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@State(Scope.Benchmark)
public class JMHTcapDialogAddingTest {

    TCAPStackImpl stack;
    TCAPProvider provider;
    SccpAddress sccpAddress = new SccpAddressImpl();

    public static String getTmpTestDir() {
        return "/aaa/aaa";
    }

    @Setup
    @BeforeMethod
    public void doStart() throws Exception {
        SccpStackImpl sccpStack = new SccpStackImpl("Test_Benchmark");
        final String dir = getTmpTestDir();
        if (dir != null) {
            sccpStack.setPersistDir(dir);
        }

        // sccpStack.setMtp3UserPart(1, mtp3UserPart1);
        sccpStack.start();
        sccpStack.removeAllResourses();
//        sccpStack.getRouter().addMtp3ServiceAccessPoint(1, 1, 111, 2, 0);
//        sccpStack.getRouter().addMtp3Destination(1, 1, 222, 222, 0, 255, 255);

        SccpProvider sccpProvider = sccpStack.getSccpProvider();

        stack = new TCAPStackImpl("Test_Benchmark", sccpProvider, 8);

        stack.start();
        stack.setMaxDialogs(1000000);
        provider = stack.getProvider();

        // adding of initial 10000 dialogs
        for (int i1 = 0; i1 < 100000; i1++) {
            provider.getNewDialog(sccpAddress, sccpAddress);
        }
    }

    @TearDown
    @AfterMethod
    public void doStop() {
        stack.stop();
    }        

    @Benchmark
    @OperationsPerInvocation(1000)
    public void measureRight() throws TCAPException {
        Dialog dialog = provider.getNewDialog(sccpAddress, sccpAddress);
        dialog.release();
    }

    
    
//    public static void main(String[] args) throws RunnerException {
//        Options opt = new OptionsBuilder().include(JMHTcapDialogAddingTest.class.getSimpleName()).forks(1).build();
//
//        new Runner(opt).run();
//    }

    @Test
    public void launchBenchmark() throws Exception {
        Options opt = new OptionsBuilder()
                // Specify which benchmarks to run.
                // You can be more specific if you'd like to run only one benchmark per test.
                .include(JMHTcapDialogAddingTest.class.getSimpleName())
                // Set the following options as needed
                .timeUnit(TimeUnit.MICROSECONDS)
                .warmupIterations(0)
                .measurementIterations(5)
                .measurementTime(TimeValue.seconds(5))
                .mode(Mode.Throughput)
                .threads(20)
                .forks(1)
                .build();

        new Runner(opt).run();

//        .shouldFailOnError(true)
//        .shouldDoGC(true)
//        .warmupTime(TimeValue.seconds(1))
//        .measurementTime(TimeValue.seconds(1))
//        .mode(Mode.AverageTime)
    
    
    }
}
