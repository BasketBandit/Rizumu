# Rizumu Java Prototype

This repo is a first prototype for Rizumu using the Java programming language.

## Current State
* This repo isn't fit to run from an artifact unless you hard-code a track object to be able to be read by the TrackReader class.

Key: 
- *Scene*: The abstraction of a component, e.g. Menu Scene, Track Scene, Results Scene. (Commonly referred to as `Screen`)
- *Track*: An object that represents a playable song, containing information about bpm, note intervals, etc. 
- *Note*: The representation of a hittable object within a track. A track will be comprised of a series of notes that the player must hit.

There are several core features that have been implemented thusly.
- Core loop represented by the `Engine` class, which runs the code to draw onto a canvas and execute any logical actions related to a `Scene`.
- Input detection for both mouse and keyboard.
- Track parsing from a basic but proprietary format.
- Track hit registration for `notes` and primitive cheese detection.
- Primitive `note` types, e.g. standard single `notes` and 'long' or sustained notes.
    