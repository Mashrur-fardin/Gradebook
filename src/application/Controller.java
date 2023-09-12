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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LongStringConverter;

public class Controller implements Initializable{
	//This has to be initialized from file first;
	private ArrayList<String> assessmentNames = new ArrayList<>();
	private ArrayList<Assessment> assessmentsArrayList = new ArrayList<>(); //this is for the mini right table
	
	private boolean tableIsLocked = false;
	
	
	@FXML
    private CheckBox lockTable;
	
	@FXML
	private TableView<Student> tableView;
	
    @FXML
    private TableView<Assessment> assessmentMarksTable;
    
    @FXML
    private TableColumn<Assessment, Double> assessmentFullMarkCol;

    @FXML
    private TableColumn<Assessment, String> assessmentNameCol;

    @FXML
    private TableColumn<Assessment, Double> assessmentWeightCol;
	
	@FXML
    private TableColumn<Student, Integer> snCol;
	
	@FXML
    private TableColumn<Student, String> idCol;
	
	@FXML
    private TableColumn<Student, String> nameCol;
	
	@FXML
    private TextArea enterId;

    @FXML
    private TextArea enterName;
    
    @FXML
    private TextField enterAssessment;

    @FXML
    private TextField idToRemove;
    
    @FXML
    private ChoiceBox<String> assessmentChoiceBox;
    
    @FXML
    private RadioButton averageRadioButton;

    @FXML
    private RadioButton bestRadioButton;

    @FXML
    private RadioButton best_n_RadioButton;

    @FXML
    private RadioButton bonusRadioButton;
    
    @FXML
    private TextField bonusTextField;

    @FXML
    private TextField best_n_textField;
    
    @FXML
    private ChoiceBox<String> choiceBoxForMarkCalculation;
    
    
    
