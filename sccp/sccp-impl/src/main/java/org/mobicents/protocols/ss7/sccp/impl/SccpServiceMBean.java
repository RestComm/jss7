/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.protocols.ss7.sccp.impl;

import org.mobicents.protocols.ss7.sccp.impl.router.Rule;
import org.mobicents.protocols.ss7.sccp.impl.router.RuleException;

/**
 * @author baranowb
 * 
 */
public interface SccpServiceMBean {

	public static final String ONAME = "org.mobicents.ss7.sccp:service=SccpService";

	public void setJndiName(String jndiName);

	public String getJndiName();

	//public void setLinksets(List<MtpProvider> linksets);

	public void start() throws Exception;

	public void stop();
	
	
	//////////////////
	// mgmt methods //
    //////////////////
	
	public void addRule(Rule r) throws RuleException;
	public boolean removeRule(int num);
	public Rule[] getRules();
}
