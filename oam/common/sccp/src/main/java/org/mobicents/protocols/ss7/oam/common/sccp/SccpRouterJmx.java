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
package org.mobicents.protocols.ss7.oam.common.sccp;

import org.mobicents.protocols.ss7.indicator.AddressIndicator;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.protocols.ss7.sccp.LoadSharingAlgorithm;
import org.mobicents.protocols.ss7.sccp.LongMessageRule;
import org.mobicents.protocols.ss7.sccp.LongMessageRuleType;
import org.mobicents.protocols.ss7.sccp.Mtp3ServiceAccessPoint;
import org.mobicents.protocols.ss7.sccp.OriginationType;
import org.mobicents.protocols.ss7.sccp.Router;
import org.mobicents.protocols.ss7.sccp.Rule;
import org.mobicents.protocols.ss7.sccp.RuleType;
import org.mobicents.protocols.ss7.sccp.SccpProtocolVersion;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

import java.util.Map;

/**
 * @author Amit Bhayani
 *
 */
public class SccpRouterJmx implements SccpRouterJmxMBean {

    private final Router wrappedRouter;
    private final ParameterFactory parameterFactory;
    public SccpRouterJmx(final Router wrappedRouter, final SccpProvider sccpProvider) {
        this.wrappedRouter = wrappedRouter;
        this.parameterFactory = sccpProvider.getParameterFactory();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.Router#addLongMessageRule(int, int, int,
     * org.mobicents.protocols.ss7.sccp.LongMessageRuleType)
     */
    @Override
    public void addLongMessageRule(int id, int firstSpc, int lastSpc, LongMessageRuleType ruleType) throws Exception {
        this.wrappedRouter.addLongMessageRule(id, firstSpc, lastSpc, ruleType);
    }

