# bottom-up migration

## readability

feeding.jar module does not export any package.

care.jar regular jar (unnamed module on the classpath) depends on feeding.jar

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