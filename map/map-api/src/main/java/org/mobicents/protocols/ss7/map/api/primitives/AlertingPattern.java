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

	public byte[] getData();

	public AlertingLevel getAlertingLevel();

	public AlertingCategory getAlertingCategory();

}
