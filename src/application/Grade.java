package application;

public class Grade {
	private String gradeName;
	private String minNumber;
	private String maxNumber;
	
	public Grade() {
		this.gradeName = "A++";
		this.minNumber = "0";
		this.maxNumber = "100";
	}
	
	public Grade(String minNumber, String maxNumber, String gradeName) {
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

	public String getMinNumber() {
		return minNumber;
	}

	public void setMinNumber(String minNumber) {
		this.minNumber = minNumber;
	}

	public String getMaxNumber() {
		return maxNumber;
	}

	public void setMaxNumber(String maxNumber) {
		this.maxNumber = maxNumber;
	}
	
}
