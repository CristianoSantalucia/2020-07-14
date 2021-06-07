/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Model;
import it.polito.tdp.PremierLeague.model.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController
{
	private Model model;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="btnCreaGrafo"
	private Button btnCreaGrafo; // Value injected by FXMLLoader

	@FXML // fx:id="btnClassifica"
	private Button btnClassifica; // Value injected by FXMLLoader

	@FXML // fx:id="btnSimula"
	private Button btnSimula; // Value injected by FXMLLoader

	@FXML // fx:id="cmbSquadra"
	private ComboBox<Team> cmbSquadra; // Value injected by FXMLLoader

	@FXML // fx:id="txtN"
	private TextField txtN; // Value injected by FXMLLoader

	@FXML // fx:id="txtX"
	private TextField txtX; // Value injected by FXMLLoader

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader

	@FXML void doCreaGrafo(ActionEvent event)
	{
		try
		{
			this.model.creaGrafo();
			this.cmbSquadra.getItems().addAll(this.model.getVertici()); 
			
			if(this.cmbSquadra.getItems().isEmpty())
			{
				this.txtResult.appendText("\nERRORE");
				return; 
			}
			
			this.btnClassifica.setDisable(false);
			this.btnSimula.setDisable(false);
			
			this.txtResult.appendText("\nGrafo Creato:\n#Vertci: " + this.model.getNumVertici() + "\n#Archi:" + this.model.getNumArchi());
		}
		catch (Exception e)
		{
			this.txtResult.appendText("\nERRORE 1 ");
			e.printStackTrace();
		}
	}

	@FXML void doClassifica(ActionEvent event)
	{
		try
		{
			Team s = this.cmbSquadra.getValue(); 
			
			Map<Team, Double> out = this.model.getBattuteDa(s);
			Map<Team, Double> in = this.model.getHannoBattuto(s);
			
			this.txtResult.appendText("\n\nBattute:");
			for(Team vicino : out.keySet())
			{
				this.txtResult.appendText("\n" + vicino.getName() + " (" + out.get(vicino) + ")");
			}
			this.txtResult.appendText("\n\nBattuta da:");
			for(Team vicino : in.keySet())
			{
				this.txtResult.appendText("\n" + vicino.getName() + " (" + in.get(vicino) + ")");
			}
		}
		catch (Exception e)
		{
			this.txtResult.appendText("\nERRORE 2 ");
			e.printStackTrace();
		}
	}
	
	@FXML void doSimula(ActionEvent event)
	{
		try
		{
			Integer N = Integer.parseInt(this.txtN.getText());
			Integer X = Integer.parseInt(this.txtX.getText());
			
			if(N > 0 && X > 0)
				this.model.simula(N, X);
			else 
				this.txtResult.appendText("\nINSERIRE NUMERI SENSATI. COGLIONE");
		}
		catch (NumberFormatException e)
		{
			this.txtResult.appendText("\nERRORE 3");
			e.printStackTrace();
		}
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize()
	{
		assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
		assert btnClassifica != null : "fx:id=\"btnClassifica\" was not injected: check your FXML file 'Scene.fxml'.";
		assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
		assert cmbSquadra != null : "fx:id=\"cmbSquadra\" was not injected: check your FXML file 'Scene.fxml'.";
		assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
		assert txtX != null : "fx:id=\"txtX\" was not injected: check your FXML file 'Scene.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
	}

	public void setModel(Model model)
	{
		this.model = model;
	}
}
