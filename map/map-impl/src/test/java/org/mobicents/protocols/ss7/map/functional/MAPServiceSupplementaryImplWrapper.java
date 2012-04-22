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

package org.mobicents.protocols.ss7.map.functional;

import org.mobicents.protocols.ss7.map.MAPProviderImpl;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.dialog.ServingCheckData;
import org.mobicents.protocols.ss7.map.api.dialog.ServingCheckResult;
import org.mobicents.protocols.ss7.map.dialog.ServingCheckDataImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.MAPServiceSupplementaryImpl;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextNameImpl;

public class MAPServiceSupplementaryImplWrapper extends MAPServiceSupplementaryImpl {
	
	private int testMode = 0;

	public MAPServiceSupplementaryImplWrapper(MAPProviderImpl mapProviderImpl) {
		super(mapProviderImpl);
	}

	public ServingCheckData isServingService(MAPApplicationContext dialogApplicationContext) {
		if(this.testMode == 1) {
			// For reproducing FunctionalTestScenario.actionC MAPFunctionalTest
			//   - remove temporally this comment comment 
			ApplicationContextNameImpl ac = new ApplicationContextNameImpl();
			ac.setOid(new long[] { 1, 2, 3 });
			ServingCheckDataImpl i1 = new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect, ac);
			return i1;
		}

		return super.isServingService(dialogApplicationContext);
	}

	public void setTestMode( int testMode ) {
		this.testMode = testMode;
	}

}
