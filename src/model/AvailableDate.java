package model;

import java.util.Arrays;

public class AvailableDate {
	
	protected int availableDateId;
	protected String availableDate;
	protected String[] availableHours;
	
	public AvailableDate(int availableDateId, String availableDate, String[] availableHours) {
		super();
		this.availableDateId = availableDateId;
		this.availableDate = availableDate;
		this.availableHours = availableHours;
	}

	public String getAvailableDate() {
		return availableDate;
	}

	public void setAvailableDate(String availableDate) {
		this.availableDate = availableDate;
	}

	public String[] getAvailableHours() {
		return availableHours;
	}

	public void setAvailableHours(String[] availableHours) {
		this.availableHours = availableHours;
	}

	public int getAvailableDateId() {
		return availableDateId;
	}

	public void setAvailableDateId(int availableDateId) {
		this.availableDateId = availableDateId;
	}

	@Override
	public String toString() {
		return "AvailableDate [availableDateId=" + availableDateId + ", availableDate=" + availableDate
				+ ", availableHours=" + Arrays.toString(availableHours) + "]";
	}
	
}
