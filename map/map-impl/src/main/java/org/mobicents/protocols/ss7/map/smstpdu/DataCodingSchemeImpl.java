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

import org.mobicents.protocols.ss7.map.api.smstpdu.CharacterSet;
import org.mobicents.protocols.ss7.map.api.smstpdu.DataCodingGroup;
import org.mobicents.protocols.ss7.map.api.smstpdu.DataCodingSchemaIndicationType;
import org.mobicents.protocols.ss7.map.api.smstpdu.DataCodingScheme;
import org.mobicents.protocols.ss7.map.api.smstpdu.DataCodingSchemaMessageClass;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class DataCodingSchemeImpl implements DataCodingScheme {

	private int code;

	public DataCodingSchemeImpl(int code) {
		this.code = code;
	}
	
	public DataCodingSchemeImpl(DataCodingGroup dataCodingGroup, DataCodingSchemaMessageClass messageClass,
			DataCodingSchemaIndicationType dataCodingSchemaIndicationType, Boolean setIndicationActive, CharacterSet characterSet, boolean isCompressed) {

		if (dataCodingGroup == null)
			return;

		switch(dataCodingGroup){
		case GeneralGroup:
		case MessageMarkedForAutomaticDeletionGroup:
			this.code = (dataCodingGroup == DataCodingGroup.MessageMarkedForAutomaticDeletionGroup ? 0x40 : 0) | (isCompressed ? 0x20 : 0)
					| (messageClass != null ? (0x10 | messageClass.getCode()) : 0) | (characterSet != null ? (characterSet.getCode() << 2) : 0);
			break;
		case MessageWaitingIndicationGroupDiscardMessage:
		case MessageWaitingIndicationGroupStoreMessage:
			int i1;
			if (dataCodingGroup == DataCodingGroup.MessageWaitingIndicationGroupDiscardMessage) {
				i1 = 0xC0;
			} else {
				if (characterSet != null && characterSet == CharacterSet.UCS2)
					i1 = 0xE0;
				else
					i1 = 0xD0;
			}
			this.code = i1 | (setIndicationActive ? 0x08 : 0) | (dataCodingSchemaIndicationType != null ? (dataCodingSchemaIndicationType.getCode()) : 0);
			break;
		case DataCodingMessageClass:
			this.code = 0xF0 | (characterSet == CharacterSet.GSM8 ? 0x04 : 0) | (messageClass != null ? (messageClass.getCode()) : 0);
			break;
		}
	}

	@Override
	public int getCode() {
		return this.code;
	}

	@Override
	public DataCodingGroup getDataCodingGroup() {
		switch ((this.code & 0xC0) >> 6) {
		case 0:
			return DataCodingGroup.GeneralGroup;
		case 1:
			return DataCodingGroup.MessageMarkedForAutomaticDeletionGroup;
		case 2:
			return DataCodingGroup.Reserved;
		default: {
			switch ((this.code & 0x30) >> 4) {
			case 0:
				return DataCodingGroup.MessageWaitingIndicationGroupDiscardMessage;
			case 1:
			case 2:
				return DataCodingGroup.MessageWaitingIndicationGroupStoreMessage;
			default:
				return DataCodingGroup.DataCodingMessageClass;
			}
		}
		}
	}

	@Override
	public DataCodingSchemaMessageClass getMessageClass() {
		DataCodingGroup dcg = this.getDataCodingGroup();
		switch(dcg){
		case GeneralGroup:
		case MessageMarkedForAutomaticDeletionGroup:
			if ((this.code & 0x10) != 0)
				return DataCodingSchemaMessageClass.getInstance(this.code & 0x03);
			else
				return null;
		case DataCodingMessageClass:
			return DataCodingSchemaMessageClass.getInstance(this.code & 0x03);
		default:
			return null;
		}
	}

	@Override
	public DataCodingSchemaIndicationType getDataCodingSchemaIndicationType() {
		DataCodingGroup dcg = this.getDataCodingGroup();
		switch (dcg) {
		case MessageWaitingIndicationGroupDiscardMessage:
		case MessageWaitingIndicationGroupStoreMessage:
			return DataCodingSchemaIndicationType.getInstance(this.code & 0x03);
		default:
			return null;
		}
	}

	@Override
	public Boolean getSetIndicationActive() {
		DataCodingGroup dcg = this.getDataCodingGroup();
		switch (dcg) {
		case MessageWaitingIndicationGroupDiscardMessage:
		case MessageWaitingIndicationGroupStoreMessage:
			if ((this.code & 0x08) != 0)
				return true;
			else
				return false;
		default:
			return null;
		}
	}

	@Override
	public CharacterSet getCharacterSet() {

		int cg1 = (this.code & 0xC0) >> 6;
		switch (cg1) {
		case 0:
		case 1:
			int cs = (this.code & 0x0C) >> 2;
			return CharacterSet.getInstance(cs);
		case 3:
			int cg2 = (this.code & 0x30) >> 4;
			switch(cg2) {
			case 0:
			case 1:
				return CharacterSet.GSM7;
			case 2:
				return CharacterSet.UCS2;
			default:
				if( (this.code & 0x04) == 0 )
					return CharacterSet.GSM7;
				else
					return CharacterSet.GSM8;
			}
		default:
			return CharacterSet.Reserved;
		}
	}

	@Override
	public boolean getIsCompressed() {
		if (((this.code & 0xC0) == 0 || (this.code & 0xC0) == 0x40) && (this.code & 0x20) != 0)
			return true;
		else
			return false;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("TP-Data-Coding-Scheme [");
		sb.append("Code=");
		sb.append(this.code);
		sb.append(", DataCodingGroup=");
		sb.append(this.getDataCodingGroup());
		if (this.getMessageClass() != null) {
			sb.append(", MessageClass=");
			sb.append(this.getMessageClass());
		}
		if (this.getDataCodingSchemaIndicationType() != null) {
			sb.append(", IndicationType=");
			sb.append(this.getDataCodingSchemaIndicationType());
		}
		if (this.getSetIndicationActive() != null) {
			sb.append(", SetIndicationActive=");
			sb.append(this.getSetIndicationActive());
		}
		sb.append(", CharacterSet=");
		sb.append(this.getCharacterSet());
		if (this.getIsCompressed())
			sb.append(", Compressed");
		sb.append("]");

		return sb.toString();
	}
}
