package com.wso2telco.services.dep.sandbox.dao.model.custom;

public class ManageNumberRequest {

	private String number;

	private double numberBalance;

	private double reservedAmount;

	private String description;

	private int status;

	private String imsi;

	private int mcc;

	private int mnc;

	private String altitude;

	private String latitude;

	private String longitude;

	private String locationRetrieveStatus;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public double getNumberBalance() {
		return numberBalance;
	}

	public void setNumberBalance(double numberBalance) {
		this.numberBalance = numberBalance;
	}

	public double getReservedAmount() {
		return reservedAmount;
	}

	public void setReservedAmount(double reservedAmount) {
		this.reservedAmount = reservedAmount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public int getMcc() {
		return mcc;
	}

	public void setMcc(int mcc) {
		this.mcc = mcc;
	}

	public int getMnc() {
		return mnc;
	}

	public void setMnc(int mnc) {
		this.mnc = mnc;
	}

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLocationRetrieveStatus() {
        return locationRetrieveStatus;
    }

    public void setLocationRetrieveStatus(String locationRetrieveStatus) {
        this.locationRetrieveStatus = locationRetrieveStatus;
    }
}
