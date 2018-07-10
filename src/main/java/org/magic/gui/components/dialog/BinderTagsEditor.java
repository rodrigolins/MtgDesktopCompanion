package org.magic.gui.components.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.apache.log4j.Logger;
import org.beta.BinderTagsMaker;
import org.magic.api.beans.MagicEdition;
import org.magic.gui.renderer.MagicEditionIconListRenderer;
import org.magic.services.MTGConstants;
import org.magic.services.MTGControler;
import org.magic.services.MTGLogger;
import org.magic.services.extra.BoosterPicturesProvider.LOGO;
import org.magic.tools.ImageUtils;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JList;

public class BinderTagsEditor extends JDialog {
	
	private transient Logger logger = MTGLogger.getLogger(this.getClass());
	private JPanel previewPanel;
	private transient BinderTagsMaker tagMaker;
	private transient BufferedImage img;
	private JComboBox<LOGO> cboLogo;
	private JCheckBox chckbxBorder;
	private JCheckBox chckbxAddHeader;
	private JButton btnBackgroundColor ;
	private JButton btnSave;
	private JPanel panel1;
	private JPanel leftPanel;
	private JButton btnNew;
	private JPanel panelInterspace;
	private JLabel lblInterSpace;
	private JSpinner spinSpace;
	private int dpi=300;
	private JScrollPane scrollListEdition;
	private JList<MagicEdition> listEditions;
	
	
	public static void main(String[] args) {
		MTGControler.getInstance().getEnabledCardsProviders().init();
		new BinderTagsEditor().setVisible(true);
	}
	
	
	public void updateInfo() {
		img = tagMaker.generate();
		previewPanel.revalidate();
		previewPanel.repaint();
	}
	
	
	private void init()
	{
		Dimension d = new Dimension(567,2173);
		double mmW = ImageUtils.toMM(d.getWidth(), dpi);
 		double mmH = ImageUtils.toMM(d.getHeight(), dpi);
 		String res = JOptionPane.showInputDialog("Dimension in mm", mmW+"x"+mmH);
 		String result[] = res.split("x");
 		
 		d = new Dimension((int)ImageUtils.toPX(Double.parseDouble(result[0]), dpi), (int)ImageUtils.toPX(Double.parseDouble(result[1]), dpi));
		tagMaker = new BinderTagsMaker(d);
	}
	
