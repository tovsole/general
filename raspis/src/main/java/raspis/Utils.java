package raspis;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by otovstiuk on 27.05.2016.
 */
public class Utils {

  	public static final Properties mainProps = new Properties();
    private static final String configFileName = "config.properties";

    public static void loadProps() {
        //Loading project properties
        FileInputStream in = null;
        try {
            in = new FileInputStream(configFileName);
            mainProps.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public static int timeToHalfMinutes(String time){
        int result=0;
        if (!time.isEmpty() ) {
            result= (Integer.parseInt(time.substring(0, time.indexOf(":"))) * 120) +
                    (Integer.parseInt(time.substring(time.indexOf(":") + 1, time.length())) * 2);
        }
        return result;
    }

    }

