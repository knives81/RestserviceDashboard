package com.dashboard.restservicedashboard.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(ignoreUnknownFields = false, prefix = "app")
public class AppProperties {

	@Getter
    @Setter
    private String biDays;
	@Getter
    @Setter
    private String restserviceDashboardVersion;

}
