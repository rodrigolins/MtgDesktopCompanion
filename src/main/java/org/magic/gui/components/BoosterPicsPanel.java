package org.magic.gui.components;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;

import org.apache.log4j.Logger;
import org.magic.api.beans.MagicEdition;
import org.magic.services.MTGLogger;
import org.magic.services.ThreadManager;
import org.magic.services.extra.BoosterPicturesProvider;
import org.magic.tools.ImageUtils;
import org.w3c.dom.NodeList;

public class BoosterPicsPanel extends JTabbedPane {
	
	private transient BoosterPicturesProvider provider;
	private static final long serialVersionUID = 1L;
	static Logger logger = MTGLogger.getLogger(BoosterPicsPanel.class);

	
	public BoosterPicsPanel() {
		setLayout(new BorderLayout(0, 0));
		provider = new BoosterPicturesProvider();
	}
	
	public void setEdition(MagicEdition ed) {
		removeAll();
		revalidate();
		if(ed!=null)
			ThreadManager.getInstance().execute(() -> adds(ed,provider.getBoostersUrl(ed)),"load booster pic for " + ed);
	}

	private void adds(MagicEdition ed, NodeList boostersUrl) {
		for(int i =0; i<boostersUrl.getLength();i++)
		{
			try {
				addTab(String.valueOf(i+1), new JLabel(new ImageIcon(resizeBooster(provider.getBoosterFor(ed, i)))));
			}catch(Exception e)
			{
				logger.error(e);
			}
		}
	}

	private Image resizeBooster(BufferedImage boosterFor) {
		return ImageUtils.resize(ImageUtils.trimAlpha(boosterFor), 450, 254);
		
	}

}
