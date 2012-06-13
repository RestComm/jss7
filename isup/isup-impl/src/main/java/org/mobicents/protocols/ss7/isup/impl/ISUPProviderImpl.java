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

/**
 * 
 */
package org.mobicents.protocols.ss7.isup.impl;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.isup.CircuitManager;
import org.mobicents.protocols.ss7.isup.ISUPEvent;
import org.mobicents.protocols.ss7.isup.ISUPListener;
import org.mobicents.protocols.ss7.isup.ISUPMessageFactory;
import org.mobicents.protocols.ss7.isup.ISUPParameterFactory;
import org.mobicents.protocols.ss7.isup.ISUPProvider;
import org.mobicents.protocols.ss7.isup.ISUPTimeoutEvent;
import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.impl.message.ISUPMessageFactoryImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.ISUPParameterFactoryImpl;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitive;

import org.mobicents.protocols.ss7.scheduler.Scheduler;
/**
 * @author baranowb
 * 
 */
public class ISUPProviderImpl implements ISUPProvider {

	private static final Logger logger = Logger.getLogger(ISUPProviderImpl.class);

	protected final List<ISUPListener> listeners = new FastList<ISUPListener>();

	protected ISUPStackImpl stack;
	protected ISUPMessageFactory messageFactory;
	protected ISUPParameterFactory parameterFactory;
	private Scheduler scheduler;
	
