# Rizumu

This repo is the official repo for the Rizumu game project, programmed in [Java](https://go.java/), using [Gradle](https://gradle.org/) for dependencies!

## Current State
* Requires Java 11+ to run. (built in j13, but has j11 source compatibility if gradle did its thing!)
* It is possible to create a functioning jar file from the repo and run it from the command line, e.g. `java -jar Rizumu.jar`
* Program folder is created on startup, located `/user/Documents/Rizumu` which contains a generated configuration file and tracks folder.

Key: 
- *Scene*: The abstraction of a component, e.g. SelectScene, PlayScene, SplashScene. (Commonly referred to as `Screen`)
- *Track*: An object that represents a playable song, containing a number of beatmaps and data about the tracks audio file, background image, etc.
- *Beatmap*: An object which defines where each note is placed in both time and position *(which key corresponds to which note)* 
- *Note*: The representation of a hittable object within a beatmap.

## Splash Scene
![Imgur](https://i.imgur.com/HfaWJiF.png)

#### Login Menu
![Imgur](https://i.imgur.com/gNDKc0t.png)

#### Settings Menu
![Imgur](https://i.imgur.com/ezjk289.png)

## Select Scene
![Imgur](https://i.imgur.com/Uj3Wdq2.png)

## Play Scene
![Imgur](https://i.imgur.com/bMIzFUE.png)

#### Pause Menu
![Imgur](https://i.imgur.com/Ah8Eacg.png)

#### Results Menu/Screen
(wip)
![Imgur](https://i.imgur.com/V6jXFIn.png)

    