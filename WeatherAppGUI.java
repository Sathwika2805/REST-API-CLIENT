import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.json.*;

public class WeatherAppGUI {
    private static final String API_KEY = "YOUR_API_KEY";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=";

    public static void main(String[] args) {
        JFrame frame = new JFrame("Weather App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTextField cityField = new JTextField("Enter city");
        JButton getWeatherButton = new JButton("Get Weather");
        JTextArea weatherDisplay = new JTextArea();
        weatherDisplay.setEditable(false);

        getWeatherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String city = cityField.getText();
                try {
                    String jsonResponse = getWeatherData(city);
                    String weatherInfo = parseWeather(jsonResponse);
                    weatherDisplay.setText(weatherInfo);
                } catch (Exception ex) {
                    weatherDisplay.setText("Error fetching weather data.");
                }
            }
        });

        panel.add(cityField, BorderLayout.NORTH);
        panel.add(getWeatherButton, BorderLayout.CENTER);
        panel.add(new JScrollPane(weatherDisplay), BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);
    }

    private static String getWeatherData(String city) throws IOException {
        String urlString = BASE_URL + city + "&appid=" + API_KEY + "&units=metric";
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    private static String parseWeather(String jsonResponse) {
        JSONObject jsonObj = new JSONObject(jsonResponse);
        String cityName = jsonObj.getString("name");
        JSONObject main = jsonObj.getJSONObject("main");
        double temperature = main.getDouble("temp");
        int humidity = main.getInt("humidity");
        String weatherDescription = jsonObj.getJSONArray("weather").getJSONObject(0).getString("description");

        return "Weather in " + cityName + ":\n" + "Temperature: " + temperature + "°C\n" + "Humidity: " + humidity
                + "%\n" + "Condition: " + weatherDescription;
    }
}
