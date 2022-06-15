package com.kramphub.recruitment.mediasearch.health;

import com.kramphub.recruitment.mediasearch.controller.MediaSearchController;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

/**
 * Custom Health Indicator:
 *   Indicate the health of Media Search Controller Class
 */
@Component
public class MediaSearchHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        if(MediaSearchController.responseFlag){
            return new Health.Builder(Status.UP).build();
        }else{
            return new Health.Builder(Status.DOWN).build();
        }
    }
}
