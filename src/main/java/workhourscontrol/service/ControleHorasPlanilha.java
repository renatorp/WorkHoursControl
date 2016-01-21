package workhourscontrol.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import workhourscontrol.entity.RegistroHora;

public class ControleHorasPlanilha implements ControleHoras {

	private Logger logger = Logger.getLogger(ControleHorasPlanilha.class);

	private static final String SHEET_NAME = "PONTO";
	private static final Integer ROW_INDEX_START = 15;
	private static final Integer COL_HORA_INICIO_MANHA = 6;
	private static final Integer COL_HORA_FIM_MANHA = 7;
	private static final Integer COL_HORA_INICIO_TARDE = 8;
	private static final Integer COL_HORA_FIM_TARDE = 9;

	private ParametrosControleHorasPlanilha parametros;
	private XSSFWorkbook wb;


	public void setParametros(ParametrosControleHorasPlanilha parametros) {
		this.parametros = parametros;
	}

	@Override
	public void registrarHoras(List<RegistroHora> registros) {
		criarWorkbook();
		Map<String, List<RegistroHora>> mapRegistrosPorDia = registros.stream().collect(Collectors.groupingBy(r -> r.getDia()));
		registrarHoraDia(mapRegistrosPorDia);
	}

	private void criarWorkbook() {
			try {
				if (Objects.isNull(wb)) {
					InputStream inp = new FileInputStream(parametros.getFile());
					wb = (XSSFWorkbook)WorkbookFactory.create(inp);
				}
			} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
				logger.error("Ocorreu um erro ao criar workbook", e);
				throw new RuntimeException(e);
			}
	}

	private void registrarHoraDia(Map<String, List<RegistroHora>> mapRegistrosPorDia) {
		for (String dia : mapRegistrosPorDia.keySet()) {
			List<RegistroHora> registrosDia = mapRegistrosPorDia.get(dia);

			registrarHoraManhaPlanilha(dia, registrosDia.get(0).getHoraInicio(), registrosDia.get(0).getHoraFim());

			if (registrosDia.size() > 1) {
				registrarHoraTardePlanilha(dia, registrosDia.get(1).getHoraInicio(), registrosDia.get(1).getHoraFim());
			}
		}
	}

	private void registrarHoraTardePlanilha(String dia, String horaInicio, String horaFim) {
		Row row = getRowByDia(dia);
		setarValorCelula(horaInicio, row, COL_HORA_INICIO_TARDE);
		setarValorCelula(horaFim, row, COL_HORA_FIM_TARDE);
	}

	private void setarValorCelula(String horaInicio, Row row, int posicao) {
		Cell cell = row.getCell(posicao);
    	cell.setCellType(Cell.CELL_TYPE_STRING);
    	cell.setCellValue(horaInicio);
	}

	private Row getRowByDia(String dia) {
		Sheet sheet = wb.getSheet(SHEET_NAME);
		return sheet.getRow(ROW_INDEX_START -1 + Integer.parseInt(dia));
	}

	private void registrarHoraManhaPlanilha(String dia, String horaInicio, String horaFim) {
		Row row = getRowByDia(dia);
		setarValorCelula(horaInicio, row, COL_HORA_INICIO_MANHA);
		setarValorCelula(horaFim, row, COL_HORA_FIM_MANHA);
	}

	@Override
	public void fecharConexao() {

		try {

			XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
			FileOutputStream fileOut = new FileOutputStream(parametros.getFile());
			wb.write(fileOut);
			fileOut.close();
			wb.close();
		} catch (IOException e) {
			logger.error("Ocorreu um erro ao fechar conexão com planilha.", e);
			throw new RuntimeException(e);
		}

	}

	@Override
	public double obterSaldoHoras() {
		// TODO Não implementado
		return 0;
	}

}
