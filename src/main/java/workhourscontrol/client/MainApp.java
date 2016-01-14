package workhourscontrol.client;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import org.apache.log4j.Logger;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import workhourscontrol.client.controller.RootLayoutController;
import workhourscontrol.client.controller.WorkHoursManagerController;
import workhourscontrol.client.model.RegistroHoraObservable;
import workhourscontrol.client.service.XmlService;
import workhourscontrol.client.util.PreferencesHelper;

public class MainApp extends Application{
	private Logger logger = Logger.getLogger(MainApp.class);

	public static final String NOME_PREFERENCIA_XML = "xmlPath";
	private static final String PROPERTY_FILE = "propriedades.xml";

	public static ConfiguracoesAplicacao configuracoesAplicacao;

	private Stage primaryStage;
    private BorderPane rootLayout;
	private ObservableList<RegistroHoraObservable> registrosHora;
	private File arquivoAberto;


	private XmlService xmlService;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Work Hours Control");

        registrosHora = FXCollections.observableArrayList();

        initServices();
        initConfig();
        initRootLayout();
        initWorkHoursManager();

    }

	private void initConfig() {
		try {
			File arquivoPropriedades = new File(PROPERTY_FILE);
			configuracoesAplicacao = xmlService.carregarXml(arquivoPropriedades, ConfiguracoesAplicacao.class);
		} catch(Exception e) {
			logger.error("Ocorreu um erro ao carregar arquivo xml de configura��es", e);
			throw new RuntimeException(e);
		}
	}

	private void initServices() {
		this.xmlService = XmlService.getInstance();
	}

	/**
     * Inicializa o root layout e tenta carregar o �ltimo arquivo
     * de pessoa aberto.
     */
    public void initRootLayout() {
        try {
            // Carrega o root layout do arquivo fxml.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class
                    .getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Adiciona referência à MainApp
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            // Mostra a scene (cena) contendo o root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initWorkHoursManager() {
    	  try {
              // Carrega a tela principal do gerenciamento de horas.
              FXMLLoader loader = new FXMLLoader();
              loader.setLocation(MainApp.class.getResource("view/WorkHoursManager.fxml"));
              AnchorPane workHoursManager = (AnchorPane) loader.load();

              // Adiciona referência à MainApp
              WorkHoursManagerController controller = loader.getController();
              controller.setMainApp(this);

              // Define o person overview dentro do root layout.
              rootLayout.setCenter(workHoursManager);

              // Se algum arquivo foi aberto recentemente, carrega o mesmo
              carregarXmlPreferencias();

          } catch (IOException e) {
              e.printStackTrace();
          }

    }

    /**
     * Carrega ultimo xml aberto, se houver
     */
	private void carregarXmlPreferencias() {
		arquivoAberto = PreferencesHelper.getEnderecoArquivo(NOME_PREFERENCIA_XML);
		  if (Objects.nonNull(arquivoAberto)) {
			  carregarRegistrosDoArquivo(arquivoAberto);
		  }
	}

    public ObservableList<RegistroHoraObservable> getRegistrosHora() {
    	return this.registrosHora;
    }



    public Stage getPrimaryStage() {
		return primaryStage;
	}

	/**
     * Salva lista de registros em arquivo xml
     */
    public void salvarRegistrosNoArquivo(File file) {
    	try {
			xmlService.salvarRegistroHoraXml(file, registrosHora);
		} catch (Exception e) {
			logger.error("Ocorreu um erro ao salvar arquivo xml", e);
			throw new RuntimeException(e);
		}
    }

    /**
     * Carrega registros do arquivo xml
     */
    public void carregarRegistrosDoArquivo(File file) {
    	try {
			registrosHora.addAll(xmlService.carregarRegistroHoraXml(file));
		} catch (Exception e) {
			logger.error("Ocorreu um erro ao carregar arquivo xml", e);
			throw new RuntimeException(e);
		}
    }

	public void limparRegistros() {
		this.registrosHora.clear();
	}

	public File getArquivoAberto() {
		return arquivoAberto;
	}

	public void setArquivoAberto(File arquivoAberto) {
		this.arquivoAberto = arquivoAberto;
		PreferencesHelper.setPersonFilePath(MainApp.NOME_PREFERENCIA_XML, arquivoAberto);
	}

	public File getDiretorioArquivoAberto() {
		if (Objects.nonNull(arquivoAberto)) {
			return arquivoAberto.getParentFile();
		}
		return null;
	}

}
