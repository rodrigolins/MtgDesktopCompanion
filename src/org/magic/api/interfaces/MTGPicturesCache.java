package org.magic.api.interfaces;

import java.awt.image.BufferedImage;

import org.apache.log4j.Logger;
import org.magic.api.beans.MagicCard;
import org.magic.api.beans.MagicEdition;
import org.magic.tools.MTGLogger;

public interface MTGPicturesCache extends MTGPlugin {

	static final Logger logger = MTGLogger.getLogger(MTGPicturesCache.class);

	public BufferedImage getPic(MagicCard mc,MagicEdition ed);
	public void put(BufferedImage im,MagicCard mc,MagicEdition ed) throws Exception;
	

}
