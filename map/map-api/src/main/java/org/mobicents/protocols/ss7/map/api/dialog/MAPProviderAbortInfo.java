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

package org.mobicents.protocols.ss7.map.api.dialog;

import org.mobicents.protocols.ss7.map.api.MAPDialog;

/**
 * MAP-ProviderAbortInfo ::= SEQUENCE { map-ProviderAbortReason
 * MAP-ProviderAbortReason, ..., extensionContainer ExtensionContainer --
 * extensionContainer must not be used in version 2 }
 * 
 * 
 * @author amit bhayani
 * 
 */
public interface MAPProviderAbortInfo {

	/**
	 * Get the {@link MAPDialog} for which this event is fired
	 * 
	 * @return
	 */
	public MAPDialog getMAPDialog();

	/**
	 * Set the {@link MAPDialog}
	 * 
	 * @param mapDialog
	 */
	public void setMAPDialog(MAPDialog mapDialog);

	/**
	 * Set the {@link MAPProviderAbortReason} indicating the reason why stack
	 * aborted this MAP Dialog
	 * 
	 * @param mapProvAbrtReas
	 */
	public void setMAPProviderAbortReason(MAPProviderAbortReason mapProvAbrtReas);

	/**
	 * get the {@link MAPProviderAbortReason} indicating the reason why stack
	 * aborted this MAP Dialog
	 * 
	 * @return
	 */
	public MAPProviderAbortReason getMAPProviderAbortReason();

	/**
	 * Get the {@link MAPExtensionContainer}
	 * 
	 * @return
	 */
	public MAPExtensionContainer getExtensionContainer();

	/**
	 * Set the {@link MAPExtensionContainer}
	 * 
	 * @param extensionContainer
	 */
	public void setExtensionContainer(MAPExtensionContainer extensionContainer);

}
