import org.junit.Before;
import org.junit.Test;
import rv_wrappers.RvCommunication;


import static org.junit.Assert.assertTrue;

/**
 * Created by JLyc on 9. 4. 2015.
 */
public class RvCommunicationTest {

    @Test
    public void jmsTest() {
        RvCommunication rv= new RvCommunication();
        rv.init();
        Thread rvListening = new Thread(rv);
        rvListening.start();
        assertTrue("rv is not listening", rvListening.isAlive());
        rv.stopListening();
        rv.close();
        assertTrue("rv is still listening", rvListening.isAlive());
    }
}
