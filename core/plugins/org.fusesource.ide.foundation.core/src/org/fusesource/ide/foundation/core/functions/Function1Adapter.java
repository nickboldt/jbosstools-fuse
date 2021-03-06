/*******************************************************************************
 * Copyright (c) 2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.fusesource.ide.foundation.core.functions;

public abstract class Function1Adapter<T,R> implements Function1WithReturnType<T,R> {
	private final Class<R> returnType;

	public Function1Adapter(Class<R> returnType) {
		this.returnType = returnType;
	}


	@Override
	public String toString() {
		return "function(T) : " + getReturnType().getName();
	}



	@Override
	public Class<R> getReturnType() {
		return returnType;
	}


}
