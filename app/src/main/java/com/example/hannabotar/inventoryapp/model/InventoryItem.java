package com.example.hannabotar.inventoryapp.model;

/**
 * Created by hanna on 7/17/2018.
 */

public class InventoryItem {

    private String name;
    private String serialNo;
    private Integer condition;
    private String description;

    public InventoryItem(String name, String serialNo, Integer condition, String description) {
        this.name = name;
        this.serialNo = serialNo;
        this.condition = condition;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public Integer getCondition() {
        return condition;
    }

    public void setCondition(Integer condition) {
        this.condition = condition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
