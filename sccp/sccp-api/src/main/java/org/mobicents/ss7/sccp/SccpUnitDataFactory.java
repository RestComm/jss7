package org.mobicents.ss7.sccp;

import org.mobicents.ss7.sccp.ud.UnitData;
import org.mobicents.ss7.sccp.ud.XUnitData;

public interface SccpUnitDataFactory {

	UnitData getUnitData();
	XUnitData getXUnitData();
}
