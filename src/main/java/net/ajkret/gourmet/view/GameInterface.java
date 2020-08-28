package net.ajkret.gourmet.view;

import java.util.Optional;
import javax.swing.JOptionPane;
import lombok.extern.slf4j.Slf4j;
import net.ajkret.gourmet.Constants;
import net.ajkret.gourmet.exception.EndGameException;
import net.ajkret.gourmet.exception.GameException;
import net.ajkret.gourmet.exception.RepositoryException;
import net.ajkret.gourmet.repository.PlateRepository;
import net.ajkret.gourmet.service.GameMachine;
import net.ajkret.gourmet.service.GameMachine.State;

public class GameInterface {

  private final GameMachine gameMachine;
  private final SystemInterface systemInterface;

  public GameInterface(final SystemInterface systemInterface) {
    this.gameMachine = new GameMachine(new PlateRepository());
    this.systemInterface = systemInterface;
  }

  public void run() {
    boolean gameRunning = true;
    while (gameRunning) {
      systemInterface.showMessage("Pense em um prato que gosta");

      State state = null;
      try {
        state = guessCorrectType();
        guessCorrectPlate(state);
      } catch (GameException e) {
        addANewPlate(Optional.ofNullable(state).map(State::getType).orElse(null));
      }
      catch(EndGameException e) {
        System.out.printf("Fim de jogo");
        gameRunning=false;
      }
    }
  }

  private void addANewPlate(String type) {
    String plateName = systemInterface.ask("Qual prato você pensou?");
    String plateType = systemInterface.ask(String.format("%s é ______?", plateName), type);
    try {
      gameMachine.addPlate(plateName, plateType);
    } catch (RepositoryException e) {
      systemInterface.showError(e.getMessage());
    }
  }

  private State guessCorrectType() {
    int rightGuess = 0;
    State state = gameMachine.guess(null);
    do {
      rightGuess = systemInterface.showYesOrNo(String.format("O prato que você pensou é %s?", state.getGuess()));
      if (rightGuess == JOptionPane.NO_OPTION) {
        state = gameMachine.guess(state);
      }
      if(rightGuess==JOptionPane.CLOSED_OPTION) {
        throw new EndGameException(Constants.END_GAME);
      }

    } while (rightGuess != JOptionPane.YES_OPTION);


    state.typeMatched();
    return state;
  }

  private void guessCorrectPlate(State state) {
    int rightGuess = 0;
    state = gameMachine.guess(state);
    do {
      rightGuess = systemInterface.showYesOrNo(String.format("O prato que você pensou é %s?", state.getGuess()));
      if (rightGuess == JOptionPane.NO_OPTION) {
        state = gameMachine.guess(state);
      }

      if(rightGuess==JOptionPane.CLOSED_OPTION) {
        throw new EndGameException(Constants.END_GAME);
      }
    } while (rightGuess != JOptionPane.YES_OPTION);

    systemInterface.showMessage("Acertei de novo!");
  }
}
