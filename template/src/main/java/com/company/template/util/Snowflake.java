package com.company.template.util;

import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Enumeration;

import lombok.extern.slf4j.Slf4j;

/**
 * Distributed Sequence Generator. Inspired by Twitter snowflake:
 * https://github.com/twitter/snowflake/tree/snowflake-2010
 *
 * This class should be used as a Singleton. Make sure that you create and reuse a Single
 * instance of Snowflake per node in your distributed system cluster.
 */
@Slf4j
public final class Snowflake implements IdGenerator {

	private static final int UNUSED_BITS = 1; // Sign bit, Unused (always set to 0)

	private static final int EPOCH_BITS = 41;

	private static final int NODE_ID_BITS = 10;

	public static final long MAX_NODE_ID = (1L << NODE_ID_BITS) - 1;

	private static final int SEQUENCE_BITS = 12;

	private static final long MAX_SEQUENCE = (1L << SEQUENCE_BITS) - 1; // 10 bits

	// Custom Epoch (January 1, 2015 Midnight UTC = 2015-01-01T00:00:00Z)
	private static final long DEFAULT_CUSTOM_EPOCH = 1420070400000L;

	private final long nodeId;

	private final long customEpoch;

	private volatile long lastTimestamp = -1L;

	private volatile long sequence = 0L;

	// Create Snowflake with a nodeId and custom epoch
	public Snowflake(long nodeId, long customEpoch) {
		if (nodeId < 0 || nodeId > MAX_NODE_ID) {
			throw new IllegalArgumentException(String.format("NodeId must be between %d and %d", 0, MAX_NODE_ID));
		}
		this.nodeId = nodeId;
		this.customEpoch = customEpoch;
	}

	// Create Snowflake with a nodeId
	public Snowflake(long nodeId) {
		this(nodeId, DEFAULT_CUSTOM_EPOCH);
		log.info("Passed NodeId is {}", nodeId);
	}

	// Let Snowflake generate a nodeId
	public Snowflake() {
		this.nodeId = createNodeIdNew();
		log.info("generated NodeId is: {}", this.nodeId);
		this.customEpoch = DEFAULT_CUSTOM_EPOCH;
	}

	public static long nodeIdFrom(String seed) {
		return (seed.hashCode()) & MAX_NODE_ID;
	}

	public synchronized long nextId() {
		long currentTimestamp = timestamp();

		if (currentTimestamp < lastTimestamp) {
			throw new IllegalStateException("Invalid System Clock!");
		}

		if (currentTimestamp == lastTimestamp) {
			sequence = (sequence + 1) & MAX_SEQUENCE;
			if (sequence == 0) {
				// Sequence Exhausted, wait till next millisecond.
				currentTimestamp = waitNextMillis(currentTimestamp);
			}
		}
		else {
			// reset sequence to start with zero for the next millisecond
			sequence = 0;
		}

		lastTimestamp = currentTimestamp;

		return currentTimestamp << (NODE_ID_BITS + SEQUENCE_BITS) | (nodeId << SEQUENCE_BITS) | sequence;
	}

	public Parsed parse(long id) {
		long maskNodeId = ((1L << NODE_ID_BITS) - 1) << SEQUENCE_BITS;
		long maskSequence = (1L << SEQUENCE_BITS) - 1;

		long timestamp = (id >> (NODE_ID_BITS + SEQUENCE_BITS)) + customEpoch;
		long localNodeId = (id & maskNodeId) >> SEQUENCE_BITS;
		long localSequence = id & maskSequence;

		return new Parsed(timestamp, localNodeId, localSequence);
	}

	// Block and wait till next millisecond
	private long waitNextMillis(long currentTimestamp) {
		while (currentTimestamp == lastTimestamp) {
			currentTimestamp = timestamp();
		}
		return currentTimestamp;
	}

	// Get current timestamp in milliseconds, adjust for the custom epoch.
	private long timestamp() {
		return Instant.now().toEpochMilli() - customEpoch;
	}

	private long createNodeIdNew() {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface networkInterface = interfaces.nextElement();
				if (networkInterface.isLoopback() || networkInterface.isVirtual() || !networkInterface.isUp()) {
					continue;
				}
				byte[] mac = networkInterface.getHardwareAddress();
				if (mac != null) {
					md.update(mac);
				}
			}
			byte[] digest = md.digest();
			long h = 0;
			for (int i = 0; i < 8; i++) {
				h = (h << 8) | (digest[i] & 0xff);
			}
			return h & MAX_NODE_ID;
		}
		catch (Exception ex) {
			log.error("Something went wrong creating NodeId: {}", ex.getMessage(), ex);
			return (new SecureRandom().nextInt()) & MAX_NODE_ID;
		}
	}

	private long createNodeId() {
		long createdNodeId;
		try {
			StringBuilder sb = new StringBuilder();
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			while (networkInterfaces.hasMoreElements()) {
				NetworkInterface networkInterface = networkInterfaces.nextElement();
				byte[] mac = networkInterface.getHardwareAddress();
				if (mac != null) {
					for (byte macPort : mac) {
						sb.append(String.format("%02X", macPort));
					}
				}
			}
			createdNodeId = sb.toString().hashCode();
		}
		catch (Exception ex) {
			createdNodeId = (new SecureRandom().nextInt());
		}
		createdNodeId = createdNodeId & MAX_NODE_ID;
		return createdNodeId;
	}

	@Override
	public String toString() {
		return "Snowflake Settings [EPOCH_BITS=" + EPOCH_BITS + ", NODE_ID_BITS=" + NODE_ID_BITS + ", SEQUENCE_BITS="
				+ SEQUENCE_BITS + ", CUSTOM_EPOCH=" + customEpoch + ", NodeId=" + nodeId + "]";
	}

	@Override
	public String generate() {
		return String.valueOf(nextId());
	}

	public record Parsed(long timestampMs, long nodeId, long sequence) {
	}

}