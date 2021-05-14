/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.util.List;
import java.util.LinkedList;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxCategoria"
    private ComboBox<String> boxCategoria; // Value injected by FXMLLoader

    @FXML // fx:id="boxMese"
    private ComboBox<Integer> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="boxArco"
    private ComboBox<Adiacenza> boxArco; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	this.txtResult.clear();
    	Adiacenza arco = this.boxArco.getValue();
    	if (arco==null) {
    		this.txtResult.setText("Seleziona un arco");
    		return;
    	}
    	List<String> percorso = this.model.trovaPercorso(arco.getV1(), arco.getV2());
    	this.txtResult.appendText("Percorso tra: "+arco.getV1()+" e "+arco.getV2()+":\n");
    	for (String v : percorso) {
    		this.txtResult.appendText(v+"\n");
    	}
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	if (!this.boxArco.getItems().isEmpty())
    		this.boxArco.getItems().removeAll(this.boxArco.getItems());
    	this.txtResult.clear();
    	String categoria = this.boxCategoria.getValue();
    	Integer mese= this.boxMese.getValue();
    	if (categoria==null || mese==null) {
    		this.txtResult.setText("Seleziona una categoria e un mese");
    		return;
    	}
    	this.model.CreaGrafo(categoria, mese);
    	for (Adiacenza a : model.getArchiSopraPesoMedio()) {
    		this.txtResult.appendText(a.toString());
    	}
    	
    	this.boxArco.getItems().addAll(model.getArchiSopraPesoMedio()); //riempio tendina per punto 2
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.boxCategoria.getItems().addAll(model.getCategories());
    	LinkedList<Integer> mesi = new LinkedList<Integer>();
    	for (int i=1; i<=12; i++)
    		mesi.add(i);
    	this.boxMese.getItems().addAll(mesi);
    }
}
