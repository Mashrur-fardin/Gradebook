package application;

import java.util.ArrayList;

public class Student {
	private int sn = -1;
	private long id;
	private String name;
//	private String[] assesment = new String[50];
//	private String[] assesmentName = new String[50];
	private static ArrayList<String> assessmentNames = new ArrayList<>();
	private ArrayList<Float> assessmentMarks = new ArrayList<>(); 
	
	public Student() {
		this.id = 0;
		this.name = "";
	}
	public Student(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public int getSn() {
		return sn;
	}
	public void setSn(int sn) {
		this.sn = sn;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public ArrayList<String> getAssessmentNames() {
		return this.assessmentNames;
	}
	public void setAssessmentNames(ArrayList<String> assessmentNames) {
		this.assessmentNames = assessmentNames;
	}
	public ArrayList<Float> getAssessmentMarks() {
		return this.assessmentMarks;
	}
	public void setAssessmentMarks(ArrayList<Float> assessmentMarks) {
		this.assessmentMarks = assessmentMarks;
	}
	
	public float getMark(String assessmentName) {
		int i;
		for(i = 0; i < this.assessmentNames.size(); i++) {
			if(this.assessmentNames.get(i).equals(assessmentName)) {
				return assessmentMarks.get(i);
			}
		}
		return 0.0f;
	}
	
	public void setMark(String assessmentName, float mark) {
		int i;
		for(i = 0; i < assessmentNames.size(); i++) {
			if(assessmentNames.get(i).equals(assessmentName)) {
				assessmentMarks.set(i, mark);
			}
		}
	}
	
}
