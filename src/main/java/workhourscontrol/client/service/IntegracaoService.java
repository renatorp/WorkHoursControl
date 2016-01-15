package workhourscontrol.client.service;

import java.io.File;
import java.util.List;
import java.util.Objects;

import org.apache.log4j.Logger;

import workhourscontrol.client.ConfiguracoesAplicacao;
import workhourscontrol.client.MainApp;
import workhourscontrol.entity.RegistroHora;
import workhourscontrol.service.ControleHoras;
import workhourscontrol.service.ControleHorasHttp;
import workhourscontrol.service.ControleHorasHttpBuilder;
import workhourscontrol.service.ControleHorasPlanilha;
import workhourscontrol.service.ControleHorasPlanilhaBuilder;

/**
 * Serviço responsável por fazer o lançamento das horas
 */
public class IntegracaoService {

	private static Logger logger = Logger.getLogger(IntegracaoService.class);

	private static IntegracaoService integracaoService;

	private ConfiguracoesAplicacao configuracaoAplicacao;

	/** Singleton */
	public static IntegracaoService getInstance() {
		if (Objects.isNull(integracaoService)) {
			integracaoService = new IntegracaoService();
			integracaoService.configuracaoAplicacao = MainApp.configuracoesAplicacao;
		}
		return integracaoService;
	}

	/**
	 * Aponta horas em sistema de acordo com implementação de ControleHorasHttp
	 * @param registros
	 */
	public void sincronizarRegistrosHora(List<RegistroHora> registros) {

		ControleHorasHttpBuilder builder = new ControleHorasHttpBuilder(getControleHorasImpl());

		ControleHoras controleHoras = builder.setProxy(configuracaoAplicacao.getProxyHost(),Integer.parseInt(configuracaoAplicacao.getProxyPort()))
											 .setCredenciaisProxy(configuracaoAplicacao.getProxyUser(),configuracaoAplicacao.getProxyPassword())
											 .setCredenciaisAcessoRemoto(configuracaoAplicacao.getLoginAplicacao(), configuracaoAplicacao.getPasswordAplicacao())
											 .build();

		controleHoras.registrarHoras(registros);
		controleHoras.fecharConexao();

	}

	/**
	 * Aponta horas em planilha de acordo com implementação de ControleHoraPlanilha
	 */
	public void sincronizarRegistrosHoraComPlanilha(List<RegistroHora> registros, File arquivo) {
		ControleHorasPlanilhaBuilder builder = new ControleHorasPlanilhaBuilder(new ControleHorasPlanilha());
		builder.setPlanilha(arquivo);
		ControleHoras controleHorasPlanilha = builder.build();
		controleHorasPlanilha.registrarHoras(registros);
		controleHorasPlanilha.fecharConexao();
	}


	/**
	 * Instancia implementação de Controle Horas
	 * @return
	 */
	private ControleHorasHttp getControleHorasImpl() {
		final String className = configuracaoAplicacao.getControleHorasClass();
		try {
			return (ControleHorasHttp) Class.forName(className).newInstance();

		} catch (InstantiationException | IllegalAccessException e) {
			logger.error("Ocorreu um erro ao instanciar " + className, e);
			throw new RuntimeException(e);

		} catch (ClassNotFoundException e) {
			logger.error("Classe " + className + " não encontrada.", e);
			throw new RuntimeException(e);
		}
	}
}
