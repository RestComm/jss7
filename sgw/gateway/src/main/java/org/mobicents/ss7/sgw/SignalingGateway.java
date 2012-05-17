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
import org.mobicents.protocols.ss7.scheduler.Scheduler;
import org.mobicents.protocols.ss7.scheduler.Task;

public class SignalingGateway {

    public Scheduler scheduler;

    private static final Logger logger = Logger.getLogger(SignalingGateway.class);

    private ShellExecutor shellExecutor = null;
    private NodalInterworkingFunction nodalInterworkingFunction = null;

    private boolean isActive = false;

    public SignalingGateway() {

    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;        
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
    }

    public void destroy() {

    }

    public void cancel() {
        this.isActive = false;
        scheduler.stop();
    }

    public boolean isActive() {
        return this.isActive;
    }
}