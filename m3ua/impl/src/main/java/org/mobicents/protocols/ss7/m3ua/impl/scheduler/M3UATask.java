/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
package org.mobicents.protocols.ss7.m3ua.impl.scheduler;

public abstract class M3UATask {

    protected boolean canceled = false;
    protected int index;
    protected M3UAScheduler scheduler;

    public void run(long now) {
        if (!canceled) {
            // exception in caught in scheduler.
            tick(now);
        }
    }

    public boolean isCanceled() {
        return this.canceled;
    }

    public abstract void tick(long now);

    public void cancel() {
        this.canceled = true;
        // dont do this, let it be lazely reclaimed if ever, this causes race!
        // remove task from list
        // if (scheduler != null && (index >=0) && (index <
        // scheduler.tasks.length)) {
        // scheduler.tasks[index] = null;
        // }
    }
}
