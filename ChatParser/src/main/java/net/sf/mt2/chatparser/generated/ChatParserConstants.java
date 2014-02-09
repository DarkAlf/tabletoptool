/* Generated By:JavaCC: Do not edit this line. ChatParserConstants.java */
package net.sf.mt2.chatparser.generated;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface ChatParserConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int MACRO_EXEC = 1;
  /** RegularExpression Id. */
  int CLEAR_CHAT = 2;
  /** RegularExpression Id. */
  int EMIT = 3;
  /** RegularExpression Id. */
  int EMOTE = 4;
  /** RegularExpression Id. */
  int GM = 5;
  /** RegularExpression Id. */
  int GOTO = 6;
  /** RegularExpression Id. */
  int IMPERSONAT = 7;
  /** RegularExpression Id. */
  int OOC = 8;
  /** RegularExpression Id. */
  int REPLY = 9;
  /** RegularExpression Id. */
  int ROLL = 10;
  /** RegularExpression Id. */
  int ROLL_GM = 11;
  /** RegularExpression Id. */
  int ROLL_ME = 12;
  /** RegularExpression Id. */
  int ROLL_SECRET = 13;
  /** RegularExpression Id. */
  int SELF = 14;
  /** RegularExpression Id. */
  int TABLE = 15;
  /** RegularExpression Id. */
  int TOKEN_MACRO = 16;
  /** RegularExpression Id. */
  int TOKEN_SPEECH = 17;
  /** RegularExpression Id. */
  int WHISPER = 18;
  /** RegularExpression Id. */
  int CHAT_COMMAND = 19;
  /** RegularExpression Id. */
  int CODE_START = 20;
  /** RegularExpression Id. */
  int TEXT = 21;
  /** RegularExpression Id. */
  int CODE_END = 22;
  /** RegularExpression Id. */
  int PLUS = 23;
  /** RegularExpression Id. */
  int MINUS = 24;
  /** RegularExpression Id. */
  int MULTIPLICATION = 25;
  /** RegularExpression Id. */
  int DIVISION = 26;
  /** RegularExpression Id. */
  int PARANTHESES_LEFT = 27;
  /** RegularExpression Id. */
  int PARANTHESES_RIGHT = 28;
  /** RegularExpression Id. */
  int NUMBER = 29;
  /** RegularExpression Id. */
  int DICE_D = 30;
  /** RegularExpression Id. */
  int DICE_KEEP = 31;
  /** RegularExpression Id. */
  int DICE_REROLL = 32;
  /** RegularExpression Id. */
  int DICE_SUCCESS = 33;
  /** RegularExpression Id. */
  int DICE_EXPLODING_SUCCESS = 34;
  /** RegularExpression Id. */
  int DICE_EXPLODING = 35;
  /** RegularExpression Id. */
  int DICE_OPEN = 36;
  /** RegularExpression Id. */
  int DICE_HERO_STUN = 37;
  /** RegularExpression Id. */
  int DICE_HERO_BODY = 38;
  /** RegularExpression Id. */
  int DICE_FUDGE = 39;
  /** RegularExpression Id. */
  int DICE_UBIQUITY = 40;
  /** RegularExpression Id. */
  int DICE_SHADOWRUN_EXPLODING_GREMLIN = 41;
  /** RegularExpression Id. */
  int DICE_SHADOWRUN_EXPLODING = 42;
  /** RegularExpression Id. */
  int DICE_SHADOWRUN_GREMLIN = 43;
  /** RegularExpression Id. */
  int DICE_SHADOWRUN = 44;

  /** Lexical state. */
  int CHATCOMMAND = 0;
  /** Lexical state. */
  int DEFAULT = 1;
  /** Lexical state. */
  int DICE_EXPR = 2;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "<MACRO_EXEC>",
    "<CLEAR_CHAT>",
    "<EMIT>",
    "<EMOTE>",
    "<GM>",
    "<GOTO>",
    "<IMPERSONAT>",
    "\"ooc\"",
    "<REPLY>",
    "<ROLL>",
    "<ROLL_GM>",
    "<ROLL_ME>",
    "<ROLL_SECRET>",
    "\"self\"",
    "<TABLE>",
    "<TOKEN_MACRO>",
    "<TOKEN_SPEECH>",
    "<WHISPER>",
    "\"/\"",
    "\"\\u00a7\"",
    "<TEXT>",
    "\"\\u00a7\"",
    "\"+\"",
    "\"-\"",
    "\"*\"",
    "\"/\"",
    "\"(\"",
    "\")\"",
    "<NUMBER>",
    "\"d\"",
    "\"k\"",
    "\"r\"",
    "\"s\"",
    "\"es\"",
    "\"e\"",
    "\"o\"",
    "\"h\"",
    "\"b\"",
    "\"df\"",
    "\"du\"",
    "\"sr4eg\"",
    "\"sr4e\"",
    "\"sr4g\"",
    "\"sr4\"",
  };

}
