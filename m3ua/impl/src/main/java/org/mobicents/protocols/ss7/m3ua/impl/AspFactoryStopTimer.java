/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * 
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free 
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.protocols.ss7.m3ua.impl;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.impl.scheduler.M3UATask;

/**
 * @author amit bhayani
 * 
 */
public class AspFactoryStopTimer extends M3UATask {

	private static Logger logger = Logger.getLogger(AspFactoryStopTimer.class);

	private int STOP_TIMER_TIMEOUT = 3000;

	private AspFactory aspFactory = null;

	private long initiatedTime = 0l;

	/**
	 * @param association
	 * @param transportManagement
	 */
	public AspFactoryStopTimer(AspFactory aspFactory) {
		super();
		this.aspFactory = aspFactory;
		this.initiatedTime = System.currentTimeMillis();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.m3ua.impl.scheduler.M3UATask#tick(long)
	 */
	@Override
	public void tick(long now) {
		if (now - this.initiatedTime >= STOP_TIMER_TIMEOUT) {
			
			if (this.aspFactory.association.isConnected()) {
				logger.warn(String.format("Asp=%s was stopped but underlying Association=%s was not stopped after TIMEOUT=%d ms. Forcing stop now",
						this.aspFactory.getName(), this.aspFactory.association.getName(), STOP_TIMER_TIMEOUT));
				try {
					this.aspFactory.transportManagement.stopAssociation(this.aspFactory.association.getName());
				} catch (Exception e) {
					logger.error(String.format("Exception while trying to stop Association=%s", this.aspFactory.association.getName()));
				}
			}
			
			//Finally cancel
			this.cancel();
		}// if(now-this.initiatedTime >= STOP_TIMER_TIMEOUT)
	}

}
