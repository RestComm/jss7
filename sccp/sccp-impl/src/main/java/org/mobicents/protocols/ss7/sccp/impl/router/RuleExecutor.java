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

package org.mobicents.protocols.ss7.sccp.impl.router;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.ss7.linkset.oam.LinkOAMMessages;

/**
 * 
 * @author amit bhayani
 * 
 */
public class RuleExecutor {

	private static final Logger logger = Logger.getLogger(RuleExecutor.class);

	private RouterImpl router = null;

	public RuleExecutor() {

	}

	public RouterImpl getRouter() {
		return router;
	}

	public void setRouter(RouterImpl router) {
		this.router = router;
	}

	public String execute(String[] options) {
		if (this.router == null) {
			logger.warn("Router not set. Command will not be executed ");
			return RuleOAMMessage.SERVER_ERROR;
		}

		// Atleast 1 option is passed?
		if (options == null || options.length < 2) {
			return RuleOAMMessage.INVALID_COMMAND;
		}

		String firstOption = options[1];

		if (firstOption == null) {
			return LinkOAMMessages.INVALID_COMMAND;
		}

		try {
			if (firstOption.equals("show")) {
				// Show
				return this.router.showRules();
			} else if (firstOption.equals("create")) {
				// Create Rule
				return this.createRule(options);
			} else if (firstOption.equals("delete")) {
				// Delete Rule
				return this.deleteRule(options);
			}
		} catch (Exception e) {
			logger.error("Error while executing command ", e);
			return e.toString();
		} catch (Throwable t) {
			return t.toString();
		}

		return RuleOAMMessage.INVALID_COMMAND;
	}

	/**
	 * <p>
	 * Command to create new rule.
	 * </p>
	 * <p>
	 * The valid combination for a command are
	 * <ul>
	 * <li>
	 * <p>
	 * <i>pattern</i> and <i>translation</i>
	 * </p>
	 * </li>
	 * <li>
	 * <p>
	 * <i>pattern</i>, <i>translation</i> and <i>mtpinfo</i>
	 * </p>
	 * </li>
	 * <li>
	 * <p>
	 * <i>pattern</i> and <i>mtpinfo</i>
	 * </p>
	 * </li>
	 * </ul>
	 * </p>
	 * <p>
	 * To know more about these options look at
	 * {@link org.mobicents.protocols.ss7.sccp.impl.router.RouterImpl}
	 * </p>
	 * 
	 * @param options
	 * @return
	 * @throws Exception
	 */
	private String createRule(String[] options) throws Exception {
		// Minimum is 23
		if (options.length < 16) {
			throw new Exception(RuleOAMMessage.INVALID_COMMAND);
		}

		if (!options[1].equals("create")) {
			throw new Exception(RuleOAMMessage.INVALID_COMMAND);
		}

		String name = options[2];
		AddressInformation translation = null;
		AddressInformation pattern = null;
		MTPInfo mtpInfo = null;

		if (options[3].equals("dpc")) {
			int dpc = Integer.parseInt(options[4]);
			if (!options[5].equals("ssn")) {
				throw new Exception(RuleOAMMessage.INVALID_COMMAND);
			}
			int ssn = Integer.parseInt(options[6]);
			if (!options[7].equals("mtpinfo")) {
				throw new Exception(RuleOAMMessage.INVALID_COMMAND);
			}
			mtpInfo = this.getMTPInfo(options, 8);

			Rule rule = new Rule(name, dpc, ssn, mtpInfo);
			return this.router.addRule(rule);

		} else if (options[3].equals("pattern")) {
			// Pattern parsing
			pattern = getAddressInformation(options, 4);

			// Translation parsing
			if (options[14].equals("translation")) {

				// For translation minimum length is 25
				if (options.length < 25) {
					throw new Exception(RuleOAMMessage.INVALID_COMMAND);
				}

				translation = getAddressInformation(options, 15);

				if (options.length > 25) {
					// We have MtpInfo also
					if (options.length < 34) {
						// Hmmm, we don't have everything to parse mtpInfo
						throw new Exception(RuleOAMMessage.INVALID_COMMAND);
					}

					if (!options[25].equals("mtpinfo")) {
						throw new Exception(RuleOAMMessage.INVALID_COMMAND);
					}
					// Lets parse mtpInfo
					mtpInfo = this.getMTPInfo(options, 26);
				}

			} else if (options[14].equals("mtpinfo")) {
				// Mtp Info Parsing
				mtpInfo = this.getMTPInfo(options, 15);
			} else {
				throw new Exception(RuleOAMMessage.INVALID_COMMAND);
			}

			Rule rule = new Rule(name, pattern, translation, mtpInfo);

			return this.router.addRule(rule);
		} else {
			throw new Exception(RuleOAMMessage.INVALID_COMMAND);
		}

	}

	private AddressInformation getAddressInformation(String[] options, int offset) throws Exception {
		if (!options[offset++].equals("tt")) {
			throw new Exception(RuleOAMMessage.INVALID_COMMAND);
		}

		int tt = Integer.parseInt(options[offset++]);

		if (!options[offset++].equals("np")) {
			throw new Exception(RuleOAMMessage.INVALID_COMMAND);
		}

		NumberingPlan np = NumberingPlan.valueOf(Integer.parseInt(options[offset++]));

		if (!options[offset++].equals("noa")) {
			throw new Exception(RuleOAMMessage.INVALID_COMMAND);
		}

		NatureOfAddress noa = NatureOfAddress.valueOf(Integer.parseInt(options[offset++]));

		if (!options[offset++].equals("digits")) {
			throw new Exception(RuleOAMMessage.INVALID_COMMAND);
		}

		String digits = options[offset++];

		if (!options[offset++].equals("ssn")) {
			throw new Exception(RuleOAMMessage.INVALID_COMMAND);
		}

		int ssn = Integer.parseInt(options[offset++]);

		return new AddressInformation(tt, np, noa, digits, ssn);
	}

	private MTPInfo getMTPInfo(String[] options, int offSet) throws Exception {
		if (!options[offSet++].equals("name")) {
			throw new Exception(RuleOAMMessage.INVALID_COMMAND);
		}

		String mtpName = options[offSet++];

		if (!options[offSet++].equals("opc")) {
			throw new Exception(RuleOAMMessage.INVALID_COMMAND);
		}

		int opc = Integer.parseInt(options[offSet++]);

		if (!options[offSet++].equals("apc")) {
			throw new Exception(RuleOAMMessage.INVALID_COMMAND);
		}

		int dpc = Integer.parseInt(options[offSet++]);

		if (!options[offSet++].equals("sls")) {
			throw new Exception(RuleOAMMessage.INVALID_COMMAND);
		}

		int sls = Integer.parseInt(options[offSet++]);

		return new MTPInfo(mtpName, opc, dpc, sls);
	}

	/**
	 * Delete the existing Rule. Command is sccprule delete <rule-name>. For
	 * example sccprule delete Rule1
	 * 
	 * @param options
	 * @return
	 * @throws Exception
	 */
	private String deleteRule(String[] options) throws Exception {

		if (options.length < 3) {
			return RuleOAMMessage.INVALID_COMMAND;
		}

		String name = options[2];

		if (name == null) {
			throw new Exception(LinkOAMMessages.INVALID_COMMAND);
		}

		return this.router.deleteRule(name);
	}
}
