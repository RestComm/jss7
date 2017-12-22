package org.mobicents.protocols.ss7.sccp.impl.router;

import java.util.Comparator;

/**
 * Created by faizan on 11/08/17.
 */
public class RuleByIdComparator  implements Comparator<RuleImpl> {

    @Override
    public int compare( RuleImpl o1, RuleImpl o2 ) {
        return (o1.getRuleId() < o2.getRuleId() ? -1 : 1);
    }
}
