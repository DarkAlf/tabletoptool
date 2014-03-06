/*
 *  This software copyright by various authors including the RPTools.net
 *  development team, and licensed under the LGPL Version 3 or, at your
 *  option, any later version.
 *
 *  Portions of this software were originally covered under the Apache
 *  Software License, Version 1.1 or Version 2.0.
 *
 *  See the file LICENSE elsewhere in this distribution for license details.
 */

package com.t3.model;

import java.util.List;
import java.util.ListIterator;

import com.t3.client.TabletopTool;

public class TextMessage {
	// Not an enum so that it can be hessian serialized
	public interface Channel {
		public static final int ALL = 0; // General message channel
		public static final int SAY = 1; // Player/character speech
		public static final int GM = 2; // GM visible only
		public static final int ME = 3; // Targeted to the current tabletoptool client
		public static final int GROUP = 4; // All in the group
		public static final int WHISPER = 5; // To a specific player/character
	}

	private int channel;
	private final String target;
	private final String message;
	private final String source;
	private final String speaker;

	////
	// CONSTRUCTION
	public TextMessage(int channel, String target, String source, String message, String speaker) {
		this.channel = channel;
		this.target = target;
		this.message = message;
		this.source = source;
		this.speaker = speaker;
	}
	
	public TextMessage(int channel, String target, String source, String message) {
		this(channel, target, source, message, null);
	}

	public static TextMessage say(String message, String speaker) {
		return new TextMessage(Channel.SAY, null, TabletopTool.getPlayer().getName(), message, speaker);
	}
	
	public static TextMessage say(String message) {
		return new TextMessage(Channel.SAY, null, TabletopTool.getPlayer().getName(), message, null);
	}

	public static TextMessage gm(String message) {
		return new TextMessage(Channel.GM, null, TabletopTool.getPlayer().getName(), message, null);
	}
	
	public static TextMessage gm(String message, String speaker) {
		return new TextMessage(Channel.GM, null, TabletopTool.getPlayer().getName(), message, speaker);
	}

	public static TextMessage me(String message) {
		return new TextMessage(Channel.ME, null, TabletopTool.getPlayer().getName(), message, null);
	}

	public static TextMessage group(String target, String message) {
		return new TextMessage(Channel.GROUP, target, TabletopTool.getPlayer().getName(), message, null);
	}

	public static TextMessage whisper(String target, String message) {
		return new TextMessage(Channel.WHISPER, target, TabletopTool.getPlayer().getName(), message, null);
	}

	@Override
	public String toString() {
		return message;
	}

	////
	// PROPERTIES
	public int getChannel() {
		return channel;
	}

	public void setChannel(int c) {
		channel = c;
	}

	public String getTarget() {
		return target;
	}

	public String getMessage() {
		return message;
	}

	public String getSource() {
		return source;
	}

	////
	// CONVENIENCE
	public boolean isGM() {
		return channel == Channel.GM;
	}

	public boolean isMessage() {
		return channel == Channel.ALL;
	}

	public boolean isSay() {
		return channel == Channel.SAY;
	}

	public boolean isMe() {
		return channel == Channel.ME;
	}

	public boolean isGroup() {
		return channel == Channel.GROUP;
	}

	public boolean isWhisper() {
		return channel == Channel.WHISPER;
	}

	public String getSpeaker() {
		return speaker;
	}
}