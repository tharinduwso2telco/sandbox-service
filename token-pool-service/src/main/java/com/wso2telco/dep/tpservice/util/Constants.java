package com.wso2telco.dep.tpservice.util;

public class Constants {
	
	public static final String CONTEXT_TOKEN="TOKEN"; 
	public enum AuthMethod{
		BASIC("Basic ");
		private final String value;
        
		AuthMethod(String val) {
            value = val;
        }

        @Override
        public String toString() {
            return value;
        }
		}
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
	
	public enum URLProperties{
		URL_METHOD("POST"),
		AUTHORIZATION_GRANT_TYPE("Authorization"),
		LENGTH("Content-Length");		
		
		private final String value;
		
		URLProperties(String val){
			 value = val;
        }
		
		public String getValue() {
         return value;
     }
	}
	public enum URLTypes{
		CONTENT("Content-Type","application/x-www-form-urlencoded"),
		ENCODING("charset","utf-8");
				
		private  String type;
		private  String value;

		URLTypes(final String  type, final String  value) {
			this.value = value;
			this.type = type;
		}
		public String getType() {
			return type;
		}

		public String getValue() {
			return value;
		}
	}
}
