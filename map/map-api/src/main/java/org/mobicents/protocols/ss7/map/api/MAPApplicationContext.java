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

package org.mobicents.protocols.ss7.map.api;

import java.util.Arrays;

/**
 * 
 * @author amit bhayani
 * @author sergey vetyutnev
 * 
 */
public class MAPApplicationContext {
	
	private static long[] oidTemplate = new long[] { 0, 4, 0, 0, 1, 0, 0, 0 };

	private MAPApplicationContextName contextName;
	private MAPApplicationContextVersion contextVersion;

	private MAPApplicationContext(MAPApplicationContextName contextName, MAPApplicationContextVersion contextVersion) {
		this.contextName = contextName;
		this.contextVersion = contextVersion;
	}

	public long[] getOID() {
		long[] res = Arrays.copyOf(oidTemplate, oidTemplate.length);
		res[6] = this.contextName.getApplicationContextCode();
		res[7] = this.contextVersion.getVersion();
		
		return res;
	}

	public MAPApplicationContextName getApplicationContextName() {
		return this.contextName;
	}

	public MAPApplicationContextVersion getApplicationContextVersion() {
		return this.contextVersion;
	}

	public static MAPApplicationContext getInstance(MAPApplicationContextName contextName, MAPApplicationContextVersion contextVersion) {
		if (MAPApplicationContext.availableApplicationContextVersion(contextName, contextVersion.getVersion()))
			return new MAPApplicationContext(contextName, contextVersion);
		else
			return null;
	}

	public static MAPApplicationContext getInstance(long[] oid) {
		
		if (oid == null || oid.length != oidTemplate.length)
			return null;
		for (int i1 = 0; i1 < oidTemplate.length - 2; i1++) {
			if (oid[i1] != oidTemplate[i1])
				return null;
		}
		
		MAPApplicationContextName contextName = MAPApplicationContextName.getInstance(oid[6]);
		MAPApplicationContextVersion contextVersion = MAPApplicationContextVersion.getInstance(oid[7]);

		if (contextName == null || contextVersion == null)
			return null;
		if (!MAPApplicationContext.availableApplicationContextVersion(contextName, (int) oid[7]))
			return null;
		
		return new MAPApplicationContext(contextName, contextVersion);
	}
	
	/**
	 * Return if the contextVersion is available for the contextName
	 * 
	 * @param contextName
	 * @param version
	 * @return
	 */
	public static boolean availableApplicationContextVersion(MAPApplicationContextName contextName, int contextVersion) {
		switch (contextName) {
		case networkUnstructuredSsContext:
		case shortMsgAlertContext:
			if (contextVersion >= 1 && contextVersion <= 2)
				return true;
			else
				return false;

		case shortMsgMORelayContext:
		case shortMsgMTRelayContext:
		case shortMsgGatewayContext:
			if (contextVersion >= 1 && contextVersion <= 3)
				return true;
			else
				return false;
		case locationSvcEnquiryContext:
			//for locationSvcEnquiryContext only version 3 is supported
			if (contextVersion == 3) 
				return true;
			else
				return false;	
		case anyTimeEnquiryContext:
			if (contextVersion == 3){
				return true;
			}
		}
		
		return false;
	}

	/**
	 * Get ApplicationContext version. If oid is bad 0 will be received
	 * 
	 * @param oid
	 * @return
	 */
	public static int getProtocolVersion(long[] oid) {
		if (oid == null || oid.length != 8)
			return 0;
		else
			return (int) oid[7];
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof MAPApplicationContext))
			return false;
		
		MAPApplicationContext x = (MAPApplicationContext)obj;
		if (this.contextName == x.contextName && this.contextVersion == x.contextVersion)
			return true;
		else
			return false;
	}

	
	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();

		s.append("MAPApplicationContext [Name=");
		s.append(this.contextName.toString());
		s.append(", Version=");
		s.append(this.contextVersion.toString());
		s.append(", Oid=");
		for (long l : this.getOID()) {
			s.append(l).append(", ");
		}
		s.append("]");

		return s.toString();
	}

}
