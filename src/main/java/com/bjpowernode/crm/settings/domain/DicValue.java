package com.bjpowernode.crm.settings.domain;

public class DicValue {

    private String appellation;
    private String clueState;
    private String returnPriority;
    private String returnState;
    private String source;
    private String stage;
    private String transactionType;

    public String getAppellation() {
        return appellation;
    }

    public void setAppellation(String appellation) {
        this.appellation = appellation;
    }

    public String getClueState() {
        return clueState;
    }

    public void setClueState(String clueState) {
        this.clueState = clueState;
    }

    public String getReturnPriority() {
        return returnPriority;
    }

    public void setReturnPriority(String returnPriority) {
        this.returnPriority = returnPriority;
    }

    public String getReturnState() {
        return returnState;
    }

    public void setReturnState(String returnState) {
        this.returnState = returnState;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    @Override
    public String toString() {
        return "DicValue{" +
                "appellation='" + appellation + '\'' +
                ", clueState='" + clueState + '\'' +
                ", returnPriority='" + returnPriority + '\'' +
                ", returnState='" + returnState + '\'' +
                ", source='" + source + '\'' +
                ", stage='" + stage + '\'' +
                ", transactionType='" + transactionType + '\'' +
                '}';
    }
}
