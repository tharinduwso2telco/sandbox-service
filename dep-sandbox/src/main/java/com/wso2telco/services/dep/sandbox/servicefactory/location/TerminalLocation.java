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
package com.wso2telco.services.dep.sandbox.servicefactory.location;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class TerminalLocation {
    
    private String address=null;
    private CurrentLocation currentLocation;
    private String locationRetrievalStatus=null;


    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the currentLocation
     */
    public CurrentLocation getCurrentLocation() {
        return currentLocation;
    }

    /**
     * @param currentLocation the currentLocation to set
     */
    public void setCurrentLocation(CurrentLocation currentLocation) {
        this.currentLocation = currentLocation;
    }

    /**
     * @return the locationRetrievalStatus
     */
    public String getLocationRetrievalStatus() {
        return locationRetrievalStatus;
    }

    /**
     * @param locationRetrievalStatus the locationRetrievalStatus to set
     */
    public void setLocationRetrievalStatus(String locationRetrievalStatus) {
        this.locationRetrievalStatus = locationRetrievalStatus;
    }

    @JsonInclude(value= JsonInclude.Include.NON_NULL)
    public static class CurrentLocation {
        
        private Double accuracy=0.0;
        private Double altitude=0.0;
        private Double latitude=0.0;
        private Double longitude=0.0;
        private String timestamp;

        /**
         * @return the accuracy
         */
        public Double getAccuracy() {
            return accuracy;
        }

        /**
         * @param accuracy the accuracy to set
         */
        public void setAccuracy(Double accuracy) {
            this.accuracy = accuracy;
        }

        /**
         * @return the altitude
         */
        public Double getAltitude() {
            return altitude;
        }

        /**
         * @param altitude the altitude to set
         */
        public void setAltitude(Double altitude) {
            this.altitude = altitude;
        }

        /**
         * @return the latitude
         */
        public Double getLatitude() {
            return latitude;
        }

        /**
         * @param latitude the latitude to set
         */
        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        /**
         * @return the longitude
         */
        public Double getLongitude() {
            return longitude;
        }

        /**
         * @param longitude the longitude to set
         */
        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        /**
         * @return the timestamp
         */
        public String getTimestamp() {
            return timestamp;
        }

        /**
         * @param timestamp the timestamp to set
         */
        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }
    
    public static class RequestError{
        
        private String messageId;
        private String text;
        private String variables;

        /**
         * @return the messageId
         */
        public String getMessageId() {
            return messageId;
        }

        /**
         * @param messageId the messageId to set
         */
        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

        /**
         * @return the text
         */
        public String getText() {
            return text;
        }

        /**
         * @param text the text to set
         */
        public void setText(String text) {
            this.text = text;
        }

        /**
         * @return the variables
         */
        public String getVariables() {
            return variables;
        }

        /**
         * @param variables the variables to set
         */
        public void setVariables(String variables) {
            this.variables = variables;
        }
        
        
    }
}
