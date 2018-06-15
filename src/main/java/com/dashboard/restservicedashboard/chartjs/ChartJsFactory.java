package com.dashboard.restservicedashboard.chartjs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChartJsFactory {
	
	private static final String PIE = "pie";
	private static final String BAR = "bar";
	private static final String LINE = "line";
	private static final String DOUGHNUT = "doughnut";
	
	private static final String LEGEND_POSITION = "bottom";
	private static final String CHART_DATASET = "Dataset1";
	
public static Chart getLineChart(ChartJsCompatible chartable) {
		
		Data data = new Data();
		data.setLabels(chartable.getLabels());
		
		List<Dataset> datasets =chartable.getDatasets();
		for(Dataset dataset : datasets) {
			dataset.setPointRadius(8);
		}
		data.setDatasets(datasets);
		
		Chart chart = new Chart();
		chart.setType(LINE);
		chart.setColors(getListOfBackgroundColors2(chartable.getColors()));
		chart.setData(data);			
		
		
		return buildOption(chartable.getTitle(), chart);	
		
		
		
	}
	
	public static Chart getPieChart(ChartJsCompatible chartable) {
		
		Chart chart = new Chart();
		chart.setType(DOUGHNUT);
		Data data = new Data();
		
				
		chart.setColors(getListOfBackgroundColors(chartable.getColors()));		
		data.setLabels(chartable.getLabels());		
		data.setDatasets(chartable.getDatasets());		
		chart.setData(data);
		
		return buildOption(chartable.getTitle(), chart);	
		
	}

	private static Chart buildOption(String textTitle, Chart chart) {
		Options options = new Options();
		
		Legend legend = new Legend();
		legend.setPosition(LEGEND_POSITION);
		legend.setDisplay(true);
		options.setLegend(legend);
		
		Tooltips tooltips = new Tooltips();
		tooltips.setEnabled(true);
		options.setTooltips(tooltips);
		
		options.setShowLabel(true);
		
		options.setResponsive(true);
		
		Animation animation = new Animation();
		animation.setAnimateRotate(true);
		animation.setAnimateScale(true);
		options.setAnimation(animation);
		
		options.setMaintainAspectRatio(false);
		
		
		Title title = new Title();
		title.setDisplay(true);
		title.setText(textTitle);
		options.setTitle(title);
		
		chart.setOptions(options);
		
		return chart;
	}
	private static List<Color> getListOfBackgroundColors(List<String> colors) {		
		Color newColor = new Color().withBackgroundColor(colors).withBorderColor(colors);		
		return Collections.singletonList(newColor);
	}
	private static List<Color> getListOfBackgroundColors2(List<String> colors) {	
		List<Color> borderColors = new ArrayList<>();
		for(String color: colors) {
			borderColors.add(new Color().withBorderColor(Collections.singletonList(color)).withBackgroundColor(Collections.singletonList(color)));
		}
				
		return borderColors;
	}

}
