package io.quarkiverse.temporal.deployment;

import jakarta.inject.Inject;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkiverse.temporal.Constants;
import io.quarkiverse.temporal.deployment.binding.SimpleActivity;
import io.quarkiverse.temporal.deployment.binding.SimpleActivityImpl;
import io.quarkus.test.QuarkusUnitTest;
import io.temporal.worker.WorkerFactory;

public class ExplicitActivityBindingTest {

    @RegisterExtension
    static final QuarkusUnitTest unitTest = new QuarkusUnitTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class)
                    .addClass(SimpleActivity.class)
                    .addClass(SimpleActivityImpl.class)
                    .addAsResource(
                            new StringAsset("quarkus.temporal.start-workers: false\n" +
                                    "quarkus.temporal.worker.namedWorker.activity-classes[0]: io.quarkiverse.temporal.deployment.binding.SimpleActivityImpl"),
                            "application.properties"));

    @Inject
    WorkerFactory factory;

    @Test
    public void testExplicitBinding() {
        Assertions.assertNull(factory.tryGetWorker(Constants.DEFAULT_WORKER_NAME));
        Assertions.assertNotNull(factory.tryGetWorker("namedWorker"));
    }
}