package com.dashboard.restservicedashboard.chartitem;

import com.dashboard.restservicedashboard.alm.AlmDataService;
import com.dashboard.restservicedashboard.usagelog.UsageLog;
import com.dashboard.restservicedashboard.usagelog.UsageLogRepository;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ChartItemService {

    private static final Logger log = LoggerFactory.getLogger(ChartItemService.class);

    @Autowired
    @Setter
    private UsageLogRepository usageLogRepository;

    @Autowired
    @Setter
    private ChartItemRepository chartItemRepository;


    public List<ChartItem> getChartItems(String username, String userTag) {

        UsageLog usageLog = new UsageLog(username, new Date(), "ChartItemList");
        usageLogRepository.save(usageLog);

        ChartItemFilter chartItemFilter = new ChartItemFilter(chartItemRepository.findAll(),username,userTag);

        return chartItemFilter.filter();
    }
}
