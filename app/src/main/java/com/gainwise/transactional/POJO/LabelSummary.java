package com.gainwise.transactional.POJO;

public class LabelSummary {

    private String label;
    String totalAmount;
    String countOfTrans;

    public String getCountOfTrans() {
        return countOfTrans;
    }

    public void setCountOfTrans(String countOfTrans) {
        this.countOfTrans = countOfTrans;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
