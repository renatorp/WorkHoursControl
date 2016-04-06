package workhourscontrol.client.service;

import java.io.File;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import workhourscontrol.client.ConfiguracoesAplicacao;
import workhourscontrol.client.MainApp;
import workhourscontrol.entity.RegistroHora;
import workhourscontrol.exception.ControleHorasException;
import workhourscontrol.service.ControleHoras;
import workhourscontrol.service.ControleHorasHttp;
import workhourscontrol.service.ControleHorasHttpBuilder;
import workhourscontrol.service.ControleHorasPlanilha;
import workhourscontrol.service.ControleHorasPlanilhaBuilder;
import workhourscontrol.strategy.MesclagemHorariosStrategy;

/**
 * Serviï¿½o responsï¿½vel por fazer o lanï¿½amento das horas
 */
public class IntegracaoService {

	private static Logger logger = Logger.getLogger(IntegracaoService.class);

	private static IntegracaoService integracaoService;

	private ConfiguracoesAplicacao configuracaoAplicacao;

	private ControleHoras controleHorasHttp;

	/** Singleton */
	public static IntegracaoService getInstance() {
		if (Objects.isNull(integracaoService)) {
			integracaoService = new IntegracaoService();
			integracaoService.configuracaoAplicacao = MainApp.configuracoesAplicacao;
			integracaoService.buildControleHorasHttp();
		}
		return integracaoService;
	}

	private void buildControleHorasHttp() {

		try {

			if (Objects.isNull(controleHorasHttp)) {

				ControleHorasHttpBuilder builder = new ControleHorasHttpBuilder(getControleHorasImpl());

				controleHorasHttp = builder.setProxy(configuracaoAplicacao.getProxyHost(),Integer.parseInt(configuracaoAplicacao.getProxyPort()))
						.setCredenciaisProxy(configuracaoAplicacao.getProxyUser(),configuracaoAplicacao.getProxyPassword())
						.setCredenciaisAcessoRemoto(configuracaoAplicacao.getLoginAplicacao(), configuracaoAplicacao.getPasswordAplicacao())
						.addAjusteHorasStrategy(new MesclagemHorariosStrategy())
						.build();
			}
		} catch(Exception e) {
			logger.warn("Ocorreu um erro ao construir componente para integraï¿½ï¿½o, a integraï¿½ï¿½o nï¿½o serï¿½ realizada", e);
		}

	}

	/**
	 * Aponta horas em sistema de acordo com implementaï¿½ï¿½o de ControleHorasHttp
	 * @param registros
	 */
	public void sincronizarRegistrosHora(List<RegistroHora> registros) {
		try {
			controleHorasHttp.registrarHoras(registros);
		} catch (ControleHorasException e) {
			logger.error("Erro ao sincronizar registros.", e);
		} finally {
			controleHorasHttp.fecharConexao();
		}

	}

	public Double obterSaldoHoras() {
		try {
			double result = controleHorasHttp.obterSaldoHoras();
			return result;
		} catch (ControleHorasException e) {
			logger.error("Erro ao obter saldo de horas.", e);
		} finally {
			controleHorasHttp.fecharConexao();
		}
		return null;
	}


	/**
	 * Aponta horas em planilha de acordo com implementaï¿½ï¿½o de ControleHoraPlanilha
	 */
	public void sincronizarRegistrosHoraComPlanilha(List<RegistroHora> registros, File arquivo) {
		try {
			ControleHorasPlanilhaBuilder builder = new ControleHorasPlanilhaBuilder(new ControleHorasPlanilha());
			builder.setPlanilha(arquivo);
			ControleHoras controleHorasPlanilha = builder.build();
			controleHorasPlanilha.registrarHoras(registros);
			controleHorasPlanilha.fecharConexao();
		} catch (ControleHorasException e) {
			logger.error("Erro ao sincronizar registros com planilha.", e);
		}
	}


	/**
	 * Instancia implementaï¿½ï¿½o de Controle Horas
	 * @return
	 * @throws Exception
	 */
	private ControleHorasHttp getControleHorasImpl() throws Exception {
		final String className = configuracaoAplicacao.getControleHorasClass();
		try {
			if (StringUtils.isBlank(className)) {
				final String msg = "Propriedade controleHorasClass não encontrada";
				logger.warn(msg);
				throw new Exception(msg);
			}

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
