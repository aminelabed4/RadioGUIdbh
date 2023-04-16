package org.example;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Range;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Getter
public enum TracksStrategyEnum {
    TRACK_1("track1.wav", Range.between(75, 95), "Jam FM"),
    TRACK_2("track2.wav", Range.between(130, 150), "RTL FM"),
    TRACK_3("track3.wav", Range.between(180, 200), "MTV FM"),
    DEFAULT_TRACK("staticSound.wav", "Searching... Try changing the frequency");

    private final String track;
    private final Clip clip;
    private final String channel;
    private Range<Integer> bandwidth;

    TracksStrategyEnum(String track, Range<Integer> bandwidth, String channel) {
        this.track = track;
        this.bandwidth = bandwidth;
        this.clip = loadAudioClipFromResources(track);
        this.channel = channel;
    }

    TracksStrategyEnum(String track, String channel) {
        this.track = track;
        this.clip = loadAudioClipFromResources(track);
        this.channel = channel;
    }

    public static TracksStrategyEnum getFromFrequency(int frequency) {
        return Arrays.stream(values())
                .filter(entry -> entry != DEFAULT_TRACK)
                .filter(entry -> entry.bandwidth.contains(frequency))
                .findFirst()
                .orElse(DEFAULT_TRACK);
    }

    private static Clip loadAudioClipFromResources(String channel) {
        try (AudioInputStream audio = AudioSystem.getAudioInputStream(new File(TracksStrategyEnum.class.getClassLoader().getResource("sounds/" + channel).toURI()))) {
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            return clip;
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException | URISyntaxException e) {
            log.error("An Error occurred when trying to load {}", channel, e);
        }
        return null;
    }

    public static Set<Clip> getClips() {
        return Arrays.stream(values())
                .map(entry -> entry.clip)
                .collect(Collectors.toSet());
    }

}
