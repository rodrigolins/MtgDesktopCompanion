package org.magic.tools;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.magic.services.MTGConstants;
import org.magic.services.MTGLogger;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

public class URLTools {

	private static Logger logger = MTGLogger.getLogger(URLTools.class);
	
	public static final String HEADER_JSON="application/json";
	
	

	private URLTools() 
	{}
	
	public static HttpURLConnection openConnection(String url) throws IOException {
		return openConnection(new URL(url));
	}
	
	public static HttpURLConnection getConnection(String url) throws IOException {
		return getConnection(new URL(url));
	}
	
	public static HttpURLConnection getConnection(URL url) throws IOException {
		logger.trace("get stream from " + url);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("User-Agent", MTGConstants.USER_AGENT);
		connection.setInstanceFollowRedirects(true);
		return connection;
	}
	
	
	public static HttpURLConnection openConnection(URL url) throws IOException {
		HttpURLConnection con = getConnection(url);
		con.connect();
		return con;
	}
	
	public static Document toHtml(String s)
	{
		return Jsoup.parse(s);
	}
	
	public static JsonElement toJson(String s)
	{
		return new JsonParser().parse(s);
	}
	
	
	
	public static Document extractHtml(String url) throws IOException
	{
		return toHtml(extractAsString(url));
	}
	
	public static JsonElement extractJson(String url) throws IOException
	{
		JsonReader reader = new JsonReader(new InputStreamReader(openConnection(url).getInputStream()));
		return new JsonParser().parse(reader);
	}
	
	public static String extractAsString(String url) throws IOException
	{
		return IOUtils.toString(openConnection(url).getInputStream(), MTGConstants.DEFAULT_ENCODING); 
	}
	

	public static BufferedImage extractImage(String url) throws IOException
	{
		return extractImage(new URL(url));
	}
	
	public static BufferedImage extractImage(URL url) throws IOException
	{
		return ImageIO.read(openConnection(url).getInputStream()); 
	}
	
	
}
