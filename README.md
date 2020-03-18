# Rizumu

The official repo for the Rizumu game project, programmed in [Java](https://go.java/), using [Gradle](https://gradle.org/) for dependencies!

## Current State
* Requires Java 11+ to run. (built in J14 [18/03/2020], but has J11 source compatibility if gradle did its thing!)
* It is possible to create a functioning jar file from the repo and run it from the command line, e.g. `java -jar Rizumu.jar`
* Program folder is created on startup, located `/user/Documents/Rizumu` which contains a generated configuration file and tracks folder.

Key: 
- *Scene*: The abstraction of a component, e.g. SelectScene, PlayScene, SplashScene. (Commonly referred to as `Screen`)
- *Track*: An object that represents a playable song, containing a number of beatmaps and data about the track's audio file, background image, etc.
- *Beatmap*: An object which defines where each note is placed in both time and position *(which key corresponds to which note)* 
- *Note*: The representation of a hittable object within a beatmap.

~ Album of below screenshots - https://imgur.com/a/YPqSpOP

## Splash Scene
![Imgur](https://i.imgur.com/rTJ1uwy.png)

#### Login Menu
![Imgur](https://i.imgur.com/lXUYo8g.png)

#### Settings Menu
![Imgur](https://i.imgur.com/R7IfA3m.png)

## Select Scene
![Imgur](https://i.imgur.com/2XMBJFH.png)

## Play Scene
![Imgur](https://i.imgur.com/L43k9H1.png)

#### Pause Menu
![Imgur](https://i.imgur.com/HB0cBJh.png)

#### Results Menu/Screen
![Imgur](https://i.imgur.com/l5tZG3d.png)

    