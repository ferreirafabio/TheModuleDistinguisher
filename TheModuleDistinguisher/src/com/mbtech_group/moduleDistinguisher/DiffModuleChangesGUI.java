package com.mbtech_group.moduleDistinguisher;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.UIManager;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;

import javax.swing.JTextArea;
import java.awt.Font;

public class DiffModuleChangesGUI {

	private JFrame frmThemoduledistinguisher;
	private JTextField xlsPathPanel;
	private JLabel lblSelectBasis;
	private JTextField basisPathPanel;
	private JLabel lblSelectSonderstandExport;
	private JTextField sonderstandPathPanel;
	private JButton btnStart;
	private JButton basisPathbtn;
	private JButton sonderstandPathBtn;
	private static String xlsPath = null;
	private static String basisPath = null;
	private static String sonderstandPath = null;

	/**
	 * @wbp.nonvisual location=604,39
	 */
	private final JFileChooser fc = new JFileChooser();
	private JTextArea textArea;
	private JScrollPane scrollPane;
	private JLabel lblReport;

	/**
	 * @wbp.nonvisual location=219,129
	 */
	/**
	 * @wbp.nonvisual location=99,109
	 */

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager
							.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
					DiffModuleChangesGUI window = new DiffModuleChangesGUI();
					window.frmThemoduledistinguisher.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public DiffModuleChangesGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmThemoduledistinguisher = new JFrame();
		frmThemoduledistinguisher.setTitle("TheModuleDistinguisher");
		frmThemoduledistinguisher.setBounds(100, 100, 501, 465);
		frmThemoduledistinguisher
				.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		xlsPathPanel = new JTextField();
		xlsPathPanel.setColumns(10);

		JLabel lblNewLabel = new JLabel("Select Excel sheet");

