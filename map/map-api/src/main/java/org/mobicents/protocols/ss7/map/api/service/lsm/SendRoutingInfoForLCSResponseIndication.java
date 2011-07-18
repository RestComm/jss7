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
package org.mobicents.protocols.ss7.map.api.service.lsm;

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 * RoutingInfoForLCS-Res ::= SEQUENCE {
 *		targetMS [0] SubscriberIdentity,
 *		lcsLocationInfo [1] LCSLocationInfo,
 *		extensionContainer [2] ExtensionContainer OPTIONAL,
 *		...,
 *		v-gmlc-Address [3] GSN-Address OPTIONAL,
 *		h-gmlc-Address [4] GSN-Address OPTIONAL,
 *		ppr-Address [5] GSN-Address OPTIONAL,
 *		additional-v-gmlc-Address [6] GSN-Address OPTIONAL }
 *
 * @author amit bhayani
 *
 */
public interface SendRoutingInfoForLCSResponseIndication extends LsmMessage {
	public SubscriberIdentity getTargetMS();
	
	public LCSLocationInfo getLCSLocationInfo();
	
	public MAPExtensionContainer getExtensionContainer();
	
	/**
	 * GSN-Address ::= OCTET STRING (SIZE (5..17))
     *		-- Octets are coded according to TS 3GPP TS 23.003 [17]
	 */
	public byte[] getVgmlcAddress();
	
	public byte[] getHGmlcAddress();
	
	public byte[] getPprAddress();
	
	public byte[] getAdditionalVGmlcAddress();

}
