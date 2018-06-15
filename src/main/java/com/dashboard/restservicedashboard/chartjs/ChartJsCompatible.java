package com.dashboard.restservicedashboard.chartjs;

import java.util.List;

public interface ChartJsCompatible {
	
	String getTitle();
	List<String> getLabels();
	List<Dataset> getDatasets();
	List<String> getColors();
	String getDataSetName();

}
