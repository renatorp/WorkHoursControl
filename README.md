# WorkHoursControl
Sistema para facilitar lançamento de horas trabalhadas.

- Deve haver um arquivo propriedades.xml no diretório da aplicação, ou é possível informar a localização do arquivo como parâmetro da jvm
java -jar -DarquivoPropriedades=C:\local\propriedades.xml WorkHoursControl.jar

- Deve ser feita uma implementação da classe ControleHorasHttp e seu "fully qualified name"(pacote + nome da classe) deve ser indicado no arquivo de propriedades 

- Ao informar a propriedade urlPlanilha, ao clicar em salvar planilha, sempre será salvo na mesma.

- Para gerar executável, executar a seguinte task jfx:native do maven.

- É obrigatório informar no arquivo xml de propriedades a localização da implementação da classe que faz a integração via http. Mas se loginAplicacao não for informado, a integração é desabilitada.

- O projeto [work-hours-control-integration](https://github.com/renatorp/work-hours-control-integration) é uma dependência dessa aplicação.

### Guia

```
git clone https://github.com/renatorp/work-hours-control-integration.git
cd work-hours-control-integration
mvn install -Dmaven.repo.local=$HOME/.m3/repository --settings $HOME/Documentos/dev/sw/maven/apache-maven-3.6.2/conf/settings.xml
```

```
git clone https://github.com/renatorp/WorkHoursControl.git
cd WorkHoursControl
mvn install -Dmaven.repo.local=$HOME/.m3/repository --settings $HOME/Documentos/dev/sw/maven/apache-maven-3.6.2/conf/settings.xml
mvn jfx:native -Dmaven.repo.local=$HOME/.m3/repository --settings $HOME/Documentos/dev/sw/maven/apache-maven-3.6.2/conf/settings.xml
```

```
WHC_DIR=~/app/destination/dir
mkdir -p $WHC_DIR
cp -R ./target/jfx/app $WHC_DIR/
cp propriedades.xml $WHC_DIR
cd $WHC_DIR
## customizar properties.xml
java -jar app/workhourscontrol-1.3.2-jfx.jar 
```

