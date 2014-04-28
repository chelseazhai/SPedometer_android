/**
 * 
 */
package com.smartsport.spedometer.network;

import java.util.HashMap;
import java.util.Map;

import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name NetworkAdapter
 * @descriptor network adapter
 * @author Ares
 * @version 1.0
 */
public class NetworkAdapter {

	// logger
	private static final SSLogger LOGGER = new SSLogger(NetworkAdapter.class);

	// singleton instance
	private static volatile NetworkAdapter _sSingletonNetworkAdapter;

	// worker network adapter map
	private Map<Class<? extends INetworkAdapter>, Object> workerNetworkAdapterMap;

	/**
	 * @title NetworkAdapter
	 * @descriptor network adapter private constructor
	 * @author Ares
	 */
	private NetworkAdapter() {
		super();

		// initialize worker network adapter map
		workerNetworkAdapterMap = new HashMap<Class<? extends INetworkAdapter>, Object>();
	}

	/**
	 * @title getInstance
	 * @descriptor get network adapter singleton instance
	 * @return network adapter singleton instance
	 * @author Ares
	 */
	public static NetworkAdapter getInstance() {
		if (null == _sSingletonNetworkAdapter) {
			synchronized (NetworkAdapter.class) {
				if (null == _sSingletonNetworkAdapter) {
					_sSingletonNetworkAdapter = new NetworkAdapter();
				}
			}
		}

		return _sSingletonNetworkAdapter;
	}

	public INetworkAdapter getWorkerNetworkAdapter(
			Class<? extends INetworkAdapter> networkAdapterCls) {
		// define worker network adapter
		INetworkAdapter _workerNetworkAdapter = null;

		// check the worker network adapter is or net existed
		if (null != networkAdapterCls) {
			if (workerNetworkAdapterMap.keySet().contains(networkAdapterCls)) {
				_workerNetworkAdapter = (INetworkAdapter) workerNetworkAdapterMap
						.get(networkAdapterCls);
			} else {
				try {
					// generate worker network adapter and then add to the map
					workerNetworkAdapterMap.put(
							networkAdapterCls,
							_workerNetworkAdapter = networkAdapterCls
									.newInstance());
				} catch (InstantiationException e) {
					LOGGER.error("Instantiation class = " + networkAdapterCls
							+ " error, exception message = " + e.getMessage());

					e.printStackTrace();
				} catch (IllegalAccessException e) {
					LOGGER.error("Illegal access instantiation class = "
							+ networkAdapterCls
							+ " error, exception message = " + e.getMessage());

					e.printStackTrace();
				}
			}
		} else {
			LOGGER.error("Get worker network adapter error, network adapter class is null");
		}

		// print worker network adapter map
		LOGGER.debug("Worker network adapter map = " + workerNetworkAdapterMap);

		return _workerNetworkAdapter;
	}

}
