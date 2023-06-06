package Test;

import java.io.IOException;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceDGS;

public class RunDGSFile {
	public static void main(String args[]) throws IOException, InterruptedException {
		System.setProperty("org.graphstream.ui", "swing");
		
		Graph graph = new MultiGraph("Some Graph");
		graph.display();
		FileSource source = new FileSourceDGS();
		source.addSink( graph );
		source.begin("./dgs/annee/1989.dgs");
		
		while (source.nextStep())
		{
			// Scanner  scanner = new Scanner(System.in);
			// while (scanner.nextLine()==null);
			Thread.sleep (500);
		}
		source.end();
		System.out.println("finish constructing");
	}
}