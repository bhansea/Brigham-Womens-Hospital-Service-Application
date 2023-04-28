package edu.wpi.punchy_pegasi;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class TTS {

    private static final String VOICE_NAME = "kevin16";

    public static void speak(String text) {
        System.setProperty("freetts.voices","com.sun.speech.freetts.en.us.cmu_time_awb.AlanVoiceDirectory");
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        VoiceManager voiceManager = VoiceManager.getInstance();
        Voice voice = voiceManager.getVoice(VOICE_NAME);
        voice.setRate(170);
        voice.setPitch(120);
        voice.allocate();
        voice.speak(text);
    }
}
