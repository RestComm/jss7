package org.mobicents.ss7.extension;

import org.jboss.as.controller.AbstractRemoveStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.dmr.ModelNode;

class SS7MbeanPropertyRemove extends AbstractRemoveStepHandler {

    public static final SS7MbeanPropertyRemove INSTANCE = new SS7MbeanPropertyRemove();

    private SS7MbeanPropertyRemove() {
    }

    @Override
    protected void performRuntime(final OperationContext context,
                                  final ModelNode operation,
                                  final ModelNode model) throws OperationFailedException {
    }

}