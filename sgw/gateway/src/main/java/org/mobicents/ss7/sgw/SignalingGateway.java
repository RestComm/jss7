/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
