package org.mobicents.protocols.ss7.m3ua.impl;

import java.util.Arrays;

import javolution.util.FastList;
import javolution.util.FastSet;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.ExchangeType;
import org.mobicents.protocols.ss7.m3ua.Functionality;
import org.mobicents.protocols.ss7.m3ua.IPSPType;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.oam.M3UAOAMMessages;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;

/**
 * <p>
 * Management class to manage the route.
 * </p>
 * <p>
 * The DPC, OPC and SI of Message Signaling unit (MSU) transfered by M3UA-User
 * to M3UA layer for routing is checked against configured key. If found, the
 * corresponding {@link As} is checked for state and if ACTIVE, message will be
 * delivered via this {@link As}. If multiple {@link As} are configured and
 * at-least 2 or more are ACTIVE, then depending on {@link TrafficModeType}
 * configured load-sharing is achieved by using SLS from received MSU.
 * </p>
 * <p>
 * For any given key (combination of DPC, OPC and SI) maximum {@link As} can be
 * configured which acts as route for these key combination.
 * </p>
 * <p>
 * Same {@link As} can serve multiple key combinations.
 * </p>
 * <p>
 * MTP3 Primitive RESUME is delivered to M3UA-User when {@link As} becomes
 * ACTIVE and PAUSE is delivered when {@link As} becomes INACTIVE
 * </p>
 * 
 * @author amit bhayani
 * 
 */
public class M3UARouteManagement {

	private static final Logger logger = Logger.getLogger(M3UARouteManagement.class);

	private static final String KEY_SEPARATOR = ":";
	private static final int WILDCARD = -1;

	private M3UAManagement m3uaManagement = null;

	/**
	 * persists key vs corresponding As that servers for this key
	 */
	protected RouteMap<String, As[]> route = new RouteMap<String, As[]>();

	/**
	 * Persists DPC vs As's serving this DPC. Used for notifying M3UA-user of
	 * MTP3 primitive PAUSE, RESUME.
	 */
	private FastSet<RouteRow> routeTable = new FastSet<RouteRow>();

	private int count = 0;

	// Stores the Set of AS that can route traffic (irrespective of OPC or NI)
	// for given DPC
	protected M3UARouteManagement(M3UAManagement m3uaManagement) {
		this.m3uaManagement = m3uaManagement;
	}

	/**
	 * Reset the routeTable. Called after the persistance state of route is read
	 * from xml file.
	 */
	protected void reset() {
		for (RouteMap.Entry<String, As[]> e = this.route.head(), end = this.route.tail(); (e = e.getNext()) != end;) {
			String key = e.getKey();
			As[] asList = e.getValue();

			try {
				String[] keys = key.split(KEY_SEPARATOR);
				int dpc = Integer.parseInt(keys[0]);
				for (count = 0; count < asList.length; count++) {
					As as = asList[count];
					if (as != null) {
						this.addAsToDPC(dpc, as);
					}
				}
			} catch (Exception ex) {
				logger.error(String.format("Error while adding key=%s to As list=%s", key, Arrays.toString(asList)));
			}
		}
	}

	/**
	 * Creates key (combination of dpc:opc:si) and adds instance of {@link As}
	 * represented by asName as route for this key
	 * 
	 * @param dpc
	 * @param opc
	 * @param si
	 * @param asName
	 * @throws Exception
	 *             If corresponding {@link As} doesn't exist or {@link As}
	 *             already added
	 */
	protected void addRoute(int dpc, int opc, int si, String asName) throws Exception {
		As as = null;
		for (FastList.Node<As> n = this.m3uaManagement.appServers.head(), end = this.m3uaManagement.appServers.tail(); (n = n
				.getNext()) != end;) {
			if (n.getValue().getName().compareTo(asName) == 0) {
				as = n.getValue();
				break;
			}
		}

		if (as == null) {
			throw new Exception(String.format(M3UAOAMMessages.ADD_ASP_TO_AS_FAIL_NO_AS, asName));
		}

		String key = (new StringBuffer().append(dpc).append(KEY_SEPARATOR).append(opc).append(KEY_SEPARATOR).append(si))
				.toString();

		As[] asArray = route.get(key);

		if (asArray != null) {
			// check is this As is already added
			for (int count = 0; count < asArray.length; count++) {
				As asTemp = asArray[count];
				if (asTemp != null && as.equals(asTemp)) {
					throw new Exception(String.format("As=%s already added for dpc=%d opc=%d si=%d", as.getName(), dpc,
							opc, si));
				}
			}
		} else {
			asArray = new As[this.m3uaManagement.maxAsForRoute];
			route.put(key, asArray);
		}

		// Add to first empty slot
		for (int count = 0; count < asArray.length; count++) {
			if (asArray[count] == null) {
				asArray[count] = as;
				this.m3uaManagement.store();

				this.addAsToDPC(dpc, as);
				return;
			}

		}

		throw new Exception(String.format("dpc=%d opc=%d si=%d combination already has maximum possible As",
				as.getName(), dpc, opc, si));
	}

