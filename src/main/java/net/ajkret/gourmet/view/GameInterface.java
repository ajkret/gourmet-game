package net.ajkret.gourmet.view;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import javax.swing.JOptionPane;
import net.ajkret.gourmet.Constants;
import net.ajkret.gourmet.exception.GameException;
import net.ajkret.gourmet.exception.RepositoryException;
import net.ajkret.gourmet.repository.PlateRepository;
import net.ajkret.gourmet.service.GameMachine;
import net.ajkret.gourmet.service.GameMachine.State;

public class GameInterface {

  private final Charset utf8charset = Charset.forName("UTF-8");
  private final Charset currentCharset = Charset.defaultCharset();
  private final GameMachine gameMachine;

  public GameInterface() {
    gameMachine = new GameMachine(new PlateRepository());
  }

  public void run() {
    while (true) {
      JOptionPane.showMessageDialog(null, "Pense em um prato que gosta", Constants.GAME, JOptionPane.PLAIN_MESSAGE);

      try {
        State state = guessCorrectType();
        guessCorrectPlate(state);
      } catch (GameException e) {
        addANewPlate();
      }
    }
  }

  private String toIso(final String message) {
    CharBuffer interim = utf8charset.decode(ByteBuffer.wrap(message.getBytes()));
    ByteBuffer result = currentCharset.encode(interim);
    return new String(result.array());
  }

  private void addANewPlate() {
    String plateName = JOptionPane.showInputDialog(null, toIso("Qual prato você pensou?"));
    String plateType = JOptionPane.showInputDialog(null, String.format(toIso("%s é ______?"),plateName));
    try {
      gameMachine.addPlate(plateName, plateType);
    } catch (RepositoryException e) {
      JOptionPane.showMessageDialog(null, e.getMessage(), Constants.GAME, JOptionPane.ERROR_MESSAGE);
    }
  }

  private State guessCorrectType() {
    int rightGuess = 0;
    State state = gameMachine.guess(null);
    do {
      rightGuess = JOptionPane.showConfirmDialog(null, String.format(toIso("O prato que você pensou é %s?"), state.getGuess()),
                                                 Constants.GAME, JOptionPane.YES_NO_OPTION);
      if (rightGuess == JOptionPane.NO_OPTION) {
        state = gameMachine.guess(state);
      }
    } while (rightGuess != JOptionPane.YES_OPTION);

    state.typeMatched();
    return state;
  }

  private void guessCorrectPlate(State state) {
    int rightGuess = 0;
    state = gameMachine.guess(state);
    do {
      rightGuess = JOptionPane.showConfirmDialog(null, String.format(toIso("O prato que você pensou é %s?"), state.getGuess()),
                                                 Constants.GAME, JOptionPane.YES_NO_OPTION);
      if (rightGuess == JOptionPane.NO_OPTION) {
        state = gameMachine.guess(state);
      }
    } while (rightGuess != JOptionPane.YES_OPTION);

    JOptionPane.showMessageDialog(null, "Acertei de novo!", Constants.GAME, JOptionPane.INFORMATION_MESSAGE);
  }
}
