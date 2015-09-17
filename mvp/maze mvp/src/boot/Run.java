package boot;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import model.CommonModel;
import model.MyModel;
import presenter.Presenter;
import presenter.Properties;
import view.CommonView;
import view.MyView;

public class Run {
	public static void main(String[] args) {
		FileInputStream settingsIn;
		try {
			settingsIn = new FileInputStream("properties.xml");
		} catch (FileNotFoundException e) {
			System.err.println("settings.xml file not found");
			return;
		}
		
		XMLDecoder xmlDecoder = new XMLDecoder(new BufferedInputStream(settingsIn));
		Properties properties;
		try {
			properties = (Properties) xmlDecoder.readObject();
		} catch(ArrayIndexOutOfBoundsException e) {
			System.err.println("error while parsing properties.xml");
			return;
		} finally {
			xmlDecoder.close();
		}
		
		CommonModel model = new MyModel(properties);
		CommonView view = new MyView();
		Presenter presenter = new Presenter(model, view);
		
		model.addObserver(presenter);
		view.addObserver(presenter);
		
		view.start();
	}
}
