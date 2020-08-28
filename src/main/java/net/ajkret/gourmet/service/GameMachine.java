package net.ajkret.gourmet.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import net.ajkret.gourmet.Constants;
import net.ajkret.gourmet.domain.Plate;
import net.ajkret.gourmet.exception.GameException;
import net.ajkret.gourmet.repository.PlateRepository;

public class GameMachine {

  private final PlateRepository repository;

  public GameMachine(final PlateRepository repository) {
    this.repository = repository;
  }

  public State guess(State state) {
    // New guess
    if (state == null) {
      return startToGuessType();
    }

    if (GuessType.TYPE.equals(state.getGuessType())) {
      return guessNextType(state);
    }

    return guessNextPlate(state);
  }

  private State startToGuessType() {
    State state = new State();
    state.setGuess(getFirstTypeOrInitRepo());
    state.setGuessType(GuessType.TYPE);
    return state;
  }

  private String getFirstTypeOrInitRepo() {
    List<String> types = repository.listTypes();
    if (types.isEmpty()) {
      types = initializeTypes();
    }
    return types.get(0);
  }

  private List<String> initializeTypes() {
    Plate plate = new Plate();
    plate.setType("bolo");
    plate.setName("Bolo de Chocolate");
    repository.add(plate);

    return repository.listTypes();
  }

  // Look for the next type
  private State guessNextType(final State state) {
    List<String> types = repository.listTypes();

    state.setGuess(types.stream().filter(item -> !state.getNames().contains(item)).findFirst().orElseThrow(() -> new GameException(Constants.OUT_OF_GUESSES)));
    state.getNames().add(state.getGuess());
    return state;
  }
  // Look for the next Plate of type
  private State guessNextPlate(final State state) {
    List<String> types = repository.findByType(state.getType()).stream().map(Plate::getName).collect(Collectors.toList());

    state.setGuess(types.stream().filter(item -> !state.getNames().contains(item)).findFirst().orElseThrow(() -> new GameException(Constants.OUT_OF_GUESSES)));
    state.getNames().add(state.getGuess());
    return state;
  }

  @Getter
  @Setter
  public static class State {

    private String guess;
    private GuessType guessType;
    private String type;
    private List<String> names = new ArrayList<>();

    public void typeMatched() {
      names = new ArrayList<>();
      type = guess;
      guessType = GuessType.PLATE;
    }
  }

  public static enum GuessType {
    TYPE, PLATE
  }

}
