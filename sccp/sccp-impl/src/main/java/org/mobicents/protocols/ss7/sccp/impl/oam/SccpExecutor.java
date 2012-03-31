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

package org.mobicents.protocols.ss7.sccp.impl.oam;

import javolution.util.FastMap;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.indicator.AddressIndicator;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.protocols.ss7.sccp.impl.ConcernedSignalingPointCode;
import org.mobicents.protocols.ss7.sccp.impl.RemoteSignalingPointCode;
import org.mobicents.protocols.ss7.sccp.impl.RemoteSubSystem;
import org.mobicents.protocols.ss7.sccp.impl.SccpResource;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.router.LongMessageRule;
import org.mobicents.protocols.ss7.sccp.impl.router.LongMessageRuleType;
import org.mobicents.protocols.ss7.sccp.impl.router.Mtp3Destination;
import org.mobicents.protocols.ss7.sccp.impl.router.Mtp3ServiceAccessPoint;
import org.mobicents.protocols.ss7.sccp.impl.router.Router;
import org.mobicents.protocols.ss7.sccp.impl.router.Rule;
import org.mobicents.protocols.ss7.sccp.impl.router.RuleType;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * 
 * @author amit bhayani
 * 
 */
public class SccpExecutor {

	private static final Logger logger = Logger.getLogger(SccpExecutor.class);

	private Router router = null;
	private SccpResource sccpResource = null;
	private SccpStackImpl sccpStack = null;

	public SccpExecutor() {

	}

	public void setSccpStack(SccpStackImpl sccpStack) {
		this.router = sccpStack.getRouter();
		this.sccpResource = sccpStack.getSccpResource();
		this.sccpStack = sccpStack;
	}


	public String execute(String[] options) {
		if (this.router == null || this.sccpResource == null) {
			logger.warn("Router not set. Command will not be executed ");
			return SccpOAMMessage.SERVER_ERROR;
		}

		// Atleast 1 option is passed?
		if (options == null || options.length < 2) {
			return SccpOAMMessage.INVALID_COMMAND;
		}

		String firstOption = options[1];

		if (firstOption == null) {
			return SccpOAMMessage.INVALID_COMMAND;
		}

		try {
			if (firstOption.equals("rule")) {
				return this.manageRule(options);
			} else if (firstOption.equals("primary_add")) {
				return this.managePrimAddress(options);
			} else if (firstOption.equals("backup_add")) {
				return this.manageBackupAddress(options);
			} else if (firstOption.equals("rsp")) {
				return this.manageRsp(options);
			} else if (firstOption.equals("rss")) {
				return this.manageRss(options);
			} else if (firstOption.equals("lmr")) {
				return this.manageLmr(options);
			} else if (firstOption.equals("sap")) {
				return this.manageSap(options);
			} else if (firstOption.equals("dest")) {
				return this.manageDest(options);
			} else if (firstOption.equals("csp")) {
				return this.manageConcernedSpc(options);
			} else if (firstOption.equals("set")) {
				return this.manageSet(options);
			} else if (firstOption.equals("get")) {
				return this.manageGet(options);
			}
		} catch (Exception e) {
			logger.error("Error while executing command ", e);
			return e.toString();
		} catch (Throwable t) {
			return t.toString();
		}

		return SccpOAMMessage.INVALID_COMMAND;
	}

	private String manageRss(String[] options) throws Exception {
		// Minimum 3 needed. Show
		if (options.length < 3) {
			return SccpOAMMessage.INVALID_COMMAND;
		}

		String command = options[2];

		if (command == null) {
			return SccpOAMMessage.INVALID_COMMAND;
		}

		if (command.equals("create")) {
			if (options.length < 7) {
				return SccpOAMMessage.INVALID_COMMAND;
			}

			int remoteSsId = Integer.parseInt(options[3]);
			if (this.sccpResource.getRemoteSsn(remoteSsId) != null) {
				return SccpOAMMessage.RSS_ALREADY_EXIST;
			}

			int remoteSpc = Integer.parseInt(options[4]);

			// TODO check if RemoteSignalingPointCode correspond to remoteSpc
			// exist?

			int remoteSs = Integer.parseInt(options[5]);
			int remoteSsFlag = Integer.parseInt(options[6]);
			boolean markProhibitedWhenSpcResuming = false;
			if (options.length >= 8) {
				if (!options[7].toLowerCase().equals("prohibitedwhenspcresuming")) {
					return SccpOAMMessage.INVALID_COMMAND;
				} else {
					markProhibitedWhenSpcResuming = true;
				}
			}

			RemoteSubSystem rsscObj = new RemoteSubSystem(remoteSpc, remoteSs, remoteSsFlag, markProhibitedWhenSpcResuming);
			this.sccpResource.addRemoteSsn(remoteSsId, rsscObj);
			this.sccpResource.store();

			return SccpOAMMessage.RSS_SUCCESSFULLY_ADDED;
		} else if (command.equals("modify")) {
			if (options.length < 7) {
				return SccpOAMMessage.INVALID_COMMAND;
			}
			int remoteSsId = Integer.parseInt(options[3]);
			if (this.sccpResource.getRemoteSsn(remoteSsId) == null) {
				return SccpOAMMessage.RSS_DOESNT_EXIST;
			}

			int remoteSpc = Integer.parseInt(options[4]);

			// TODO check if RemoteSignalingPointCode correspond to remoteSpc
			// exist?

			int remoteSs = Integer.parseInt(options[5]);
			int remoteSsFlag = Integer.parseInt(options[6]);
			boolean markProhibitedWhenSpcResuming = false;
			if (options.length >= 8) {
				if (!options[7].toLowerCase().equals("prohibitedwhenspcresuming")) {
					return SccpOAMMessage.INVALID_COMMAND;
				} else {
					markProhibitedWhenSpcResuming = true;
				}
			}

			RemoteSubSystem rsscObj = new RemoteSubSystem(remoteSpc, remoteSs, remoteSsFlag, markProhibitedWhenSpcResuming);
			this.sccpResource.addRemoteSsn(remoteSsId, rsscObj);
			this.sccpResource.store();

			return SccpOAMMessage.RSS_SUCCESSFULLY_MODIFIED;
		} else if (command.equals("delete")) {
			if (options.length < 4) {
				return SccpOAMMessage.INVALID_COMMAND;
			}
			int remoteSsId = Integer.parseInt(options[3]);
			if (this.sccpResource.getRemoteSsn(remoteSsId) == null) {
				return SccpOAMMessage.RSS_DOESNT_EXIST;
			}

			this.sccpResource.removeRemoteSsn(remoteSsId);
			this.sccpResource.store();
			return SccpOAMMessage.RSS_SUCCESSFULLY_DELETED;
		} else if (command.equals("show")) {
			if (options.length == 4) {
				int remoteSsId = Integer.parseInt(options[3]);
				RemoteSubSystem rss = this.sccpResource.getRemoteSsn(remoteSsId);
				if (rss == null) {
					return SccpOAMMessage.RSS_DOESNT_EXIST;
				}
				return rss.toString();
			}
			
			if(this.sccpResource.getRemoteSsns().size() == 0){
				return SccpOAMMessage.RSS_DOESNT_EXIST;
			}

			StringBuffer sb = new StringBuffer();
			for (FastMap.Entry<Integer, RemoteSubSystem> e = this.sccpResource.getRemoteSsns().head(), end = this.sccpResource
					.getRemoteSsns().tail(); (e = e.getNext()) != end;) {
				int key = e.getKey();
				RemoteSubSystem rss = e.getValue();
				sb.append("key=");
				sb.append(key);
				sb.append("  ");
				sb.append(rss);
				sb.append("\n");
			}
			return sb.toString();
		}

		return SccpOAMMessage.INVALID_COMMAND;
	}

