/*******************************************************************************
 * Copyright (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) 
 * 
 *  All Rights Reserved. WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.ussdstub.pojo;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.ToStringBuilder;

public class OutboundUSSDMessageRequest {

    private String address;
    private String shortCode;
    private String keyword;
    private String outboundUSSDMessage;
    private String clientCorrelator;
    private ResponseRequest responseRequest;
    private String ussdAction;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public OutboundUSSDMessageRequest() {
    }

    /**
     * 
     * @param responseRequest
     * @param outboundUSSDMessage
     * @param clientCorrelator
     * @param shortCode
     * @param address
     * @param keyword
     * @param ussdAction
     */
    public OutboundUSSDMessageRequest(String address, String shortCode, String keyword, String outboundUSSDMessage, String clientCorrelator, ResponseRequest responseRequest, String ussdAction) {
        this.address = address;
        this.shortCode = shortCode;
        this.keyword = keyword;
        this.outboundUSSDMessage = outboundUSSDMessage;
        this.clientCorrelator = clientCorrelator;
        this.responseRequest = responseRequest;
        this.ussdAction = ussdAction;
    }

    /**
     * 
     * @return
     *     The address
     */
    public String getAddress() {
        return address;
    }

    /**
     * 
     * @param address
     *     The address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 
     * @return
     *     The shortCode
     */
    public String getShortCode() {
        return shortCode;
    }

    /**
     * 
     * @param shortCode
     *     The shortCode
     */
    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    /**
     * 
     * @return
     *     The keyword
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * 
     * @param keyword
     *     The keyword
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
     * 
     * @return
     *     The outboundUSSDMessage
     */
    public String getOutboundUSSDMessage() {
        return outboundUSSDMessage;
    }

    /**
     * 
     * @param outboundUSSDMessage
     *     The outboundUSSDMessage
     */
    public void setOutboundUSSDMessage(String outboundUSSDMessage) {
        this.outboundUSSDMessage = outboundUSSDMessage;
    }

    /**
     * 
     * @return
     *     The clientCorrelator
     */
    public String getClientCorrelator() {
        return clientCorrelator;
    }

    /**
     * 
     * @param clientCorrelator
     *     The clientCorrelator
     */
    public void setClientCorrelator(String clientCorrelator) {
        this.clientCorrelator = clientCorrelator;
    }

    /**
     * 
     * @return
     *     The responseRequest
     */
    public ResponseRequest getResponseRequest() {
        return responseRequest;
    }

    /**
     * 
     * @param responseRequest
     *     The responseRequest
     */
    public void setResponseRequest(ResponseRequest responseRequest) {
        this.responseRequest = responseRequest;
    }

    /**
     * 
     * @return
     *     The ussdAction
     */
    public String getUssdAction() {
        return ussdAction;
    }

    /**
     * 
     * @param ussdAction
     *     The ussdAction
     */
    public void setUssdAction(String ussdAction) {
        this.ussdAction = ussdAction;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
