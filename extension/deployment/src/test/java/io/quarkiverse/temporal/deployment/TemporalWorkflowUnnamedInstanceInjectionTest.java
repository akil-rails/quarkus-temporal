package io.quarkiverse.temporal.deployment;

import static io.quarkiverse.temporal.Constants.DEFAULT_WORKER_NAME;

import jakarta.inject.Inject;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkiverse.temporal.TemporalInstance;
import io.quarkiverse.temporal.TemporalWorkflowStub;
import io.quarkiverse.temporal.deployment.stub.SimpleWorkflow;
import io.quarkiverse.temporal.deployment.stub.UnnamedSimpleWorkflowImpl;
import io.quarkus.test.QuarkusUnitTest;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowStub;

public class TemporalWorkflowUnnamedInstanceInjectionTest {

    @RegisterExtension
    static final QuarkusUnitTest unitTest = new QuarkusUnitTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class)
                    .addClass(SimpleWorkflow.class)
                    .addClass(UnnamedSimpleWorkflowImpl.class)
                    .addAsResource(
                            new StringAsset("quarkus.temporal.start-workers: false\n"),
                            "application.properties"));

    @Inject
    @TemporalWorkflowStub
    TemporalInstance<SimpleWorkflow> instance;

    @Test
    public void testUnnamedWorkerWorkflowStubInjection() {
        Assertions.assertNotNull(instance);
        WorkflowStub workflowStub = WorkflowStub.fromTyped(instance.workflowId("theWorkflowId"));
        WorkflowOptions workflowOptions = workflowStub.getOptions().orElse(null);
        Assertions.assertNotNull(workflowOptions);
        Assertions.assertEquals(DEFAULT_WORKER_NAME, workflowOptions.getTaskQueue());
        Assertions.assertEquals("theWorkflowId", workflowOptions.getWorkflowId());

    }
}