	private String manageRsp(String[] options) throws Exception {
		// Minimum 3 needed. Show
		if (options.length < 3) {
			return SccpOAMMessage.INVALID_COMMAND;
		}

		String command = options[2];

		if (command == null) {
			return SccpOAMMessage.INVALID_COMMAND;
		}

		if (command.equals("create")) {
			if (options.length < 7) {
				return SccpOAMMessage.INVALID_COMMAND;
			}

			int remoteSpcId = Integer.parseInt(options[3]); //FIXME: Amit is this ok? - to use ID?
			if (this.sccpResource.getRemoteSpc(remoteSpcId) != null) {
				return SccpOAMMessage.RSPC_ALREADY_EXIST;
			}

			int remoteSpc = Integer.parseInt(options[4]);
			int remoteSpcFlag = Integer.parseInt(options[5]);
			int mask = Integer.parseInt(options[6]);

			RemoteSignalingPointCode rspcObj = new RemoteSignalingPointCode(remoteSpc, remoteSpcFlag, mask);
			this.sccpResource.addRemoteSpc(remoteSpcId, rspcObj);
			this.sccpResource.store();

			return SccpOAMMessage.RSPC_SUCCESSFULLY_ADDED;
		} else if (command.equals("modify")) {
			if (options.length < 7) {
				return SccpOAMMessage.INVALID_COMMAND;
			}

			int remoteSpcId = Integer.parseInt(options[3]);
			if (this.sccpResource.getRemoteSpc(remoteSpcId) == null) {
				return SccpOAMMessage.RSPC_DOESNT_EXIST;
			}

			int remoteSpc = Integer.parseInt(options[4]);
			int remoteSpcFlag = Integer.parseInt(options[5]);
			int mask = Integer.parseInt(options[6]);

			RemoteSignalingPointCode rspcObj = new RemoteSignalingPointCode(remoteSpc, remoteSpcFlag, mask);
			this.sccpResource.addRemoteSpc(remoteSpcId, rspcObj);
			this.sccpResource.store();

			return SccpOAMMessage.RSPC_SUCCESSFULLY_MODIFIED;
		} else if (command.equals("delete")) {
			if (options.length < 4) {
				return SccpOAMMessage.INVALID_COMMAND;
			}
			int remoteSpcId = Integer.parseInt(options[3]);
			if (this.sccpResource.getRemoteSpc(remoteSpcId) == null) {
				return SccpOAMMessage.RSPC_DOESNT_EXIST;
			}

			this.sccpResource.removeRemoteSpc(remoteSpcId);
			this.sccpResource.store();
			return SccpOAMMessage.RSPC_SUCCESSFULLY_DELETED;

		} else if (command.equals("show")) {
			if (options.length == 4) {
				int remoteSpcId = Integer.parseInt(options[3]);
				RemoteSignalingPointCode rspc = this.sccpResource.getRemoteSpc(remoteSpcId);
				if (rspc == null) {
					return SccpOAMMessage.RSPC_DOESNT_EXIST;
				}

				return rspc.toString();
			}

			if(this.sccpResource.getRemoteSpcs().size() == 0){
				return SccpOAMMessage.RSPC_DOESNT_EXIST;
			}
			
			StringBuffer sb = new StringBuffer();
			for (FastMap.Entry<Integer, RemoteSignalingPointCode> e = this.sccpResource.getRemoteSpcs().head(), end = this.sccpResource
					.getRemoteSpcs().tail(); (e = e.getNext()) != end;) {
				int key = e.getKey();
				RemoteSignalingPointCode rsp = e.getValue();
				sb.append("key=");
				sb.append(key);
				sb.append("  ");
				sb.append(rsp);
				sb.append("\n");
			}
			return sb.toString();

		}

		return SccpOAMMessage.INVALID_COMMAND;
	}

