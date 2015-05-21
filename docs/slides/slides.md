# Scala?

- [@eparejatobes](https://github.com/eparejatobes)
- [@laughedelic](https://github.com/laughedelic)

----

#### Session structure

1. Generalities blahblahblah
2. show me teh codez!

----

# Scala

## Infrastructure

- **JVM**-based (GC, optimizations, ...)
- **Java** interoperability
- (can be) as fast as **Java**, sometimes more!

----

## Language design

- (kinda) contains Java
- contains a functional language
- strict evaluation

----

### wait, _how?_

well, it's complicated...

----

### Java compat

> Scala = Java - ";"

Not that is a good idea, but...

----

### Java compat II

All the huge Java library ecosystem directly available!

----

### Functional programming in Scala

- functions
- immutable values
- (primitive) recursion support
- method ⟷ function

----

### Functions

``` scala
val f: String => Int = x => x.length
val g: Int => Int = _+1
(f andThen g)("hola")
```

----

### Immutable values

``` scala
val z = 3
val u = "hola"
val zu = z + u.size
// nonono
zu = zu + 3
```

----

### Recursion support

``` scala
import scala.annotation.tailrec
@tailrec def factorial_rec(x: Int, y: Int): Int =  if (x == 1) y else factorial_rec(x - 1, x * y)
def factorial(x: Int): Int = factorial_rec(x,1)
```

----

### Methods ⟷ functions

``` scala
val f: Int => Int = factorial
def factorialAgain(x: Int) = f(x)
```

----

### Functional programming in Scala II

- pattern matching
- type abstraction (traits)
- objects (modules)
- parametric polymorphism (type constructors)
- type classes (through implicits)
- type-dependent types (type members)
- GADTs (sealed traits + pattern matching)

----

### Pattern matching

``` scala
def length[X](l: List[X]): Int = length_rec(l,0)

@tailrec def length_rec[X](l: List[X], acc: Int): Int = l match {

  case Nil      => acc
  case x :: xs  => length_rec(l, 1 + acc)
}
```
<!-- and much more. Mention extractors, but also buggy implementation, type refinement not working, etc -->
----

### Pattern matching II

``` scala
object even {

  def unapply(z: Int) = z % 2 == 0
}

def halve(x: Int) = x match {

  case even() => Some( x / 2 )
  case _      => None
}
```

<!-- Uses: match only on part of a datatype, model unsafe (runtime) dependent types -->

----

### Traits

API + (optional) code = trait

``` scala
trait Equals[X] {

  def eq(x: X, other: X): Boolean
  def notEq(x: X, other: X): Boolean = ! eq(x, other)
}
```

<!-- say something about multiple inheritance etc etc -->
----

### Objects

``` scala
object DefaultIntEquals extends Equals[Int] {

  // default eq
  def eq(x: Int, other: Int): Boolean = x == y
}
```


### Type-dependent types

``` scala
trait AnyProperty {

  type Value
  val value: Value
}

def get[P <: AnyProperty](p: P): P#Value = p.value
```

----

### Parametric polymorphism

``` scala
def id[A]: A => A = a: A => a

def compose[A,B,C](f: A => B, g: B => C): A => C = a => g(f(a))
```

----

### GADTs

<!-- TODO -->


### Implicits

aka compile-time code rewriting API. Powerful and dangerous!

``` scala
implicit def IntToString(x: Int) = x.toString
val x: String = 2
```

### Implicits II

Uses:

- type classes and extensible syntax
- type dependent pattern matching

----

### Type classes through implicits

Traits for defining them

``` scala
trait AnyMonoid {

  type M

  def combine(x1: X, x2: X): X
  def unit: X
}
```

----

### Type classes through implicits II

Define syntax

``` scala
case class MonoidSyntax[X, Monoid <: AnyMonoid { type M = X }](val x: X) {

  def ⋅(other: X)(implicit monoid: Monoid): X = monoid.combine(x,other)
}
```

### Type classes through implicits III

implicits for providing syntax

``` scala
implicit def monoidSyntax[X, Monoid <: AnyMonoid { type M = X }](x: X)(implicit m: Monoid): MonoidSyntax[X,M] =
  MonoidSyntax(x)
```

### Type classes through implicits IV

use it!

``` scala
val z = x ⋅ y
```

### Type system summary

- Incredibly complex
- powerful, the compiler is crap though
- nice ideas, spoilt by Java compat requirements

----

#### Single most useful feature

**Type members and refinements!!**

and **implicits**! yeah that's two

----

### Where Scala shines: DSLs

- implicits
- overloading
- minimal set of reserved keywords

**Example** [BASIC in Scala](https://github.com/fogus/baysick)

----

### Ecosystem

- [Spark](https://spark.apache.org/)
- [Akka](http://akka.io/)
- Twitter stuff: [Finagle](https://twitter.github.io/finagle/), [Algebird](https://github.com/twitter/algebird), [util](https://github.com/twitter/util), [summingbird](https://github.com/twitter/summingbird)
- https://github.com/non/spire

### Our (positive) experience

1. JVM, Java compat: incredibly useful
2. really cool things are possible, but really hard. But possible!
3. writing reasonably fast easy to understand code for normal tasks
4. extensible build tool: [sbt](http://www.scala-sbt.org/)

### Our (negative) experience

1. Scala contains *several* languages, fighting against each other; stick with one of them
2. The compiler (type checker mostly) is crap
3. Community: A lot of people saying a lot of incompatible things (see 1.)
4. don't use macros. Please.
