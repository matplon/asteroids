package com.example.MotorolaScienceCup;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.sound.sampled.*;

import static javax.sound.sampled.AudioSystem.getAudioInputStream;


public class Sound {


    public static void play(String filepath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        File file = new File(filepath);
        AudioInputStream stream = getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(stream);
        clip.start();
    }

}
