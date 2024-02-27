package com.example.MotorolaScienceCup;

import com.example.MotorolaScienceCup.Asteroids.Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import javax.sound.sampled.*;

import javax.swing.JOptionPane;


import static javax.sound.sampled.AudioSystem.getAudioInputStream;


public class Sound {

    public static AudioInputStream getStream(String filepath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {


        File file = new File(filepath);
        AudioInputStream stream = getAudioInputStream(file);
        return stream;
    }

    public static Clip getClip(String filepath,float volume) throws UnsupportedAudioFileException, IOException, LineUnavailableException {


        File file = new File(filepath);
        AudioInputStream stream = getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(stream);
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(volume);
        return clip;
    }
    public static void play(String filepath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        File file = new File(filepath);
        AudioInputStream stream = getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(stream);
        clip.start();
    }

    public static void play(String filepath, float volume) throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        File file = new File(filepath);
        AudioInputStream stream = getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(stream);
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(volume);
        clip.start();
    }


    public static void loopPlay(String filepath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        File music = new File(filepath);
        AudioInputStream inputStream = AudioSystem.getAudioInputStream(music);
        Clip clip = AudioSystem.getClip();
        clip.open(inputStream);
        FloatControl gainControl =
                (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(-30.0f);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.start();
        Menu.clips.add(clip);


    }

    public static void loopPlay(String filepath, int count) throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        File music = new File(filepath);
        AudioInputStream inputStream = AudioSystem.getAudioInputStream(music);
        Clip clip = AudioSystem.getClip();
        clip.open(inputStream);
        FloatControl gainControl =
                (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(-30.0f);
        clip.loop(count);
        clip.start();
        Menu.clips.add(clip);


    }





}
