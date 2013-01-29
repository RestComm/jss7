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

package org.mobicents.protocols.ss7.map.api.primitives;

import java.io.Serializable;

/**
 * AlertingPattern ::= OCTET STRING (SIZE (1) )
 *		-- This type is used to represent Alerting Pattern
 *		-- bits 8765 : 0000 (unused)
 *		-- bits 43 : type of Pattern
 *		-- 				00 level
 *		-- 				01 category
 *		-- 				10 category
 * 		-- 				all other values are reserved.
 *		-- bits 21 : type of alerting
 *	alertingLevel-0 AlertingPattern ::= '00000000'B
 *	alertingLevel-1 AlertingPattern ::= '00000001'B
 *	alertingLevel-2 AlertingPattern ::= '00000010'B
 *		-- all other values of Alerting level are reserved
 *		-- Alerting Levels are defined in GSM 02.07
 *	alertingCategory-1 AlertingPattern ::= '00000100'B
 *	alertingCategory-2 AlertingPattern ::= '00000101'B
 *	alertingCategory-3 AlertingPattern ::= '00000110'B
 *	alertingCategory-4 AlertingPattern ::= '00000111'B
 *	alertingCategory-5 AlertingPattern ::= '00001000'B
 *		-- all other values of Alerting Category are reserved
 *		-- Alerting categories are defined in GSM 02.07
 * 
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public interface AlertingPattern extends Serializable {

	public int getData();

	public AlertingLevel getAlertingLevel();

	public AlertingCategory getAlertingCategory();

}
