/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wso2telco.services.dep.sandbox.dao.model.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author User
 */
@Entity
@Table(name = "numbers")
public class ManageNumber implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "number")
	private String Number;

	@Column(name = "imsi")
	private String imsi;

	@Column(name = "mcc")
	private int mcc;

	@Column(name = "mnc")
	private int mnc;

	@Column(name = "num_description")
	private String description;

	@Column(name = "num_balance")
	private double balance;

	@Column(name = "reserved_amount", columnDefinition = "double default true")
	private double reserved_amount;

	@Column(name = "num_status")
	private int status;

	@Column(name="altitude")
	private String altitude;

	@Column(name="latitude")
	private String latitude;

	@Column(name="longitude")
	private String longitude;

	@Column(name="loc_ret_status")
	private String locationRetrieveStatus;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the ManageNumber
	 */
	public String getNumber() {
		return Number;
	}

	/**
	 * @param Number
	 *            the Number to set
	 */
	public void setNumber(String Number) {
		this.Number = Number;
	}

	public String getIMSI() {
		return imsi;
	}

	public void setIMSI(String imsi) {
		this.imsi = imsi;
	}

	public int getMNC() {
		return mnc;
	}

	public void setMNC(int mnc) {
		this.mnc = mnc;
	}

	public int getMCC() {
		return mcc;
	}

	public void setMCC(int mcc) {
		this.mcc = mcc;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the balance
	 */
	public double getBalance() {
		return balance;
	}

	/**
	 * @param balance
	 *            the balance to set
	 */
	public void setBalance(double balance) {
		this.balance = balance;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the reserved_amount
	 */
	public double getReserved_amount() {
		return reserved_amount;
	}

	/**
	 * @param reserved_amount
	 *            the reserved_amount to set
	 */
	public void setReserved_amount(double reserved_amount) {
		this.reserved_amount = reserved_amount;
	}

	/**
	 * get location parameter altitude.
	 * @return altitude
	 */
	public String getAltitude() {
		return altitude;
	}

	/**
	 * Set location parameter altitude.
	 * @param altitude altitude
	 */
	public void setAltitude(String altitude) {
		this.altitude = altitude;
	}

    /**
     * Get location parameter latitude
     * @return latitude
     */
	public String getLatitude() {
		return latitude;
	}

    /**
     * Set location parameter latitude.
     * @param latitude latitude
     */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

    /**
     * Get location parameter longitude.
     * @return longitude
     */
	public String getLongitude() {
		return longitude;
	}

    /**
     * Set location parameter longitude.
     * @param longitude longitude
     */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

    /**
     * Get Location Retrieve Status.
     * @return status
     */
	public String getLocationRetrieveStatus() {
		return locationRetrieveStatus;
	}

    /**
     * Set Location Retrieve Status.
     * @param locationRetrieveStatus status
     */
	public void setLocationRetrieveStatus(String locationRetrieveStatus) {
		this.locationRetrieveStatus = locationRetrieveStatus;
	}
}
