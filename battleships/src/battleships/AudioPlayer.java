/********************************************************************************
 * Author: 1816477                                                              *
 * Date: Dec 2020                                                               *
 * Assignment: Element 011 Battleships Game                                     *
 * Description: Class AudioPlayer is an enum class which can be called to       *
 * invoke sound effect files.                                                   *
 * To play a sound file once use AudioPlayer.SOUND_NAME.play().                 *
 * To loop the sound file use AudioPlayer.SOUND_NAME.loop().                    *
 * The sound files are sourced from https://www.dl-sounds.com , this is a       *
 * royalty free sound file website.                                             *
 ********************************************************************************/

package battleships;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public enum AudioPlayer {

    //********************************Class enums********************************//

    // Sound file enums
    MUSIC("battleships/audio/music.wav"), // game music
    HIT("battleships/audio/hit.wav"), // hit sound effect
    DESTROY("battleships/audio/destroy.wav"); // destroy sound effect

    // Volume enums
    public enum Volume {
        MUTE, // mute volume
        ON // turn on volume
    }

    //***************************************************************************//

    //**************************Class instance variables*************************//

    public static Volume volume = Volume.ON; // Pre-set volume on

    private Clip clip; // Clip object used for each sound effect

    //***************************************************************************//

    //****************************Class constructors*****************************//

    /** Constructor
     * This constructor sets up each enum with its sound file to be ready for being called.
     * @param soundFileName: {String} path of sound file
     */
    AudioPlayer(String soundFileName) {

        try { // try to setup the sound clip object with provided file path

            URL url = this.getClass().getClassLoader().getResource(soundFileName); // Read file from disk

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);  // Create new audio input tream

            clip = AudioSystem.getClip(); // Prepare clip attribute

            clip.open(audioInputStream); // Open and load audio clip

        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) { // catch any errors thrown

            e.printStackTrace();   // print out error caught
        }
    }

    //***************************************************************************//

    //********************************Class methods******************************//

    /** method play()
     * This method plays a sound clip enum a single time.
     */
    public void play() {

        // check if volume is on before attempting to play
        if (volume == Volume.ON) {

            // check if clip is already running
            if (clip.isRunning()) {

                clip.stop(); // stop the sound
            }

            clip.setFramePosition(0); // set the track at the beginning
            clip.start(); // start the clip
        }
    }

    /** method loop()
     * This method plays a sound clip enum in a continuous loop.
     */
    public void loop() {

        if (volume != Volume.MUTE) {

            clip.setFramePosition(0); // rewind to the beginning
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

} // close class AudioPlayer