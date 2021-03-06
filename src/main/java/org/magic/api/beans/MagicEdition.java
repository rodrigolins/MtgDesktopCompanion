package org.magic.api.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MagicEdition implements Serializable, Comparable<MagicEdition> {
	public static final long serialVersionUID = 1L;

	private String set;
	private String rarity;
	private String artist;
	private String multiverse_id;
	private String flavor;
	private String number;
	private String layout;
	private String url;
	private String id;
	private String releaseDate;
	private String type;
	private int cardCount;
	private String block;
	private String border;
	private transient List<Object> booster;
	private Map<String, String> translations;
	private boolean onlineOnly;
	private String magicCardsInfoCode;
	private Integer mkm_id;
	private String mkm_name;
	private String gathererCode;
	private boolean foilOnly;

	
	
	public boolean isFoilOnly() {
		return foilOnly;
	}

	public void setFoilOnly(boolean foilOnly) {
		this.foilOnly = foilOnly;
	}

	public String getMultiverseid() {
		return multiverse_id;
	}
	
	public void setMultiverseid(String multiverseid) {
		this.multiverse_id = multiverseid;
	}
	
	public Integer getMkmid() {
		return mkm_id;
	}

	public void setMkmid(Integer mkmid) {
		this.mkm_id = mkmid;
	}

	public String getMkmName() {
		return mkm_name;
	}

	public void setMkmName(String mkmname) {
		this.mkm_name = mkmname;
	}

	public String getMagicCardsInfoCode() {
		return magicCardsInfoCode;
	}

	public void setMagicCardsInfoCode(String magicCardsInfoCode) {
		this.magicCardsInfoCode = magicCardsInfoCode;
	}

	public boolean isOnlineOnly() {
		return onlineOnly;
	}

	public void setOnlineOnly(boolean onlineOnly) {
		this.onlineOnly = onlineOnly;
	}

	public Map<String, String> getTranslations() {
		return translations;
	}

	public void setTranslations(Map<String, String> translations) {
		this.translations = translations;
	}
	
	public MagicEdition(String idMe)
	{
		this.id=idMe;
		booster = new ArrayList<>();
	}

	public MagicEdition() {
		booster = new ArrayList<>();
	}

	public List<Object> getBooster() {
		return booster;
	}

	public void setBooster(List<Object> booster) {
		this.booster = booster;
	}

	public String getBorder() {
		return border;
	}

	public void setBorder(String border) {
		this.border = border;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getCardCount() {
		return cardCount;
	}

	public void setCardCount(int cardCount) {
		this.cardCount = cardCount;
	}

	public String getBlock() {
		return block;
	}

	public void setBlock(String block) {
		this.block = block;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (this.getClass() != obj.getClass())
			return false;

		return getId().equals(((MagicEdition) obj).getId());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return getSet();
	}

	public String getSet() {
		return set;
	}

	public void setSet(String set) {
		this.set = set;
	}

	public String getRarity() {
		return rarity;
	}

	public void setRarity(String rarity) {
		this.rarity = rarity;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}


	public String getFlavor() {
		return flavor;
	}

	public void setFlavor(String flavor) {
		this.flavor = flavor;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int compare(MagicEdition o1, MagicEdition o2) {
		return o1.getSet().compareTo(o2.getSet());
	}

	@Override
	public int hashCode() {
		if (set != null)
			return set.hashCode();

		return -1;
	}

	@Override
	public int compareTo(MagicEdition o) {
		return compare(this, o);
	}

	public String getGathererCode() {
		return gathererCode;
	}

	public void setGathererCode(String gathererCode) {
		this.gathererCode = gathererCode;
	}

}
