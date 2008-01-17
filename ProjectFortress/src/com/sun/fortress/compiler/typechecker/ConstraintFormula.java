package com.sun.fortress.compiler.typechecker;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Collections;
import edu.rice.cs.plt.collect.CollectUtil;

import com.sun.fortress.nodes.Type;
import com.sun.fortress.nodes.InferenceVarType;

import com.sun.fortress.compiler.typechecker.Types.SubtypeHistory;
    
public abstract class ConstraintFormula {
    
    public static final ConstraintFormula TRUE = new ConstraintFormula() {
        public ConstraintFormula and(ConstraintFormula f, SubtypeHistory history) { return f; }
        public ConstraintFormula or(ConstraintFormula f, SubtypeHistory history) { return this; }
        public boolean isTrue() { return true; }
        public boolean isFalse() { return false; }
    };
    
    public static final ConstraintFormula FALSE = new ConstraintFormula() {
        public ConstraintFormula and(ConstraintFormula f, SubtypeHistory history) { return this; }
        public ConstraintFormula or(ConstraintFormula f, SubtypeHistory history) { return f; }
        public boolean isTrue() { return false; }
        public boolean isFalse() { return true; }
    };
    
    /** A conjunction of a number of binding constraints on inference variables.  Clients are
      * responsible for insuring that all constructed formulas are neither unsatisfiable (due to
      * conflicting bounds on a variable) nor true (due to trivial bounds).
      */
    private static class SimpleFormula extends ConstraintFormula {
        private Map<InferenceVarType, Type> _upperBounds;
        private Map<InferenceVarType, Type> _lowerBounds;
        public SimpleFormula(Map<InferenceVarType, Type> upperBounds, Map<InferenceVarType, Type> lowerBounds) {
            _upperBounds = upperBounds;
            _lowerBounds = lowerBounds;
        }
        
        public ConstraintFormula and(ConstraintFormula f, SubtypeHistory history) {
            if (f.isTrue()) { return this; }
            else if (f.isFalse()) { return f; }
            else if (f instanceof SimpleFormula) { return merge((SimpleFormula) f, history); }
            else { throw new RuntimeException("unexpected case"); }
        }
        
        public ConstraintFormula or(ConstraintFormula f, SubtypeHistory history) {
            if (f.isTrue()) { return f; }
            else if (f.isFalse()) { return this; }
            else {
                // simplification for now -- arbitrarily pick one
                return this;
            }
        }
        
        public boolean isTrue() { return false; }
        public boolean isFalse() { return false; }
        
        private ConstraintFormula merge(SimpleFormula f, SubtypeHistory history) {
            Map<InferenceVarType, Type> uppers = new HashMap<InferenceVarType, Type>();
            Map<InferenceVarType, Type> lowers = new HashMap<InferenceVarType, Type>();
            ConstraintFormula conditions = TRUE;
            
            Set<InferenceVarType> upperVars = CollectUtil.union(_upperBounds.keySet(), f._upperBounds.keySet());
            Set<InferenceVarType> lowerVars = CollectUtil.union(_lowerBounds.keySet(), f._lowerBounds.keySet());
            for (InferenceVarType t : CollectUtil.union(upperVars, lowerVars)) {
                Type upper = null;
                Type lower = null;
                if (_upperBounds.containsKey(t) && f._upperBounds.containsKey(t)) {
                    upper = history.meet(_upperBounds.get(t), f._upperBounds.get(t));
                }
                else if (_upperBounds.containsKey(t)) { upper = _upperBounds.get(t); }
                else if (f._upperBounds.containsKey(t)) { upper = f._upperBounds.get(t); }
                if (_lowerBounds.containsKey(t) && f._upperBounds.containsKey(t)) {
                    lower = history.join(_lowerBounds.get(t), f._lowerBounds.get(t));
                }
                else if (_lowerBounds.containsKey(t)) { lower = _lowerBounds.get(t); }
                else if (f._lowerBounds.containsKey(t)) { lower = f._lowerBounds.get(t); }
                
                if (_upperBounds.containsKey(t) && f._lowerBounds.containsKey(t) ||
                    _lowerBounds.containsKey(t) && f._upperBounds.containsKey(t)) {
                    conditions = conditions.and(history.subtype(lower, upper), history);
                    if (conditions.isFalse()) { return FALSE; }
                }
                if (upper != null) { uppers.put(t, upper); }
                if (lower != null) { lowers.put(t, lower); }
            }
            return new SimpleFormula(uppers, lowers).and(conditions, history);
        }
    }
    
    
    public static ConstraintFormula upperBound(InferenceVarType var, Type bound, SubtypeHistory history) {
        if (history.subtype(Types.ANY, bound).isTrue()) { return TRUE; }
        else {
            return new SimpleFormula(Collections.singletonMap(var, bound),
                                     Collections.<InferenceVarType, Type>emptyMap());
        }
    }
    
    public static ConstraintFormula lowerBound(InferenceVarType var, Type bound, SubtypeHistory history) {
        if (history.subtype(bound, Types.BOTTOM).isTrue()) { return TRUE; }
        else {
            return new SimpleFormula(Collections.<InferenceVarType, Type>emptyMap(),
                                     Collections.singletonMap(var, bound));
        }
    }
    
    public static ConstraintFormula fromBoolean(boolean b) {
        return b ? TRUE : FALSE;
    }
    
    public abstract ConstraintFormula and(ConstraintFormula c, SubtypeHistory history);  
    public abstract ConstraintFormula or(ConstraintFormula c, SubtypeHistory history);
    public abstract boolean isTrue();
    public abstract boolean isFalse();
    public boolean isSatisfiable() { return !isFalse(); }
}