    @Override
    public void addSccpLongMessageRule(int id, int firstSpc, int lastSpc, String ruleType) throws Exception {
        LongMessageRuleType currRuleType = LongMessageRuleType.valueOf(ruleType);
        this.wrappedRouter.addLongMessageRule(id, firstSpc, lastSpc, currRuleType);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.Router#addMtp3Destination(int, int, int, int, int, int, int)
     */
    @Override
    public void addMtp3Destination(int sapId, int destId, int firstDpc, int lastDpc, int firstSls, int lastSls, int slsMask) throws Exception {
        this.wrappedRouter.addMtp3Destination(sapId, destId, firstDpc, lastDpc, firstSls, lastSls, slsMask);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.Router#addMtp3ServiceAccessPoint(int, int, int, int)
     */
    @Override
    public void addMtp3ServiceAccessPoint(int id, int mtp3Id, int opc, int ni, int networkId, String localGtDigits) throws Exception {
        this.wrappedRouter.addMtp3ServiceAccessPoint(id, mtp3Id, opc, ni, networkId, localGtDigits);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.Router#addRoutingAddress(int,
     * org.mobicents.protocols.ss7.sccp.parameter.SccpAddress)
     */
    @Override
    public void addRoutingAddress(int id, SccpAddress routingAddress) throws Exception {
        this.wrappedRouter.addRoutingAddress(id, routingAddress);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.Router#addRule(int, org.mobicents.protocols.ss7.sccp.RuleType,
     * org.mobicents.protocols.ss7.sccp.LoadSharingAlgorithm, org.mobicents.protocols.ss7.sccp.OriginationType,
     * org.mobicents.protocols.ss7.sccp.parameter.SccpAddress, java.lang.String, int, int, java.lang.Integer)
     */
    //RuleType;LoadSharingAlgorithm;OriginationType;SccpAddress;String;Integer;SccpAddress;
    //int,RuleType,LoadSharingAlgorithm,OriginationType,SccpAddress,String, int pAddressId, int sAddressId, Integer newCallingPartyAddressAddressId, int networkId, SccpAddress patternCallingAddress
    @Override
    public void addRule(int id, RuleType ruleType, LoadSharingAlgorithm algo, OriginationType originationType,
            SccpAddress pattern, String mask, int pAddressId, int sAddressId, Integer newCallingPartyAddressAddressId,
            int networkId, SccpAddress patternCallingAddress) throws Exception {
        this.wrappedRouter.addRule(id, ruleType, algo, originationType, pattern, mask, pAddressId, sAddressId,
                newCallingPartyAddressAddressId, networkId, patternCallingAddress);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.Router#getLongMessageRule(int)
     */
    @Override
    public LongMessageRule getLongMessageRule(int id) {
        return this.wrappedRouter.getLongMessageRule(id);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.Router#getLongMessageRules()
     */
    @Override
    public Map<Integer, LongMessageRule> getLongMessageRules() {
        return this.wrappedRouter.getLongMessageRules();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.Router#getMtp3ServiceAccessPoint(int)
     */
    @Override
    public Mtp3ServiceAccessPoint getMtp3ServiceAccessPoint(int id) {
        return this.wrappedRouter.getMtp3ServiceAccessPoint(id);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.Router#getMtp3ServiceAccessPoints()
     */
    @Override
    public Map<Integer, Mtp3ServiceAccessPoint> getMtp3ServiceAccessPoints() {
        return this.wrappedRouter.getMtp3ServiceAccessPoints();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.Router#getRoutingAddress(int)
     */
    @Override
    public SccpAddress getRoutingAddress(int id) {
        return this.wrappedRouter.getRoutingAddress(id);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.Router#getRoutingAddresses()
     */
    @Override
    public Map<Integer, SccpAddress> getRoutingAddresses() {
        return this.wrappedRouter.getRoutingAddresses();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.Router#getRule(int)
     */
    @Override
    public Rule getRule(int id) {
        return this.wrappedRouter.getRule(id);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.Router#getRules()
     */
    @Override
    public Map<Integer, Rule> getRules() {
        return this.wrappedRouter.getRules();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.Router#modifyLongMessageRule(int, int, int,
     * org.mobicents.protocols.ss7.sccp.LongMessageRuleType)
     */
    @Override
    public void modifyLongMessageRule(int id, int firstSpc, int lastSpc, LongMessageRuleType ruleType) throws Exception {
        this.wrappedRouter.modifyLongMessageRule(id, firstSpc, lastSpc, ruleType);
    }

    @Override
    public void modifySccpLongMessageRule(int id, int firstSpc, int lastSpc, String ruleType) throws Exception {
        LongMessageRuleType currRuleType = LongMessageRuleType.valueOf(ruleType);
        this.wrappedRouter.modifyLongMessageRule(id, firstSpc, lastSpc, currRuleType);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.Router#modifyMtp3Destination(int, int, int, int, int, int, int)
     */
    @Override
    public void modifyMtp3Destination(int sapId, int destId, int firstDpc, int lastDpc, int firstSls, int lastSls, int slsMask) throws Exception {
        this.wrappedRouter.modifyMtp3Destination(sapId, destId, firstDpc, lastDpc, firstSls, lastSls, slsMask);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.Router#modifyMtp3ServiceAccessPoint(int, int, int, int)
     */
    @Override
    public void modifyMtp3ServiceAccessPoint(int id, int mtp3Id, int opc, int ni, int networkId, String localGtDigits) throws Exception {
        this.wrappedRouter.modifyMtp3ServiceAccessPoint(id, mtp3Id, opc, ni, networkId, localGtDigits);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.Router#modifyRoutingAddress(int,
     * org.mobicents.protocols.ss7.sccp.parameter.SccpAddress)
     */
    @Override
    public void modifyRoutingAddress(int routingAddressId, SccpAddress routingAddress) throws Exception {
        this.wrappedRouter.modifyRoutingAddress(routingAddressId, routingAddress);
    }

    @Override
    public void modifySccpRoutingAddress(int id, int ai, int pc, int ssn, int tt, int np, int nao, String digits) throws Exception {
        SccpAddress sccpAddress = this.createSccpAddress(ai, pc, ssn, tt, np, nao, digits);
        this.wrappedRouter.modifyRoutingAddress(id, sccpAddress);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.Router#modifyRule(int, org.mobicents.protocols.ss7.sccp.RuleType,
     * org.mobicents.protocols.ss7.sccp.LoadSharingAlgorithm, org.mobicents.protocols.ss7.sccp.OriginationType,
     * org.mobicents.protocols.ss7.sccp.parameter.SccpAddress, java.lang.String, int, int, java.lang.Integer)
     */
    @Override
    public void modifyRule(int id, RuleType ruleType, LoadSharingAlgorithm algo, OriginationType originationType,
            SccpAddress pattern, String mask, int pAddressId, int sAddressId, Integer newCallingPartyAddressAddressId,
            int networkId, SccpAddress patternCallingAddress
                           ) throws Exception {
        this.wrappedRouter.modifyRule(id, ruleType, algo, originationType, pattern, mask, pAddressId, sAddressId,
                newCallingPartyAddressAddressId, networkId, patternCallingAddress);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.Router#removeLongMessageRule(int)
     */
    @Override
    public void removeLongMessageRule(int id) throws Exception {
        this.wrappedRouter.removeLongMessageRule(id);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.Router#removeMtp3Destination(int, int)
     */
    @Override
    public void removeMtp3Destination(int sapId, int destId) throws Exception {
        this.wrappedRouter.removeMtp3Destination(sapId, destId);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.Router#removeMtp3ServiceAccessPoint(int)
     */
    @Override
    public void removeMtp3ServiceAccessPoint(int id) throws Exception {
        this.wrappedRouter.removeMtp3ServiceAccessPoint(id);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.Router#removeRoutingAddress(int)
     */
    @Override
    public void removeRoutingAddress(int id) throws Exception {
        this.wrappedRouter.removeRoutingAddress(id);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.Router#removeRule(int)
     */
    @Override
    public void removeRule(int id) throws Exception {
        this.wrappedRouter.removeRule(id);
    }

    @Override
    public void addRoutingAddress(int id, int ai, int pc, int ssn, int tt, int np, int nao, String digits) throws Exception {
        SccpAddress sccpAddress = this.createSccpAddress(ai, pc, ssn, tt, np, nao, digits);
        this.wrappedRouter.addRoutingAddress(id, sccpAddress);

    }

    private SccpAddress createSccpAddress(int ai, int pc, int ssn, int tt, int np, int nao, String digits) throws Exception {
        AddressIndicator aiObj = new AddressIndicator((byte) ai, SccpProtocolVersion.ITU);

        if (aiObj.isSSNPresent() && ssn == 0) {
            throw new Exception(String.format("Address Indicator %d indicates that SSN is present, however SSN passed is 0", ai));
        }

        if (aiObj.isPCPresent() && pc == 0) {
            throw new Exception(String.format("Address Indicator %d indicates that PointCode is present, however PointCode passed is 0", ai));
        }

        if (aiObj.getGlobalTitleIndicator() == null) {
            throw new Exception(String.format("GlobalTitle type is not recognizes, possible bad AddressIndicator value"));
        }

        NumberingPlan npObj = NumberingPlan.valueOf(np);
        NatureOfAddress naiObj = NatureOfAddress.valueOf(nao);
        //TODO: encoding scheme?
        GlobalTitle gt = null;

        switch (aiObj.getGlobalTitleIndicator()) {
            case GLOBAL_TITLE_INCLUDES_NATURE_OF_ADDRESS_INDICATOR_ONLY:
                gt = this.parameterFactory.createGlobalTitle(digits,naiObj);
                break;
            case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY:
                gt = this.parameterFactory.createGlobalTitle(digits,tt );
                break;
            case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_AND_ENCODING_SCHEME:
                gt = this.parameterFactory.createGlobalTitle(digits,tt, npObj,null);
                break;
            case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_ENCODING_SCHEME_AND_NATURE_OF_ADDRESS:
                gt = this.parameterFactory.createGlobalTitle(digits,tt, npObj, null, naiObj);
                break;

            case NO_GLOBAL_TITLE_INCLUDED:
                gt = this.parameterFactory.createGlobalTitle(digits);
                break;
        }

        SccpAddress sccpAddress = this.parameterFactory.createSccpAddress(aiObj.getRoutingIndicator(), gt, pc, ssn);

        return sccpAddress;

    }

    @Override
    public void addRule(int id, String ruleType, String algo, String originationType, int ai, int pc, int ssn, int tt, int np,
            int nao, String digits, String mask, int pAddressId, int sAddressId, int newCallingPartyAddressAddressId, int networkId,
                        int callingai, int callingpc, int callingssn, int callingtt, int callingnp,int callingnao, String callingdigits)
            throws Exception {

        SccpAddress patternAddress = this.createSccpAddress(ai, pc, ssn, tt, np, nao, digits);

        SccpAddress patternAddressCalling = null;
        if (callingdigits != null && !callingdigits.isEmpty()) {
            patternAddressCalling = this.createSccpAddress(callingai, callingpc, callingssn, callingtt, callingnp, callingnao,
                    callingdigits);
        }

        this.wrappedRouter.addRule(id, RuleType.getInstance(ruleType), LoadSharingAlgorithm.getInstance(algo),
                OriginationType.getInstance(originationType), patternAddress, mask, pAddressId, sAddressId,
                newCallingPartyAddressAddressId == -1 ? null : newCallingPartyAddressAddressId, networkId, patternAddressCalling);

    }

    @Override
    public void modifySccpRule(int id, String ruleType, String algo, String originationType, int ai, int pc, int ssn, int tt, int np,
            int nao, String digits, String mask, int pAddressId, int sAddressId, int newCallingPartyAddressAddressId, int networkId,
                        int callingai, int callingpc, int callingssn, int callingtt, int callingnp,int callingnao, String callingdigits)
            throws Exception {

        SccpAddress patternAddress = this.createSccpAddress(ai, pc, ssn, tt, np, nao, digits);

        SccpAddress patternAddressCalling = null;
        if (callingdigits != null && !callingdigits.isEmpty()) {
            patternAddressCalling = this.createSccpAddress(callingai, callingpc, callingssn, callingtt, callingnp, callingnao,
                    callingdigits);
        }

        this.wrappedRouter.modifyRule(id, RuleType.getInstance(ruleType), LoadSharingAlgorithm.getInstance(algo),
                OriginationType.getInstance(originationType), patternAddress, mask, pAddressId, sAddressId,
                newCallingPartyAddressAddressId == -1 ? null : newCallingPartyAddressAddressId, networkId, patternAddressCalling);

    }

}
