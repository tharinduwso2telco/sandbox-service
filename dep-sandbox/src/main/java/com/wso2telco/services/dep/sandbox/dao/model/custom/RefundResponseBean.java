/*******************************************************************************
 * Copyright (c) 2015-2017, WSO2.Telco Inc. (http://www.wso2telco.com)
 *
 * All Rights Reserved. WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
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

public class RefundResponseBean {

    private RefundResponse refundResponse;


    public RefundResponse getRefundResponse() {
        return refundResponse;
    }


    public void setRefundResponse(RefundResponse refundResponse) {
        this.refundResponse = refundResponse;
    }

    @JsonInclude(value= JsonInclude.Include.NON_NULL)
    public static class RefundResponse {

        private String clientCorrelator;

        private String endUserID;

        private String originalServerReferenceCode;

        private String reasonForRefund;

        private double refundAmount;

        private String merchantIdentification;

        private PaymentAmountWithTax paymentAmount;

        private ReceiptResponse receiptResponse;

        private String referenceCode;

        private String resourceURL;

        private String transactionOperationStatus;

        private String serverReferanceCode;


        public String getClientCorrelator() {
            return clientCorrelator;
        }

        public void setClientCorrelator(String clientCorrelator) {
            this.clientCorrelator = clientCorrelator;
        }

        public String getEndUserID() {
            return endUserID;
        }

        public void setEndUserID(String endUserID) {
            this.endUserID = endUserID;
        }

        public String getOriginalServerReferenceCode() {
            return originalServerReferenceCode;
        }

        public void setOriginalServerReferenceCode(String originalServerReferenceCode) {
            this.originalServerReferenceCode = originalServerReferenceCode;
        }

        public String getReasonForRefund() {
            return reasonForRefund;
        }

        public void setReasonForRefund(String reasonForRefund) {
            this.reasonForRefund = reasonForRefund;
        }

        public double getRefundAmount() {
            return refundAmount;
        }

        public void setRefundAmount(double refundAmount) {
            this.refundAmount = refundAmount;
        }


        public String getReferenceCode() {
            return referenceCode;
        }

        public void setReferenceCode(String referenceCode) {
            this.referenceCode = referenceCode;
        }

        public String getResourceURL() {
            return resourceURL;
        }

        public void setResourceURL(String resourceURL) {
            this.resourceURL = resourceURL;
        }

        public String getTransactionOperationStatus() {
            return transactionOperationStatus;
        }

        public void setTransactionOperationStatus(String transactionOperationStatus) {
            this.transactionOperationStatus = transactionOperationStatus;
        }

        public PaymentAmountWithTax getPaymentAmount() {
            return paymentAmount;
        }

        public void setPaymentAmount(PaymentAmountWithTax paymentAmount) {
            this.paymentAmount = paymentAmount;
        }

        public String getServerReferanceCode() {
            return serverReferanceCode;
        }

        public void setServerReferanceCode(String serverReferanceCode) {
            this.serverReferanceCode = serverReferanceCode;
        }

        public ReceiptResponse getReceiptResponse() {
            return receiptResponse;
        }

        public void setReceiptResponse(ReceiptResponse receiptResponse) {
            this.receiptResponse = receiptResponse;
        }

        public String getMerchantIdentification() {
            return merchantIdentification;
        }

        public void setMerchantIdentification(String merchantIdentification) {
            this.merchantIdentification = merchantIdentification;
        }
    }

}
