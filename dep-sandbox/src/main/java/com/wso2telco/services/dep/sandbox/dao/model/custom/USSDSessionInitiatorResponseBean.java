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

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class USSDSessionInitiatorResponseBean {

    private OutboundUSSDMessageRequest outboundUSSDMessageRequest;

    public OutboundUSSDMessageRequest getOutboundUSSDMessageRequest() {
        return outboundUSSDMessageRequest;
    }

    public void setOutboundUSSDMessageRequest(OutboundUSSDMessageRequest outboundUSSDMessageRequest) {
        this.outboundUSSDMessageRequest = outboundUSSDMessageRequest;
    }

    public static class OutboundUSSDMessageRequest {

        private String address;

        private String keyword;

        private String shortCode;

        private String outboundUSSDMessage;

        private String clientCorrelator;

        private USSDSessionResponseRequest responseRequest;

        private String ussdAction;

        private String deliveryStatus;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public String getShortCode() {
            return shortCode;
        }

        public void setShortCode(String shortCode) {
            this.shortCode = shortCode;
        }

        public String getOutboundUSSDMessage() {
            return outboundUSSDMessage;
        }

        public void setOutboundUSSDMessage(String outboundUSSDMessage) {
            this.outboundUSSDMessage = outboundUSSDMessage;
        }

        public String getClientCorrelator() {
            return clientCorrelator;
        }

        public void setClientCorrelator(String clientCorrelator) {
            this.clientCorrelator = clientCorrelator;
        }

        public USSDSessionResponseRequest getResponseRequest() {
            return responseRequest;
        }

        public void setResponseRequest(USSDSessionResponseRequest responseRequest) {
            this.responseRequest = responseRequest;
        }

        public String getUssdAction() {
            return ussdAction;
        }

        public void setUssdAction(String ussdAction) {
            this.ussdAction = ussdAction;
        }

        public String getDeliveryStatus() {
            return deliveryStatus;
        }

        public void setDeliveryStatus(String deliveryStatus) {
            this.deliveryStatus = deliveryStatus;
        }
    }

}