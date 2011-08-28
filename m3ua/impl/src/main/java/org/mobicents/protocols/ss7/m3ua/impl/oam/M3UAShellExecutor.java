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

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.impl.As;
import org.mobicents.protocols.ss7.m3ua.impl.AspFactory;
import org.mobicents.protocols.ss7.m3ua.impl.M3UAManagement;
import org.mobicents.protocols.ss7.m3ua.impl.as.ClientM3UAManagement;
import org.mobicents.protocols.ss7.m3ua.impl.sg.ServerM3UAManagement;
import org.mobicents.ss7.management.console.ShellExecutor;

/**
 * 
 * @author amit bhayani
 * 
 */
public class M3UAShellExecutor implements ShellExecutor {

	private static final Logger logger = Logger.getLogger(M3UAShellExecutor.class);

	private M3UAManagement m3uaManagement;

	public M3UAShellExecutor() {

	}

	public M3UAManagement getM3uaManagement() {
		return m3uaManagement;
	}

	public void setM3uaManagement(M3UAManagement m3uaManagement) {
		this.m3uaManagement = m3uaManagement;
	}

	public String execute(String[] args) {
		try {
			if (args.length < 3) {
				// any command will have atleast 3 args
				return M3UAOAMMessages.INVALID_COMMAND;
			}

			if (args[1] == null) {
				return M3UAOAMMessages.INVALID_COMMAND;
			}

			// args[0] is m3ua
			if (args[1].equals("ras")) {
				// related to rem AS for SigGatewayImpl
				String rasCmd = args[2];
				if (rasCmd == null) {
					return M3UAOAMMessages.INVALID_COMMAND;
				}

				if (rasCmd.equals("create")) {
					// Create new Rem AS
					As as = this.m3uaManagement.createAppServer(args);
					return String.format(M3UAOAMMessages.CREATE_AS_SUCESSFULL, as.getName());
				} else if (rasCmd.equals("add")) {
					// Add Rem ASP to Rem AS
					if (args[3] == null || args[4] == null) {
						return M3UAOAMMessages.INVALID_COMMAND;
					}

					this.m3uaManagement.assignAspToAs(args[3], args[4]);
					return String.format(M3UAOAMMessages.ADD_ASP_TO_AS_SUCESSFULL, args[4], args[3]);
				}
				return M3UAOAMMessages.INVALID_COMMAND;
			} else if (args[1].equals("rasp")) {
				// related to rem AS for SigGatewayImpl
				String raspCmd = args[2];

				if (raspCmd == null) {
					return M3UAOAMMessages.INVALID_COMMAND;
				}

				if (raspCmd.equals("create")) {
					// Create new Rem ASP
					AspFactory factory = this.m3uaManagement.createAspFactory(args);
					return String.format(M3UAOAMMessages.CREATE_ASP_SUCESSFULL, factory.getName());
				}
				return M3UAOAMMessages.INVALID_COMMAND;
			} else if (args[1].equals("as")) {
				// related to rem AS for SigGatewayImpl
				String rasCmd = args[2];
				if (rasCmd == null) {
					return M3UAOAMMessages.INVALID_COMMAND;
				}

				if (rasCmd.equals("create")) {
					// Create new Rem AS
					As as = this.m3uaManagement.createAppServer(args);
					return String.format(M3UAOAMMessages.CREATE_AS_SUCESSFULL, as.getName());
				} else if (rasCmd.equals("add")) {
					// Add Rem ASP to Rem AS
					if (args[3] == null || args[4] == null) {
						return M3UAOAMMessages.INVALID_COMMAND;
					}

					this.m3uaManagement.assignAspToAs(args[3], args[4]);
					return String.format(M3UAOAMMessages.ADD_ASP_TO_AS_SUCESSFULL, args[4], args[3]);
				}
				return M3UAOAMMessages.INVALID_COMMAND;
			} else if (args[1].equals("asp")) {
				// related to rem AS for SigGatewayImpl
				String raspCmd = args[2];

				if (raspCmd == null) {
					return M3UAOAMMessages.INVALID_COMMAND;
				}

				if (raspCmd.equals("create")) {
					// Create new ASP
					AspFactory factory = this.m3uaManagement.createAspFactory(args);
					return String.format(M3UAOAMMessages.CREATE_ASP_SUCESSFULL, factory.getName());
				}

				if (raspCmd.equals("start")) {
					String aspName = args[3];
					this.m3uaManagement.managementStartAsp(aspName);
					return String.format(M3UAOAMMessages.ASP_START_SUCESSFULL, aspName);
				}

				if (raspCmd.equals("stop")) {
					String aspName = args[3];
					this.m3uaManagement.managementStopAsp(aspName);
					return String.format(M3UAOAMMessages.ASP_STOP_SUCESSFULL, aspName);
				}

				return M3UAOAMMessages.INVALID_COMMAND;
			} else if (args[1].equals("route")) {

				if (!(this.m3uaManagement instanceof ClientM3UAManagement)) {
					return String.format(M3UAOAMMessages.CMD_NOTSUPPORTED_M3UAMANAGEMENT_IS_SERVER, "m3ua route ...");
				}

				String routeCmd = args[2];

				if (routeCmd == null) {
					return M3UAOAMMessages.INVALID_COMMAND;
				}

				if (routeCmd.equals("add")) {
					int dpc = Integer.parseInt(args[3]);

					((ClientM3UAManagement) m3uaManagement).addRouteAsForDpc(dpc, args[4]);
					return String.format(M3UAOAMMessages.ADD_ROUTE_AS_FOR_DPC_SUCCESSFULL, args[4], dpc);
				}

				if (routeCmd.equals("remove")) {
					int dpc = Integer.parseInt(args[3]);

					((ClientM3UAManagement) m3uaManagement).removeRouteAsForDpc(dpc, args[4]);
					return String.format(M3UAOAMMessages.REMOVE_AS_ROUTE_FOR_DPC_SUCCESSFULL, args[4], dpc);
				}

				if (routeCmd.equals("show")) {
					return M3UAOAMMessages.NOT_SUPPORTED_YET;

				}
			}
			return M3UAOAMMessages.INVALID_COMMAND;
		} catch (Exception e) {
			logger.error(String.format("Error while executing comand %s", Arrays.toString(args)), e);
			return e.getMessage();
		}
	}

}
