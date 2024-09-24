package couches;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Session
{
	private ProcessusET ET;
	private ProcessusER ER;
	public static int count = 0;
	private BufferedReader l_lec;
	private BufferedReader l_ecr;
	private BufferedReader s_ecr;
	private BufferedReader s_lec;

	public Session(Path S_lecPath) throws IOException, InterruptedException
	{
		s_lec = Files.newBufferedReader(S_lecPath);
		ET = new ProcessusET();
		ER = new ProcessusER();
	}

	// D�marrage de la boucle de lecture des donn�es d'applications
	public void start() throws InterruptedException, IOException
	{
		while (s_lec.ready())
		{
			ET.traitement(ER, s_lec.readLine(), s_lec );
		}
		s_lec.close();
	}

}
