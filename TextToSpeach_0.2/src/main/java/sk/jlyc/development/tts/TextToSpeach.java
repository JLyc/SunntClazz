package sk.jlyc.development.tts;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import sun.plugin.dom.core.Document;


/**
 * Created by JLyc on 12/12/2015.
 */
public class TextToSpeach {
    private static String VOICE = "kevin16";


    private static VoiceManager voiceManager;
    private static Voice voice;

    private static TextToSpeach textToSpeach;

    public static TextToSpeach getInstance() {
        if (textToSpeach == null) textToSpeach = new TextToSpeach();
        return textToSpeach;
    }

    public TextToSpeach() {
        init();
    }

    private void init() {
        voiceManager = VoiceManager.getInstance();
        if (voiceManager.contains(VOICE)) {
            voice = voiceManager.getVoice(VOICE);
            voice.allocate();
        }
        else {
            throw new RuntimeException();
        }

    }

    public boolean ttsSay(String words) {
        return voice.speak(words);
    }


    public boolean ttsRead(Document doc){
        return voice.speak(doc);
    }

    //TODO deallocation preven from deadlock and memory leack
//    public static void restart(){}


}