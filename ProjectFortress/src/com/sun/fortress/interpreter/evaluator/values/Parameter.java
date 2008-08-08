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

package com.sun.fortress.interpreter.evaluator.values;
import com.sun.fortress.interpreter.evaluator.types.FType;

public class Parameter {
  String param_name;
  FType param_type;
  boolean is_mutable;
  boolean is_transient;

  public Parameter(String pname, FType ptype, boolean mutable,
                   boolean _transient) {
    param_name = pname;
    param_type = ptype;
    is_mutable = mutable;
    is_transient = _transient;
  }

  public String getName() { return param_name;}
  public FType  getType() { return param_type;}
  public boolean getMutable() { return is_mutable;}
  public boolean isTransient() { return is_transient;}

  public String toString() {
      return param_name+":"+param_type;
  }
  
  public boolean equals(Object o) {
      if (o instanceof Parameter) {
          Parameter p = (Parameter) o;
          if (! param_type.equals(p.param_type))
              return false;
          return is_mutable == p.is_mutable && is_transient == p.is_transient;
      }
      return false;
  }
}