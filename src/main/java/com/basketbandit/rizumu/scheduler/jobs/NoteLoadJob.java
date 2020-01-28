package com.basketbandit.rizumu.scheduler.jobs;

import com.basketbandit.rizumu.beatmap.Note;
import com.basketbandit.rizumu.scene.TrackScene;
import com.basketbandit.rizumu.scheduler.Job;
import com.basketbandit.rizumu.scheduler.tasks.NoteLoadTask;

import java.util.concurrent.TimeUnit;

public class NoteLoadJob extends Job {
    private final NoteLoadTask noteLoadTask;

    public NoteLoadJob(Note note, TrackScene scene) {
        super(note.getTime(), 0, TimeUnit.MILLISECONDS);
        this.noteLoadTask = new NoteLoadTask(note, scene);
    }

    @Override
    public void run() {
        handleTask(noteLoadTask);
    }
}