	public void addIdName(ActionEvent event) {
		if(!tableIsLocked) {
			int i, j;
			String[] idStrings = enterId.getText().split("\n");
			String[] nameStrings = enterName.getText().split("\n");
			int length = idStrings.length <= nameStrings.length ? idStrings.length : nameStrings.length;

			Student[] student = new Student[length];
			
			for(i = 0; i < length; i++) {
				student[i] = new Student();
				student[i].setSn(tableView.getItems().size() + 1);
				try {					
					student[i].setId(idStrings[i].trim());
				} catch (Exception e) {
					break;
				}
				student[i].setName(nameStrings[i].trim());
				
				if(student[i].getName().equals("")) break;
				
				student[i].setAssessmentNames(assessmentNames);
				
				//setting default marks(0.0f) for each assessment in student class
				ArrayList<String> assessmentMarks = student[i].getAssessmentMarks();
				for(j = 0; j < this.assessmentNames.size(); j++) {
					assessmentMarks.add("");
					student[i].setAssessmentMarks(assessmentMarks);
				}
				
				ObservableList<Student> students = tableView.getItems();
				students.add(student[i]);
				tableView.setItems(students);
			}
		}
		enterId.clear();
		enterName.clear();
		
	}
	
	
	public void addAssessment(ActionEvent event) {
		if(!tableIsLocked) {
			String assessmentName = enterAssessment.getText().trim();
			
			if(assessmentName.equals("")) return;
			
			
			TableColumn<Student, String> assessmentCol = new TableColumn<> (assessmentName);
			assessmentNames.add(assessmentName);
			
			Assessment assessment = new Assessment();
			assessment.setAssessmentName(assessmentName);
			assessment.setAssessmentFullMark(0.0);
			assessment.setAssessmentWeight(0.0);
			assessmentsArrayList.add(assessment);
			
			ObservableList<Student> students = tableView.getItems();
			
			int i;
			for(i = 0; i < students.size(); i++) {
				Student student = students.get(i);
				student.setAssessmentNames(assessmentNames);
				
				ArrayList<String> assessmentMarks = student.getAssessmentMarks();
				assessmentMarks.add("");
				student.setAssessmentMarks(assessmentMarks);
				
			}
			
			assessmentCol.setCellValueFactory(c -> {
			    Student student = c.getValue();
			    String mark = student.getMark(assessmentName);

			    return new ReadOnlyObjectWrapper<>(mark);
			});
			
			assessmentCol.setCellFactory(TextFieldTableCell.forTableColumn());
			assessmentCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Student,String>>() {
				@Override
				public void handle(CellEditEvent<Student, String> arg0) {
					if(!tableIsLocked) {
						Student student = arg0.getRowValue();
						String newMark = arg0.getNewValue();
						student.setMark(assessmentName, newMark);
					}

					tableView.refresh();
				}
			});;
			
			tableView.getColumns().add(assessmentCol);
			tableView.setItems(students);
			
//			This is for the right most mini assessment table.
			ObservableList<Assessment> assessmentsObservableList = assessmentMarksTable.getItems();
			for(i = 0; i < assessmentsArrayList.size(); i++) {
				try {
//					set to index i
					assessmentsObservableList.set(i, assessmentsArrayList.get(i));
				} catch (Exception e) {
//					if index i doesn't exist then add it
					assessmentsObservableList.add(assessmentsArrayList.get(i));
				}
				
			}
			assessmentMarksTable.setItems(assessmentsObservableList);

			
//		This is for removing assessments later
			assessmentChoiceBox.getItems().removeAll(assessmentNames);
			assessmentChoiceBox.getItems().addAll(assessmentNames); 
			
			choiceBoxForMarkCalculation.getItems().removeAll(assessmentNames);
			choiceBoxForMarkCalculation.getItems().addAll(assessmentNames); 
		}
		enterAssessment.clear();
	}
	
	
	public void recalculateSerialNumber(ActionEvent event) {
		if(!tableIsLocked) {
			ObservableList<Student> students = tableView.getItems();
			int i;
			for(i = 0; i < students.size(); i++) {
				students.get(i).setSn(i + 1);
				tableView.setItems(students);
				tableView.refresh();
			}
		}
		
	}
	
	public void lockTable(ActionEvent event) {
		tableIsLocked = lockTable.isSelected() ? true : false;
	}
	
	public void removeStudent(ActionEvent event) {
		if(!tableIsLocked) {
			ObservableList<Student> students = tableView.getItems();
			int i;
			for(i = 0; i < students.size(); i++) {
				if(students.get(i).getId().equals(idToRemove.getText())) {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Remove Student");
					alert.setHeaderText("You are about remove ID-" + idToRemove.getText() + " from the table.");
					alert.setContentText("This cannot be undone. Press cancel if you are not sure.");
					if(alert.showAndWait().get() == ButtonType.OK) {						
						students.remove(i);
					}
					break;
				}
			}
			idToRemove.clear();
		}
	}
	
	public void removeAssessment(ActionEvent event) {
		if(!tableIsLocked) {
			ObservableList<TableColumn<Student, ?>> tableColumns = tableView.getColumns();
			ObservableList<Assessment> assessmentsToRemove = assessmentMarksTable.getItems(); //some assessments will be removed from this list
			String columnToRemove = assessmentChoiceBox.getValue();
			int i;
			for(i = 0; i < tableColumns.size(); i++) {
				if(columnToRemove != null && columnToRemove.equals(tableColumns.get(i).getText())) {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Remove Assessment");
					alert.setHeaderText("You are about remove " + tableColumns.get(i).getText() + " from the table.");
					alert.setContentText("This cannot be undone. Press cancel if you are not sure.");
					if(alert.showAndWait().get() == ButtonType.OK) {						
						tableView.getColumns().remove(i);
						
//						removing from the mini table
						for(i = 0; i < assessmentsToRemove.size(); i++) {
							if(assessmentsToRemove.get(i).getAssessmentName().equals(columnToRemove)) {
								assessmentsToRemove.remove(i);
								assessmentMarksTable.refresh();
								break;
							}
						}
						
						for(i = 0; i < this.assessmentNames.size(); i++) {
							if(assessmentNames.get(i).equals(columnToRemove)) {
								assessmentNames.remove(i);
								assessmentsArrayList.remove(i);
								assessmentChoiceBox.getItems().remove(i);
								choiceBoxForMarkCalculation.getItems().remove(i);
								break;
							}
						}
						
					}
					break;
				}
			}
		}
	}
	
	
	public void markCalculationProcess(ActionEvent event) {
		if(averageRadioButton.isSelected()) {
			best_n_textField.setDisable(true);
			bonusTextField.setDisable(true);
			
		} else if(bestRadioButton.isSelected()) {
			best_n_textField.setDisable(true);
			bonusTextField.setDisable(true);
			
		} else if(best_n_RadioButton.isSelected()) {
			best_n_textField.setDisable(false);
			bonusTextField.setDisable(true);
			
		} else if(bonusRadioButton.isSelected()) {
			best_n_textField.setDisable(true);
			bonusTextField.setDisable(false);
		} 
	}
	
	public void calculateMark(ActionEvent event) {
		if(bonusRadioButton.isSelected() && !bonusTextField.getText().equals("")) {
			String colName = choiceBoxForMarkCalculation.getValue();
			float markToAdd = Float.parseFloat(bonusTextField.getText());
			ObservableList<Student> students = tableView.getItems();
			int i;
			for(i = 0; i < students.size(); i++) {
				try {
					float mark;
					if(students.get(i).getMark(colName).equals("")) {
						mark = 0.0f;
					} else {						
						mark = Float.parseFloat(students.get(i).getMark(colName));
					}
					mark += markToAdd;
					students.get(i).setMark(colName, mark+"");
				} catch (Exception e) {
					// do nothing
				}
			}
			tableView.setItems(students);
			tableView.refresh();
			bonusTextField.clear();
			bonusRadioButton.setSelected(false);
		}
	}
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		snCol.setCellValueFactory(new PropertyValueFactory<Student, Integer>("sn"));
		snCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
		snCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Student,Integer>>() {
			
			@Override
			public void handle(CellEditEvent<Student, Integer> arg0) {
				if(!tableIsLocked) {
					Student student = arg0.getRowValue();
					student.setSn(arg0.getNewValue());
				}
				tableView.refresh();
			}
		});;
		
		idCol.setCellValueFactory(new PropertyValueFactory<Student, String>("id"));
		idCol.setCellFactory(TextFieldTableCell.forTableColumn());
		idCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Student,String>>() {
			
			@Override
			public void handle(CellEditEvent<Student, String> arg0) {
				if(!tableIsLocked) {
					Student student = arg0.getRowValue();
					student.setName(arg0.getNewValue());
				}
				tableView.refresh();
			}
		});;
		
		nameCol.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
		nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
		nameCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Student,String>>() {
			
			@Override
			public void handle(CellEditEvent<Student, String> arg0) {
				if(!tableIsLocked) {
					Student student = arg0.getRowValue();
					student.setName(arg0.getNewValue());
				}
				tableView.refresh();
			}
		});;
		
		
		assessmentNameCol.setCellValueFactory(new PropertyValueFactory<Assessment, String>("assessmentName"));
		assessmentNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
		assessmentNameCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Assessment,String>>() {
			
			@Override
			public void handle(CellEditEvent<Assessment, String> arg0) {
				if(!tableIsLocked) {
					Assessment a = arg0.getRowValue();
					String newValueString = arg0.getNewValue();
					
					//now editing other related stuff
					 int i;
					 for(i = 0; i < assessmentNames.size(); i++) {
						 if(assessmentNames.get(i).equals(a.getAssessmentName())) {
							 assessmentNames.set(i, newValueString);
							 assessmentsArrayList.set(i, a);
							 assessmentChoiceBox.getItems().set(i, newValueString);
							 choiceBoxForMarkCalculation.getItems().set(i, newValueString);
							 break;
						 }
					 }
					 
					 
//					 changing name in main table
					 for(i = 0; i < tableView.getColumns().size(); i++) {
						 if(tableView.getColumns().get(i).getText().equals(a.getAssessmentName())) {
							 tableView.getColumns().get(i).setText(newValueString);
							 tableView.refresh();
							 break;
						 }
					 }
					 
//					 changing name in student class
					 try {
						 tableView.getItems().get(0).setAssessmentNames(assessmentNames); //used get(0) because assessmentNames is static						
					 } catch (Exception e) {
						//If table is empty do nothing
					 }

					
					a.setAssessmentName(newValueString);
				}
				assessmentMarksTable.refresh();
			}
		});;
		
		
		assessmentFullMarkCol.setCellValueFactory(new PropertyValueFactory<Assessment, Double>("assessmentFullMark"));
		assessmentFullMarkCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
		assessmentFullMarkCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Assessment, Double>>() {
			
			@Override
			public void handle(CellEditEvent<Assessment, Double> arg0) {
				if(!tableIsLocked) {
					Assessment a = arg0.getRowValue();
					a.setAssessmentFullMark(arg0.getNewValue());
				}
				assessmentMarksTable.refresh();
			}
		});;
		
		assessmentWeightCol.setCellValueFactory(new PropertyValueFactory<Assessment, Double>("assessmentWeight"));
		assessmentWeightCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
		assessmentWeightCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Assessment, Double>>() {
			
			@Override
			public void handle(CellEditEvent<Assessment, Double> arg0) {
				if(!tableIsLocked) {
					Assessment a = arg0.getRowValue();
					a.setAssessmentWeight(arg0.getNewValue());
				}
				assessmentMarksTable.refresh();
			}
		});;
	}
}
