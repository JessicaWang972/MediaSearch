package com.kramphub.recruitment.mediasearch.health;

import com.netflix.appinfo.HealthCheckHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.netflix.appinfo.InstanceInfo.InstanceStatus;
import org.springframework.boot.actuate.health.Status;

/**
 * Media Search Health Check Handler
 */
@Component
public class MediaSearchHealthCheckHandler implements HealthCheckHandler {
    @Autowired
    private MediaSearchHealthIndicator mediaSearchHealthIndicator;

    @Override
    public InstanceStatus getStatus(InstanceStatus instanceStatus) {
        Status status = mediaSearchHealthIndicator.health().getStatus();
        if(status.equals(Status.UP)){
            return InstanceStatus.UP;
        }else{
            return InstanceStatus.DOWN;
        }
    }
}
