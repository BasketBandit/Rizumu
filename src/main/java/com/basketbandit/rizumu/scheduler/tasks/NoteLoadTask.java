package com.basketbandit.rizumu.scheduler.tasks;

import com.basketbandit.rizumu.drawable.Note;
import com.basketbandit.rizumu.scene.TrackScene;
import com.basketbandit.rizumu.scheduler.Task;

public class NoteLoadTask implements Task {
    private final Note note;
    private final TrackScene scene;

    public NoteLoadTask(Note note, TrackScene scene) {
        this.note = note;
        this.scene = scene;
    }

    // This override of the run method is made to be synchronized since its almost certain that multiple threads are going to be adding to this collection at once.
    @Override
    public synchronized void run() {
        scene.getNotes().add(note);
    }
}
