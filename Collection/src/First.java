import java.util.HashMap;
import java.util.Map;

/**
 * Created by JLyc.Development@gmail.com on 1/11/2016.
 */
public class First {
    Map<String, String> pole = new HashMap<>();

    public First(Map<String, String> pole) {
        this.pole=pole;
        pole.put("moje", "tvoje");
        returnPole(this.pole);
    }

    public Map<String, String> returnPole(final Map<String, String> pole){
        Map<String, String> pole1 = pole;
        pole1.put("nieco", "nove");
        pole.put("nase", "tvoje");
        return pole;
    }

}
