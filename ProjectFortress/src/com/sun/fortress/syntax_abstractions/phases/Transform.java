/*******************************************************************************
    Copyright 2008 Sun Microsystems, Inc.,
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

package com.sun.fortress.syntax_abstractions.phases;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import com.sun.fortress.compiler.GlobalEnvironment;
import com.sun.fortress.compiler.index.ApiIndex;

import com.sun.fortress.nodes.NodeDepthFirstVisitor_void;
import com.sun.fortress.nodes.Node;
import com.sun.fortress.nodes.Id;
import com.sun.fortress.nodes.TransformerNode;
import com.sun.fortress.nodes.TemplateGap;
import com.sun.fortress.nodes._SyntaxTransformation;
import com.sun.fortress.nodes.TemplateUpdateVisitor;
import com.sun.fortress.nodes.TemplateNodeDepthFirstVisitor_void;
import com.sun.fortress.useful.Debug;
// import com.sun.fortress.tools.FortressAstToConcrete;

public class Transform extends TemplateUpdateVisitor {
    private Map<String,Node> transformers;
    private Map<String,Object> variables;

    private Transform( Map<String,Node> transformers, Map<String,Object> variables ){
        this.transformers = transformers;
        this.variables = variables;
    }
    
    public static Node transform( GlobalEnvironment env, Node node ){
        return node.accept( new Transform( populateTransformers(env), new HashMap<String,Object>() ) );
    }

    private static Map<String,Node> populateTransformers( GlobalEnvironment env ){
        final Map<String,Node> map = new HashMap<String,Node>();
        for ( ApiIndex api : env.apis().values() ){
            api.ast().accept( new NodeDepthFirstVisitor_void(){
                @Override public void forTransformerNode(TransformerNode that) {
                    //Debug.debug(Debug.Type.SYNTAX, 3,
                    //            "transformer " + that.getTransformer() + " =\n" + 
                    //            FortressAstToConcrete.astToString(that.getNode()));
                    map.put( that.getTransformer(), that.getNode() );
                }
            });
        }
        return map;
    }

    private Node lookupTransformer( String name ){
        if ( transformers.get( name ) == null ){
            throw new RuntimeException( "Cannot find transformer for " + name );
        }
        return transformers.get( name );
    }

    @Override public Node forTemplateGapOnly(TemplateGap that, Id gapId_result, List<Id> templateParams_result) {
        String variable = gapId_result.getText();
        Object binding = variables.get(variable);
        if ( binding == null ){
            throw new RuntimeException( "Can't find a binding for gap " + gapId_result );
        } else {
            return (Node)binding;
        }
    }

    @Override public Node defaultTransformationNodeCase(_SyntaxTransformation that) {
        /*
           Debug.debug( Debug.Type.SYNTAX, 1, "Run transformation on " + that + " is " + that.getSyntaxTransformer().invoke() );
           return that.getSyntaxTransformer().invoke().accept( this );
           */
        Debug.debug( Debug.Type.SYNTAX, 1, "Run transformation " + that.getSyntaxTransformer() );
        Node transformer = lookupTransformer( that.getSyntaxTransformer() );
        //Debug.debug( Debug.Type.SYNTAX, 1, 
        //             "Transformation is " + FortressAstToConcrete.astToString(transformer));
        Map<String,Object> arguments = that.getVariables();
        Map<String,Object> evaluated = new HashMap<String,Object>();
        for ( Map.Entry<String,Object> var : arguments.entrySet() ){
            String varName = var.getKey();
            Node argument = ((Node)var.getValue()).accept(this);
            checkFullyTransformed(argument);
            evaluated.put(varName, argument);
            Debug.debug( Debug.Type.SYNTAX, 3, " argument " + varName + " is " + argument);
        }

        Debug.debug( Debug.Type.SYNTAX, "Invoking transformer " + that.getSyntaxTransformer() );
        Node transformed = transformer.accept( new Transform(this.transformers, evaluated) );
        checkFullyTransformed(transformed);
        return transformed;
        // run this recursively??
        // return that.invoke().accept( this );
    }

    private void checkFullyTransformed(Node n) {
        n.accept(new TemplateNodeDepthFirstVisitor_void() {
                @Override public void forTemplateGapOnly(TemplateGap that) {
                    throw new RuntimeException("Transformation left over template gap: " + that);
                }
                @Override public void for_SyntaxTransformationOnly(_SyntaxTransformation that) {
                    throw new RuntimeException("Transformation left over transformation application: "
                                               + that);
                }
            });
    }

}
