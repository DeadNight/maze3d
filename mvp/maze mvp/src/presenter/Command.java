package presenter;

/**
 * @author Nir Leibovitch
 * <h1>Representation of a Command</h1>
 * Specifies the template of the command, how to verify its parameters &amp; how to do the
 * command
 */
public abstract class Command {
	/**
	 * Get the template of the command. Empty template (no parameters) by default.
	 * <br> Override to specify a template.
	 * @return String Template of the command
	 */
	public String getTemplate() {
		return "";
	}
	
	/**
	 * Verify the parameters given to the command. Tests that the given parameters match with
	 * the template from {@link Command#getTemplate()} by default.
	 * <br> Override for more specific verification.
	 * @param args Parameters given to the command
	 * @return boolean Whether the parameters are valid
	 * @see Command#getTemplate()
	 */
	public boolean verifyParams(String[] args) {
		if(getTemplate() == "")
			return args.length == 0;
		else
			return getTemplate().split(" ").length == args.length;
	}
	
	/**
	 * Do the command
	 * @param args Parameters passed to the command
	 */
	public abstract void doCommand(String[] args);
}
