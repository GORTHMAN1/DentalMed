package model;

public class Treatment {
	protected int treatmentId;
	protected String name;
	protected int price;
	
	public Treatment(int treatmentId, String name, int price) {
		super();
		this.treatmentId = treatmentId;
		this.name = name;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getTreatmentId() {
		return treatmentId;
	}

	public void setTreatmentId(int treatmentId) {
		this.treatmentId = treatmentId;
	}

	@Override
	public String toString() {
		return treatmentId + ". " + name + " " + price + " lei.";
	}

	
}