	protected final ConcurrentHashMap<Long,Circuit> cic2Circuit = new ConcurrentHashMap<Long,Circuit>();
	protected int ni, localSpc;
	public ISUPProviderImpl(ISUPStackImpl isupStackImpl,Scheduler scheduler, Properties props) {
		this.stack = isupStackImpl;
		this.scheduler=scheduler;
		this.T1Timeout = Long.parseLong(props.getProperty(T1, this.T1Timeout + ""));
		this.T5Timeout = Long.parseLong(props.getProperty(T5, this.T5Timeout + ""));
		this.T7Timeout = Long.parseLong(props.getProperty(T7, this.T7Timeout + ""));
		this.T12Timeout = Long.parseLong(props.getProperty(T12, this.T12Timeout + ""));
		this.T13Timeout = Long.parseLong(props.getProperty(T13, this.T13Timeout + ""));
		this.T14Timeout = Long.parseLong(props.getProperty(T14, this.T14Timeout + ""));
		this.T15Timeout = Long.parseLong(props.getProperty(T15, this.T15Timeout + ""));
		this.T16Timeout = Long.parseLong(props.getProperty(T16, this.T16Timeout + ""));
		this.T17Timeout = Long.parseLong(props.getProperty(T17, this.T17Timeout + ""));
		this.T18Timeout = Long.parseLong(props.getProperty(T18, this.T18Timeout + ""));
		this.T19Timeout = Long.parseLong(props.getProperty(T19, this.T19Timeout + ""));
		this.T20Timeout = Long.parseLong(props.getProperty(T20, this.T20Timeout + ""));
		this.T21Timeout = Long.parseLong(props.getProperty(T21, this.T21Timeout + ""));
		this.T22Timeout = Long.parseLong(props.getProperty(T22, this.T22Timeout + ""));
		this.T23Timeout = Long.parseLong(props.getProperty(T23, this.T23Timeout + ""));
		// this.T28Timeout = Long.parseLong(props.getProperty(T28,
		// this.T28Timeout + ""));
		this.T33Timeout = Long.parseLong(props.getProperty(T33, this.T33Timeout + ""));
		
		if(!props.containsKey(NI))
		{
			throw new IllegalArgumentException("No definition of local NI!("+NI+")");
		}else
		{
			this.ni = Integer.parseInt(props.getProperty(NI));
		}
		
		if(!props.containsKey(LOCAL_SPC))
		{
			throw new IllegalArgumentException("No definition of localSPC!("+LOCAL_SPC+")");
		}else
		{
			this.localSpc = Integer.parseInt(props.getProperty(LOCAL_SPC));
		}
		
		// check bounds for timers.... trick might be that... for
		// national/internationl those are different... ech
		if (this.T1Timeout < 5000 || this.T1Timeout > 60000) {
			if (logger.isEnabledFor(Level.WARN)) {
				logger.warn("Wrong value of T1 timeout: " + this.T1Timeout + ", using default value.");
			}
			this.T1Timeout = ISUPTimeoutEvent.T1_DEFAULT;
		}

		if (this.T5Timeout < 5 * 60 * 1000 || this.T5Timeout > 15 * 60 * 1000) {
			if (logger.isEnabledFor(Level.WARN)) {
				logger.warn("Wrong value of T5 timeout: " + this.T5Timeout + ", using default value.");
			}
			this.T5Timeout = ISUPTimeoutEvent.T5_DEFAULT;
		}

		if (this.T7Timeout < 20 * 1000 || this.T7Timeout > 30 * 1000) {
			if (logger.isEnabledFor(Level.WARN)) {
				logger.warn("Wrong value of T7 timeout: " + this.T7Timeout + ", using default value.");
			}
			this.T7Timeout = ISUPTimeoutEvent.T7_DEFAULT;
		}

		if (this.T12Timeout < 15 * 1000 || this.T12Timeout > 60 * 1000) {
			if (logger.isEnabledFor(Level.WARN)) {
				logger.warn("Wrong value of T12 timeout: " + this.T12Timeout + ", using default value.");
			}
			this.T12Timeout = ISUPTimeoutEvent.T12_DEFAULT;
		}
		if (this.T13Timeout < 5 * 60 * 1000 || this.T13Timeout > 15 * 60 * 1000) {
			if (logger.isEnabledFor(Level.WARN)) {
				logger.warn("Wrong value of T13 timeout: " + this.T13Timeout + ", using default value.");
			}
			//this.T13Timeout = ISUPTimeoutEvent.T13_DEFAULT;
		}

		if (this.T14Timeout < 15 * 1000 || this.T14Timeout > 60 * 1000) {
			if (logger.isEnabledFor(Level.WARN)) {
				logger.warn("Wrong value of T14 timeout: " + this.T14Timeout + ", using default value.");
			}
			this.T14Timeout = ISUPTimeoutEvent.T14_DEFAULT;
		}
		if (this.T15Timeout < 5 * 60 * 1000 || this.T15Timeout > 15 * 60 * 1000) {
			if (logger.isEnabledFor(Level.WARN)) {
				logger.warn("Wrong value of T15 timeout: " + this.T15Timeout + ", using default value.");
			}
			this.T15Timeout = ISUPTimeoutEvent.T15_DEFAULT;
		}

		if (this.T16Timeout < 15 * 1000 || this.T16Timeout > 60 * 1000) {
			if (logger.isEnabledFor(Level.WARN)) {
				logger.warn("Wrong value of T16 timeout: " + this.T16Timeout + ", using default value.");
			}
			this.T16Timeout = ISUPTimeoutEvent.T16_DEFAULT;
		}
		if (this.T17Timeout < 5 * 60 * 1000 || this.T17Timeout > 15 * 60 * 1000) {
			if (logger.isEnabledFor(Level.WARN)) {
				logger.warn("Wrong value of T17 timeout: " + this.T17Timeout + ", using default value.");
			}
			this.T17Timeout = ISUPTimeoutEvent.T17_DEFAULT;
		}

		if (this.T18Timeout < 15 * 1000 || this.T18Timeout > 60 * 1000) {
			if (logger.isEnabledFor(Level.WARN)) {
				logger.warn("Wrong value of T18 timeout: " + this.T18Timeout + ", using default value.");
			}
			this.T18Timeout = ISUPTimeoutEvent.T18_DEFAULT;
		}
		if (this.T19Timeout < 5 * 60 * 1000 || this.T19Timeout > 15 * 60 * 1000) {
			if (logger.isEnabledFor(Level.WARN)) {
				logger.warn("Wrong value of T19 timeout: " + this.T19Timeout + ", using default value.");
			}
			this.T19Timeout = ISUPTimeoutEvent.T19_DEFAULT;
		}

		if (this.T20Timeout < 15 * 1000 || this.T20Timeout > 60 * 1000) {
			if (logger.isEnabledFor(Level.WARN)) {
				logger.warn("Wrong value of T20 timeout: " + this.T20Timeout + ", using default value.");
			}
			this.T20Timeout = ISUPTimeoutEvent.T20_DEFAULT;
		}
		if (this.T21Timeout < 5 * 60 * 1000 || this.T21Timeout > 15 * 60 * 1000) {
			if (logger.isEnabledFor(Level.WARN)) {
				logger.warn("Wrong value of T21 timeout: " + this.T21Timeout + ", using default value.");
			}
			this.T21Timeout = ISUPTimeoutEvent.T21_DEFAULT;
		}

		if (this.T22Timeout < 15 * 1000 || this.T22Timeout > 60 * 1000) {
			if (logger.isEnabledFor(Level.WARN)) {
				logger.warn("Wrong value of T22 timeout: " + this.T22Timeout + ", using default value.");
			}
			this.T22Timeout = ISUPTimeoutEvent.T22_DEFAULT;
		}
		if (this.T23Timeout < 5 * 60 * 1000 || this.T23Timeout > 15 * 60 * 1000) {
			if (logger.isEnabledFor(Level.WARN)) {
				logger.warn("Wrong value of T23 timeout: " + this.T23Timeout + ", using default value.");
			}
			this.T23Timeout = ISUPTimeoutEvent.T23_DEFAULT;
		}

		if (this.T33Timeout < 12 * 1000 || this.T33Timeout > 15 * 60 * 1000) {
			if (logger.isEnabledFor(Level.WARN)) {
				logger.warn("Wrong value of T33 timeout: " + this.T33Timeout + ", using default value.");
			}
			this.T33Timeout = ISUPTimeoutEvent.T33_DEFAULT;
		}
		
	
		this.parameterFactory = new ISUPParameterFactoryImpl();
		this.messageFactory = new ISUPMessageFactoryImpl(this.parameterFactory);
	}

