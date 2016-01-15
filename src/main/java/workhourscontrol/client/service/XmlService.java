package workhourscontrol.client.service;

import java.io.File;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import workhourscontrol.client.model.ListaRegistroHoraWrapper;
import workhourscontrol.client.model.RegistroHoraObservable;

/**
 * Serviço para lidar com arquivos xml
 * @author renatorp
 *
 */
public class XmlService {

	private static XmlService xmlService;

	public static XmlService getInstance() {
		if (Objects.isNull(xmlService)) {
			xmlService = new XmlService();
		}
		return xmlService;
	}

	public void salvarRegistroHoraXml(File arquivo, List<RegistroHoraObservable> registrosHora) throws Exception {
		//Preenchendo wrapper
		ListaRegistroHoraWrapper wrapper = new ListaRegistroHoraWrapper();
		wrapper.setListaRegistroHora(registrosHora);

		salvarXml(arquivo, wrapper, ListaRegistroHoraWrapper.class);
	}

	public <E> void salvarXml(File arquivo, E objeto, Class<E> clazz) throws Exception {
		try {
			JAXBContext context = JAXBContext.newInstance(clazz);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			//Processando e salvando xml no arquivo
			m.marshal(objeto, arquivo);
		} catch (Exception e) {
			throw new Exception("Ocorreu um erro ao salvar XML", e);
		}
	}

	@SuppressWarnings("unchecked")
	public <E> E carregarXml(File arquivo, Class<E> clazz) throws Exception {
		try {

			JAXBContext context = JAXBContext.newInstance(clazz);
			Unmarshaller un = context.createUnmarshaller();

			//Lendo do xml
			return (E)un.unmarshal(arquivo);

		} catch (Exception e) {
			throw new Exception("Ocorreu um erro ao carregar XML", e);
		}
	}

	public List<RegistroHoraObservable> carregarRegistroHoraXml(File arquivo) throws Exception {
		ListaRegistroHoraWrapper wrapper = carregarXml(arquivo, ListaRegistroHoraWrapper.class);
		return wrapper.getListaRegistroHora();
	}

}
