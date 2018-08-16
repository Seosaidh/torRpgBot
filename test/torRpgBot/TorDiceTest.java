package torRpgBot;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import torRpgBot.TorDice.CommandResults;
import torRpgBot.TorDice.torDiceInterface;

public class TorDiceTest {
	
	private class TestDice implements torDiceInterface {
		
		int[] d6, d12;
		int d6Index = 0;
		int d12Index = 0;

		@Override
		public int rolld6() {
			int val = d6[d6Index];
			d6Index++;
			return val;
		}

		@Override
		public int rolld12() {
			int val = d12[d12Index];
			d12Index++;
			return val;
		}
		
		TestDice(int[] d6, int[] d12) {
			this.d6 = d6;
			this.d12 = d12;
		}
		
		public void setd6 (int[] num) {
			this.d6 = num;
			d6Index = 0;
		}
		
		public void setd12 (int[] num) {
			this.d12 = num;
			d12Index = 0;
		}
		
	}
	
	private class TestClass extends TorDice {

		public TestClass(List<CommandFlag> flag, torDiceInterface dice, EmoteInterface emotes) {
			super(flag, dice, emotes);
		}

		@Override
		protected void handleCommand(MessageReceivedEvent event) {
			System.out.println("Don't actually handle commands, we just want to test the interior methods.");
			
		}

		@Override
		protected String getHelp() {
			System.out.println("Don't actually gethelp, we just want to test the interior methods.");
			return null;
		}

		@Override
		protected List<String> getAliases() {
			System.out.println("Don't actually getAliases, we just want to test the interior methods.");
			return null;
		}
		
		public CommandResults publicParseCommandString(String command) {
			return super.parseCommandString(command);
		}
		
		public int[] publicRollFeat(CommandResults command, boolean isAdversary) {
			return super.rollFeat(command, isAdversary);
		}
		
		public int[] publicRollSuccess(int numOfSuccess, int numOfMastery) {
			return super.rollSuccess(numOfSuccess, numOfMastery);
		}
	}
	
	public TestDice diceProvider = new TestDice(new int[] {1}, new int[] {1});
	public List<CommandFlag> tempList = new ArrayList<CommandFlag>();
	private EmoteInterface emotes = new EmoteStrings();
	public TestClass testClass = new TestClass(tempList, diceProvider, emotes);
	
	

  @Test(groups = {"torRpgBotFull", "torDice", "parseString"})
  public void testParseCommandStringEmptyString() {
	
	String testString = "";
	
	CommandResults results = testClass.publicParseCommandString(testString);

	Assert.assertEquals(results.parseSuccessful, false, "Full results object = " + results.toString());
  }
  
  @Test(groups = {"torRpgBotFull", "torDice", "parseString"})
  public void testParseCommandStringJustNum() {
	
	String testString = "3";
	
	CommandResults results = testClass.publicParseCommandString(testString);
	
	Assert.assertEquals(results.parseSuccessful, false, "Full results object = " + results.toString());
  }
  
  @Test(groups = {"torRpgBotFull", "torDice", "parseString"})
  public void testParseCommandStringValidSimple() {
	
	String testString = "3 Awe";
	
	CommandResults results = testClass.publicParseCommandString(testString);
	
	Assert.assertEquals(results.parseSuccessful, true, "Full results object = " + results.toString());
	Assert.assertEquals(results.hasAdvantage, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.hasDisadvantage, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.isWeary, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.modifier, 0, "Full results object = " + results.toString());
	Assert.assertEquals(results.numOfMastery, 0, "Full results object = " + results.toString());
	Assert.assertEquals(results.numOfSuccess, 3, "Full results object = " + results.toString());
	Assert.assertEquals(results.skillName, "Awe", "Full results object = " + results.toString());
	Assert.assertEquals(results.targetNumber, 14, "Full results object = " + results.toString());
  }
  
