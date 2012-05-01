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

import java.io.Serializable;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.indicator.GlobalTitleIndicator;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.parameter.GT0001;
import org.mobicents.protocols.ss7.sccp.parameter.GT0010;
import org.mobicents.protocols.ss7.sccp.parameter.GT0011;
import org.mobicents.protocols.ss7.sccp.parameter.GT0100;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * @author amit bhayani
 * @author kulikov
 * @author sergey vetyutnev
 */
public class Rule implements Serializable {

	private static final Logger logger = Logger.getLogger(Rule.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 2147449454267320237L;

	private static final String RULETYPE = "ruleType";
	private static final String LS_ALGO = "loadSharingAlgo";
	private static final String PATTERN = "pattern";
	private static final String OPEN_BRACKET = "(";
	private static final String CLOSE_BRACKET = ")";
	private static final String PRIMARY_ADDRESS = "paddress";
	private static final String SECONDARY_ADDRESS = "saddress";
	private static final String MASK = "mask";

	private final static String SEPARATOR = ";";

	private RuleType ruleType = RuleType.Solitary;
	private LoadSharingAlgorithm loadSharingAlgo = LoadSharingAlgorithm.Undefined;
	
	/** Pattern used for selecting rule */
	private SccpAddress pattern;
	
	private int ruleId;

	/** Translation method */
	private int primaryAddressId = 0;

	private int secondaryAddressId = 0;

	private String mask = null;

	private String[] maskPattern = null;

	public Rule() {

	}

	/**
	 * Creates new routing rule.
	 * 
	 * @param the
	 *            order number of the rule.
	 * @param pattern
	 *            pattern for rule selection.
	 * @param translation
	 *            translation method.
	 * @param mtpInfo
	 *            MTP routing info
	 */
	public Rule(RuleType ruleType, LoadSharingAlgorithm loadSharingAlgo, SccpAddress pattern, String mask) {
		this.ruleType = ruleType;
		this.pattern = pattern;
		this.mask = mask;
		this.setLoadSharingAlgorithm(loadSharingAlgo);

		configure();
	}

	/**
	 * @return the ruleId
	 */
	protected int getRuleId() {
		return ruleId;
	}

	/**
	 * @param ruleId the ruleId to set
	 */
	protected void setRuleId(int ruleId) {
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

	/**
	 * Translate specified address according to the rule.
	 * 
	 * @param address
	 *            the origin address
	 * @param ruleAddress
	 *            the address from the rule
	 * @return translated address
	 */
	public SccpAddress translate(SccpAddress address, SccpAddress ruleAddress) {

		String digits = address.getGlobalTitle().getDigits();
		String patternDigits = pattern.getGlobalTitle().getDigits();

		String primaryDigits = ruleAddress.getGlobalTitle().getDigits();

		// step #1. translate digits
		String translatedDigits = translateDigits(digits, maskPattern, patternDigits.split("/"),
				primaryDigits.split("/"));
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
		SccpAddress sccpAddress = new SccpAddress(ruleAddress.getAddressIndicator().getRoutingIndicator(), ruleAddress.getSignalingPointCode(), gt, ssn);

		// This is translated message
		sccpAddress.setTranslated(true);
		return sccpAddress;
	}

	private GlobalTitle createNewGT(GlobalTitleIndicator gti, GlobalTitle primaryGt, String translatedDigits) {
		GlobalTitle gt = null;
		switch (gti) {
		case GLOBAL_TITLE_INCLUDES_NATURE_OF_ADDRESS_INDICATOR_ONLY:
			gt = GlobalTitle.getInstance(((GT0001) primaryGt).getNoA(), translatedDigits);
			break;
		case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_AND_ENCODING_SCHEME:
			gt = GlobalTitle.getInstance(((GT0011) primaryGt).getTranslationType(), ((GT0011) primaryGt).getNp(),
					translatedDigits);
			break;
		case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_ENCODING_SCHEME_AND_NATURE_OF_ADDRESS:
			gt = GlobalTitle.getInstance(((GT0100) primaryGt).getTranslationType(),
					((GT0100) primaryGt).getNumberingPlan(), ((GT0100) primaryGt).getNatureOfAddress(),
					translatedDigits);
			break;
		case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY:
			gt = GlobalTitle.getInstance(((GT0010) primaryGt).getTranslationType(), translatedDigits);
			break;
		case NO_GLOBAL_TITLE_INCLUDED:
			// Use Global Title from received aadress
			break;
		}
		return gt;
	}

	public boolean matches(SccpAddress address) {

		// Rule is for GTT only
		if (address.getAddressIndicator().getRoutingIndicator() == RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN) {
			logger.info("RoutingIndicator == ROUTING_BASED_ON_DPC_AND_SSN. Return");
			return false;
		}

		// Routing on GTT
		GlobalTitleIndicator gti = address.getAddressIndicator().getGlobalTitleIndicator();
		GlobalTitle patternGT = pattern.getGlobalTitle();
		switch (gti) {
		case GLOBAL_TITLE_INCLUDES_NATURE_OF_ADDRESS_INDICATOR_ONLY:
			GT0001 gt = (GT0001) address.getGlobalTitle();

			if (!(patternGT instanceof GT0001)) {
				logger.info("patternGT not instanceof GT0001. Return False");
				return false;
			}

			// nature of address must match
			if (((GT0001) patternGT).getNoA() != gt.getNoA()) {
				logger.info(String.format("Noa didn't match. Pattern Noa=%s Address Noad=%s Return  False",
						((GT0001) patternGT).getNoA(), gt.getNoA()));
				return false;
			}

			// digits must match
			if (!matchPattern(gt.getDigits().toCharArray(), patternGT.getDigits().toCharArray())) {
				logger.info(String.format("digits didn't match. Pattern digits=%s Address Digits=%s Return  False",
						patternGT.getDigits(), gt.getDigits()));
				return false;
			}

			// all conditions passed
			return true;
		case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_AND_ENCODING_SCHEME:
			GT0011 gt1 = (GT0011) address.getGlobalTitle();

			if (!(patternGT instanceof GT0011)) {
				logger.info("patternGT not instanceof GT0011. Return False");
				return false;
			}

			// translation type should match
			if (((GT0011) patternGT).getTranslationType() != gt1.getTranslationType()) {
				logger.info(String.format("TT didn't match. Pattern TT=%s Address TT=%s Return  False",
						((GT0011) patternGT).getTranslationType(), gt1.getTranslationType()));
				return false;
			}

			// numbering plan should match
			if (((GT0011) patternGT).getNp() != gt1.getNp()) {
				logger.info(String.format("Np didn't match. Pattern Np=%s Address Np=%s Return  False",
						((GT0011) patternGT).getNp(), gt1.getNp()));
				return false;
			}

			// digits must match
			if (!matchPattern(gt1.getDigits().toCharArray(), patternGT.getDigits().toCharArray())) {
				logger.info(String.format("digits didn't match. Pattern digits=%s Address Digits=%s Return  False",
						patternGT.getDigits(), gt1.getDigits()));
				return false;
			}

			// all conditions passed
			return true;
		case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_ENCODING_SCHEME_AND_NATURE_OF_ADDRESS:
			GT0100 gt2 = (GT0100) address.getGlobalTitle();

			if (!(patternGT instanceof GT0100)) {
				logger.info("patternGT not instanceof GT0100. Return False");
				return false;
			}

			// translation type should match
			if (((GT0100) patternGT).getTranslationType() != gt2.getTranslationType()) {
				logger.info(String.format("TT didn't match. Pattern TT=%s Address TT=%s Return  False",
						((GT0100) patternGT).getTranslationType(), gt2.getTranslationType()));
				return false;
			}

			// numbering plan should match
			if (((GT0100) patternGT).getNumberingPlan() != gt2.getNumberingPlan()) {
				logger.info(String.format("Np didn't match. Pattern Np=%s Address Np=%s Return  False",
						((GT0100) patternGT).getNumberingPlan(), gt2.getNumberingPlan()));
				return false;
			}

			// nature of address must match
			if (((GT0100) patternGT).getNatureOfAddress() != gt2.getNatureOfAddress()) {
				logger.info(String.format("Noa didn't match. Pattern Noa=%s Address Noa=%s Return  False",
						((GT0100) patternGT).getNatureOfAddress(), gt2.getNatureOfAddress()));
				return false;
			}

			// digits must match
			if (!matchPattern(gt2.getDigits().toCharArray(), pattern.getGlobalTitle().getDigits().toCharArray())) {
				logger.info(String.format("digits didn't match. Pattern digits=%s Address Digits=%s Return  False",
						patternGT.getDigits(), gt2.getDigits()));
				return false;
			}

			// all conditions passed
			return true;
		case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY:
			GT0010 gt3 = (GT0010) address.getGlobalTitle();

			if (!(patternGT instanceof GT0010)) {
				logger.info("patternGT not instanceof GT0100. Return False");
				return false;
			}

			// translation type should match
			if (((GT0010) patternGT).getTranslationType() != gt3.getTranslationType()) {
				logger.info(String.format("TT didn't match. Pattern TT=%s Address TT=%s Return  False",
						((GT0010) patternGT).getTranslationType(), gt3.getTranslationType()));
				return false;
			}

			// digits must match
			if (!matchPattern(gt3.getDigits().toCharArray(), pattern.getGlobalTitle().getDigits().toCharArray())) {
				logger.info(String.format("digits didn't match. Pattern digits=%s Address Digits=%s Return  False",
						patternGT.getDigits(), gt3.getDigits()));
				return false;
			}

			// all conditions passed
			return true;
		default:
			return false;
		}
	}

	private String translateDigits(String digits, String[] masks, String[] patternDigits, String[] addressDigits) {
		StringBuffer translatedDigits = new StringBuffer();
		String[] digitComponents = new String[patternDigits.length];
		int offset = 0;
		for (int count = 0; count < patternDigits.length; count++) {
			if (patternDigits[count].equals("*")) {
				digitComponents[count] = digits.substring(offset, digits.length());
			} else {
				digitComponents[count] = digits.substring(offset, offset + patternDigits[count].length());
			}
			offset += patternDigits[count].length();
		}

		for (int count = 0; count < patternDigits.length; count++) {
			if (masks[count].equals("K")) {
				translatedDigits.append(digitComponents[count]);
			} else if (masks[count].equals("R")) {
				// If its not padding
				if (!addressDigits[count].contains("-")) {
					translatedDigits.append(addressDigits[count]);
				}
			} else {
				// TODO Throw exception or return original digits only?
			}
		}
		return translatedDigits.toString();
	}

	private boolean matchPattern(char[] digits, char[] pattern) {
		int j = 0;
		for (int i = 0; i < pattern.length; i++) {
			if (pattern[i] == '*') {
				return true;
			} else if (pattern[i] == '?') {
				j++;
				continue;
			} else if (pattern[i] == '/') {
				continue;
			} else if (pattern[i] != digits[j]) {
				return false;
			} else {
				j++;
			}
		}
		
		if(j == digits.length){
			//We compared all the digits and all of them matched, this Rule matches.
			return true;
		}
		
		return false;
	}

	/**
	 * XML Serialization/Deserialization
	 */
	protected static final XMLFormat<Rule> RULE_XML = new XMLFormat<Rule>(Rule.class) {

		public void read(javolution.xml.XMLFormat.InputElement xml, Rule rule) throws XMLStreamException {
			rule.ruleType = RuleType.valueOf(xml.getAttribute(RULETYPE).toString());
			rule.loadSharingAlgo = LoadSharingAlgorithm.valueOf(xml.getAttribute(LS_ALGO).toString());
			rule.mask = xml.getAttribute(MASK).toString();
			rule.primaryAddressId = xml.getAttribute(PRIMARY_ADDRESS).toInt();
			rule.secondaryAddressId = xml.getAttribute(SECONDARY_ADDRESS).toInt();
			rule.pattern = xml.get(PATTERN);
			rule.configure();
		}

		public void write(Rule rule, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
			xml.setAttribute(RULETYPE, rule.ruleType.toString());
			xml.setAttribute(LS_ALGO, rule.loadSharingAlgo.toString());
			xml.setAttribute(MASK, rule.mask);
			xml.setAttribute(PRIMARY_ADDRESS, rule.primaryAddressId);
			xml.setAttribute(SECONDARY_ADDRESS, rule.secondaryAddressId);
			xml.add(rule.pattern, PATTERN);
		}
	};

	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append(RULETYPE);
		buff.append(OPEN_BRACKET);
		buff.append(ruleType.toString());
		buff.append(CLOSE_BRACKET);
		buff.append(SEPARATOR);

		if (this.ruleType == RuleType.Loadshared) {
			buff.append(LS_ALGO);
			buff.append(OPEN_BRACKET);
			buff.append(this.loadSharingAlgo.toString());
			buff.append(CLOSE_BRACKET);
			buff.append(SEPARATOR);
		}

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

		buff.append(MASK);
		buff.append(OPEN_BRACKET);
		buff.append(this.mask);
		buff.append(CLOSE_BRACKET);
		buff.append("\n");
		return buff.toString();
	}
}
