package boot;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import presenter.Properties;
import presenter.Properties.MazeGenerators;
import presenter.Properties.MazeSearchAlgorithms;

public class Setup {
	public static void main(String[] args) {
		XMLEncoder xmlEncoder;
		try {
			xmlEncoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("properties.xml")));
		} catch (FileNotFoundException e) {
			System.err.println("unable to create/open settings.xml for writing");
			return;
		}
		
		Properties props = new Properties();
		props.setPoolSize(10);
		props.setMazeGenerator(MazeGenerators.MY);
		props.setMazeSearchAlgorithm(MazeSearchAlgorithms.AStar_Manhattan);
		
		xmlEncoder.writeObject(props);
		xmlEncoder.close();
	}
}
