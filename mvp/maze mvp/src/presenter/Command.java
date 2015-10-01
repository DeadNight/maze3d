package presenter;

public abstract class Command {
	public String getTemplate() {
		return "";
	}
	
	public boolean verifyParams(String[] args) {
		if(getTemplate() == "")
			return args.length == 0;
		else
			return getTemplate().split(" ").length == args.length;
	}
	
	public abstract void doCommand(String[] args);
}
