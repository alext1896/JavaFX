package modelo;

import java.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import util.AdaptadorFecha;
 
/**
 * Model class for a Person.
 *
 * @author Marco Jakob
 */
public class Persona {
	
	private final IntegerProperty idpersona;
    private final StringProperty nombre;
    private final StringProperty apellido;
    private final StringProperty calle;
    private final IntegerProperty codigoPostal;
    private final StringProperty ciudad;
    private final ObjectProperty<LocalDate> nacimiento;
    /**
     * Default constructor.
     */
	public Persona() {
		this( null, null);
    }
    
    /**
     * Constructor with some initial data.
     * 
     * @param firstName
     * @param lastName
     */
	public Persona(String nombre, String apellido) {
		this.idpersona = new SimpleIntegerProperty ();
        this.nombre = new SimpleStringProperty(nombre);
        this.apellido = new SimpleStringProperty(apellido);
        
        // Some initial dummy data, just for convenient testing.
        this.calle = new SimpleStringProperty("Alguna calle");
        this.codigoPostal = new SimpleIntegerProperty(1234);
        this.ciudad = new SimpleStringProperty("Alguna ciudad");
        this.nacimiento = new SimpleObjectProperty<LocalDate>(LocalDate.of(1996, 12, 18));
    }
	
	public void setIdPersona (int idPersona) {
		this.idpersona.set(idPersona);
	}
	
   public int getIdPersona() {
        return idpersona.get();
    }
    
    public String getFirstName() {
        return nombre.get();
    }

    public void setFirstName(String nombre) {
        this.nombre.set(nombre);
    }
    
    public StringProperty firstNameProperty() {
        return nombre;
    }

    public String getLastName() {
        return apellido.get();
    }

    public void setLastName(String apellido) {
        this.apellido.set(apellido);
    }
    
    public StringProperty lastNameProperty() {
        return apellido;
    }

    public String getStreet() {
        return calle.get();
    }

    public void setStreet(String calle) {
        this.calle.set(calle);
    }
    
    public StringProperty streetProperty() {
        return calle;
    }

    public int getPostalCode() {
        return codigoPostal.get();
    }

    public void setPostalCode(int codigoPostal) {
        this.codigoPostal.set(codigoPostal);
    }
    
    public IntegerProperty postalCodeProperty() {
        return codigoPostal;
    }

    public String getCity() {
        return ciudad.get();
    }

    public void setCity(String calle) {
        this.ciudad.set(calle);
    }
    
    public StringProperty cityProperty() {
        return ciudad;
    }
    
    @XmlJavaTypeAdapter(AdaptadorFecha.class)
    public LocalDate getNacimiento() {
        return nacimiento.get();
    }

    public void setBirthday(LocalDate nacimiento) {
        this.nacimiento.set(nacimiento);
    }
    
    public ObjectProperty<LocalDate> birthdayProperty() {
        return nacimiento;
    }
}