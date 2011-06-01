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
import org.mobicents.protocols.ss7.sccp.impl.RemoteSignalingPointCode;
import org.mobicents.protocols.ss7.sccp.impl.RemoteSubSystem;
import org.mobicents.protocols.ss7.sccp.impl.SccpResource;
import org.mobicents.protocols.ss7.sccp.impl.router.Router;
import org.mobicents.protocols.ss7.sccp.impl.router.Rule;
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

	public SccpExecutor() {

	}

	public Router getRouter() {
		return router;
	}

	public void setRouter(Router router) {
		this.router = router;
	}

	public SccpResource getSccpResource() {
		return sccpResource;
	}

	public void setSccpResource(SccpResource sccpResource) {
		this.sccpResource = sccpResource;
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
				return this.managebackupAddress(options);
			} else if (firstOption.equals("rsp")) {
				return this.manageRsp(options);
			} else if (firstOption.equals("rss")) {
				return this.manageRss(options);
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

			RemoteSubSystem rsscObj = new RemoteSubSystem(remoteSpc, remoteSs, remoteSsFlag);
			this.sccpResource.addRemoteSsn(remoteSsId, rsscObj);

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

			RemoteSubSystem rsscObj = new RemoteSubSystem(remoteSpc, remoteSs, remoteSsFlag);
			this.sccpResource.addRemoteSsn(remoteSsId, rsscObj);

			return SccpOAMMessage.RSS_SUCCESSFULLY_MODIFIED;
		} else if (command.equals("delete")) {
			if (options.length < 4) {
				return SccpOAMMessage.INVALID_COMMAND;
			}
			int remoteSsId = Integer.parseInt(options[3]);
			if (this.sccpResource.getRemoteSsn(remoteSsId) == null) {
				return SccpOAMMessage.RSS_DOESNT_EXIST;
			}

			this.sccpResource.getRemoteSsns().remove(remoteSsId);
			return SccpOAMMessage.RSS_SUCCESSFULLY_DELETED;
		} else if (command.equals("show")) {
			if (options.length == 4) {
				int remoteSsId = Integer.parseInt(options[3]);
				RemoteSubSystem rss = this.sccpResource.getRemoteSsn(remoteSsId);
				if (rss == null) {
					return SccpOAMMessage.RSS_DOESNT_EXIST;
				}
				rss.toString();
			}

			StringBuffer sb = new StringBuffer();
			for (FastMap.Entry<Integer, RemoteSubSystem> e = this.sccpResource.getRemoteSsns().head(), end = this.sccpResource
					.getRemoteSsns().tail(); (e = e.getNext()) != end;) {
				int key = e.getKey();
				RemoteSubSystem rss = e.getValue();
				sb.append("key=");
				sb.append(key);
				sb.append("  rss=");
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

			return SccpOAMMessage.RSPC_SUCCESSFULLY_MODIFIED;
		} else if (command.equals("delete")) {
			if (options.length < 4) {
				return SccpOAMMessage.INVALID_COMMAND;
			}
			int remoteSpcId = Integer.parseInt(options[3]);
			if (this.sccpResource.getRemoteSpc(remoteSpcId) == null) {
				return SccpOAMMessage.RSPC_DOESNT_EXIST;
			}

			this.sccpResource.getRemoteSpcs().remove(remoteSpcId);
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

			StringBuffer sb = new StringBuffer();
			for (FastMap.Entry<Integer, RemoteSignalingPointCode> e = this.sccpResource.getRemoteSpcs().head(), end = this.sccpResource
					.getRemoteSpcs().tail(); (e = e.getNext()) != end;) {
				int key = e.getKey();
				RemoteSignalingPointCode rsp = e.getValue();
				sb.append("key=");
				sb.append(key);
				sb.append("  rsp=");
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
			if (this.router.getPrimaryAddresses().get(primAddressId) != null) {
				return SccpOAMMessage.ADDRESS_ALREADY_EXIST;
			}
			SccpAddress primAddress = this.createAddress(options, 4);

			this.router.getPrimaryAddresses().put(primAddressId, primAddress);
			this.router.store();
			return SccpOAMMessage.ADDRESS_SUCCESSFULLY_ADDED;
		} else if (command.equals("modify")) {
			if (options.length < 4) {
				return SccpOAMMessage.INVALID_COMMAND;
			}
			int primAddressId = Integer.parseInt(options[3]);
			if (this.router.getPrimaryAddresses().get(primAddressId) == null) {
				return SccpOAMMessage.ADDRESS_DOESNT_EXIST;
			}
			SccpAddress primAddress = this.createAddress(options, 4);

			this.router.getPrimaryAddresses().put(primAddressId, primAddress);
			this.router.store();
			return SccpOAMMessage.ADDRESS_SUCCESSFULLY_MODIFIED;
		} else if (command.equals("delete")) {
			if (options.length < 4) {
				return SccpOAMMessage.INVALID_COMMAND;
			}
			int primAddressId = Integer.parseInt(options[3]);
			if (this.router.getPrimaryAddresses().remove(primAddressId) == null) {
				return SccpOAMMessage.ADDRESS_DOESNT_EXIST;
			}
			this.router.store();
			return SccpOAMMessage.ADDRESS_SUCCESSFULLY_DELETED;

		} else if (command.equals("show")) {

			if (options.length == 4) {
				int primAddressId = Integer.parseInt(options[3]);
				SccpAddress pa = this.router.getPrimaryAddresses().get(primAddressId);
				if (pa == null) {
					return SccpOAMMessage.ADDRESS_DOESNT_EXIST;
				}
				return pa.toString();
			}

			StringBuffer sb = new StringBuffer();
			for (FastMap.Entry<Integer, SccpAddress> e = this.router.getPrimaryAddresses().head(), end = this.router
					.getPrimaryAddresses().tail(); (e = e.getNext()) != end;) {
				int key = e.getKey();
				SccpAddress address = e.getValue();
				sb.append("key=");
				sb.append(key);
				sb.append("  primary_add=");
				sb.append(address);
				sb.append("\n");
			}
			return sb.toString();
		}

		return SccpOAMMessage.INVALID_COMMAND;
	}

	private String managebackupAddress(String[] options) throws Exception {
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
			if (this.router.getBackupAddresses().get(backupAddressId) != null) {
				return SccpOAMMessage.ADDRESS_ALREADY_EXIST;
			}
			SccpAddress backupAddress = this.createAddress(options, 4);

			this.router.getBackupAddresses().put(backupAddressId, backupAddress);
			this.router.store();
			return SccpOAMMessage.ADDRESS_SUCCESSFULLY_ADDED;
		} else if (command.equals("modify")) {
			if (options.length < 4) {
				return SccpOAMMessage.INVALID_COMMAND;
			}
			int backupAddressId = Integer.parseInt(options[3]);
			if (this.router.getBackupAddresses().get(backupAddressId) == null) {
				return SccpOAMMessage.ADDRESS_DOESNT_EXIST;
			}
			SccpAddress backupAddress = this.createAddress(options, 4);

			this.router.getBackupAddresses().put(backupAddressId, backupAddress);
			this.router.store();
			return SccpOAMMessage.ADDRESS_SUCCESSFULLY_MODIFIED;

		} else if (command.equals("delete")) {
			if (options.length < 4) {
				return SccpOAMMessage.INVALID_COMMAND;
			}
			int backupAddressId = Integer.parseInt(options[3]);
			if (this.router.getBackupAddresses().remove(backupAddressId) == null) {
				return SccpOAMMessage.ADDRESS_DOESNT_EXIST;
			}
			this.router.store();
			return SccpOAMMessage.ADDRESS_SUCCESSFULLY_DELETED;
		} else if (command.equals("show")) {
			if (options.length == 4) {
				int backupAddressId = Integer.parseInt(options[3]);
				SccpAddress pa = this.router.getBackupAddresses().get(backupAddressId);
				if (pa == null) {
					return SccpOAMMessage.ADDRESS_DOESNT_EXIST;
				}
				return pa.toString();
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
		if (options.length < 13) {
			return SccpOAMMessage.INVALID_COMMAND;
		}
		int ruleId;
		Rule rule = null;
		ruleId = Integer.parseInt(options[3]);

		rule = this.router.getRules().get(ruleId);

		if (rule != null) {
			return SccpOAMMessage.RULE_ALREADY_EXIST;
		}

		String mask = options[4];

		if (mask == null) {
			return SccpOAMMessage.INVALID_MASK;
		}

		int pAddressId = Integer.parseInt(options[12]);

		SccpAddress pAddress = this.router.getPrimaryAddresses().get(pAddressId);
		if (pAddress == null) {
			return String.format(SccpOAMMessage.NO_PRIMARY_ADDRESS, pAddressId);
		}

		if (options.length > 13) {
			int sAddressId = Integer.parseInt(options[13]);
			SccpAddress sAddress = this.router.getBackupAddresses().get(sAddressId);
			if (sAddress == null) {
				return String.format(SccpOAMMessage.NO_BACKUP_ADDRESS, sAddressId);
			}
		}

		SccpAddress pattern = this.createAddress(options, 5);

		rule = new Rule(pattern, mask);
		this.router.getRules().put(ruleId, rule);
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

		rule = this.router.getRules().get(ruleId);

		if (rule == null) {
			return SccpOAMMessage.RULE_DOESNT_EXIST;
		}

		String mask = options[4];

		if (mask == null) {
			return SccpOAMMessage.INVALID_MASK;
		}

		int pAddressId = Integer.parseInt(options[12]);

		SccpAddress pAddress = this.router.getPrimaryAddresses().get(pAddressId);
		if (pAddress == null) {
			return String.format(SccpOAMMessage.NO_PRIMARY_ADDRESS, pAddressId);
		}

		if (options.length > 13) {
			int sAddressId = Integer.parseInt(options[13]);
			SccpAddress sAddress = this.router.getBackupAddresses().get(sAddressId);
			if (sAddress == null) {
				return String.format(SccpOAMMessage.NO_BACKUP_ADDRESS, sAddressId);
			}
		}

		SccpAddress pattern = this.createAddress(options, 5);

		rule = new Rule(pattern, mask);
		this.router.getRules().put(ruleId, rule);
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
		Rule rule = null;
		ruleId = Integer.parseInt(options[3]);

		rule = this.router.getRules().remove(ruleId);

		if (rule == null) {
			return SccpOAMMessage.RULE_DOESNT_EXIST;
		}

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
			Rule rule = this.router.getRules().remove(ruleId);
			if (rule == null) {
				return SccpOAMMessage.RULE_DOESNT_EXIST;
			}
			return rule.toString();
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
		NumberingPlan np = NumberingPlan.valueOf(tt = Integer.parseInt(options[index++]));
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

}
