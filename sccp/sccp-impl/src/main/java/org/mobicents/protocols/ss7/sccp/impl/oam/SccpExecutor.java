/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.protocols.ss7.sccp.impl.oam;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.indicator.AddressIndicator;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.protocols.ss7.sccp.ConcernedSignalingPointCode;
import org.mobicents.protocols.ss7.sccp.LoadSharingAlgorithm;
import org.mobicents.protocols.ss7.sccp.LongMessageRule;
import org.mobicents.protocols.ss7.sccp.LongMessageRuleType;
import org.mobicents.protocols.ss7.sccp.Mtp3Destination;
import org.mobicents.protocols.ss7.sccp.Mtp3ServiceAccessPoint;
import org.mobicents.protocols.ss7.sccp.OriginationType;
import org.mobicents.protocols.ss7.sccp.RemoteSignalingPointCode;
import org.mobicents.protocols.ss7.sccp.RemoteSubSystem;
import org.mobicents.protocols.ss7.sccp.Router;
import org.mobicents.protocols.ss7.sccp.Rule;
import org.mobicents.protocols.ss7.sccp.RuleType;
import org.mobicents.protocols.ss7.sccp.SccpResource;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.ss7.management.console.ShellExecutor;

/**
 *
 * @author amit bhayani
 *
 */
