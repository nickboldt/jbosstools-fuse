/*******************************************************************************
 * Copyright (c) 2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.fusesource.ide.launcher.ui.tabs;

import java.io.UnsupportedEncodingException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.internal.ui.SWTFactory;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.equinox.security.storage.StorageException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.fusesource.ide.launcher.debug.util.ICamelDebugConstants;
import org.fusesource.ide.launcher.ui.Activator;
import org.fusesource.ide.launcher.util.SecureStorageUtil;

/**
 * @author lhein
 */
public class DebugJmxTab extends AbstractLaunchConfigurationTab {
	
	private Label jmxUriLabel;
	private Label jmxUserLabel;
	private Label jmxPasswdLabel;
	
	private Text jmxUriText;
	private Text jmxUserText;
	private Text jmxPasswdText;
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.AbstractLaunchConfigurationTab#getImage()
	 */
	@Override
	public Image getImage() {
		return Activator.getDefault().getImage("camel.png");
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		Composite c = SWTFactory.createComposite(parent, 1, 1, GridData.FILL_BOTH);
		setControl(c);
		Group group = SWTFactory.createGroup(c, "Specify the JMX connection details...", 2, 1, GridData.FILL_HORIZONTAL);
		
		this.jmxUriLabel = SWTFactory.createLabel(group, "JMX Uri:", 1);
		this.jmxUriText = SWTFactory.createSingleText(group, 1);
		this.jmxUriText.addModifyListener(new ModifyListener() {
			/*
			 * (non-Javadoc)
			 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
			 */
			@Override
			public void modifyText(ModifyEvent e) {
				setDirty(true);
				updateLaunchConfigurationDialog();
			}
		});

		this.jmxUserLabel = SWTFactory.createLabel(group, "JMX User:", 1);
		this.jmxUserText = SWTFactory.createSingleText(group, 1);
		this.jmxUserText.addModifyListener(new ModifyListener() {
			/*
			 * (non-Javadoc)
			 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
			 */
			@Override
			public void modifyText(ModifyEvent e) {
				setDirty(true);
				updateLaunchConfigurationDialog();
			}
		});

		this.jmxPasswdLabel = SWTFactory.createLabel(group, "JMX Password:", 1);
		this.jmxPasswdText = SWTFactory.createText(group, SWT.PASSWORD , 1);
		this.jmxPasswdText.addModifyListener(new ModifyListener() {
			/*
			 * (non-Javadoc)
			 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
			 */
			@Override
			public void modifyText(ModifyEvent e) {
				setDirty(true);
				updateLaunchConfigurationDialog();
			}
		});

		Dialog.applyDialogFont(parent);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#setDefaults(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */
	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(ICamelDebugConstants.ATTR_JMX_URI_ID, ICamelDebugConstants.DEFAULT_JMX_URI);
		configuration.setAttribute(ICamelDebugConstants.ATTR_JMX_USER_ID, (String)null);
		setPassword(configuration, "");
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#initializeFrom(org.eclipse.debug.core.ILaunchConfiguration)
	 */
	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		try {
			String jmxUri = configuration.getAttribute(ICamelDebugConstants.ATTR_JMX_URI_ID, ICamelDebugConstants.DEFAULT_JMX_URI);
			String jmxUser = configuration.getAttribute(ICamelDebugConstants.ATTR_JMX_USER_ID, "");
			String jmxPasswd = getPassword(configuration);
			
			jmxUriText.setText(jmxUri);
			jmxUserText.setText(jmxUser);
			jmxPasswdText.setText(jmxPasswd);			
		} catch (CoreException ce) {
			jmxUriText.setText(ICamelDebugConstants.DEFAULT_JMX_URI);
			jmxUserText.setText("");
			jmxPasswdText.setText("");			
		}
		updateLaunchConfigurationDialog();
	}

	private String getPassword(ILaunchConfiguration configuration) {
		String s = SecureStorageUtil.getFromSecureStorage(org.fusesource.ide.launcher.Activator.getBundleID(), 
				configuration, ICamelDebugConstants.ATTR_JMX_PASSWORD_ID);
		if( s == null )	return "";
		return s;
	}
	
	private void setPassword(ILaunchConfiguration configuration, String pass) {
		try {
			SecureStorageUtil.storeInSecureStorage(org.fusesource.ide.launcher.Activator.getBundleID(), 
					configuration, ICamelDebugConstants.ATTR_JMX_PASSWORD_ID, pass);
        } catch (StorageException e) {
        	Activator.getDefault().getLog().log(new Status(IStatus.ERROR, org.fusesource.ide.launcher.Activator.getBundleID(), "Could not save password for JMX in secure storage.", e)); //$NON-NLS-1$
        } catch (UnsupportedEncodingException e) {
        	Activator.getDefault().getLog().log(new Status(IStatus.ERROR, org.fusesource.ide.launcher.Activator.getBundleID(), "Could not save password for JMX in secure storage.", e)); //$NON-NLS-1$	
        }
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#performApply(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */
	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(ICamelDebugConstants.ATTR_JMX_URI_ID, jmxUriText.getText().trim());
		configuration.setAttribute(ICamelDebugConstants.ATTR_JMX_USER_ID, jmxUserText.getText().trim());
		setPassword(configuration, jmxPasswdText.getText().trim());
		setDirty(false);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#getName()
	 */
	@Override
	public String getName() {
		return "JMX";
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.AbstractLaunchConfigurationTab#isValid(org.eclipse.debug.core.ILaunchConfiguration)
	 */
	@Override
	public boolean isValid(ILaunchConfiguration launchConfig) {
		return jmxUriText.getText().trim().length()>0;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.AbstractLaunchConfigurationTab#canSave()
	 */
	@Override
	public boolean canSave() {
		return isValid(null);
	}
}