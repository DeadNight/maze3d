package view;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import common.Client;

public class MazeServerWindow extends BasicWindow {
	Button exitButton;
	Table clientsTable;
	
	HashMap<Integer, TableItem> clientRows;
	
	public MazeServerWindow() {
		super("Maze Server", 600, 300);
		clientRows = new HashMap<Integer, TableItem>();
	}

	@Override
	void initWidgets() {
		shell.setLayout(new GridLayout(2, false));
		
		exitButton = new Button(shell, SWT.PUSH);
		exitButton.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false));
		exitButton.setText("Exit");
		
		clientsTable = new Table(shell, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		clientsTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
		clientsTable.setHeaderVisible(true);
		
		new TableColumn(clientsTable, SWT.CENTER).setText("Client #");
		new TableColumn(clientsTable, SWT.NONE).setText("Last Command");
		for(TableColumn column : clientsTable.getColumns())
			column.pack();
		
		Table statusTable = new Table(shell, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		statusTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
		
		new TableColumn(statusTable, SWT.NONE).setText("Key");;
		new TableColumn(statusTable, SWT.NONE).setText("Value");;
		for(TableColumn column : statusTable.getColumns())
			column.pack();
	}

	public void addExitListener(Listener listener) {
		shell.addListener(SWT.Close, listener);
		exitButton.addListener(SWT.Selection, listener);
	}
	
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

	public void updateClient(Client client) {
		setClientRow(clientRows.get(client.getId()), client);
		
	}
	
	private void setClientRow(TableItem clientRow, Client client) {
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				clientRow.setText(1, client.getLastCommand());
				for(TableColumn column : clientsTable.getColumns())
					column.pack();
			}
		});
	}
}
