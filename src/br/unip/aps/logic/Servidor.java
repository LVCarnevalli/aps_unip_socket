package br.unip.aps.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

public class Servidor extends Thread {
		
	private Socket conexao;
	
	private String nomeCliente;
	
	@SuppressWarnings("rawtypes")
	private static Vector clientes = new Vector();
	
	@SuppressWarnings("rawtypes")
	private static List listaNomesClientes = new ArrayList();
	
	private static String LOG_SERVIDOR;
	private static int PORTA_SERVIDOR;
	
	public Servidor() {
		
	}
	
	public Servidor(Socket socket) {
		this.conexao = socket;
	}
	
	public static void main(String args[]) {
		if (args.length < 2) {
			System.out.println("java -jar Servidor.jar <Diretorio Log Servidor> <Porta Servidor>");
		} else if(Utils.existFile(args[0])) {
			System.out.println("Diretorio Log Servidor incorreto. Favor verificar.");	
		} else {
			try {
				LOG_SERVIDOR = args[0];
				PORTA_SERVIDOR = Integer.parseInt(args[1]);
				ServerSocket server = new ServerSocket(PORTA_SERVIDOR);
				Log.setMessageLog("Servidor rodando na porta " + PORTA_SERVIDOR + ".", LOG_SERVIDOR);
				while (true) {
					Socket conexao = server.accept();
					Thread t = new Servidor(conexao);
					t.start();
				}
			} catch (IOException ioe) {
				Log.setMessageLog("Erro no servidor. IOException: " + ioe, LOG_SERVIDOR);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public boolean adicionarCliente(String newName) {
		for (int i = 0; i < listaNomesClientes.size(); i++) {
			if (listaNomesClientes.get(i).equals(newName))
				return true;
		}
		listaNomesClientes.add(newName);
		return false;
	}

	public void removerCliente(String oldName) {
		for (int i = 0; i < listaNomesClientes.size(); i++) {
			if (listaNomesClientes.get(i).equals(oldName))
				listaNomesClientes.remove(oldName);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void run() {
		PrintStream saida = null;
		try {
			BufferedReader entrada = new BufferedReader(new InputStreamReader(this.conexao.getInputStream()));
			saida = new PrintStream(this.conexao.getOutputStream());
			this.nomeCliente = entrada.readLine();
			if (adicionarCliente(this.nomeCliente)) {
				saida.println("COD_322_USER");
				clientes.add(saida);
				this.conexao.close();
				return;
			} else {
				Log.setMessageLog(this.nomeCliente	+ " conectou ao servidor!", LOG_SERVIDOR);
			}
			if (this.nomeCliente == null) {
				return;
			}
			clientes.add(saida);
			for (int i = 0; i < listaNomesClientes.size(); i++) {
				if(listaNomesClientes.get(i) != this.nomeCliente) {
					saida.println("[SERVIDOR] " + listaNomesClientes.get(i) + " est\u00e1 conectado.");
				}		
			}
			Enumeration e = clientes.elements();
			while (e.hasMoreElements()) {
				PrintStream chat = (PrintStream) e.nextElement();
				if (chat != saida) {
					chat.println("[SERVIDOR] " + this.nomeCliente + " entrou na sala.");
				}
			}
			String msg = entrada.readLine();
			while(msg != null) {
				while(entrada.ready()) {
					msg += "\n" + entrada.readLine();
				}
				enviarMensagem(saida, " diz: \n", msg);
				msg = entrada.readLine();
			}
			Log.setMessageLog(this.nomeCliente	+ " desconectou do servidor!", LOG_SERVIDOR);
			removerCliente(this.nomeCliente);
			clientes.remove(saida);
			this.conexao.close();
		} catch (IOException ioe) {			
			removerCliente(this.nomeCliente);
			clientes.remove(saida);
			try {
				this.conexao.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Enumeration e = clientes.elements();
			while (e.hasMoreElements()) {
				PrintStream chat = (PrintStream) e.nextElement();
				if (chat != saida) {
					chat.println("[SERVIDOR] " + this.nomeCliente + " saiu da sala.");
				}
			}
			Log.setMessageLog(this.nomeCliente	+ " desconectou do servidor! IOException: " + ioe, LOG_SERVIDOR);
		}
	}

	@SuppressWarnings("rawtypes")
	public void enviarMensagem(PrintStream saida, String acao, String msg) throws IOException {
		Enumeration e = clientes.elements();
		while (e.hasMoreElements()) {
			PrintStream chat = (PrintStream) e.nextElement();
			chat.println("[" + Utils.getHoraAtual() + "] " + this.nomeCliente + acao + msg);
		}
	}

	public Socket getConexao() {
		return conexao;
	}

	public void setConexao(Socket conexao) {
		this.conexao = conexao;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	@SuppressWarnings("rawtypes")
	public static Vector getClientes() {
		return clientes;
	}

	@SuppressWarnings("rawtypes")
	public static void setClientes(Vector clientes) {
		Servidor.clientes = clientes;
	}

	@SuppressWarnings("rawtypes")
	public static List getListaNomesClientes() {
		return listaNomesClientes;
	}

	@SuppressWarnings("rawtypes")
	public static void setListaNomesClientes(List listaNomesClientes) {
		Servidor.listaNomesClientes = listaNomesClientes;
	}

	public static String getLOG_SERVIDOR() {
		return LOG_SERVIDOR;
	}

	public static void setLOG_SERVIDOR(String log_servidor) {
		LOG_SERVIDOR = log_servidor;
	}

	public static int getPORTA_SERVIDOR() {
		return PORTA_SERVIDOR;
	}

	public static void setPORTA_SERVIDOR(int porta_servidor) {
		PORTA_SERVIDOR = porta_servidor;
	}
	
}