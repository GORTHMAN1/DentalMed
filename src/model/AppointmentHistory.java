package model;

import java.util.Arrays;

public class AppointmentHistory {
	
	protected Appointment[] appointmentHistory;

	public AppointmentHistory(Appointment[] appointmentHistory) {
		super();
		this.appointmentHistory = appointmentHistory;
	}

	public Appointment[] getAppointmentHistory() {
		return appointmentHistory;
	}

	public void setAppointmentHistory(Appointment[] appointmentHistory) {
		this.appointmentHistory = appointmentHistory;
	}

	@Override
	public String toString() {
		return "AppointmentHistory [appointmentHistory=" + Arrays.toString(appointmentHistory) + "]";
	}
	
}
