package br.unip.aps.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringWriter;
import java.net.Socket;
import java.net.URL;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLWriter;

import br.unip.aps.view.Form;

public class Cliente extends Thread {

	private Socket conexao;
	private PrintStream saida;
	
	public Cliente(Socket socket) {
		this.conexao = socket;
	}
	
	public Cliente() {

	}

	@SuppressWarnings("static-access")
	public void iniciar(String servidor, int porta, String nome) {
		try {
			Socket socket = new Socket(servidor, porta);
			saida = new PrintStream(socket.getOutputStream());
			saida.println(nome);
			Thread thread = new Cliente(socket);
			try {
				thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			thread.start();
		} catch (IOException ioe) {
			Utils.alertaErro("Erro de conexao. IOException: " + ioe);
			Form.getFrmChat().setVisible(false);
			Form.initializea();
		}
	}

	public void enviarMensagem(String mensagem)	throws IOException {
		StringWriter writer = new StringWriter();
        HTMLDocument doc = (HTMLDocument) Form.getBoxEntrada().getStyledDocument();
        HTMLWriter htmlWriter = new OnlyBodyHTMLWriter(writer, doc);			   
        try {
			htmlWriter.write();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
		if(writer.toString() != null && writer.toString().trim().length() > 0) {
			saida.println(mensagem);
		}		
	}

	public void run() {
		try {
			BufferedReader entrada = new BufferedReader(new InputStreamReader(this.conexao.getInputStream()));
			String mensagem = "";
			while (true) {
				mensagem = entrada.readLine();
				if (mensagem != null && !mensagem.equals("COD_322_USER")) {
					String mensagemConcat = "";
					while (entrada.ready()) {
						mensagemConcat += "\n" + entrada.readLine();
					}
					JTextPane pane = new JTextPane(); 
					
					String tagImgInicio = "<img src=\"";
					String tagImgFim = "\">";
					int indexTagImg = mensagemConcat.indexOf(tagImgInicio);				
					if(indexTagImg > 0) {
						for (int i = 0; i < mensagemConcat.length(); i++) {
							if(mensagemConcat.substring(indexTagImg+i, indexTagImg+i+tagImgFim.length()).equals(tagImgFim)) {								
								String path = mensagemConcat.substring(indexTagImg+tagImgInicio.length(),indexTagImg+i);
								String pathSystem = "/br/unip/aps/images/";
								for (int j = 0; j < path.length(); j++) {
									if(path.substring(j, j+pathSystem.length()).equals(pathSystem)) {	
										URL url = Form.class.getResource(path.substring(j, path.length()));	
										// CASO FOR TESTAR PELA IDE, ALTERAR: 
										// mensagemConcat = mensagemConcat.replaceAll(mensagemConcat.substring(indexTagImg+tagImgInicio.length(),indexTagImg+i), "file:" + url.getFile());
										mensagemConcat = mensagemConcat.replaceAll(mensagemConcat.substring(indexTagImg+tagImgInicio.length(),indexTagImg+i), "jar:" + url.getFile());
										j = path.length();
										i = mensagemConcat.length();
									}
								}
								
							}
						}
					}
					
					pane.setContentType("text/html");
					pane.setText(mensagemConcat);
					StringWriter writer = new StringWriter();
			        HTMLDocument doc = (HTMLDocument) pane.getStyledDocument();
			        HTMLWriter htmlWriter = new OnlyBodyHTMLWriter(writer, doc);			   
			        try {	
						htmlWriter.write();
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}  
			        HTMLEditorKit kit = (HTMLEditorKit) Form.getBoxSaida().getEditorKit();
				    HTMLDocument docBoxSaida = (HTMLDocument) Form.getBoxSaida().getDocument();	
				    try {	   
				    	if(!writer.toString().contains("<p style=\"margin-top: 0\">")) {
				    		if(writer.toString().contains("<img src")) {
				    			kit.insertHTML(docBoxSaida, docBoxSaida.getLength(), "<span>" + mensagem + "</span><br/>" + writer.toString(), 0, 0, null);
				    		} else {
				    			kit.insertHTML(docBoxSaida, docBoxSaida.getLength(), "<span>" + mensagem + "</span><br/><span>" + mensagemConcat + "</span>", 0, 0, null);
				    		}    		
				    	} else if(!mensagem.contains("diz") && mensagem != null && mensagem.length() > 0) {
				    		kit.insertHTML(docBoxSaida, docBoxSaida.getLength(), "<span>" + mensagem + "</span>", 0, 0, null);
				    	}
				    } catch (BadLocationException e2) {
						e2.printStackTrace();
					} catch (IOException e2) {
						e2.printStackTrace();
					}
				    Form.getBoxSaida().setCaretPosition(docBoxSaida.getLength()); 
				} else {
					Utils.alertaAviso("Nome já utilizado, favor utilizar outro nome.");
					Form.getFrmChat().setVisible(false);
					Form.initializea();
					return;
				}
			}
		} catch (IOException ioe) {
			Utils.alertaErro("Erro no cliente. IOException: " + ioe);
			Form.getFrmChat().setVisible(false);
			Form.initializea();
		}
	}
	
}