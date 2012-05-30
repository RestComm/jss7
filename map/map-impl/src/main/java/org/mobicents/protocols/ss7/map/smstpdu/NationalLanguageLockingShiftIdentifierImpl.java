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

package org.mobicents.protocols.ss7.map.smstpdu;

import org.mobicents.protocols.ss7.map.api.smstpdu.NationalLanguageLockingShiftIdentifier;
import org.mobicents.protocols.ss7.map.api.smstpdu.UserDataHeader;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class NationalLanguageLockingShiftIdentifierImpl extends Gsm7NationalLanguageIdentifierImpl implements NationalLanguageLockingShiftIdentifier {

	public NationalLanguageLockingShiftIdentifierImpl(int nationalLanguageCode) {
		super(nationalLanguageCode);
	}

	public NationalLanguageLockingShiftIdentifierImpl(byte[] encodedInformationElementData) {
		super(encodedInformationElementData);
	}

	public int getEncodedInformationElementIdentifier() {
		return UserDataHeader._InformationElementIdentifier_NationalLanguageLockingShift;
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("NationalLanguageLockingShiftIdentifier [");
		sb.append("nationalLanguageCode=");
		sb.append(this.getNationalLanguageCode());
		sb.append("]");

		return sb.toString();
	}
}
