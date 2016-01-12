package workhourscontrol.client.controller;

import java.text.ParseException;
import java.time.LocalDate;

import org.apache.commons.lang3.StringUtils;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import workhourscontrol.client.MainApp;
import workhourscontrol.client.model.RegistroHoraObservable;
import workhourscontrol.client.util.DateUtils;

public class EditHourController {

	private Stage stage;

	private RegistroHoraObservable registroHora;

	@FXML private DatePicker campoData;
	@FXML private TextField campoHoraInicio;
	@FXML private TextField campoHoraFim;
	@FXML private TextArea campoObs;
	@FXML private TextField campoIssue;
	@FXML private CheckBox campoSync;

	@FXML private Button btnSalvar;

	private WorkHoursManagerController controllerPai;

	private Runnable onSave = () -> {};

	public void setDialogStage(Stage dialogStage) {
		this.stage = dialogStage;
	}

	public void setRegistroHora(RegistroHoraObservable registroHora) {

		if (registroHora != null) {

			try {
				this.registroHora = registroHora;

				campoData.setValue(DateUtils.parseData(registroHora.getDia(), registroHora.getMes(), registroHora.getAno()));
				campoHoraInicio.setText(registroHora.getHoraInicio());
				campoHoraFim.setText(registroHora.getHoraFim());
				campoObs.setText(registroHora.getObservacao());
				campoIssue.setText(registroHora.getIssue());
				campoSync.setSelected(registroHora.isLancado());

			} catch (ParseException e) {
				throw new RuntimeException(e);
			}

		} else {

			// Se houver uma issue default, já a insere no campo
			String issueDefault = MainApp.configuracoesAplicacao.getIssueDefault();
			if (StringUtils.isNotBlank(issueDefault)) {
				campoIssue.setText(issueDefault);
			}

			// Inicializa data com data atual
			campoData.setValue(LocalDate.now());
		}

	}

	@FXML
	public void handleSalvar() {

		if (isNovoRegistro()) {
			preencherRegistro();
			controllerPai.adicionarRegistro(registroHora);
		} else {
			preencherRegistro();
		}

		this.onSave.run();

		stage.close();
	}


	private void preencherRegistro() {
		if (registroHora == null) {
			registroHora = new RegistroHoraObservable();
		}

		//Somene registros novos
		LocalDate data = campoData.getValue();

		registroHora.setDia(DateUtils.getDiaAsString(data));
		registroHora.setMes(DateUtils.getMesAsString(data));
		registroHora.setAno(DateUtils.getAnoAsString(data));
		registroHora.setObservacao(campoObs.getText());
		registroHora.setHoraInicio(campoHoraInicio.getText());
		registroHora.setHoraFim(campoHoraFim.getText());
		registroHora.setIssue(campoIssue.getText());
		registroHora.setLancado(campoSync.isSelected());
	}

	@FXML
	public void initialize() {
		campoData.setEditable(false);
	}

	public boolean isNovoRegistro() {
		return registroHora == null;
	}

	public void setControllerPai(WorkHoursManagerController controllerPai) {
		this.controllerPai = controllerPai;
	}

	public void setOnSave(Runnable acao) {
		this.onSave  = acao;
	}
}
