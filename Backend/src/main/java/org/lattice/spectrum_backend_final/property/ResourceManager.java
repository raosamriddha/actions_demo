package org.lattice.spectrum_backend_final.property;

import java.util.ResourceBundle;

/**
 * The Class used to load the application property file.
 * 
 * @author Pinak
 */
public final class ResourceManager {

	private ResourceManager() {
	}

	/** The Constant APPLICATION_PROPERTY_LOCATION. */
	private static final String APPLICATION_PROPERTY_LOCATION = "org.lattice.spectrum_backend_final.property.application";

	/** The Constant applicationBundle. */
	private static final ResourceBundle applicationBundle;

	static {
		applicationBundle = ResourceBundle.getBundle(APPLICATION_PROPERTY_LOCATION);
	}

	/**
	 * Gets the property value from the application.properties file.
	 *
	 * @param key
	 * @return the property value
	 */
	public static final String getProperty(final String key) {
		return applicationBundle.getString(key);
	}

}
