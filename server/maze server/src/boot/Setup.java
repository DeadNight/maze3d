package boot;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import common.MazeSearcherTypes;
import common.Properties;

/**
 * @author Nir Leibovitch
 * <h1>Setup application properties</h1>
 */
public class Setup {
	/**
	 * Create / edit properties.xml with default settings
	 * @param args Startup arguments
	 */
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
		props.setMazeSearcherType(MazeSearcherTypes.A_STAR_MANHATTER);
		
		xmlEncoder.writeObject(props);
		xmlEncoder.close();
		
		System.out.println("default properties file created");
	}
}
