package com.basketbandit.rizumu.drawable;

import java.util.Arrays;
import java.util.List;

public class NoteGroup {
    private Note n0;
    private Note n1;
    private Note n2;
    private Note n3;

    public NoteGroup(Note n0, Note n1, Note n2, Note n3) {
        this.n0 = n0;
        this.n1 = n1;
        this.n2 = n2;
        this.n3 = n3;
    }

    public NoteGroup(List<Note> notes) {
        if(notes.size() < 4) {
            return;
        }
        Object[] notez = notes.toArray();
        this.n0 = (Note) notez[0];
        this.n1 = (Note) notez[1];
        this.n2 = (Note) notez[2];
        this.n3 = (Note) notez[3];
    }

    public List<Note> getGroup() {
        return Arrays.asList(n0, n1, n2, n3);
    }
}
