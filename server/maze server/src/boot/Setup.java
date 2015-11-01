package boot;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import common.MazeSearcherTypes;
import common.Properties;
import presenter.CommonPresenter;

/**
 * @author Nir Leibovitch
 * <h1>Setup application properties</h1>
 */
public class Setup {
	/**
	 * Create / edit properties file with default settings
	 * @param args Startup arguments
	 */
	public static void main(String[] args) {
		XMLEncoder xmlEncoder;
		try {
			xmlEncoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(CommonPresenter.PROPERTIES_FILE_NAME)));
		} catch (FileNotFoundException e) {
			System.err.println("unable to create/open settings.xml for writing");
			return;
		}
		
		Properties props = new Properties();
		props.setPort(5400);
		props.setNumOfClients(10);
		props.setSocketTimeout(20 * 1000);
		props.setPoolSize(10);
		props.setMazeSearcherType(MazeSearcherTypes.A_STAR_MANHATTER);
		
		xmlEncoder.writeObject(props);
		xmlEncoder.close();
		
		System.out.println("default properties file created");
	}
}
