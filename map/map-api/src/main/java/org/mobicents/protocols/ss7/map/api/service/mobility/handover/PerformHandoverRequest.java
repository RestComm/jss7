/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.mobicents.protocols.ss7.map.api.service.mobility.handover;

import org.mobicents.protocols.ss7.map.api.primitives.GlobalCellId;
import org.mobicents.protocols.ss7.map.api.service.mobility.MobilityMessage;

/**
 * 

MAP V1:
PerformHandover ::= OPERATION --Timer s
ARGUMENT
	performHO-Arg PerformHO-Arg
RESULT
	performHO-Res PerformHO-Res
ERRORS {
	SystemFailure, 
	UnexpectedDataValue,
	UnknownBaseStation, 
	InvalidTargetBaseStation, 
	NoRadioResourceAvailable, 
	NoHandoverNumberAvailable}

MAP V1:
PerformHO-Arg ::= SEQUENCE { 146
	targetCellId 		GlobalCellId, 
	servingCellId 		GlobalCellId, 
	channelType 		ChannelType, 
	classmarkInfo 		ClassmarkInfo, 
	handoverPriority 	[11] HandoverPriority OPTIONAL, 
	kc 					[12] Kc OPTIONAL}

Kc ::= octet STRING (SIZE (8))

 * 
 * @author sergey vetyutnev
 * 
 */
public interface PerformHandoverRequest extends MobilityMessage {

	public GlobalCellId getTargetCellId();

	public GlobalCellId getServingCellId();

	public ChannelType getChannelType();

	public ClassmarkInfo getClassmarkInfo();

	public HandoverPriority getHandoverPriority();

	public byte[] getKc();

}
