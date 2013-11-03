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

import javolution.util.FastList;

import org.apache.log4j.Logger;

/**
 *
 * @author amit bhayani
 *
 */
public class M3UAScheduler implements Runnable {
    private static final Logger logger = Logger.getLogger(M3UAScheduler.class);

    // TODO : Synchronize tasks? Use Iterator?
    protected FastList<M3UATask> tasks = new FastList<M3UATask>();

    private FastList<M3UATask> removed = new FastList<M3UATask>();

    public void execute(M3UATask task) {
        if (task == null) {
            return;
        }
        this.tasks.add(task);
    }

    public void run() {
        long now = System.currentTimeMillis();
        for (FastList.Node<M3UATask> n = tasks.head(), end = tasks.tail(); (n = n.getNext()) != end;) {
            M3UATask task = n.getValue();
            // check if has been canceled from different thread.
            if (task.isCanceled()) {
                // tasks.delete(n);
                removed.add(task);
            } else {

                try {
                    task.run(now);
                } catch (Exception e) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Failuer on task run.", e);
                    }
                }
                // check if its canceled after run;
                if (task.isCanceled()) {
                    removed.add(task);
                }
            }
            // tempTask = null;
        }

        if (this.removed.size() > 0) {
            this.tasks.removeAll(this.removed);
            this.removed.clear();
        }
    }
}
