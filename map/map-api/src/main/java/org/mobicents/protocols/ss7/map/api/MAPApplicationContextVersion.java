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

package org.mobicents.protocols.ss7.map.api;


/**
 * 
 * @author sergey vetyutnev
 * 
 */
public enum MAPApplicationContextVersion {
	version1(1), version2(2), version3(3), version4(4);

	private int version;

	private MAPApplicationContextVersion(int version) {
		this.version = version;
	}

	public int getVersion() {
		return this.version;
	}

	public static MAPApplicationContextVersion getInstance(Long version) {
		long vers = (long) version;
		switch( (int)vers ) {
		case 1:
			return MAPApplicationContextVersion.version1;
		case 2:
			return MAPApplicationContextVersion.version2;
		case 3:
			return MAPApplicationContextVersion.version3;
		case 4:
			return MAPApplicationContextVersion.version4;
		}
		
		return null;
	}

}
