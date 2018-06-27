package com.dashboard.restservicedashboard.alm;

import com.dashboard.commondashboard.Chart;
import com.dashboard.commondashboard.DataRow;
import com.dashboard.commondashboard.Entity;
import com.dashboard.restservicedashboard.chartitem.ChartItem;
import com.dashboard.restservicedashboard.chartitem.ChartItemRepository;
import com.dashboard.restservicedashboard.configuration.AppProperties;
import com.dashboard.restservicedashboard.configuration.Entity2DataRowMapper;
import com.dashboard.restservicedashboard.domaincontroller.ChartData;
import com.dashboard.restservicedashboard.domaincontroller.ChartMatrix;
import com.dashboard.restservicedashboard.selector.ConfigurationService;
import com.dashboard.restservicedashboard.selector.Selector;
import com.dashboard.restservicedashboard.selector.SelectorManager;
import com.dashboard.restservicedashboard.usagelog.UsageLogRepository;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class AlmDataService {

	@Autowired
	private AppProperties appProp;

	@Autowired
	private ChartDataComputer chartDataComputer;

	@Autowired
	private DataRowReader dbClientReadMongo;

	@Autowired
	private SelectorManager selectorManager;

	@Autowired
	@Setter
    private ChartItemRepository chartItemRepository;

	
	@Autowired
	private ConfigurationService configurationService;

	private static final Logger log = LoggerFactory.getLogger(AlmDataService.class);

	public ChartData getChartData(Selector selector) {
		List<Integer> ids = selectorManager.getConfPositionIndex(appProp, selector);
        Entity.EntityType entityType = selector.getEntityType();
		Chart.ChartType chartType = selector.identifyChartType();

		return buildChartData(ids, entityType, chartType, "Selector");
	}

	public ChartData getChartData(Integer confId) {
		ChartItem chartItem = chartItemRepository.findAll().stream().filter(item -> item.getConfId()==confId).findFirst().get();
		String title = chartItem.getDesc();
		List<Integer> ids = chartItem.getIds();
        Entity.EntityType entityType = Entity.EntityType.valueOf(chartItem.getEntityType());
		Chart.ChartType chartType = Chart.ChartType.valueOf(chartItem.getChartType());

		return buildChartData(ids, entityType, chartType, title);

	}

	private ChartData buildChartData(List<Integer> ids, Entity.EntityType entityType,
									 Chart.ChartType chartType, String title) {
		List<DataRow> dataRows = readEntitiesFromDb(ids, entityType);
		ChartMatrix chartMatrix = chartDataComputer.transform(dataRows);

		ChartData chartData;
		if (chartType.equals(Chart.ChartType.PIECHART)) {
			SimpleDateFormat sdf = new SimpleDateFormat(Chart.DATE_FORMAT);
			chartData = chartMatrix.computePieChart(sdf.format(new Date()));
		} else if (chartType.equals(Chart.ChartType.LINECHART)) {
			chartData = chartMatrix.computeLineChart();
		}
		else {
			throw new RuntimeException("Type of chart not supported");
		}
		chartData.setTitle(title);
		return chartData;
	}

	private List<DataRow> readEntitiesFromDb(List<Integer> ids, Entity.EntityType entityType) {

	    String className = Entity2DataRowMapper.getDataRowType(entityType);
	    return dbClientReadMongo.read(className, ids, Integer.valueOf(appProp.getBiDays()));
	}

	public List<Selector> getSelectors() {
		return selectorManager.computeSelectors(configurationService);
	}



}
