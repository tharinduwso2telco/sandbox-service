package com.wso2telco.services.dep.sandbox.dao.model.custom;

/**
 * Created by sidath on 2/7/17.
 */
public class PaymentAmountResponseWithTax {

    private ChargingInformation chargingInformation;

    private ChargingMetaDataWithTax chargingMetaData;

    public ChargingMetaDataWithTax getChargingMetaDataWithTax() {
        return chargingMetaData;
    }

    public void setChargingMetaDataWithTax(ChargingMetaDataWithTax chargingMetaDataWithTax) {
        this.chargingMetaData = chargingMetaDataWithTax;
    }

    public ChargingInformation getChargingInformation() {
        return chargingInformation;
    }

    public void setChargingInformation(ChargingInformation chargingInformation) {
        this.chargingInformation = chargingInformation;
    }

}

