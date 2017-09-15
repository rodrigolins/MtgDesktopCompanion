package org.magic.api.pricers.impl;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.magic.api.beans.MagicCard;
import org.magic.api.beans.MagicEdition;
import org.magic.api.beans.MagicPrice;
import org.magic.api.interfaces.abstracts.AbstractMagicPricesProvider;

public class CardKingdomPricer extends AbstractMagicPricesProvider {

	Document doc;
	List<MagicPrice> list;
	CloseableHttpClient httpclient;
	
	static final Logger logger = LogManager.getLogger(CardKingdomPricer.class.getName());

	public CardKingdomPricer() {
		super();
		
		list=new ArrayList<MagicPrice>();
		httpclient = HttpClients.createDefault();
		
		if(!new File(confdir, getName()+".conf").exists()){
				props.put("URL", "http://www.cardkingdom.com/mtg/");
				props.put("WEBSITE", "http://www.cardkingdom.com/");
				props.put("USER_AGENT", "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6");
				save();
		}
		
	}
	
	public static void main(String[] args) throws IOException {
		MagicCard mc = new MagicCard();
		mc.setName("Commandeer");
		
		MagicEdition ed = new MagicEdition();
		ed.setSet("Coldsnap");
		
		CardKingdomPricer pric = new CardKingdomPricer();
		pric.getPrice(ed, mc);
		
	}
	
	
	public String format(String s)
	{
		return s.replaceAll("'s", "s").replaceAll(" ", "-").toLowerCase();
	}
	
	
	public List<MagicPrice> getPrice(MagicEdition me,MagicCard card) throws IOException {
		
		list.clear();
		String html = props.getProperty("URL");
		
		if(me==null)
			me = card.getEditions().get(0);

		String keyword ="";
		
		String url = html+format(me.getSet())+"/"+format(card.getName());
		

		logger.info(getName() +" looking for prices " + url );
		doc = Jsoup.connect(url).userAgent(props.getProperty("USER_AGENT")).timeout(0).get();
		Elements prices =null;
		Elements qualities = null;
		try{
		qualities = doc.select(".cardTypeList li");
		prices = doc.select(".stylePrice");
		
		}catch(IndexOutOfBoundsException e)
		{
			logger.info(getName() +" no item");
			return list;
		}
		
		List<MagicPrice> list = new ArrayList<MagicPrice>();
		
		
		for(int i=0;i<qualities.size();i++)
		{
			 MagicPrice mp =new MagicPrice();
			 
			 String price = prices.get(i).html().replaceAll("\\$", "");
			 mp.setValue(Double.parseDouble(price));
			 mp.setCurrency("$");
			 mp.setSeller("Card Kingdom");
			 mp.setSite(getName());
			 mp.setUrl(url);
			 mp.setQuality(qualities.get(i).html());
			 mp.setLanguage("");
			 
			 if(!qualities.get(i).hasClass("disabled"))
			 list.add(mp);
		}
		logger.info(getName() +" found " + list.size() +" item(s)" );
		return list;
	}
	
	

	@Override
	public String getName() {
		return "Card Kingdom";
	}


	@Override
	public void alertDetected(List<MagicPrice> p) {
		// TODO Auto-generated method stub
		
	}
	
}


