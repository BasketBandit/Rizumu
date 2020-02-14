package com.basketbandit.rizumu.scheduler.tasks;

import com.basketbandit.rizumu.beatmap.core.Note;
import com.basketbandit.rizumu.scene.TrackScene;
import com.basketbandit.rizumu.scheduler.Task;

public class NoteLoadTask implements Task {
    private final Note note;
    private final TrackScene scene;

    public NoteLoadTask(Note note, TrackScene scene) {
        this.note = note;
        this.scene = scene;
    }

    // run() is made to be synchronized to prevent ConcurrentModificationExceptions, ensuring that all notes DO load correctly.
    @Override
    public synchronized void run() {
        scene.getNotes().add(note);
    }
}