  @Test(groups = {"torRpgBotFull", "torDice", "parseString"})
  public void testParseCommandStringMasterySimple() {
	
	String testString = "3 (1) Stealth";
	
	CommandResults results = testClass.publicParseCommandString(testString);
	
	Assert.assertEquals(results.parseSuccessful, true, "Full results object = " + results.toString());
	Assert.assertEquals(results.hasAdvantage, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.hasDisadvantage, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.isWeary, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.modifier, 0, "Full results object = " + results.toString());
	Assert.assertEquals(results.numOfMastery, 1, "Full results object = " + results.toString());
	Assert.assertEquals(results.numOfSuccess, 3, "Full results object = " + results.toString());
	Assert.assertEquals(results.skillName, "Stealth", "Full results object = " + results.toString());
	Assert.assertEquals(results.targetNumber, 14, "Full results object = " + results.toString());
  }
  
  @Test(groups = {"torRpgBotFull", "torDice", "parseString"})
  public void testParseCommandStringMasteryTN() {
	
	String testString = "3 (1) Riddle 16";
	
	CommandResults results = testClass.publicParseCommandString(testString);
	
	Assert.assertEquals(results.parseSuccessful, true, "Full results object = " + results.toString());
	Assert.assertEquals(results.hasAdvantage, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.hasDisadvantage, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.isWeary, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.modifier, 0, "Full results object = " + results.toString());
	Assert.assertEquals(results.numOfMastery, 1, "Full results object = " + results.toString());
	Assert.assertEquals(results.numOfSuccess, 3, "Full results object = " + results.toString());
	Assert.assertEquals(results.skillName, "Riddle", "Full results object = " + results.toString());
	Assert.assertEquals(results.targetNumber, 16, "Full results object = " + results.toString());
  }
  
  @Test(groups = {"torRpgBotFull", "torDice", "parseString"})
  public void testParseCommandStringMasteryTN2() {
	
	String testString = "3 (1) Riddle > 16";
	
	CommandResults results = testClass.publicParseCommandString(testString);
	
	Assert.assertEquals(results.parseSuccessful, true, "Full results object = " + results.toString());
	Assert.assertEquals(results.hasAdvantage, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.hasDisadvantage, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.isWeary, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.modifier, 0, "Full results object = " + results.toString());
	Assert.assertEquals(results.numOfMastery, 1, "Full results object = " + results.toString());
	Assert.assertEquals(results.numOfSuccess, 3, "Full results object = " + results.toString());
	Assert.assertEquals(results.skillName, "Riddle", "Full results object = " + results.toString());
	Assert.assertEquals(results.targetNumber, 16, "Full results object = " + results.toString());
  }
  
  @Test(groups = {"torRpgBotFull", "torDice", "parseString"})
  public void testParseCommandStringMasteryInvalid() {
	
	String testString = "3 1 Awe";
	
	CommandResults results = testClass.publicParseCommandString(testString);
	
	Assert.assertEquals(results.parseSuccessful, true, "Full results object = " + results.toString());
	Assert.assertEquals(results.hasAdvantage, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.hasDisadvantage, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.isWeary, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.modifier, 1, "Full results object = " + results.toString());
	Assert.assertEquals(results.numOfMastery, 0, "Full results object = " + results.toString());
	Assert.assertEquals(results.numOfSuccess, 3, "Full results object = " + results.toString());
	Assert.assertEquals(results.skillName, "Awe", "Full results object = " + results.toString());
	Assert.assertEquals(results.targetNumber, 14, "Full results object = " + results.toString());
  }
  
  @Test(groups = {"torRpgBotFull", "torDice", "parseString"})
  public void testParseCommandStringWearySimple() {
	
	String testString = "w 4 Awe";
	
	CommandResults results = testClass.publicParseCommandString(testString);
	
	Assert.assertEquals(results.parseSuccessful, true, "Full results object = " + results.toString());
	Assert.assertEquals(results.hasAdvantage, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.hasDisadvantage, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.isWeary, true, "Full results object = " + results.toString());
	Assert.assertEquals(results.modifier, 0, "Full results object = " + results.toString());
	Assert.assertEquals(results.numOfMastery, 0, "Full results object = " + results.toString());
	Assert.assertEquals(results.numOfSuccess, 4, "Full results object = " + results.toString());
	Assert.assertEquals(results.skillName, "Awe", "Full results object = " + results.toString());
	Assert.assertEquals(results.targetNumber, 14, "Full results object = " + results.toString());
  }
  
