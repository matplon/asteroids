package com.example.MotorolaScienceCup;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import javax.sound.sampled.*;

import javax.swing.JOptionPane;


import static javax.sound.sampled.AudioSystem.getAudioInputStream;


public class Sound {


    public static void play(String filepath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        File file = new File(filepath);
        AudioInputStream stream = getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(stream);
        clip.start();
    }

    public static void loopPlay(String filepath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File music = new File(filepath);
        AudioInputStream inputStream = AudioSystem.getAudioInputStream(music);
        Clip clip = AudioSystem.getClip();
        clip.open(inputStream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.start();

    }

}
