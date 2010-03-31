package org.mobicents.protocols.ss7.mtp;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;


public class Utils {
	
	/////////////////////////
	// Some common statics //
	/////////////////////////
	
	/**
	 * Indicate value not set;
	 */
	public static final byte _VALUE_NOT_SET = -1;
	
	
	public final static String dump(ByteBuffer buff, int size, boolean asBits) {
		return dump(buff.array(), size, asBits);
	}
	public final static String dump(byte[] buff, int size, boolean asBits) {
		String s = "";
		for (int i = 0; i < size; i++) {
			String ss = null;
			if(!asBits)
			{
				ss = Integer.toHexString(buff[i] & 0xff);
			}
			else
			{
				ss = Integer.toBinaryString(buff[i] & 0xff); 
			}
			ss = fillInZeroPrefix(ss,asBits);
			s += " " + ss;
		}
		return s;
	}

	public final static String fillInZeroPrefix(String ss, boolean asBits) {
		if (asBits) {
			if (ss.length() < 8) {
				for (int j = ss.length(); j < 8; j++) {
					ss = "0" + ss;
				}
			}
		} else {
			// hex
			if (ss.length() < 2) {

				ss = "0" + ss;
			}
		}

		return ss;
	}

	public final static String dump(int[] buff, int size) {
		String s = "";
		for (int i = 0; i < size; i++) {
			String ss = Integer.toHexString(buff[i] & 0xff);
			if (ss.length() == 1) {
				ss = "0" + ss;
			}
			s += " " + ss;
		}
		return s;
	}
	public static void createTrace(Throwable t,StringBuilder sb, boolean top)
	{
		
		if(!top)
		{
			sb.append("\nCaused by: "+t.toString());
			
		}
		StackTraceElement[] trace = t.getStackTrace();
		for (int i=0; i < trace.length; i++)
            sb.append("\tat " + trace[i]);

        Throwable ourCause = t.getCause();
        if(ourCause!=null)
        {
        	createTrace(ourCause, sb,false);
        }
	}
	public static String  createTrace(Throwable t)
	{
		StringBuilder sb = new StringBuilder();
		createTrace(t,sb,true);
		return sb.toString();
	}
	
	public static Utils getInstance()
	{
		return _INSTANCE_;
	}
	
	/**
	 * General logger.
	 */
	private static final Logger logger = Logger.getLogger(Utils.class);
	private static final Utils _INSTANCE_ = new Utils();
	private StringBuilder loggBuilder = new StringBuilder();
	private LinkedList<BufferHolder> dataHolder = new LinkedList<BufferHolder>();
	private Future debugFuture;
	
	//this is doubling of vars, but....
	private boolean enabledL2Debug = false;
	private boolean enableDataTrace = false;
	//private boolean enableSuTrace = false;
	private boolean enabledL3Debug = false;
	
	
	/**
	 * @return the enabledL2Debug
	 */
	public boolean isEnabledL2Debug() {
		return enabledL2Debug;
	}
	/**
	 * @param enabledL2Debug the enabledL2Debug to set
	 */
	public void setEnabledL2Debug(boolean enabledL2Debug) {
		this.enabledL2Debug = enabledL2Debug;
	}
	/**
	 * @return the enableDataTrace
	 */
	public boolean isEnableDataTrace() {
		return enableDataTrace;
	}
	/**
	 * @param enableDataTrace the enableDataTrace to set
	 */
	public void setEnableDataTrace(boolean enableDataTrace) {
		this.enableDataTrace = enableDataTrace;
	}
//	/**
//	 * @return the enableSuTrace
//	 */
//	public boolean isEnableSuTrace() {
//		return enableSuTrace;
//	}
//	/**
//	 * @param enableSuTrace the enableSuTrace to set
//	 */
//	public void setEnableSuTrace(boolean enableSuTrace) {
//		this.enableSuTrace = enableSuTrace;
//	}
	/**
	 * @return the enabledL3Debug
	 */
	public boolean isEnabledL3Debug() {
		return enabledL3Debug;
	}
	/**
	 * @param enabledL3Debug the enabledL3Debug to set
	 */
	public void setEnabledL3Debug(boolean enabledL3Debug) {
		this.enabledL3Debug = enabledL3Debug;
	}
	public void append(String s)
	{
		this.loggBuilder.append("\n"+s);
	}
	
	public void startDebug() {
		if(enabledL2Debug || enabledL3Debug)
		{
			//1s is enough time to get a lot of data(2ms is 16B transmision so, more time would cause OutOfMemory :))
			this.debugFuture = mtpTimer.scheduleAtFixedRate(new LogginAction(), 1, 1, TimeUnit.SECONDS);
		}
		
	}
	public void stopDebug()
	{
		if(this.debugFuture!=null)
		{
			this.debugFuture.cancel(false);
			this.debugFuture = null;
		}
	}
	
	
	
	private final class LogginAction implements Runnable
	{

		public void run() {
			
			// TODO Auto-generated method stub
			if((enabledL2Debug || enabledL3Debug) && loggBuilder.length() > 0)
			{
				logger.info(loggBuilder);
				loggBuilder = new StringBuilder();
			}
			if(enableDataTrace && enabledL2Debug && dataHolder.size() > 0)
			{
				StringBuilder sb = new StringBuilder();
				while(dataHolder.size()>0)
				{
					sb.append(dataHolder.remove(0)).append("\n");
				}
				logger.info("\n"+sb);
			}
		}
		
	}
	//FIXME: this will be moved once everything is coded and we have proper framework
	// //////////////////////////////////////
	// LOGGERS Section. Note that MTP acts //
	// on 2ms basis, logging proves to be  //
	// killer, we use async loggin, one    //
	// event per second                    //
	// //////////////////////////////////////
	private class BufferHolder
	{
		private long stamp;
		private byte[] buffer;
		private int len;
		private String linkName;
		private int linkSls=-1;
		private String linkSetName;
		private int linkSetId=-1;
		

		private BufferHolder(long stamp, byte[] buffer, int len,
				String linkName, int linkSls, String linkSetName, int linkSetId) {
			super();
			this.stamp = stamp;
			this.buffer = buffer;
			this.len = len;
			System.arraycopy(buffer, 0, this.buffer, 0, len);
			this.linkName = linkName;
			this.linkSls = linkSls;
			this.linkSetName = linkSetName;
			this.linkSetId = linkSetId;
		}


		@Override
		public String toString() {
			return "T:"+this.stamp+":"+linkName+":"+linkSls+":"+linkSetName+":"+linkSetId+":"+Utils.dump(this.buffer,len,false);
		}
		
	}
	
	private final static MTPThreadFactory timerThreadFactory = new MTPThreadFactory(
			"MTPTimerTGroup", Thread.MAX_PRIORITY);
	private final static ScheduledExecutorService mtpTimer = Executors
			.newSingleThreadScheduledExecutor(timerThreadFactory);
	
	public static ScheduledExecutorService getMtpTimer()
	{
		return mtpTimer;
	}
	public void addBuffer(int sls, String name, String linkSetName, int linkSetId,
			long currentTimeMillis, byte[] buff, int len) {
		//hmm works better if method is implemented :)
		BufferHolder bh = new BufferHolder(currentTimeMillis, buff, len, name, sls, linkSetName, linkSetId);
		dataHolder.add(bh);
	}
}
