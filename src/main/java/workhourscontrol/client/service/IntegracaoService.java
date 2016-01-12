package workhourscontrol.client.service;

import java.io.File;
import java.util.List;
import java.util.Objects;

import workhourscontrol.client.ConfiguracoesAplicacao;
import workhourscontrol.client.MainApp;
import workhourscontrol.entity.RegistroHora;
import workhourscontrol.service.ControleHoras;
import workhourscontrol.service.ControleHorasHttpBuilder;
import workhourscontrol.service.ControleHorasPlanilha;
import workhourscontrol.service.ControleHorasPlanilhaBuilder;

/**
 * Serviço responsável por fazer o lançamento das horas
 */
public class IntegracaoService {

	private static IntegracaoService integracaoService;

	private ConfiguracoesAplicacao configuracaoAplicacao;

	public static IntegracaoService getInstance() {
		if (Objects.isNull(integracaoService)) {
			integracaoService = new IntegracaoService();
			integracaoService.configuracaoAplicacao = MainApp.configuracoesAplicacao;
		}
		return integracaoService;
	}

	public void sincronizarRegistrosHora(List<RegistroHora> registros) {

		ControleHorasHttpBuilder builder = new ControleHorasHttpBuilder(null);

		ControleHoras controleHoras = builder.setProxy(configuracaoAplicacao.getProxyHost(),Integer.parseInt(configuracaoAplicacao.getProxyPort()))
											 .setCredenciaisProxy(configuracaoAplicacao.getProxyUser(),configuracaoAplicacao.getProxyPassword())
											 .setCredenciaisAcessoRemoto(configuracaoAplicacao.getLoginAplicacao(), configuracaoAplicacao.getPasswordAplicacao())
											 .build();

		controleHoras.registrarHoras(registros);
		controleHoras.fecharConexao();

	}

	public void sincronizarRegistrosHoraComPlanilha(List<RegistroHora> registros, File arquivo) {
		ControleHorasPlanilhaBuilder builder2 = new ControleHorasPlanilhaBuilder(new ControleHorasPlanilha());
		builder2.setPlanilha(arquivo);
		ControleHoras controleHorasPlanilha = builder2.build();
		controleHorasPlanilha.registrarHoras(registros);
		controleHorasPlanilha.fecharConexao();
	}
}
