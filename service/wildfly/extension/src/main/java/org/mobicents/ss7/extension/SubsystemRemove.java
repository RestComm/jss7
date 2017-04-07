package org.mobicents.ss7.extension;

import org.jboss.as.controller.AbstractRemoveStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.dmr.ModelNode;
import org.jboss.logging.Logger;
import org.jboss.msc.service.ServiceName;
import org.mobicents.ss7.service.SS7ExtensionService;

/**
 * Handler responsible for removing the subsystem resource from the model
 *
 * @author <a href="mailto:sergey.povarnin@telestax.com">Sergey Povarnin</a>
 */
class SubsystemRemove extends AbstractRemoveStepHandler {

  static final SubsystemRemove INSTANCE = new SubsystemRemove();

  private final Logger log = Logger.getLogger(SubsystemRemove.class);

  private SubsystemRemove() {
  }

  @Override
  protected void performRuntime(OperationContext context, ModelNode operation, ModelNode model) throws OperationFailedException {
    //Remove any services installed by the corresponding add handler here
    ServiceName name = SS7ExtensionService.getServiceName();
    context.removeService(name);
  }


}