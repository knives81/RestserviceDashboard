package com.dashboard.restservicedashboard.chartitem;


import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ChartItemController {

    @Autowired
    public ChartItemService chartItemService;

    private static final Logger log = LoggerFactory.getLogger(ChartItemController.class);

    @CrossOrigin
    @ApiOperation(value = "getChartItem", nickname = "getChartItem")
    @RequestMapping(method = RequestMethod.GET, path = "/chartitem", produces = "application/json")
    public List<ChartItem> getChartItem(@RequestParam(required = false, value = "filter") String tag) {
        String username = getUserName();
        return chartItemService.getChartItems(username,tag);
    }


    private String getUserName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("Request done by: " + auth.getName());
        return auth.getName();
    }
}
