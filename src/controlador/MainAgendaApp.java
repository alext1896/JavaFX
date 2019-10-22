package controlador;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.ListaPersonaXml;
import modelo.Persona;
import vista.ControladorEditar;
import vista.ControladorEstadistica;
import vista.ControladorPersona;
import vista.ControladorRaiz;

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
	     * Initializes the root layout and tries to load the last opened
	     * person file.
	     */
	    public void initRootLayout() {
	        try {
	            // Load root layout from fxml file.
	            FXMLLoader loader = new FXMLLoader();
	            loader.setLocation(MainAgendaApp.class
	                    .getResource("../vista/Raiz.fxml"));
	            rootLayout = (BorderPane) loader.load();

	            // Show the scene containing the root layout.
	            Scene scene = new Scene(rootLayout);
	            primaryStage.setScene(scene);

	            // Give the controller access to the main app.
	            ControladorRaiz controller = loader.getController();
	            controller.setMainApp(this);

	            primaryStage.show();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        // Try to load last opened person file.
	        File file = getPersonFilePath();
	        if (file != null) {
	            loadPersonDataFromFile(file);
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
	            dialogStage.setTitle("Editar Persona");
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
	    
	    /**
	     * Returns the person file preference, i.e. the file that was last opened.
	     * The preference is read from the OS specific registry. If no such
	     * preference can be found, null is returned.
	     * 
	     * @return
	     */
	    public File getPersonFilePath() {
	        Preferences prefs = Preferences.userNodeForPackage(MainAgendaApp.class);
	        String filePath = prefs.get("filePath", null);
	        if (filePath != null) {
	            return new File(filePath);
	        } else {
	            return null;
	        }
	    }

	    /**
	     * Sets the file path of the currently loaded file. The path is persisted in
	     * the OS specific registry.
	     * 
	     * @param file the file or null to remove the path
	     */
	    public void setPersonFilePath(File file) {
	        Preferences prefs = Preferences.userNodeForPackage(MainAgendaApp.class);
	        if (file != null) {
	            prefs.put("filePath", file.getPath());

	            // Update the stage title.
	            primaryStage.setTitle("AgendaApp - " + file.getName());
	        } else {
	            prefs.remove("filePath");

	            // Update the stage title.
	            primaryStage.setTitle("AgendaApp");
	        }
	    }
	    
	    /**
	     * Loads person data from the specified file. The current person data will
	     * be replaced.
	     * 
	     * @param file
	     */
	    public void loadPersonDataFromFile(File file) {
	        try {
	            JAXBContext context = JAXBContext
	                    .newInstance(ListaPersonaXml.class);
	            Unmarshaller um = context.createUnmarshaller();

	            // Reading XML from the file and unmarshalling.
	            ListaPersonaXml wrapper = (ListaPersonaXml) um.unmarshal(file);

	            personData.clear();
	            personData.addAll(wrapper.getPersons());

	            // Save the file path to the registry.
	            setPersonFilePath(file);

	        } catch (Exception e) { // catches ANY exception
	            Alert alert = new Alert(AlertType.ERROR);
	            alert.setTitle("Error");
	            alert.setHeaderText("No se puede cargar los datos");
	            alert.setContentText("No se puede cargar los datos del fichero:\n" + file.getPath());

	            alert.showAndWait();
	        }
	    }

	    /**
	     * Saves the current person data to the specified file.
	     * 
	     * @param file
	     */
	    public void savePersonDataToFile(File file) {
	        try {
	            JAXBContext context = JAXBContext.newInstance(ListaPersonaXml.class);
	            Marshaller m = context.createMarshaller();
	            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

	            // Wrapping our person data.
	            ListaPersonaXml wrapper = new ListaPersonaXml();
	            wrapper.setPersons(personData);

	            // Marshalling and saving XML to the file.
	            m.marshal(wrapper, file);

	            // Save the file path to the registry.
	            setPersonFilePath(file);
	        } catch (Exception e) { // catches ANY exception
	            Alert alert = new Alert(AlertType.ERROR);
	            alert.setTitle("Error");
	            alert.setHeaderText("No se pudo guardar los datos");
	            alert.setContentText("No se pudo guardar los datos en el fichero:\n" + file.getPath());

	            alert.showAndWait();
	        }
	    }
	    
	    /**
	     * Opens a dialog to show birthday statistics.
	     */
	    public void verEstadisticaCumpleaños() {
	        try {
	            // Load the fxml file and create a new stage for the popup.
	            FXMLLoader loader = new FXMLLoader();
	            loader.setLocation(MainAgendaApp.class.getResource("../vista/Estadistica.fxml"));
	            AnchorPane page = (AnchorPane) loader.load();
	            Stage dialogStage = new Stage();
	            dialogStage.setTitle("Estadistica de nacimiento");
	            dialogStage.initModality(Modality.WINDOW_MODAL);
	            dialogStage.initOwner(primaryStage);
	            Scene scene = new Scene(page);
	            dialogStage.setScene(scene);

	            // Set the persons into the controller.
	            ControladorEstadistica controller = loader.getController();
	            controller.setPersonData(personData);

	            dialogStage.show();

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    public static void main(String[] args) {
	        launch(args);
	    }
}
