To assemble a jar file for your project, run the "jar" gradle task, either through IntelliJ or by doing
`gradle jar` on a terminal. Gradle will automatically download all dependencies needed to compile your jar file,
which will be stored in the build/libs folder.

Make sure to edit the main class attribute the build.gradle file, you'll need to change it in order to obtain
a working jar file.

# Requirements
## Java Development Kit (JDK) version 17.x
This starter code was developed using JDK17 which can be acquired at this [link](https://www.oracle.com/java/technologies/downloads/#java17 "JDK Download")
<br>Your submitted programs are expected to be written using this JDK and should not use other JDKs unless otherwise specified
## MaterialFX
MaterialFX is a JavaFX (JFX) module library for pre-made components. 
<br>The MaterialFX Github page can be found [here](https://github.com/palexdev/MaterialFX "MaterialFX GitHub") and the corresponding Wiki with component examples can be found [here](https://github.com/palexdev/MaterialFX/wiki "MaterialFX GitHub Wiki") 
<br>The library is a great <i>starting point</i> for UI design during the early iterations.



# Important Files
## build.gradle / gradle.properties
This is the gradle configuration file. Modify this file to add dependencies to your project. In
 general you should only modify the depedencies section of this file, however there are a few
  modification you will need to make when you begin the project
  
  - `mainClassName`
    - Modifiy this variable to point to your main class. By default it is `edu.wpi.punchy_pegasi.frontend.Main
    `, but once you update your teamname package to your team letter you will need to update this
     path.
     
  - jaCoCo
    - jaCoCo is a JAva COde COverage checker that enforces testing. By default the rules are 25
    % line coverage and 25% branch coverage, but if you would like to be more successful you
     should raise these numbers higher to enforce team members to write more tests. Simply modify
      the `minimum` values to enforce stricter tests (but do not change to below .25, as that is
       the required minimum for this class)
  - spotless
    - spotless is a style guider checker/formatter that will automatically detect if your code
     adheres to an agreed style guide. For this starter code I have defaulted to Google's style
     guide, as it is well known and well liked. You can find more documentation for spotless 
     [here](https://github.com/diffplug/spotless). If you would like to disable the spotless
      checks, comment out the spotless plugin as well as the spotless configuration at the bottom
       of the file

## .travis.yml
This is the Travis-CI configuration file. **Only modify this file if you are sure you know what you are doing**

## lombox.config
This is the configuration for [Lombok](https://projectlombok.org/), a very useful java library
 that makes 'enterprise' coding a breeze. **Only modify this file if you are sure you know what you are doing**

## .gitignore
This file tells git which files to ignore in the repo. It should be correctly configured already
**Only modify this file if you are sure you know what you are doing**

## .hooks/
This directory contains a useful git hook that will force your teammates to run tests before
pushing to github. This `pre-push` hook will run gradle tests to make sure code passes.

To install these hooks, simply run `git config core.hookspath .hooks` from the root directory

## config/
Config contains styleguide config files both for checkstyle (another optional plugin for gradle
) and for intellij. Teams should determine a style guide to follow to make 
although it is not a requirement for this class.

To install the styleguide scheme into IntelliJ, `Preferences -> Editor -> Code Style -> Scheme
 -> ... -> Import Scheme -> IntelliJ IDEA code style XML`, then select `config/intellij-java
 -google-style.xml` from the project's root directory 
