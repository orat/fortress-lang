(*******************************************************************************
    Copyright 2009, Oracle and/or its affiliates.
    All rights reserved.


    Use is subject to license terms.

    This distribution may include materials developed by third parties.

 ******************************************************************************)

api Compiled2.e
import Compiled2.TestTypes.{...}

trait A extends {Foo,B} end
trait B extends C end
trait C extends {D,A} end
trait D extends {A,B} end

end
