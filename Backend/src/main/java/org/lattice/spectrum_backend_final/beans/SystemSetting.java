package org.lattice.spectrum_backend_final.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/* POJO for system settings*/
@JsonInclude(Include.ALWAYS)
public class SystemSetting {

	private int systemId;
	private int pressureUnit;
	private int weightUnit;
	private int volumeUnit;
	private int lengthUnit;
	private int decimalPressure;
	private int decimalWeight;
	private int decimalFlowRate;
	private int decimalVolume;
	private int decimalLength;
	private int dateFormat;
	private int isAUXkonduit;
	private int isAUXkonduit_2;
	private Double uvMin;
	private Double uvMax;
	private Double phMin;
	private Double phMax;
	private Double phMin_2;
	private Double phMax_2;
	private Double turbidityMin;
	private Double turbidityMax;
	private Double turbidityMin_2;
	private Double turbidityMax_2;
	private Double totalizerMin;
	private Double totalizerMax;
	private Double totalizerMin_2;
	private Double totalizerMax_2;
	private Double proteinConcMin;
	private Double proteinConcMax;
	private Double proteinConcMin_2;
	private Double proteinConcMax_2;
	private int totalizerUnit;
	private int totalizerUnit_2;
	@JsonIgnore
	private int isActive;
	@JsonIgnore
	private Long createdOn;
	@JsonIgnore
	private Long updatedOn;
	private String digital_username;
	private String digital_password;
	private String digital_notes;

	public SystemSetting() {
		super();
	}

	public SystemSetting(String digital_username, String digital_password, String digital_notes) {
		this.digital_username = digital_username;
		this.digital_password = digital_password;
		this.digital_notes = digital_notes;
	}

	public SystemSetting(int systemId, int pressureUnit, int weightUnit, int volumeUnit, int lengthUnit,
			int decimalPressure, int decimalWeight, int decimalFlowRate, int decimalVolume, int decimalLength,
			int dateFormat, int isAUXkonduit, int isAUXkonduit_2, Double uvMin, Double uvMax, Double phMin,
			Double phMax, Double phMin_2, Double phMax_2, Double turbidityMin, Double turbidityMax,
			Double turbidityMin_2, Double turbidityMax_2, Double totalizerMin, Double totalizerMax,
			Double totalizerMin_2, Double totalizerMax_2, Double proteinConcMin, Double proteinConcMax,
			Double proteinConcMin_2, Double proteinConcMax_2, int totalizerUnit, int totalizerUnit_2, int isActive,
			Long createdOn, Long updatedOn, String digital_username, String digital_password) {
		super();
		this.systemId = systemId;
		this.pressureUnit = pressureUnit;
		this.weightUnit = weightUnit;
		this.volumeUnit = volumeUnit;
		this.lengthUnit = lengthUnit;
		this.decimalPressure = decimalPressure;
		this.decimalWeight = decimalWeight;
		this.decimalFlowRate = decimalFlowRate;
		this.decimalVolume = decimalVolume;
		this.decimalLength = decimalLength;
		this.dateFormat = dateFormat;
		this.isAUXkonduit = isAUXkonduit;
		this.isAUXkonduit_2 = isAUXkonduit_2;
		this.uvMin = uvMin;
		this.uvMax = uvMax;
		this.phMin = phMin;
		this.phMax = phMax;
		this.phMin_2 = phMin_2;
		this.phMax_2 = phMax_2;
		this.turbidityMin = turbidityMin;
		this.turbidityMax = turbidityMax;
		this.turbidityMin_2 = turbidityMin_2;
		this.turbidityMax_2 = turbidityMax_2;
		this.totalizerMin = totalizerMin;
		this.totalizerMax = totalizerMax;
		this.totalizerMin_2 = totalizerMin_2;
		this.totalizerMax_2 = totalizerMax_2;
		this.proteinConcMin = proteinConcMin;
		this.proteinConcMax = proteinConcMax;
		this.proteinConcMin_2 = proteinConcMin_2;
		this.proteinConcMax_2 = proteinConcMax_2;
		this.totalizerUnit = totalizerUnit;
		this.totalizerUnit_2 = totalizerUnit_2;
		this.isActive = isActive;
		this.createdOn = createdOn;
		this.updatedOn = updatedOn;
		this.digital_username = digital_username;
		this.digital_password = digital_password;
	}

	public int getSystemId() {
		return systemId;
	}

	public void setSystemId(int systemId) {
		this.systemId = systemId;
	}

	public int getPressureUnit() {
		return pressureUnit;
	}

	public void setPressureUnit(int pressureUnit) {
		this.pressureUnit = pressureUnit;
	}

	public int getWeightUnit() {
		return weightUnit;
	}

	public void setWeightUnit(int weightUnit) {
		this.weightUnit = weightUnit;
	}

	public int getVolumeUnit() {
		return volumeUnit;
	}

	public void setVolumeUnit(int volumeUnit) {
		this.volumeUnit = volumeUnit;
	}

	public int getLengthUnit() {
		return lengthUnit;
	}

	public void setLengthUnit(int lengthUnit) {
		this.lengthUnit = lengthUnit;
	}

	public int getDecimalPressure() {
		return decimalPressure;
	}

	public void setDecimalPressure(int decimalPressure) {
		this.decimalPressure = decimalPressure;
	}

	public int getDecimalWeight() {
		return decimalWeight;
	}

	public void setDecimalWeight(int decimalWeight) {
		this.decimalWeight = decimalWeight;
	}

