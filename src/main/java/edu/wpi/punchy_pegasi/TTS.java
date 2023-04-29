package edu.wpi.punchy_pegasi;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class TTS {

    private static final String VOICE_NAME = "kevin16";
    private static final VoiceManager voiceManager = VoiceManager.getInstance();
    private static final Voice voice = voiceManager.getVoice(VOICE_NAME);

    public static void speak(String text) {
        System.setProperty("freetts.voices","com.sun.speech.freetts.en.us.cmu_time_awb.AlanVoiceDirectory");
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        voice.setRate(170);
        voice.setPitch(120);
        voice.allocate();
        voice.speak(text);
    }
}
