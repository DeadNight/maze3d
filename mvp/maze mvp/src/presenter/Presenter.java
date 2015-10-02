package presenter;

import java.util.Observer;

import view.View;

public interface Presenter extends Observer {
	void setView(View view);
	
	void start();

	ViewTypes getViewType();
}
