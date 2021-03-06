package view;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import common.Client;
import common.ServerStats;

/**
 * @author Nir Leibovitch
 * <h1>Maze server window</h1>
 * Lists connected clients, shown server statistics &amp; enables to edit, import &amp; export server
 * properties
 */
public class MazeServerWindow extends BasicWindow {
	Table clientsTable;
	HashMap<Integer, TableItem> clientRows;
	
	private MenuItem exitMenuItem;
	private Text connectedClientsText;
	private Text pendingRequestsText;
	private Text solvingText;
	private Text solvedText;
	private Text statusText;
	private Text NoSolutionText;
	private Text cachedText;
	private MenuItem editPropertiesMenuItem;
	private MenuItem importPropertiesMenuItem;
	private MenuItem exportPropertiesMenuItem;
	
	/**
	 * Initialize the server window
	 */
	public MazeServerWindow() {
		super("Maze Server", 600, 300);
		clientRows = new HashMap<Integer, TableItem>();
	}

	@Override
	void initWidgets() {
		initMenu();
		
		shell.setLayout(new GridLayout(2, false));
		
		clientsTable = new Table(shell, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
		clientsTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		clientsTable.setHeaderVisible(true);
		
		new TableColumn(clientsTable, SWT.CENTER).setText("Client #");
		new TableColumn(clientsTable, SWT.NONE).setText("Pending");
		new TableColumn(clientsTable, SWT.NONE).setText("Solving");
		new TableColumn(clientsTable, SWT.NONE).setText("No Solution");
		new TableColumn(clientsTable, SWT.NONE).setText("Solved");
		for(TableColumn column : clientsTable.getColumns())
			column.pack();
		
//		Menu contextMenu = new Menu(clientsTable);
//		clientsTable.setMenu(contextMenu);
//	    disconnectMenuItem = new MenuItem(contextMenu, SWT.None);
//	    disconnectMenuItem.setText("Disconnect");
//
//	    clientsTable.addListener(SWT.MouseDown, new Listener() {
//			@Override
//			public void handleEvent(Event e) {
//				TableItem[] selection = clientsTable.getSelection();
//	            if(selection.length!=0 && (e.button == 3)){
//	                contextMenu.setVisible(true);
//	            }
//			}
//		});
		
		Group statsGroup = new Group(shell, SWT.NONE);
		statsGroup.setText("Stats");
		statsGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
		statsGroup.setLayout(new GridLayout(2, false));
		
		new Label(statsGroup, SWT.NONE).setText("Status: ");
		statusText = new Text(statsGroup, SWT.READ_ONLY);
		statusText.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false));
		statusText.setText("Running");
		
