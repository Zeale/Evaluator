package org.alixia.libs.evaluator.api.types.casting;

import java.util.ArrayList;
import java.util.List;

import org.alixia.libs.evaluator.api.types.Data;

public class SimpleTypeMap implements DefaultTypeMap {

	private final List<Type> types = new ArrayList<>();

	public final class Type {
		public final Class<? extends Data<?>> type;
		public final String matching;

		public boolean match(String ref) {
			return ref.equalsIgnoreCase(matching);
		}

		public Type(Class<? extends Data<?>> type, String matching) {
			this.type = type;
			this.matching = matching;
		}

		{
			types.add(this);
		}

	}

	@Override
	public Class<? extends Data<?>> get(String ref) {
		for (Type t : types)
			if (t.match(ref))
				return t.type;
		return DefaultTypeMap.super.get(ref);
	}
}
