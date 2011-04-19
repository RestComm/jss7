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
 * From GSM 09.02 Version 5.15.1 Release 1996
 * 
 * MAP-OpenInfo ::= SEQUENCE { destinationReference [0] AddressString
 * originationReference [1] AddressString ..., extensionContainer
 * ExtensionContainer -- extensionContainer must not be used in version 2 }
 * 
 * 
 * @author amit bhayani
 * 
 */
public interface MAPOpenInfo {

	/**
	 * Get the destination {@link AddressString}
	 * 
	 * @return
	 */
	public AddressString getDestReference();

	/**
	 * Set the destination {@link AddressString}
	 * 
	 * @param destReference
	 */
	public void setDestReference(AddressString destReference);

	/**
	 * Get the originating {@link AddressString}
	 * 
	 * @return
	 */
	public AddressString getOrigReference();

	/**
	 * Set the originating {@link AddressString}
	 * 
	 * @param origReference
	 */
	public void setOrigReference(AddressString origReference);

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

}