	private String managePrimAddress(String[] options) throws Exception {
		// Minimum 3 needed. Show
		if (options.length < 3) {
			return SccpOAMMessage.INVALID_COMMAND;
		}

		String command = options[2];

		if (command == null) {
			return SccpOAMMessage.INVALID_COMMAND;
		}

		if (command.equals("create")) {
			if (options.length < 4) {
				return SccpOAMMessage.INVALID_COMMAND;
			}
			int primAddressId = Integer.parseInt(options[3]);
			if (this.router.getPrimaryAddress(primAddressId) != null) {
				return SccpOAMMessage.ADDRESS_ALREADY_EXIST;
			}
			SccpAddress primAddress = this.createAddress(options, 4);

			this.router.addPrimaryAddress(primAddressId, primAddress);
			this.router.store();
			return SccpOAMMessage.ADDRESS_SUCCESSFULLY_ADDED;
		} else if (command.equals("modify")) {
			if (options.length < 4) {
				return SccpOAMMessage.INVALID_COMMAND;
			}
			int primAddressId = Integer.parseInt(options[3]);
			if (this.router.getPrimaryAddress(primAddressId) == null) {
				return SccpOAMMessage.ADDRESS_DOESNT_EXIST;
			}
			SccpAddress primAddress = this.createAddress(options, 4);

			this.router.addPrimaryAddress(primAddressId, primAddress);
			this.router.store();
			return SccpOAMMessage.ADDRESS_SUCCESSFULLY_MODIFIED;
		} else if (command.equals("delete")) {
			if (options.length < 4) {
				return SccpOAMMessage.INVALID_COMMAND;
			}
			int primAddressId = Integer.parseInt(options[3]);
			if (this.router.getPrimaryAddress(primAddressId) == null) {
				return SccpOAMMessage.ADDRESS_DOESNT_EXIST;
			}
			this.router.removePrimaryAddress(primAddressId);
			this.router.store();
			return SccpOAMMessage.ADDRESS_SUCCESSFULLY_DELETED;

		} else if (command.equals("show")) {

			if (options.length == 4) {
				int primAddressId = Integer.parseInt(options[3]);
				SccpAddress pa = this.router.getPrimaryAddress(primAddressId);
				if (pa == null) {
					return SccpOAMMessage.ADDRESS_DOESNT_EXIST;
				}
				return pa.toString();
			}
			
			if(this.router.getPrimaryAddresses().size() == 0){
				return SccpOAMMessage.ADDRESS_DOESNT_EXIST;
			}

			StringBuffer sb = new StringBuffer();
			for (FastMap.Entry<Integer, SccpAddress> e = this.router.getPrimaryAddresses().head(), end = this.router
					.getPrimaryAddresses().tail(); (e = e.getNext()) != end;) {
				int key = e.getKey();
				SccpAddress address = e.getValue();
				sb.append("key=");
				sb.append(key);
				sb.append("  ");
				sb.append(address);
				sb.append("\n");
			}
			return sb.toString();
		}

		return SccpOAMMessage.INVALID_COMMAND;
	}

	private String manageBackupAddress(String[] options) throws Exception {
		// Minimum 3 needed. Show
		if (options.length < 3) {
			return SccpOAMMessage.INVALID_COMMAND;
		}

		String command = options[2];

		if (command == null) {
			return SccpOAMMessage.INVALID_COMMAND;
		}

		if (command.equals("create")) {
			if (options.length < 4) {
				return SccpOAMMessage.INVALID_COMMAND;
			}
			int backupAddressId = Integer.parseInt(options[3]);
			if (this.router.getBackupAddress(backupAddressId) != null) {
				return SccpOAMMessage.ADDRESS_ALREADY_EXIST;
			}
			SccpAddress backupAddress = this.createAddress(options, 4);

			this.router.addBackupAddress(backupAddressId, backupAddress);
			this.router.store();
			return SccpOAMMessage.ADDRESS_SUCCESSFULLY_ADDED;
		} else if (command.equals("modify")) {
			if (options.length < 4) {
				return SccpOAMMessage.INVALID_COMMAND;
			}
			int backupAddressId = Integer.parseInt(options[3]);
			if (this.router.getBackupAddress(backupAddressId) == null) {
				return SccpOAMMessage.ADDRESS_DOESNT_EXIST;
			}
			SccpAddress backupAddress = this.createAddress(options, 4);

			this.router.addBackupAddress(backupAddressId, backupAddress);
			this.router.store();
			return SccpOAMMessage.ADDRESS_SUCCESSFULLY_MODIFIED;

		} else if (command.equals("delete")) {
			if (options.length < 4) {
				return SccpOAMMessage.INVALID_COMMAND;
			}
			int backupAddressId = Integer.parseInt(options[3]);
			if (this.router.getBackupAddress(backupAddressId) == null) {
				return SccpOAMMessage.ADDRESS_DOESNT_EXIST;
			}
			this.router.removeBackupAddress(backupAddressId);
			this.router.store();
			return SccpOAMMessage.ADDRESS_SUCCESSFULLY_DELETED;
		} else if (command.equals("show")) {
			if (options.length == 4) {
				int backupAddressId = Integer.parseInt(options[3]);
				SccpAddress pa = this.router.getBackupAddress(backupAddressId);
				if (pa == null) {
					return SccpOAMMessage.ADDRESS_DOESNT_EXIST;
				}
				return pa.toString();
			}
			
			if(this.router.getBackupAddresses().size() == 0){
				return SccpOAMMessage.ADDRESS_DOESNT_EXIST;
			}

			StringBuffer sb = new StringBuffer();
			for (FastMap.Entry<Integer, SccpAddress> e = this.router.getBackupAddresses().head(), end = this.router
					.getBackupAddresses().tail(); (e = e.getNext()) != end;) {
				int key = e.getKey();
				SccpAddress address = e.getValue();
				sb.append("key=");
				sb.append(key);
				sb.append("  backup_add=");
				sb.append(address);
				sb.append("\n");
			}
			return sb.toString();

		}

		return SccpOAMMessage.INVALID_COMMAND;
	}

