package boot;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import presenter.CommonPresenter;
import presenter.MazeGeneratorTypes;
import presenter.MazeSearcherTypes;
import presenter.Properties;
import presenter.ViewTypes;

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
			System.err.println("unable to create/open properties file for writing");
			System.out.println("Press return to exit...");
			try {
				new BufferedReader(new InputStreamReader(System.in)).readLine();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		}
		
		Properties props = new Properties();
		props.setPoolSize(10);
		props.setMazeGeneratorType(MazeGeneratorTypes.MY);
		props.setMazeSearcherType(MazeSearcherTypes.A_STAR_MANHATTER);
		props.setViewType(ViewTypes.GUI);
		
		xmlEncoder.writeObject(props);
		xmlEncoder.close();
		
		System.out.println("default properties file created");
		System.out.println("Press return to exit...");
		try {
			new BufferedReader(new InputStreamReader(System.in)).readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
