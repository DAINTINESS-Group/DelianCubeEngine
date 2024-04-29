package chartManagement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit.ApplicationTest;

import client.gui.controllers.ChartQueryEditorController;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChartQueryEditorControllerTest extends  ApplicationTest
{
	
	private ChartQueryEditorController controller;

    public void start(Stage stage) {
        controller = new ChartQueryEditorController();
    }

    @Test
    public void testConstructQuery() {
        // Set up input fields (Arrange)
        controller.setCubeNameTextField(new TextField("SalesCube"));
        controller.setDataSeries1(new TextField("Product"));
        controller.setX_axis(new TextField("Month"));
        controller.setY_axis(new TextField("Revenue"));
        controller.setAggregationChooser(new ComboBox<>());
        controller.getAggregationChooser().getItems().addAll("SUM", "AVG", "MAX", "MIN");
        controller.getAggregationChooser().getSelectionModel().select(0); // Select SUM
        controller.setWhereColumn(new TextField("Region"));
        controller.setWhereColumnValue(new TextField("North"));
        // Mock additional filter fields
        controller.setTextFieldContainer(new VBox());
        controller.addTextField(); // Add one additional filter field
        HBox additionalFilter = (HBox) controller.getTextFieldContainer().getChildren().get(0);
        ((TextField) additionalFilter.getChildren().get(1)).setText("ProductCategory");
        ((TextField) additionalFilter.getChildren().get(3)).setText("Electronics");

        // Call the method (Act)
        String query = controller.constructQuery();

        // Expected query string
        String expectedQuery = "ANALYZE SUM(Revenue) FROM SalesCube FOR Region='North' AND ProductCategory='Electronics' GROUP BY Product,Month AS first_query";

        // Assert
        assertEquals(expectedQuery, query);
    }
    
    /*@Test
    public void testPlotSelected_BarchartSelected() {
    	//Arrange
    	ChartQueryEditorController chartQueryEditorController = new ChartQueryEditorController();
    	ToggleGroup toggleGroup = Mockito.mock(ToggleGroup.class);
    	chartQueryEditorController.setGroup(toggleGroup);
    	RadioButton barchartRadioButton = Mockito.mock(RadioButton.class);
    	
    	chartQueryEditorController.setGroup(toggleGroup);
    	when(toggleGroup.getSelectedToggle()).thenReturn(barchartRadioButton);
//    	when(barchartRadioButton.equals(Mockito.any())).thenReturn(true);
    	//Act
    	String chartSelected = chartQueryEditorController.plotSelected();
    	//Assert
    	assertNotNull(chartSelected);
    	assertEquals("Barchart", chartSelected);
    }

    @Test
    public void testPlotSelected_ScatterplotSelected() {
        // Set up RadioButton selection
        controller.setScatterplot(new RadioButton());
        controller.getScatterplot().setSelected(true);

        // Call the method
        String plotType = controller.plotSelected();

        // Assert
        assertEquals("Scatterplot", plotType);
    }

    @Test
    public void testPlotSelected_LinechartSelected() {
        // Set up RadioButton selection
        controller.setLinechart(new RadioButton());
        controller.getLinechart().setSelected(true);

        // Call the method
        String plotType = controller.plotSelected();

        // Assert
        assertEquals("Linechart", plotType);
    }

    @Test
    public void testPlotSelected_NoRadioButtonSelected() {
        // No RadioButton selected

        // Call the method
        String plotType = controller.plotSelected();

        // Assert
        assertEquals(null, plotType);
    }*/



}
