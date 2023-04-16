package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.Clip;
import java.util.Set;

public class TracksStrategyEnumTest {

    @Test
    void testGetFromFrequency() {
        TracksStrategyEnum track1 = TracksStrategyEnum.getFromFrequency(80);
        Assertions.assertEquals("track1.wav", track1.getTrack());
        Assertions.assertEquals("Jam FM", track1.getChannel());

        TracksStrategyEnum track2 = TracksStrategyEnum.getFromFrequency(140);
        Assertions.assertEquals("track2.wav", track2.getTrack());
        Assertions.assertEquals("RTL FM", track2.getChannel());

        TracksStrategyEnum track3 = TracksStrategyEnum.getFromFrequency(185);
        Assertions.assertEquals("track3.wav", track3.getTrack());
        Assertions.assertEquals("MTV FM", track3.getChannel());

        TracksStrategyEnum defaultTrack = TracksStrategyEnum.getFromFrequency(60);
        Assertions.assertEquals("staticSound.wav", defaultTrack.getTrack());
        Assertions.assertEquals("Searching... Try changing the frequency", defaultTrack.getChannel());

        TracksStrategyEnum invalidFrequency = TracksStrategyEnum.getFromFrequency(10);
        Assertions.assertEquals("staticSound.wav", invalidFrequency.getTrack());
        Assertions.assertEquals("Searching... Try changing the frequency", invalidFrequency.getChannel());
    }

    @Test
    void testGetClips() {
        Set<Clip> clips = TracksStrategyEnum.getClips();
        Assertions.assertEquals(4, clips.size());
        Assertions.assertTrue(clips.stream().allMatch(Clip::isOpen));
    }

}
