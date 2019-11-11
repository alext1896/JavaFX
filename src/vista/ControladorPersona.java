package vista;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import controlador.MainAgendaApp;
import controlador.UtilesData;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import modelo.Persona;
import util.Conexion;

public class ControladorPersona {
    private static final String DELETE_PERSONA = "DELETE FROM persona where idpersona = ?";

    @FXML
    private TableView<Persona> personTable;
    @FXML
    private TableColumn<Persona, String> firstNameColumn;
    @FXML
    private TableColumn<Persona, String> lastNameColumn;
    
    @FXML
    private Label idLabel;
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label streetLabel;
    @FXML
    private Label postalCodeLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label birthdayLabel;
    


    // Reference to the main application.
    private MainAgendaApp mainApp;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public ControladorPersona() {
    }
    /**
     * Fills all text fields to show details about the person.
     * If the specified person is null, all text fields are cleared.
     * 
     * @param person the person or null
     */
    
    @FXML
    private void initialize() {
        // Initialize the person table with the two columns.
        firstNameColumn.setCellValueFactory(
                cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(
                cellData -> cellData.getValue().lastNameProperty());
        
        
        // Clear person details.
        showPersonDetails(null);

        // Listen for selection changes and show the person details when changed.
        personTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPersonDetails(newValue));
    }
    
    /**
     * Is called by the main application to give a reference back to itself.
     * @param mainApp
     */
    public void setMainApp(MainAgendaApp mainApp) {
        this.mainApp = mainApp;
        
        // Add observable list data to the table
        personTable.setItems(mainApp.getPersonData());
    }

    /**
     * Called when the user clicks on the delete button.
     */
    @FXML
    private void handleDeletePerson() {
        int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            
            Persona person = new Persona ();
            person = personTable.getItems().get(selectedIndex);
            borrarPersonaBaseData (person);
            personTable.getItems().remove(selectedIndex);
            
        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No seleccinado");
            alert.setHeaderText("Ninguna persona seleccionada");
            alert.setContentText("Por favor, seleccione a alguna persona.");

            alert.showAndWait();
        }
        
    }
    
    public void borrarPersonaBaseData(Persona persona) {
        try {
        	Connection con = null;
    		PreparedStatement stmt = null;
    		ResultSet rs = null;

    			
    		try {
    			con = new Conexion().getConnection();

    			stmt = con.prepareStatement (DELETE_PERSONA);
    			
    			stmt.setLong(1, persona.getIdPersona());
    			stmt.executeUpdate ();

    		} catch (SQLException sqle) {
    			// En una aplicacion real, escribo en el log y delego
    			System.err.println(sqle.getMessage());
    		} finally {
    			try {
    				// Liberamos todos los recursos pase lo que pase
    				if (rs != null) {
    					rs.close();
    				}
    				if (stmt != null) {
    					stmt.close();
    				}
    				if (con != null) {
    					Conexion.closeConnection(con);
    				}
    			} catch (SQLException sqle) {
    				// En una aplicacon real, escribo en el log, no delego porque es error al liberar recursos
    			}
    		}



        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No se puede cargar los datos");

            alert.showAndWait();
        }
    }

    
    /**
     * Called when the user clicks the new button. Opens a dialog to edit
     * details for a new person. 
     */
    @FXML
    private void handleNewPerson() {
        Persona tempPerson = new Persona();
        boolean okClicked = mainApp.showPersonEditDialog(tempPerson);
        if (okClicked) {
            mainApp.getPersonData().add(tempPerson);
        }
    }

    /**
     * Called when the user clicks the edit button. Opens a dialog to edit
     * details for the selected person.
     */
    @FXML
    private void handleEditPerson() {
        Persona selectedPerson = personTable.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            boolean okClicked = mainApp.showPersonEditDialog(selectedPerson);
            
            if (okClicked) {
                showPersonDetails(selectedPerson);
                editarPersonaBaseData (selectedPerson);
            }
        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No seleccionado");
            alert.setHeaderText("No se ha seleccionado ninguna persona");
            alert.setContentText("Por favor, selecciona a una persona.");

            alert.showAndWait();
        }
    } 
    
    public void editarPersonaBaseData(Persona persona) {
        try {
        	Connection con = null;
    		PreparedStatement stmt = null;
    		ResultSet rs = null;

    			
    		try {
    			con = new Conexion().getConnection();

    			stmt = con.prepareStatement (DELETE_PERSONA);
    			
    			stmt.setLong(1, persona.getIdPersona());
    			stmt.executeUpdate ();

    		} catch (SQLException sqle) {
    			// En una aplicacion real, escribo en el log y delego
    			System.err.println(sqle.getMessage());
    		} finally {
    			try {
    				// Liberamos todos los recursos pase lo que pase
    				if (rs != null) {
    					rs.close();
    				}
    				if (stmt != null) {
    					stmt.close();
    				}
    				if (con != null) {
    					Conexion.closeConnection(con);
    				}
    			} catch (SQLException sqle) {
    				// En una aplicacon real, escribo en el log, no delego porque es error al liberar recursos
    			}
    		}



        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No se puede cargar los datos");

            alert.showAndWait();
        }
    }

    
    
    
    private void showPersonDetails(Persona person) {
        if (person != null) {
            // Fill the labels with info from the person object.
        	idLabel.setText(Integer.toString(person.getIdPersona()));
        	firstNameLabel.setText(person.getFirstName());
            lastNameLabel.setText(person.getLastName());
            streetLabel.setText(person.getStreet());
            postalCodeLabel.setText(Integer.toString(person.getPostalCode()));
            cityLabel.setText(person.getCity());
            birthdayLabel.setText(UtilesData.format(person.getNacimiento()));
           // cargarPersonas (person);
            
        } else {
            // Person is null, remove all the text.
        	idLabel.setText("");
            firstNameLabel.setText("");
            lastNameLabel.setText("");
            streetLabel.setText("");
            postalCodeLabel.setText("");
            cityLabel.setText("");
            birthdayLabel.setText("");
        }
    }
}

