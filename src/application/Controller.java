package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LongStringConverter;

public class Controller implements Initializable{
	//This has to be initialized from file first;
	private ArrayList<String> assessmentNames = new ArrayList<>();
	
	@FXML
	private TableView<Student> tableView;
	
	@FXML
    private TableColumn<Student, Integer> snCol;
	
	@FXML
    private TableColumn<Student, Long> idCol;
	
	@FXML
    private TableColumn<Student, String> nameCol;
	
	@FXML
    private TextArea enterId;

    @FXML
    private TextArea enterName;
    
    @FXML
    private TextField enterAssessment;

	
    
	public void addAssessment(ActionEvent event) {
		String assessmentName = enterAssessment.getText().trim();
		
		if(assessmentName.equals("")) return;
		
		
		TableColumn<Student, Float> assessmentCol = new TableColumn<> (assessmentName);
		assessmentNames.add(assessmentName);
		
		ObservableList<Student> students = tableView.getItems();
		
		int i;
		for(i = 0; i < students.size(); i++) {
			Student student = students.get(i);
			student.setAssessmentNames(assessmentNames);
			
			ArrayList<Float> assessmentMarks = student.getAssessmentMarks();
			assessmentMarks.add(0.0f);
			student.setAssessmentMarks(assessmentMarks);
			
		}
		
		assessmentCol.setCellValueFactory(c -> {
		    Student student = c.getValue();
		    float mark = student.getMark(assessmentName);

		    return new ReadOnlyObjectWrapper<>(Float.valueOf(mark));
		});
		
		assessmentCol.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
		assessmentCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Student,Float>>() {
			@Override
			public void handle(CellEditEvent<Student, Float> arg0) {
				Student student = arg0.getRowValue();
				float newMark = arg0.getNewValue();
				
				student.setMark(assessmentName, newMark);
				System.out.println(student.getAssessmentMarks().get(0));
				tableView.refresh();
			}
		});;
		
		tableView.getColumns().add(assessmentCol);
		tableView.setItems(students);
		enterAssessment.clear();
	}
	
	public void addIdName(ActionEvent event) {
		int i, j;
		String[] idStrings = enterId.getText().split("\n");
		String[] nameStrings = enterName.getText().split("\n");
		int length = idStrings.length <= nameStrings.length ? idStrings.length : nameStrings.length;

		Student[] student = new Student[length];
		
		for(i = 0; i < length; i++) {
			student[i] = new Student();
			student[i].setSn(tableView.getItems().size() + 1);
			student[i].setId(Long.parseLong(idStrings[i].trim()));
			student[i].setName(nameStrings[i].trim());
			
			if(student[i].getName().equals("")) break;
			
			student[i].setAssessmentNames(assessmentNames);
			
			//setting default marks(0.0f) for each assessment in student class
			ArrayList<Float> assessmentMarks = student[i].getAssessmentMarks();
			for(j = 0; j < this.assessmentNames.size(); j++) {
				assessmentMarks.add(0.0f);
				student[i].setAssessmentMarks(assessmentMarks);
			}
			
			ObservableList<Student> students = tableView.getItems();
			students.add(student[i]);
			tableView.setItems(students);
		}
		enterId.clear();
		enterName.clear();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		snCol.setCellValueFactory(new PropertyValueFactory<Student, Integer>("sn"));
		snCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
		snCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Student,Integer>>() {
			
			@Override
			public void handle(CellEditEvent<Student, Integer> arg0) {
				Student student = arg0.getRowValue();
				student.setSn(arg0.getNewValue());
				tableView.refresh();
			}
		});;
		
		idCol.setCellValueFactory(new PropertyValueFactory<Student, Long>("id"));
		idCol.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));
		idCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Student,Long>>() {
			
			@Override
			public void handle(CellEditEvent<Student, Long> arg0) {
				Student student = arg0.getRowValue();
				student.setId(arg0.getNewValue());
				tableView.refresh();
			}
		});;
		
		nameCol.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
		nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
		nameCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Student,String>>() {
			
			@Override
			public void handle(CellEditEvent<Student, String> arg0) {
				Student student = arg0.getRowValue();
				student.setName(arg0.getNewValue());
			}
		});;
	}
}
