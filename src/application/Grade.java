package application;

public class Grade {
	private String gradeName;
	private double minNumber;
	private double maxNumber;
	
	public Grade() {
		this.gradeName = null;
		this.minNumber = 0;
		this.maxNumber = 100;
	}
	
	public Grade(String gradeName, double minNumber, double maxNumber) {
		this.gradeName = gradeName;
		this.minNumber = minNumber;
		this.maxNumber = maxNumber;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public double getMinNumber() {
		return minNumber;
	}

	public void setMinNumber(double minNumber) {
		this.minNumber = minNumber;
	}

	public double getMaxNumber() {
		return maxNumber;
	}

	public void setMaxNumber(double maxNumber) {
		this.maxNumber = maxNumber;
	}
	
	
	
}
