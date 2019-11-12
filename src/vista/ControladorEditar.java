package vista;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import controlador.UtilesData;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import modelo.Persona;

import util.Conexion;

 
/**
 * Dialog to edit details of a person.
 * 
 * @author Marco Jakob
 */
public class ControladorEditar {
	 @FXML
	    private TextField firstNameField;
	    @FXML
	    private TextField lastNameField;
	    @FXML
	    private TextField streetField;
	    @FXML
	    private TextField postalCodeField;
	    @FXML
	    private TextField cityField;
	    @FXML
	    private TextField birthdayField;
	    
	    private static final String INSERT_PERSONA = "insert into persona (nombre, apellido, calle, ciudad, codigoPostal, nacimiento) values ( ?, ?, ?, ?, ?, ?)";
	    
	    private Stage dialogStage;
	    private Persona person;
	    private boolean okClicked = false;

	    /**
	     * Initializes the controller class. This method is automatically called
	     * after the fxml file has been loaded.
	     */
	    @FXML
	    private void initialize() {
	    }

	    /**
	     * Sets the stage of this dialog.
	     * 
	     * @param dialogStage
	     */
	    public void setDialogStage(Stage dialogStage) {
	        this.dialogStage = dialogStage;
	    }
	    
	    /**
	     * Sets the person to be edited in the dialog.
	     * 
	     * @param person
	     */
	    
		  public void nuevoUsuario (Persona person) {
		    	
				Connection con = null;
				PreparedStatement stmt = null;
				ResultSet rs = null;
				
				try {
					con = new Conexion().getConnection();

					stmt = con.prepareStatement(INSERT_PERSONA);

					stmt.setString(1, person.getFirstName());
					stmt.setString(2, person.getLastName());
					stmt.setString(3, person.getStreet());
					stmt.setString(4, person.getCity());
					stmt.setLong(5, person.getPostalCode());
					stmt.setString(6, person.getNacimiento().toString());;
					
					stmt.executeUpdate();
					
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
		    }
	    
	    
	    public void setPerson(Persona person) {
	        this.person = person;
	        
	        firstNameField.setText(person.getFirstName());
	        lastNameField.setText(person.getLastName());
	        streetField.setText(person.getStreet());
	        postalCodeField.setText(Integer.toString(person.getPostalCode()));
	        cityField.setText(person.getCity());
	        birthdayField.setText(UtilesData.format(person.getNacimiento()));
	        birthdayField.setPromptText("dd.mm.yyyy");
	        
	        
	    }

	    /**
	     * Returns true if the user clicked OK, false otherwise.
	     * 
	     * @return
	     */
	    public boolean isOkClicked() {
	        return okClicked;
	    }

	    /**
	     * Called when the user clicks ok.
	     */
		@FXML
	    private void handleOk() {
	        if (isInputValid()) {
	        	
	            person.setFirstName(firstNameField.getText());
	            person.setLastName(lastNameField.getText());
	            person.setStreet(streetField.getText());
	            person.setPostalCode(Integer.parseInt(postalCodeField.getText()));
	            person.setCity(cityField.getText());
	            person.setBirthday(UtilesData.parse(birthdayField.getText()));
	        	
	            nuevoUsuario (person);
	            
	            okClicked = true;
	            dialogStage.close();

	        }
	    }

	    /**
	     * Called when the user clicks cancel.
	     */
	    @FXML
	    private void handleCancel() {
	        dialogStage.close();
	    }

	    /**
	     * Validates the user input in the text fields.
	     * 
	     * @return true if the input is valid
	     */
	    private boolean isInputValid() {
	        String errorMessage = "";

	        if (firstNameField.getText() == null || firstNameField.getText().length() == 0) {
	            errorMessage += "Nombre no valido!\n"; 
	        }
	        if (lastNameField.getText() == null || lastNameField.getText().length() == 0) {
	            errorMessage += "Apellido no valido!\n"; 
	        }
	        if (streetField.getText() == null || streetField.getText().length() == 0) {
	            errorMessage += "Calle no valida!\n"; 
	        }

	        if (postalCodeField.getText() == null || postalCodeField.getText().length() == 0) {
	            errorMessage += "Codigo postal no valido!\n"; 
	        } else {
	            // try to parse the postal code into an int.
	            try {
	                Integer.parseInt(postalCodeField.getText());
	            } catch (NumberFormatException e) {
	                errorMessage += "Codigo postal no valido (Debe ser un numero)!\n"; 
	            }
	        }

	        if (cityField.getText() == null || cityField.getText().length() == 0) {
	            errorMessage += "Ciudad no valida!\n"; 
	        }

	        if (birthdayField.getText() == null || birthdayField.getText().length() == 0) {
	            errorMessage += "Nacimiento no valido!\n";
	        } else {
	            if (!UtilesData.validDate(birthdayField.getText())) {
	                errorMessage += "Nacimiento no valido. Use el formato dd.mm.yyyy!\n";
	            }
	        }

	        if (errorMessage.length() == 0) {
	            return true;
	        } else {
	            // Show the error message.
	            Alert alert = new Alert(AlertType.ERROR);
	            alert.initOwner(dialogStage);
	            alert.setTitle("Campos no validos");
	            alert.setHeaderText("Por favor corrija los campos incorrectos.");
	            alert.setContentText(errorMessage);
	            
	            alert.showAndWait();
	            
	            return false;
	        }
	    }
}
