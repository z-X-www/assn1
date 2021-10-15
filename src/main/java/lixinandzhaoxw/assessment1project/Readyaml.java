package lixinandzhaoxw.assessment1project;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class Readyaml {
    public static Map<String, ArrayList> readYaml(){
        Map<String, ArrayList> colors = new HashMap<>();
        InputStream in = null;
        try {
            File file = new File("src\\main\\resources\\color.yaml");
            in = new FileInputStream(file);
            Yaml yaml = new Yaml();
            colors =  yaml.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return colors;
    }

}
