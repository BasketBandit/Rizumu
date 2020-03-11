package com.basketbandit.rizumu.database;

import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.beatmap.core.Beatmap;
import com.basketbandit.rizumu.beatmap.core.Track;
import com.basketbandit.rizumu.score.Score;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Database {
    private static final Logger log = LoggerFactory.getLogger(Database.class);

    /**
     * @param username {@link String}
     * @param password {@link String}
     * @return boolean
     */
    public static boolean login(String username, String password) {
        try(java.sql.Connection c = Connection.getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT * FROM `user` WHERE `username` = ?")) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if(!rs.next()) {
                return false;
            }

            if(BCrypt.checkpw(password, rs.getString("password_hash"))) {
                Configuration.setUserId(rs.getInt("id"));
                return true;
            }

            return false;
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", Database.class.getSimpleName(), ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * Retrieves the username corresponding to the input id
     * @param id {@link Integer}
     * @return {@link String}
     */
    private static String getUsername(int id) {
        try(java.sql.Connection c = Connection.getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT `username` FROM `user` WHERE id = ?")) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if(!rs.next()) {
                return null;
            }

            return rs.getString("username");

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", Database.class.getSimpleName(), ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * Retrieves a beatmap id using the set key {track_id, name, keys}
     * @param trackId {@link Integer}
     * @param name {@link String}
     * @param keys {@link Integer}
     * @return {@link Integer}
     */
    private static int getBeatmapId(int trackId, String name, int keys) {
        try(java.sql.Connection c = Connection.getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT `id` FROM `beatmap` WHERE track_id = ? AND title = ? AND key_count = ?")) {

            ps.setInt(1, trackId);
            ps.setString(2, name);
            ps.setInt(3, keys);
            ResultSet rs = ps.executeQuery();

            if(!rs.next()) {
                return -1;
            }

            return rs.getInt("id");

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", Database.class.getSimpleName(), ex.getMessage(), ex);
            return -1;
        }
    }

    /**
     * Retrieves the top 5 scores for a given beatmap
     * @param track {@link Track}
     * @param beatmap {@link Beatmap}
     * @return {@link ArrayList<Score>}
     */
    public static ArrayList<Score> getScores(Track track, Beatmap beatmap) {
        try(java.sql.Connection c = Connection.getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT score.*, user.username FROM `score` INNER JOIN user ON score.user_id = user.id WHERE track_id = ? AND beatmap_id = ? ORDER BY `score` DESC LIMIT 5")) {

            ps.setInt(1, track.getId());
            ps.setInt(2, getBeatmapId(track.getId(), beatmap.getName(), beatmap.getKeys()));
            ResultSet rs = ps.executeQuery();

            ArrayList<Score> scores = new ArrayList<>();
            while(rs.next()) {
                scores.add(new Score(track, beatmap, rs.getString("username"), rs.getInt("score"), rs.getInt("max_combo"), rs.getInt("mx_hits"), rs.getInt("ex_hits"), rs.getInt("nm_hits"), rs.getInt("misses")));
            }

            return scores;

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", Database.class.getSimpleName(), ex.getMessage(), ex);
            return new ArrayList<>();
        }
    }

    /**
     * Uploads a score generated form playing a {@link Beatmap}
     * @param score {@link Score}
     * @return boolean
     */
    public static boolean uploadScore(Score score) {
        try(java.sql.Connection c = Connection.getConnection();
            PreparedStatement ps = c.prepareStatement("INSERT INTO `score` (user_id, track_id, beatmap_id, score, max_combo, mx_hits, ex_hits, nm_hits, misses) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            int beatmapId;
            if((beatmapId = getBeatmapId(score.getTrack().getId(), score.getBeatmap().getName(), score.getBeatmap().getKeys())) == -1) {
                return false;
            }

            ps.setInt(1, Configuration.getUserId());
            ps.setInt(2, score.getTrack().getId());
            ps.setInt(3, beatmapId);
            ps.setInt(4, score.getScore());
            ps.setInt(5, score.getHighestCombo());
            ps.setInt(6, score.getMxHit());
            ps.setInt(7, score.getExHit());
            ps.setInt(8, score.getNmHit());
            ps.setInt(9, score.getMissedNotes());

            return !ps.execute(); // sql returns 0 on success (yikes)

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", Database.class.getSimpleName(), ex.getMessage(), ex);
            return false;
        }
    }
}
