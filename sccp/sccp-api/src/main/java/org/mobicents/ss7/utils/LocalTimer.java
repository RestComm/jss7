/*
 * The Java Call Control API for CAMEL 2
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * AND DATA ACCURACY.  We do not warrant or make any representations
 * regarding the use of the software or the  results thereof, including
 * but not limited to the correctness, accuracy, reliability or
 * usefulness of the software.
 */

package org.mobicents.ss7.utils;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Oleg Kulikov
 */
public class LocalTimer {
    
    private Runnable task;
    private volatile boolean canceled = false;
    private int delay;
    
    private static Timer timer = new Timer();
    private TimerTask timerTask;
        
    /** Creates a new instance of Timer */
    public LocalTimer() {
    }
    
    public synchronized void schedule(Runnable task, int delay) {
        this.task = task;
        this.delay = delay;
        timerTask = new LocalTimerTask();
        timer.schedule(timerTask, delay*1000);
        canceled = false;
    }
    
    public synchronized void stop() {        
        if (!canceled) {
            timerTask.cancel();
            canceled = true;
        }
    }
    
    public synchronized void reset(int delay) {
        stop();
        schedule(task, delay);
    }
    
    private class LocalTimerTask extends TimerTask {
        public void run() {
            try {
                task.run();
            } finally {
                cancel();
                canceled = true;
            }
        }
    }
}
