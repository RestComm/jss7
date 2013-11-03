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

package org.mobicents.protocols.ss7.m3ua.impl.scheduler;

/**
 *
 * @author Amit Bhayani
 *
 */
public abstract class M3UATask {

    protected volatile boolean canceled = false;
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

    public void start(){
        this.canceled = false;
    }
}
