package io.quarkiverse.temporal.deployment.config;

import io.quarkiverse.temporal.TemporalActivity;

@TemporalActivity(workers = "namedWorker")
public class NamedSimpleActivityImpl implements SimpleActivity {
    @Override
    public void withdraw() {
    }
}