	@Override
	public int getNi() {
		return this.ni;
	}

	@Override
	public int getLocalSpc() {
		return this.localSpc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.isup.ISUPProvider#addListener(org.mobicents.isup.ISUPListener
	 * )
	 */
	public void addListener(ISUPListener listener) {
		if (listener == null) {
			throw new NullPointerException("Listener must not be null!");
		}
		if(this.listeners.contains(listener))
		{
			throw new IllegalArgumentException("Listener already present: "+listener+" !");
		}else
		{
			this.listeners.add(listener);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.isup.ISUPProvider#removeListener(org.mobicents.isup.
	 * ISUPListener)
	 */
	public void removeListener(ISUPListener listener) {
		if (listener == null) {
			throw new NullPointerException("Listener must not be null!");
		}
		this.listeners.remove(listener);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPProvider#getMessageFactory()
	 */
	public ISUPMessageFactory getMessageFactory() {
		return this.messageFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPProvider#getParameterFactory()
	 */
	public ISUPParameterFactory getParameterFactory() {
		return this.parameterFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.isup.ISUPProvider#sendMessage(org.mobicents
	 * .protocols.ss7.isup.message.ISUPMessage)
	 */

	public void sendMessage(ISUPMessage msg,int dpc) throws ParameterException, IOException {
		if (!msg.hasAllMandatoryParameters()) {
			throw new ParameterException("Message does not have all required parameters!");
		}
		getCircuit(msg,dpc).send(msg);
	}

	public boolean cancelTimer(int cic,int dpc, int timerId) {
		long channelID=this.stack.getCircuitManager().getChannelID(cic,dpc);
		if (this.cic2Circuit.containsKey(channelID)) {
			Circuit c = this.cic2Circuit.get(channelID);
			return c.cancelTimer(timerId);
		}

		return false;
	}

	// ---------------------- non interface methods ----------------

	public void start() {
		CircuitManager cm = this.stack.getCircuitManager(); 
		long[] channelIDs = cm.getChannelIDs();		
		this.cic2Circuit.clear();
		
		for(long channelID:channelIDs)
		{
			Circuit c = new Circuit(cm.getCIC(channelID), cm.getDPC(channelID),this, scheduler);
			cic2Circuit.put(channelID, c);
		}	
	}

	public void stop() {		
		Enumeration<Long> keys=cic2Circuit.keys();
		while(keys.hasMoreElements()) {
			try{
				cic2Circuit.remove(keys.nextElement()).onStop();
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}		
	}

	// --------- private methods and class defs.
	/**
	 * @param message
	 * @return
	 */
	void receive(ISUPMessage message, int dpc) {
        Circuit c = getCircuit(message,dpc);
        if(c!=null)
        	c.receive(message);
	}

	private Circuit getCircuit(ISUPMessage message, int dpc) {
		Circuit c = null;
		int cic = message.getCircuitIdentificationCode().getCIC();
		long channelID=this.stack.getCircuitManager().getChannelID(cic,dpc);
		if (!this.stack.getCircuitManager().isCircuitPresent(cic,dpc)) {			
			if (this.cic2Circuit.containsKey(channelID)) {
				this.cic2Circuit.remove(channelID);
			}
			throw new IllegalArgumentException("Curcuit not defined, no route definition present!");

		} else {
			c = this.cic2Circuit.get(channelID);
		}
		return c;
	}	

	void send(Mtp3TransferPrimitive encoded) throws IOException {
		this.stack.send(encoded);
	}

	/**
	 * @param request
	 */
	public void deliver(ISUPEvent event) {
		for (int index = 0; index < listeners.size(); index++) {
			try {
				listeners.get(index).onEvent(event);
			} catch (Exception e) {
				if (logger.isEnabledFor(Level.ERROR)) {
					logger.error("Exception thrown from listener.", e);
				}
			}
		}

	}

	/**
	 * @param timeoutEvent
	 */
	public void deliver(ISUPTimeoutEvent timeoutEvent) {
		for (int index = 0; index < listeners.size(); index++) {
			try {
				listeners.get(index).onTimeout(timeoutEvent);
			} catch (Exception e) {
				if (logger.isEnabledFor(Level.ERROR)) {
					logger.error("Exception thrown from listener.", e);
				}
			}
		}
	}
	
	protected static final String NI = "ni";
	protected static final String LOCAL_SPC = "localspc";

	protected final static String T1 = "t1";
	protected final static String T5 = "t5";
	protected final static String T7 = "t7";
	protected final static String T12 = "t12";
	protected final static String T13 = "t13";
	protected final static String T14 = "t14";
	protected final static String T15 = "t15";
	protected final static String T16 = "t16";
	protected final static String T17 = "t17";
	protected final static String T18 = "t18";
	protected final static String T19 = "t19";
	protected final static String T20 = "t20";
	protected final static String T21 = "t21";
	protected final static String T22 = "t22";
	protected final static String T23 = "t23";
	// protected final static String T28 = "t28";
	protected final static String T33 = "t33";
	private long T1Timeout = ISUPTimeoutEvent.T1_DEFAULT;
	private long T5Timeout = ISUPTimeoutEvent.T5_DEFAULT;
	private long T7Timeout = ISUPTimeoutEvent.T7_DEFAULT;
	private long T12Timeout = ISUPTimeoutEvent.T12_DEFAULT;
	private long T13Timeout = ISUPTimeoutEvent.T13_DEFAULT;
	private long T14Timeout = ISUPTimeoutEvent.T14_DEFAULT;
	private long T15Timeout = ISUPTimeoutEvent.T15_DEFAULT;
	private long T16Timeout = ISUPTimeoutEvent.T16_DEFAULT;
	private long T17Timeout = ISUPTimeoutEvent.T17_DEFAULT;
	private long T18Timeout = ISUPTimeoutEvent.T18_DEFAULT;
	private long T19Timeout = ISUPTimeoutEvent.T19_DEFAULT;
	private long T20Timeout = ISUPTimeoutEvent.T20_DEFAULT;
	private long T21Timeout = ISUPTimeoutEvent.T21_DEFAULT;
	private long T22Timeout = ISUPTimeoutEvent.T22_DEFAULT;
	private long T23Timeout = ISUPTimeoutEvent.T23_DEFAULT;
	private long T28Timeout = ISUPTimeoutEvent.T28_DEFAULT;
	private long T33Timeout = ISUPTimeoutEvent.T33_DEFAULT;

	/**
	 * @return
	 */
	long getT1Timeout() {
		return T1Timeout;
	}

	/**
	 * @return
	 */
	long getT5Timeout() {
		return T5Timeout;
	}

	/**
	 * @return
	 */
	long getT7Timeout() {
		return T7Timeout;
	}

	/**
	 * @return
	 */
	long getT12Timeout() {
		return T12Timeout;
	}

	/**
	 * @return
	 */
	long getT13Timeout() {
		return T13Timeout;
	}

	/**
	 * @return
	 */
	long getT14Timeout() {
		return T14Timeout;
	}

	/**
	 * @return
	 */
	long getT15Timeout() {

		return T15Timeout;
	}

	/**
	 * @return
	 */
	long getT16Timeout() {

		return T16Timeout;
	}

	/**
	 * @return
	 */
	long getT17Timeout() {

		return T17Timeout;
	}

	/**
	 * @return
	 */
	long getT18Timeout() {

		return T18Timeout;
	}

	/**
	 * @return
	 */
	long getT19Timeout() {

		return T19Timeout;
	}

	/**
	 * @return
	 */
	long getT20Timeout() {

		return T20Timeout;
	}

	/**
	 * @return
	 */
	long getT21Timeout() {

		return T21Timeout;
	}

	/**
	 * @return
	 */
	long getT22Timeout() {

		return T22Timeout;
	}

	/**
	 * @return
	 */
	long getT23Timeout() {

		return T23Timeout;
	}

	/**
	 * @return
	 */
	long getT28Timeout() {

		return T28Timeout;
	}

	/**
	 * @return
	 */
	long getT33Timeout() {

		return T33Timeout;
	}

}
