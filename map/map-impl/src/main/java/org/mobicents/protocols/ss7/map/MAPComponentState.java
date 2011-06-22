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
package org.mobicents.protocols.ss7.map;

/**
 * @author amit bhayani
 *
 */
public enum MAPComponentState {
	//No activity associated with the ID.
	Idle,
	
	//An operation has been transmitted to the remote end, but no result has been received. The timer associated with 
	//the operation invocation (with the value of "Timeout") is	started when the transition from "Idle" to "Operation Sent" occurs
	OperationPending,

	//The result has been received; TCAP is waiting for its possible rejection by the TC-user.
	WaitforReject,
	
	//Reject of the result has been requested by the TC-user, but no request for transmission has been issued.
	Rejectpending;
}
