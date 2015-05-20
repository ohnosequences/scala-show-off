# Scala?

- [@eparejatobes](https://github.com/eparejatobes)
- [@laughedelic](https://github.com/laughedelic)

## Session structure

1. Generalities blahblahblah
2. show me teh codez!

## Infrastructure

- **JVM**-based (GC, optimizations, ...)
- **Java** interoperability
- (can be) as fast as **Java**, sometimes more!

## Design

- (kinda) contains Java
- functional features
- strict eval by default
- funny type system

### wait, how?

is complicated...

### Java compat

> Scala = Java - ";"

Not that is a good idea, but...

<!-- TODO use libs, syntax, same features -->

### Functional programming in Scala

- functions
- immutable values
- (primitive) recursion support
- method <--> function
- type abstraction
- type classes

### Functions

``` scala
val f: String => Int = x => x.length
val g: Int => Int = _+1
(f andThen g)("hola")
```

### Immutable values

``` scala
val z = 3
val u = "hola"
val zu = z + u.size
// nonono
zu = zu + 3
```

### Recursion support

``` scala
import scala.annotation.tailrec
@tailrec def factorial_rec(x: Int, y: Int): Int =  if (x == 1) y else factorial_rec(x - 1, x * y)
def factorial(x: Int): Int = factorial_rec(x,1)
```

### Methods <--> functions

``` scala
val f: Int => Int = factorial
def factorialAgain(x: Int) = f(x)
```

### Type abstraction

``` scala
type L[Z] = List[Z]
trait Function {

  type Domain
  type Codomain
  def apply(x: Domain): Codomain
}
```

### Type abstraction II

``` scala
class list[X]
trait A
trait C[Z <: A]
```

### Interfaces

+ code = **traits**

### Type system

- Incredibly complex
- powerful, the compiler is crap though
- nice ideas, spoilt by Java compat requirements

<!-- TODO: funny type-level stuff goes here -->

#### Single most useful feature

**Type members!!**

and implicits! yeah that's two

### DSLs

- implicits
- overloading
- minimal set of reserved keywords

<!-- TODO explain, why Scala helps, examples? -->

- https://github.com/fogus/baysick !!!

### Ecosystem

- Spark
- Akka ??
- Twitter stuff (Finagle and the like)
- https://github.com/non/spire
