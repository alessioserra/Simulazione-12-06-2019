/**
 * Sample Skeleton for 'Food.fxml' Controller Class
 */

package it.polito.tdp.food;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.food.db.Condiment;
import it.polito.tdp.food.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FoodController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtCalorie"
    private TextField txtCalorie; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="boxIngrediente"
    private ComboBox<Condiment> boxIngrediente; // Value injected by FXMLLoader
    
    @FXML
    private TextArea txtResult;

    @FXML // fx:id="btnDietaEquilibrata"
    private Button btnDietaEquilibrata; // Value injected by FXMLLoader

    @FXML
    void doCalcolaDieta(ActionEvent event) {
    	
    	txtResult.clear();
    	
    	Condiment ingrediente = boxIngrediente.getValue();
    	
    	List<Condiment> result = model.ricorsione(ingrediente);
    	Collections.sort(result);
    	
    	for (Condiment c : result) txtResult.appendText(c+"\n");
    	
    	//Calorie totali
    	double tot = 0.0;
    	for (Condiment c : result) tot = tot + c.getCondiment_calories(); 	
    	txtResult.appendText("\nCalorie totali: "+tot+" kCal");
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	boxIngrediente.getItems().clear();
    	
    	try {
    	
    	double calorie = Double.parseDouble(txtCalorie.getText());	
    	List<Condiment> ingredienti = model.listAllCondiment(calorie);
    	boxIngrediente.getItems().addAll(ingredienti); 	
    	
    	model.creaGrafo();
    	
    	for (Condiment c : ingredienti) {
    		txtResult.appendText(c+" numero di cibi: "+model.getGradoNodo(c)+"\n");		
    	}
    	
    	}catch (NumberFormatException e) {
			System.out.print("ERRORE, inserire un reale!");
		}

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtCalorie != null : "fx:id=\"txtCalorie\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Food.fxml'.";
        assert boxIngrediente != null : "fx:id=\"boxIngrediente\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnDietaEquilibrata != null : "fx:id=\"btnDietaEquilibrata\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
