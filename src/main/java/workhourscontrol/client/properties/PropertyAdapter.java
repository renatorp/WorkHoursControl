package workhourscontrol.client.properties;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

public class PropertyAdapter {

	private static Map<String, Property<?>> mapProperties = new HashMap<>();

	@SuppressWarnings("unchecked")
	public static <T extends Object> Property<T> getProperty(T object, String attribute) {
		String id = System.identityHashCode(object) + attribute;
		Property<T> property = (Property<T>) mapProperties.get(id);

		if (Objects.isNull(property)) {
			property = new SimpleObjectProperty<T>(object);
			mapProperties.put(id, property);
		}
		return property;
	}
}
