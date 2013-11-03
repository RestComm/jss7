/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.ss7.sgw;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.scheduler.Scheduler;

public class SignalingGateway {

    public Scheduler scheduler;

    private static final Logger logger = Logger.getLogger(SignalingGateway.class);

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