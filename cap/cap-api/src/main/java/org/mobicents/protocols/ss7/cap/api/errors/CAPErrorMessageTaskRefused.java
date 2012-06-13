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

package org.mobicents.protocols.ss7.cap.api.errors;

/**
 * 

taskRefused ERROR ::= { 
 PARAMETER ENUMERATED { 
   generic      (0), 
   unobtainable    (1), 
   congestion     (2) 
   } 
 CODE errcode-taskRefused 
 } 
-- An entity normally capable of the task requested cannot or chooses not to perform the task at 
-- this time. This includes error situations like congestion and unobtainable address as used in 
-- e.g. the connect operation.)
  
 * 
 * @author sergey vetyutnev
 * 
 */
public interface CAPErrorMessageTaskRefused extends CAPErrorMessage {

	TaskRefusedParameter getTaskRefusedParameter();

}
