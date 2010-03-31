package org.mobicents.protocols.ss7.mtp;

import java.util.concurrent.ThreadFactory;


public class MTPThreadFactory implements ThreadFactory {
	//seq, can be inacurate.
		private static long _seq;
	  	private String threadName;
	  	private int priority = Thread.NORM_PRIORITY;
	    private ThreadGroup factoryThreadGroup ;

	    
	    public MTPThreadFactory(String threadName, int priority) {
			super();
			this.threadName = threadName;
			this.priority = priority;
			this.factoryThreadGroup = new ThreadGroup("MMS_SS7_ThreadGroup");
		}


		public Thread newThread(Runnable r) {
	        Thread t = new Thread(this.factoryThreadGroup, r);
	        t.setPriority(this.priority);
	        t.setName("MTPThread["+threadName+"-"+(_seq++)+"]");
	        // ??
	        //t.start();
	        return t;
	    }
}
