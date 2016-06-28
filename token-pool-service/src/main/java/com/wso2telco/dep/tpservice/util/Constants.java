package com.wso2telco.dep.tpservice.util;

public class Constants {

	public static final String TABLE_TSXWHO = "tsxwho";
	public static final String TABLE_TSTTOKEN = "tsttoken";
	public static final String TABLE_TSTEVENT = "tstevent";
	
	public enum TableTsxWhoColumns {
		TSXWHODID("tsxwhodid"),
		OWNER_ID("ownerid"),
		TOKEN_URL("tokenurl"),
		DEFAULT_CONNECTION_RESET_TIME("defaultconnectionresettime"),
		IS_VALID("isvalid"),
		CREATED_DATE("createddate"),
		UC("uc");
		
		private final String value;
        
		TableTsxWhoColumns(String val) {
            value = val;
        }

        @Override
        public String toString() {
            return value;
        }
    }

	public enum TableTstTokenColumns {
		TOKENDID("tokendid"),
		TSXWHODID("tsxwhodid"),
		TOKEN_AUTH("tokenauth"),
		TOKEN_VALIDITY("tokenvalidity"),
		ACCESS_TOKEN("accesstoken"),
		REFRESH_TOKEN("refreshtoken"),
		LAST_REFRESH_DONE("lastrefreshdone"),
		IS_VALID("isvalid"),
		UC("uc"),
		CREATED_TIME("createdtime");
		
		private final String value;
        
		TableTstTokenColumns(String val) {
            value = val;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
