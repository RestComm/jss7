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

import javolution.text.CharArray;
import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;
import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.indicator.GlobalTitleIndicator;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.LoadSharingAlgorithm;
import org.mobicents.protocols.ss7.sccp.OriginationType;
import org.mobicents.protocols.ss7.sccp.Rule;
import org.mobicents.protocols.ss7.sccp.RuleType;
import org.mobicents.protocols.ss7.sccp.impl.parameter.BCDEvenEncodingScheme;
import org.mobicents.protocols.ss7.sccp.impl.parameter.BCDOddEncodingScheme;
import org.mobicents.protocols.ss7.sccp.impl.parameter.GlobalTitle0001Impl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.GlobalTitle0010Impl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.GlobalTitle0011Impl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.GlobalTitle0100Impl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.mobicents.protocols.ss7.sccp.parameter.EncodingScheme;
import org.mobicents.protocols.ss7.sccp.parameter.EncodingSchemeType;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle0001;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle0010;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle0011;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle0100;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

import java.io.Serializable;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 */
public class RuleImpl implements Rule, Serializable {

    private static final char CHAR_WILD_CARD_ALL = '*';
    private static final char CHAR_WILD_CARD_SINGLE = '?';
    private static final char CHAR_MASK_SEPARATOR = '/';

    private static final String WILD_CARD_ALL = "*";
    private static final String WILD_CARD_SINGLE = "?";
    private static final String MASK_SEPARATOR = "/";
    private static final String MASK_KEEP = "K";
    private static final String MASK_REPLACE = "R";
    private static final String MASK_IGNORE = "-";
    private static final String NETWORK_ID = "networkId";

    private static final Logger logger = Logger.getLogger(RuleImpl.class);
    /**
     *
     */
    private static final long serialVersionUID = 2147449454267320237L;

    private static final String RULEID = "ruleId";
    private static final String RULETYPE = "ruleType";
    private static final String ORIGINATING_TYPE = "originatingType";
    private static final String LS_ALGO = "loadSharingAlgo";
    private static final String PATTERN = "patternSccpAddress";
    private static final String OPEN_BRACKET = "(";
    private static final String CLOSE_BRACKET = ")";
    private static final String PRIMARY_ADDRESS = "paddress";
    private static final String SECONDARY_ADDRESS = "saddress";
    private static final String NEW_CALLING_PARTY_ADDRESS = "ncpaddress";
    private static final String MASK = "mask";
    private static final String PATTERN_CALLING_ADDRESS = "patternCallingAddress";

    private static final String SEPARATOR = ";";

    private RuleType ruleType = RuleType.SOLITARY;
    private LoadSharingAlgorithm loadSharingAlgo = LoadSharingAlgorithm.Undefined;
    private OriginationType originationType = OriginationType.ALL;

    /** Pattern used for selecting rule */
    private SccpAddress pattern;
    private SccpAddress patternCallingAddress;

    private int ruleId;

    /** Translation method */
    private int primaryAddressId = 0;
    private int secondaryAddressId = 0;
    private Integer newCallingPartyAddressId = null;
    private int networkId;

    private String mask = null;

    private String[] maskPattern = null;



    public static final int MIN_SIGNIFICANT_SSN = 1;
    public static final int MAX_SIGNIFICANT_SSN = 255;

    public RuleImpl() {

    }

    /**
     * Creates new routing rule.
     *
     */
    public RuleImpl( RuleType ruleType, LoadSharingAlgorithm loadSharingAlgo, OriginationType originationType,
                     SccpAddress pattern, String mask, int networkId, SccpAddress patternCallingAddress) {
        this.ruleType = ruleType;
        this.pattern = pattern;
        this.mask = mask;
        this.networkId = networkId;
        this.setLoadSharingAlgorithm(loadSharingAlgo);
        this.setOriginationType(originationType);

        // Calling SCCP Address
        this.patternCallingAddress = patternCallingAddress;

        configure();
    }

