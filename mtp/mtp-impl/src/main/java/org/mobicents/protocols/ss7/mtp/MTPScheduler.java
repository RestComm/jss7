/*
 * Mobicents, Communications Middleware
 *
 * Copyright (c) 2008, Red Hat Middleware LLC or third-party
 * contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors. All third-party contributions are
 * distributed under license by Red Hat Middleware LLC.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 *
 * Boston, MA 02110-1301 USA
 */

package org.mobicents.protocols.ss7.mtp;

import org.apache.log4j.Logger;

/**
 *
 * @author kulikov
 */
public class MTPScheduler {
	protected static final Logger logger = Logger.getLogger(MTPScheduler.class);
    protected MTPTask[] tasks = new MTPTask[5];
    
    MTPTask tempTask = null;
    
    public void schedule(MTPTask task, int delay) {
        task.deadLine= System.currentTimeMillis() + delay;
        for (int i = 0; i < tasks.length; i++) {
            if (tasks[i] == null || tasks[i].canceled) {
                task.canceled = false;
                tasks[i] = task;
                task.index = i;
                task.scheduler = this;
                break;
            }
        }
    }
    
    public void tick() {
        long now = System.currentTimeMillis();
        for (int i = 0; i < tasks.length; i++) {
            if (tasks[i] != null && (tasks[i].deadLine <= now)) {
            	//check if has been canceled from different thread.
            	if (tasks[i].canceled) {
            		tasks[i] = null;
            	}else
            	{
            		tempTask = tasks[i];
            		try{
            			tempTask.run();
            		}catch(Exception e)
            		{
            			if(logger.isDebugEnabled())
            			{
            				logger.debug("Failuer on task run.",e);
            			}
            		}
            		//check if its canceled after run;
            		if (tasks[i].canceled) {
            			tasks[i] = null;
            		}
            	}
            	//tempTask = null;
            	
            } 
        }
    }
}
