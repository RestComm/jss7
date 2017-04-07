package org.mobicents.ss7.extension;

import org.jboss.as.controller.AbstractRemoveStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.PathAddress;
import org.jboss.dmr.ModelNode;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;

/**
 * Created by sergeypovarnin on 26.01.17.
 */
class SS7MbeanRemove extends AbstractRemoveStepHandler {

    static final SS7MbeanRemove INSTANCE = new SS7MbeanRemove();

    private SS7MbeanRemove() {
        //
    }

    protected void performRuntime(OperationContext context, ModelNode operation, ModelNode model) {
        final PathAddress address = PathAddress.pathAddress(operation.require(OP_ADDR));
        final String name = address.getLastElement().getValue();
    }

    protected void recoverServices(OperationContext context, ModelNode operation, ModelNode model) {
        // TODO: RE-ADD SERVICES
    }

}