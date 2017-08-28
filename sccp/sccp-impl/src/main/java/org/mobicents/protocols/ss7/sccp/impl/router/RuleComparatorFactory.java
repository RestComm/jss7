package org.mobicents.protocols.ss7.sccp.impl.router;

import java.util.Comparator;

/**
 * Created by faizan on 11/08/17.
 */
public class RuleComparatorFactory {

    private final String name;

    private static RuleComparatorFactory instance = null;
    private Comparator<RuleImpl> ruleComparator = new RuleComparator(); // by default we have this one

    public RuleComparatorFactory( String name ) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
    public static RuleComparatorFactory getInstance( String name) {
        if ( instance == null ) {
            instance = new RuleComparatorFactory( name);
        }
        return instance;
    }

    public static RuleComparatorFactory getInstance() {
        return instance;
    }

    public void setRuleComparator(Comparator<RuleImpl> comparator) {
        this.ruleComparator = comparator;
    }

    public Comparator<RuleImpl> getRuleComparator() {
        return this.ruleComparator;
    }
}
