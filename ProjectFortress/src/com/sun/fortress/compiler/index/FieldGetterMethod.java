/*******************************************************************************
 Copyright 2009 Sun Microsystems, Inc.,
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

package com.sun.fortress.compiler.index;

import com.sun.fortress.compiler.typechecker.StaticTypeReplacer;
import com.sun.fortress.compiler.typechecker.TypesUtil;
import com.sun.fortress.nodes.*;
import com.sun.fortress.nodes_util.NodeUtil;
import com.sun.fortress.nodes_util.Span;
import com.sun.fortress.nodes_util.NodeFactory;
import edu.rice.cs.plt.lambda.SimpleBox;
import edu.rice.cs.plt.lambda.Thunk;
import edu.rice.cs.plt.tuple.Option;

import java.util.Collections;
import java.util.List;

public class FieldGetterMethod extends Method {

    private final Binding _ast;
    private final Id _declaringTrait;
    private final Option<Type> _selfType;

    public FieldGetterMethod(Binding ast, TraitObjectDecl traitDecl) {
        _ast = ast;
        _declaringTrait = NodeUtil.getName(traitDecl);
        _selfType = traitDecl.getSelfType();

        if (_ast.getIdType().isSome()) _thunk = Option.<Thunk<Option<Type>>>some(SimpleBox.make(_ast.getIdType()));
    }

    /**
     * Copy another FieldGetterMethod, performing a substitution with the visitor.
     */
    public FieldGetterMethod(FieldGetterMethod that, NodeUpdateVisitor visitor) {
        _ast = (Binding) that._ast.accept(visitor);
        _declaringTrait = that._declaringTrait;
        _selfType = visitor.recurOnOptionOfType(that._selfType);

        _thunk = that._thunk;
        _thunkVisitors = that._thunkVisitors;
        pushVisitor(visitor);
    }

    public Binding ast() {
        return _ast;
    }

    @Override
    public Span getSpan() {
        return NodeUtil.getSpan(_ast);
    }

    @Override
    public Option<Expr> body() {
        return Option.none();
    }

    @Override
    public List<Param> parameters() {
        return Collections.emptyList();
    }

    @Override
    public List<StaticParam> staticParameters() {
        return Collections.emptyList();
    }

    @Override
    public List<BaseType> thrownTypes() {
        return Collections.emptyList();
    }

    @Override
    public Method instantiate(List<StaticParam> params, List<StaticArg> args) {
        StaticTypeReplacer replacer = new StaticTypeReplacer(params, args);
        return new FieldGetterMethod(this, replacer);
    }

    public Id declaringTrait() {
        return this._declaringTrait;
    }

    public Option<Type> selfType() {
        return _selfType;
    }

    @Override
    public Functional acceptNodeUpdateVisitor(NodeUpdateVisitor visitor) {
        return new FieldGetterMethod(this, visitor);
    }

    @Override
    public Id name() {
        return _ast.getName();
    }


}
