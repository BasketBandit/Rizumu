package com.basketbandit.rizumu.track;

import com.basketbandit.rizumu.drawable.Note;
import com.basketbandit.rizumu.drawable.NoteGroup;

import java.io.*;
import java.util.ArrayList;

public class TrackReader {
    private BufferedReader bufferedReader;
    private String title;
    private int beatsPerMinute;
    private int introLength;
    private String noteData;
    private ArrayList<NoteGroup> noteGroups = new ArrayList<>();

    public TrackReader(String location) {
        try {
            File file = new File(getClass().getResource(location).getFile());
            this.bufferedReader = new BufferedReader(new FileReader(file));
        } catch(FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public Track build() {
        try {
            bufferedReader.mark(5000);

            this.title = bufferedReader.readLine();
            this.beatsPerMinute = Integer.parseInt(bufferedReader.readLine());
            this.introLength = Integer.parseInt(bufferedReader.readLine());
            int lines = (int) bufferedReader.lines().count();
            int[][] trackArray = new int[lines][4];

            bufferedReader.reset();
            bufferedReader.readLine();
            bufferedReader.readLine();
            bufferedReader.readLine();

            for(int y = 0; y < lines; y++) {
                String[] data =  bufferedReader.readLine().split(""); // Split nibble into bits
                for(int x = 0; x < data.length; x++) {
                    trackArray[y][x] = Integer.parseInt(data[x]);
                }
            }

            for(int y = 0; y < trackArray.length; y++) {
                ArrayList<Note> notes = new ArrayList<>();

                for(int x = 0; x < trackArray[0].length; x++) {
                    int type = trackArray[y][x];

                    if(type == 2) {
                        int counter = y;
                        int count = 0;
                        while(trackArray[counter][x] == 2) {
                            trackArray[counter][x] = 0;
                            counter++;
                            count++;
                        }
                        notes.add(new Note(x, count));
                        continue;
                    }

                    notes.add((type > 0) ? new Note(x): null);
                }

                noteGroups.add(new NoteGroup(notes));
                notes.clear();
            }


            return new Track(this.title, this.beatsPerMinute, this.introLength, this.noteGroups);
        } catch(IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
