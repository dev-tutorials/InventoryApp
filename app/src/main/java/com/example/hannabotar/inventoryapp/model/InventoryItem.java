package com.example.hannabotar.inventoryapp.model;

/**
 * Created by hanna on 7/17/2018.
 */

public class InventoryItem {

    private String name;
    private String serial_no;
    private Integer condition;
    private String description;

    public InventoryItem(String name, String serialNo, Integer condition, String description) {
        this.name = name;
        this.serial_no = serialNo;
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
        return serial_no;
    }

    public void setSerialNo(String serial_no) {
        this.serial_no = serial_no;
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
