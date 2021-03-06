/*******************************************************************************
 * Copyright (c) 2014 Lars Pfannenschmidt and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Lars Pfannenschmidt - initial API and implementation, ongoing development and documentation
 *******************************************************************************/

package com.swookiee.runtime.ewok.representation;

import java.util.Dictionary;

public class BundleHeaderRepresentation {

    private Dictionary<String, String> headers;

    public BundleHeaderRepresentation() {
    }

    public BundleHeaderRepresentation(final Dictionary<String, String> headers) {
        this.headers = headers;
    }

    public Dictionary<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(final Dictionary<String, String> headers) {
        this.headers = headers;
    }

}
