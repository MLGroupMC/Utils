package xyz;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MCServerStat {

	/*
		JSON-Simple Required
	*/

    private final String address;
    private final int port;
    private String hostName;
    private boolean online;
    private String version;
    private int onlinePlayers;
    private int maxOnlinePlayers;
    private List<String> playerList = new ArrayList<>();

    public MCServerStat(String address, int port){
        this.address = address;
        this.port = port;
        refresh();
    }

    public void refresh(){
        try {
            URL url = null;
            if(port != 0) {
                url = new URL("https://api.mcsrvstat.us/2/" + address + ":" + port);
            } else {
                url = new URL("https://api.mcsrvstat.us/2/" + address);
            }
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            InputStream in = new BufferedInputStream(connection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            JSONParser parser = new JSONParser();

            JSONObject jsonObject = (JSONObject) parser.parse(reader.readLine());
            JSONObject jsonPlayersObject = (JSONObject) jsonObject.get("players");

            setOnline((Boolean) jsonObject.get("online"));
            setHostName((String) jsonObject.get("hostname"));;
            setVersion((String) jsonObject.get("version"));

            try {
                setOnlinePlayers(Integer.parseInt(jsonPlayersObject.get("online").toString()));
                setMaxOnlinePlayers(Integer.parseInt(jsonPlayersObject.get("max").toString()));
            } catch (NullPointerException ignored){ }

            try {
                JSONArray playersListJsonArray = (JSONArray) jsonPlayersObject.get("list");
                List<String> playersList = new ArrayList<>();

                Iterator<String> iterator = playersListJsonArray.iterator();
                int values = 0;
                while (iterator.hasNext() && values <= 50) {
                    playersList.add(iterator.next());
                    values++;
                }
                setPlayerList(playersList);
            } catch (Exception ignored){ }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getOnlinePlayers() {
        return onlinePlayers;
    }

    public void setOnlinePlayers(int onlinePlayers) {
        this.onlinePlayers = onlinePlayers;
    }

    public int getMaxOnlinePlayers() {
        return maxOnlinePlayers;
    }

    public void setMaxOnlinePlayers(int maxOnlinePlayers) {
        this.maxOnlinePlayers = maxOnlinePlayers;
    }

    public List<String> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<String> playerList) {
        this.playerList = playerList;
    }
}
