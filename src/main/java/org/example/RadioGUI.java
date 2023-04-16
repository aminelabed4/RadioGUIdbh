package org.example;

import lombok.extern.slf4j.Slf4j;

import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.sound.sampled.Clip.LOOP_CONTINUOUSLY;

@Slf4j
public class RadioGUI extends JFrame {
    private int volumePercentage = 50;
    private int frequency = 100;
    private boolean isOn = false;
    private JPanel panel1;
    private JButton frequencyUpButton;
    private JButton frequencyDownButton;
    private JLabel frequencyValueLabel;
    private JButton powerButton;
    private JButton volumeUpButton;
    private JButton volumeDownButton;
    private JLabel volumeLabel;
    private JLabel channelNameLabel;
    private JLabel channelTitleLabel;

    public RadioGUI() {
        setContentPane(panel1);
        setTitle("Radio");
        setSize(400, 400);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        channelNameLabel.setVisible(false);
        channelTitleLabel.setVisible(false);
        frequencyUpButton.setEnabled(false);
        frequencyDownButton.setEnabled(false);
        volumeUpButton.setEnabled(false);
        volumeDownButton.setEnabled(false);

        frequencyDownButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (frequency > 30) {
                    frequency = frequency - 5;
                    frequencyValueLabel.setText(String.valueOf(frequency));
                    if (changedStationMinus()) {
                        changeChannelNameMinus();
                        playMusic();
                    }
                }

            }
        });
        frequencyUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (frequency < 300) {
                    frequency = frequency + 5;
                    frequencyValueLabel.setText(String.valueOf(frequency));
                    if (changedStationPlus()) {
                        changeChannelNamePlus();
                        playMusic();
                    }
                }

            }
        });
        powerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isOn) {
                    powerButton.setText("ON");
                    isOn = false;
                    stopMusic();
                    channelNameLabel.setVisible(false);
                    channelTitleLabel.setVisible(false);
                    frequencyUpButton.setEnabled(false);
                    frequencyDownButton.setEnabled(false);
                    volumeUpButton.setEnabled(false);
                    volumeDownButton.setEnabled(false);
                } else {
                    powerButton.setText("OFF");
                    isOn = true;
                    playMusic();
                    channelNameLabel.setVisible(true);
                    channelTitleLabel.setVisible(true);
                    frequencyUpButton.setEnabled(true);
                    frequencyDownButton.setEnabled(true);
                    volumeUpButton.setEnabled(true);
                    volumeDownButton.setEnabled(true);
                }
            }
        });
        volumeUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (volumePercentage < 100) {
                    volumePercentage = volumePercentage + 10;
                    volumeLabel.setText(String.valueOf(volumePercentage));

                    try {
                        for (Clip clip : TracksStrategyEnum.getClips()) {
                            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                            gainControl.setValue(pertentageToDecibel());
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid volume level", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        volumeDownButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (volumePercentage > 0) {
                    volumePercentage = volumePercentage - 10;
                    volumeLabel.setText(String.valueOf(volumePercentage));
                    try {
                        // Set the new volume level for all audio clips
                        for (Clip clip : TracksStrategyEnum.getClips()) {
                            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                            gainControl.setValue(pertentageToDecibel());
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid volume level", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    public void playMusic() {
        if (isOn) {
            Clip clip = TracksStrategyEnum.getFromFrequency(frequency).getClip();
            stopMusic();
            clip.setLoopPoints(0, -1);
            clip.loop(LOOP_CONTINUOUSLY);
        }
    }

    public void stopMusic() {
        TracksStrategyEnum.getClips().forEach(DataLine::stop);
    }

    public void changeChannelNamePlus() {
        if (changedStationPlus()) {
            channelNameLabel.setText(TracksStrategyEnum.getFromFrequency(frequency).getChannel());
        }
    }

    public void changeChannelNameMinus() {
        if (changedStationMinus()) {
            channelNameLabel.setText(TracksStrategyEnum.getFromFrequency(frequency).getChannel());
        }
    }

    public boolean changedStationPlus() {
        return (frequency == 75) || (frequency == 100) || (frequency == 130) || (frequency == 155) || (frequency == 180) || (frequency == 205);
    }

    public boolean changedStationMinus() {
        return (frequency == 70) || (frequency == 95) || (frequency == 125) || (frequency == 150) || (frequency == 175) || (frequency == 200);
    }

    public Float pertentageToDecibel() {
        if (volumePercentage == 0) {
            return -60f;
        } else {
            float volume = (float) volumePercentage / 100;
            float decibels = 0;
            if (volume > 0) {
                decibels = (float) (20 * Math.log10(volume));
            }
            return decibels;
        }
    }
}
