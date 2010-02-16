package org.mobicents.protocols.ss7.sccp;

import org.mobicents.protocols.ss7.sccp.ud.UnitData;
import org.mobicents.protocols.ss7.sccp.ud.XUnitData;

public interface SccpUnitDataFactory {

	UnitData getUnitData();
	XUnitData getXUnitData();
}
