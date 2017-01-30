package org.mobicents.ss7.extension;

import org.jboss.as.controller.AbstractBoottimeAddStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.jmx.MBeanServerService;
import org.jboss.dmr.ModelNode;
import org.jboss.logging.Logger;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.mobicents.ss7.service.SS7ExtensionService;

import javax.management.MBeanServer;
import java.util.List;

/**
 * Handler responsible for adding the subsystem resource to the model
 *
 * @author <a href="mailto:sergey.povarnin@telestax.com">Sergey Povarnin</a>
 */
class SubsystemAdd extends AbstractBoottimeAddStepHandler {

  static final SubsystemAdd INSTANCE = new SubsystemAdd();

  private final Logger log = Logger.getLogger(SubsystemAdd.class);

  private SubsystemAdd() {
  }

  /** {@inheritDoc} */
  @Override
  protected void populateModel(ModelNode operation, ModelNode model) throws OperationFailedException {
    log.info("Populating the model: "+model);
    //model.setEmptyObject();
  }

  /** {@inheritDoc} */
  @Override
  public void performBoottime(OperationContext context, ModelNode operation, ModelNode model,
                              ServiceVerificationHandler verificationHandler, List<ServiceController<?>> newControllers)
      throws OperationFailedException {

    SS7ExtensionService service = SS7ExtensionService.INSTANCE;

    ServiceName name = SS7ExtensionService.getServiceName();
    ServiceController<SS7ExtensionService> controller = context.getServiceTarget()
        .addService(name, service)
        .addDependency(MBeanServerService.SERVICE_NAME, MBeanServer.class, service.getMbeanServer())
        .addListener(verificationHandler)
        .setInitialMode(ServiceController.Mode.ACTIVE)
        .install();
    newControllers.add(controller);
  }
}