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

package org.mobicents.ss7.management.console;

/**
 * Represents the Subject of command formated as "subject <options>".
 * 
 * @author amit bhayani
 * 
 */
public enum Subject {

	/**
	 * linkset subject. Any command to manipulate the linkset, should begin with
	 * linkset subject
	 */
	LINKSET("linkset"),

	SCCP("sccp"),
	/**
	 * M3UA Subject. Any command to manage the M3UA should begin with m3ua
	 * subject
	 */
	M3UA("m3ua");

	private String subject = null;

	private Subject(String subject) {
		this.subject = subject;
	}

	/**
	 * get the string representation of this subject enum
	 * 
	 * @return
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * get the corresponding enum for passed subject String. Returns null if
	 * there is no subject enum defined for passed subject string
	 * 
	 * @param subject
	 * @return
	 */
	public static Subject getSubject(String subject) {
		if (subject.equals(LINKSET.getSubject())) {
			return LINKSET;
		} else if (subject.equals(M3UA.getSubject())) {
			return M3UA;
		} else if (subject.equals(SCCP.getSubject())) {
			return SCCP;
		}
		return null;
	}

}
