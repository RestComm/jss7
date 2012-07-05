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

package org.mobicents.protocols.ss7.m3ua.impl.oam;

import java.util.Arrays;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.ExchangeType;
import org.mobicents.protocols.ss7.m3ua.Functionality;
import org.mobicents.protocols.ss7.m3ua.IPSPType;
import org.mobicents.protocols.ss7.m3ua.impl.As;
import org.mobicents.protocols.ss7.m3ua.impl.AspFactory;
import org.mobicents.protocols.ss7.m3ua.impl.M3UAManagement;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.mobicents.protocols.ss7.m3ua.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;
import org.mobicents.ss7.management.console.ShellExecutor;

/**
 * 
 * @author amit bhayani
 * 
 */
public class M3UAShellExecutor implements ShellExecutor {

	private static final Logger logger = Logger.getLogger(M3UAShellExecutor.class);

	private M3UAManagement m3uaManagement;

	protected ParameterFactory parameterFactory = new ParameterFactoryImpl();

	public M3UAShellExecutor() {

	}

	public M3UAManagement getM3uaManagement() {
		return m3uaManagement;
	}

	public void setM3uaManagement(M3UAManagement m3uaManagement) {
		this.m3uaManagement = m3uaManagement;
	}

	/**
	 * m3ua as create <as-name> <AS | SGW | IPSP> mode <SE | DE> ipspType <
	 * client | server > rc <routing-context> traffic-mode <traffic mode>
	 * network-appearance <network appearance>
	 * 
	 * @param args
	 * @return
	 */
	private String createAs(String[] args) throws Exception {
		if (args.length < 5 || args.length > 15) {
			return M3UAOAMMessages.INVALID_COMMAND;
		}

		// Create new Rem AS
		String asName = args[3];
		if (asName == null) {
			return M3UAOAMMessages.INVALID_COMMAND;
		}

		Functionality functionlaity = Functionality.getFunctionality(args[4]);
		ExchangeType exchangeType = null;
		IPSPType ipspType = null;
		RoutingContext rc = null;
		TrafficModeType trafficModeType = null;
		NetworkAppearance na = null;

		if (functionlaity == null) {
			return M3UAOAMMessages.INVALID_COMMAND;
		}

		int count = 5;

		while (count < args.length) {
			String key = args[count++];
			if (key == null) {
				return M3UAOAMMessages.INVALID_COMMAND;
			}

			if (key.equals("mode")) {
				exchangeType = ExchangeType.getExchangeType(args[count++]);
				if (exchangeType == null) {
					return M3UAOAMMessages.INVALID_COMMAND;
				}
			} else if (key.equals("ipspType")) {
				ipspType = IPSPType.getIPSPType(args[count++]);
			} else if (key.equals("rc")) {
				long rcLong = Long.parseLong(args[count++]);
				rc = parameterFactory.createRoutingContext(new long[] { rcLong });
			} else if (key.equals("traffic-mode")) {
				trafficModeType = getTrafficModeType(args[count++]);
			} else if (key.equals("network-appearance")) {
				na = parameterFactory.createNetworkAppearance(Long.parseLong(args[count++]));
			} else {
				return M3UAOAMMessages.INVALID_COMMAND;
			}
		}

		As as = this.m3uaManagement.createAs(asName, functionlaity, exchangeType, ipspType, rc, trafficModeType, na);
		return String.format(M3UAOAMMessages.CREATE_AS_SUCESSFULL, as.getName());
	}

	/**
	 * m3ua as destroy <as-name>
	 * 
	 * @param args
	 * @return
	 * @throws Exception
	 */
	private String destroyAs(String[] args) throws Exception {
		if (args.length < 4) {
			return M3UAOAMMessages.INVALID_COMMAND;
		}

		String asName = args[3];
		if (asName == null) {
			return M3UAOAMMessages.INVALID_COMMAND;
		}

		As as = this.m3uaManagement.destroyAs(asName);

		return String.format(M3UAOAMMessages.DESTROY_AS_SUCESSFULL, asName);
	}

	/**
	 * m3ua as add <as-name> <asp-name>
	 * 
	 * @param args
	 * @return
	 * @throws Exception
	 */
	private String addAspToAs(String[] args) throws Exception {
		if (args.length < 5) {
			return M3UAOAMMessages.INVALID_COMMAND;
		}

		// Add Rem ASP to Rem AS
		if (args[3] == null || args[4] == null) {
			return M3UAOAMMessages.INVALID_COMMAND;
		}

		this.m3uaManagement.assignAspToAs(args[3], args[4]);
		return String.format(M3UAOAMMessages.ADD_ASP_TO_AS_SUCESSFULL, args[4], args[3]);
	}

