/*
*    DelianCubeEngine. A simple cube query engine.
*    Copyright (C) 2018  Panos Vassiliadis
*
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU Affero General Public License as published
*    by the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU Affero General Public License for more details.
*
*    You should have received a copy of the GNU Affero General Public License
*    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*
*/

package client.gui.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import client.gui.utils.CustomAlertDialog;
import client.gui.utils.ExitController;

public class DataWindowController extends AbstractController {
	@FXML
	private TableView<ObservableList<StringProperty>> dataTable;
	
	@FXML
	private TextArea textLogger;
	
	private File file = null;

	
	
	public DataWindowController() {
		super();
	}

	public DataWindowController(String aFileName) {
		super();
		dataTable = new TableView<ObservableList<StringProperty>>();
		textLogger = new TextArea();
		
		this.file = new File(aFileName);
	}
	
	public void autoloadFile() {
		//String textLogged = "File Opened:" + aFileName;
		
		if(file == null)
			return;
		try {
System.out.println(file.getAbsolutePath());		
			DataWindowController.readTSVandPopulateTableView(dataTable, file,true);
		} catch (IOException eio) {
			eio.printStackTrace();
		}


		//logToTextLogger(textLogged);	
		
	}
	
//	public DataWindowController(ActionEvent event) {
//		super(event);
//	}
//

	
	private void logToTextLogger(String textLogged) {
		String prevText = textLogger.getText();
		textLogger.setText(prevText + "\n" + textLogged + "\n");
	}
	
	@FXML
	private void handleClose() {
		ExitController x = new client.gui.utils.ExitController(this.stage);
    	x.exit();
	}

	@FXML
	private void handleAbout() {
		String contentText = null;
		if(file == null) {
			contentText = "Delian Cube Engine implements a simple query engine that receives cube queries and returns the results in tsv files.";
		}
		else {
			contentText = "This data window opens a tsv file. \nFile : " + file;
		}
		CustomAlertDialog a = new CustomAlertDialog("about", null, contentText, this.stage); 
		a.show();
		
		String textLogged = "About clicked.";
		logToTextLogger(textLogged);
		
	}


	
	@FXML
	private void handleDWLoad() {
		FileChooser fileChooser = new FileChooser();
		file = fileChooser.showOpenDialog(this.stage);
		if(file == null)
			return;
		try {
			DataWindowController.readTSVandPopulateTableView(dataTable, file,true);
		} catch (IOException eio) {
			eio.printStackTrace();
		}

		String textLogged = "File Opened:" + file;
		logToTextLogger(textLogged);
	}

		
	/**
	 * Reads a tab delimited file and populates a TableView
	 * <p>
	 * From {@link https://community.oracle.com/message/10731570}
	 * Many thanks!!
	 * 
	 * @param tableView  the TableView to be populated
	 * @param fileName   a String with the tsv file name to be loaded
	 * @param hasHeader  whether the file has a header or not
	 * 
	 * @throws IOException
	 */
	public static void readTSVandPopulateTableView(TableView<ObservableList<StringProperty>> tableView, File fileName,  boolean hasHeader) throws IOException {
		BufferedReader bufferedReader = null;

		tableView.getItems().clear();
		tableView.getColumns().clear();
		tableView.setPlaceholder(new Label("Loading..."));


		if(fileName != null){
			bufferedReader = new BufferedReader(new FileReader(fileName));
		}

		// Header line
		if (hasHeader) {
			final String headerLine = bufferedReader.readLine();
			final String[] headerValues = headerLine.split("\t");
			for (int column = 0; column < headerValues.length; column++) {
				tableView.getColumns().add(
						createColumn(column, headerValues[column]));
			}
		}
		// Data:

		String dataLine;
		while ((dataLine = bufferedReader.readLine()) != null) {
			final String[] dataValues = dataLine.split("\t");
			// Add additional columns if necessary:
			for (int columnIndex = tableView.getColumns().size(); columnIndex < dataValues.length; columnIndex++) {
				tableView.getColumns().add(createColumn(columnIndex, ""));
			}
			// Add data to table:
			ObservableList<StringProperty> data = FXCollections.observableArrayList();
			for (String value : dataValues) {
				data.add(new SimpleStringProperty(value));
			}
			tableView.getItems().add(data);
		}
	}// end readCSVandPopulateTableView

	/** Auxiliary to readTSVandPopulateTableView()
	 * 
	 * @param columnIndex
	 * @param columnTitle
	 * @return
	 */
	private static TableColumn<ObservableList<StringProperty>, String> createColumn(final int columnIndex, String columnTitle) {
		TableColumn<ObservableList<StringProperty>, String> column = new TableColumn<>();
		String title;
		if (columnTitle == null || columnTitle.trim().length() == 0) {
			title = "Column " + (columnIndex + 1);
		} else {
			title = columnTitle;
		}
		column.setText(title);
		column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<StringProperty>, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(
					CellDataFeatures<ObservableList<StringProperty>, String> cellDataFeatures) {
				ObservableList<StringProperty> values = cellDataFeatures.getValue();
				if (columnIndex >= values.size()) {
					return new SimpleStringProperty("");
				} else {
					return cellDataFeatures.getValue().get(columnIndex);
				}
			}
		});
		return column;
	}//end createColumn
	
}//end class DataWindowController
