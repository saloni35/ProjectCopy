package com.example.acer.projectcopy;

import com.example.acer.projectcopy.util.FormatterUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Acer on 4/2/2018.
 */

public class Plan {

    private String id;
    private String authorId;
    private String title;
    private String description;
    private String source;
    private String destination;
    private int budget;
    private String placesToTravel;
    private String tripType;
    private String descriptionOfTravelPartner;
    private int noOfDays;
    private long createdDate;
    private Date fromDate;
    private Date toDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public String getPlacesToTravel() {
        return placesToTravel;
    }

    public void setPlacesToTravel(String placesToTravel) {
        this.placesToTravel = placesToTravel;
    }

    public String getDescriptionOfTravelPartner() {
        return descriptionOfTravelPartner;
    }

    public void setDescriptionOfTravelPartner(String descriptionOfTravelPartner) {
        this.descriptionOfTravelPartner = descriptionOfTravelPartner;
    }

    public int getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(int noOfDays) {
        this.noOfDays = noOfDays;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("title", title);
        result.put("description", description);
        result.put("createdDate", createdDate);
        result.put("source", source);
        result.put("destination", destination);
        result.put("authorId", authorId);
        result.put("budget", budget);
        result.put("placesToTravel", placesToTravel);
        result.put("tripType", tripType);
        result.put("descriptionOfTravelPartner", descriptionOfTravelPartner);
        result.put("noOfDays", noOfDays);
        result.put("createdDateText", FormatterUtil.getFirebaseDateFormat().format(new Date(createdDate)));
        result.put("fromDate", fromDate);
        result.put("toDate", toDate);

        return result;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }
}
