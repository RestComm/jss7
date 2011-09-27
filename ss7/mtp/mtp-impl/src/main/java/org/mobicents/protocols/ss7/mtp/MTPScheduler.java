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
