/**
 * 
 */
package com.smartsport.spedometer.splash;

import android.content.Intent;

/**
 * @name IAppLaunch
 * @descriptor application launch interface
 * @author Ares
 * @version 1.0
 */
public interface IAppLaunch {

	/**
	 * @title didFinishLaunching
	 * @descriptor application launching
	 * @author Ares
	 */
	public boolean didFinishLaunching();

	/**
	 * @title targetIntent
	 * @descriptor application target launch activity
	 * @author Ares
	 */
	public Intent targetIntent();

}
