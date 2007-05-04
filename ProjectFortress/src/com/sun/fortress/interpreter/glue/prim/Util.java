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

package com.sun.fortress.interpreter.glue.prim;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import com.sun.fortress.interpreter.evaluator.values.FBool;
import com.sun.fortress.interpreter.evaluator.values.FFloat;
import com.sun.fortress.interpreter.evaluator.values.FInt;
import com.sun.fortress.interpreter.evaluator.values.FLong;
import com.sun.fortress.interpreter.evaluator.values.FString;
import com.sun.fortress.interpreter.evaluator.values.FBufferedReader;
import com.sun.fortress.interpreter.evaluator.values.FBufferedWriter;
import com.sun.fortress.interpreter.evaluator.values.FValue;
import com.sun.fortress.interpreter.evaluator.values.FVoid;
import com.sun.fortress.interpreter.glue.NativeFn1;
import com.sun.fortress.interpreter.glue.NativeFn2;


/**
 * Utility classes to make it easier to define many simple primitives.
 */
public class Util {
    static public abstract class Z2o extends NativeFn1 {
        protected abstract FValue f(int x);
        protected final FValue act(FValue x) {
            return f(x.getInt());
        }
    }
    static public abstract class Z2Z extends NativeFn1 {
        protected abstract int f(int x);
        protected final FValue act(FValue x) {
            return FInt.make(f(x.getInt()));
        }
    }
    static public abstract class ZZ2o extends NativeFn2 {
        protected abstract FValue f(int x, int y);
        protected final FValue act(FValue x, FValue y) {
            return f(x.getInt(),y.getInt());
        }
    }
    static public abstract class ZZ2Z extends NativeFn2 {
        protected abstract int f(int x, int y);
        protected final FValue act(FValue x, FValue y) {
            return FInt.make(f(x.getInt(),y.getInt()));
        }
    }
    static public abstract class ZL2Z extends NativeFn2 {
        protected abstract int f(int x, long y);
        protected final FValue act(FValue x, FValue y) {
            return FInt.make(f(x.getInt(),y.getLong()));
        }
    }
    static public abstract class ZZ2B extends NativeFn2 {
        protected abstract boolean f(int x, int y);
        protected final FValue act(FValue x, FValue y) {
            return FBool.make(f(x.getInt(),y.getInt()));
        }
    }
    static public abstract class Z2L extends NativeFn1 {
        protected abstract long f(int x);
        protected final FValue act(FValue x) {
            return FLong.make(f(x.getInt()));
        }
    }

    static public abstract class L2o extends NativeFn1 {
        protected abstract FValue f(long x);
        protected final FValue act(FValue x) {
            return f(x.getLong());
        }
    }
    static public abstract class L2L extends NativeFn1 {
        protected abstract long f(long x);
        protected final FValue act(FValue x) {
            return FLong.make(f(x.getLong()));
        }
    }
    static public abstract class LL2o extends NativeFn2 {
        protected abstract FValue f(long x, long y);
        protected final FValue act(FValue x, FValue y) {
            return f(x.getLong(),y.getLong());
        }
    }
    static public abstract class LL2L extends NativeFn2 {
        protected abstract long f(long x, long y);
        protected final FValue act(FValue x, FValue y) {
            return FLong.make(f(x.getLong(),y.getLong()));
        }
    }
    static public abstract class LL2B extends NativeFn2 {
        protected abstract boolean f(long x, long y);
        protected final FValue act(FValue x, FValue y) {
            return FBool.make(f(x.getLong(),y.getLong()));
        }
    }
    static public abstract class L2Z extends NativeFn1 {
        protected abstract int f(long x);
        protected final FValue act(FValue x) {
            return FInt.make(f(x.getLong()));
        }
    }

