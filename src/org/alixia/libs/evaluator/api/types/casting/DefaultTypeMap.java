package org.alixia.libs.evaluator.api.types.casting;

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
			if (!Data.class.isAssignableFrom(resolvedClass))
				throw new RuntimeException("The specified class is not a viable Data type.");
			else
				return (Class<? extends Data<?>>) resolvedClass;
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
}
