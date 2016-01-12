package workhourscontrol.client.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Wrapper para permitir persistir lista de registroHora em arquivo xml.
 * @author renatorp
 */
@XmlRootElement(name = "registros")
public class ListaRegistroHoraWrapper {

	private List<RegistroHoraObservable> listaRegistroHora;

	public ListaRegistroHoraWrapper() {}

	public ListaRegistroHoraWrapper(List<RegistroHoraObservable> listaRegistroHora) {
		this.listaRegistroHora = listaRegistroHora;
	}

	@XmlElement(name = "registro")
	public List<RegistroHoraObservable> getListaRegistroHora() {
		return listaRegistroHora;
	}

	public void setListaRegistroHora(List<RegistroHoraObservable> listaRegistroHora) {
		this.listaRegistroHora = listaRegistroHora;
	}

}