		new Label(statsGroup, SWT.NONE).setText("Connected clients: ");
		connectedClientsText = new Text(statsGroup, SWT.READ_ONLY);
		connectedClientsText.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false));
		connectedClientsText.setText("0");
		
		new Label(statsGroup, SWT.NONE).setText("Pending searchables: ");
		pendingRequestsText = new Text(statsGroup, SWT.READ_ONLY);
		pendingRequestsText.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false));
		pendingRequestsText.setText("0");
		
		new Label(statsGroup, SWT.NONE).setText("Solving: ");
		solvingText = new Text(statsGroup, SWT.READ_ONLY);
		solvingText.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false));
		solvingText.setText("0");
		
		new Label(statsGroup, SWT.NONE).setText("No solution: ");
		NoSolutionText = new Text(statsGroup, SWT.READ_ONLY);
		NoSolutionText.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false));
		NoSolutionText.setText("0");
		
		new Label(statsGroup, SWT.NONE).setText("Solved: ");
		solvedText = new Text(statsGroup, SWT.READ_ONLY);
		solvedText.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false));
		solvedText.setText("0");
		
		new Label(statsGroup, SWT.NONE).setText("Cached Solutions: ");
		cachedText = new Text(statsGroup, SWT.READ_ONLY);
		cachedText.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false));
		cachedText.setText("0");
	}
	
	private void initMenu() {
		Menu menu = new Menu(shell, SWT.BAR);
		
		MenuItem fileMenuItem = new MenuItem(menu, SWT.CASCADE);
		fileMenuItem.setText("File");
		
		Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
		fileMenuItem.setMenu(fileMenu);
		
		editPropertiesMenuItem = new MenuItem(fileMenu, SWT.PUSH);
		editPropertiesMenuItem.setText("Properties");
		
		importPropertiesMenuItem = new MenuItem(fileMenu, SWT.PUSH);
		importPropertiesMenuItem.setText("Import properties");
		
		exportPropertiesMenuItem = new MenuItem(fileMenu, SWT.PUSH);
		exportPropertiesMenuItem.setText("Export properties");
		
		new MenuItem(fileMenu, SWT.SEPARATOR);
		
		exitMenuItem = new MenuItem(fileMenu, SWT.PUSH);
		exitMenuItem.setText("Exit");
		
		shell.setMenuBar(menu);
	}

	/**
	 * Add a listener to handle user requests to close the server
	 * @param listener Listener
	 */
	public void addExitListener(Listener listener) {
		shell.addListener(SWT.Close, listener);
		exitMenuItem.addListener(SWT.Selection, listener);
	}
	
	/**
	 * Add a listener to handle user requests to edit the game properties
	 * @param listener Listener
	 */
	public void addEditPropertiesListener(SelectionListener listener) {
		editPropertiesMenuItem.addSelectionListener(listener);
	}
	
	/**
	 * Add a listener to handle user requests to import game properties
	 * @param listener Listener
	 */
	public void addImportPropertiesListener(SelectionListener listener) {
		importPropertiesMenuItem.addSelectionListener(listener);
	}
	
	/**
	 * Add a listener to handle user requests to export the current game properties
	 * @param listener Listener
	 */
	public void addExportPropertiesListener(SelectionListener listener) {
		exportPropertiesMenuItem.addSelectionListener(listener);
	}
	
//	public void addDisconnectSelectionListener(SelectionListener listener) {
//		disconnectMenuItem.addSelectionListener(listener);
//	}
	
	/**
	 * Add a new client to the clients list
	 * @param client Client data
	 */
	public void addClient(Client client) {
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				TableItem clientRow = new TableItem(clientsTable, SWT.NONE);
				clientRow.setText(0, ""+client.getId());
				setClientRow(clientRow, client);
				clientRows.put(client.getId(), clientRow);
			}
		});
	}
	
	/**
	 * Remove a client ftom the clents list
	 * @param clientId Client id
	 */
	public void removeClient(int clientId) {
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				TableItem clientRow = clientRows.remove(clientId);
				if(clientRow != null) {
					clientsTable.remove(clientsTable.indexOf(clientRow));
					for(TableColumn column : clientsTable.getColumns())
						column.pack();
				}
			}
		});
	}

	/**
	 * Update an existing client from the clients list
	 * @param client Updated client data
	 */
	public void updateClient(Client client) {
		setClientRow(clientRows.get(client.getId()), client);
	}
	
	private void setClientRow(TableItem clientRow, Client client) {
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				clientRow.setText(1, ""+client.getPending());
				clientRow.setText(2, ""+client.getSolving());
				clientRow.setText(3, ""+client.getNoSolution());
				clientRow.setText(4, ""+client.getSolved());
				for(TableColumn column : clientsTable.getColumns())
					column.pack();
			}
		});
	}

	/**
	 * Inform the user that the server is shutting down
	 */
	public void displayShuttingDown() {
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				statusText.setText("Shutting Down...");
			}
		});
	}

	/**
	 * Update server statistics
	 * @param serverStats Updated server statistics
	 */
	public void updateServerStats(ServerStats serverStats) {
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				connectedClientsText.setText(""+serverStats.getConnected());
				pendingRequestsText.setText(""+serverStats.getPending());
				solvingText.setText(""+serverStats.getSolving());
				NoSolutionText.setText(""+serverStats.getNoSolution());
				solvedText.setText(""+serverStats.getSolved());
				cachedText.setText(""+serverStats.getCached());
			}
		});
	}
}