  @Test(groups = {"torRpgBotFull", "torDice", "parseString"})
  public void testParseCommandStringInvalidAdvantage() {
	
	String testString = "ad 3 Awe";
	
	CommandResults results = testClass.publicParseCommandString(testString);
	
	Assert.assertEquals(results.parseSuccessful, false, "Full results object = " + results.toString());
  }
  
  @Test(groups = {"torRpgBotFull", "torDice", "parseString"})
  public void testParseCommandStringAdvantageSimple() {
	
	String testString = "a 3 Awe";
	
	CommandResults results = testClass.publicParseCommandString(testString);
	
	Assert.assertEquals(results.parseSuccessful, true, "Full results object = " + results.toString());
	Assert.assertEquals(results.hasAdvantage, true, "Full results object = " + results.toString());
	Assert.assertEquals(results.hasDisadvantage, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.isWeary, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.modifier, 0, "Full results object = " + results.toString());
	Assert.assertEquals(results.numOfMastery, 0, "Full results object = " + results.toString());
	Assert.assertEquals(results.numOfSuccess, 3, "Full results object = " + results.toString());
	Assert.assertEquals(results.skillName, "Awe", "Full results object = " + results.toString());
	Assert.assertEquals(results.targetNumber, 14, "Full results object = " + results.toString());
  }
  
  @Test(groups = {"torRpgBotFull", "torDice", "parseString"})
  public void testParseCommandStringDisadvantageSimple() {
	
	String testString = "d 3 Awe";
	
	CommandResults results = testClass.publicParseCommandString(testString);
	
	Assert.assertEquals(results.parseSuccessful, true, "Full results object = " + results.toString());
	Assert.assertEquals(results.hasAdvantage, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.hasDisadvantage, true, "Full results object = " + results.toString());
	Assert.assertEquals(results.isWeary, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.modifier, 0, "Full results object = " + results.toString());
	Assert.assertEquals(results.numOfMastery, 0, "Full results object = " + results.toString());
	Assert.assertEquals(results.numOfSuccess, 3, "Full results object = " + results.toString());
	Assert.assertEquals(results.skillName, "Awe", "Full results object = " + results.toString());
	Assert.assertEquals(results.targetNumber, 14, "Full results object = " + results.toString());
  }
  
  @Test(groups = {"torRpgBotFull", "torDice", "parseString"})
  public void testParseCommandStringInvalidOption() {
	
	String testString = "t 3 Awe";
	
	CommandResults results = testClass.publicParseCommandString(testString);
	
	Assert.assertEquals(results.parseSuccessful, true, "Full results object = " + results.toString());
  }
  
  @Test(groups = {"torRpgBotFull", "torDice", "parseString"})
  public void testParseCommandStringWearyAdvantage() {
	
	String testString = "wa 3 Awe";
	
	CommandResults results = testClass.publicParseCommandString(testString);
	
	Assert.assertEquals(results.parseSuccessful, true, "Full results object = " + results.toString());
	Assert.assertEquals(results.hasAdvantage, true, "Full results object = " + results.toString());
	Assert.assertEquals(results.hasDisadvantage, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.isWeary, true, "Full results object = " + results.toString());
	Assert.assertEquals(results.modifier, 0, "Full results object = " + results.toString());
	Assert.assertEquals(results.numOfMastery, 0, "Full results object = " + results.toString());
	Assert.assertEquals(results.numOfSuccess, 3, "Full results object = " + results.toString());
	Assert.assertEquals(results.skillName, "Awe", "Full results object = " + results.toString());
	Assert.assertEquals(results.targetNumber, 14, "Full results object = " + results.toString());
  }
  
