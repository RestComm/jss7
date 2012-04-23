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

package org.mobicents.protocols.ss7.sccp.impl.router;

/**
 * Class for exception concerning rule.
 * @author baranowb
 *
 */
public class RuleException extends RuntimeException {

	private Rule rule;
	/**
	 * 
	 */
	public RuleException(Rule rule) {
		this.rule = rule;
	}

	/**
	 * @param message
	 */
	public RuleException(String message,Rule rule) {
		super(message);
		this.rule = rule;
	}

	/**
	 * @param cause
	 */
	public RuleException(Throwable cause,Rule rule) {
		super(cause);
		this.rule = rule;
	}

	/**
	 * @param message
	 * @param cause
	 */
	public RuleException(String message, Throwable cause,Rule rule) {
		super(message, cause);
		this.rule = rule;
	}

	
	public String toString() {
		return "RuleException [rule=" + rule + ", toString()=" + super.toString() + "]";
	}

}
