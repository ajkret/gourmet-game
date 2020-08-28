package net.ajkret.gourmet.view;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.swing.JOptionPane;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class GameInterfaceTest {

  private SystemInterface systemInterface;
  private GameInterface fixture;

  @Before
  public void init() {
    systemInterface = mock(SystemInterface.class);

    when(systemInterface.ask(anyString(), nullable(String.class))).thenReturn("cake");
    when(systemInterface.ask(anyString())).thenReturn("Chocolate Cake");
    when(systemInterface.showYesOrNo(anyString())).thenReturn(JOptionPane.YES_OPTION).thenReturn(JOptionPane.YES_OPTION).thenReturn(JOptionPane.CLOSED_OPTION);

    fixture = new GameInterface(systemInterface);
  }

  @Test
  public void shouldPerformPerfectScenario() {
    fixture.run();

    verify(systemInterface,times(3)).showMessage(anyString());
    verify(systemInterface,Mockito.times(3)).showYesOrNo(anyString());
  }

  @Test
  public void shouldEndGameOnTypeGuess() {
    when(systemInterface.showYesOrNo(anyString())).thenReturn(JOptionPane.CLOSED_OPTION);
    fixture.run();

    verify(systemInterface,times(1)).showMessage(anyString());
    verify(systemInterface,Mockito.times(1)).showYesOrNo(anyString());
  }

  @Test
  public void shouldEndGameOnPlateGuess() {
    when(systemInterface.showYesOrNo(anyString())).thenReturn(JOptionPane.YES_OPTION).thenReturn(JOptionPane.CLOSED_OPTION);
    fixture.run();

    verify(systemInterface,times(1)).showMessage(anyString());
    verify(systemInterface,Mockito.times(2)).showYesOrNo(anyString());
  }

  @Test
  public void shouldAskNewPlateAfterOutOfTypeGuesses() {
    when(systemInterface.showYesOrNo(anyString())).thenReturn(JOptionPane.NO_OPTION).thenReturn(JOptionPane.CLOSED_OPTION);
    fixture.run();

    verify(systemInterface,times(2)).showMessage(anyString());
    verify(systemInterface,Mockito.times(2)).showYesOrNo(anyString());
    verify(systemInterface).ask(anyString());
    verify(systemInterface).ask(contains("Chocolate Cake"), nullable(String.class));
  }

  @Test
  public void shouldAskNewPlateAfterOutOfPlateGuesses() {
    when(systemInterface.showYesOrNo(anyString())).thenReturn(JOptionPane.YES_OPTION).thenReturn(JOptionPane.NO_OPTION).thenReturn(JOptionPane.CLOSED_OPTION);
    fixture.run();

    verify(systemInterface,times(2)).showMessage(anyString());
    verify(systemInterface,Mockito.times(3)).showYesOrNo(anyString());
    verify(systemInterface).ask(anyString());
    verify(systemInterface).ask(contains("Chocolate Cake"), eq("bolo"));
  }


}
