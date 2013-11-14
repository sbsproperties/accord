Overview
========

Accord is a validation library written in and for Scala. Compared to [JSR 303](http://jcp.org/en/jsr/detail?id=303) and [Scalaz validation](https://github.com/scalaz/scalaz/blob/scalaz-seven/core/src/main/scala/scalaz/Validation.scala) it aims to provide the following:

* __Composable__: Because JSR 303 is annotation based, validation rules cannot be composed (annotations cannot receive other annotations as parameters). This is a real problem with some Scala features, for example `Option`s or collections. Accord's validation rules are trivially composable.
* __Simple__: Accord provides a dead-simple story for validation rule definition by leveraging macros, as well as the validation call site (see example below).
* __Self-contained__: Accord is macro-based but completely self-contained, and consequently only relies on the Scala runtime and reflection libraries.
* __Integrated__: Other than providing its own DSL and matcher library, Accord is intended to play well with [Hamcrest matchers](https://github.com/hamcrest/JavaHamcrest), and fully integrate with [Specs<sup>2</sup>](http://etorreborre.github.io/specs2/) and [ScalaTest](http://www.scalatest.org/).

Accord is work-in-progress and distributed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0), which basically means you can use and modify it freely. Feedback, bug reports and improvements are welcome!


Getting Started
===============

Accord is in the process of being published to the Maven central repository. In the meantime, you can simply clone the repository and build it yourself:

```
arilou:demo tomer$ git clone git@github.com:holograph/accord.git
Cloning into 'accord'...
remote: Counting objects: 101, done.
remote: Compressing objects: 100% (46/46), done.
remote: Total 101 (delta 23), reused 89 (delta 13)
Receiving objects: 100% (101/101), 17.50 KiB | 0 bytes/s, done.
Resolving deltas: 100% (23/23), done.
Checking connectivity... done

arilou:demo tomer$ cd accord
arilou:accord tomer$ sbt package
[info] Loading global plugins from /Users/tomer/.sbt/0.13/plugins
[info] Set current project to accord (in build file:/private/tmp/demo/accord/)
[info] Updating {file:/private/tmp/demo/accord/}accord...
[info] Resolving org.fusesource.jansi#jansi;1.4 ...
[info] Done updating.
[info] Compiling 6 Scala sources to /private/tmp/demo/accord/target/scala-2.10/classes...
[warn] there were 4 feature warning(s); re-run with -feature for details
[warn] one warning found
[info] Packaging /private/tmp/demo/accord/target/scala-2.10/accord_2.10-0.1.jar ...
[info] Done packaging.
[success] Total time: 11 s, completed Nov 14, 2013 3:08:41 PM
```

Example
=======

Importing the library for use:

```scala
import com.tomergabel.accord._
```

Defining a validator:

```scala
import dsl._    // Import the validator DSL

case class Person( firstName: String, lastName: String )
case class Classroom( teacher: Person, students: Seq[ Person ] )

implicit val personValidator = validator[ Person ] { p =>
  p.firstName is notEmpty
  p.lastName is notEmpty
}

implicit val classValidator = validator[ Classroom ] { c =>
  c.teacher is valid        // Implicitly relies on personValidator!
  c.students.each is valid
  c.students have size > 0
}
```


Running a validator:

```
scala> val validPerson = Person( "Wernher", "von Braun" )
validPerson: Person = Person(Wernher,von Braun)

scala> validate( validPerson )
res1: com.tomergabel.accord.Result = Success

scala> val invalidPerson = Person( "No Last Name", "" )
invalidPerson: Person = Person(No Last Name,)

scala> validate( invalidPerson )
res3: com.tomergabel.accord.Result = Failure(List(Violation(lastName must not be empty,)))

scala> val invalidClassroom = Classroom( Person( "Alfred", "Aho" ), Seq.empty )
invalidClassroom: Classroom = Classroom(Person(Alfred,Aho),List())

scala> validate( invalidClassroom )
res0: com.tomergabel.accord.Result = Failure(List(Violation(students has size 0, expected more than 0,List())))
```