  @Test(groups = {"torRpgBotFull", "torDice", "parseString"})
  public void testParseCommandStringWearyDisadvantage() {
	
	String testString = "wd 3 Awe";
	
	CommandResults results = testClass.publicParseCommandString(testString);
	
	Assert.assertEquals(results.parseSuccessful, true, "Full results object = " + results.toString());
	Assert.assertEquals(results.hasAdvantage, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.hasDisadvantage, true, "Full results object = " + results.toString());
	Assert.assertEquals(results.isWeary, true, "Full results object = " + results.toString());
	Assert.assertEquals(results.modifier, 0, "Full results object = " + results.toString());
	Assert.assertEquals(results.numOfMastery, 0, "Full results object = " + results.toString());
	Assert.assertEquals(results.numOfSuccess, 3, "Full results object = " + results.toString());
	Assert.assertEquals(results.skillName, "Awe", "Full results object = " + results.toString());
	Assert.assertEquals(results.targetNumber, 14, "Full results object = " + results.toString());
  }
  
  @Test(groups = {"torRpgBotFull", "torDice", "parseString"})
  public void testParseCommandStringPositiveModifier1() {
	
	String testString = "3 + 1 Awe";
	
	CommandResults results = testClass.publicParseCommandString(testString);
	
	Assert.assertEquals(results.parseSuccessful, true, "Full results object = " + results.toString());
	Assert.assertEquals(results.hasAdvantage, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.hasDisadvantage, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.isWeary, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.modifier, 1, "Full results object = " + results.toString());
	Assert.assertEquals(results.numOfMastery, 0, "Full results object = " + results.toString());
	Assert.assertEquals(results.numOfSuccess, 3, "Full results object = " + results.toString());
	Assert.assertEquals(results.skillName, "Awe", "Full results object = " + results.toString());
	Assert.assertEquals(results.targetNumber, 14, "Full results object = " + results.toString());
  }
  
  @Test(groups = {"torRpgBotFull", "torDice", "parseString"})
  public void testParseCommandStringPositiveModifier2() {
	
	String testString = "3 +2 Awe";
	
	CommandResults results = testClass.publicParseCommandString(testString);
	
	Assert.assertEquals(results.parseSuccessful, true, "Full results object = " + results.toString());
	Assert.assertEquals(results.hasAdvantage, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.hasDisadvantage, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.isWeary, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.modifier, 2, "Full results object = " + results.toString());
	Assert.assertEquals(results.numOfMastery, 0, "Full results object = " + results.toString());
	Assert.assertEquals(results.numOfSuccess, 3, "Full results object = " + results.toString());
	Assert.assertEquals(results.skillName, "Awe", "Full results object = " + results.toString());
	Assert.assertEquals(results.targetNumber, 14, "Full results object = " + results.toString());
  }
  
  @Test(groups = {"torRpgBotFull", "torDice", "parseString"})
  public void testParseCommandStringNegativeModifier1() {
	
	String testString = "3 - 1 Awe";
	
	CommandResults results = testClass.publicParseCommandString(testString);
	
	Assert.assertEquals(results.parseSuccessful, true, "Full results object = " + results.toString());
	Assert.assertEquals(results.hasAdvantage, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.hasDisadvantage, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.isWeary, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.modifier, -1, "Full results object = " + results.toString());
	Assert.assertEquals(results.numOfMastery, 0, "Full results object = " + results.toString());
	Assert.assertEquals(results.numOfSuccess, 3, "Full results object = " + results.toString());
	Assert.assertEquals(results.skillName, "Awe", "Full results object = " + results.toString());
	Assert.assertEquals(results.targetNumber, 14, "Full results object = " + results.toString());
  }
  
