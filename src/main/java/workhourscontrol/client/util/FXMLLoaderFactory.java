package workhourscontrol.client.util;

import javafx.fxml.FXMLLoader;
import workhourscontrol.client.MainApp;

public class FXMLLoaderFactory {

	public static FXMLLoader createLoader(String resourceUrl) {
		return createLoader(resourceUrl, MainApp.class);
	}

	public static <E> FXMLLoader createLoader(String resourceUrl, Class<E> context) {
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(context.getResource(resourceUrl));
        return loader;
	}
}
