import java.util.HashMap;
import java.util.Map;

/**
 * Created by JLyc.Development@gmail.com on 1/11/2016.
 */
public class Second {
    Map<String, String> pole = new HashMap<>();

    public static void main(String[] args) {
        Second second = new Second();
        second.pole.put("1", "1");
        First first = new First(second.pole);
        System.out.println(second.pole);

    }


}