public class SccpExecutor implements ShellExecutor {

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
            } else if (firstOption.equals("address")) {
                return this.manageAddress(options);
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
            logger.error(String.format("Error while executing comand %s", Arrays.toString(options)), e);
            return e.toString();
        } catch (Throwable t) {
            logger.error(String.format("Error while executing comand %s", Arrays.toString(options)), t);
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

            int remoteSpc = Integer.parseInt(options[4]);

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
            this.sccpResource.addRemoteSsn(remoteSsId, remoteSpc, remoteSs, remoteSsFlag, markProhibitedWhenSpcResuming);
            return SccpOAMMessage.RSS_SUCCESSFULLY_ADDED;
        } else if (command.equals("modify")) {
            if (options.length < 7) {
                return SccpOAMMessage.INVALID_COMMAND;
            }
            int remoteSsId = Integer.parseInt(options[3]);
            int remoteSpc = Integer.parseInt(options[4]);
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
            this.sccpResource.modifyRemoteSsn(remoteSsId, remoteSpc, remoteSs, remoteSsFlag, markProhibitedWhenSpcResuming);

            return SccpOAMMessage.RSS_SUCCESSFULLY_MODIFIED;
        } else if (command.equals("delete")) {
            if (options.length < 4) {
                return SccpOAMMessage.INVALID_COMMAND;
            }
            int remoteSsId = Integer.parseInt(options[3]);
            this.sccpResource.removeRemoteSsn(remoteSsId);
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

            if (this.sccpResource.getRemoteSsns().size() == 0) {
                return SccpOAMMessage.RSS_DOESNT_EXIST;
            }

            Map<Integer, RemoteSubSystem> idVsRemoteSsn = this.sccpResource.getRemoteSsns();

            StringBuffer sb = new StringBuffer();
            for (Integer e : idVsRemoteSsn.keySet()) {
                RemoteSubSystem rss = idVsRemoteSsn.get(e);
                sb.append("key=");
                sb.append(e);
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

            int remoteSpcId = Integer.parseInt(options[3]);
            int remoteSpc = Integer.parseInt(options[4]);
            int remoteSpcFlag = Integer.parseInt(options[5]);
            int mask = Integer.parseInt(options[6]);

            this.sccpResource.addRemoteSpc(remoteSpcId, remoteSpc, remoteSpcFlag, mask);

            return SccpOAMMessage.RSPC_SUCCESSFULLY_ADDED;
        } else if (command.equals("modify")) {
            if (options.length < 7) {
                return SccpOAMMessage.INVALID_COMMAND;
            }

            int remoteSpcId = Integer.parseInt(options[3]);

            int remoteSpc = Integer.parseInt(options[4]);
            int remoteSpcFlag = Integer.parseInt(options[5]);
            int mask = Integer.parseInt(options[6]);

            this.sccpResource.modifyRemoteSpc(remoteSpcId, remoteSpc, remoteSpcFlag, mask);

            return SccpOAMMessage.RSPC_SUCCESSFULLY_MODIFIED;
        } else if (command.equals("delete")) {
            if (options.length < 4) {
                return SccpOAMMessage.INVALID_COMMAND;
            }
            int remoteSpcId = Integer.parseInt(options[3]);
            this.sccpResource.removeRemoteSpc(remoteSpcId);
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

            if (this.sccpResource.getRemoteSpcs().size() == 0) {
                return SccpOAMMessage.RSPC_DOESNT_EXIST;
            }

            StringBuffer sb = new StringBuffer();
            Map<Integer, RemoteSignalingPointCode> idVsRspc = this.sccpResource.getRemoteSpcs();
            Set<Integer> rspIds = idVsRspc.keySet();
            for (Integer e : rspIds) {
                RemoteSignalingPointCode rsp = idVsRspc.get(e);
                sb.append("key=");
                sb.append(e);
                sb.append("  ");
                sb.append(rsp);
                sb.append("\n");
            }
            return sb.toString();

        }

        return SccpOAMMessage.INVALID_COMMAND;
    }

    private String manageAddress(String[] options) throws Exception {
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
            int addressId = Integer.parseInt(options[3]);
            SccpAddress address = this.createAddress(options, 4);

            this.router.addRoutingAddress(addressId, address);
            return SccpOAMMessage.ADDRESS_SUCCESSFULLY_ADDED;
        } else if (command.equals("modify")) {
            if (options.length < 4) {
                return SccpOAMMessage.INVALID_COMMAND;
            }
            int addressId = Integer.parseInt(options[3]);
            SccpAddress address = this.createAddress(options, 4);

            this.router.modifyRoutingAddress(addressId, address);
            return SccpOAMMessage.ADDRESS_SUCCESSFULLY_MODIFIED;
        } else if (command.equals("delete")) {
            if (options.length < 4) {
                return SccpOAMMessage.INVALID_COMMAND;
            }
            int addressId = Integer.parseInt(options[3]);
            this.router.removeRoutingAddress(addressId);
            return SccpOAMMessage.ADDRESS_SUCCESSFULLY_DELETED;

        } else if (command.equals("show")) {

            if (options.length == 4) {
                int addressId = Integer.parseInt(options[3]);
                SccpAddress pa = this.router.getRoutingAddress(addressId);
                if (pa == null) {
                    return SccpOAMMessage.ADDRESS_DOESNT_EXIST;
                }
                return pa.toString();
            }

            if (this.router.getRoutingAddresses().size() == 0) {
                return SccpOAMMessage.ADDRESS_DOESNT_EXIST;
            }

            StringBuffer sb = new StringBuffer();

            Map<Integer, SccpAddress> idVsPrimAdd = this.router.getRoutingAddresses();
            for (Integer e : idVsPrimAdd.keySet()) {
                SccpAddress address = idVsPrimAdd.get(e);
                sb.append("key=");
                sb.append(e);
                sb.append("  ");
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
     *
     * sccp rule create <id> <mask> <address-indicator> <point-code> <subsystem-number> <translation-type> <numbering-plan>
     * <nature-of-address-indicator> <digits> <ruleType> <primary-address-id> backup-addressid <backup-address-id>
     * loadsharing-algo <loadsharing-algorithm> newcgparty-addressid <new-callingPartyAddress-id> origination-type
     * <originationType>
     * </p>
     *
     *
     * @param options
     * @return
     * @throws Exception
     */
    private String createRule(String[] options) throws Exception {
        // Minimum is 13
        if (options.length < 14 || options.length > 22) {
            return SccpOAMMessage.INVALID_COMMAND;
        }
        int ruleId = Integer.parseInt(options[3]);
        String mask = options[4];
        if (mask == null) {
            return SccpOAMMessage.INVALID_MASK;
        }

        RuleType ruleType;
        String s1 = options[12].toLowerCase();
        if (s1.equalsIgnoreCase(RuleType.Solitary.getType())) {
            ruleType = RuleType.Solitary;
        } else if (s1.equalsIgnoreCase(RuleType.Dominant.getType())) {
            ruleType = RuleType.Dominant;
        } else if (s1.equalsIgnoreCase(RuleType.Loadshared.getType())) {
            ruleType = RuleType.Loadshared;
        } else if (s1.equals("broadcast")) {
            ruleType = RuleType.Broadcast;
        } else {
            return SccpOAMMessage.INVALID_COMMAND;
        }

        int pAddressId = Integer.parseInt(options[13]);

        int count = 14;
        int sAddressId = -1;
        Integer newcgpartyAddressId = null;
        LoadSharingAlgorithm algo = LoadSharingAlgorithm.Undefined;
        OriginationType originationType = OriginationType.All;

        while (count < options.length) {
            String key = options[count++];
            if (key == null) {
                return SccpOAMMessage.INVALID_COMMAND;
            }

            if (key.equals("loadsharing-algo")) {
                algo = LoadSharingAlgorithm.getInstance(options[count++]);
            } else if (key.equals("backup-addressid")) {
                sAddressId = Integer.parseInt(options[count++]);
            } else if (key.equals("newcgparty-addressid")) {
                newcgpartyAddressId = Integer.parseInt(options[count++]);
            } else if (key.equals("origination-type")) {
                originationType = OriginationType.getInstance(options[count++]);
            } else {
                return SccpOAMMessage.INVALID_COMMAND;
            }
        }

        SccpAddress pattern = this.createAddress(options, 5);

        this.router
                .addRule(ruleId, ruleType, algo, originationType, pattern, mask, pAddressId, sAddressId, newcgpartyAddressId);
        return SccpOAMMessage.RULE_SUCCESSFULLY_ADDED;
    }

    private String modifyRule(String[] options) throws Exception {
        // Minimum is 13
        if (options.length < 14 || options.length > 22) {
            return SccpOAMMessage.INVALID_COMMAND;
        }
        int ruleId = Integer.parseInt(options[3]);

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
        } else if (s1.equals("broadcast")) {
            ruleType = RuleType.Broadcast;
        } else {
            return SccpOAMMessage.INVALID_COMMAND;
        }

        int pAddressId = Integer.parseInt(options[13]);

        int count = 14;
        int sAddressId = -1;
        Integer newcgpartyAddressId = null;
        LoadSharingAlgorithm algo = LoadSharingAlgorithm.Undefined;
        OriginationType originationType = OriginationType.All;

        while (count < options.length) {
            String key = options[count++];
            if (key == null) {
                return SccpOAMMessage.INVALID_COMMAND;
            }

            if (key.equals("loadsharing-algo")) {
                algo = LoadSharingAlgorithm.getInstance(options[count++]);
            } else if (key.equals("backup-addressid")) {
                sAddressId = Integer.parseInt(options[count++]);
            } else if (key.equals("newcgparty-addressid")) {
                newcgpartyAddressId = Integer.parseInt(options[count++]);
            } else if (key.equals("origination-type")) {
                originationType = OriginationType.getInstance(options[count++]);
            } else {
                return SccpOAMMessage.INVALID_COMMAND;
            }
        }

        SccpAddress pattern = this.createAddress(options, 5);
        this.router.modifyRule(ruleId, ruleType, algo, originationType, pattern, mask, pAddressId, sAddressId,
                newcgpartyAddressId);
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

        this.router.removeRule(ruleId);
        return SccpOAMMessage.RULE_SUCCESSFULLY_REMOVED;
    }

    /**
     * Command is "sccp rule show <id>" where id is optional. If id is not passed, all rules configured are shown
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

        if (this.router.getRules().size() == 0) {
            return SccpOAMMessage.RULE_DOESNT_EXIST;
        }

        Map<Integer, Rule> idVsRule = this.router.getRules();

        StringBuffer sb = new StringBuffer();
        for (Integer e : idVsRule.keySet()) {
            Rule rule = idVsRule.get(e);
            sb.append("key=");
            sb.append(e);
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

        if(aiObj.ssnPresent() && ssn == 0){
            throw new Exception(String.format("Address Indicator %d indicates that SSN is present, however SSN passed is 0", ai));
        }

        if(aiObj.pcPresent() && pc == 0){
            throw new Exception(String.format("Address Indicator %d indicates that PointCode is present, however PointCode passed is 0", ai));
        }

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
            this.router.addLongMessageRule(lmrId, firstSpc, lastSpc, ruleType);
            return SccpOAMMessage.LMR_SUCCESSFULLY_ADDED;
        } else if (command.equals("modify")) {
            if (options.length < 7) {
                return SccpOAMMessage.INVALID_COMMAND;
            }
            int lmrId = Integer.parseInt(options[3]);
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
            this.router.modifyLongMessageRule(lmrId, firstSpc, lastSpc, ruleType);
            return SccpOAMMessage.LMR_SUCCESSFULLY_MODIFIED;
        } else if (command.equals("delete")) {
            if (options.length < 4) {
                return SccpOAMMessage.INVALID_COMMAND;
            }
            int lmrId = Integer.parseInt(options[3]);
            this.router.removeLongMessageRule(lmrId);

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

            Map<Integer, LongMessageRule> idVsLngmsgRule = this.router.getLongMessageRules();

            StringBuffer sb = new StringBuffer();
            for (Integer e : idVsLngmsgRule.keySet()) {
                LongMessageRule lmr = idVsLngmsgRule.get(e);
                sb.append("key=");
                sb.append(e);
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

            int mtp3Id = Integer.parseInt(options[4]);
            int opc = Integer.parseInt(options[5]);
            int ni = Integer.parseInt(options[6]);

            this.router.addMtp3ServiceAccessPoint(sapId, mtp3Id, opc, ni);

            return SccpOAMMessage.SAP_SUCCESSFULLY_ADDED;
        } else if (command.equals("modify")) {
            if (options.length < 7) {
                return SccpOAMMessage.INVALID_COMMAND;
            }
            int sapId = Integer.parseInt(options[3]);

            int mtp3Id = Integer.parseInt(options[4]);
            int opc = Integer.parseInt(options[5]);
            int ni = Integer.parseInt(options[6]);

            this.router.modifyMtp3ServiceAccessPoint(sapId, mtp3Id, opc, ni);

            return SccpOAMMessage.SAP_SUCCESSFULLY_MODIFIED;
        } else if (command.equals("delete")) {
            if (options.length < 4) {
                return SccpOAMMessage.INVALID_COMMAND;
            }
            int sapId = Integer.parseInt(options[3]);

            this.router.removeMtp3ServiceAccessPoint(sapId);

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

            Map<Integer, Mtp3ServiceAccessPoint> idVsMtp3SerAccPt = this.router.getMtp3ServiceAccessPoints();
            StringBuffer sb = new StringBuffer();
            for (Integer e : idVsMtp3SerAccPt.keySet()) {
                Mtp3ServiceAccessPoint sap = idVsMtp3SerAccPt.get(e);
                sb.append("key=");
                sb.append(e);
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

            int destId = Integer.parseInt(options[4]);

            int firstDpc = Integer.parseInt(options[5]);
            int lastDpc = Integer.parseInt(options[6]);
            int firstSls = Integer.parseInt(options[7]);
            int lastSls = Integer.parseInt(options[8]);
            int slsMask = Integer.parseInt(options[9]);
            this.router.addMtp3Destination(sapId, destId, firstDpc, lastDpc, firstSls, lastSls, slsMask);
            return SccpOAMMessage.DEST_SUCCESSFULLY_ADDED;
        } else if (command.equals("modify")) {
            if (options.length < 10) {
                return SccpOAMMessage.INVALID_COMMAND;
            }
            int sapId = Integer.parseInt(options[3]);
            int destId = Integer.parseInt(options[4]);
            int firstDpc = Integer.parseInt(options[5]);
            int lastDpc = Integer.parseInt(options[6]);
            int firstSls = Integer.parseInt(options[7]);
            int lastSls = Integer.parseInt(options[8]);
            int slsMask = Integer.parseInt(options[9]);
            this.router.modifyMtp3Destination(sapId, destId, firstDpc, lastDpc, firstSls, lastSls, slsMask);
            return SccpOAMMessage.DEST_SUCCESSFULLY_MODIFIED;
        } else if (command.equals("delete")) {
            if (options.length < 5) {
                return SccpOAMMessage.INVALID_COMMAND;
            }

            int sapId = Integer.parseInt(options[3]);
            int destId = Integer.parseInt(options[4]);
            this.router.removeMtp3Destination(sapId, destId);

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
            int conSpc = Integer.parseInt(options[4]);

            this.sccpResource.addConcernedSpc(concernedSpcId, conSpc);

            return SccpOAMMessage.CS_SUCCESSFULLY_ADDED;
        } else if (command.equals("modify")) {
            if (options.length < 5) {
                return SccpOAMMessage.INVALID_COMMAND;
            }

            int concernedSpcId = Integer.parseInt(options[3]);
            if (this.sccpResource.getConcernedSpc(concernedSpcId) == null) {
                return SccpOAMMessage.CS_DOESNT_EXIST;
            }

            int conSpc = Integer.parseInt(options[4]);

            this.sccpResource.modifyConcernedSpc(concernedSpcId, conSpc);

            return SccpOAMMessage.CS_SUCCESSFULLY_MODIFIED;
        } else if (command.equals("delete")) {
            if (options.length < 4) {
                return SccpOAMMessage.INVALID_COMMAND;
            }
            int concernedSpcId = Integer.parseInt(options[3]);
            this.sccpResource.removeConcernedSpc(concernedSpcId);
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

            if (this.sccpResource.getConcernedSpcs().size() == 0) {
                return SccpOAMMessage.CS_DOESNT_EXIST;
            }

            StringBuffer sb = new StringBuffer();

            Map<Integer, ConcernedSignalingPointCode> idvsCsp = this.sccpResource.getConcernedSpcs();
            for (Integer e : idvsCsp.keySet()) {
                ConcernedSignalingPointCode ConcSpc = idvsCsp.get(e);
                sb.append("key=");
                sb.append(e);
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
        } else if (parName.equals("previewmode")) {
            boolean val = Boolean.parseBoolean(options[3]);
            this.sccpStack.setPreviewMode(val);
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
            } else if (parName.equals("previewmode")) {
                sb.append(this.sccpStack.isPreviewMode());
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

            sb.append("previewMode = ");
            sb.append(this.sccpStack.isPreviewMode());
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

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.management.console.ShellExecutor#handles(java.lang. String)
     */
    @Override
    public boolean handles(String command) {
        return command.startsWith("sccp");
    }
}