    static public abstract class R2o extends NativeFn1 {
        protected abstract FValue f(double x);
        protected final FValue act(FValue x) {
            return f(x.getFloat());
        }
    }
    static public abstract class R2R extends NativeFn1 {
        protected abstract double f(double x);
        protected final FValue act(FValue x) {
            return FFloat.make(f(x.getFloat()));
        }
    }
    static public abstract class RR2o extends NativeFn2 {
        protected abstract FValue f(double x, double y);
        protected final FValue act(FValue x, FValue y) {
            return f(x.getFloat(),y.getFloat());
        }
    }
    static public abstract class RR2R extends NativeFn2 {
        protected abstract double f(double x, double y);
        protected final FValue act(FValue x, FValue y) {
            return FFloat.make(f(x.getFloat(),y.getFloat()));
        }
    }
    static public abstract class RR2B extends NativeFn2 {
        protected abstract boolean f(double x, double y);
        protected final FValue act(FValue x, FValue y) {
            return FBool.make(f(x.getFloat(),y.getFloat()));
        }
    }
    static public abstract class R2L extends NativeFn1 {
        protected abstract long f(double x);
        protected final FValue act(FValue x) {
            return FLong.make(f(x.getFloat()));
        }
    }

    static public abstract class B2o extends NativeFn1 {
        protected abstract FValue f(boolean x);
        protected final FValue act(FValue x) {
            return f(x==FBool.TRUE);
        }
    }
    static public abstract class B2B extends NativeFn1 {
        protected abstract boolean f(boolean x);
        protected final FValue act(FValue x) {
            return FBool.make(f(x==FBool.TRUE));
        }
    }
    static public abstract class BB2o extends NativeFn2 {
        protected abstract FValue f(boolean x, boolean y);
        protected final FValue act(FValue x, FValue y) {
            return f(x==FBool.TRUE,y==FBool.TRUE);
        }
    }
    static public abstract class BB2B extends NativeFn2 {
        protected abstract boolean f(boolean x, boolean y);
        protected final FValue act(FValue x, FValue y) {
            return FBool.make(f(x==FBool.TRUE,y==FBool.TRUE));
        }
    }

    static public abstract class SS2o extends NativeFn2 {
        protected abstract FValue f(String x, String y);
        protected final FValue act(FValue x, FValue y) {
            return f(x.getString(),y.getString());
        }
    }
    static public abstract class SS2S extends NativeFn2 {
        protected abstract String f(String x, String y);
        protected final FValue act(FValue x, FValue y) {
            return FString.make(f(x.getString(),y.getString()));
        }
    }
    static public abstract class SS2B extends NativeFn2 {
        protected abstract boolean f(String x, String y);
        protected final FValue act(FValue x, FValue y) {
            return FBool.make(f(x.getString(),y.getString()));
        }
    }

    static public abstract class S2V extends NativeFn1 {
        protected abstract void f(String x);
        protected final FValue act(FValue x) {
            f(x.getString());
            return FVoid.V;
        }
    }
    static public abstract class Z2V extends NativeFn1 {
        protected abstract void f(int x);
        protected final FValue act(FValue x) {
            f(x.getInt());
            return FVoid.V;
        }
    }
    static public abstract class o2V extends NativeFn1 {
        protected abstract void f(FValue x);
        protected final FValue act(FValue x) {
            f(x);
            return FVoid.V;
        }
    }

    static public abstract class S2Fr extends NativeFn1 {
        protected abstract BufferedReader f(String x);
        protected final FValue act(FValue x) {
            return FBufferedReader.make(f(x.getString()));
        }
    }

    static public abstract class Fr2S extends NativeFn1 {
        protected abstract String f(BufferedReader x);
        protected final FValue act(FValue x) {
            return FString.make(f(x.getBufferedReader()));
        }
    }

    static public abstract class Fr2V extends NativeFn1 {
        protected abstract void f(BufferedReader x);
        protected final FValue act(FValue x) {
            f(x.getBufferedReader());
            return FVoid.V;
        }
    }

    static public abstract class Fw2V extends NativeFn1 {
        protected abstract void f(BufferedWriter x);
        protected final FValue act(FValue x) {
            f(x.getBufferedWriter());
            return FVoid.V;
        }
    }

    static public abstract class FwS2V extends NativeFn2 {
        protected abstract void f(BufferedWriter x, String y);
        protected final FValue act(FValue x, FValue y) {
            f(x.getBufferedWriter(), y.getString());
            return FVoid.V;

        }
    }

    static public abstract class S2Fw extends NativeFn1 {
        protected abstract BufferedWriter f(String x);
        protected final FValue act(FValue x) {
            return FBufferedWriter.make(f(x.getString()));
        }
    }
}
