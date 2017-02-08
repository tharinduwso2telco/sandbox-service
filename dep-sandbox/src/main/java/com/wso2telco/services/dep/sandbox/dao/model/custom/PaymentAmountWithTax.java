package com.wso2telco.services.dep.sandbox.dao.model.custom;

/**
 * Created by sidath on 2/7/17.
 */
public class PaymentAmountWithTax {

    private ChargingInformation chargingInformation;

    private ChargingMetaDataWithTax chargingMetaData;

    public ChargingMetaDataWithTax getChargingMetaData() {
        return chargingMetaData;
    }

    public void setChargingMetaData(ChargingMetaDataWithTax chargingMetaData) {
        this.chargingMetaData = chargingMetaData;
    }

    public ChargingInformation getChargingInformation() {
        return chargingInformation;
    }

    public void setChargingInformation(ChargingInformation chargingInformation) {
        this.chargingInformation = chargingInformation;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();

        if (chargingInformation != null) {
            builder.append(" " + getChargingInformation().toString());
        }

        if(chargingMetaData != null){
            builder.append(" "+getChargingMetaData().toString());
        }

        return builder.toString();
    }



}