    /**
     * @return the mask
     */
    public String getMask() {
        return mask;
    }

    /**
     * @return the ruleId
     */
    public int getRuleId() {
        return ruleId;
    }

    /**
     * @param ruleId the ruleId to set
     */
    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
    }

    public RuleType getRuleType() {
        return ruleType;
    }

    public void setRuleType(RuleType ruleType) {
        this.ruleType = ruleType;
    }

    public LoadSharingAlgorithm getLoadSharingAlgorithm() {
        return loadSharingAlgo;
    }

    public void setLoadSharingAlgorithm(LoadSharingAlgorithm loadSharingAlgo) {
        if (loadSharingAlgo == null)
            this.loadSharingAlgo = LoadSharingAlgorithm.Undefined;
        else
            this.loadSharingAlgo = loadSharingAlgo;
    }

    public OriginationType getOriginationType() {
        return originationType;
    }

    public void setOriginationType(OriginationType originationType) {
        this.originationType = originationType;
    }

    public SccpAddress getPattern() {
        return pattern;
    }

    private void configure() {
        this.maskPattern = this.mask.split("/");
    }

    public int getPrimaryAddressId() {
        return primaryAddressId;
    }

    public void setPrimaryAddressId(int primaryAddressId) {
        this.primaryAddressId = primaryAddressId;
    }

    public int getSecondaryAddressId() {
        return secondaryAddressId;
    }

    public void setSecondaryAddressId(int secondaryAddressId) {
        this.secondaryAddressId = secondaryAddressId;
    }

    public Integer getNewCallingPartyAddressId() {
        return newCallingPartyAddressId;
    }

    public void setNewCallingPartyAddressId(Integer newCallingPartyAddressId) {
        this.newCallingPartyAddressId = newCallingPartyAddressId;
    }

    public int getNetworkId() {
        return networkId;
    }

    public void setNetworkId(int networkId) {
        this.networkId = networkId;
    }

    public SccpAddress getPatternCallingAddress() {
        return patternCallingAddress;
    }

    /**
     * Translate specified address according to the rule.
     *
     * @param address the origin address
     * @param ruleAddress the address from the rule
     * @return translated address
     */
    public SccpAddress translate(SccpAddress address, SccpAddress ruleAddress) {

        String digits = address.getGlobalTitle().getDigits();
        String patternDigits = pattern.getGlobalTitle().getDigits();

        String primaryDigits = ruleAddress.getGlobalTitle().getDigits();

        // step #1. translate digits
        String translatedDigits = translateDigits(digits, maskPattern, patternDigits.split(MASK_SEPARATOR),
                primaryDigits.split(MASK_SEPARATOR));
        // step #2. translate global title
        GlobalTitle gt = null;

        if (translatedDigits != null && !translatedDigits.equals("")) {
            GlobalTitleIndicator gti = ruleAddress.getAddressIndicator().getGlobalTitleIndicator();
            GlobalTitle primaryGt = ruleAddress.getGlobalTitle();
            gt = createNewGT(gti, primaryGt, translatedDigits);

            if (gt == null) {
                // lets use GT from received address
                gt = createNewGT(address.getAddressIndicator().getGlobalTitleIndicator(), address.getGlobalTitle(),
                        translatedDigits);
            }
        }

        int ssn = address.getSubsystemNumber();
        if (ruleAddress.getSubsystemNumber() > 0)
            ssn = ruleAddress.getSubsystemNumber();
        SccpAddress sccpAddress = new SccpAddressImpl(ruleAddress.getAddressIndicator().getRoutingIndicator(),
                gt,ruleAddress.getSignalingPointCode(),  ssn);

        // This is translated message
        sccpAddress.setTranslated(true);
        return sccpAddress;
    }

    private GlobalTitle createNewGT(GlobalTitleIndicator gti, GlobalTitle primaryGt, String translatedDigits) {
        GlobalTitle gt = null;
        switch (gti) {
            case GLOBAL_TITLE_INCLUDES_NATURE_OF_ADDRESS_INDICATOR_ONLY:
                gt = new GlobalTitle0001Impl(translatedDigits, ((GlobalTitle0001) primaryGt).getNatureOfAddress());
                break;
            case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_AND_ENCODING_SCHEME:
                final GlobalTitle0011 globalTitle0011 = (GlobalTitle0011) primaryGt;
                gt = new GlobalTitle0011Impl(translatedDigits, globalTitle0011.getTranslationType(),
                        getEncodingScheme(globalTitle0011.getEncodingScheme(),translatedDigits), globalTitle0011.getNumberingPlan());
                break;
            case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_ENCODING_SCHEME_AND_NATURE_OF_ADDRESS:
                final GlobalTitle0100 globalTitle0100 = (GlobalTitle0100) primaryGt;
                gt = new GlobalTitle0100Impl(translatedDigits, globalTitle0100.getTranslationType(),
                        getEncodingScheme(globalTitle0100.getEncodingScheme(),translatedDigits), globalTitle0100.getNumberingPlan(),
                        globalTitle0100.getNatureOfAddress());
                break;
            case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY:
                gt = new GlobalTitle0010Impl(translatedDigits, ((GlobalTitle0010) primaryGt).getTranslationType());
                break;
            case NO_GLOBAL_TITLE_INCLUDED:
                // Use Global Title from received address
                break;
        }
        return gt;
    }

    private EncodingScheme getEncodingScheme(final EncodingScheme scheme, String translatedDigits){
        //not a good thing but...
        //in case odd/even we need to adjust it, since translation may change ES...
        //in case of specific schemes, its up to them,
        EncodingSchemeType type = scheme.getType();
        if(type == EncodingSchemeType.BCD_EVEN || type == EncodingSchemeType.BCD_ODD){
            return translatedDigits.length() % 2 == 1 ? BCDOddEncodingScheme.INSTANCE : BCDEvenEncodingScheme.INSTANCE;
        } else {
            return scheme;
        }
    }

    public boolean matches(SccpAddress address, SccpAddress callingAddress, boolean isMtpOriginated, int msgNetworkId) {
        if (logger.isTraceEnabled()) {
            logger.trace(String.format("Matching rule Id=%s Rule=[%s]", this.getRuleId(), this.toString()));
        }

        // checking NetworkId
        if (this.networkId != msgNetworkId) {
            if (logger.isTraceEnabled()) {
                logger.trace(String.format("networkId didn't match. Pattern networkId=%s Address networkId=%s Return  False",
                        this.networkId, msgNetworkId));
            }
            return false;
        }

        // Rule is for GTT only
        if (address.getAddressIndicator().getRoutingIndicator() == RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN) {
            if (logger.isDebugEnabled()) {
                logger.debug("RoutingIndicator == ROUTING_BASED_ON_DPC_AND_SSN. Return");
            }
            return false;
        }

        // checking firstly about rule OriginationType
        boolean isOriginationTypeCorrect = true;
        if (this.getOriginationType() == OriginationType.LOCAL && isMtpOriginated) {
            isOriginationTypeCorrect = false;
        }
        if (this.getOriginationType() == OriginationType.REMOTE && !isMtpOriginated) {
            isOriginationTypeCorrect = false;
        }
        if (!isOriginationTypeCorrect) {
            if (logger.isTraceEnabled()) {
                logger.trace(String.format(
                        "OriginationType didn't match. Pattern OriginationType=%s Address isMtpOriginated=%s Return  False",
                        this.getOriginationType(), isMtpOriginated));
            }
            return false;
        }

        // SSN if present flag is set in pattern - must match address SSN & flag
        if (!isSsnMatch(address, pattern)) {
            return false;
        }


        if ( !matchGt( address, pattern ) ) {
            return false; // Called GT didn't match. No point in going forward to match calling
        }

        if ( patternCallingAddress == null || callingAddress == null) {
            // callingAddress or pattern is null then we consider it a match
            return true;
        }
        // SSN matching for calling address just as for called address
        if ( !isSsnMatch( callingAddress, patternCallingAddress ) ) {
            return false;
        }

        if (patternCallingAddress.getGlobalTitle() == null)
            return false;

        if(callingAddress.getGlobalTitle() == null)
            return true;

        // finally match on calling GT
        return matchGt( callingAddress, patternCallingAddress );
    }

    private boolean matchGt( SccpAddress address, SccpAddress patternAddress ) {
        // Routing on GTT
        GlobalTitleIndicator gti = address.getAddressIndicator().getGlobalTitleIndicator();
        GlobalTitle patternGT = patternAddress.getGlobalTitle();
        switch (gti) {
            case GLOBAL_TITLE_INCLUDES_NATURE_OF_ADDRESS_INDICATOR_ONLY:
                GlobalTitle0001 gt = (GlobalTitle0001) address.getGlobalTitle();

                if (!(patternGT instanceof GlobalTitle0001)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("patternGT not instanceof GlobalTitle0001. Return False");
                    }
                    return false;
                }

                // nature of address must match
                if (((GlobalTitle0001) patternGT).getNatureOfAddress() != gt.getNatureOfAddress()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("Noa didn't match. Pattern Noa=%s Address Noad=%s Return  False",
                                ((GlobalTitle0001) patternGT).getNatureOfAddress(), gt.getNatureOfAddress()));
                    }
                    return false;
                }

                // digits must match
                if (!matchPattern(gt.getDigits(), patternGT.getDigits())) {
                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("digits didn't match. Pattern digits=%s Address Digits=%s Return  False",
                                patternGT.getDigits(), gt.getDigits()));
                    }
                    return false;
                }

                // all conditions passed
                return true;
            case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_AND_ENCODING_SCHEME:
                GlobalTitle0011 gt1 = (GlobalTitle0011) address.getGlobalTitle();

                if (!(patternGT instanceof GlobalTitle0011)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("patternGT not instanceof GlobalTitle0011. Return False");
                    }
                    return false;
                }

                // translation type should match
                if (((GlobalTitle0011) patternGT).getTranslationType() != gt1.getTranslationType()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("TT didn't match. Pattern TT=%s Address TT=%s Return  False",
                                ((GlobalTitle0011) patternGT).getTranslationType(), gt1.getTranslationType()));
                    }
                    return false;
                }

                // numbering plan should match
                if (((GlobalTitle0011) patternGT).getNumberingPlan() != gt1.getNumberingPlan()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("Np didn't match. Pattern Np=%s Address Np=%s Return  False",
                                ((GlobalTitle0011) patternGT).getNumberingPlan(), gt1.getNumberingPlan()));
                    }
                    return false;
                }

                // digits must match
                if (!matchPattern(gt1.getDigits(), patternGT.getDigits())) {
                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("digits didn't match. Pattern digits=%s Address Digits=%s Return  False",
                                patternGT.getDigits(), gt1.getDigits()));
                    }
                    return false;
                }

                // all conditions passed
                return true;
            case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_ENCODING_SCHEME_AND_NATURE_OF_ADDRESS:
                GlobalTitle0100 gt2 = (GlobalTitle0100) address.getGlobalTitle();

                if (!(patternGT instanceof GlobalTitle0100)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("patternGT not instanceof GlobalTitle0100. Return False");
                    }
                    return false;
                }

                // translation type should match
                if (((GlobalTitle0100) patternGT).getTranslationType() != gt2.getTranslationType()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("TT didn't match. Pattern TT=%s Address TT=%s Return  False",
                                ((GlobalTitle0100) patternGT).getTranslationType(), gt2.getTranslationType()));
                    }
                    return false;
                }

                // numbering plan should match
                if (((GlobalTitle0100) patternGT).getNumberingPlan() != gt2.getNumberingPlan()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("Np didn't match. Pattern Np=%s Address Np=%s Return  False",
                                ((GlobalTitle0100) patternGT).getNumberingPlan(), gt2.getNumberingPlan()));
                    }
                    return false;
                }

                // nature of address must match
                if (((GlobalTitle0100) patternGT).getNatureOfAddress() != gt2.getNatureOfAddress()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("Noa didn't match. Pattern Noa=%s Address Noa=%s Return  False",
                                ((GlobalTitle0100) patternGT).getNatureOfAddress(), gt2.getNatureOfAddress()));
                    }
                    return false;
                }

                // digits must match
                if (!matchPattern(gt2.getDigits(), patternAddress.getGlobalTitle().getDigits())) {
                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("digits didn't match. Pattern digits=%s Address Digits=%s Return  False",
                                patternGT.getDigits(), gt2.getDigits()));
                    }
                    return false;
                }

                // all conditions passed
                return true;
            case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY:
                GlobalTitle0010 gt3 = (GlobalTitle0010) address.getGlobalTitle();

                if (!(patternGT instanceof GlobalTitle0010)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("patternGT not instanceof GlobalTitle0010. Return False");
                    }
                    return false;
                }

                // translation type should match
                if (((GlobalTitle0010) patternGT).getTranslationType() != gt3.getTranslationType()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("TT didn't match. Pattern TT=%s Address TT=%s Return  False",
                                ((GlobalTitle0010) patternGT).getTranslationType(), gt3.getTranslationType()));
                    }
                    return false;
                }

                // digits must match
                if (!matchPattern(gt3.getDigits(), patternAddress.getGlobalTitle().getDigits())) {
                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("digits didn't match. Pattern digits=%s Address Digits=%s Return  False",
                                patternGT.getDigits(), gt3.getDigits()));
                    }
                    return false;
                }

                // all conditions passed
                return true;
            default:
                return false;
        }
    }

    /**
     * Checks if SSN matches between rule address pattern and provided destination address. SSN is assumed to always match in
     * case it has insignificant value or pattern AI SSNPresent flag is set to false.
     *
     * @param address - a provided address to match
     * @param pattern - a rule pattern address
     * @return true if SSN is present in both pattern and received addresses and they are the same or pattern has SSN flag unset
     *         in AI (bit 7)(isSsnPresent = false for pattern) or pattern SSN value is insignificant
     */
    private boolean isSsnMatch(SccpAddress address, SccpAddress pattern) {
        if (!isSsnSignificant(pattern.getSubsystemNumber()) || !pattern.getAddressIndicator().isSSNPresent()) {
            if (logger.isTraceEnabled()) {
                logger.trace(String.format("SSN is not present or insignificant [%s]. Assume SSN matches. Return True",
                        pattern.getSubsystemNumber()));
            }
            return true;
        }
        if (pattern.getAddressIndicator().isSSNPresent() && address.getAddressIndicator().isSSNPresent()) {
            if (address.getSubsystemNumber() == pattern.getSubsystemNumber()) {
                return true;
            }
        }

        if (logger.isTraceEnabled()) {
            logger.trace(String.format(
                    "SSN didn't match. Pattern: isSsnPresent=%s, SSN=%s Address: isSsnPresent=%s, SSN=%s Return  False",
                    pattern.getAddressIndicator().isSSNPresent(), pattern.getSubsystemNumber(), address.getAddressIndicator()
                            .isSSNPresent(), address.getSubsystemNumber()));
        }

        return false;
    }

    /**
     * Checks if provided SSN value is a meaningful value = between 1 and 255. SSN=0 is for management messages and is not
     * perceived as significant value
     *
     * @param ssn SSN value to check
     * @return true if SSN value is within significant range
     */
    private boolean isSsnSignificant(int ssn) {
        return (MIN_SIGNIFICANT_SSN <= ssn && ssn <= MAX_SIGNIFICANT_SSN);
    }

    private String translateDigits(String digits, String[] masks, String[] patternDigits, String[] addressDigits) {
        StringBuffer translatedDigits = new StringBuffer();
        String[] digitComponents = new String[patternDigits.length];
        int offset = 0;
        for (int count = 0; count < patternDigits.length; count++) {
            if (patternDigits[count].equals(WILD_CARD_ALL)) {
                digitComponents[count] = digits.substring(offset, digits.length());
                break;
            } else {
                digitComponents[count] = digits.substring(offset, offset + patternDigits[count].length());
            }
            offset += patternDigits[count].length();
        }

        for (int count = 0; count < patternDigits.length; count++) {
            if (masks[count].equals(MASK_KEEP)) {
                if (digitComponents[count] != null) {
                    // Check if digits is null? This is possible if user
                    // specified pattern like */5555 where all the digits are
                    // consumed
                    translatedDigits.append(digitComponents[count]);
                }
            } else if (masks[count].equals(MASK_REPLACE)) {
                // If its not padding
                if (!addressDigits[count].contains(MASK_IGNORE)) {
                    translatedDigits.append(addressDigits[count]);
                }
            } else {
                // TODO Throw exception or return original digits only?
            }
        }
        return translatedDigits.toString();
    }

    private boolean matchPattern(String digitsStr, String patternStr) {
        char[] digits = digitsStr.toLowerCase().toCharArray();
        char[] pattern = patternStr.toLowerCase().toCharArray();

        int j = 0;
        for (int i = 0; i < pattern.length; i++) {

            if (j >= digits.length) {
                // Pattern has more digits to match than digits;

                // special case where pattern can be xxxxx/* and digits are xxxxx so this should match.
                if (pattern[j] == CHAR_MASK_SEPARATOR && pattern[j + 1] == CHAR_WILD_CARD_ALL) {
                    return true;
                }

                // return false
                return false;
            }

            if (pattern[i] == CHAR_WILD_CARD_ALL) {
                return true;
            } else if (pattern[i] == CHAR_WILD_CARD_SINGLE) {
                j++;
                continue;
            } else if (pattern[i] == CHAR_MASK_SEPARATOR) {
                continue;
            } else if (pattern[i] != digits[j]) {
                return false;
            } else {
                j++;
            }
        }

        if (j == digits.length) {
            // We compared all the digits and all of them matched, this Rule
            // matches.
            return true;
        }

        return false;
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<RuleImpl> RULE_XML = new XMLFormat<RuleImpl>(RuleImpl.class) {

        public void read(javolution.xml.XMLFormat.InputElement xml, RuleImpl rule) throws XMLStreamException {
            rule.ruleType = RuleType.getInstance(xml.getAttribute(RULETYPE, RuleType.SOLITARY.getValue()));
            rule.loadSharingAlgo = LoadSharingAlgorithm.getInstance(xml.getAttribute(LS_ALGO,
                    LoadSharingAlgorithm.Undefined.getValue()));
            rule.originationType = OriginationType
                    .getInstance(xml.getAttribute(ORIGINATING_TYPE, OriginationType.ALL.getValue()));
            rule.mask = xml.getAttribute(MASK).toString();
            rule.primaryAddressId = xml.getAttribute(PRIMARY_ADDRESS).toInt();
            rule.secondaryAddressId = xml.getAttribute(SECONDARY_ADDRESS).toInt();
            rule.networkId = xml.getAttribute(NETWORK_ID, 0);
            CharArray cha = xml.getAttribute(NEW_CALLING_PARTY_ADDRESS);
            if (cha != null)
                rule.newCallingPartyAddressId = cha.toInt();
            else
                rule.newCallingPartyAddressId = null;
            rule.pattern = xml.get(PATTERN, SccpAddressImpl.class);
            rule.patternCallingAddress = xml.get(PATTERN_CALLING_ADDRESS, SccpAddressImpl.class);
            rule.configure();
        }

        public void write(RuleImpl rule, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            xml.setAttribute(RULETYPE, rule.ruleType.getValue());
            xml.setAttribute(LS_ALGO, rule.loadSharingAlgo.getValue());
            xml.setAttribute(ORIGINATING_TYPE, rule.originationType.getValue());
            xml.setAttribute(MASK, rule.mask);
            xml.setAttribute(PRIMARY_ADDRESS, rule.primaryAddressId);
            xml.setAttribute(SECONDARY_ADDRESS, rule.secondaryAddressId);
            xml.setAttribute(NETWORK_ID, rule.networkId);
            if (rule.newCallingPartyAddressId != null)
                xml.setAttribute(NEW_CALLING_PARTY_ADDRESS, rule.newCallingPartyAddressId);
            xml.add((SccpAddressImpl)rule.pattern, PATTERN, SccpAddressImpl.class);
            if ( rule.patternCallingAddress != null )
                xml.add( ( SccpAddressImpl ) rule.patternCallingAddress, PATTERN_CALLING_ADDRESS, SccpAddressImpl.class );
        }
    };

    public String toString() {
        StringBuffer buff = new StringBuffer();
        buff.append(RULEID);
        buff.append(OPEN_BRACKET);
        buff.append(ruleId);
        buff.append(CLOSE_BRACKET);
        buff.append(SEPARATOR);
        buff.append(RULETYPE);
        buff.append(OPEN_BRACKET);
        buff.append(ruleType.getValue());
        buff.append(CLOSE_BRACKET);
        buff.append(SEPARATOR);

        if (this.ruleType == RuleType.LOADSHARED) {
            buff.append(LS_ALGO);
            buff.append(OPEN_BRACKET);
            buff.append(this.loadSharingAlgo.getValue());
            buff.append(CLOSE_BRACKET);
            buff.append(SEPARATOR);
        }

        buff.append(ORIGINATING_TYPE);
        buff.append(OPEN_BRACKET);
        buff.append(this.originationType.getValue());
        buff.append(CLOSE_BRACKET);
        buff.append(SEPARATOR);

        buff.append(PATTERN);
        buff.append(OPEN_BRACKET);
        buff.append(pattern.toString());
        buff.append(CLOSE_BRACKET);
        buff.append(SEPARATOR);

        buff.append(PRIMARY_ADDRESS);
        buff.append(OPEN_BRACKET);
        buff.append(primaryAddressId);
        buff.append(CLOSE_BRACKET);
        buff.append(SEPARATOR);

        buff.append(SECONDARY_ADDRESS);
        buff.append(OPEN_BRACKET);
        buff.append(secondaryAddressId);
        buff.append(CLOSE_BRACKET);
        buff.append(SEPARATOR);

        if (newCallingPartyAddressId != null) {
            buff.append(NEW_CALLING_PARTY_ADDRESS);
            buff.append(OPEN_BRACKET);
            buff.append(newCallingPartyAddressId);
            buff.append(CLOSE_BRACKET);
            buff.append(SEPARATOR);
        }

        buff.append(MASK);
        buff.append(OPEN_BRACKET);
        buff.append(this.mask);
        buff.append(CLOSE_BRACKET);
        buff.append(SEPARATOR);

        buff.append(NETWORK_ID);
        buff.append(OPEN_BRACKET);
        buff.append(this.networkId);
        buff.append(CLOSE_BRACKET);

        if ( patternCallingAddress != null ) {
            buff.append(SEPARATOR);
            buff.append(PATTERN_CALLING_ADDRESS);
            buff.append(OPEN_BRACKET);
            buff.append( patternCallingAddress.toString() );
            buff.append(CLOSE_BRACKET);
        }
        return buff.toString();
    }
}
