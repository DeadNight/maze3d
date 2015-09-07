package model;

import java.io.File;

import controller.Controller;

public class MyModel extends CommonModel {
	public MyModel(Controller controller) {
		super(controller);
	}

	@Override
	public void list(String[] args) {
		if(args.length != 1) {
			controller.displayError(new String[] { "wrong number of arguments", "dir <path>" });
		} else {
			File f = new File(args[0]);
			if(f.exists())
				if(f.isDirectory())
					controller.displayFiles(f.list());
				else
					controller.displayError(new String[] { "path is not a directory" });
			else
				controller.displayError(new String[] { "directory not found" });
		}
	}
}
