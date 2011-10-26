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

package org.mobicents.protocols.ss7.m3ua.impl;

/**
 * 
 * @author amit bhayani
 * 
 */
public enum AsState {
	DOWN("DOWN"), INACTIVE("INACTIVE"), ACTIVE("ACTIVE"), PENDING("PENDING");

	private String name;

	private AsState(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public static AsState getState(String name) {
		if (name.equals(DOWN.getName())) {
			return DOWN;
		} else if (name.equals(INACTIVE.getName())) {
			return INACTIVE;
		} else if (name.equals(ACTIVE.getName())) {
			return ACTIVE;
		} else if (name.equals(PENDING.getName())) {
			return PENDING;
		}

		return null;
	}

}
