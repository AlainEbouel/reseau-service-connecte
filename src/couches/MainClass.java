package couches;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class MainClass // D�marrage du programme
{
	public static void main(String[] args) throws IOException, InterruptedException
	{	

		/* Creation des fichiers */
		// // ArrayList<Path> paths = new ArrayList<>();
		
		// // paths.add(Paths.get("fichiers/L_lec.txt"));
		// // paths.add(Paths.get("fichiers/L_ecr.txt")); 	
		// // paths.add(Paths.get("fichiers/S_ecr.txt")); 

		// // BufferedWriter bufWriter;
		// // for(Path p : paths){
		// // 	bufWriter = Files.newBufferedWriter(p);
		// // 	bufWriter.close();			
		// // }

		// D�marrage de la communication
		Session session = new Session(Paths.get("fichiers/S_lec.txt"));
		session.start();

		System.out.println("Fin des communications");
	}
}
