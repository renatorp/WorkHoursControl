package workhourscontrol.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import workhourscontrol.client.util.DateUtils;
import workhourscontrol.entity.RegistroHora;

public abstract class ControleHorasHttp implements ControleHoras {

	private ParametrosControleHorasHttp parametros;
	private boolean loggedIn = false;
	private CloseableHttpClient httpClient;
	private RequestConfig requestGlobalConfig;
	private CookieStore cookieStore;

	public void setParametros(ParametrosControleHorasHttp parametros) {
		this.parametros = parametros;
	}

	private CloseableHttpClient gerarHttpClient() {

		if (httpClient == null) {

			HttpClientBuilder builder = HttpClients.custom();

			if (parametros.isUsarProxy()) {

				//Definindo rota de acesso ao host
				DefaultProxyRoutePlanner routePlanner = criarRoutePlanner();
				builder.setRoutePlanner(routePlanner);

				//Definindo autentiação para acesso ao host
				CredentialsProvider credsProvider = criarCredenciais();
				builder.setDefaultCredentialsProvider(credsProvider);

			}

			this.httpClient = builder.build();
		}

		return httpClient;

	}

	private void login() {

		try {
			httpClient = gerarHttpClient();

			List<NameValuePair> loginParameters = obterParametrosLogin();

			HttpPost post = montarHttpPost(loginParameters, getUrlLogin());

			HttpClientContext localContext = createLocalContext();

			HttpResponse r = httpClient.execute(post, localContext);
			System.out.println(r.getStatusLine().getStatusCode());
			System.out.println(EntityUtils.toString(r.getEntity()));

		} catch (IOException e) {
			throw new RuntimeException("Ocorreu um erro ao efetuar login", e);
		}
	}

	private HttpPost montarHttpPost(List<NameValuePair> parameters, String url) {
		try {

			HttpPost post = new HttpPost(url);

			post.setEntity(new UrlEncodedFormEntity(parameters));

			post.addHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (X11; Linux x86_64; rv:12.0) Gecko/20100101 Firefox/21.0");

			post.setConfig(getRequestGlobalConfig());

			return post;

		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Ocorreu um erro ao gerar HttpPost", e);
		}


	}

	private RequestConfig getRequestGlobalConfig() {
		if (this.requestGlobalConfig == null) {
			this.requestGlobalConfig = RequestConfig.custom().build();
		}
		return this.requestGlobalConfig;
	}

	private HttpClientContext createLocalContext() {

		//Create local HTTP context
		HttpClientContext localContext = HttpClientContext.create();

		if (this.cookieStore == null) {
			this.cookieStore = new BasicCookieStore();
		}

		//Bind custom cookie store to the local context
		localContext.setCookieStore(cookieStore);

		return localContext;
	}


	protected final void registrarHora(RegistroHora registro) {

		try {
			HttpPost post = montarHttpPost(montarParametrosRegistroHora(registro), getUrlRegistroHora());

			HttpClientContext localContext = createLocalContext();

			post.addHeader("Cookie", getCookieHeader(cookieStore));

			httpClient.execute(post, localContext);

			registro.setLancado(true);

		} catch (IOException | ParseException e) {
			throw new RuntimeException("Ocorreu ao registrar hora", e);
		}
	}

	abstract String getCookieHeader(CookieStore cookieStore);

	protected List<NameValuePair> getParametrosAdicionaisRegistroHora() {
		return Collections.emptyList();
	}

	abstract String getFormatoParametroData();

	abstract String getObservacoes();
	abstract String getNomeParametroHoraFim();
	abstract String getNomeParametroHoraInicio();
	abstract String getNomeParametroData();
	abstract String getNomeParametroIssue();

	abstract String getUrlRegistroHora();

	@Override
	public void registrarHoras(List<RegistroHora> registros) {
		logarUsuario();
		registrarHorasIndividualmente(registros);
	}

	private void registrarHorasIndividualmente(List<RegistroHora> registros) {
		for (RegistroHora registroHora : registros) {
			registrarHora(registroHora);
		}
	}

	abstract String getUrlLogin();

	private List<NameValuePair> obterParametrosLogin() {
		List<NameValuePair> loginParameters = new ArrayList<NameValuePair>();

        loginParameters.add(new BasicNameValuePair(getNomeParametroLogin(), parametros.getUser()));
        loginParameters.add(new BasicNameValuePair(getNomeParametroSenha(), parametros.getPassword()));
        loginParameters.addAll(getParametrosAdicionaisLogin());

        return loginParameters;
	}

	protected List<NameValuePair> montarParametrosRegistroHora(RegistroHora registroHora) throws ParseException {
		List<NameValuePair> loginParameters = new ArrayList<NameValuePair>();

		loginParameters.add(new BasicNameValuePair(getNomeParametroData(), DateUtils.formatarData(registroHora.getDia(),registroHora.getMes(),registroHora.getAno(), getFormatoParametroData())));
		loginParameters.add(new BasicNameValuePair(getNomeParametroIssue(), registroHora.getIssue()));
		loginParameters.add(new BasicNameValuePair(getNomeParametroHoraInicio(), registroHora.getHoraInicio()));
		loginParameters.add(new BasicNameValuePair(getNomeParametroHoraFim(), registroHora.getHoraFim()));
		loginParameters.add(new BasicNameValuePair(getObservacoes(), registroHora.getObservacao()));
		loginParameters.addAll(getParametrosAdicionaisRegistroHora());

        return loginParameters;
	}

	protected List<NameValuePair> getParametrosAdicionaisLogin() {
		return Collections.emptyList();
	}

	abstract String getNomeParametroSenha();

	abstract String getNomeParametroLogin();

	private void logarUsuario() {
		if (!loggedIn) {
			login();
			this.loggedIn = true;
		}
	}

	private DefaultProxyRoutePlanner criarRoutePlanner() {
		HttpHost proxy = new HttpHost(parametros.getProxyHost(), parametros.getProxyPort());
		DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
		return routePlanner;
	}

	private CredentialsProvider criarCredenciais() {
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
        		 AuthScope.ANY,
        		 new UsernamePasswordCredentials(parametros.getProxyUser(), parametros.getProxyPassword())
        );
		return credsProvider;
	}

	@Override
	public void fecharConexao() {
		try {
			if (httpClient != null) {
				httpClient.close();
			}
		} catch (IOException e) {
			throw new RuntimeException("Ocorreu ao fechar httpClient", e);
		}

	}

}
