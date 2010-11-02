/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.protocols.ss7.sccp.impl.router;

import org.mobicents.protocols.ss7.indicator.GlobalTitleIndicator;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.parameter.GT0001;
import org.mobicents.protocols.ss7.sccp.parameter.GT0010;
import org.mobicents.protocols.ss7.sccp.parameter.GT0011;
import org.mobicents.protocols.ss7.sccp.parameter.GT0100;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 *
 * @author kulikov
 */
public class Rule {
    private final static String SEPARATOR = ";";
    
    /** the number of the rule */
    protected int no;    
    /** Pattern used for selecting rule */
    private AddressInformation pattern;
    /** Translation method */
    private AddressInformation translation;
    /** Additional MTP info */
    private MTPInfo mtpInfo;
    
    /**
     * Creeates new routing rule.
     * 
     * @param the order number of the rule.
     * @param pattern pattern for rule selection.
     * @param translation translation method.
     * @param mtpInfo MTP routing info
     */
    public Rule(AddressInformation pattern, AddressInformation translation, MTPInfo mtpInfo) {
        this.pattern = pattern;
        this.translation = translation;
        this.mtpInfo = mtpInfo;
    }

    protected Rule(int no, AddressInformation pattern, AddressInformation translation, MTPInfo mtpInfo) {
        this.no = no;
        this.pattern = pattern;
        this.translation = translation;
        this.mtpInfo = mtpInfo;
    }
    
    public static Rule getInstance(String s) {
        String[] tokens = s.trim().split(";");
        
        int no = Integer.parseInt(tokens[0]);
        String addressInfo = tokens[1];
        String translation = tokens[2];
        String mtpInfo = tokens[3];
        
        return new Rule(no, AddressInformation.getInstance(addressInfo), 
                AddressInformation.getInstance(translation),
                MTPInfo.getInstance(mtpInfo));
    }
    
    public int getNo() {
        return no;
    }
    
    protected void setNo(int no) {
        this.no = no;
    }
    
    /**
     * Gets the pattern for rule selection.
     * 
     * @return address information object.
     */
    public AddressInformation getPattern() {
        return pattern;
    }
    
    /**
     * Get the translation rules.
     * 
     * @return the address information object
     */
    public AddressInformation getTranslation() {
        return translation;
    }
    
    /**
     * Gets the MTP routing info.
     * 
     * @return MTP info object.
     */
    public MTPInfo getMTPInfo() {
        return mtpInfo;
    }
    
    /**
     * Translate specified address according to the rule.
     * 
     * @param address the origin address
     * @return translated address
     */
    public SccpAddress translate(SccpAddress address) {
        //step #1. translate digits
        //TODO enable expression
        String digits = this.translation.getDigits();
        
        //step #2. translate global title
        GlobalTitle gt = null;
        if (translation.getNatureOfAddress() != null && translation.getTranslationType() == -1 && translation.getNumberingPlan() == null) {
            gt = GlobalTitle.getInstance(translation.getNatureOfAddress(), digits);
        } else if (translation.getNatureOfAddress() == null && translation.getTranslationType() != -1 && translation.getNumberingPlan() != null) {
            gt = GlobalTitle.getInstance(translation.getTranslationType(), translation.getNumberingPlan(), digits);
        } else if (translation.getNatureOfAddress() == null && translation.getTranslationType() != -1 && translation.getNumberingPlan() == null) {
            gt = GlobalTitle.getInstance(translation.getTranslationType(), digits);
        } else if (translation.getNatureOfAddress() != null && translation.getTranslationType() != -1 && translation.getNumberingPlan() != null) {
            gt = GlobalTitle.getInstance(translation.getTranslationType(), translation.getNumberingPlan(), translation.getNatureOfAddress(), digits);
        }
        
        //step #3. create new address object
        return new SccpAddress(gt, translation.getSubsystem());
    }
    
    public boolean matches(SccpAddress address) {
        //consider routing based on global title only
        if (address.getAddressIndicator().getRoutingIndicator() == RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN) {
            return false;
        }
        
        GlobalTitleIndicator gti = address.getAddressIndicator().getGlobalTitleIndicator();
        switch (gti) {
            case GLOBAL_TITLE_INCLUDES_NATURE_OF_ADDRESS_INDICATOR_ONLY :
                GT0001 gt = (GT0001) address.getGlobalTitle();
                //translation type should not be specified
                if (pattern.getTranslationType() != -1) {
                    return false;
                }
                //numbering plan should not be specified
                if (pattern.getNumberingPlan() != null) {
                    return false;
                }
                
                //nature of address must match
                if (pattern.getNatureOfAddress() != gt.getNoA()) {
                    return false;
                }
                
                //digits must match
                if (!gt.getDigits().matches(pattern.getDigits())) {
                    return false;
                }
                
                //all conditions passed
                return true;
            case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_AND_ENCODING_SCHEME :
                GT0011 gt1 = (GT0011) address.getGlobalTitle();
                //translation type should match
                if (pattern.getTranslationType() != gt1.getTranslationType()) {
                    return false;
                }
                
                //numbering plan should match
                if (pattern.getNumberingPlan() != gt1.getNp()) {
                    return false;
                }
                
                //nature must not be specified
                if (pattern.getNatureOfAddress() != null) {
                    return false;
                }
                
                //digits must match
                if (!gt1.getDigits().matches(pattern.getDigits())) {
                    return false;
                }
                
                //all conditions passed
                return true;
            case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_ENCODING_SCHEME_AND_NATURE_OF_ADDRESS :
                GT0100 gt2 = (GT0100) address.getGlobalTitle();
                //translation type should match
                if (pattern.getTranslationType() != gt2.getTranslationType()) {
                    return false;
                }
                
                //numbering plan should match
                if (pattern.getNumberingPlan() != gt2.getNumberingPlan()) {
                    return false;
                }
                
                //nature of address must match
                if (pattern.getNatureOfAddress() != gt2.getNatureOfAddress()) {
                    return false;
                }
                
                //digits must match
                if (!gt2.getDigits().matches(pattern.getDigits())) {
                    return false;
                }
                
                //all conditions passed
                return true;
            case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY :
                GT0010 gt3 = (GT0010) address.getGlobalTitle();
                //translation type should match
                if (pattern.getTranslationType() != gt3.getTranslationType()) {
                    return false;
                }
                
                //numbering plan not specified
                if (pattern.getNumberingPlan() != null) {
                    return false;
                }
                
                //nature of address not specified
                if (pattern.getNatureOfAddress() != null) {
                    return false;
                }
                
                //digits must match
                if (!gt3.getDigits().matches(pattern.getDigits())) {
                    return false;
                }
                
                //all conditions passed
                return true;
            default : 
                return false;
        }
    }
    
    @Override
    public String toString() {
        StringBuffer buff = new StringBuffer();
        buff.append(no);
        buff.append(SEPARATOR);
        buff.append(pattern.toString());
        buff.append(SEPARATOR);
        buff.append(translation.toString());
        buff.append(SEPARATOR);
        buff.append(mtpInfo.toString());
        buff.append("\n");
        return buff.toString();
    }
}