	private String manageRule(String[] options) throws Exception {
		// Minimum 3 needed. Show
		if (options.length < 3) {
			return SccpOAMMessage.INVALID_COMMAND;
		}

		String command = options[2];

		if (command == null) {
			return SccpOAMMessage.INVALID_COMMAND;
		}

		if (command.equals("create")) {
			return this.createRule(options);
		} else if (command.equals("modify")) {
			return this.modifyRule(options);
		} else if (command.equals("delete")) {
			return this.deleteRule(options);
		} else if (command.equals("show")) {
			return this.showRule(options);
		}

		return SccpOAMMessage.INVALID_COMMAND;
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
	 * {@link org.mobicents.protocols.ss7.sccp.impl.router.Router}
	 * </p>
	 * 
	 * @param options
	 * @return
	 * @throws Exception
	 */
	private String createRule(String[] options) throws Exception {
		// Minimum is 13
		if (options.length < 14) {
			return SccpOAMMessage.INVALID_COMMAND;
		}
		int ruleId;
		Rule rule = null;
		ruleId = Integer.parseInt(options[3]);

		rule = this.router.getRule(ruleId);

		if (rule != null) {
			return SccpOAMMessage.RULE_ALREADY_EXIST;
		}

		String mask = options[4];

		if (mask == null) {
			return SccpOAMMessage.INVALID_MASK;
		}

		RuleType ruleType;
		String s1 = options[12].toLowerCase();
		if (s1.equals("solitary")) {
			ruleType = RuleType.Solitary;
		} else if (s1.equals("dominant")) {
			ruleType = RuleType.Dominant;
		} else if (s1.equals("loadshared")) {
			ruleType = RuleType.Loadshared;
		} else {
			return SccpOAMMessage.INVALID_COMMAND;
		}
		
		int pAddressId = Integer.parseInt(options[13]);

		SccpAddress pAddress = this.router.getPrimaryAddress(pAddressId);
		if (pAddress == null) {
			return String.format(SccpOAMMessage.NO_PRIMARY_ADDRESS, pAddressId);
		}

		int sAddressId = -1;
		if (options.length > 14) {
			sAddressId = Integer.parseInt(options[14]);
			SccpAddress sAddress = this.router.getBackupAddress(sAddressId);
			if (sAddress == null) {
				return String.format(SccpOAMMessage.NO_BACKUP_ADDRESS, sAddressId);
			}
		}

		if (sAddressId == -1 && ruleType != RuleType.Solitary) {
			return SccpOAMMessage.INVALID_COMMAND;
		}

		SccpAddress pattern = this.createAddress(options, 5);

		rule = new Rule(ruleType, pattern, mask);
		rule.setPrimaryAddressId(pAddressId);
		rule.setSecondaryAddressId(sAddressId);
		this.router.addRule(ruleId, rule);
		this.router.store();
		return SccpOAMMessage.RULE_SUCCESSFULLY_ADDED;
	}

	private String modifyRule(String[] options) throws Exception {
		// Minimum is 13
		if (options.length < 13) {
			return SccpOAMMessage.INVALID_COMMAND;
		}
		int ruleId;
		Rule rule = null;
		ruleId = Integer.parseInt(options[3]);

		rule = this.router.getRule(ruleId);

		if (rule == null) {
			return SccpOAMMessage.RULE_DOESNT_EXIST;
		}

		String mask = options[4];

		if (mask == null) {
			return SccpOAMMessage.INVALID_MASK;
		}

		RuleType ruleType;
		String s1 = options[12].toLowerCase();
		if (s1.equals("solitary")) {
			ruleType = RuleType.Solitary;
		} else if (s1.equals("dominant")) {
			ruleType = RuleType.Dominant;
		} else if (s1.equals("loadshared")) {
			ruleType = RuleType.Loadshared;
		} else {
			return SccpOAMMessage.INVALID_COMMAND;
		}

		int pAddressId = Integer.parseInt(options[13]);

		SccpAddress pAddress = this.router.getPrimaryAddress(pAddressId);
		if (pAddress == null) {
			return String.format(SccpOAMMessage.NO_PRIMARY_ADDRESS, pAddressId);
		}

		int sAddressId = -1;
		if (options.length > 14) {
			sAddressId = Integer.parseInt(options[14]);
			SccpAddress sAddress = this.router.getBackupAddress(sAddressId);
			if (sAddress == null) {
				return String.format(SccpOAMMessage.NO_BACKUP_ADDRESS, sAddressId);
			}
		}

		if (sAddressId == -1 && ruleType != RuleType.Solitary) {
			return SccpOAMMessage.INVALID_COMMAND;
		}

		SccpAddress pattern = this.createAddress(options, 5);

		rule = new Rule(ruleType, pattern, mask);
		rule.setPrimaryAddressId(pAddressId);
		rule.setSecondaryAddressId(sAddressId);
		this.router.addRule(ruleId, rule);
		this.router.store();
		return SccpOAMMessage.RULE_SUCCESSFULLY_MODIFIED;
	}

	/**
	 * Command is "sccp rule delete <id>"
	 * 
	 * @param options
	 * @return
	 * @throws Exception
	 */
	private String deleteRule(String[] options) throws Exception {
		// Minimum is 4
		if (options.length < 4) {
			return SccpOAMMessage.INVALID_COMMAND;
		}
		int ruleId;
		ruleId = Integer.parseInt(options[3]);

		if (this.router.getRule(ruleId) == null) {
			return SccpOAMMessage.RULE_DOESNT_EXIST;
		}
		this.router.removeRule(ruleId);
		this.router.store();

		return SccpOAMMessage.RULE_SUCCESSFULLY_REMOVED;
	}

	/**
	 * Command is "sccp rule show <id>" where id is optional. If id is not
	 * passed, all rules configured are shown
	 * 
	 * @param options
	 * @return
	 * @throws Exception
	 */
	private String showRule(String[] options) throws Exception {
		// Minimum is 4
		if (options.length < 3) {
			return SccpOAMMessage.INVALID_COMMAND;
		}

		int ruleId = -1;
		if (options.length == 4) {
			ruleId = Integer.parseInt(options[3]);
			Rule rule = this.router.getRule(ruleId);
			if (rule == null) {
				return SccpOAMMessage.RULE_DOESNT_EXIST;
			}
			return rule.toString();
		}
		
		if(this.router.getRules().size() == 0){
			return SccpOAMMessage.RULE_DOESNT_EXIST;
		}

		StringBuffer sb = new StringBuffer();
		for (FastMap.Entry<Integer, Rule> e = this.router.getRules().head(), end = this.router.getRules().tail(); (e = e
				.getNext()) != end;) {
			int key = e.getKey();
			Rule rule = e.getValue();
			sb.append("key=");
			sb.append(key);
			sb.append("  Rule=");
			sb.append(rule);
			sb.append("\n");
		}

		return sb.toString();
	}

	private SccpAddress createAddress(String[] options, int index) throws Exception {
		SccpAddress sccpAddress = null;

		int ai = Integer.parseInt(options[index++]);
		int pc = 0;
		int ssn = 0;

		AddressIndicator aiObj = new AddressIndicator((byte) ai);
		pc = Integer.parseInt(options[index++]);
		ssn = Integer.parseInt(options[index++]);
		int tt = Integer.parseInt(options[index++]);
		NumberingPlan np = NumberingPlan.valueOf(Integer.parseInt(options[index++]));
		NatureOfAddress nai = NatureOfAddress.valueOf(Integer.parseInt(options[index++]));

		String digits = options[index++];

		GlobalTitle gt = null;

		switch (aiObj.getGlobalTitleIndicator()) {
		case GLOBAL_TITLE_INCLUDES_NATURE_OF_ADDRESS_INDICATOR_ONLY:
			gt = GlobalTitle.getInstance(nai, digits);
			break;
		case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY:
			gt = GlobalTitle.getInstance(tt, digits);
			break;
		case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_AND_ENCODING_SCHEME:
			gt = GlobalTitle.getInstance(tt, np, digits);
			break;
		case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_ENCODING_SCHEME_AND_NATURE_OF_ADDRESS:
			gt = GlobalTitle.getInstance(tt, np, nai, digits);
			break;

		case NO_GLOBAL_TITLE_INCLUDED:
			gt = GlobalTitle.getInstance(digits);
			break;
		}

		sccpAddress = new SccpAddress(aiObj.getRoutingIndicator(), pc, gt, ssn);

		return sccpAddress;
	}

	private String manageLmr(String[] options) throws Exception {
		// Minimum 3 needed. Show
		if (options.length < 3) {
			return SccpOAMMessage.INVALID_COMMAND;
		}

		String command = options[2];

		if (command == null) {
			return SccpOAMMessage.INVALID_COMMAND;
		}

		if (command.equals("create")) {
			if (options.length < 7) {
				return SccpOAMMessage.INVALID_COMMAND;
			}

			int lmrId = Integer.parseInt(options[3]);
			if (this.router.getLongMessageRule(lmrId) != null) {
				return SccpOAMMessage.LMR_ALREADY_EXIST;
			}

			int firstSpc = Integer.parseInt(options[4]);
			int lastSpc = Integer.parseInt(options[5]);

			LongMessageRuleType ruleType;
			String s1 = options[6].toLowerCase();
			if (s1.equals("udt")) {
				ruleType = LongMessageRuleType.LongMessagesForbidden;
			} else if (s1.equals("xudt")) {
				ruleType = LongMessageRuleType.XudtEnabled;
			} else if (s1.equals("ludt")) {
				ruleType = LongMessageRuleType.LudtEnabled;
			} else if (s1.equals("ludt_segm")) {
				ruleType = LongMessageRuleType.LudtEnabled_WithSegmentationField;
			} else {
				return SccpOAMMessage.INVALID_COMMAND;
			}

			LongMessageRule lmr = new LongMessageRule(firstSpc, lastSpc, ruleType);
			this.router.addLongMessageRule(lmrId, lmr);
			this.router.store();

			return SccpOAMMessage.LMR_SUCCESSFULLY_ADDED;
		} else if (command.equals("modify")) {
			if (options.length < 7) {
				return SccpOAMMessage.INVALID_COMMAND;
			}
			int lmrId = Integer.parseInt(options[3]);
			if (this.router.getLongMessageRule(lmrId) == null) {
				return SccpOAMMessage.LMR_DOESNT_EXIST;
			}

			int firstSpc = Integer.parseInt(options[4]);
			int lastSpc = Integer.parseInt(options[5]);

			LongMessageRuleType ruleType;
			String s1 = options[6].toLowerCase();
			if (s1.equals("udt")) {
				ruleType = LongMessageRuleType.LongMessagesForbidden;
			} else if (s1.equals("xudt")) {
				ruleType = LongMessageRuleType.XudtEnabled;
			} else if (s1.equals("ludt")) {
				ruleType = LongMessageRuleType.LudtEnabled;
			} else if (s1.equals("ludt_segm")) {
				ruleType = LongMessageRuleType.LudtEnabled_WithSegmentationField;
			} else {
				return SccpOAMMessage.INVALID_COMMAND;
			}

			LongMessageRule lmr = new LongMessageRule(firstSpc, lastSpc, ruleType);
			this.router.addLongMessageRule(lmrId, lmr);
			this.router.store();

			return SccpOAMMessage.LMR_SUCCESSFULLY_MODIFIED;
		} else if (command.equals("delete")) {
			if (options.length < 4) {
				return SccpOAMMessage.INVALID_COMMAND;
			}
			int lmrId = Integer.parseInt(options[3]);
			if (this.router.getLongMessageRule(lmrId) == null) {
				return SccpOAMMessage.LMR_DOESNT_EXIST;
			}

			this.router.removeLongMessageRule(lmrId);
			this.router.store();

			return SccpOAMMessage.LMR_SUCCESSFULLY_DELETED;
		} else if (command.equals("show")) {
			if (options.length == 4) {
				int lmrId = Integer.parseInt(options[3]);
				LongMessageRule lmr = this.router.getLongMessageRule(lmrId);
				if (lmr == null) {
					return SccpOAMMessage.LMR_DOESNT_EXIST;
				}
				return lmr.toString();
			}

			if (this.router.getLongMessageRules().size() == 0) {
				return SccpOAMMessage.LMR_DOESNT_EXIST;
			}

			StringBuffer sb = new StringBuffer();
			for (FastMap.Entry<Integer, LongMessageRule> e = this.router.getLongMessageRules().head(), end = this.router.getLongMessageRules().tail(); (e = e
					.getNext()) != end;) {
				int key = e.getKey();
				LongMessageRule lmr = e.getValue();
				sb.append("key=");
				sb.append(key);
				sb.append("  ");
				sb.append(lmr);
				sb.append("\n");
			}
			return sb.toString();
		}

		return SccpOAMMessage.INVALID_COMMAND;
	}

	private String manageSap(String[] options) throws Exception {
		// Minimum 3 needed. Show
		if (options.length < 3) {
			return SccpOAMMessage.INVALID_COMMAND;
		}

		String command = options[2];

		if (command == null) {
			return SccpOAMMessage.INVALID_COMMAND;
		}

		if (command.equals("create")) {
			if (options.length < 7) {
				return SccpOAMMessage.INVALID_COMMAND;
			}

			int sapId = Integer.parseInt(options[3]);
			if (this.router.getMtp3ServiceAccessPoint(sapId) != null) {
				return SccpOAMMessage.SAP_ALREADY_EXIST;
			}

			int mtp3Id = Integer.parseInt(options[4]);
			int opc = Integer.parseInt(options[5]);
			int ni = Integer.parseInt(options[6]);

			if (this.sccpStack.getMtp3UserPart(mtp3Id) == null) {
				return SccpOAMMessage.MUP_DOESNT_EXIST;
			}

			Mtp3ServiceAccessPoint sap = new Mtp3ServiceAccessPoint(mtp3Id, opc, ni);
			this.router.addMtp3ServiceAccessPoint(sapId, sap);
			this.router.store();

			return SccpOAMMessage.SAP_SUCCESSFULLY_ADDED;
		} else if (command.equals("modify")) {
			if (options.length < 7) {
				return SccpOAMMessage.INVALID_COMMAND;
			}
			int sapId = Integer.parseInt(options[3]);
			if (this.router.getMtp3ServiceAccessPoint(sapId) == null) {
				return SccpOAMMessage.SAP_DOESNT_EXIST;
			}

			int mtp3Id = Integer.parseInt(options[4]);
			int opc = Integer.parseInt(options[5]);
			int ni = Integer.parseInt(options[6]);

			if (this.sccpStack.getMtp3UserPart(mtp3Id) == null) {
				return SccpOAMMessage.MUP_DOESNT_EXIST;
			}

			Mtp3ServiceAccessPoint sap = new Mtp3ServiceAccessPoint(mtp3Id, opc, ni);
			this.router.addMtp3ServiceAccessPoint(sapId, sap);
			this.router.store();

			return SccpOAMMessage.SAP_SUCCESSFULLY_MODIFIED;
		} else if (command.equals("delete")) {
			if (options.length < 4) {
				return SccpOAMMessage.INVALID_COMMAND;
			}
			int sapId = Integer.parseInt(options[3]);
			if (this.router.getMtp3ServiceAccessPoint(sapId) == null) {
				return SccpOAMMessage.SAP_DOESNT_EXIST;
			}

			this.router.removeMtp3ServiceAccessPoint(sapId);
			this.router.store();

			return SccpOAMMessage.SAP_SUCCESSFULLY_DELETED;
		} else if (command.equals("show")) {
			if (options.length == 4) {
				int sapId = Integer.parseInt(options[3]);
				Mtp3ServiceAccessPoint sap = this.router.getMtp3ServiceAccessPoint(sapId);
				if (sap == null) {
					return SccpOAMMessage.SAP_DOESNT_EXIST;
				}
				return sap.toString();
			}

			if (this.router.getMtp3ServiceAccessPoints().size() == 0) {
				return SccpOAMMessage.SAP_DOESNT_EXIST;
			}

			StringBuffer sb = new StringBuffer();
			for (FastMap.Entry<Integer, Mtp3ServiceAccessPoint> e = this.router.getMtp3ServiceAccessPoints().head(), end = this.router
					.getMtp3ServiceAccessPoints().tail(); (e = e.getNext()) != end;) {
				int key = e.getKey();
				Mtp3ServiceAccessPoint sap = e.getValue();
				sb.append("key=");
				sb.append(key);
				sb.append("  ");
				sb.append(sap);
				sb.append("\n");
			}
			return sb.toString();
		}

		return SccpOAMMessage.INVALID_COMMAND;
	}

	private String manageDest(String[] options) throws Exception {
		// Minimum 4 needed. Show
		if (options.length < 4) {
			return SccpOAMMessage.INVALID_COMMAND;
		}

		String command = options[2];

		if (command == null) {
			return SccpOAMMessage.INVALID_COMMAND;
		}

		if (command.equals("create")) {
			if (options.length < 10) {
				return SccpOAMMessage.INVALID_COMMAND;
			}

			int sapId = Integer.parseInt(options[3]);
			Mtp3ServiceAccessPoint sap = this.router.getMtp3ServiceAccessPoint(sapId);
			if (sap == null) {
				return SccpOAMMessage.SAP_DOESNT_EXIST;
			}
			int destId = Integer.parseInt(options[4]);
			if (sap.getMtp3Destination(destId) != null) {
				return SccpOAMMessage.DEST_ALREADY_EXIST;
			}

			int firstDpc = Integer.parseInt(options[5]);
			int lastDpc = Integer.parseInt(options[6]);
			int firstSls = Integer.parseInt(options[7]);
			int lastSls = Integer.parseInt(options[8]);
			int slsMask = Integer.parseInt(options[9]);

			Mtp3Destination dest = new Mtp3Destination(firstDpc, lastDpc, firstSls, lastSls, slsMask);
			sap.addMtp3Destination(destId, dest);
			this.router.store();

			return SccpOAMMessage.DEST_SUCCESSFULLY_ADDED;
		} else if (command.equals("modify")) {
			if (options.length < 10) {
				return SccpOAMMessage.INVALID_COMMAND;
			}
			
			int sapId = Integer.parseInt(options[3]);
			Mtp3ServiceAccessPoint sap = this.router.getMtp3ServiceAccessPoint(sapId);
			if (sap == null) {
				return SccpOAMMessage.SAP_DOESNT_EXIST;
			}
			int destId = Integer.parseInt(options[4]);
			if (sap.getMtp3Destination(destId) == null) {
				return SccpOAMMessage.DEST_DOESNT_EXIST;
			}

			int firstDpc = Integer.parseInt(options[5]);
			int lastDpc = Integer.parseInt(options[6]);
			int firstSls = Integer.parseInt(options[7]);
			int lastSls = Integer.parseInt(options[8]);
			int slsMask = Integer.parseInt(options[9]);

			Mtp3Destination dest = new Mtp3Destination(firstDpc, lastDpc, firstSls, lastSls, slsMask);
			sap.addMtp3Destination(destId, dest);
			this.router.store();

			return SccpOAMMessage.DEST_SUCCESSFULLY_MODIFIED;
		} else if (command.equals("delete")) {
			if (options.length < 5) {
				return SccpOAMMessage.INVALID_COMMAND;
			}

			int sapId = Integer.parseInt(options[3]);
			Mtp3ServiceAccessPoint sap = this.router.getMtp3ServiceAccessPoint(sapId);
			if (sap == null) {
				return SccpOAMMessage.SAP_DOESNT_EXIST;
			}
			int destId = Integer.parseInt(options[4]);
			if (sap.getMtp3Destination(destId) == null) {
				return SccpOAMMessage.DEST_DOESNT_EXIST;
			}

			sap.removeMtp3Destination(destId);
			this.router.store();

			return SccpOAMMessage.DEST_SUCCESSFULLY_DELETED;
		} else if (command.equals("show")) {
			if (options.length == 5) {
				int sapId = Integer.parseInt(options[3]);
				Mtp3ServiceAccessPoint sap = this.router.getMtp3ServiceAccessPoint(sapId);
				if (sap == null) {
					return SccpOAMMessage.SAP_DOESNT_EXIST;
				}
				int destId = Integer.parseInt(options[4]);
				Mtp3Destination dest = sap.getMtp3Destination(destId);
				if (dest == null) {
					return SccpOAMMessage.DEST_DOESNT_EXIST;
				}

				return dest.toString();
			}

			if (options.length == 4) {
				int sapId = Integer.parseInt(options[3]);
				Mtp3ServiceAccessPoint sap = this.router.getMtp3ServiceAccessPoint(sapId);
				if (sap == null) {
					return SccpOAMMessage.SAP_DOESNT_EXIST;
				}
				return sap.toString();
			}

			return SccpOAMMessage.INVALID_COMMAND;
		}

		return SccpOAMMessage.INVALID_COMMAND;
	}

	private String manageConcernedSpc(String[] options) throws Exception {
		// Minimum 3 needed. Show
		if (options.length < 3) {
			return SccpOAMMessage.INVALID_COMMAND;
		}

		String command = options[2];

		if (command == null) {
			return SccpOAMMessage.INVALID_COMMAND;
		}

		if (command.equals("create")) {
			if (options.length < 5) {
				return SccpOAMMessage.INVALID_COMMAND;
			}

			int concernedSpcId = Integer.parseInt(options[3]);
			if (this.sccpResource.getConcernedSpc(concernedSpcId) != null) {
				return SccpOAMMessage.CS_ALREADY_EXIST;
			}

			int spc = Integer.parseInt(options[4]);

			ConcernedSignalingPointCode conSpc = new ConcernedSignalingPointCode(spc);
			this.sccpResource.addConcernedSpc(concernedSpcId, conSpc);
			this.sccpResource.store();

			return SccpOAMMessage.CS_SUCCESSFULLY_ADDED;
		} else if (command.equals("modify")) {
			if (options.length < 5) {
				return SccpOAMMessage.INVALID_COMMAND;
			}

			int concernedSpcId = Integer.parseInt(options[3]);
			if (this.sccpResource.getConcernedSpc(concernedSpcId) == null) {
				return SccpOAMMessage.CS_DOESNT_EXIST;
			}

			int spc = Integer.parseInt(options[4]);

			ConcernedSignalingPointCode conSpc = new ConcernedSignalingPointCode(spc);
			this.sccpResource.addConcernedSpc(concernedSpcId, conSpc);
			this.sccpResource.store();

			return SccpOAMMessage.CS_SUCCESSFULLY_MODIFIED;
		} else if (command.equals("delete")) {
			if (options.length < 4) {
				return SccpOAMMessage.INVALID_COMMAND;
			}
			int concernedSpcId = Integer.parseInt(options[3]);
			if (this.sccpResource.getConcernedSpc(concernedSpcId) == null) {
				return SccpOAMMessage.CS_DOESNT_EXIST;
			}

			this.sccpResource.removeConcernedSpc(concernedSpcId);
			this.sccpResource.store();
			return SccpOAMMessage.CS_SUCCESSFULLY_DELETED;
		} else if (command.equals("show")) {
			if (options.length == 4) {
				int concernedSpcId = Integer.parseInt(options[3]);
				ConcernedSignalingPointCode conSpc = this.sccpResource.getConcernedSpc(concernedSpcId);
				if (conSpc == null) {
					return SccpOAMMessage.CS_DOESNT_EXIST;
				}
				return conSpc.toString();
			}

			if(this.sccpResource.getConcernedSpcs().size() == 0){
				return SccpOAMMessage.CS_DOESNT_EXIST;
			}

			StringBuffer sb = new StringBuffer();
			for (FastMap.Entry<Integer, ConcernedSignalingPointCode> e = this.sccpResource.getConcernedSpcs().head(), end = this.sccpResource
					.getConcernedSpcs().tail(); (e = e.getNext()) != end;) {
				int key = e.getKey();
				ConcernedSignalingPointCode ConcSpc = e.getValue();
				sb.append("key=");
				sb.append(key);
				sb.append("  ");
				sb.append(ConcSpc);
				sb.append("\n");
			}
			return sb.toString();
		}

		return SccpOAMMessage.INVALID_COMMAND;
	}

	private String manageSet(String[] options) throws Exception {
		// Minimum 4 needed. Show
		if (options.length < 4) {
			return SccpOAMMessage.INVALID_COMMAND;
		}

		String parName = options[2].toLowerCase();

		if (parName.equals("zmarginxudtmessage")) {
			int val = Integer.parseInt(options[3]);
			this.sccpStack.setZMarginXudtMessage(val);
		} else if (parName.equals("reassemblytimerdelay")) {
			int val = Integer.parseInt(options[3]);
			this.sccpStack.setReassemblyTimerDelay(val);
		} else if (parName.equals("maxdatamessage")) {
			int val = Integer.parseInt(options[3]);
			this.sccpStack.setMaxDataMessage(val);
		} else if (parName.equals("removespc")) {
			boolean val = Boolean.parseBoolean(options[3]);
			this.sccpStack.setRemoveSpc(val);
		} else if (parName.equals("ssttimerduration_min")) {
			int val = Integer.parseInt(options[3]);
			this.sccpStack.setSstTimerDuration_Min(val);
		} else if (parName.equals("ssttimerduration_max")) {
			int val = Integer.parseInt(options[3]);
			this.sccpStack.setSstTimerDuration_Max(val);
		} else if (parName.equals("ssttimerduration_increasefactor")) {
			double val = Double.parseDouble(options[3]);
			this.sccpStack.setSstTimerDuration_IncreaseFactor(val);
		} else {
			return SccpOAMMessage.INVALID_COMMAND;
		}

		return SccpOAMMessage.PARAMETER_SUCCESSFULLY_SET;
	}

	private String manageGet(String[] options) throws Exception {
		// Minimum 2 needed. Show
		if (options.length < 2) {
			return SccpOAMMessage.INVALID_COMMAND;
		}

		if (options.length == 3) {
			String parName = options[2].toLowerCase();

			StringBuilder sb = new StringBuilder();
			sb.append(options[2]);
			sb.append(" = ");
			if (parName.equals("zmarginxudtmessage")) {
				sb.append(this.sccpStack.getZMarginXudtMessage());
			} else if (parName.equals("reassemblytimerdelay")) {
				sb.append(this.sccpStack.getReassemblyTimerDelay());
			} else if (parName.equals("maxdatamessage")) {
				sb.append(this.sccpStack.getMaxDataMessage());
			} else if (parName.equals("removespc")) {
				sb.append(this.sccpStack.isRemoveSpc());
			} else if (parName.equals("ssttimerduration_min")) {
				sb.append(this.sccpStack.getSstTimerDuration_Min());
			} else if (parName.equals("ssttimerduration_max")) {
				sb.append(this.sccpStack.getSstTimerDuration_Max());
			} else if (parName.equals("ssttimerduration_increasefactor")) {
				sb.append(this.sccpStack.getSstTimerDuration_IncreaseFactor());
			} else {
				return SccpOAMMessage.INVALID_COMMAND;
			}

			return sb.toString();
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append("zMarginXudtMessage = ");
			sb.append(this.sccpStack.getZMarginXudtMessage());
			sb.append("\n");

			sb.append("reassemblyTimerDelay = ");
			sb.append(this.sccpStack.getReassemblyTimerDelay());
			sb.append("\n");

			sb.append("maxDataMessage = ");
			sb.append(this.sccpStack.getMaxDataMessage());
			sb.append("\n");

			sb.append("removeSpc = ");
			sb.append(this.sccpStack.isRemoveSpc());
			sb.append("\n");

			sb.append("sstTimerDuration_Min = ");
			sb.append(this.sccpStack.getSstTimerDuration_Min());
			sb.append("\n");

			sb.append("sstTimerDuration_Max = ");
			sb.append(this.sccpStack.getSstTimerDuration_Max());
			sb.append("\n");

			sb.append("sstTimerDuration_IncreaseFactor = ");
			sb.append(this.sccpStack.getSstTimerDuration_IncreaseFactor());
			sb.append("\n");

			return sb.toString();
		}
	}
}

