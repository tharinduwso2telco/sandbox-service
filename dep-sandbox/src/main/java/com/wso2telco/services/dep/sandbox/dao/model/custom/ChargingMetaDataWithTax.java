package com.wso2telco.services.dep.sandbox.dao.model.custom;



import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value=Include.NON_NULL)
public class ChargingMetaDataWithTax {

    private String onBehalfOf;

    private String purchaseCategoryCode;

    private String channel;

    private String tax;

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getOnBehalfOf() {
        return onBehalfOf;
    }

    public void setOnBehalfOf(String onBehalfOf) {
        this.onBehalfOf = onBehalfOf;
    }

    public String getPurchaseCategoryCode() {
        return purchaseCategoryCode;
    }

    public void setPurchaseCategoryCode(String purchaseCategoryCode) {
        this.purchaseCategoryCode = purchaseCategoryCode;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(" onBehalfOf : " + onBehalfOf);
        builder.append(" categoryCode : " + purchaseCategoryCode);
        builder.append(" channel : " + channel);
        builder.append("taxAmount" +tax);

        return builder.toString();
    }

}
