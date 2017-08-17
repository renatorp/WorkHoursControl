# WorkHoursControl
Sistema para facilitar lançamento de horas trabalhadas.

- Deve haver um arquivo propriedades.xml no diretório da aplicação, ou é possível informar a localização do arquivo como parâmetro da jvm
java -jar -DarquivoPropriedades=C:\local\propriedades.xml WorkHoursControl.jar

- Deve ser feita uma implementação da classe ControleHorasHttp e seu "fully qualified name"(pacote + nome da classe) deve ser indicado no arquivo de propriedades 

- Ao informar a propriedade urlPlanilha, ao clicar em salvar planilha, sempre será salvo na mesma.

- Para gerar executável, executar a seguinte task jfx:native do maven.

- É obrigatório informar no arquivo xml de propriedades a localização da implementação da classe que faz a integração via http.

- O projeto [work-hours-control-integration](https://github.com/renatorp/work-hours-control-integration) é uma dependência dessa aplicação.

