package view;

import java.util.HashMap;

import org.eclipse.swt.SWT;
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
	
	public MazeServerWindow() {
		super("Maze Server", 600, 300);
		clientRows = new HashMap<Integer, TableItem>();
	}

	@Override
	void initWidgets() {
		initMenu();
		
		shell.setLayout(new GridLayout(2, false));
		
		clientsTable = new Table(shell, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		clientsTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		clientsTable.setHeaderVisible(true);
		
		new TableColumn(clientsTable, SWT.CENTER).setText("Client #");
		new TableColumn(clientsTable, SWT.NONE).setText("Pending");
		new TableColumn(clientsTable, SWT.NONE).setText("Solving");
		new TableColumn(clientsTable, SWT.NONE).setText("No Solution");
		new TableColumn(clientsTable, SWT.NONE).setText("Solved");
		for(TableColumn column : clientsTable.getColumns())
			column.pack();
		
		Group statsGroup = new Group(shell, SWT.V_SCROLL | SWT.H_SCROLL);
		statsGroup.setText("Stats");
		statsGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
		statsGroup.setLayout(new GridLayout(2, false));
		
		new Label(statsGroup, SWT.NONE).setText("Status: ");
		statusText = new Text(statsGroup, SWT.READ_ONLY);
		statusText.setText("Connected");
		
		new Label(statsGroup, SWT.NONE).setText("Connected clients: ");
		connectedClientsText = new Text(statsGroup, SWT.READ_ONLY);
		connectedClientsText.setText("0");
		
		new Label(statsGroup, SWT.NONE).setText("Pending requests: ");
		pendingRequestsText = new Text(statsGroup, SWT.READ_ONLY);
		pendingRequestsText.setText("0");
		
		new Label(statsGroup, SWT.NONE).setText("Solving: ");
		solvingText = new Text(statsGroup, SWT.READ_ONLY);
		solvingText.setText("0");
		
		new Label(statsGroup, SWT.NONE).setText("No solution: ");
		NoSolutionText = new Text(statsGroup, SWT.READ_ONLY);
		NoSolutionText.setText("0");
		
		new Label(statsGroup, SWT.NONE).setText("Solved: ");
		solvedText = new Text(statsGroup, SWT.READ_ONLY);
		solvedText.setText("0");
	}
	
	void initMenu() {
		Menu menu = new Menu(shell, SWT.BAR);
		
		MenuItem fileMenuItem = new MenuItem(menu, SWT.CASCADE);
		fileMenuItem.setText("File");
		
		Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
		fileMenuItem.setMenu(fileMenu);
		
		exitMenuItem = new MenuItem(fileMenu, SWT.PUSH);
		exitMenuItem.setText("Exit");
		
		shell.setMenuBar(menu);
	}

	public void addExitListener(Listener listener) {
		shell.addListener(SWT.Close, listener);
		exitMenuItem.addListener(SWT.Selection, listener);
	}
	
	public void addClient(Client client) {
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				TableItem clientRow = new TableItem(clientsTable, SWT.NONE);
				clientRow.setText(0, ""+client.getId());
				setClientRow(clientRow, client);
				clientRows.put(client.getId(), clientRow);
				connectedClientsText.setText(""+clientRows.size());
			}
		});
	}
	
	public void removeClient(int clientId) {
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				TableItem clientRow = clientRows.remove(clientId);
				if(clientRow != null) {
					clientsTable.remove(clientsTable.indexOf(clientRow));
					for(TableColumn column : clientsTable.getColumns())
						column.pack();
					connectedClientsText.setText(""+clientRows.size());
				}
			}
		});
	}

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

	public void displayShuttingDown() {
		statusText.setText("Shutting Down...");
	}

	public void updateServerStats(ServerStats serverStats) {
		pendingRequestsText.setText(""+serverStats.getPending());
		solvingText.setText(""+serverStats.getSolving());
		NoSolutionText.setText(""+serverStats.getNoSolution());
		solvedText.setText(""+serverStats.getSolved());
	}
}
