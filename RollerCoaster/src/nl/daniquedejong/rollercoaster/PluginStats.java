/*
*  _______                .__                           .___            ____.
* \______ \ _____    ____ |__| ________ __   ____     __| _/____       |    | ____   ____    ____
*  |    |  \\__  \  /    \|  |/ ____/  |  \_/ __ \   / __ |/ __ \      |    |/  _ \ /    \  / ___\
*  |    `   \/ __ \|   |  \  < <_|  |  |  /\  ___/  / /_/ \  ___/  /\__|    (  <_> )   |  \/ /_/  >
* /_______  (____  /___|  /__|\__   |____/  \___  > \____ |\___  > \________|\____/|___|  /\___  /
*         \/     \/     \/       |__|           \/       \/    \/                       \//_____/
* Authors:  Daníque de Jong <danique at daniquedejong.nl>
*     		Bart Fijneman <bart at bartfijneman.com>
*
*********************
*
* Copyright (c) 2017  Daníque de Jong
* All Rights Reserved.
*
*/
package nl.daniquedejong.rollercoaster;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

import org.bukkit.Bukkit;

public class PluginStats 
{
	private final String USER_AGENT = "Mozilla/5.0";
	
	public boolean sendStats() throws IOException
	{
		String url = "https://daniquedejong.nl/check/modelride.php";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		InetAddress IP = InetAddress.getLocalHost();
		String urlParameters = String.format("ip=%s&key=%s", IP.getHostAddress() + ":" + Bukkit.getPort(), Main.r.c.getString("key"));

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		if(response.toString().startsWith("allow"))
		{
			if(!Main.r.c.getString("key").startsWith("ab_")) return false;
			return true;
		}
		else
		{
			System.out.println("---");
			System.out.println("---");
			System.out.println("---");
			System.out.println("---");
			System.out.println("Your plugin isn't active anymore! What did i do wrong?");
			System.out.println("* You didn't insert the key, check the config.");
			System.out.println("* Your key has been deleted due abuse.");
			System.out.println("* For questions contact us at info@daniquedejong.nl.");
			System.out.println("---");
			System.out.println("---");
			System.out.println("---");
			System.out.println("---");
			return false;
		}
	}
}
