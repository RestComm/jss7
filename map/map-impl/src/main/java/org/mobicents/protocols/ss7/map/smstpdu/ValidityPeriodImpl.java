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

import org.mobicents.protocols.ss7.map.api.smstpdu.AbsoluteTimeStamp;
import org.mobicents.protocols.ss7.map.api.smstpdu.ValidityEnhancedFormatData;
import org.mobicents.protocols.ss7.map.api.smstpdu.ValidityPeriod;
import org.mobicents.protocols.ss7.map.api.smstpdu.ValidityPeriodFormat;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class ValidityPeriodImpl implements ValidityPeriod {

	private Integer relativeFormatValue;
	private AbsoluteTimeStamp absoluteFormatValue;
	private ValidityEnhancedFormatData enhancedFormatValue;

	public ValidityPeriodImpl(int relativeFormatValue) {
		this.relativeFormatValue = relativeFormatValue;
	}

	public ValidityPeriodImpl(AbsoluteTimeStamp absoluteFormatValue) {
		this.absoluteFormatValue = absoluteFormatValue;
	}

	public ValidityPeriodImpl(ValidityEnhancedFormatData enhancedFormatValue) {
		this.enhancedFormatValue = enhancedFormatValue;
	}

	public ValidityPeriodFormat getValidityPeriodFormat() {
		if (relativeFormatValue != null)
			return ValidityPeriodFormat.fieldPresentRelativeFormat;
		if (absoluteFormatValue != null)
			return ValidityPeriodFormat.fieldPresentAbsoluteFormat;
		if (enhancedFormatValue != null)
			return ValidityPeriodFormat.fieldPresentEnhancedFormat;

		return ValidityPeriodFormat.fieldNotPresent;
	}

	public Integer getRelativeFormatValue() {
		return this.relativeFormatValue;
	}

	public Double getRelativeFormatHours() {

		if (this.relativeFormatValue == null)
			return null;
		
		int i1 = this.relativeFormatValue;
		if (i1 < 0)
			i1 = 0;
		if (i1 > 255)
			i1 = 255;
		if (i1 <= 143) {
			return (i1 + 1) * 5.0 / 60.0;
		}
		if (i1 <= 167) {
			return 12 + (i1 - 143) * 0.5;
		}
		if (i1 <= 196) {
			return (i1 - 166) * 24.0;
		}
		return (i1 - 192) * 24.0 * 7.0;
	}

	public AbsoluteTimeStamp getAbsoluteFormatValue() {
		return this.absoluteFormatValue;
	}

	public ValidityEnhancedFormatData getEnhancedFormatValue() {
		return this.enhancedFormatValue;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("TP-Validity-Period [");

		if (relativeFormatValue != null) {
			sb.append("Relative hours=");
			sb.append(this.getRelativeFormatHours());
		} else if (absoluteFormatValue != null) {
			sb.append("Absolute=");
			sb.append(this.getAbsoluteFormatValue().toString());
		} else if (enhancedFormatValue != null) {
			sb.append("Enhanced=");
			sb.append(this.getEnhancedFormatValue().toString());
		}
		sb.append("]");

		return sb.toString();
	}
}