	/**
	 * m3ua as remove <as-name> <asp-name>
	 * 
	 * @param args
	 * @return
	 * @throws Exception
	 */
	private String removeAspFromAs(String[] args) throws Exception {
		if (args.length < 5) {
			return M3UAOAMMessages.INVALID_COMMAND;
		}

		// Add Rem ASP to Rem AS
		if (args[3] == null || args[4] == null) {
			return M3UAOAMMessages.INVALID_COMMAND;
		}

		this.m3uaManagement.unassignAspFromAs(args[3], args[4]);
		return String.format(M3UAOAMMessages.REMOVE_ASP_FROM_AS_SUCESSFULL, args[4], args[3]);
	}

	private TrafficModeType getTrafficModeType(String mode) {
		int iMode = -1;
		if (mode == null) {
			return null;
		} else if (mode.equals("loadshare")) {
			iMode = TrafficModeType.Loadshare;
		} else if (mode.equals("override")) {
			iMode = TrafficModeType.Override;
		} else if (mode.equals("broadcast")) {
			iMode = TrafficModeType.Broadcast;
		} else {
			return null;
		}

		return this.parameterFactory.createTrafficModeType(iMode);
	}

	private String showAspFactories() {
		FastList<AspFactory> aspfactories = this.m3uaManagement.getAspfactories();
		if (aspfactories.size() == 0) {
			return M3UAOAMMessages.NO_ASP_DEFINED_YET;
		}
		StringBuffer sb = new StringBuffer();
		for (FastList.Node<AspFactory> n = aspfactories.head(), end = aspfactories.tail(); (n = n.getNext()) != end;) {
			AspFactory aspFactory = n.getValue();
			sb.append(M3UAOAMMessages.NEW_LINE);
			aspFactory.show(sb);
			sb.append(M3UAOAMMessages.NEW_LINE);
		}
		return sb.toString();
	}

	private String showRoutes() {
		FastMap<String, As[]> route = this.m3uaManagement.getRoute();

		if (route.size() == 0) {
			return M3UAOAMMessages.NO_ROUTE_DEFINED_YET;
		}
		StringBuffer sb = new StringBuffer();
		for (FastMap.Entry<String, As[]> e = route.head(), end = route.tail(); (e = e.getNext()) != end;) {
			String key = e.getKey();
			As[] asList = e.getValue();

			sb.append(M3UAOAMMessages.NEW_LINE);
			sb.append(key);
			sb.append(M3UAOAMMessages.TAB);
			for (int i = 0; i < asList.length; i++) {
				As as = asList[i];
				if (as != null) {
					sb.append(as.getName());
					sb.append(M3UAOAMMessages.COMMA);
				}
			}
			sb.append(M3UAOAMMessages.NEW_LINE);
		}
		return sb.toString();
	}

	private String showAs() {
		FastList<As> appServers = this.m3uaManagement.getAppServers();
		if (appServers.size() == 0) {
			return M3UAOAMMessages.NO_AS_DEFINED_YET;
		}
		StringBuffer sb = new StringBuffer();
		for (FastList.Node<As> n = appServers.head(), end = appServers.tail(); (n = n.getNext()) != end;) {
			As as = n.getValue();
			sb.append(M3UAOAMMessages.NEW_LINE);
			as.show(sb);
			sb.append(M3UAOAMMessages.NEW_LINE);
		}
		return sb.toString();
	}