	/**
	 * Removes the {@link As} from key (combination of DPC:OPC:Si)
	 * 
	 * @param dpc
	 * @param opc
	 * @param si
	 * @param asName
	 * @throws Exception
	 *             If no As found, or this As is not serving this key
	 * 
	 */
	protected void removeRoute(int dpc, int opc, int si, String asName) throws Exception {
		As as = null;
		for (FastList.Node<As> n = this.m3uaManagement.appServers.head(), end = this.m3uaManagement.appServers.tail(); (n = n
				.getNext()) != end;) {
			if (n.getValue().getName().compareTo(asName) == 0) {
				as = n.getValue();
				break;
			}
		}

		if (as == null) {
			throw new Exception(String.format(M3UAOAMMessages.ADD_ASP_TO_AS_FAIL_NO_AS, asName));
		}

		String key = (new StringBuffer().append(dpc).append(KEY_SEPARATOR).append(opc).append(KEY_SEPARATOR).append(si))
				.toString();

		As[] asArray = route.get(key);

		if (asArray == null) {
			throw new Exception(String.format("No AS=%s configured  for dpc=%d opc=%d si=%d", as.getName(), dpc, opc,
					si));
		}

		for (int count = 0; count < asArray.length; count++) {
			As asTemp = asArray[count];
			if (asTemp != null && as.equals(asTemp)) {
				asArray[count] = null;
				this.m3uaManagement.store();

				this.removeAsFromDPC(dpc, as);
				return;
			}
		}

		throw new Exception(String.format("No AS=%s configured  for dpc=%d opc=%d si=%d", as.getName(), dpc, opc, si));
	}

	/**
	 * <p>
	 * Get {@link As} that is serving key (combination DPC:OPC:SI). It can
	 * return null if no key configured or all the {@link As} are INACTIVE
	 * </p>
	 * <p>
	 * If two or more {@link As} are active and {@link TrafficModeType}
	 * configured is load-shared, load is configured between each {@link As}
	 * depending on SLS
	 * </p>
	 * 
	 * @param dpc
	 * @param opc
	 * @param si
	 * @param sls
	 * @return
	 */
	protected As getAsForRoute(int dpc, int opc, int si, int sls) {
		// TODO : Loadsharing needs to be implemented

		String key = (new StringBuffer().append(dpc).append(KEY_SEPARATOR).append(opc).append(KEY_SEPARATOR).append(si))
				.toString();
		As[] asArray = route.get(key);

		if (asArray == null) {
			key = (new StringBuffer().append(dpc).append(KEY_SEPARATOR).append(opc).append(KEY_SEPARATOR)
					.append(WILDCARD)).toString();

			asArray = route.get(key);

			if (asArray == null) {
				key = (new StringBuffer().append(dpc).append(KEY_SEPARATOR).append(WILDCARD).append(KEY_SEPARATOR)
						.append(WILDCARD)).toString();

				asArray = route.get(key);
			}
		}

		if (asArray == null) {
			return null;
		}

		for (int count = 0; count < asArray.length; count++) {
			As as = asArray[count];

			FSM fsm = null;
			if (as != null) {
				if (as.getFunctionality() == Functionality.AS
						|| (as.getFunctionality() == Functionality.SGW && as.getExchangeType() == ExchangeType.DE)
						|| (as.getFunctionality() == Functionality.IPSP && as.getExchangeType() == ExchangeType.DE)
						|| (as.getFunctionality() == Functionality.IPSP && as.getExchangeType() == ExchangeType.SE && as
								.getIpspType() == IPSPType.CLIENT)) {
					fsm = as.getPeerFSM();
				} else {
					fsm = as.getLocalFSM();
				}

				AsState asState = AsState.getState(fsm.getState().getName());

				if (asState == AsState.ACTIVE) {
					return as;
				}

			}// if (as != null)
		}// for

		return null;
	}

	private void addAsToDPC(int dpc, As as) {
		RouteRow row = null;
		for (FastSet.Record r = routeTable.head(), end = routeTable.tail(); (r = r.getNext()) != end;) {
			RouteRow value = routeTable.valueOf(r);
			if (value.getDpc() == dpc) {
				row = value;
				break;
			}
		}

		if (row == null) {
			row = new RouteRow(dpc, this.m3uaManagement);
			this.routeTable.add(row);
		}

		row.addServedByAs(as);

	}

	private void removeAsFromDPC(int dpc, As as) {

		// Now decide if we should remove As from RouteRow? If the same As is
		// assigned as route for different key combination we shouldn't remove
		// it from RouteRow
		for (RouteMap.Entry<String, As[]> e = this.route.head(), end = this.route.tail(); (e = e.getNext()) != end;) {
			String key = e.getKey();
			String[] keys = key.split(KEY_SEPARATOR);
			if (keys[0].equals(Integer.toString(dpc))) {
				As[] asList = e.getValue();
				for (count = 0; count < asList.length; count++) {
					As asTemp = asList[count];
					if (asTemp != null && asTemp.equals(as)) {
						return;
					}
				}
			}
		}

		// We reached here means time to remove this As from RouteRow.
		RouteRow row = null;
		for (FastSet.Record r = routeTable.head(), end = routeTable.tail(); (r = r.getNext()) != end;) {
			RouteRow value = routeTable.valueOf(r);
			if (value.getDpc() == dpc) {
				row = value;
				break;
			}
		}

		if (row == null) {
			logger.error(String.format("Removing route As=%s from DPC=%d failed. No RouteRow found!", as, dpc));
		} else {
			row.removeServedByAs(as);
			if (row.servedByAsSize() == 0) {
				this.routeTable.remove(row);
			}
		}
	}

}
