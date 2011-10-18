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

package org.mobicents.protocols.ss7.cap.api.errors;

/**
 * CAP Error codes 
 * Carried by ReturnError primitive 
 * 
 * @author sergey vetyutnev
 * 
 */
public interface CAPErrorCode {

	public static final int canceled = 0;
	public static final int cancelFailed = 1;
	public static final int eTCFailed = 3;
	public static final int improperCallerResponse = 4;
	public static final int missingCustomerRecord = 6;
	public static final int missingParameter = 7;
	public static final int parameterOutOfRange = 8;
	public static final int requestedInfoError = 10;
	public static final int systemFailure = 11;
	public static final int taskRefused = 12;
	public static final int unavailableResource = 13;
	public static final int unexpectedComponentSequence = 14;
	public static final int unexpectedDataValue = 15;
	public static final int unexpectedParameter = 16;
	public static final int unknownLegID = 17;
	public static final int unknownPDPID = 50;
	public static final int unknownCSID = 51;
}
