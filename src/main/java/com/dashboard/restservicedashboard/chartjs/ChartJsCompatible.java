package com.dashboard.restservicedashboard.chartjs;

import com.dashboard.commondashboard.Chart;
import lombok.Getter;

import java.util.List;

public interface ChartJsCompatible {
	
	String getTitle();
	List<String> getLabels();
	List<Dataset> getDatasets();
	List<String> getColors();
	String getDataSetName();
	Chart.ChartType getChartType();

}
