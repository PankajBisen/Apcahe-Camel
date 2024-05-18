package org.acme;

import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.health.Readiness;

import javax.inject.Singleton;

@Singleton
public class HealthChecks {
    @Readiness
    public HealthCheckResponse isReady() {
        return HealthCheckResponse.up("Application is ready");
    }

    @Liveness
    public HealthCheckResponse isLive() {
        return HealthCheckResponse.up("Application is Live");
    }
}