		JButton xlsPathBtn = new JButton("...");
		xlsPathBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int returnVal = fc.showOpenDialog((Component) e.getSource());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					try {
						DiffModuleChangesGUI.xlsPath = file.toString();
						if (!(xlsPath.contains(".xls"))) {
							JOptionPane.showMessageDialog(
									(Component) e.getSource(), "No .xls file.",
									"Error", JOptionPane.ERROR_MESSAGE);
						} else {
							xlsPathPanel.setText(xlsPath);
						}
					} catch (Exception ex) {
						System.out.println("problem accessing file"
								+ file.getAbsolutePath());
					}
				}
			}
		});
		xlsPathBtn
				.setSelectedIcon(new ImageIcon(
						DiffModuleChangesGUI.class
								.getResource("/com/sun/java/swing/plaf/windows/icons/Directory.gif")));

		lblSelectBasis = new JLabel("Select basis export");

		basisPathPanel = new JTextField();
		basisPathPanel.setColumns(10);

		lblSelectSonderstandExport = new JLabel("Select Sonderstand export");

		sonderstandPathPanel = new JTextField();
		sonderstandPathPanel.setColumns(10);

		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (basisPath != null & sonderstandPath != null
						& xlsPath != null) {
					try {
						TheModuleDistinguisherController controller = new TheModuleDistinguisherController(
								basisPath, sonderstandPath, xlsPath);
						String log = controller.getLog();
						textArea.append(log);
					} catch (IOException d) {
						JOptionPane.showMessageDialog(
								(Component) e.getSource(), d.getMessage(),
								"Error", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog((Component) e.getSource(),
							"Select all files first.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		basisPathbtn = new JButton("...");
		basisPathbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showOpenDialog((Component) e.getSource());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					try {
						DiffModuleChangesGUI.basisPath = file.toString();
						if (!(basisPath.contains(".txt"))) {
							JOptionPane.showMessageDialog(
									(Component) e.getSource(), "No .txt file.",
									"Error", JOptionPane.ERROR_MESSAGE);
						} else {
							basisPathPanel.setText(basisPath);
						}

					} catch (Exception ex) {
						System.out.println("problem accessing file"
								+ file.getAbsolutePath());
					}
				}
			}
		});

		sonderstandPathBtn = new JButton("...");
		sonderstandPathBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showOpenDialog((Component) e.getSource());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					try {
						DiffModuleChangesGUI.sonderstandPath = file.toString();
						if (!(sonderstandPath.contains(".txt"))) {
							JOptionPane.showMessageDialog(
									(Component) e.getSource(), "No .txt file.",
									"Error", JOptionPane.ERROR_MESSAGE);
						} else {
							sonderstandPathPanel.setText(sonderstandPath);
						}

					} catch (Exception ex) {
						System.out.println("problem accessing file"
								+ file.getAbsolutePath());
					}
				}
			}
		});

		scrollPane = new JScrollPane();

		lblReport = new JLabel("Report");

		GroupLayout groupLayout = new GroupLayout(
				frmThemoduledistinguisher.getContentPane());
		groupLayout
				.setHorizontalGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.LEADING)
														.addGroup(
																Alignment.TRAILING,
																groupLayout
																		.createSequentialGroup()
																		.addGap(16)
																		.addGroup(
																				groupLayout
																						.createParallelGroup(
																								Alignment.TRAILING)
																						.addGroup(
																								groupLayout
																										.createSequentialGroup()
																										.addComponent(
																												lblSelectBasis,
																												GroupLayout.DEFAULT_SIZE,
																												402,
																												Short.MAX_VALUE)
																										.addGap(57))
																						.addGroup(
																								groupLayout
																										.createSequentialGroup()
																										.addGroup(
																												groupLayout
																														.createParallelGroup(
																																Alignment.TRAILING)
																														.addGroup(
																																groupLayout
																																		.createSequentialGroup()
																																		.addGroup(
																																				groupLayout
																																						.createParallelGroup(
																																								Alignment.TRAILING)
																																						.addComponent(
																																								basisPathPanel,
																																								Alignment.LEADING,
																																								GroupLayout.DEFAULT_SIZE,
																																								415,
																																								Short.MAX_VALUE)
																																						.addComponent(
																																								sonderstandPathPanel,
																																								Alignment.LEADING,
																																								GroupLayout.DEFAULT_SIZE,
																																								415,
																																								Short.MAX_VALUE))
																																		.addPreferredGap(
																																				ComponentPlacement.UNRELATED))
																														.addGroup(
																																groupLayout
																																		.createSequentialGroup()
																																		.addComponent(
																																				lblSelectSonderstandExport,
																																				GroupLayout.DEFAULT_SIZE,
																																				417,
																																				Short.MAX_VALUE)
																																		.addGap(8)))
																										.addGroup(
																												groupLayout
																														.createParallelGroup(
																																Alignment.LEADING)
																														.addComponent(
																																basisPathbtn,
																																GroupLayout.PREFERRED_SIZE,
																																34,
																																GroupLayout.PREFERRED_SIZE)
																														.addComponent(
																																sonderstandPathBtn,
																																GroupLayout.PREFERRED_SIZE,
																																34,
																																GroupLayout.PREFERRED_SIZE)))
																						.addGroup(
																								groupLayout
																										.createSequentialGroup()
																										.addComponent(
																												xlsPathPanel,
																												GroupLayout.DEFAULT_SIZE,
																												415,
																												Short.MAX_VALUE)
																										.addPreferredGap(
																												ComponentPlacement.UNRELATED)
																										.addComponent(
																												xlsPathBtn,
																												GroupLayout.PREFERRED_SIZE,
																												34,
																												GroupLayout.PREFERRED_SIZE))))
														.addGroup(
																Alignment.TRAILING,
																groupLayout
																		.createSequentialGroup()
																		.addGap(16)
																		.addComponent(
																				lblNewLabel,
																				GroupLayout.DEFAULT_SIZE,
																				459,
																				Short.MAX_VALUE))
														.addGroup(
																Alignment.TRAILING,
																groupLayout
																		.createSequentialGroup()
																		.addContainerGap(
																				16,
																				Short.MAX_VALUE)
																		.addComponent(
																				scrollPane,
																				GroupLayout.PREFERRED_SIZE,
																				417,
																				GroupLayout.PREFERRED_SIZE)
																		.addGap(42))
														.addGroup(
																Alignment.TRAILING,
																groupLayout
																		.createSequentialGroup()
																		.addContainerGap(
																				16,
																				Short.MAX_VALUE)
																		.addComponent(
																				lblReport,
																				GroupLayout.PREFERRED_SIZE,
																				459,
																				GroupLayout.PREFERRED_SIZE)))
										.addContainerGap())
						.addGroup(
								Alignment.TRAILING,
								groupLayout
										.createSequentialGroup()
										.addContainerGap(16, Short.MAX_VALUE)
										.addComponent(btnStart,
												GroupLayout.PREFERRED_SIZE, 71,
												GroupLayout.PREFERRED_SIZE)
										.addGap(398)));
		groupLayout
				.setVerticalGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addGap(19)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.TRAILING)
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addComponent(
																				lblSelectBasis)
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addComponent(
																				basisPathPanel,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE))
														.addComponent(
																basisPathbtn,
																GroupLayout.PREFERRED_SIZE,
																20,
																GroupLayout.PREFERRED_SIZE))
										.addGap(18)
										.addComponent(
												lblSelectSonderstandExport)
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																sonderstandPathPanel,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																sonderstandPathBtn,
																GroupLayout.PREFERRED_SIZE,
																20,
																GroupLayout.PREFERRED_SIZE))
										.addGap(20)
										.addComponent(lblNewLabel)
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.LEADING)
														.addComponent(
																xlsPathBtn,
																GroupLayout.PREFERRED_SIZE,
																20,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																xlsPathPanel,
																GroupLayout.PREFERRED_SIZE,
																20,
																GroupLayout.PREFERRED_SIZE))
										.addGap(18)
										.addComponent(btnStart,
												GroupLayout.PREFERRED_SIZE, 25,
												GroupLayout.PREFERRED_SIZE)
										.addGap(18)
										.addComponent(lblReport)
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addComponent(scrollPane,
												GroupLayout.PREFERRED_SIZE,
												152, GroupLayout.PREFERRED_SIZE)
										.addGap(17)));

		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
		scrollPane.setViewportView(textArea);
		frmThemoduledistinguisher.getContentPane().setLayout(groupLayout);
	}
}
