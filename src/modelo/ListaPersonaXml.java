package modelo;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Helper class to wrap a list of persons. This is used for saving the
 * list of persons to XML.
 * 
 * @author Marco Jakob
 */
@XmlRootElement(name = "personas")

public class ListaPersonaXml {

	
    private List<Persona> personas;
    @XmlElement(name = "persona")
    public List<Persona> getPersons() {
        return personas;
    }
    
	public void setPersons(List<Persona> personas) {
		this.personas = personas;
	}
	
}
