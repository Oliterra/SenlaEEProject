package edu.senla.helper;

import edu.senla.Application;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
public class JsonWorker {

    private String readJson(InputStream is) {
        StringBuilder jsonInfo = new StringBuilder();
        try (InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {
            String jsonLine;
            while ((jsonLine = reader.readLine()) != null) {
                jsonInfo.append(jsonLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonInfo.toString();
    }

    public String getJson(String jsonFileName) {
        return readJson(Application.class.getClassLoader().getResourceAsStream(jsonFileName));
    }

}
