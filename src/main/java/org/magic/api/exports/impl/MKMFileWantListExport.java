package org.magic.api.exports.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.magic.api.beans.MagicCard;
import org.magic.api.beans.MagicDeck;
import org.magic.api.interfaces.abstracts.AbstractCardExport;
import org.magic.services.MTGControler;

public class MKMFileWantListExport extends AbstractCardExport {


	@Override
	public MagicDeck importDeck(File f) throws IOException {

		try (BufferedReader read = new BufferedReader(new FileReader(f))) {
			MagicDeck deck = new MagicDeck();
			deck.setName(f.getName().substring(0, f.getName().indexOf('.')));

			String line = read.readLine();

			while (line != null) {
				int qte = Integer.parseInt(line.substring(0, line.indexOf(' ')));
				String name = line.substring(line.indexOf(' '), line.indexOf('('));

				deck.getMap().put(MTGControler.getInstance().getEnabledCardsProviders()
						.searchCardByName( name.trim(), null, true).get(0), qte);
				line = read.readLine();
			}
			return deck;
		}

	}

	@Override
	public String getFileExtension() {
		return ".txt";
	}

	@Override
	public void export(MagicDeck deck, File dest) throws IOException {

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(dest))) {
			for (MagicCard mc : deck.getMap().keySet()) {
				StringBuilder temp = new StringBuilder();
				temp.append(deck.getMap().get(mc)).append(" ").append(mc.getName()).append(" (")
						.append(mc.getCurrentSet().getSet()).append(")");
				bw.write(temp.toString() + "\n");
			}
			for (MagicCard mc : deck.getMapSideBoard().keySet()) {
				StringBuilder temp = new StringBuilder();

				if (mc.getCurrentSet().getMkmName() != null)
					temp.append(deck.getMapSideBoard().get(mc)).append(" ").append(mc.getName()).append(" (")
							.append(mc.getCurrentSet().getMkmName()).append(")");
				else
					temp.append(deck.getMapSideBoard().get(mc)).append(" ").append(mc.getName()).append(" (")
							.append(mc.getCurrentSet().getSet()).append(")");

				bw.write(temp.toString() + "\n");
			}
		}
	}

	@Override
	public String getName() {
		return "MKM File WantList";
	}

	@Override
	public Icon getIcon() {
		return new ImageIcon(MKMFileWantListExport.class.getResource("/icons/plugins/magiccardmarket.png"));
	}

}
