/**
 * 
 */
package org.mobicents.protocols.ss7.sccp.impl.ud;

import org.mobicents.protocols.ss7.sccp.SccpUnitDataFactory;
import org.mobicents.protocols.ss7.sccp.ud.UnitData;
import org.mobicents.protocols.ss7.sccp.ud.XUnitData;

/**
 * @author baranowb
 * 
 */
public class SccpUnitDataFactoryImpl implements SccpUnitDataFactory {

	public UnitData getUnitData() {
		// TODO Auto-generated method stub
		return new UnitDataImpl();
	}

	public XUnitData getXUnitData() {
		// TODO Auto-generated method stub
		return new XUnitDataImpl();
	}

}
