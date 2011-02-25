package org.mobicents.ss7.sgw;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.mobicents.ss7.impl.clock.LocalTask;
import org.mobicents.ss7.impl.clock.Scheduler;
import org.mobicents.ss7.spi.clock.Task;

public class SignalingGateway implements Task {

    public final static Scheduler scheduler = new Scheduler();

    private static final Logger logger = Logger.getLogger(SignalingGateway.class);

    private ShellExecutor shellExecutor = null;
    private NodalInterworkingFunction nodalInterworkingFunction = null;

    private LocalTask task = null;
    private boolean isActive = false;

    public SignalingGateway() {

    }

    public ShellExecutor getShellExecutor() {
        return shellExecutor;
    }

    public void setShellExecutor(ShellExecutor shellExecutor) {
        this.shellExecutor = shellExecutor;
    }

    public NodalInterworkingFunction getNodalInterworkingFunction() {
        return nodalInterworkingFunction;
    }

    public void setNodalInterworkingFunction(NodalInterworkingFunction nodalInterworkingFunction) {
        this.nodalInterworkingFunction = nodalInterworkingFunction;
    }

    /**
     * Life Cycle methods
     */
    public void create() {

    }

    public void start() throws Exception {
        scheduler.start();
        task = scheduler.execute(this);
    }

    public void stop() {
        task.cancel();
    }

    public void destroy() {

    }

    public void cancel() {
        this.isActive = false;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public int perform() {
        try {
            this.shellExecutor.perform();
            this.nodalInterworkingFunction.perform();
            // Management
        } catch (IOException e) {
            logger.error("IOException ", e);
        }

        return 1;
    }

}
