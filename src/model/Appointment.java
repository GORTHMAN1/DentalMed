package model;

public class Appointment {
	
	protected int idAppointment;
	protected Treatment treatment;
	protected AvailableDate date;
	protected boolean completed;
	
	public Appointment(int idAppointment, Treatment treatment, AvailableDate date) {
		super();
		this.idAppointment = idAppointment;
		this.treatment = treatment;
		this.date = date;
		this.completed = false;
	}

	public int getIdAppointment() {
		return idAppointment;
	}

	public Treatment getTreatment() {
		return treatment;
	}

	public void setTreatment(Treatment treatment) {
		this.treatment = treatment;
	}

	public AvailableDate getDate() {
		return date;
	}

	public void setDate(AvailableDate date) {
		this.date = date;
	} 

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	@Override
	public String toString() {
		return "Appointment [idAppointment= " + idAppointment + ", treatment=" + treatment + ", date=" + date + "]";
	}
	
	
}
