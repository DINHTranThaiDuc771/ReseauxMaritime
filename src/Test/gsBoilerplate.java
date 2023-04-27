package Test;

import java.io.IOException;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceDGS;

public class gsBoilerplate {
	public static void main(String args[]) throws IOException, InterruptedException {
		System.setProperty("org.graphstream.ui", "swing");
		
		Graph graph = new MultiGraph("Some Graph");
		graph.display();
		FileSource source = new FileSourceDGS();
		source.addSink( graph );
		source.begin("graphDynamic.dgs");
		
		while (source.nextStep())
		{
			Thread.sleep(1000);
		
		}
		source.end();
		System.out.println("finish constructing");
	}
}