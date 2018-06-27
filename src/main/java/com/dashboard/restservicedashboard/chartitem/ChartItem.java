
package com.dashboard.restservicedashboard.chartitem;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "confId",
    "ids",
    "desc",
    "tags",
    "chartType",
    "entityType",
    "isVisible",
    "usernames"
})
public class ChartItem {

    @JsonProperty("confId")
    private Integer confId;
    
    @JsonProperty("ids")
    private List<Integer> ids = null;
    @JsonProperty("desc")    
    private String desc;
    @JsonProperty("tags")

    private List<String> tags = null;
    @JsonProperty("chartType")
    private String chartType;
    @JsonProperty("entityType")
    private String entityType;
    @JsonProperty("isVisible")

    private Boolean isVisible;
    @JsonProperty("usernames")

    private List<String> usernames = null;

    @JsonProperty("confId")
    public Integer getConfId() {
        return confId;
    }

    @JsonProperty("confId")
    public void setIndex(Integer confId) {
        this.confId = confId;
    }

    @JsonProperty("ids")
    public List<Integer> getIds() {
        return ids;
    }

    @JsonProperty("ids")
    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    @JsonProperty("desc")
    public String getDesc() {
        return desc;
    }

    @JsonProperty("desc")
    public void setDesc(String desc) {
        this.desc = desc;
    }

    @JsonProperty("tags")
    public List<String> getTags() {
        return tags;
    }

    @JsonProperty("tags")
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @JsonProperty("chartType")
    public String getChartType() {
        return chartType;
    }

    @JsonProperty("chartType")
    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    @JsonProperty("entityType")
    public String getEntityType() {
        return entityType;
    }

    @JsonProperty("entityType")
    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    @JsonProperty("isVisible")
    public Boolean getIsVisible() {
        return isVisible;
    }

    @JsonProperty("isVisible")
    public void setIsVisible(Boolean isVisible) {
        this.isVisible = isVisible;
    }

    @JsonProperty("usernames")
    public List<String> getUsernames() {
        return usernames;
    }

    @JsonProperty("usernames")
    public void setUsernames(List<String> usernames) {
        this.usernames = usernames;
    }
    
   

}
