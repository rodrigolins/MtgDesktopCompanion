package org.magic.gui.dashlet;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jdesktop.swingx.JXTable;
import org.magic.api.interfaces.abstracts.AbstractJDashlet;
import org.magic.gui.models.conf.MapTableModel;
import org.magic.services.MTGConstants;
import org.magic.services.MTGControler;

public class IndexationDashlet extends AbstractJDashlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MapTableModel<String, Long> indexModel;

	private JComboBox<String> cboField;

	@Override
	public Icon getIcon() {
		return MTGConstants.ICON_TAB_ANALYSE;
	}
	
	public void initGUI() {
	
		JPanel panneauHaut = new JPanel();
		getContentPane().add(panneauHaut, BorderLayout.NORTH);

		cboField = new JComboBox<>(new DefaultComboBoxModel<>(MTGControler.getInstance().getEnabledCardIndexer().listFields()));
		panneauHaut.add(cboField);

		

		indexModel = new MapTableModel<>();
		indexModel.setColumnNameAt(0, "Term");
		indexModel.setColumnNameAt(1, "Occurences");
		JXTable table = new JXTable(indexModel);
		
		getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
		
		cboField.addItemListener(ie -> {
			if (ie.getStateChange() == ItemEvent.SELECTED) {
				init();
			}
		});

		if (getProperties().size() > 0) {
			Rectangle r = new Rectangle((int) Double.parseDouble(getString("x")),
					(int) Double.parseDouble(getString("y")), (int) Double.parseDouble(getString("w")),
					(int) Double.parseDouble(getString("h")));

			setBounds(r);
		}

		setVisible(true);

	}

	public void init() {
		indexModel.init(MTGControler.getInstance().getEnabledCardIndexer().terms(cboField.getSelectedItem().toString()));
	
	}

	@Override
	public String getName() {
		return "Magic Indexation Stats";
	}

}