  @Test(groups = {"torRpgBotFull", "torDice", "parseString"})
  public void testParseCommandStringnegativeModifier2() {
	
	String testString = "3 -2 Awe";
	
	CommandResults results = testClass.publicParseCommandString(testString);
	
	Assert.assertEquals(results.parseSuccessful, true, "Full results object = " + results.toString());
	Assert.assertEquals(results.hasAdvantage, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.hasDisadvantage, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.isWeary, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.modifier, -2, "Full results object = " + results.toString());
	Assert.assertEquals(results.numOfMastery, 0, "Full results object = " + results.toString());
	Assert.assertEquals(results.numOfSuccess, 3, "Full results object = " + results.toString());
	Assert.assertEquals(results.skillName, "Awe", "Full results object = " + results.toString());
	Assert.assertEquals(results.targetNumber, 14, "Full results object = " + results.toString());
  }
  
  @Test(groups = {"torRpgBotFull", "torDice", "parseString"})
  public void testParseCommandStringMasteryModifier() {
	
	String testString = "4 (2) -3 Awe";
	
	CommandResults results = testClass.publicParseCommandString(testString);
	
	Assert.assertEquals(results.parseSuccessful, true, "Full results object = " + results.toString());
	Assert.assertEquals(results.hasAdvantage, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.hasDisadvantage, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.isWeary, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.modifier, -3, "Full results object = " + results.toString());
	Assert.assertEquals(results.numOfMastery, 2, "Full results object = " + results.toString());
	Assert.assertEquals(results.numOfSuccess, 4, "Full results object = " + results.toString());
	Assert.assertEquals(results.skillName, "Awe", "Full results object = " + results.toString());
	Assert.assertEquals(results.targetNumber, 14, "Full results object = " + results.toString());
  }
  
  @Test(groups = {"torRpgBotFull", "torDice", "parseString"})
  public void testParseCommandStringTN2() {
	
	String testString = "3 Awe >16";
	
	CommandResults results = testClass.publicParseCommandString(testString);
	
	Assert.assertEquals(results.parseSuccessful, true, "Full results object = " + results.toString());
	Assert.assertEquals(results.hasAdvantage, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.hasDisadvantage, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.isWeary, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.modifier, 0, "Full results object = " + results.toString());
	Assert.assertEquals(results.numOfMastery, 0, "Full results object = " + results.toString());
	Assert.assertEquals(results.numOfSuccess, 3, "Full results object = " + results.toString());
	Assert.assertEquals(results.skillName, "Awe", "Full results object = " + results.toString());
	Assert.assertEquals(results.targetNumber, 16, "Full results object = " + results.toString());
  }
  
  @Test(groups = {"torRpgBotFull", "torDice", "parseString"})
  public void testParseCommandStringFullOptions() {
	
	String testString = "wa 3 (2) + 4 Awe > 20";
	
	CommandResults results = testClass.publicParseCommandString(testString);
	
	Assert.assertEquals(results.parseSuccessful, true, "Full results object = " + results.toString());
	Assert.assertEquals(results.hasAdvantage, true, "Full results object = " + results.toString());
	Assert.assertEquals(results.hasDisadvantage, false, "Full results object = " + results.toString());
	Assert.assertEquals(results.isWeary, true, "Full results object = " + results.toString());
	Assert.assertEquals(results.modifier, 4, "Full results object = " + results.toString());
	Assert.assertEquals(results.numOfMastery, 2, "Full results object = " + results.toString());
	Assert.assertEquals(results.numOfSuccess, 3, "Full results object = " + results.toString());
	Assert.assertEquals(results.skillName, "Awe", "Full results object = " + results.toString());
	Assert.assertEquals(results.targetNumber, 20, "Full results object = " + results.toString());
  }




  @Test(groups = {"torRpgBotFull", "torDice", "rollFeatTest"})
  public void rollFeatTestSimple() {
    diceProvider.setd12(new int[] {1, 2});
    
    CommandResults command = testClass.publicParseCommandString("2 Awe");
    command.hasAdvantage = false;
    command.hasDisadvantage = false;
    command.isWeary = false;
    
    int[] results = testClass.publicRollFeat(command, false);
    
    Assert.assertEquals(results.length, 1, results.toString());
    Assert.assertEquals(results[0], 1, results.toString());
  }
  
