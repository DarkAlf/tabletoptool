/*
 * Copyright (c) 2014 tabletoptool.com team.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     rptools.com team - initial implementation
 *     tabletoptool.com team - further development
 */
package com.t3.networking;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.log4j.Logger;

import com.t3.client.TabletopTool;
import com.t3.clientserver.connection.ClientConnection;
import com.t3.clientserver.connection.ServerObserver;
import com.t3.common.T3Constants;
import com.t3.model.campaign.Campaign;
import com.t3.networking.registry.T3Registry;
import com.t3.transfer.AssetChunk;
import com.t3.transfer.AssetProducer;
import com.t3.transfer.AssetTransferManager;

/**
 * @author drice
 */
public class T3Server {
	private static final Logger log = Logger.getLogger(T3Server.class);
	private static final int ASSET_CHUNK_SIZE = 5 * 1024;

	private final T3ServerConnection conn;
	private final ServerMethodHandler handler;
	private final ServerConfig config;

	private final Map<String, AssetTransferManager> assetManagerMap = Collections.synchronizedMap(new HashMap<String, AssetTransferManager>());
	private final Map<String, ClientConnection> connectionMap = Collections.synchronizedMap(new HashMap<String, ClientConnection>());
	private final AssetProducerThread assetProducerThread;

	private Campaign campaign;
	private ServerPolicy policy;
	private HeartbeatThread heartbeatThread;

	public T3Server(ServerConfig config, ServerPolicy policy) throws IOException {
		handler = new ServerMethodHandler(this);
		conn = new T3ServerConnection(this, config.getPort());
		conn.addMessageHandler(handler);

		campaign = new Campaign();

		assetProducerThread = new AssetProducerThread();
		assetProducerThread.start();

		this.config = config;
		this.policy = policy;

		// Start a heartbeat if requested
		if (config.isServerRegistered()) {
			heartbeatThread = new HeartbeatThread();
			heartbeatThread.start();
		}
	}

	public void configureClientConnection(ClientConnection connection) {
		String id = connection.getId();
		assetManagerMap.put(id, new AssetTransferManager());
		connectionMap.put(id, connection);
	}

	public ClientConnection getClientConnection(String id) {
		return connectionMap.get(id);
	}

	public String getConnectionId(String playerId) {
		return conn.getConnectionId(playerId);
	}

	/**
	 * Forceably disconnects a client and cleans up references to it
	 * 
	 * @param id
	 *            the connection ID
	 */
	public void releaseClientConnection(String id) {
		ClientConnection connection = getClientConnection(id);
		if (connection != null) {
			try {
				connection.close();
			} catch (IOException e) {
				log.error("Could not release connection: " + id, e);
			}
		}
		assetManagerMap.remove(id);
		connectionMap.remove(id);
	}

	public void addAssetProducer(String connectionId, AssetProducer producer) {
		AssetTransferManager manager = assetManagerMap.get(connectionId);
		manager.addProducer(producer);
	}

	public void addObserver(ServerObserver observer) {
		if (observer != null) {
			conn.addObserver(observer);
		}
	}

	public void removeObserver(ServerObserver observer) {
		conn.removeObserver(observer);
	}

	public boolean isHostId(String playerId) {
		return config.getHostPlayerId() != null && config.getHostPlayerId().equals(playerId);
	}

	public T3ServerConnection getConnection() {
		return conn;
	}

	public boolean isPlayerConnected(String id) {
		return conn.getPlayer(id) != null;
	}

	public void setCampaign(Campaign campaign) {
		// Don't allow null campaigns, but allow the campaign to be cleared out
		if (campaign == null) {
			campaign = new Campaign();
		}
		this.campaign = campaign;
	}

	public Campaign getCampaign() {
		return campaign;
	}

	public ServerPolicy getPolicy() {
		return policy;
	}

	public void updateServerPolicy(ServerPolicy policy) {
		this.policy = policy;
	}

	public ServerMethodHandler getMethodHandler() {
		return handler;
	}

	public ServerConfig getConfig() {
		return config;
	}

	public void stop() {
		try {
			conn.close();
			if (heartbeatThread != null) {
				heartbeatThread.shutdown();
			}
			if (assetProducerThread != null) {
				assetProducerThread.shutdown();
			}
		} catch (IOException e) {
			// Not too concerned about this
			e.printStackTrace();
		}
	}

	private static final Random random = new Random();

	private class HeartbeatThread extends Thread {
		private boolean stop = false;
		private static final int HEARTBEAT_DELAY = 80 * 1000; // 80-100 seconds (server expects at least 120s)
		private static final int HEARTBEAT_FLUX = 20 * 1000; // 20 seconds

		@Override
		public void run() {
			while (!stop) {
				try {
					Thread.sleep(HEARTBEAT_DELAY + random.nextInt(HEARTBEAT_FLUX));
				} catch (InterruptedException ie) {
					// This means stop
					break;
				}
				// Pulse
				if(!T3Registry.heartBeat(config.getPort())) {
					//if server is no longer registered (temporary disconnected?)
					TabletopTool.showError("Your server disconnected from T³. It can no longer be found.");
					//TODO try to reconnect first!
				}
			}
		}

		public void shutdown() {
			stop = true;
			interrupt();
		}
	}

	////
	// CLASSES
	private class AssetProducerThread extends Thread {
		private boolean stop = false;

		@Override
		public void run() {
			while (!stop) {
				try {
					boolean lookForMore = false;
					for (Entry<String, AssetTransferManager> entry : assetManagerMap.entrySet()) {
						AssetChunk chunk = entry.getValue().nextChunk(ASSET_CHUNK_SIZE);
						if (chunk != null) {
							lookForMore = true;
							getConnection().callMethod(entry.getKey(), T3Constants.Channel.IMAGE, NetworkCommand.updateAssetTransfer, chunk);
						}
					}
					if (lookForMore) {
						continue;
					}
					// Sleep for a bit
					synchronized (this) {
						Thread.sleep(500);
					}
				} catch (Exception e) {
					e.printStackTrace();
					// keep on going
				}
			}
		}

		public void shutdown() {
			stop = true;
		}
	}

	////
	// STANDALONE SERVER
	public static void main(String[] args) throws IOException {
		// This starts the server thread.
		new T3Server(new ServerConfig(), new ServerPolicy());
	}
}
