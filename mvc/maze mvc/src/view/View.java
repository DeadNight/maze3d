package view;

import java.util.HashSet;


public interface View {
	void start();
	void setCommands(HashSet<String> commands);
	void displayError(String[] strings);
	void displayFiles(String[] list);
	void displayAsyncMessage(String[] strings);
}
