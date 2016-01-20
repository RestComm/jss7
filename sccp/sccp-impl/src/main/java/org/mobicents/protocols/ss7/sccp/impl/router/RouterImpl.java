/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javolution.text.TextBuilder;
import javolution.util.FastMap;
import javolution.xml.XMLBinding;
import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;
import javolution.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.sccp.LoadSharingAlgorithm;
import org.mobicents.protocols.ss7.sccp.LongMessageRule;
import org.mobicents.protocols.ss7.sccp.LongMessageRuleType;
import org.mobicents.protocols.ss7.sccp.Mtp3ServiceAccessPoint;
import org.mobicents.protocols.ss7.sccp.NetworkIdState;
import org.mobicents.protocols.ss7.sccp.OriginationType;
import org.mobicents.protocols.ss7.sccp.RemoteSignalingPointCode;
import org.mobicents.protocols.ss7.sccp.Router;
import org.mobicents.protocols.ss7.sccp.Rule;
import org.mobicents.protocols.ss7.sccp.RuleType;
import org.mobicents.protocols.ss7.sccp.SccpStack;
import org.mobicents.protocols.ss7.sccp.impl.congestion.NetworkIdStateImpl;
import org.mobicents.protocols.ss7.sccp.impl.congestion.SccpCongestionControl;
import org.mobicents.protocols.ss7.sccp.impl.oam.SccpOAMMessage;
import org.mobicents.protocols.ss7.sccp.impl.parameter.GlobalTitle0001Impl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.GlobalTitle0010Impl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.GlobalTitle0011Impl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.GlobalTitle0100Impl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.NoGlobalTitle;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * <p>
 * The default implementation for the SCCP router.
 * </p>
 *
 * <p>
 * The SCCP router allows to add/remove/list routing rules and implements persistence for the routing rules.
 * </p>
 * <p>
 * RouterImpl when {@link #start() started} looks for file <tt>sccprouter.xml</tt> containing serialized information of
 * underlying {@link RuleImpl}. Set the directory path by calling {@link #setPersistDir(String)} to direct RouterImpl to look at
 * specified directory for underlying serialized file.
 * </p>
 * <p>
 * If directory path is not set, RouterImpl searches for system property <tt>sccprouter.persist.dir</tt> to get the path for
 * directory
 * </p>
 *
 * <p>
 * Even if <tt>sccprouter.persist.dir</tt> system property is not set, RouterImpl will look at property <tt>user.dir</tt>
 * </p>
 *
 * <p>
 * Implementation of SCCP routing mechanism makes routing decisions based on rules. Each rule consists of three elements:
 * <ul>
 * <li>
 * <p>
 * The <i>pattern</i> determines pattern to which destination address is compared. It has complex structure which looks as
 * follows:
 * <ul>
 * <li>
 * <p>
 * <i>translation type</i> (tt) integer numer which is used in a network to indicate the preferred method of global title
 * analysis
 * </p>
 * </li>
 * <li>
 * <p>
 * <i>numbering plan</i> (np) integer value which inidcates which numbering plan will be used for the global title. Its value
 * aids the routing system in determining the correct network system to route message to.
 * </p>
 * </li>
 * <li>
 * <p>
 * <i>nature of address</i> (noa) integer value which indicates address type., Specifically it indicates the scope of the
 * address value, such as whether it is an international number (i.e. including the country code), a "national" or domestic
 * number (i.e. without country code), and other formats such as "local" format.
 * </p>
 * </li>
 * <li>
 * <p>
 * <i>digits</i> (digits) actual address
 * </p>
 * </li>
 * <li>
 * <p>
 * <i>sub-system number</i> (ssn) identifies application in SCCP routing network.
 * </p>
 * </li>
 * </ul>
 * </p>
 * </li>
 * <li>
 * <p>
 * The <i>translation</i> determines target for messages which destination address matches pattern. It has exactly the same
 * structure as pattern .
 * </p>
 * </li>
 * <li>
 * <p>
 * The <i>mtpinfo</i> determines mtp layer information. If translation does not indicate local address, this information is used
 * to send message through MTP layer. It has following structure:
 * <ul>
 * <li>
 * <p>
 * <i>name</i> (name) identifying one of link sets used by SCCP
 * </p>
 * </li>
 * <li>
 * <p>
 * <i>originating point code</i> (opc) local point code used as originating MTP address
 * </p>
 * </li>
 * <li>
 * <p>
 * <i>adjacent point code</i> (apc) remote point code used as destination MTP address
 * </p>
 * </li>
 * <li>
 * <p>
 * <i>signaling link selection</i> (sls) indentifies link in set
 * </p>
 * </li>
 * </ul>
 * </p>
 * </li>
 * </ul>
 * </p>
 * <p>
 * While the <i>pattern</i> is mandatory, <i>translation</i> and <i>mtpinfo</i> is optional. Following combinations are possible
 * <ul>
 * <li>
 * <p>
 * <i>pattern</i> and <i>translation</i> : specifies local routing
 * </p>
 * </li>
 * <li>
 * <p>
 * <i>pattern</i> and <i>mtpinfo</i> : specifies remote routing using specified mtp routing info and no translation needed
 * </p>
 * </li>
 * <li>
 * <p>
 * <i>pattern</i>, <i>translation</i> and <i>mtpinfo</i> specifies remote routing using specified mtp routing info after
 * applying specified translation
 * </p>
 * </li>
 * </ul>
 * </p>
 *
 * @author amit bhayani
 * @author kulikov
 */
public class RouterImpl implements Router {
    private static final Logger logger = Logger.getLogger(RouterImpl.class);

    private static final String SCCP_ROUTER_PERSIST_DIR_KEY = "sccprouter.persist.dir";
    private static final String USER_DIR_KEY = "user.dir";
    private static final String PERSIST_FILE_NAME = "sccprouter2.xml";

    private static final String RULE = "rule";
    private static final String ROUTING_ADDRESS = "routingAddress";
    // private static final String BACKUP_ADDRESS = "backupAddress";
    private static final String LONG_MESSAGE_RULE = "longMessageRule";
    private static final String MTP3_SERVICE_ACCESS_POINT = "sap";

    private final TextBuilder persistFile = TextBuilder.newInstance();

    private static final SccpRouterXMLBinding binding = new SccpRouterXMLBinding();
    private static final String TAB_INDENT = "\t";
    private static final String CLASS_ATTRIBUTE = "type";

    private String persistDir = null;

    private final RuleComparator ruleComparator = new RuleComparator();
    // rule list
    private RuleMap<Integer, Rule> rulesMap = new RuleMap<Integer, Rule>();
    private SccpAddressMap<Integer, SccpAddressImpl> routingAddresses = new SccpAddressMap<Integer, SccpAddressImpl>();
    // private SccpAddressMap<Integer, SccpAddress> backupAddresses = new SccpAddressMap<Integer, SccpAddress>();
    private LongMessageRuleMap<Integer, LongMessageRule> longMessageRules = new LongMessageRuleMap<Integer, LongMessageRule>();
    private Mtp3ServiceAccessPointMap<Integer, Mtp3ServiceAccessPoint> saps = new Mtp3ServiceAccessPointMap<Integer, Mtp3ServiceAccessPoint>();

    private final String name;
    private final SccpStack sccpStack;

    public RouterImpl(String name, SccpStack sccpStack) {
        this.name = name;
        this.sccpStack = sccpStack;

        binding.setAlias(RuleImpl.class, RULE);
        binding.setClassAttribute(CLASS_ATTRIBUTE);
        binding.setAlias(Mtp3DestinationMap.class, "mtp3DestinationMap");

        binding.setAlias(GlobalTitle0001Impl.class, "GT0001");
        binding.setAlias(GlobalTitle0010Impl.class, "GT0010");
        binding.setAlias(GlobalTitle0011Impl.class, "GT0011");
        binding.setAlias(GlobalTitle0100Impl.class, "GT0100");
        binding.setAlias(NoGlobalTitle.class, "NoGlobalTitle");
    }

    public String getName() {
        return name;
    }

    public String getPersistDir() {
        return persistDir;
    }

    public void setPersistDir(String persistDir) {
        this.persistDir = persistDir;
    }

    public void start() {
        this.persistFile.clear();

        if (persistDir != null) {
            this.persistFile.append(persistDir).append(File.separator).append(this.name).append("_").append(PERSIST_FILE_NAME);
        } else {
            persistFile.append(System.getProperty(SCCP_ROUTER_PERSIST_DIR_KEY, System.getProperty(USER_DIR_KEY)))
                    .append(File.separator).append(this.name).append("_").append(PERSIST_FILE_NAME);
        }

        logger.info(String.format("SCCP Router configuration file path %s", persistFile.toString()));

        this.load();

        logger.info("Started SCCP Router");
    }

    public void stop() {
        this.store();
    }

    /**
     * Looks up rule for translation.
     *
     * @param calledParty called party address
     * @return the rule with match to the called party address
     */
    public Rule findRule(SccpAddress calledParty, boolean isMtpOriginated, int msgNetworkId) {

        for (FastMap.Entry<Integer, Rule> e = this.rulesMap.head(), end = this.rulesMap.tail(); (e = e.getNext()) != end;) {
            Rule rule = e.getValue();
            if (rule.matches(calledParty, isMtpOriginated, msgNetworkId)) {
                return rule;
            }
        }
        return null;
    }

    public LongMessageRule findLongMessageRule(int dpc) {
        for (FastMap.Entry<Integer, LongMessageRule> e = this.longMessageRules.head(), end = this.longMessageRules.tail(); (e = e
                .getNext()) != end;) {
            LongMessageRule rule = e.getValue();
            if (rule.matches(dpc)) {
                return rule;
            }
        }
        return null;
    }

    public Mtp3ServiceAccessPoint findMtp3ServiceAccessPoint(int dpc, int sls) {
        for (FastMap.Entry<Integer, Mtp3ServiceAccessPoint> e = this.saps.head(), end = this.saps.tail(); (e = e.getNext()) != end;) {
            Mtp3ServiceAccessPoint sap = e.getValue();
            if (sap.matches(dpc, sls)) {
                return sap;
            }
        }
        return null;
    }

    public Mtp3ServiceAccessPoint findMtp3ServiceAccessPoint(int dpc, int sls, int networkId) {
        for (FastMap.Entry<Integer, Mtp3ServiceAccessPoint> e = this.saps.head(), end = this.saps.tail(); (e = e.getNext()) != end;) {
            Mtp3ServiceAccessPoint sap = e.getValue();
            if (sap.matches(dpc, sls)) {
                if (sap.getNetworkId() == networkId) {
                    return sap;
                }
            }
        }
        return null;
    }

    public Mtp3ServiceAccessPoint findMtp3ServiceAccessPointForIncMes(int localPC, int remotePC) {
        for (FastMap.Entry<Integer, Mtp3ServiceAccessPoint> e = this.saps.head(), end = this.saps.tail(); (e = e.getNext()) != end;) {
            Mtp3ServiceAccessPoint sap = e.getValue();
            if (sap.getOpc() == localPC && sap.matches(remotePC)) {
                return sap;
            }
        }
        return null;
    }

    public Rule getRule(int id) {
        return this.rulesMap.get(id);
    }

    public SccpAddress getRoutingAddress(int id) {
        return this.routingAddresses.get(id);
    }

    // public SccpAddress getBackupAddress(int id) {
    // return this.backupAddresses.get(id);
    // }

    public LongMessageRule getLongMessageRule(int id) {
        return this.longMessageRules.get(id);
    }

    public Mtp3ServiceAccessPoint getMtp3ServiceAccessPoint(int id) {
        return this.saps.get(id);
    }

    public boolean spcIsLocal(int spc) {
        for (FastMap.Entry<Integer, Mtp3ServiceAccessPoint> e = this.saps.head(), end = this.saps.tail(); (e = e.getNext()) != end;) {
            Mtp3ServiceAccessPoint sap = e.getValue();
            if (sap.getOpc() == spc) {
                return true;
            }
        }
        return false;
    }

    public Map<Integer, Rule> getRules() {
        Map<Integer, Rule> rulesMapTmp = new HashMap<Integer, Rule>();
        rulesMapTmp.putAll(rulesMap);
        return rulesMapTmp;
    }

    public Map<Integer, SccpAddress> getRoutingAddresses() {
        Map<Integer, SccpAddress> routingAddressesTmp = new HashMap<Integer, SccpAddress>();
        routingAddressesTmp.putAll(routingAddresses);
        return routingAddressesTmp;
    }

    // public Map<Integer, SccpAddress> getBackupAddresses() {
    // return backupAddresses.unmodifiable();
    // }

    public Map<Integer, LongMessageRule> getLongMessageRules() {
        Map<Integer, LongMessageRule> longMessageRulesTmp = new HashMap<Integer, LongMessageRule>();
        longMessageRulesTmp.putAll(longMessageRules);
        return longMessageRulesTmp;
    }

    public Map<Integer, Mtp3ServiceAccessPoint> getMtp3ServiceAccessPoints() {
        Map<Integer, Mtp3ServiceAccessPoint> sapsTmp = new HashMap<Integer, Mtp3ServiceAccessPoint>();
        sapsTmp.putAll(saps);
        return sapsTmp;
    }

    public void addRule(int id, RuleType ruleType, LoadSharingAlgorithm algo, OriginationType originationType, SccpAddress pattern, String mask,
            int pAddressId, int sAddressId, Integer newCallingPartyAddressAddressId, int networkId) throws Exception {

        Rule ruleTmp = this.getRule(id);

        if (ruleTmp != null) {
            throw new Exception(SccpOAMMessage.RULE_ALREADY_EXIST);
        }

        int maskumberOfSecs = (mask.split("/").length - 1);
        int patternNumberOfSecs = (pattern.getGlobalTitle().getDigits().split("/").length - 1);

        if (maskumberOfSecs != patternNumberOfSecs) {
            throw new Exception(SccpOAMMessage.SEC_MISMATCH_PATTERN);
        }

        SccpAddress pAddress = this.getRoutingAddress(pAddressId);
        if (pAddress == null) {
            throw new Exception(String.format(SccpOAMMessage.NO_PRIMARY_ADDRESS, pAddressId));
        }

        int primAddNumberOfSecs = (pAddress.getGlobalTitle().getDigits().split("/").length - 1);
        if (maskumberOfSecs != primAddNumberOfSecs) {
            throw new Exception(SccpOAMMessage.SEC_MISMATCH_PRIMADDRESS);
        }

        if (sAddressId != -1) {
            SccpAddress sAddress = this.getRoutingAddress(sAddressId);
            if (sAddress == null) {
                throw new Exception(String.format(SccpOAMMessage.NO_BACKUP_ADDRESS, sAddressId));
            }

            int secAddNumberOfSecs = (sAddress.getGlobalTitle().getDigits().split("/").length - 1);
            if (maskumberOfSecs != secAddNumberOfSecs) {
                throw new Exception(SccpOAMMessage.SEC_MISMATCH_SECADDRESS);
            }
        }

        if (sAddressId == -1 && ruleType != RuleType.SOLITARY) {
            throw new Exception(SccpOAMMessage.RULETYPE_NOT_SOLI_SEC_ADD_MANDATORY);
        }

        synchronized (this) {
            RuleImpl rule = new RuleImpl(ruleType, algo, originationType, pattern, mask, networkId);
            rule.setPrimaryAddressId(pAddressId);
            rule.setSecondaryAddressId(sAddressId);
            rule.setNewCallingPartyAddressId(newCallingPartyAddressAddressId);

            rule.setRuleId(id);
            RuleImpl[] rulesArray = new RuleImpl[(this.rulesMap.size() + 1)];
            int count = 0;

            for (FastMap.Entry<Integer, Rule> e = this.rulesMap.head(), end = this.rulesMap.tail(); (e = e.getNext()) != end;) {
                Integer ruleId = e.getKey();
                RuleImpl ruleTemp1 = (RuleImpl) e.getValue();
                ruleTemp1.setRuleId(ruleId);
                rulesArray[count++] = ruleTemp1;
            }

            // add latest rule
            rulesArray[count++] = rule;

            // Sort
            Arrays.sort(rulesArray, ruleComparator);

            RuleMap<Integer, Rule> newRule = new RuleMap<Integer, Rule>();
            for (int i = 0; i < rulesArray.length; i++) {
                RuleImpl ruleTemp = rulesArray[i];
                newRule.put(ruleTemp.getRuleId(), ruleTemp);
            }
            this.rulesMap = newRule;
            this.store();
        }
    }

    public void modifyRule(int id, RuleType ruleType, LoadSharingAlgorithm algo, OriginationType originationType, SccpAddress pattern, String mask,
            int pAddressId, int sAddressId, Integer newCallingPartyAddressAddressId, int networkId) throws Exception {
        Rule ruleTmp = this.getRule(id);

        if (ruleTmp == null) {
            throw new Exception(String.format(SccpOAMMessage.RULE_DOESNT_EXIST, name));
        }

        int maskumberOfSecs = (mask.split("/").length - 1);
        int patternNumberOfSecs = (pattern.getGlobalTitle().getDigits().split("/").length - 1);

        if (maskumberOfSecs != patternNumberOfSecs) {
            throw new Exception(SccpOAMMessage.SEC_MISMATCH_PATTERN);
        }

        SccpAddress pAddress = this.getRoutingAddress(pAddressId);

        if (pAddress == null) {
            throw new Exception(String.format(SccpOAMMessage.NO_PRIMARY_ADDRESS, pAddressId));
        }
        int primAddNumberOfSecs = (pattern.getGlobalTitle().getDigits().split("/").length - 1);
        if (maskumberOfSecs != primAddNumberOfSecs) {
            throw new Exception(SccpOAMMessage.SEC_MISMATCH_PRIMADDRESS);
        }

        if (sAddressId != -1) {
            SccpAddress sAddress = this.getRoutingAddress(sAddressId);
            if (sAddress == null) {
                throw new Exception(String.format(SccpOAMMessage.NO_BACKUP_ADDRESS, sAddressId));
            }
            int secAddNumberOfSecs = (pattern.getGlobalTitle().getDigits().split("/").length - 1);
            if (maskumberOfSecs != secAddNumberOfSecs) {
                throw new Exception(SccpOAMMessage.SEC_MISMATCH_SECADDRESS);
            }
        }

        if (sAddressId == -1 && ruleType != RuleType.SOLITARY) {
            throw new Exception(SccpOAMMessage.RULETYPE_NOT_SOLI_SEC_ADD_MANDATORY);
        }
        synchronized (this) {
            RuleImpl rule = new RuleImpl(ruleType, algo, originationType, pattern, mask, networkId);
            rule.setPrimaryAddressId(pAddressId);
            rule.setSecondaryAddressId(sAddressId);
            rule.setNewCallingPartyAddressId(newCallingPartyAddressAddressId);

            rule.setRuleId(id);
            RuleImpl[] rulesArray = new RuleImpl[(this.rulesMap.size() + 1)];
            int count = 0;

            for (FastMap.Entry<Integer, Rule> e = this.rulesMap.head(), end = this.rulesMap.tail(); (e = e.getNext()) != end;) {
                Integer ruleId = e.getKey();
                RuleImpl ruleTemp1 = (RuleImpl) e.getValue();
                ruleTemp1.setRuleId(ruleId);
                rulesArray[count++] = ruleTemp1;
            }

            // add latest rule
            rulesArray[count++] = rule;

            // Sort
            Arrays.sort(rulesArray, ruleComparator);

            RuleMap<Integer, Rule> newRule = new RuleMap<Integer, Rule>();
            for (int i = 0; i < rulesArray.length; i++) {
                RuleImpl ruleTemp = rulesArray[i];
                newRule.put(ruleTemp.getRuleId(), ruleTemp);
            }
            this.rulesMap = newRule;
            this.store();
        }
    }

    public void removeRule(int id) throws Exception {

        if (this.getRule(id) == null) {
            throw new Exception(String.format(SccpOAMMessage.RULE_DOESNT_EXIST, name));
        }

        synchronized (this) {
            RuleMap<Integer, Rule> newRule = new RuleMap<Integer, Rule>();
            newRule.putAll(this.rulesMap);
            newRule.remove(id);
            this.rulesMap = newRule;
            this.store();
        }
    }

    public void addRoutingAddress(int primAddressId, SccpAddress primaryAddress) throws Exception {

        if (this.getRoutingAddress(primAddressId) != null) {
            throw new Exception(SccpOAMMessage.ADDRESS_ALREADY_EXIST);
        }

        synchronized (this) {
            SccpAddressMap<Integer, SccpAddressImpl> newPrimaryAddress = new SccpAddressMap<Integer, SccpAddressImpl>();
            newPrimaryAddress.putAll(this.routingAddresses);
            newPrimaryAddress.put(primAddressId, (SccpAddressImpl) primaryAddress);
            this.routingAddresses = newPrimaryAddress;
            this.store();
        }
    }

    public void modifyRoutingAddress(int primAddressId, SccpAddress primaryAddress) throws Exception {
        if (this.getRoutingAddress(primAddressId) == null) {
            throw new Exception(String.format(SccpOAMMessage.ADDRESS_DOESNT_EXIST, name));
        }

        synchronized (this) {
            SccpAddressMap<Integer, SccpAddressImpl> newPrimaryAddress = new SccpAddressMap<Integer, SccpAddressImpl>();
            newPrimaryAddress.putAll(this.routingAddresses);
            newPrimaryAddress.put(primAddressId, (SccpAddressImpl) primaryAddress);
            this.routingAddresses = newPrimaryAddress;
            this.store();
        }
    }

    public void removeRoutingAddress(int id) throws Exception {
        if (this.getRoutingAddress(id) == null) {
            throw new Exception(String.format(SccpOAMMessage.ADDRESS_DOESNT_EXIST, name));
        }

        synchronized (this) {
            SccpAddressMap<Integer, SccpAddressImpl> newPrimaryAddress = new SccpAddressMap<Integer, SccpAddressImpl>();
            newPrimaryAddress.putAll(this.routingAddresses);
            newPrimaryAddress.remove(id);
            this.routingAddresses = newPrimaryAddress;
            this.store();
        }
    }

    // public void addBackupAddress(int id, SccpAddress backupAddress) throws Exception {
    //
    // if (this.getBackupAddress(id) != null) {
    // throw new Exception(SccpOAMMessage.ADDRESS_ALREADY_EXIST);
    // }
    //
    // synchronized (this) {
    // SccpAddressMap<Integer, SccpAddress> newBackupAddress = new SccpAddressMap<Integer, SccpAddress>();
    // newBackupAddress.putAll(this.backupAddresses);
    // newBackupAddress.put(id, backupAddress);
    // this.backupAddresses = newBackupAddress;
    // this.store();
    // }
    // }
    //
    // public void modifyBackupAddress(int id, SccpAddress backupAddress) throws Exception {
    // if (this.getBackupAddress(id) == null) {
    // throw new Exception(SccpOAMMessage.ADDRESS_DOESNT_EXIST);
    // }
    //
    // synchronized (this) {
    // SccpAddressMap<Integer, SccpAddress> newBackupAddress = new SccpAddressMap<Integer, SccpAddress>();
    // newBackupAddress.putAll(this.backupAddresses);
    // newBackupAddress.put(id, backupAddress);
    // this.backupAddresses = newBackupAddress;
    // this.store();
    // }
    // }
    //
    // public void removeBackupAddress(int id) throws Exception {
    //
    // if (this.getBackupAddress(id) == null) {
    // throw new Exception(SccpOAMMessage.ADDRESS_DOESNT_EXIST);
    // }
    //
    // synchronized (this) {
    // SccpAddressMap<Integer, SccpAddress> newBackupAddress = new SccpAddressMap<Integer, SccpAddress>();
    // newBackupAddress.putAll(this.backupAddresses);
    // newBackupAddress.remove(id);
    // this.backupAddresses = newBackupAddress;
    // this.store();
    // }
    // }

    public void addLongMessageRule(int id, int firstSpc, int lastSpc, LongMessageRuleType ruleType) throws Exception {
        if (this.getLongMessageRule(id) != null) {
            throw new Exception(SccpOAMMessage.LMR_ALREADY_EXIST);
        }

        LongMessageRuleImpl longMessageRule = new LongMessageRuleImpl(firstSpc, lastSpc, ruleType);

        synchronized (this) {
            LongMessageRuleMap<Integer, LongMessageRule> newLongMessageRule = new LongMessageRuleMap<Integer, LongMessageRule>();
            newLongMessageRule.putAll(this.longMessageRules);
            newLongMessageRule.put(id, longMessageRule);
            this.longMessageRules = newLongMessageRule;
            this.store();
        }
    }

    public void modifyLongMessageRule(int id, int firstSpc, int lastSpc, LongMessageRuleType ruleType) throws Exception {
        if (this.getLongMessageRule(id) == null) {
            throw new Exception(String.format(SccpOAMMessage.LMR_DOESNT_EXIST, name));
        }

        LongMessageRuleImpl longMessageRule = new LongMessageRuleImpl(firstSpc, lastSpc, ruleType);

        synchronized (this) {
            LongMessageRuleMap<Integer, LongMessageRule> newLongMessageRule = new LongMessageRuleMap<Integer, LongMessageRule>();
            newLongMessageRule.putAll(this.longMessageRules);
            newLongMessageRule.put(id, longMessageRule);
            this.longMessageRules = newLongMessageRule;
            this.store();
        }
    }

    public void removeLongMessageRule(int id) throws Exception {

        if (this.getLongMessageRule(id) == null) {
            throw new Exception(String.format(SccpOAMMessage.LMR_DOESNT_EXIST, name));
        }

        synchronized (this) {
            LongMessageRuleMap<Integer, LongMessageRule> newLongMessageRule = new LongMessageRuleMap<Integer, LongMessageRule>();
            newLongMessageRule.putAll(this.longMessageRules);
            newLongMessageRule.remove(id);
            this.longMessageRules = newLongMessageRule;
            this.store();
        }
    }

    public void addMtp3Destination(int sapId, int destId, int firstDpc, int lastDpc, int firstSls, int lastSls, int slsMask)
            throws Exception {
        Mtp3ServiceAccessPoint sap = this.getMtp3ServiceAccessPoint(sapId);
        if (sap == null) {
            throw new Exception(String.format(SccpOAMMessage.SAP_DOESNT_EXIST, name));
        }
        // TODO Synchronize??
        sap.addMtp3Destination(destId, firstDpc, lastDpc, firstSls, lastSls, slsMask);
        this.store();
    }

    public void modifyMtp3Destination(int sapId, int destId, int firstDpc, int lastDpc, int firstSls, int lastSls, int slsMask)
            throws Exception {
        Mtp3ServiceAccessPoint sap = this.getMtp3ServiceAccessPoint(sapId);

        if (sap == null) {
            throw new Exception(String.format(SccpOAMMessage.SAP_DOESNT_EXIST, name));
        }
        // TODO Synchronize??
        sap.modifyMtp3Destination(destId, firstDpc, lastDpc, firstSls, lastSls, slsMask);
        this.store();
    }

    public void removeMtp3Destination(int sapId, int destId) throws Exception {
        Mtp3ServiceAccessPoint sap = this.getMtp3ServiceAccessPoint(sapId);

        if (sap == null) {
            throw new Exception(String.format(SccpOAMMessage.SAP_DOESNT_EXIST, name));
        }

        sap.removeMtp3Destination(destId);
        this.store();
    }

    public void addMtp3ServiceAccessPoint(int id, int mtp3Id, int opc, int ni, int networkId) throws Exception {

        if (this.getMtp3ServiceAccessPoint(id) != null) {
            throw new Exception(SccpOAMMessage.SAP_ALREADY_EXIST);
        }

        if (this.sccpStack.getMtp3UserPart(mtp3Id) == null) {
            throw new Exception(SccpOAMMessage.MUP_DOESNT_EXIST);
        }

        Mtp3ServiceAccessPointImpl sap = new Mtp3ServiceAccessPointImpl(mtp3Id, opc, ni, this.name, networkId);
        synchronized (this) {
            Mtp3ServiceAccessPointMap<Integer, Mtp3ServiceAccessPoint> newSap = new Mtp3ServiceAccessPointMap<Integer, Mtp3ServiceAccessPoint>();
            newSap.putAll(this.saps);
            newSap.put(id, sap);
            this.saps = newSap;
            this.store();
        }
    }

    public void modifyMtp3ServiceAccessPoint(int id, int mtp3Id, int opc, int ni, int networkId) throws Exception {
        if (this.getMtp3ServiceAccessPoint(id) == null) {
            throw new Exception(String.format(SccpOAMMessage.SAP_DOESNT_EXIST, name));
        }

        if (this.sccpStack.getMtp3UserPart(mtp3Id) == null) {
            throw new Exception(SccpOAMMessage.MUP_DOESNT_EXIST);
        }

        Mtp3ServiceAccessPointImpl sap = new Mtp3ServiceAccessPointImpl(mtp3Id, opc, ni, this.name, networkId);
        synchronized (this) {
            Mtp3ServiceAccessPointMap<Integer, Mtp3ServiceAccessPoint> newSap = new Mtp3ServiceAccessPointMap<Integer, Mtp3ServiceAccessPoint>();
            newSap.putAll(this.saps);
            newSap.put(id, sap);
            this.saps = newSap;
            this.store();
        }
    }

    public void removeMtp3ServiceAccessPoint(int id) throws Exception {

        if (this.getMtp3ServiceAccessPoint(id) == null) {
            throw new Exception(String.format(SccpOAMMessage.SAP_DOESNT_EXIST, name));
        }

        synchronized (this) {
            Mtp3ServiceAccessPointMap<Integer, Mtp3ServiceAccessPoint> newSap = new Mtp3ServiceAccessPointMap<Integer, Mtp3ServiceAccessPoint>();
            newSap.putAll(this.saps);
            newSap.remove(id);
            this.saps = newSap;
            this.store();
        }
    }

    public void removeAllResourses() {

        synchronized (this) {
            // if (this.rulesMap.size() == 0 && this.routingAddresses.size() == 0 && this.backupAddresses.size() == 0
            // && this.longMessageRules.size() == 0 && this.saps.size() == 0)
            if (this.rulesMap.size() == 0 && this.routingAddresses.size() == 0 && this.longMessageRules.size() == 0
                    && this.saps.size() == 0)
                // no resources allocated - nothing to do
                return;

            rulesMap = new RuleMap<Integer, Rule>();
            routingAddresses = new SccpAddressMap<Integer, SccpAddressImpl>();
            // backupAddresses = new SccpAddressMap<Integer, SccpAddress>();
            longMessageRules = new LongMessageRuleMap<Integer, LongMessageRule>();
            saps = new Mtp3ServiceAccessPointMap<Integer, Mtp3ServiceAccessPoint>();

            // We store the cleared state
            this.store();
        }
    }

    public FastMap<Integer, NetworkIdState> getNetworkIdStateList() {
        return getNetworkIdList(-1);
    }

    public FastMap<Integer, NetworkIdState> getNetworkIdList(int affectedPc) {
        FastMap<Integer, NetworkIdState> res = new FastMap<Integer, NetworkIdState>();

        for (FastMap.Entry<Integer, Rule> e = this.rulesMap.head(), end = rulesMap.tail(); (e = e.getNext()) != end;) {
            Rule rule = e.getValue();
            NetworkIdStateImpl networkIdState = getRoutingAddressStatusForRoutingRule(rule, affectedPc);
            if (networkIdState != null) {
                NetworkIdState prevNetworkIdState = res.get(rule.getNetworkId());
                if (prevNetworkIdState != null) {
                    if (prevNetworkIdState.isAvailavle()) {
                        if (networkIdState.isAvailavle()) {
                            if (prevNetworkIdState.getCongLevel() < networkIdState.getCongLevel()) {
                                res.put(rule.getNetworkId(), networkIdState);
                            }
                        } else {
                            res.put(rule.getNetworkId(), networkIdState);
                        }
                    }
                } else {
                    res.put(rule.getNetworkId(), networkIdState);
                }
            }
        }

        return res;
    }

    private NetworkIdStateImpl getRoutingAddressStatusForRoutingRule(Rule rule, int affectedPc) {
        SccpAddress translationAddressPri = getRoutingAddress(rule.getPrimaryAddressId());
        NetworkIdStateImpl rspStatusPri = getRoutingAddressStatusForRoutingAddress(translationAddressPri, affectedPc);

        if (rule.getRuleType() == RuleType.DOMINANT || rule.getRuleType() == RuleType.LOADSHARED) {
            SccpAddress translationAddressSec = getRoutingAddress(rule.getSecondaryAddressId());
            NetworkIdStateImpl rspStatusSec = getRoutingAddressStatusForRoutingAddress(translationAddressSec, affectedPc);

            if (rspStatusPri.isAffectedByPc() || rspStatusSec.isAffectedByPc()) {
                if (rule.getRuleType() == RuleType.DOMINANT) {
                    if (rspStatusPri.isAvailavle())
                        return rspStatusPri;

                    return rspStatusSec;
                }
                if (rule.getRuleType() == RuleType.LOADSHARED) {
                    if (rspStatusPri.isAvailavle()) {
                        if (rspStatusSec.isAvailavle()) {
                            if (rspStatusPri.getCongLevel() >= rspStatusSec.getCongLevel())
                                return rspStatusPri;
                            else
                                return rspStatusSec;
                        } else {
                            return rspStatusPri;
                        }
                    } else {
                        if (rspStatusSec.isAvailavle()) {
                            return rspStatusSec;
                        } else {
                            // both are prohibited - we can select any response
                            return rspStatusPri;
                        }
                    }
                }
            } else {
                return null;
            }
        } else {
            if (rspStatusPri.isAffectedByPc())
                return rspStatusPri;
            else
                return null;
        }

        return null;
    }

    private NetworkIdStateImpl getRoutingAddressStatusForRoutingAddress(SccpAddress routingAddress, int affectedPc) {
        if (routingAddress != null && routingAddress.getAddressIndicator().isPCPresent()) {
            boolean affectedByPc = true;
            if ((affectedPc >= 0 && routingAddress.getSignalingPointCode() != affectedPc))
                affectedByPc = false;
            boolean spcIsLocal = spcIsLocal(routingAddress.getSignalingPointCode());
            if (spcIsLocal) {
                return new NetworkIdStateImpl(affectedByPc);
            }

            RemoteSignalingPointCode remoteSpc = sccpStack.getSccpResource().getRemoteSpcByPC(
                    routingAddress.getSignalingPointCode());
            if (remoteSpc == null) {
                return new NetworkIdStateImpl(affectedByPc);
            }
            if (remoteSpc.isRemoteSpcProhibited()) {
                return new NetworkIdStateImpl(false, affectedByPc);
            }
            int congLevel = SccpCongestionControl.generateSccpUserCongLevel(remoteSpc.getCurrentRestrictionLevel());
            if (congLevel > 0) {
                return new NetworkIdStateImpl(congLevel, affectedByPc);
            }
            return new NetworkIdStateImpl(affectedByPc);
        }

        // we return here value that this affectedPc does not affect this rule
        return new NetworkIdStateImpl(false);
    }

    /**
     * Persist
     */
    public void store() {

        // TODO : Should we keep reference to Objects rather than recreating
        // everytime?
        try {
            XMLObjectWriter writer = XMLObjectWriter.newInstance(new FileOutputStream(persistFile.toString()));
            writer.setBinding(binding);
            // Enables cross-references.
            // writer.setReferenceResolver(new XMLReferenceResolver());
            writer.setIndentation(TAB_INDENT);
            writer.write(rulesMap, RULE, RuleMap.class);
            writer.write(routingAddresses, ROUTING_ADDRESS, SccpAddressMap.class);
            // writer.write(backupAddresses, BACKUP_ADDRESS, SccpAddressMap.class);

            writer.write(longMessageRules, LONG_MESSAGE_RULE, LongMessageRuleMap.class);
            writer.write(saps, MTP3_SERVICE_ACCESS_POINT, Mtp3ServiceAccessPointMap.class);

            writer.close();
        } catch (Exception e) {
            logger.error("Error while persisting the Rule state in file", e);
        }
    }

    /**
     * Load and create LinkSets and Link from persisted file
     *
     * @throws Exception
     */
    private void load() {

        try {
            File f = new File(persistFile.toString());
            if (f.exists()) {
                // we have V3 config
                loadVer3(persistFile.toString());
            } else {
                String s1 = persistFile.toString().replace("2.xml", ".xml");
                f = new File(s1);

                if (f.exists()) {
                    if (!loadVer1(s1)) {
                        loadVer2(s1);
                    }
                }

                this.store();
                f.delete();
            }
        } catch (XMLStreamException ex) {
            ex.printStackTrace();
            logger.error(String.format("Failed to load the SS7 configuration file. \n%s", ex.getMessage()));
        } catch (FileNotFoundException e) {
            logger.warn(String.format("Failed to load the SS7 configuration file. \n%s", e.getMessage()));
        } catch (IOException e) {
            logger.error(String.format("Failed to load the SS7 configuration file. \n%s", e.getMessage()));
        }
    }

    private void moveBackupToRoutingAddress(SccpAddressMap<Integer, SccpAddress> backupAddresses) {
        FastMap<Integer, Integer> lstChange = new FastMap<Integer, Integer>();
        for (Integer bId : backupAddresses.keySet()) {
            SccpAddress addr = backupAddresses.get(bId);

            int i1 = bId + 100;
            while (true) {
                if (routingAddresses.get(i1) == null)
                    break;
                i1++;
            }
            routingAddresses.putEntry(i1, (SccpAddressImpl) addr);
            lstChange.putEntry(bId, i1);
        }

        for (Rule rule : rulesMap.values()) {
            Integer newVal = lstChange.get(rule.getSecondaryAddressId());
            if (newVal != null) {
                ((RuleImpl) rule).setSecondaryAddressId(newVal);
            }
        }
    }

    private boolean loadVer1(String fn) throws XMLStreamException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fn)));
        StringBuilder sb = new StringBuilder();
        while (true) {
            String s1 = br.readLine();
            if (s1 == null)
                break;
            sb.append(s1);
            sb.append("\n");
        }
        br.close();
        String s2 = sb.toString();
        s2 = s2.replace("type=\"org.mobicents.protocols.ss7.sccp.parameter.NoGlobalTitle\"", "type=\"NoGlobalTitle\"");

        s2 = s2.replace("type=\"rule\"", "");
        s2 = s2.replace("pattern type=\"org.mobicents.protocols.ss7.sccp.parameter.SccpAddress\"", "patternSccpAddress");
        s2 = s2.replace("ai type=\"org.mobicents.protocols.ss7.indicator.AddressIndicator\" ai=", "ai value=");
        s2 = s2.replace("gt type=\"org.mobicents.protocols.ss7.sccp.parameter.", "gt type=\"");
        s2 = s2.replace("Key type=\"java.lang.Integer\"", "id");
        s2 = s2.replace("Value", "value");
        s2 = s2.replace("/pattern", "/patternSccpAddress");
        s2 = s2.replace("value type=\"org.mobicents.protocols.ss7.sccp.parameter.SccpAddress\"", "sccpAddress");
        s2 = s2.replace("</value>\r\n</primaryAddress>", "</sccpAddress>\r\n</primaryAddress>");
        s2 = s2.replace("</value>\n</primaryAddress>", "</sccpAddress>\n</primaryAddress>");
        s2 = s2.replace("</value>\r\n</backupAddress>", "</sccpAddress>\r\n</backupAddress>");
        s2 = s2.replace("</value>\n</backupAddress>", "</sccpAddress>\n</backupAddress>");
        s2 = s2.replace("type=\"org.mobicents.protocols.ss7.sccp.parameter.", "type=\"");
        s2 = s2.replace("type=\"org.mobicents.protocols.ss7.sccp.impl.router.Mtp3ServiceAccessPoint\"", "");
        s2 = s2.replace("javolution.util.FastMap", "mtp3DestinationMap");
        s2 = s2.replace("type=\"org.mobicents.protocols.ss7.sccp.impl.router.Mtp3Destination\"", "");
        // s2 = s2.replace("", "");

        StringReader sr = new StringReader(s2);
        XMLObjectReader reader = XMLObjectReader.newInstance(sr);

        reader.setBinding(binding);

        // String REMOTE_SSN_V1 = "remoteSsn";
        // String REMOTE_SPC_V1 = "remoteSpc";
        // String CONCERNED_SPC_V1 = "concernedSpc";
        XMLBinding binding2 = new XMLBinding();
        binding2.setClassAttribute(CLASS_ATTRIBUTE);

        String BACKUP_ADDRESS_V2 = "backupAddress";
        String ROUTING_ADDRESS_V2 = "primaryAddress";

        try {
            rulesMap = reader.read(RULE, RuleMap.class);
        } catch (XMLStreamException e) {
            return false;
        }
        routingAddresses = reader.read(ROUTING_ADDRESS_V2, SccpAddressMap.class);
        SccpAddressMap<Integer, SccpAddress> backupAddresses = reader.read(BACKUP_ADDRESS_V2, SccpAddressMap.class);

        longMessageRules = reader.read(LONG_MESSAGE_RULE, LongMessageRuleMap.class);
        saps = reader.read(MTP3_SERVICE_ACCESS_POINT, Mtp3ServiceAccessPointMap.class);

        for (FastMap.Entry<Integer, Mtp3ServiceAccessPoint> e = this.saps.head(), end = this.saps.tail(); (e = e.getNext()) != end;) {
            Mtp3ServiceAccessPoint sap = e.getValue();
            ((Mtp3ServiceAccessPointImpl)sap).setStackName(name);
        }

        reader.close();

        moveBackupToRoutingAddress(backupAddresses);

        return true;
    }

    private void loadVer2(String fn) throws XMLStreamException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fn)));
        StringBuilder sb = new StringBuilder();
        while (true) {
            String s1 = br.readLine();
            if (s1 == null)
                break;
            sb.append(s1);
            sb.append("\n");
        }
        br.close();
        String s2 = sb.toString();
        s2 = s2.replace("type=\"org.mobicents.protocols.ss7.sccp.parameter.NoGlobalTitle\"", "type=\"NoGlobalTitle\"");

        StringReader sr = new StringReader(s2);
        XMLObjectReader reader = XMLObjectReader.newInstance(sr);

        String ROUTING_ADDRESS_V2 = "primaryAddress";
        String BACKUP_ADDRESS_V2 = "backupAddress";

        reader.setBinding(binding);
        rulesMap = reader.read(RULE, RuleMap.class);
        routingAddresses = reader.read(ROUTING_ADDRESS_V2, SccpAddressMap.class);
        SccpAddressMap<Integer, SccpAddress> backupAddresses = reader.read(BACKUP_ADDRESS_V2, SccpAddressMap.class);

        longMessageRules = reader.read(LONG_MESSAGE_RULE, LongMessageRuleMap.class);
        saps = reader.read(MTP3_SERVICE_ACCESS_POINT, Mtp3ServiceAccessPointMap.class);

        for (FastMap.Entry<Integer, Mtp3ServiceAccessPoint> e = this.saps.head(), end = this.saps.tail(); (e = e.getNext()) != end;) {
            Mtp3ServiceAccessPoint sap = e.getValue();
            ((Mtp3ServiceAccessPointImpl)sap).setStackName(name);
        }

        reader.close();

        moveBackupToRoutingAddress(backupAddresses);
    }

    private void loadVer3(String fn) throws XMLStreamException, FileNotFoundException {
        XMLObjectReader reader = XMLObjectReader.newInstance(new FileInputStream(fn));

        reader.setBinding(binding);
        rulesMap = reader.read(RULE, RuleMap.class);
        routingAddresses = reader.read(ROUTING_ADDRESS, SccpAddressMap.class);

        longMessageRules = reader.read(LONG_MESSAGE_RULE, LongMessageRuleMap.class);
        saps = reader.read(MTP3_SERVICE_ACCESS_POINT, Mtp3ServiceAccessPointMap.class);

        for (FastMap.Entry<Integer, Mtp3ServiceAccessPoint> e = this.saps.head(), end = this.saps.tail(); (e = e.getNext()) != end;) {
            Mtp3ServiceAccessPoint sap = e.getValue();
            ((Mtp3ServiceAccessPointImpl)sap).setStackName(name);
        }

        reader.close();
    }
}
