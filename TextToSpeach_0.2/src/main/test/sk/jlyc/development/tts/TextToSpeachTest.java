package sk.jlyc.development.tts;

import org.junit.Test;

import static junit.framework.Assert.*;

/**
 * Created by JLyc on 12/24/2015.
 */
public class TextToSpeachTest {


    @Test
    public void TextToSpeachTest(){
        assertNotNull(TextToSpeach.getInstance());
    }

    @Test
    public void TestSpeakingOfTextToSPeach(){
        assertTrue(TextToSpeach.getInstance().ttsSay("hello my friend how are you this has to be long to take time to check it"));
    }
}
