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

package com.wso2telco.services.dep.sandbox.servicefactory.payment;

import com.wso2telco.services.dep.sandbox.dao.model.custom.ChargePaymentDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ErrorResponseDTO;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractReturnWrapperDTO;

public class PaymentResponseWrapper extends AbstractReturnWrapperDTO {

    private ChargePaymentDTO makePaymentDTO;

    public ChargePaymentDTO getMakePaymentDTO() {
        return makePaymentDTO;
    }

    public void setMakePaymentDTO(ChargePaymentDTO makePaymentDTO) {
        this.makePaymentDTO = makePaymentDTO;
    }

    @Override
    public Object getResponse() {
        if (getRequestError() == null) {
            return makePaymentDTO;
        } else {
            ErrorResponseDTO response = new ErrorResponseDTO(getRequestError());
            return response;

        }
    }
}
