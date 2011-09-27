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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.protocols.ss7.mtp;

import static org.testng.Assert.*;

import org.testng.*;import org.testng.annotations.*;

/**
 *
 * @author kulikov
 */
public class MTPSchedulerTest implements Runnable {

    private MTPScheduler scheduler = new MTPScheduler();
    
    private boolean done;
    private boolean failed;
    
    private long stopTime;
    
    public MTPSchedulerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }



    /**
     * Test of schedule method, of class MTPScheduler.
     */
    @Test
    public void testSchedule() throws InterruptedException {
        stopTime = System.currentTimeMillis() + 5000;
        new Thread(this).start();
        
        Thread.currentThread().sleep(1000);
        
        MTPTask task = new TestTask();
        scheduler.schedule(task, 1000);
        
        Thread.currentThread().sleep(5000);
        assertTrue(done, "Task is not executed");
        assertFalse(failed, "Task executed more then one time");
    }


    /**
     * Test of schedule method, of class MTPScheduler.
     */
    @Test
    public void testCancel() throws InterruptedException {
        this.done = false;
        this.failed = false;
        
        stopTime = System.currentTimeMillis() + 5000;
        new Thread(this).start();
        
        Thread.currentThread().sleep(1000);
        
        MTPTask task = new TestTask();
        scheduler.schedule(task, 3000);
        
        Thread.currentThread().sleep(1000);
        task.cancel();
        
        Thread.currentThread().sleep(4000);
        assertFalse(done, "Task is  executed");
        assertFalse(failed, "Task executed more then one time");
    }
    
    public void run() {
        while (System.currentTimeMillis() < stopTime) {
            scheduler.tick();
        }
    }

    private class TestTask extends MTPTask {

        @Override
        public void perform() {
            if (done) {
                failed = true;
            }
            done = true;
        }
        
    }
}