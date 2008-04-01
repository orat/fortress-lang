(*******************************************************************************
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
 ******************************************************************************)

api List

(** Array lists, immutable style (not the mutable Java \texttt{ArrayList} style).

    A %List% is an immutable segment of an immutable (really
    write-once) array.  The rest of the array may contain elements of
    lists which overlap this list, or may be free for future use.
    Every %List% includes two internal flags %canExtendLeft% and
    %canExtendRight%; if a flag is true we are permitted to add
    additional elements to the %List% in place by initializing
    additional elements of the array.  At most one instance sharing
    the same backing array will obtain permission to extend the array
    in this way; we atomically check and update the flag to guarantee
    this.  Having obtained permission to extend the list, that
    permission may be extended to future attempts to extend.

    Eventually the backing array fills and we must allocate a new
    backing array to accept new elements.  At the moment, we are not
    particularly careful to avoid stealing permission to extend for
    overflowing %append% operations.

    Note that because of this implementation, a %List% can be
    efficiently extended on either side, but only in a non-persistent
    way; if a single list is extended by two different calls to
    %addRight% or %append% then one of them must pay the cost of
    copying the list elements.

    Note also that the implementation has not yet been carefully
    checked for amortization, so it is quite likely there are a number
    of asymptotic infelicities.

    Finally, note that this is an efficient \emph{amortized} structure.  An
    individual operation may be quite slow due to copying work.

    Baking these off vs %PureList%s (which have good persistent behavior
    and non-amortized worst case behavior), they look very good in
    practice.
 **)
(******************** *)
(** Lists of some item type.  Used to collect elements of unknown type
    into a list whose element type is as specific as possible.  This
    should not be necessary in the presence of true type
    inference. **)
trait SomeList excludes { Number, HasRank }
        (** \vspace{-4ex} Not yet: ``%comprises List[\E\] where [\E\]%'' *)
    append(f:SomeList): SomeList
    addLeft(e:Any): SomeList
    addRight(e:Any): SomeList
end

(** %List%.  We return a %Generator% for non-list-specific operations
    for which reuse of the %Generator% will not increase asymptotic
    complexity, but return a %List% in cases (such as %map% and
    %filter%) where it will.  *)
trait List[\E\] extends { Equality[\E\], ZeroIndexed[\E\] }
        excludes { Number, HasRank }
  getter left():Maybe[\E\]
  getter right():Maybe[\E\]
  getter extractLeft(): Maybe[\(E,List[\E\])\]
  getter extractRight(): Maybe[\(List[\E\],E)\]
  append(f:List[\E\]): List[\E\]
  addLeft(e:E):List[\E\]
  addRight(e:E):List[\E\]
  take(n:ZZ32): List[\E\]
  drop(n:ZZ32): List[\E\]
  split(n:ZZ32): (List[\E\], List[\E\])
  split(): (List[\E\], List[\E\])
  reverse(): List[\E\]
  zip[\F\](other: List[\F\]): Generator[\(E,F)\]
  filter(p: E -> Boolean): List[\E\]
  toString():String
  concatMap[\G\](f: E->List[\G\]): List[\G\]
end

(** Vararg factory for lists; provides aggregate list constants: *)
opr <|[\E\] xs: E... |>: List[\E\]
(** List comprehensions: *)
opr BIG <|[\T,U\] g: ( Reduction[\SomeList\], T->SomeList) -> SomeList |>: List[\U\]

(** Convert generator into list (simpler type than comprehension above): *)
list[\E\](g:Generator[\E\]):List[\E\]

(** Flatten a list of lists *)
concat[\E\](x:List[\List[\E\]\]):List[\E\]

emptyList[\E\](): List[\E\]

(** %emptyList[\E\](n)% allocates an empty list that can accept %n%
    %addRight% operations without reallocating the underlying storage. **)
emptyList[\E\](n:ZZ32): List[\E\]

singleton[\E\](e:E): List[\E\]

(** A reduction object for concatenating lists. *)
object Concat[\E\] extends Reduction[\ List[\E\] \]
  empty(): List[\E\]
  join(a:List[\E\], b:List[\E\]): List[\E\]
end

(** Covariant Singleton function, for use with %CVConcat%: **)
cvSingleton(e:Any): SomeList

(** A reduction object for concatenating %SomeList%s covariantly. *)
object CVConcat extends Reduction[\SomeList\]
  empty(): SomeList
  join(a:SomeList, b:SomeList): SomeList
end

end