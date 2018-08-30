package org.alixia.libs.evaluator.api.types.casting;

import java.lang.reflect.Modifier;

import org.alixia.libs.evaluator.api.exceptions.UnresolvableTypeException;
import org.alixia.libs.evaluator.api.types.Data;

public interface DefaultTypeMap extends TypeMap {
	@SuppressWarnings("unchecked")
	@Override
	default Class<? extends Data<?>> get(String ref) {
		// TODO Don't ignore classes in the default package. :3
		// Ignoring classes in the default package (for now).
		String absoluteName = ref.contains(".") ? ref : Data.class.getPackage().getName() + "." + ref;
		try {
			Class<?> resolvedClass = Class.forName(absoluteName);
			if (!Data.class.isAssignableFrom(resolvedClass) || Modifier.isAbstract(resolvedClass.getModifiers())
					|| Modifier.isInterface(resolvedClass.getModifiers()))
				throw new UnresolvableTypeException("The specified class is not a viable Data type.",
						(CharSequence) ref);
			else
				return (Class<? extends Data<?>>) resolvedClass;
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	static String filter(String ref) {
		String[] pieces = ref.split("\\.");
		String name = pieces[pieces.length - 1];
		name = name.replaceAll(" ", "-");
		name = name.replaceAll("_", "-");
		while (name.contains("--"))
			name = name.replaceAll("--", "-");

		String[] words = name.split("-");
		for (int i = 0; i < words.length; i++)
			words[i] = words[i].substring(0, 1).toUpperCase() + (words[i].length() > 1 ? words[i].substring(1) : "");
		String finalizedName = "";
		for (String s : words)
			finalizedName += s;
		String absolutePath = "";
		for (int i = 0; i < pieces.length - 1; i++) {
			String s = pieces[i];
			absolutePath += s + ".";
		}
		return absolutePath + finalizedName;// We append this out here to make sure that no trailing period is appended.
	}

}