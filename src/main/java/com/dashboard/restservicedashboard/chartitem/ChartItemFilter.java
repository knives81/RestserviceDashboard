package com.dashboard.restservicedashboard.chartitem;

import com.dashboard.commondashboard.ChartItem;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ChartItemFilter {

    private final List<ChartItem> chartItems;
    private final String username;
    private final String userTag;


    public List<ChartItem> filter() {

        List<ChartItem> filteredByIsVisible = filterByIsVisible(chartItems);
        List<ChartItem> filteredByUsername = filterByUsername(filteredByIsVisible);
        return filterByTag(filteredByUsername);
    }

    private List<ChartItem> filterByIsVisible(List<ChartItem> chartItems) {
        List<ChartItem> filteredByIsVisible = chartItems.stream().filter(ChartItem::getIsVisible).collect(Collectors.toList());
        return filteredByIsVisible;
    }

    private List<ChartItem> filterByUsername(List<ChartItem> filteredByIsVisible) {
        List<ChartItem> filteredByUsername = new ArrayList<>();
        for (ChartItem chartItem : filteredByIsVisible) {
            if (chartItem.getUsernames().contains(username)) {
                filteredByUsername.add(chartItem);
            }
        }
        if (filteredByUsername.isEmpty()) {
            return filteredByIsVisible;
        }
        return filteredByUsername;
    }

    private List<ChartItem> filterByTag(List<ChartItem> filteredByUsername) {
        List<ChartItem> filteredByTag = new ArrayList<>();
        if (StringUtils.isBlank(userTag)) {
            filteredByTag = filteredByUsername;
        } else {
            for (ChartItem chartItem : filteredByUsername) {
                List<String> tags = chartItem.getTags();
                for (String tag : tags) {
                    if (tag.toUpperCase().indexOf(userTag.toUpperCase()) >= 0) {
                        filteredByTag.add(chartItem);
                        break;
                    }
                }
            }
        }
        return filteredByTag;
    }

}
