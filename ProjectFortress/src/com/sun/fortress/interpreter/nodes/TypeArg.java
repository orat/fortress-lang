/*******************************************************************************
    Copyright 2007 Sun Microsystems, Inc.,
    4150 Network Circle, Santa Clara, California 95054, U.S.A.
    All rights reserved.

    U.S. Government Rights - Commercial software.
    Government users are subject to the Sun Microsystems, Inc. standard
    license agreement and applicable provisions of the FAR and its supplements.

    Use is subject to license terms.

    This distribution may include materials developed by third parties.

    Sun, Sun Microsystems, the Sun logo and Java are trademarks or registered
    trademarks of Sun Microsystems, Inc. in the U.S. and other countries.
 ******************************************************************************/

package com.sun.fortress.interpreter.nodes;

import com.sun.fortress.interpreter.nodes_util.*;

public class TypeArg extends StaticArg {
    TypeRef type;

    public TypeArg(Span span, TypeRef type) {
        super(span);
        this.type = type;
    }

    @Override
    public <T> T accept(NodeVisitor<T> v) {
        return v.forTypeArg(this);
    }

    TypeArg(Span span) {
        super(span);
    }

    /**
     * @return Returns the name.
     */
    public TypeRef getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.valueOf(getType());
    }

    public static TypeArg make(String string) {
        return make(new Span(), string);
    }

    public static TypeArg make(Span s, String string) {
        TypeArg tra = new TypeArg(s);
        tra.type = new IdType(new Span(), NodeFactory.makeDottedId(new Span(), string));
        return tra;
    }

}