  @Test(groups = {"torRpgBotFull", "torDice", "rollFeatTest"})
  public void rollFeatTestWithAdvantageInequalLargerSecond() {
    diceProvider.setd12(new int[] {1, 2});
    
    CommandResults command = testClass.publicParseCommandString("2 Awe");
    command.hasAdvantage = true;
    command.hasDisadvantage = false;
    command.isWeary = false;
    
    int[] results = testClass.publicRollFeat(command, false);
    
    Assert.assertEquals(results.length, 2, results.toString());
    Assert.assertEquals(results[0], 1, results.toString());
    Assert.assertEquals(results[1], 2, results.toString());
  }
  
  @Test(groups = {"torRpgBotFull", "torDice", "rollFeatTest"})
  public void rollFeatTestWithAdvantageInequalLargerFirst() {
    diceProvider.setd12(new int[] {12, 5});
    
    CommandResults command = testClass.publicParseCommandString("2 Awe");
    command.hasAdvantage = true;
    command.hasDisadvantage = false;
    command.isWeary = false;
    
    int[] results = testClass.publicRollFeat(command, false);
    
    Assert.assertEquals(results.length, 2, results.toString());
    Assert.assertEquals(results[0], 12, results.toString());
    Assert.assertEquals(results[1], 5, results.toString());
  }
  
  @Test(groups = {"torRpgBotFull", "torDice", "rollFeatTest"})
  public void rollFeatTestWithAdvantageEqual() {
    diceProvider.setd12(new int[] {6, 6});
    
    CommandResults command = testClass.publicParseCommandString("2 Awe");
    command.hasAdvantage = true;
    command.hasDisadvantage = false;
    command.isWeary = false;
    
    int[] results = testClass.publicRollFeat(command, false);
    
    Assert.assertEquals(results.length, 2, results.toString());
    Assert.assertEquals(results[0], 6, results.toString());
    Assert.assertEquals(results[1], 6, results.toString());
  }
  
  @Test(groups = {"torRpgBotFull", "torDice", "rollFeatTest"})
  public void rollFeatTestWithAdvantageWithSauron() {
    diceProvider.setd12(new int[] {11, 6});
    
    CommandResults command = testClass.publicParseCommandString("2 Awe");
    command.hasAdvantage = true;
    command.hasDisadvantage = false;
    command.isWeary = false;
    
    int[] results = testClass.publicRollFeat(command, false);
    
    Assert.assertEquals(results.length, 2, results.toString());
    Assert.assertEquals(results[0], 0, results.toString());
    Assert.assertEquals(results[1], 6, results.toString());
  }
  
  @Test(groups = {"torRpgBotFull", "torDice", "rollFeatTest"})
  public void rollFeatTestWithDisadvantageInequalLargerSecond() {
    diceProvider.setd12(new int[] {1, 2});
    
    CommandResults command = testClass.publicParseCommandString("2 Awe");
    command.hasAdvantage = false;
    command.hasDisadvantage = true;
    command.isWeary = false;
    
    int[] results = testClass.publicRollFeat(command, false);
    
    Assert.assertEquals(results.length, 2, results.toString());
    Assert.assertEquals(results[0], 1, results.toString());
    Assert.assertEquals(results[1], 2, results.toString());
  }
  
  @Test(groups = {"torRpgBotFull", "torDice", "rollFeatTest"})
  public void rollFeatTestWithDisadvantageInequalLargerFirst() {
    diceProvider.setd12(new int[] {12, 5});
    
    CommandResults command = testClass.publicParseCommandString("2 Awe");
    command.hasAdvantage = false;
    command.hasDisadvantage = true;
    command.isWeary = false;
    
    int[] results = testClass.publicRollFeat(command, false);
    
    Assert.assertEquals(results.length, 2, results.toString());
    Assert.assertEquals(results[0], 12, results.toString());
    Assert.assertEquals(results[1], 5, results.toString());
  }
  
