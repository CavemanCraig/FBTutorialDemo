package com.example.domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.google.gson.Gson;

public class TestUtils {

	public static String doGET(String URLstring) {
		try {
			String returnVal = "";

			URL url = new URL(URLstring);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				returnVal = "Failed : HTTP error code : "
						+ conn.getResponseCode();
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			String output;
			while ((output = br.readLine()) != null) {
				returnVal += output;
			}

			conn.disconnect();
			return returnVal;

		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String doPOST(String URLstring, String JSONInput) {
		try {
			String returnVal = "";

			URL url = new URL(URLstring);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");

			OutputStreamWriter wr = new OutputStreamWriter(
					conn.getOutputStream());
			wr.write(JSONInput);
			wr.flush();

			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				returnVal += line;
			}
			wr.close();
			rd.close();
			return returnVal;

		} catch (MalformedURLException e) {
			e.printStackTrace();
			return "printStackTrace error: " + e;
		} catch (IOException e) {
			e.printStackTrace();
			return "IOException error: " + e;
		}
	}

	public static Player createPlayerWithFriends(URL baseContext, long facebookID, String name,
			ArrayList<String> friendIDList) {
		String targetURL = baseContext
				+ "rest/webService/UserRequest/" + facebookID + "/" + name;
		String JSONInput = "";
		String response = doPOST(targetURL, JSONInput);

		targetURL = baseContext
				+ "rest/webService/PlayerRequest/" + facebookID + "/" + name;
		JSONInput = friendIDList.toString();
		response = doPOST(targetURL, JSONInput);

		targetURL = baseContext
				+ "rest/webService/Player/" + facebookID;
		response = doGET(targetURL);

		Gson gsonPlayer = new Gson();
		Player responsePlayer = gsonPlayer.fromJson(response, Player.class);

		return responsePlayer;
	}

	public static User getUser(URL baseContext, long facebookID) {
		String targetURL = baseContext
				+ "rest/webService/User/" + facebookID;
		String response = doGET(targetURL);

		Gson gsonUser = new Gson();
		User responseUser = gsonUser.fromJson(response, User.class);

		return responseUser;
	}

	public static Player getPlayer(URL baseContext, long facebookID) {
		String targetURL = baseContext
				+ "rest/webService/Player/" + facebookID;
		String response = doGET(targetURL);

		Gson gsonPlayer = new Gson();
		Player responsePlayer = gsonPlayer.fromJson(response, Player.class);

		return responsePlayer;
	}

	public static String removePlayer(URL baseContext, long facebookID) {
		String targetURL = baseContext
				+ "rest/webService/PlayerRemovalRequest/" + facebookID;
		String JSONInput = "";
		String response = doPOST(targetURL, JSONInput);

		return response;
	}
}