	public BinderTagsEditor() {
		
		init();
		
		setTitle("Binder Tags Editor");

		
		leftPanel = new JPanel();
		JPanel editorPanel = new JPanel();

		
		
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		List<MagicEdition> list = new ArrayList<>();
		try {
			list = MTGControler.getInstance().getEnabledCardsProviders().loadEditions();
		} catch (IOException e2) {
			logger.error(e2);
		}
		
		previewPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if(img!=null)
					g.drawImage(img, 0, 0, null);
					
				this.revalidate();
			}
		};
		previewPanel.setPreferredSize(new Dimension(576, 2173));
		
		previewPanel.setBackground(Color.white);
		
		
		JScrollPane scrollPane = new JScrollPane(previewPanel);
		scrollPane.setPreferredSize(new Dimension(250, 250));
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		getContentPane().add(leftPanel, BorderLayout.WEST);
		leftPanel.setLayout(new BorderLayout(0, 0));
		leftPanel.add(editorPanel, BorderLayout.NORTH);
		GridBagLayout gbleditorPanel = new GridBagLayout();
		gbleditorPanel.columnWidths = new int[]{0, 275, 0, 0};
		gbleditorPanel.rowHeights = new int[]{0, 249, 0, 0, 0, 0};
		gbleditorPanel.columnWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbleditorPanel.rowWeights = new double[]{1.0, 1.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
		editorPanel.setLayout(gbleditorPanel);
		listEditions = new JList<>();
		
		scrollListEdition = new JScrollPane(listEditions);
		GridBagConstraints gbcscrollPane1 = new GridBagConstraints();
		gbcscrollPane1.insets = new Insets(0, 0, 5, 5);
		gbcscrollPane1.fill = GridBagConstraints.BOTH;
		gbcscrollPane1.gridx = 1;
		gbcscrollPane1.gridy = 1;
		editorPanel.add(scrollListEdition, gbcscrollPane1);
		
		
		
		panel1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel1.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		GridBagConstraints gbcpanel1 = new GridBagConstraints();
		gbcpanel1.insets = new Insets(0, 0, 5, 5);
		gbcpanel1.fill = GridBagConstraints.BOTH;
		gbcpanel1.gridx = 1;
		gbcpanel1.gridy = 2;
		editorPanel.add(panel1, gbcpanel1);
		
		chckbxAddHeader = new JCheckBox("add Header");
		panel1.add(chckbxAddHeader);
		
		cboLogo = new JComboBox<>(new DefaultComboBoxModel<>(LOGO.values()));
		panel1.add(cboLogo);
		
		panelInterspace = new JPanel();
		FlowLayout flowLayout1 = (FlowLayout) panelInterspace.getLayout();
		flowLayout1.setAlignment(FlowLayout.LEFT);
		GridBagConstraints gbcPanelInterSpace = new GridBagConstraints();
		gbcPanelInterSpace.insets = new Insets(0, 0, 0, 5);
		gbcPanelInterSpace.fill = GridBagConstraints.BOTH;
		gbcPanelInterSpace.gridx = 1;
		gbcPanelInterSpace.gridy = 4;
		editorPanel.add(panelInterspace, gbcPanelInterSpace);
		
		lblInterSpace = new JLabel("Space :");
		panelInterspace.add(lblInterSpace);
		
		spinSpace = new JSpinner();
		
		panelInterspace.add(spinSpace);
		
		btnBackgroundColor = new JButton(MTGConstants.ICON_GAME_COLOR);
		panelInterspace.add(btnBackgroundColor);
		
		chckbxBorder = new JCheckBox("Border");
		panelInterspace.add(chckbxBorder);
		JPanel commandsPanel = new JPanel();
		leftPanel.add(commandsPanel, BorderLayout.SOUTH);
		
		btnNew = new JButton(MTGConstants.ICON_NEW);
		
		commandsPanel.add(btnNew);
		
		btnSave = new JButton(MTGConstants.ICON_SAVE);
		commandsPanel.add(btnSave);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		initActions();
		
		pack();
		

	}


	private void initActions() {
		
		btnBackgroundColor.addActionListener(ae->{
			Color selected = JColorChooser.showDialog(null, "Color Selection", Color.WHITE);
			tagMaker.setBackColor(selected);
			updateInfo();
		});
	
		chckbxAddHeader.addActionListener(ae->{
			if(!chckbxAddHeader.isSelected())
			{
				tagMaker.setLogo(null);
			}
			else
			{
				tagMaker.setLogo((LOGO)cboLogo.getSelectedItem());
			}

		});
		
		cboLogo.addItemListener(e-> {
				if(chckbxAddHeader.isSelected())
					tagMaker.setLogo((LOGO)cboLogo.getSelectedItem());
				
				updateInfo();
		});
		
		chckbxBorder.addActionListener(e->{
			tagMaker.setBorder(chckbxBorder.isSelected());
			updateInfo();	
		});
		
		btnNew.addActionListener(e->{
			tagMaker.clear();
			updateInfo();
		});
		
		spinSpace.addChangeListener(ce ->{
			tagMaker.setSpace((int)spinSpace.getValue());
			updateInfo();
		});
		
		btnSave.addActionListener(e->{
			JFileChooser choose = new JFileChooser();
						 choose.showSaveDialog(null);
			File f = choose.getSelectedFile();
			try {
				ImageUtils.saveImageInPng(img, f, dpi);
			} catch (IOException e1) {
				logger.error("Error saving image", e1);
			}
						 
		});
		
		
	}
	

	

}
