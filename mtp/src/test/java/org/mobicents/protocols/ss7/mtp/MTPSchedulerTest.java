/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.protocols.ss7.mtp;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

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

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
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
        assertTrue("Task is not executed", done);
        assertFalse("Task executed more then one time", failed);
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
        assertFalse("Task is  executed", done);
        assertFalse("Task executed more then one time", failed);
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