package controlador;

import java.io.IOException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.Persona;
import vista.ControladorEditar;
import vista.ControladorPersona;

public class MainAgendaApp extends Application {

	 private Stage primaryStage;
	 private BorderPane rootLayout;

	 /**
	     * The data as an observable list of Persons.
	     */
	    private ObservableList<Persona> personData = FXCollections.observableArrayList();

	    /**
	     * Constructor
	     */
	    public MainAgendaApp() {
	        // Add some sample data
	        personData.add(new Persona("Hans", "Muster"));
	        personData.add(new Persona("Ruth", "Mueller"));
	        personData.add(new Persona("Heinz", "Kurz"));
	        personData.add(new Persona("Cornelia", "Meier"));
	        personData.add(new Persona("Werner", "Meyer"));
	        personData.add(new Persona("Lydia", "Kunz"));
	        personData.add(new Persona("Anna", "Best"));
	        personData.add(new Persona("Stefan", "Meier"));
	        personData.add(new Persona("Martin", "Mueller"));
	    }
	  
	    /**
	     * Returns the data as an observable list of Persons. 
	     * @return
	     */
	    public ObservableList<Persona> getPersonData() {
	        return personData;
	    }
	 
	    @Override
	    public void start(Stage primaryStage) {
	        this.primaryStage = primaryStage;
	        this.primaryStage.setTitle("AgendaApp");

	        initRootLayout();

	        showPersonOverview();
	    }
	    
	    /**
	     * Initializes the root layout.
	     */
	    public void initRootLayout() {
	        try {
	            // Load root layout from fxml file.
	            FXMLLoader loader = new FXMLLoader();
	            loader.setLocation(MainAgendaApp.class.getResource("../vista/Raiz.fxml"));
	            rootLayout = (BorderPane) loader.load();
	            
	            // Show the scene containing the root layout.
	            Scene scene = new Scene(rootLayout);
	            primaryStage.setScene(scene);
	            primaryStage.show();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    /**
	     * Shows the person overview inside the root layout.
	     */
	    public void showPersonOverview() {
	        try {
	            // Load person overview.
	            FXMLLoader loader = new FXMLLoader();
	            loader.setLocation(MainAgendaApp.class.getResource("../vista/Persona.fxml"));
	            AnchorPane personOverview = (AnchorPane) loader.load();
	            
	            // Set person overview into the center of root layout.
	            rootLayout.setCenter(personOverview);

	            // Give the controller access to the main app.
	            ControladorPersona controller = loader.getController();
	            controller.setMainApp(this);

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    /**
	     * Returns the main stage.
	     * @return
	     */
	    public Stage getPrimaryStage() {
	        return primaryStage;
	    }
	    
	    /**
	     * Opens a dialog to edit details for the specified person. If the user
	     * clicks OK, the changes are saved into the provided person object and true
	     * is returned.
	     * 
	     * @param person the person object to be edited
	     * @return true if the user clicked OK, false otherwise.
	     */
	    public boolean showPersonEditDialog(Persona person) {
	        try {
	            // Load the fxml file and create a new stage for the popup dialog.
	            FXMLLoader loader = new FXMLLoader();
	            loader.setLocation(MainAgendaApp.class.getResource("../vista/EditarPersona.fxml"));
	            AnchorPane page = (AnchorPane) loader.load();

	            // Create the dialog Stage.
	            Stage dialogStage = new Stage();
	            dialogStage.setTitle("Edit Person");
	            dialogStage.initModality(Modality.WINDOW_MODAL);
	            dialogStage.initOwner(primaryStage);
	            Scene scene = new Scene(page);
	            dialogStage.setScene(scene);

	            // Set the person into the controller.
	            ControladorEditar controller = loader.getController();
	            controller.setDialogStage(dialogStage);
	            controller.setPerson(person);

	            // Show the dialog and wait until the user closes it
	            dialogStage.showAndWait();

	            return controller.isOkClicked();
	        } catch (IOException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }

	    public static void main(String[] args) {
	        launch(args);
	    }
}
