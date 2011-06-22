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
package org.mobicents.protocols.ss7.map.service.supplementary;

import org.mobicents.protocols.ss7.map.MAPDialogImpl;
import org.mobicents.protocols.ss7.map.MAPService;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;

/**
 * @author amit bhayani
 *
 */
public class SupplementaryService extends MAPService {

	/**
	 * 
	 */
	public SupplementaryService() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.MAPService#processComponents(org.mobicents.protocols.ss7.map.MAPDialogImpl, org.mobicents.protocols.ss7.tcap.asn.comp.Component[])
	 */
	@Override
	protected void processComponents(MAPDialogImpl mapDialogImpl, Component[] components) {
		// TODO Auto-generated method stub

	}

}
