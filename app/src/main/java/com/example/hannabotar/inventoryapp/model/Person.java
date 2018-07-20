package com.example.hannabotar.inventoryapp.model;

import java.math.BigDecimal;
import java.util.Date;

public class Person {

    private String name;
    private Date date;
    private String priority;
    private Boolean shipped;
    private BigDecimal total;

    public Person(String name, Date date, String priority, Boolean shipped, BigDecimal total) {
        this.name = name;
        this.date = date;
        this.priority = priority;
        this.shipped = shipped;
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Boolean getShipped() {
        return shipped;
    }

    public void setShipped(Boolean shipped) {
        this.shipped = shipped;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
