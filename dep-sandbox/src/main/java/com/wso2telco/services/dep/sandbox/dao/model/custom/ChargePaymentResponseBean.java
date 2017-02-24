/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *
 * WSO2.Telco Inc. licences this file to you under  the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.wso2telco.services.dep.sandbox.dao.model.custom;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public class ChargePaymentResponseBean {

    private String clientCorrelator;

    private String endUserId;

    private String originalReferenceCode;

    private String originalServerReferenceCode;

    private ChargeAmountResponse paymentAmount;

    private String referenceCode;

    private String serverReferenceCode;

    private String resourceURL;

    private String transactionOperationStatus;

    public String getClientCorrelator() {
        return clientCorrelator;
    }

    public void setClientCorrelator(String clientCorrelator) {
        this.clientCorrelator = clientCorrelator;
    }

    public String getEndUserId() {
        return endUserId;
    }

    public void setEndUserId(String endUserId) {
        this.endUserId = endUserId;
    }

    public String getOriginalReferenceCode() {
        return originalReferenceCode;
    }

    public void setOriginalReferenceCode(String originalReferenceCode) {
        this.originalReferenceCode = originalReferenceCode;
    }

    public String getOriginalServerReferenceCode() {
        return originalServerReferenceCode;
    }

    public void setOriginalServerReferenceCode(String originalServerReferenceCode) {
        this.originalServerReferenceCode = originalServerReferenceCode;
    }

    public String getReferenceCode() {
        return referenceCode;
    }


    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }

    public String getServerReferenceCode() {
        return serverReferenceCode;
    }

    public void setServerReferenceCode(String serverReferenceCode) {
        this.serverReferenceCode = serverReferenceCode;
    }

    public String getTransactionOperationStatus() {
        return transactionOperationStatus;
    }

    public void setTransactionOperationStatus(String transactionOperationStatus) {
        this.transactionOperationStatus = transactionOperationStatus;
    }

    public ChargeAmountResponse getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(ChargeAmountResponse paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getResourceURL() {
        return resourceURL;
    }

    public void setResourceURL(String resourceURL) {
        this.resourceURL = resourceURL;
    }
}
