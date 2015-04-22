Clojure memoize task
====================

**Task**
* [link a](https://gist.github.com/serzh/59fe8814b504a6382ff7)

**There are two solutions exist:**
* /src/main/clojure/mem1.clj
* /src/main/clojure/mem2.clj


**To run solution do the following:**
* Install java (7 or upper)
* Install Maven (3.2.5 or upper)
* Start REPL: mvn clean clojure:repl or just run ./start-repl.sh in the terminal
* load one of two scripts in REPL: (load "/src/main/clojure/mem1") or (load "/src/main/clojure/mem2")
* Run function "mem" with different arguments and see how function is memoized.