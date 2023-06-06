package Test;

import java.io.IOException;
import java.util.Scanner;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceDGS;

public class RunDGSFile {
	public static void main(String args[]) throws IOException, InterruptedException {
		System.setProperty("org.graphstream.ui", "swing");
		Scanner scanner = new Scanner (System.in);
		System.out.println("Enter Path");
		String path  = scanner.nextLine();
		scanner.close();
		Graph graph = new MultiGraph("Some Graph");
		graph.setAttribute("ui.stylesheet", "url('style.css')");
		graph.display();
		FileSource source = new FileSourceDGS();
		source.addSink( graph );
		source.begin(path);
		
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