package workhourscontrol.util;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import workhourscontrol.client.util.DateUtils;
import workhourscontrol.entity.RegistroHora;

public class RegistroHoraUtil {

	public static Map<LocalDate, List<RegistroHora>> agruparRegistrosPorData(List<RegistroHora> registros) {
		Map<LocalDate, List<RegistroHora>> collect = registros.stream()
				.collect(Collectors.groupingBy(
					new Function<RegistroHora, LocalDate>() {

						@Override
						public LocalDate apply(RegistroHora r) {
							try {
								return DateUtils.parseData(r.getDia(), r.getMes(), r.getAno());
							} catch (Exception e) {
								throw new RuntimeException(e);
							}
						}

					}

		));
		return collect;
	}

}
