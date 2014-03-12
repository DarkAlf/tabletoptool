package com.t3.macro.api;

import groovy.lang.Script;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.t3.chatparser.generated.ChatParser;
import com.t3.chatparser.generated.ParseException;
import com.t3.dice.DiceBuilder;
import com.t3.dice.expression.DiceExpression;
import com.t3.client.TabletopTool;
import com.t3.client.ui.commandpanel.ChatExecutor;
import com.t3.language.I18N;
import com.t3.macro.MacroException;
import com.t3.macro.api.functions.DialogFunctions;
import com.t3.macro.api.functions.InfoFunctions;
import com.t3.macro.api.functions.MapFunctions;
import com.t3.macro.api.functions.PathFunctions;
import com.t3.macro.api.functions.input.InputFunctions;
import com.t3.macro.api.functions.player.PlayerFunctions;
import com.t3.macro.api.functions.token.TokenLocation;
import com.t3.macro.api.views.TokenView;
import com.t3.model.CellPoint;
import com.t3.model.Player;
import com.t3.model.TextMessage;
import com.t3.model.ZonePoint;
import com.t3.model.campaign.TokenProperty;

//FIXME make this package into a PLUG-IN
public abstract class MacroAPI extends Script {
	
	public final InfoFunctions info;
	public final PlayerFunctions player;
	public final MapFunctions map;
	public final DialogFunctions dialog;
	public final PathFunctions path;

	public MacroAPI() {
		super();
		this.info=new InfoFunctions();
		this.player=new PlayerFunctions();
		this.map=new MapFunctions();
		this.dialog=new DialogFunctions();
		this.path=new PathFunctions();
	}

	/**
	 * Simply writes to the chat
	 * @param message a string or some other kind of objects that is written to the chat
	 */
	public void say(Object message) {
		ChatExecutor.say(message.toString(),TabletopTool.getPlayer().getName());
	}
	
	/**
	 * Whispers to a certain player so that only you two can see it
	 * @param message a string or some other kind of objects that is written to the chat
	 */
	public void whisper(Object message, String targetPlayer) {
		ChatExecutor.whisper(message.toString(), TabletopTool.getPlayer().getName(), targetPlayer);		
	}
	
	/**
	 * Whispers to to the GM so that only you two can see it
	 * @param message a string or some other kind of objects that is written to the chat
	 */
	public void whisperToGM(Object message) {
		ChatExecutor.gm(message.toString(), TabletopTool.getPlayer().getName());
	}
	
	/**
	 * Says something out of character
	 * @param message a string or some other kind of objects that is written to the chat
	 */
	public void sayOOC(Object message) {
		ChatExecutor.outOfCharacter(message.toString());
	}
	
	/**
	 * This writes a message without your name to the chat if you are the GM
	 * @param message a string or some other kind of objects that is written to the chat
	 */
	public void emit(Object message) {
		ChatExecutor.emit(message.toString(), TabletopTool.getPlayer().getName());
	}

	/**
	 * This writes a message about you to the chat
	 * @param message a string or some other kind of objects that is written to the chat
	 */
	public void emote(Object message) {
		ChatExecutor.emote(message.toString(), TabletopTool.getPlayer().getName());
	}
	
	/**
	 * This whispers an answer back to last person that wrote to you
	 * @param message a string or some other kind of objects that is written to the chat
	 */
	public void reply(Object message) {
		ChatExecutor.reply(message.toString(), TabletopTool.getPlayer().getName());
	}
	
	/**
	 * This writes a message to the chat so that only you can see it
	 * @param o a string or some other kind of objects that is written to the chat
	 */
	public void print(Object o) {
		TabletopTool.addLocalMessage(o==null?"null":o.toString());
	}

	/**
	 * This moves your camera to the given token
	 * @param token the token you want to focus on
	 */
	public void goTo(TokenView token) {
		TokenLocation tl=token.getLocation(false);
		TabletopTool.getFrame().getCurrentZoneRenderer().centerOn(new ZonePoint(tl.getX(), tl.getY()));
	}
	
	/**
	 * This moves your camera to the point
	 * @param x the x part of the coordinate
	 * @param x the x part of the coordinate
	 */
	public void goTo(int x, int y) {
		goTo(x,y,true);
	}
	
	/**
	 * This moves your camera to the point
	 * @param x the x part of the coordinate
	 * @param x the x part of the coordinate
	 * @param gridUnit if the given coordinates are in grid or zone units
	 */
	public void goTo(int x, int y, boolean gridUnit) {
		if(gridUnit)
			TabletopTool.getFrame().getCurrentZoneRenderer().centerOn(new CellPoint(x, y));
		else
			TabletopTool.getFrame().getCurrentZoneRenderer().centerOn(new ZonePoint(x, y));
	}
	
	/**
	 * Gets all the property names.
	 * @return a string list containing the property names.
	 */
	public List<String> getAllPropertyNames() {
		Map<String, List<TokenProperty>> pmap = TabletopTool.getCampaign().getCampaignProperties().getTokenTypeMap();
		ArrayList<String> namesList = new ArrayList<String>();

		for (Entry<String, List<TokenProperty>> entry : pmap.entrySet()) {
			for (TokenProperty tp : entry.getValue()) {
				namesList.add(tp.getName());
			}
		}
		return namesList;
	}
	
	/**
	 * Gets all the property names for the specified type.
	 * 
	 * @param type  The type of property.
	 * @return a string list containing the property names.
	 */
	public List<String> getAllPropertyNames(String type) {
		List<TokenProperty> props = TabletopTool.getCampaign().getCampaignProperties().getTokenPropertyList(type);
		ArrayList<String> namesList = new ArrayList<String>();
		for (TokenProperty tp : props) {
			namesList.add(tp.getName());
		}
		return namesList;
	}

	public DiceExpression roll(String diceExpression) throws MacroException {
		try {
			return new ChatParser(diceExpression).parseDiceExpression();
		} catch (ParseException e) {
			throw new MacroException("Could not parse dice Expression '"+diceExpression+"'",e);
		}
	}
	
	/**
	 * This will allow you to roll dice.<br>
	 * Example:<br>
	 * 1d6: roll(1).{@link DiceBuilder#d d}(6).{@link com.t3.dice.Dice#getResult getResult}();<br>
	 * 3d6e: roll(3).{@link DiceBuilder#d d}(6).{@link com.t3.dice.ExtendableDice#explode explode}().{@link com.t3.dice.Dice#getResult getResult}();
	 * @param numberOfDices
	 * @return
	 */
	public DiceBuilder roll(int numberOfDices) {
		return DiceBuilder.roll(numberOfDices);
	}
}
