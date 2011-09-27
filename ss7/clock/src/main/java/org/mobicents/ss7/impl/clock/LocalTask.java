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

package org.mobicents.ss7.impl.clock;

import org.mobicents.ss7.spi.clock.Task;
import org.mobicents.ss7.spi.clock.TimerTask;

/**
 *
 * @author kulikov
 */
public class LocalTask implements TimerTask {

    private Task task;
    protected long seconds = 0;

    private volatile boolean isActive = true;
    /**
     * Wraps spacified task.
     * 
     * @param task the task to wrap.
     */
    protected LocalTask(Task task) {
        this.task = task;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void cancel() {
        isActive = false;
    }
    
    public int perform() {
        return task.perform();
    }
}
