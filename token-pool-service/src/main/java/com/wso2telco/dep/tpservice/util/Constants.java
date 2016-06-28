package com.wso2telco.dep.tpservice.util;

public class Constants {
	public enum Tables {
		TABLE_TSXWHO("tsxwho"),
		TABLE_TSTTOKEN("tsttoken"),
		TABLE_TSTEVENT("tstevent");
		
		private final String value;
        
		Tables(String val) {
            value = val;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
