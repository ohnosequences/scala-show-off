package ohnosequences.scala

object slides {
  
  // use this for checking whether the compiler can optimize a tail-recursive method or not
  import scala.annotation.tailrec

  def length[X](l: List[X]): Int = length_rec(l,0)

  @tailrec def length_rec[X](l: List[X], acc: Int): Int = l match { 

    case Nil      => acc
    case x :: xs  => length_rec(xs, 1 + acc) 
  }

  trait AnyProperty {

    type Value
    val value: Value
  }

  def get[P <: AnyProperty](p: P): P#Value = p.value

  def compose[A,B,C](f: A => B, g: B => C): A => C = a => g(f(a))

  /*
  #### defining patterns: extractors

  The name comes from being a kind of dual to constructors. Destructors would be a better (though non-standard) name.
  */
  object even {

    def unapply(z: Int) = z % 2 == 0
  }

  def halve(x: Int): Option[Int] = x match {

    case even() => Some( x / 2 )
    case _      => None
  }


  /*
  ### (G)ADTs

  This is a bit more involved
  */

  sealed trait AnyTerm {

    type V
    val v: V
  }
  sealed trait Term[v] extends AnyTerm { type V = v }

  case class Number(val v: Int) extends Term[Int]
  case class Pair[T1 <: AnyTerm, T2 <: AnyTerm](t1: T1, t2: T2) extends Term[(T1#V,T2#V)] {

    val v = (t1.v, t2.v)
  }
  
  trait Eval[T <: AnyTerm] {

    def apply(t: T): T#V
  }

  implicit def eval_Number: Eval[Number] = new Eval[Number] { 

    def apply(t: Number) = t.v 
  }

  implicit def eval_Pair[T1 <: AnyTerm, T2 <: AnyTerm](implicit 
    evt1: Eval[T1],
    evt2: Eval[T2]
  ): Eval[Pair[T1,T2]] = new Eval[Pair[T1,T2]] { 

    def apply(t: Pair[T1,T2]) = (evt1(t.t1), evt2(t.t2)) 
  }

  implicit def getEvalOps[T <: AnyTerm](t: T)(implicit ev: Eval[T]): evalOps[T] = evalOps(t)
  case class evalOps[T <: AnyTerm](t: T) extends AnyVal {

    def eval(implicit ev: Eval[T]): T#V = ev(t) 
  }




  /*
    ### Type classes and syntax

    The classical monoid example.
  */
  trait AnyMonoid {

    type M

    def combine(x1: M, x2: M): M
    def unit: M
  }

  case class MonoidSyntax[X, Monoid <: AnyMonoid { type M = X }](val x: X, val monoid: Monoid) {
  
    def ⋅(other: X): X = monoid.combine(x,other)
  }

  implicit def monoidSyntax[X, Monoid <: AnyMonoid { type M = X }](x: X)(implicit m: Monoid)
    : MonoidSyntax[X,Monoid] = 
      MonoidSyntax(x,m)

  implicit object IntMult extends AnyMonoid {

    type M = Int

    def combine(x1: M, x2: M): M = x1 * x2
    def unit: M = 1
  }

  val z = 2 ⋅ 3
}