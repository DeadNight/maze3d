package view;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class ObjectInitializer {
	// declare the instance as a data member to enable access from the event listeners
	Object instance;
	
	public Object initialize(Class<?> c) {
		return initialize(c, null);
	}
	
	public Object initialize(Class<?> c, Object copyFrom) {
		String className = c.getName().replaceAll(".*\\.", "");
		// clear the instance to enable multiple uses of the same ObjectInitializer instance
		instance = null;
		
		new BasicWindow(className, 200, 200) {
			@Override
			void initWidgets() {
				try {
					instance = c.newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					displayError("Initialization error",
							"Error occurred while initializing object");
					close();
				}
				
				shell.setLayout(new GridLayout(2, false));
				
				for(Method setter : c.getMethods()) {
					String fieldName = setter.getName();
					if(!(fieldName.startsWith("set") && setter.getParameterCount() == 1))
						continue;
					
					fieldName = fieldName.substring(3, fieldName.length());
					
					Method getter = null;
					if(copyFrom != null)
						try {
							getter = c.getMethod("get" + fieldName);
						} catch (NoSuchMethodException e) {
							// OK
						}
					
					Label label = new Label(shell, SWT.READ_ONLY);
					label.setText(fieldName);
					label.setLayoutData(new GridData(SWT.NONE, SWT.NONE, false, false));
					
					Class<?> paramType = setter.getParameters()[0].getType();
					
					if(paramType.isAssignableFrom(int.class)) {
						Text text = new Text(shell, SWT.SINGLE | SWT.BORDER);
						text.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
						if(getter != null)
							try {
								text.setText(""+(int)getter.invoke(copyFrom));
							} catch (IllegalAccessException e) {
								// shouldn't happen (the getter is public)
								e.printStackTrace();
								// continue
							} catch (InvocationTargetException e) {
								// getter threw an exception
								e.printStackTrace();
								// continue
							}
						text.addVerifyListener(new VerifyListener() {
							@Override
							public void verifyText(VerifyEvent event) {
								if(event.text != null && !event.text.equals(""))
									try {
										Integer.parseInt(event.text);
									} catch (NumberFormatException e) {
										event.doit = false;
									}
							}
						});
						text.addModifyListener(new ModifyListener() {
							@Override
							public void modifyText(ModifyEvent event) {
								int value = 0;
								if(!(text.getText() == null || text.getText().equals(""))) {
									// we passed VerifyEvent so the value can be parsed to int
									value = Integer.parseInt(text.getText());
								}
								try {
									setter.invoke(instance, value);
								} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
									displayError("Setter error", "Error setting value");
									instance = null;
									close();
								}
							}
						});
					}
				}
				
				Button cancelButton = new Button(shell, SWT.PUSH);
				cancelButton.setText("Cancel");
				cancelButton.addSelectionListener(new SelectionListener() {
					@Override
					public void widgetSelected(SelectionEvent event) {
						instance = null;
						close();
					}
					
					@Override
					public void widgetDefaultSelected(SelectionEvent e) { }
				});
				
				Button okButton = new Button(shell, SWT.PUSH);
				okButton.setText("OK");
				okButton.addSelectionListener(new SelectionListener() {
					@Override
					public void widgetSelected(SelectionEvent event) {
						close();
					}
					
					@Override
					public void widgetDefaultSelected(SelectionEvent e) { }
				});
				
				shell.pack();
			}
		}.run();
		
		return instance;
	}
}
