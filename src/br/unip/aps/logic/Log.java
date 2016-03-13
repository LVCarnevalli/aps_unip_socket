package br.unip.aps.logic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Log {
	
	public static void setMessageLog(String messageLog, String folderLog) {
		if(folderLog != null && folderLog.trim().length() > 0) {
			try {
				File arquivo = new File(folderLog , "log.txt");
				if (!arquivo.exists()) {
					arquivo.createNewFile();
				}
				FileWriter fw = new FileWriter(arquivo, true);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write("[" + Utils.getHoraAtual() + "] " + messageLog);
				System.out.println("[" + Utils.getHoraAtual() + "] " + messageLog);
				bw.newLine();
				bw.close();
				fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}	
		}	
	}
	
}
