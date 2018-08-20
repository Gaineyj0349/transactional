package com.gainwise.transactional.POJO;

public class BarChartData {
    String label;
    String percentage;
    Float total;

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public BarChartData(String label, String percentage, Float total) {
        this.label = label;
        this.total = total;
        this.percentage = percentage;
    }
    public BarChartData() {
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