  @Test(groups = {"torRpgBotFull", "torDice", "rollFeatTest"})
  public void rollFeatTestWithDisadvantageEqual() {
    diceProvider.setd12(new int[] {6, 6});
    
    CommandResults command = testClass.publicParseCommandString("2 Awe");
    command.hasAdvantage = false;
    command.hasDisadvantage = true;
    command.isWeary = false;
    
    int[] results = testClass.publicRollFeat(command, false);
    
    Assert.assertEquals(results.length, 2, results.toString());
    Assert.assertEquals(results[0], 6, results.toString());
    Assert.assertEquals(results[1], 6, results.toString());
  }
  
  @Test(groups = {"torRpgBotFull", "torDice", "rollFeatTest"})
  public void rollFeatTestWithDisadvantageWithSauron() {
    diceProvider.setd12(new int[] {11, 6});
    
    CommandResults command = testClass.publicParseCommandString("2 Awe");
    command.hasAdvantage = false;
    command.hasDisadvantage = true;
    command.isWeary = false;
    
    int[] results = testClass.publicRollFeat(command, false);
    
    Assert.assertEquals(results.length, 2, results.toString());
    Assert.assertEquals(results[0], 0, results.toString());
    Assert.assertEquals(results[1], 6, results.toString());
  }
  
  @Test(groups = {"torRpgBotFull", "torDice", "rollFeatTest"})
  public void rollFeatTestSauronGandalf() {
    diceProvider.setd12(new int[] {11, 12});
    
    CommandResults command = testClass.publicParseCommandString("2 Awe");
    command.hasAdvantage = false;
    command.hasDisadvantage = true;
    command.isWeary = false;
    
    int[] results = testClass.publicRollFeat(command, false);
    
    Assert.assertEquals(results.length, 2, results.toString());
    Assert.assertEquals(results[0], 0, results.toString());
    Assert.assertEquals(results[1], 12, results.toString());
  }
  
  @Test(groups = {"torRpgBotFull", "torDice", "rollFeatTest"})
  public void rollFeatTestSauronGandalfAdversary() {
    diceProvider.setd12(new int[] {11, 12});
    
    CommandResults command = testClass.publicParseCommandString("2 Awe");
    command.hasAdvantage = false;
    command.hasDisadvantage = true;
    command.isWeary = false;
    
    int[] results = testClass.publicRollFeat(command, true);
    
    Assert.assertEquals(results.length, 2, results.toString());
    Assert.assertEquals(results[0], 11, results.toString());
    Assert.assertEquals(results[1], 0, results.toString());
  }
  
  

  @Test(groups = {"torRpgBotFull", "torDice", "rollSuccess"})
  public void rollSuccessSimple() {
    diceProvider.setd6(new int[] {1, 2, 3, 4, 5, 6, 3, 5, 2});
    
    int[] results = testClass.publicRollSuccess(3, 0);
    
    Assert.assertEquals(results.length, 3, results.toString());
    Assert.assertEquals(results[0], 3, results.toString());
    Assert.assertEquals(results[1], 2, results.toString());
    Assert.assertEquals(results[2], 1, results.toString());
  }
  
  @Test(groups = {"torRpgBotFull", "torDice", "rollSuccess"})
  public void rollSuccessWithMastery() {
    diceProvider.setd6(new int[] {6, 3, 3, 4, 5, 6, 3, 5, 2});
    
    int[] results = testClass.publicRollSuccess(3, 2);
    
    Assert.assertEquals(results.length, 5, results.toString());
    Assert.assertEquals(results[0], 6, results.toString());
    Assert.assertEquals(results[1], 5, results.toString());
    Assert.assertEquals(results[2], 4, results.toString());
    Assert.assertEquals(results[3], 3, results.toString());
    Assert.assertEquals(results[4], 3, results.toString());
  }
}
