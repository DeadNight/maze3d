package view;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
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
					
					Object value = null;
					if(copyFrom != null) {
						try {
							Method getter = c.getMethod("get" + fieldName);
							value = getter.invoke(copyFrom);
						} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
							// OK
						}
					}
					
					Label label = new Label(shell, SWT.READ_ONLY);
					label.setText(fieldName);
					label.setLayoutData(new GridData(SWT.NONE, SWT.NONE, false, false));
					
					Class<?> paramType = setter.getParameters()[0].getType();
					
					if(paramType.isAssignableFrom(int.class)) {
						Spinner spinner = new Spinner(shell, SWT.READ_ONLY);
						spinner.setLayoutData(new GridData(SWT.NONE, SWT.NONE, false, false));
						spinner.addModifyListener(new ModifyListener() {
							@Override
							public void modifyText(ModifyEvent event) {
								try {
									setter.invoke(instance, spinner.getSelection());
								} catch (IllegalAccessException | InvocationTargetException e) {
									displayError("Setter error", "Error setting value");
									instance = null;
									close();
								}
							}
						});
						if(value != null)
							spinner.setSelection((int) value);
					} else if(paramType.isEnum()) {
						Combo combo = new Combo(shell, SWT.READ_ONLY);
						combo.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
						Enum<?>[] enumConstants = (Enum[]) paramType.getEnumConstants();
						String[] items = new String[enumConstants.length];
						for(int i = 0; i < items.length; ++i)
							items[i] = enumConstants[i].name();
						combo.setItems(items);
						combo.addModifyListener(new ModifyListener() {
							@Override
							public void modifyText(ModifyEvent event) {
								try {
									setter.invoke(instance, enumConstants[combo.getSelectionIndex()]);
								} catch (IllegalAccessException | InvocationTargetException e) {
									displayError("Setter error", "Error setting value");
									instance = null;
									close();
								}
							}
						});
						if(value != null)
							combo.select(((Enum<?>)value).ordinal());
					} else if(paramType.isAssignableFrom(String.class)) {
						Text text = new Text(shell, SWT.SINGLE | SWT.BORDER);
						text.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
						text.addModifyListener(new ModifyListener() {
							@Override
							public void modifyText(ModifyEvent event) {
								try {
									setter.invoke(instance, text.getText());
								} catch (IllegalAccessException | InvocationTargetException e) {
									displayError("Setter error", "Error setting value");
									instance = null;
									close();
								}
							}
						});
						if(value != null)
							text.setText((String) value); 
					} else {
						
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
