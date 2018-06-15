package com.dashboard.restservicedashboard.alm;

import com.dashboard.commondashboard.Chart;
import com.dashboard.restservicedashboard.chartjs.ChartJsFactory;
import com.dashboard.restservicedashboard.domaincontroller.ChartData;
import com.dashboard.restservicedashboard.selector.Selector;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.mashape.unirest.http.exceptions.UnirestException;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class AlmDashboardController {

	@Autowired
	public AlmDataService almDataService;

	private static final Logger log = LoggerFactory.getLogger(AlmDashboardController.class);

	@CrossOrigin
	@ApiOperation(value = "getChart", nickname = "getChart")
	@RequestMapping(method = RequestMethod.POST, path = "/chart", produces = "application/json")
	public com.dashboard.restservicedashboard.chartjs.Chart getChart(@RequestBody Selector selector) {
		ChartData chartData = almDataService.getChartData(selector);
		return computeChartJs(chartData);
	}

	@CrossOrigin
	@ApiOperation(value = "getChart", nickname = "getChart")
	@RequestMapping(method = RequestMethod.GET, path = "/chart/{chartItemConfId}", produces = "application/json")
	public com.dashboard.restservicedashboard.chartjs.Chart getChart(@PathVariable String chartItemConfId) throws UnirestException {
		ChartData chartData = almDataService.getChartData(Integer.valueOf(chartItemConfId));
		return computeChartJs(chartData);
	}

	private com.dashboard.restservicedashboard.chartjs.Chart computeChartJs(ChartData chartData) {
		if (chartData.getChartType().equals(Chart.ChartType.PIECHART)) {
			return ChartJsFactory.getPieChart(chartData);
		} else if (chartData.getChartType().equals(Chart.ChartType.LINECHART)) {
			return ChartJsFactory.getLineChart(chartData);
		} else {
			throw new RuntimeException("Type of chart not supported");
		}
	}

	@CrossOrigin
	@ApiOperation(value = "getChartData", nickname = "getChartData")
	@RequestMapping(method = RequestMethod.GET, path = "/chartdata/{chartItemConfId}", produces = "application/json")
	public ChartData chartData(@PathVariable String chartItemConfId)
			throws UnirestException, JsonParseException, JsonMappingException, NumberFormatException, IOException {
		return almDataService.getChartData(Integer.valueOf(chartItemConfId));

	}


	@CrossOrigin
	@ApiOperation(value = "getSelector", nickname = "getSelector")
	@RequestMapping(method = RequestMethod.GET, path = "/selector", produces = "application/json")
	public List<Selector> selector() {
		return almDataService.getSelectors();
	}
	
	@CrossOrigin
	@ApiOperation(value = "getChartItem", nickname = "getChartItem")
	@RequestMapping(method = RequestMethod.GET, path = "/chartitem", produces = "application/json")
	public List<ChartItem> chartitemconf(@RequestParam(required = false, value = "filter") String tag) {
		String username = getUserName();
		return almDataService.getChartItems(username,tag);
	}
	
	
	private String getUserName() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		log.info("Request done by: " + auth.getName());
		return auth.getName();
	}



}
