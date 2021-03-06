package org.magic.gui.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;
import org.magic.api.beans.CardDominance;
import org.magic.api.beans.MTGFormat;
import org.magic.services.MTGControler;
import org.magic.services.MTGLogger;

public class CardDominanceTableModel extends DefaultTableModel {

	static final String[] columns = new String[] { "CARD",
			"POSITION",
			"PC_DOMINANCE",
			"PC_DECKS",
			"PLAYERS" };
	private static final long serialVersionUID = 1L;
	private transient Logger logger = MTGLogger.getLogger(this.getClass());
	private transient List<CardDominance> list;

	public CardDominanceTableModel() {
		list = new ArrayList<>();
	}

	public void init(MTGFormat f, String filter) {
		try {
			list = MTGControler.getInstance().getEnabledDashBoard().getBestCards(f, filter);
		} catch (IOException e) {
			logger.error(e);
		}
	}

	@Override
	public int getColumnCount() {
		return columns.length;
	}

	@Override
	public int getRowCount() {
		if (list != null)
			return list.size();
		return 0;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return CardDominance.class;
		case 1:
			return Integer.class;
		case 2:
			return Double.class;
		case 3:
			return Double.class;
		case 4:
			return Double.class;
		default:
			return super.getColumnClass(columnIndex);
		}

	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	@Override
	public String getColumnName(int column) {
		return MTGControler.getInstance().getLangService().getCapitalize(columns[column]);
	}

	@Override
	public Object getValueAt(int row, int column) {

		switch (column) {
		case 0:
			return list.get(row);
		case 1:
			return list.get(row).getPosition();
		case 2:
			return list.get(row).getDominance();
		case 3:
			return list.get(row).getDecksPercent();
		case 4:
			return list.get(row).getPlayers();
		default:
			return "";
		}
	}

}
