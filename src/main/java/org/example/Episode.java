package org.example;

public class Episode {
    public String episodeName;
    public String duration;

    public Episode(String episodeName, String duration) {
        this.episodeName = episodeName;
        this.duration = duration;
    }

    public String getEpisodeName() {
        return episodeName;
    }

    public String getDuration() {
        return duration;
    }
}
