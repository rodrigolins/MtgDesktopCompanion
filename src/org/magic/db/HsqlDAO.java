package org.magic.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.magic.api.beans.MagicCard;
import org.magic.api.beans.MagicCollection;
import org.magic.api.beans.MagicDeck;
import org.magic.api.beans.MagicEdition;

import sun.util.locale.provider.LocaleServiceProviderPool.LocalizedObjectGetter;

public class HsqlDAO implements MagicDAO {

	static final Logger logger = LogManager.getLogger(HsqlDAO.class.getName());
    Connection con;
    List<MagicCard> listNeeded ;
    
	 public void init() throws ClassNotFoundException, SQLException {
	      Class.forName("org.hsqldb.jdbc.JDBCDriver");
		  con=DriverManager.getConnection("jdbc:hsqldb:"+System.getProperty("user.home")+"/magicDeskCompanion/db/magicDB","SA","");
		  
		  createDB();
		
	 }
	 
	 public boolean createDB()
	 {
		 try{
		 	con.createStatement().executeUpdate("create table cards (name varchar(250), mcard OBJECT, edition varchar(20), cardprovider varchar(50),collection varchar(250))");
		 	 logger.debug("Create table Cards");
		 	con.createStatement().executeUpdate("create table decks (id_deck integer PRIMARY KEY , name varchar(45),commentaire varchar(1500))");
		 	logger.debug("Create table decks");
		 	con.createStatement().executeUpdate("create table decks_cartes (id_deck integer PRIMARY KEY, id_carte varchar(45),exemplaire integer)");
		 	logger.debug("Create table decks_carte");
		 	con.createStatement().executeUpdate("create table collections (name varchar(250) PRIMARY KEY)");
		 	logger.debug("Create table collections");
		 	con.createStatement().executeUpdate("insert into collections values ('Needed')");
		 	con.createStatement().executeUpdate("insert into collections values ('For sell')");
		 	logger.debug("populate collections");
		 	
		 	
		 	return true;
		 }catch(SQLException e)
		 {
			 logger.debug("Base already exist");
			 return false;
		 }
		 
	 }

	@Override
	public void saveCard(MagicCard mc, MagicCollection collection) throws SQLException {
		
		logger.debug("saving " + mc +" in " + collection);
		
		PreparedStatement pst = con.prepareStatement("insert into cards values (?,?,?,?,?)");
		 pst.setString(1, mc.getName());
		 pst.setObject(2, mc);
		 pst.setString(3, mc.getEditions().get(0).getId());
		 pst.setString(4, "");
		 pst.setString(5, collection.getName());
		 
		 pst.executeUpdate();
			
		
		 
		
	}

	@Override
	public void removeCard(MagicCard mc, MagicCollection collection) throws SQLException {
		logger.debug("remove " + mc + " from " + collection);
		PreparedStatement pst = con.prepareStatement("delete from cards where name=? and edition=? and collection=?");
		 pst.setString(1, mc.getName());
		 pst.setString(2, mc.getEditions().get(0).getId());
		 pst.setString(3, collection.getName());
		 pst.executeUpdate();
	}

	@Override
	public MagicDeck getDeck(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveDeck(MagicDeck deck) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeDeck(MagicDeck deck) {
		// TODO Auto-generated method stub
		
	}

	
	public List<MagicCard> getCardsFromCollection(MagicCollection collection,MagicEdition me) throws SQLException
	{
		PreparedStatement pst=con.prepareStatement("select * from cards where collection= ? and edition = ?");	
		pst.setString(1, collection.getName());
		pst.setString(2, me.getId());
		ResultSet rs = pst.executeQuery();
		List<MagicCard> list = new ArrayList<MagicCard>();
		while(rs.next())
		{
			list.add((MagicCard) rs.getObject("mcard"));
		}
	
	return list;
	}
	
	
	@Override
	public List<MagicCard> getCardsFromCollection(MagicCollection collection) throws SQLException {
		PreparedStatement pst=con.prepareStatement("select * from cards where collection= ?");	
		pst.setString(1, collection.getName());
		ResultSet rs = pst.executeQuery();
		List<MagicCard> list = new ArrayList<MagicCard>();
		while(rs.next())
		{
			list.add((MagicCard) rs.getObject("mcard"));
		}
	
	return list;
	}

	@Override
	public MagicCollection getCollection(String name) throws SQLException {
		PreparedStatement pst=con.prepareStatement("select * from collections where name= ?");	
			pst.setString(1, name);
		
		ResultSet rs = pst.executeQuery();
		
		if(rs.next())
		{
			MagicCollection mc = new MagicCollection();
			mc.setName(rs.getString("name"));
			
			return mc;
		}
		
		return null;
	}

	 
	
	@Override
	public void saveCollection(MagicCollection c) throws SQLException {

		PreparedStatement pst = con.prepareStatement("insert into collections values (?)");
		 pst.setString(1, c.getName());
		 
		 pst.executeUpdate();
			
		
	}

	@Override
	public void removeCollection(MagicCollection c) throws SQLException {
		PreparedStatement pst = con.prepareStatement("delete from collections where name = ?");
		 pst.setString(1, c.getName());
		 pst.executeUpdate();
		 
		 
		 pst = con.prepareStatement("delete from cartes where collection = ?");
		 pst.setString(1, c.getName());
		 pst.executeUpdate();
		
	}

	@Override
	public List<MagicCollection> getCollections() throws SQLException {
		PreparedStatement pst=con.prepareStatement("select * from collections");	
		ResultSet rs = pst.executeQuery();
		List<MagicCollection> colls = new ArrayList<MagicCollection>();
		while(rs.next())
		{
			MagicCollection mc = new MagicCollection();
			mc.setName(rs.getString(1));
			colls.add(mc);
		}
		return colls;
	}

	@Override
	public void removeEdition(MagicEdition me, MagicCollection col) throws SQLException {
		logger.debug("remove " + me + " from " + col);
		PreparedStatement pst = con.prepareStatement("delete from cards where edition=? and collection=?");
		 pst.setString(1, me.getId());
		 pst.setString(2, col.getName());
		 pst.executeUpdate();
		
	}

	
	
}