	public int getDecimalFlowRate() {
		return decimalFlowRate;
	}

	public void setDecimalFlowRate(int decimalFlowRate) {
		this.decimalFlowRate = decimalFlowRate;
	}

	public int getDecimalVolume() {
		return decimalVolume;
	}

	public void setDecimalVolume(int decimalVolume) {
		this.decimalVolume = decimalVolume;
	}

	public int getDecimalLength() {
		return decimalLength;
	}

	public void setDecimalLength(int decimalLength) {
		this.decimalLength = decimalLength;
	}

	public int getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(int dateFormat) {
		this.dateFormat = dateFormat;
	}

	public int getIsAUXkonduit() {
		return isAUXkonduit;
	}

	public void setIsAUXkonduit(int isAUXkonduit) {
		this.isAUXkonduit = isAUXkonduit;
	}

	public int getIsAUXkonduit_2() {
		return isAUXkonduit_2;
	}

	public void setIsAUXkonduit_2(int isAUXkonduit_2) {
		this.isAUXkonduit_2 = isAUXkonduit_2;
	}

	public Double getUvMin() {
		return uvMin;
	}

	public void setUvMin(Double uvMin) {
		this.uvMin = uvMin;
	}

	public Double getUvMax() {
		return uvMax;
	}

	public void setUvMax(Double uvMax) {
		this.uvMax = uvMax;
	}

	public Double getPhMin() {
		return phMin;
	}

	public void setPhMin(Double phMin) {
		this.phMin = phMin;
	}

	public Double getPhMax() {
		return phMax;
	}

	public void setPhMax(Double phMax) {
		this.phMax = phMax;
	}

	public Double getPhMin_2() {
		return phMin_2;
	}

	public void setPhMin_2(Double phMin_2) {
		this.phMin_2 = phMin_2;
	}

	public Double getPhMax_2() {
		return phMax_2;
	}

	public void setPhMax_2(Double phMax_2) {
		this.phMax_2 = phMax_2;
	}

	public Double getTurbidityMin() {
		return turbidityMin;
	}

	public void setTurbidityMin(Double turbidityMin) {
		this.turbidityMin = turbidityMin;
	}

	public Double getTurbidityMax() {
		return turbidityMax;
	}

	public void setTurbidityMax(Double turbidityMax) {
		this.turbidityMax = turbidityMax;
	}

	public Double getTurbidityMin_2() {
		return turbidityMin_2;
	}

	public void setTurbidityMin_2(Double turbidityMin_2) {
		this.turbidityMin_2 = turbidityMin_2;
	}

	public Double getTurbidityMax_2() {
		return turbidityMax_2;
	}

	public void setTurbidityMax_2(Double turbidityMax_2) {
		this.turbidityMax_2 = turbidityMax_2;
	}

	public Double getTotalizerMin() {
		return totalizerMin;
	}

	public void setTotalizerMin(Double totalizerMin) {
		this.totalizerMin = totalizerMin;
	}

	public Double getTotalizerMax() {
		return totalizerMax;
	}

	public void setTotalizerMax(Double totalizerMax) {
		this.totalizerMax = totalizerMax;
	}

	public Double getTotalizerMin_2() {
		return totalizerMin_2;
	}

	public void setTotalizerMin_2(Double totalizerMin_2) {
		this.totalizerMin_2 = totalizerMin_2;
	}

	public Double getTotalizerMax_2() {
		return totalizerMax_2;
	}

	public void setTotalizerMax_2(Double totalizerMax_2) {
		this.totalizerMax_2 = totalizerMax_2;
	}

	public Double getProteinConcMin() {
		return proteinConcMin;
	}

	public void setProteinConcMin(Double proteinConcMin) {
		this.proteinConcMin = proteinConcMin;
	}

	public Double getProteinConcMax() {
		return proteinConcMax;
	}

	public void setProteinConcMax(Double proteinConcMax) {
		this.proteinConcMax = proteinConcMax;
	}

	public Double getProteinConcMin_2() {
		return proteinConcMin_2;
	}

	public void setProteinConcMin_2(Double proteinConcMin_2) {
		this.proteinConcMin_2 = proteinConcMin_2;
	}

	public Double getProteinConcMax_2() {
		return proteinConcMax_2;
	}

	public void setProteinConcMax_2(Double proteinConcMax_2) {
		this.proteinConcMax_2 = proteinConcMax_2;
	}

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	public Long getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Long createdOn) {
		this.createdOn = createdOn;
	}

	public Long getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Long updatedOn) {
		this.updatedOn = updatedOn;
	}

	@JsonIgnore
	public String getDigital_username() {
		return digital_username;
	}

	public void setDigital_username(String digital_username) {
		this.digital_username = digital_username;
	}

	@JsonIgnore
	public String getDigital_password() {
		return digital_password;
	}

	public void setDigital_password(String digital_password) {
		this.digital_password = digital_password;
	}

	public int getTotalizerUnit() {
		return totalizerUnit;
	}

	public void setTotalizerUnit(int totalizerUnit) {
		this.totalizerUnit = totalizerUnit;
	}

	public int getTotalizerUnit_2() {
		return totalizerUnit_2;
	}

	public void setTotalizerUnit_2(int totalizerUnit_2) {
		this.totalizerUnit_2 = totalizerUnit_2;
	}

	public String getDigital_notes() {
		return digital_notes;
	}

	public void setDigital_notes(String digital_notes) {
		this.digital_notes = digital_notes;
	}
}
