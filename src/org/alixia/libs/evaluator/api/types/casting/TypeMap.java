package org.alixia.libs.evaluator.api.types.casting;

import org.alixia.libs.evaluator.api.types.Data;

public interface TypeMap {
	/**
	 * Returns a {@link Class} representing the specified ref. The class object will
	 * be a subclass of {@link Data} or {@link Data} itself.
	 * 
	 * @param ref The text that should reference the wanted type in some way.
	 * @return This method makes its best attempt to figure out what
	 *         <code>ref</code> refers to. If this method figures out what
	 *         <code>ref</code> refers to, it will return it. Otherwise, it will
	 *         return <code>null</code>.
	 */
	Class<? extends Data<?>> get(String ref);
}