	private String executeM3UA(String[] args) {
		try {
			if (args.length < 3 || args.length > 15) {
				// any command will have atleast 3 args
				return M3UAOAMMessages.INVALID_COMMAND;
			}

			if (args[1] == null) {
				return M3UAOAMMessages.INVALID_COMMAND;
			}

			if (args[1].equals("as")) {
				// related to rem AS for SigGatewayImpl
				String rasCmd = args[2];
				if (rasCmd == null) {
					return M3UAOAMMessages.INVALID_COMMAND;
				}

				if (rasCmd.equals("create")) {
					return this.createAs(args);
				} else if (rasCmd.equals("destroy")) {
					return this.destroyAs(args);
				} else if (rasCmd.equals("add")) {
					return this.addAspToAs(args);
				} else if (rasCmd.equals("remove")) {
					return this.removeAspFromAs(args);
				} else if (rasCmd.equals("show")) {
					return this.showAs();
				}
				return M3UAOAMMessages.INVALID_COMMAND;
			} else if (args[1].equals("asp")) {

				if (args.length > 5) {
					return M3UAOAMMessages.INVALID_COMMAND;
				}

				// related to rem AS for SigGatewayImpl
				String raspCmd = args[2];

				if (raspCmd == null) {
					return M3UAOAMMessages.INVALID_COMMAND;
				} else if (raspCmd.equals("create")) {
					// Create new ASP
					if (args.length < 5) {
						return M3UAOAMMessages.INVALID_COMMAND;
					}

					String aspname = args[3];
					String assocName = args[4];

					if (aspname == null || assocName == null) {
						return M3UAOAMMessages.INVALID_COMMAND;
					}

					AspFactory factory = this.m3uaManagement.createAspFactory(aspname, assocName);
					return String.format(M3UAOAMMessages.CREATE_ASP_SUCESSFULL, factory.getName());
				} else if (raspCmd.equals("destroy")) {
					if (args.length < 4) {
						return M3UAOAMMessages.INVALID_COMMAND;
					}

					String aspName = args[3];
					this.m3uaManagement.destroyAspFactory(aspName);
					return String.format(M3UAOAMMessages.DESTROY_ASP_SUCESSFULL, aspName);

				} else if (raspCmd.equals("show")) {
					return this.showAspFactories();

				} else if (raspCmd.equals("start")) {
					if (args.length < 4) {
						return M3UAOAMMessages.INVALID_COMMAND;
					}

					String aspName = args[3];
					this.m3uaManagement.startAsp(aspName);
					return String.format(M3UAOAMMessages.ASP_START_SUCESSFULL, aspName);
				} else if (raspCmd.equals("stop")) {
					if (args.length < 4) {
						return M3UAOAMMessages.INVALID_COMMAND;
					}

					String aspName = args[3];
					this.m3uaManagement.stopAsp(aspName);
					return String.format(M3UAOAMMessages.ASP_STOP_SUCESSFULL, aspName);
				}

				return M3UAOAMMessages.INVALID_COMMAND;
			} else if (args[1].equals("route")) {

				String routeCmd = args[2];

				if (routeCmd == null) {
					return M3UAOAMMessages.INVALID_COMMAND;
				}

				if (routeCmd.equals("add")) {

					if (args.length < 5 || args.length > 7) {
						return M3UAOAMMessages.INVALID_COMMAND;
					}

					int count = 3;
					String asName = args[count++];
					int dpc = -1;
					int opc = -1;
					int si = -1;

					if (asName == null) {
						return M3UAOAMMessages.INVALID_COMMAND;
					}

					dpc = Integer.parseInt(args[count++]);

					while (count < args.length) {
						opc = Integer.parseInt(args[count++]);
						si = Integer.parseInt(args[count++]);
					}

					this.m3uaManagement.addRoute(dpc, opc, si, asName);

					return String.format(M3UAOAMMessages.ADD_ROUTE_AS_FOR_DPC_SUCCESSFULL, asName, dpc);
				}

				if (routeCmd.equals("remove")) {

					if (args.length < 5 || args.length > 7) {
						return M3UAOAMMessages.INVALID_COMMAND;
					}

					int count = 3;
					String asName = args[count++];
					int dpc = -1;
					int opc = -1;
					int si = -1;

					if (asName == null) {
						return M3UAOAMMessages.INVALID_COMMAND;
					}

					dpc = Integer.parseInt(args[count++]);

					while (count < args.length) {
						opc = Integer.parseInt(args[count++]);
						si = Integer.parseInt(args[count++]);
					}

					this.m3uaManagement.removeRoute(dpc, opc, si, asName);
					return String.format(M3UAOAMMessages.REMOVE_AS_ROUTE_FOR_DPC_SUCCESSFULL, asName, dpc);
				}

				if (routeCmd.equals("show")) {
					return this.showRoutes();

				}
			}
			return M3UAOAMMessages.INVALID_COMMAND;
		} catch (Exception e) {
			logger.error(String.format("Error while executing comand %s", Arrays.toString(args)), e);
			return e.getMessage();
		}
	}

	public String execute(String[] args) {
		if (args[0].equals("m3ua")) {
			return this.executeM3UA(args);
		}
		return M3UAOAMMessages.INVALID_COMMAND;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.ss7.management.console.ShellExecutor#handles(java.lang.
	 * String)
	 */
	@Override
	public boolean handles(String command) {
		return (command.startsWith("m3ua"));
	}

}
