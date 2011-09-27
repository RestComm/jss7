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

package org.mobicents.protocols.ss7.map.api.service.lsm;

/**
 * AreaEventInfo ::= SEQUENCE {
 *     areaDefinition [0] AreaDefinition,
 *     occurrenceInfo [1] OccurrenceInfo OPTIONAL,
 *     intervalTime [2] IntervalTime OPTIONAL,
 *     ...}
 * 
 * @author amit bhayani
 *
 */
public interface AreaEventInfo {
	
	public AreaDefinition getAreaDefinition();
	
	public OccurrenceInfo getOccurrenceInfo();
	
	/**
	 * IntervalTime ::= INTEGER (1..32767)
     *       -- minimum interval time between area reports in seconds
     *       
	 * @return
	 */
	public Integer getIntervalTime();
}
