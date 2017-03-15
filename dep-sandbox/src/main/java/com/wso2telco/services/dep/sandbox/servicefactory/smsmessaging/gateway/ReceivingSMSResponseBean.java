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
package com.wso2telco.services.dep.sandbox.servicefactory.smsmessaging.gateway;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.wso2telco.services.dep.sandbox.dao.model.custom.InboundSMSMessage;
import java.util.List;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
 class ReceivingSMSResponseBean {

    private InboundSMSMessageList inboundSMSMessageList;
    private int numberOfMessagesInThisBatch;
    private String resourceURL;
    private int totalNumberOfPendingMessages;

    public InboundSMSMessageList getInboundSMSMessageList() {
        return inboundSMSMessageList;
    }

    public void setInboundSMSMessageList(InboundSMSMessageList inboundSMSMessageList) {
        this.inboundSMSMessageList = inboundSMSMessageList;
    }

    public int getNumberOfMessagesInThisBatch() {
        return numberOfMessagesInThisBatch;
    }

    public void setNumberOfMessagesInThisBatch(int numberOfMessagesInThisBatch) {
        this.numberOfMessagesInThisBatch = numberOfMessagesInThisBatch;
    }

    public String getResourceURL() {
        return resourceURL;
    }

    public void setResourceURL(String resourceURL) {
        this.resourceURL = resourceURL;
    }

    public int getTotalNumberOfPendingMessages() {
        return totalNumberOfPendingMessages;
    }

    public void setTotalNumberOfPendingMessages(int totalNumberOfPendingMessages) {
        this.totalNumberOfPendingMessages = totalNumberOfPendingMessages;
    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class InboundSMSMessageList{

       private List<InboundSMSMessage> inboundSMSMessages;

       public List<InboundSMSMessage> getInboundSMSMessages() {
           return inboundSMSMessages;
       }

       public void setInboundSMSMessages(List<InboundSMSMessage> inboundSMSMessages) {
           this.inboundSMSMessages = inboundSMSMessages;
       }



   }

}
