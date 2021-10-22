# bottom-up migration

## readability

* feeding.jar module does not export any package.
* care.jar regular jar (unnamed module on the classpath) depends on feeding.jar

_care_ depends on _feeding_

"Code in any type loaded from the class path will thus be able to access the **_exported_** types of all other readable modules, 
which by default will include all of the named, built-in platform modules"
[http://openjdk.java.net/projects/jigsaw/spec/sotms/#bridges-to-the-class-path](http://openjdk.java.net/projects/jigsaw/spec/sotms/#bridges-to-the-class-path)

vs

"all unnamed modules on the classpath can access all packages in the [named] module"
[https://app.efficientlearning.com/pv5/v8/5/app/ocp/javase11prgiisg.html?#](https://app.efficientlearning.com/pv5/v8/5/app/ocp/javase11prgiisg.html?#)

Conclusion: *Unnamed module (on the classpath) can only access exported packages from a named module*

## note
"if non-modular code depends on artifacts on the module path, you need to add them manually with the --add-modules option. 
Not necessarily all of them, just those that you directly depend on"
[https://stackoverflow.com/questions/46288170/is-it-possible-to-mix-class-path-and-module-path-in-javac-jdk-9](https://stackoverflow.com/questions/46288170/is-it-possible-to-mix-class-path-and-module-path-in-javac-jdk-9)

# test

## feeding does not export anything

* 1_feeding-module-build.bat
* 2_feeding-module-jar.bat
* 3_care-build.bat
* 4_care-jar.bat

* 5_care-run-from-jar.bat
```
java -p "_mods" --add-modules zoo.animal.feeding -cp "_jars/*" zoo.animal.care.Care
Exception in thread "main" java.lang.IllegalAccessError: class zoo.animal.care.Care (in unnamed module @0x73f792cf) cannot access class zoo.animal.feeding.api.Feeding (in module zoo.animal.feeding) because module zoo.animal.feeding does not export zoo.animal.feeding.api to unnamed module @0x73f792cf
        at zoo.animal.care.Care.main(Care.java:8)
```

## feeding exports to care (regular jar)

Is it possible to make it available to exactly one other specific (unnamed) "module" on the classpath?

```
module zoo.animal.feeding {
	exports zoo.animal.feeding.api to zoo.animal.care;
}
```

javac returns a warning. This is logical since care is not a module:
```
javac -p _mods -d feeding/ feeding/module-info.java feeding/zoo/animal/feeding/api/*.java feeding/zoo/animal/feeding/internal/*.java
feeding\module-info.java:2: warning: [module] module not found: zoo.animal.care
        exports zoo.animal.feeding.api to zoo.animal.care;
                                                    ^
1 warning
```
Nothing is exported.

* 5_care-run-from-jar.bat
```
java -p "_mods" --add-modules zoo.animal.feeding -cp "_jars/*" zoo.animal.care.Care
Exception in thread "main" java.lang.IllegalAccessError: class zoo.animal.care.Care (in unnamed module @0x73f792cf) cannot access class zoo.animal.feeding.api.Feeding (in module zoo.animal.feeding) because module zoo.animal.feeding does not export zoo.animal.feeding.api to unnamed module @0x73f792cf
        at zoo.animal.care.Care.main(Care.java:8)
```

## feeding exports to every module and jar

Is it possible to make it available to any unnamed module on the classpath?

```
module zoo.animal.feeding {
	exports zoo.animal.feeding.api;
}
```

* 5_care-run-from-jar.bat
```
java -p "_mods" --add-modules zoo.animal.feeding -cp "_jars/*" zoo.animal.care.Care
some food the inner feeding
```
answer is yes.