package ca.gc.triagency.datastore.model;

import org.springframework.context.i18n.LocaleContextHolder;

public interface LocalizedParametersModel {
	public default String getLocalizedAttribute(String attributeName) {
		String retval = null;
		String langAddOn = null;
		if (LocaleContextHolder.getLocale().toString().contains("en")) {
			langAddOn = "En";
		} else {
			langAddOn = "Fr";
		}

		try {
			retval = (String) Agency.class.getDeclaredField(attributeName + langAddOn).get(this);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retval;
	}

}
