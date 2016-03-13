package br.unip.aps.view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import br.unip.aps.logic.Cliente;
import br.unip.aps.logic.Utils;

public class Form {

	private static Cliente Cliente;
	
	private static JFrame frmChat;
	
	private static JTextPane boxSaida;
	private static JScrollPane scrollPane_boxSaida;
	
	private static JTextPane boxEntrada;
	private static JScrollPane scrollPane_boxEntrada;
	
	private static JLabel emoticon_01;
	private static JLabel emoticon_02;
	private static JLabel emoticon_03;
	private static JLabel emoticon_04;
	private static JLabel emoticon_05;
	private static JLabel emoticon_06;
	private static JLabel emoticon_07;
	private static JLabel emoticon_08;
	private static JLabel emoticon_09;
	private static JLabel emoticon_10;
	private static JLabel emoticon_11;
	private static JLabel emoticon_12;
	private static JLabel emoticon_13;
	private static JLabel emoticon_14;
	private static JLabel emoticon_15;
	private static JLabel emoticon_16;
	private static JLabel emoticon_17;
	private static JLabel emoticon_18;
	
	private static JTextField txtServidor;
	private static JTextField txtPorta;
	private static JTextField txtNome;	
	
	private static JCheckBox chkboxSalvar;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					initializea();	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public static void initializea() {
		
		frmChat = new JFrame();
		frmChat.getContentPane().setBackground(Color.WHITE);
		frmChat.setTitle("Configuração");
		frmChat.setResizable(false);
		frmChat.setBounds(100, 100, 481, 392);
		frmChat.setLocationRelativeTo(null);  
		frmChat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmChat.setVisible(true);

		frmChat.getContentPane().setLayout(null);
		
		JLabel lblServidor = new JLabel("Servidor:");
		lblServidor.setBounds(127, 109, 56, 25);
		frmChat.getContentPane().add(lblServidor);
		
		JLabel lblPorta = new JLabel("Porta:");
		lblPorta.setBounds(127, 145, 56, 25);
		frmChat.getContentPane().add(lblPorta);
		
		JLabel lblNome = new JLabel("Nome:");
		lblNome.setBounds(127, 183, 56, 25);
		frmChat.getContentPane().add(lblNome);
		
		JLabel unip = new JLabel("");
		unip.setIcon(new ImageIcon(Form.class.getResource("/br/unip/aps/images/unip.png")));
		unip.setBounds(127, -3, 230, 87);
		frmChat.getContentPane().add(unip);
		
		txtServidor = new JTextField();
		txtServidor.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (!Character.isDigit(e.getKeyChar()) 
						&& (e.getKeyChar() != KeyEvent.VK_BACK_SPACE) 
						&& (e.getKeyChar() != KeyEvent.VK_PERIOD))  
	            e.consume();
			}
		});
		txtServidor.setBounds(193, 111, 154, 25);
		frmChat.getContentPane().add(txtServidor);
		txtServidor.setColumns(10);
		
		txtPorta = new JTextField();
		txtPorta.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (!Character.isDigit(e.getKeyChar()) 
						&& (e.getKeyChar() != KeyEvent.VK_BACK_SPACE))  
	            e.consume();
			}
		});
		txtPorta.setBounds(193, 145, 154, 25);
		txtPorta.setColumns(10);
		frmChat.getContentPane().add(txtPorta);
		
		txtNome = new JTextField();
		txtNome.setBounds(193, 183, 154, 25);
		txtNome.setColumns(10);
		frmChat.getContentPane().add(txtNome);
		
		chkboxSalvar = new JCheckBox("Salvar Informa\u00E7\u00F5es");
		chkboxSalvar.setForeground(Color.RED);
		chkboxSalvar.setBackground(Color.WHITE);
		chkboxSalvar.setBounds(189, 208, 148, 23);
		frmChat.getContentPane().add(chkboxSalvar);
		
		JButton btnNewButton = new JButton("Entrar");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(validate()) {
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								Cliente = new Cliente();
								String servidor = txtServidor.getText();
								int porta = Integer.parseInt(txtPorta.getText());
								String nome = txtNome.getText();
								if(chkboxSalvar.isSelected()) {
									Utils.setCookie(servidor + ";" + porta + ";" + nome);
								} else {
									Utils.deleteCookie();
								}
								Cliente.iniciar(servidor, porta, nome);				
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});	
					initializeaChat();
				} 			
			}
		});
		btnNewButton.setBounds(269, 233, 78, 31);
		frmChat.getContentPane().add(btnNewButton);
		
		String cookie = Utils.getCookie();
		if(cookie != null && cookie.trim().length() > 0) {
			String[] cookieSplit = cookie.split(";");
			txtServidor.setText(cookieSplit[0]);
			txtPorta.setText(cookieSplit[1]);
			txtNome.setText(cookieSplit[2]);
			chkboxSalvar.setSelected(true);						
		}	
	}
	
	private static boolean validate() {
		if((txtServidor.getText() != null && txtServidor.getText().length() > 0)
				&& (txtPorta.getText() != null && txtPorta.getText().length() > 0)
				&& (txtNome.getText() != null && txtNome.getText().length() > 0)) {
			return true;
		} else {
			Utils.alertaAviso("Favor preencher os filtros corretamente.");
			return false;
		}	
	}
	
	/**
	 * Initialize Chat the contents of the frame.
	 */
	public static void initializeaChat() {
		
		frmChat.setVisible(false);
		frmChat = new JFrame();
		frmChat.getContentPane().setBackground(Color.WHITE);
		frmChat.setTitle("Chat (" + txtNome.getText() + ") - SERVIDOR: " + txtServidor.getText() + ":" + txtPorta.getText());
		frmChat.setResizable(false);
		frmChat.setBounds(100, 100, 481, 392);
		frmChat.setLocationRelativeTo(null);  
		frmChat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmChat.getContentPane().setLayout(null);
		frmChat.setVisible(true);
		
		scrollPane_boxSaida = new JScrollPane();
		scrollPane_boxSaida.setBounds(10, 11, 456, 233);
		frmChat.getContentPane().add(scrollPane_boxSaida);	
		
		scrollPane_boxEntrada = new JScrollPane();
		scrollPane_boxEntrada.setBounds(10, 281, 369, 72);
		frmChat.getContentPane().add(scrollPane_boxEntrada);
		
		boxSaida = new JTextPane();
		boxSaida.setEditable(false);
		boxSaida.setContentType("text/html");
		scrollPane_boxSaida.setViewportView(boxSaida);
		
		boxEntrada = new JTextPane();
		boxEntrada.setContentType("text/html");
		scrollPane_boxEntrada.setViewportView(boxEntrada);
		
		final String emptyHtml = "<html><body></body></html>";
		try {
			boxEntrada.getEditorKit().read(new StringReader(emptyHtml), boxEntrada.getDocument(), 0);
			boxEntrada.setText("");
			boxSaida.getEditorKit().read(new StringReader(emptyHtml), boxSaida.getDocument(), 0);
			boxSaida.setText("");
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
		
		JButton btnEnviar = new JButton("Enviar");
		btnEnviar.setBounds(389, 281, 76, 72);
		btnEnviar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Cliente.enviarMensagem(boxEntrada.getText());
					boxEntrada.setText("");
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		});
		frmChat.getContentPane().add(btnEnviar);
		
		emoticon_01 = new JLabel();
		final URL imgEmoticon_01 = Form.class.getResource("/br/unip/aps/images/angry.png");
		emoticon_01.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				inserirEmoticon(imgEmoticon_01);
			}	
		});
		emoticon_01.setIcon(new ImageIcon(imgEmoticon_01));
		emoticon_01.setBounds(10, 255, 16, 16);
		frmChat.getContentPane().add(emoticon_01);
		
		emoticon_02 = new JLabel();
		final URL imgEmoticon_02 = Form.class.getResource("/br/unip/aps/images/biggrin.png");
		emoticon_02.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				inserirEmoticon(imgEmoticon_02);
			}
		});
		emoticon_02.setIcon(new ImageIcon(imgEmoticon_02));
		emoticon_02.setBounds(36, 255, 16, 16);
		frmChat.getContentPane().add(emoticon_02);
		
		emoticon_03 = new JLabel();
		final URL imgEmoticon_03 = Form.class.getResource("/br/unip/aps/images/blush.png");
		emoticon_03.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				inserirEmoticon(imgEmoticon_03);
			}
		});
		emoticon_03.setIcon(new ImageIcon(imgEmoticon_03));
		emoticon_03.setBounds(62, 255, 16, 16);
		frmChat.getContentPane().add(emoticon_03);
		
		emoticon_04 = new JLabel();
		final URL imgEmoticon_04 = Form.class.getResource("/br/unip/aps/images/coolglasses.png");
		emoticon_04.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				inserirEmoticon(imgEmoticon_04);
			}
		});
		emoticon_04.setIcon(new ImageIcon(imgEmoticon_04));
		emoticon_04.setBounds(88, 255, 16, 16);
		frmChat.getContentPane().add(emoticon_04);
		
		emoticon_05 = new JLabel();
		final URL imgEmoticon_05 = Form.class.getResource("/br/unip/aps/images/cry.png");
		emoticon_05.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				inserirEmoticon(imgEmoticon_05);
			}
		});
		emoticon_05.setIcon(new ImageIcon(imgEmoticon_05));
		emoticon_05.setBounds(114, 255, 16, 16);
		frmChat.getContentPane().add(emoticon_05);
		
		emoticon_06 = new JLabel();
		final URL imgEmoticon_06 = Form.class.getResource("/br/unip/aps/images/devil.png");
		emoticon_06.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				inserirEmoticon(imgEmoticon_06);
			}
		});
		emoticon_06.setIcon(new ImageIcon(imgEmoticon_06));
		emoticon_06.setBounds(140, 255, 16, 16);
		frmChat.getContentPane().add(emoticon_06);
		
		emoticon_07 = new JLabel();
		final URL imgEmoticon_07 = Form.class.getResource("/br/unip/aps/images/frowning.png");
		emoticon_07.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				inserirEmoticon(imgEmoticon_07);
			}
		});
		emoticon_07.setIcon(new ImageIcon(imgEmoticon_07));
		emoticon_07.setBounds(166, 255, 16, 16);
		frmChat.getContentPane().add(emoticon_07);
		
		emoticon_08 = new JLabel();
		final URL imgEmoticon_08 = Form.class.getResource("/br/unip/aps/images/smile.png");
		emoticon_08.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				inserirEmoticon(imgEmoticon_08);
			}
		});
		emoticon_08.setIcon(new ImageIcon(imgEmoticon_08));
		emoticon_08.setBounds(192, 254, 16, 16);
		frmChat.getContentPane().add(emoticon_08);
		
		emoticon_09 = new JLabel();
		final URL imgEmoticon_09 = Form.class.getResource("/br/unip/aps/images/stare.png");
		emoticon_09.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				inserirEmoticon(imgEmoticon_09);
			}
		});
		emoticon_09.setIcon(new ImageIcon(imgEmoticon_09));
		emoticon_09.setBounds(218, 255, 16, 16);
		frmChat.getContentPane().add(emoticon_09);
		
		emoticon_10 = new JLabel();
		final URL imgEmoticon_10 = Form.class.getResource("/br/unip/aps/images/tongue.png");
		emoticon_10.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				inserirEmoticon(imgEmoticon_10);
			}
		});
		emoticon_10.setIcon(new ImageIcon(imgEmoticon_10));
		emoticon_10.setBounds(244, 255, 16, 16);
		frmChat.getContentPane().add(emoticon_10);
		
		emoticon_11 = new JLabel();
		final URL imgEmoticon_11 = Form.class.getResource("/br/unip/aps/images/unhappy.png");
		emoticon_11.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				inserirEmoticon(imgEmoticon_11);
			}
		});
		emoticon_11.setIcon(new ImageIcon(imgEmoticon_11));
		emoticon_11.setBounds(270, 255, 16, 16);
		frmChat.getContentPane().add(emoticon_11);
		
		emoticon_12 = new JLabel();
		final URL imgEmoticon_12 = Form.class.getResource("/br/unip/aps/images/wink.png");
		emoticon_12.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				inserirEmoticon(imgEmoticon_12);
			}
		});
		emoticon_12.setIcon(new ImageIcon(imgEmoticon_12));
		emoticon_12.setBounds(296, 255, 16, 16);
		frmChat.getContentPane().add(emoticon_12);
		
		emoticon_13 = new JLabel();
		final URL imgEmoticon_13 = Form.class.getResource("/br/unip/aps/images/oh.png");
		emoticon_13.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				inserirEmoticon(imgEmoticon_13);
			}
		});
		emoticon_13.setIcon(new ImageIcon(imgEmoticon_13));
		emoticon_13.setBounds(322, 255, 16, 16);
		frmChat.getContentPane().add(emoticon_13);
		
		emoticon_14 = new JLabel();
		final URL imgEmoticon_14 = Form.class.getResource("/br/unip/aps/images/beer.png");
		emoticon_14.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				inserirEmoticon(imgEmoticon_14);
			}
		});
		emoticon_14.setIcon(new ImageIcon(imgEmoticon_14));
		emoticon_14.setBounds(348, 255, 16, 16);
		frmChat.getContentPane().add(emoticon_14);
		
		emoticon_15 = new JLabel();
		final URL imgEmoticon_15 = Form.class.getResource("/br/unip/aps/images/brheart.png");
		emoticon_15.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				inserirEmoticon(imgEmoticon_15);
			}
		});
		emoticon_15.setIcon(new ImageIcon(imgEmoticon_15));
		emoticon_15.setBounds(374, 255, 16, 16);
		frmChat.getContentPane().add(emoticon_15);
		
		emoticon_16 = new JLabel();
		final URL imgEmoticon_16 = Form.class.getResource("/br/unip/aps/images/heart.png");
		emoticon_16.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				inserirEmoticon(imgEmoticon_16);
			}
		});
		emoticon_16.setIcon(new ImageIcon(imgEmoticon_16));
		emoticon_16.setBounds(400, 255, 16, 16);
		frmChat.getContentPane().add(emoticon_16);
		
		emoticon_17 = new JLabel();
		final URL imgEmoticon_17 = Form.class.getResource("/br/unip/aps/images/music.png");
		emoticon_17.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				inserirEmoticon(imgEmoticon_17);
			}
		});
		emoticon_17.setIcon(new ImageIcon(imgEmoticon_17));
		emoticon_17.setBounds(425, 255, 16, 16);
		frmChat.getContentPane().add(emoticon_17);
		
		emoticon_18 = new JLabel();
		final URL imgEmoticon_18 = Form.class.getResource("/br/unip/aps/images/mail.png");
		emoticon_18.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				inserirEmoticon(imgEmoticon_18);
			}
		});
		emoticon_18.setIcon(new ImageIcon(imgEmoticon_18));
		emoticon_18.setBounds(450, 255, 16, 16);
		frmChat.getContentPane().add(emoticon_18);
		
	}
	
	private static void inserirEmoticon(final URL imgEmoticon) {
		int caretPosition = boxEntrada.getCaretPosition();
		HTMLEditorKit editor = (HTMLEditorKit) boxEntrada.getEditorKit();
		HTMLDocument document = (HTMLDocument) boxEntrada.getDocument();
		String html = "<span><img src =\"" + imgEmoticon + "\"></span>";
		try {
			editor.insertHTML(document, caretPosition, html, 0, 0, HTML.Tag.SPAN);
		} catch (BadLocationException e2) {
			e2.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}

	public static Cliente getCliente() {
		return Cliente;
	}

	public static void setCliente(Cliente Cliente) {
		Form.Cliente = Cliente;
	}

	public static JFrame getFrmChat() {
		return frmChat;
	}

	public static void setFrmChat(JFrame frmChat) {
		Form.frmChat = frmChat;
	}

	public static JTextPane getBoxSaida() {
		return boxSaida;
	}

	public static void setBoxSaida(JTextPane boxSaida) {
		Form.boxSaida = boxSaida;
	}

	public static JScrollPane getScrollPane_boxSaida() {
		return scrollPane_boxSaida;
	}

	public static void setScrollPane_boxSaida(JScrollPane scrollPane_boxSaida) {
		Form.scrollPane_boxSaida = scrollPane_boxSaida;
	}

	public static JTextPane getBoxEntrada() {
		return boxEntrada;
	}

	public static void setBoxEntrada(JTextPane boxEntrada) {
		Form.boxEntrada = boxEntrada;
	}

	public static JScrollPane getScrollPane_boxEntrada() {
		return scrollPane_boxEntrada;
	}

	public static void setScrollPane_boxEntrada(JScrollPane scrollPane_boxEntrada) {
		Form.scrollPane_boxEntrada = scrollPane_boxEntrada;
	}

	public static JTextField getTxtServidor() {
		return txtServidor;
	}

	public static void setTxtServidor(JTextField txtServidor) {
		Form.txtServidor = txtServidor;
	}

	public static JTextField getTxtPorta() {
		return txtPorta;
	}

	public static void setTxtPorta(JTextField txtPorta) {
		Form.txtPorta = txtPorta;
	}

	public static JTextField getTxtNome() {
		return txtNome;
	}

	public static void setTxtNome(JTextField txtNome) {
		Form.txtNome = txtNome;
	}

	public static JCheckBox getChkboxSalvar() {
		return chkboxSalvar;
	}

	public static void setChkboxSalvar(JCheckBox chkboxSalvar) {
		Form.chkboxSalvar = chkboxSalvar;
	}
}
