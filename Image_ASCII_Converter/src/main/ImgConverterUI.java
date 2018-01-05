
package main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ImgConverterUI extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private Converter converter;
	private JButton load, convert, save, loadASCIIList;
	private JTextField scale;
	private JLabel optional, info;

	public ImgConverterUI()
	{	
		converter = new Converter();
		init();
	}
	
	
	private void init()
	{
		this.setPreferredSize(new Dimension(300,1000));
		this.setTitle("Image to ASCII Art");
		this.setIconImage(Toolkit.getDefaultToolkit()
		.createImage(
				 //System.getProperty("user.dir") + File.separator + "imgs" + File.separator + "Personal_Icon.png")
				ImgConverterUI.class.getResource("Personal_Icon.png")
				));
		this.getContentPane().setBackground(new Color(166, 242, 235));
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(),BoxLayout.Y_AXIS));
		
		this.compInitialize();
		
		this.getContentPane().add(Box.createRigidArea(new Dimension(40,40)));
		this.getContentPane().add(load);
		this.getContentPane().add(Box.createRigidArea(new Dimension(0,30)));
		this.getContentPane().add(convert);
		this.getContentPane().add(Box.createRigidArea(new Dimension(0,30)));
		this.getContentPane().add(save);
		this.getContentPane().add(Box.createRigidArea(new Dimension(0,80)));
		this.getContentPane().add(optional);
		this.getContentPane().add(Box.createRigidArea(new Dimension(0,5)));
		this.getContentPane().add(loadASCIIList);
		this.getContentPane().add(Box.createRigidArea(new Dimension(0,40)));
		this.getContentPane().add(info);
		
		JPanel panel = (JPanel)this.getGlassPane();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints cs = new GridBagConstraints();
		cs.fill = GridBagConstraints.BOTH;
		cs.weightx = 1;
		cs.weighty = 1;
		cs.insets = new Insets(225,65,670,70);
		
		panel.add(scale,cs);
		panel.setVisible(true);

		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	 
	private void compInitialize()
	{
		
		load = new JButton();
		convert = new JButton();
		scale = new JTextField("Enter scale factor");	
		save = new JButton();
		loadASCIIList = new JButton();
		
		scale.setBackground(new Color(160, 240, 230));
		scale.setBorder(null);
		scale.setFont(new Font(Font.SANS_SERIF,Font.BOLD, 19));
		scale.setHorizontalAlignment(JTextField.CENTER);
		scale.setForeground(new Color(123, 173, 170));
		
	
		scale.addFocusListener(new FocusListener() {
			
			public void focusLost(FocusEvent e) {
				
				if (scale.getText().equals(""))
					scale.setText("Enter scale factor"); 
			}
			
			public void focusGained(FocusEvent e) {
				if (scale.getText().equals("Enter scale factor"))
						scale.setText(""); 
			}

			});   
		
		
		optional = new JLabel(" Optional:");
		optional.setFont(new Font(Font.SANS_SERIF,Font.BOLD, 40));
		optional.setForeground(new Color(123, 173, 170));
		info = new JLabel("                                          @ Powered by Junyao Y.");
		info.setFont(new Font(Font.SANS_SERIF,Font.BOLD, 10));
		info.setForeground(new Color(123, 173, 170));
		
		buttonLook(load, "Load_Button.png", "Load_Button_Activate.png");
		buttonLook(convert, "Convert_Button.png", "Convert_Button_Activate.png");
		buttonLook(save, "Save_Button.png", "Save_Button_Activate.png");
		buttonLook(loadASCIIList, "ASCII_Button.png", "ASCII_Button_Activate.png");
		
		
		UIListener listener = new UIListener();
		
		load.addActionListener(listener);
		convert.addActionListener(listener);
		save.addActionListener(listener);
		loadASCIIList.addActionListener(listener);
	}
	
	
	//helper
	private void buttonLook(JButton button, String imageName, String pressedImageName)
	{
		
		button.setFocusPainted(false);
		button.setBorder(null);
		button.setContentAreaFilled(false);
		button.setIcon(new ImageIcon(
				//System.getProperty("user.dir") + File.separator + "imgs" + File.separator + imageName
				ImgConverterUI.class.getResource(imageName)
				));
		button.setPressedIcon(new ImageIcon(
				//System.getProperty("user.dir") + File.separator + "imgs" + File.separator + pressedImageName
				ImgConverterUI.class.getResource(pressedImageName)
				));
	}
	
	
	
	class UIListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
			if (e.getSource() == load)
			{
				FileDialog openchooser = new FileDialog(ImgConverterUI.this, "Choose Image",FileDialog.LOAD);
				
				openchooser.setVisible(true);
				
				String filename = openchooser.getFile();
				
				if (filename != null) {	
					try {
							converter.setImage(
								converter.grayifyImage(
									converter.loadImage( openchooser.getDirectory() + File.separator + openchooser.getFile())
											)
											);	
					} catch (IOException ex) {
						
						ex.printStackTrace();
					}
				}
			}
			
			if (e.getSource() == convert)
			{
				converter.setASCIIArt(null);
				int scaleFactor = 0;
				try{
							
					 scaleFactor = Integer.parseInt(scale.getText());
					if ( scaleFactor == 0)
					{
						JOptionPane.showMessageDialog(ImgConverterUI.this, "Invalid Input, must enter non-zero integer.");
						return;
					}
				}catch(NumberFormatException ex)
				{
					JOptionPane.showMessageDialog(ImgConverterUI.this, "Invalid Input, must enter non-zero integer.");
					return;
				}
					converter.setScale(scaleFactor);
					
					if (converter.getImage() == null)
					{
						JOptionPane.showMessageDialog(ImgConverterUI.this, "Load Image before convert.");
						return;
					}
					converter.setASCIIArt(
							converter.convertASC(converter.getImage(), converter.getScale(), converter.getASCIIList())
							);
						
				
			}
			
			
			if (e.getSource() == save)
			{
				FileDialog savechooser = new FileDialog
						(ImgConverterUI.this, "Use .txt as extension",FileDialog.SAVE);
				savechooser.setVisible(true);
				String fileName = savechooser.getFile();
				
				if (fileName != null) {
					
					if (!(fileName.endsWith(".txt")))	fileName = fileName + ".txt";
		
					try {
					
						converter.saveASCII(converter.getASCIIArt(), savechooser.getDirectory() + File.separator + fileName);
						
					} catch (IOException e1) {
						
						e1.printStackTrace();
					}
				}

			}
			
			
			if (e.getSource() == loadASCIIList)
			{
				FileDialog openchooser = new FileDialog(ImgConverterUI.this, "Choose ASCIIList",FileDialog.LOAD);
				
				openchooser.setVisible(true);
				
				String filename = openchooser.getFile();
				
				if (filename != null) {	
					try {
							converter.setASCIIList(
								converter.loadASCIIList(( openchooser.getDirectory() + File.separator + openchooser.getFile()))
										);
										
					} catch (IOException ex) {
						
						ex.printStackTrace();
					}
				}
			}
 	
		}
		
	}
	
	
	
	
	public static void main(String [] args)
	{
		 SwingUtilities.invokeLater(new Runnable() {
	         public void run() {
	           new ImgConverterUI();
	         }
	      });
		 
		}
}
