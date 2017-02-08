package com.wso2telco.services.dep.sandbox.dao.model.custom;

public class RefundResponseBean {

    private RefundResponse refundResponse;


    public RefundResponse getRefundResponse() {
        return refundResponse;
    }


    public void setRefundResponse(RefundResponse refundResponse) {
        this.refundResponse = refundResponse;
    }


    public static class RefundResponse {

        private String clientCorrelator;

        private String endUserID;

        private String originalServerReferenceCode;

        private String reasonForRefund;

        private double refundAmount;

        private PaymentAmountWithTax paymentAmount;

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
    }

}
