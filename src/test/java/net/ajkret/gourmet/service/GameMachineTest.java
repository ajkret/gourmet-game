package net.ajkret.gourmet.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import net.ajkret.gourmet.domain.Plate;
import net.ajkret.gourmet.exception.GameException;
import net.ajkret.gourmet.repository.PlateRepository;
import net.ajkret.gourmet.service.GameMachine.GuessType;
import net.ajkret.gourmet.service.GameMachine.State;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class GameMachineTest {

  private GameMachine fixture;
  private PlateRepository repository;
  private Plate cake;
  private Plate ravioli;

  @Before
  public void init() {
    repository = mock(PlateRepository.class);

    when(repository.listTypes()).thenReturn(Arrays.asList("cake", "pasta"));
    cake = new Plate();
    cake.setType("cake");
    cake.setName("Chocolate Cake");

    ravioli = new Plate();
    ravioli.setType("pasta");
    ravioli.setName("Ravioli");

    when(repository.findByType(eq(cake.getType()))).thenReturn(Arrays.asList(cake));
    when(repository.findByType(eq(ravioli.getType()))).thenReturn(Arrays.asList(ravioli));

    fixture = new GameMachine(repository);
  }

  @Test
  public void shouldInitializeRepoAndGiveFirstGuess() {
    when(repository.listTypes()).thenReturn(Arrays.asList()).thenReturn(Arrays.asList(cake.getType()));
    State result = fixture.guess(null);

    Mockito.verify(repository).add(any(Plate.class));

    assertNotNull(result);

    assertEquals(cake.getType(), result.getGuess());
    assertEquals(GameMachine.GuessType.TYPE, result.getGuessType());
  }

  @Test
  public void shouldStartNewGame() {
    State result = fixture.guess(null);
    assertNotNull(result);

    assertEquals(cake.getType(), result.getGuess());
    assertEquals(GameMachine.GuessType.TYPE, result.getGuessType());
  }

  @Test
  public void shouldGuessNextType() {
    State state = new State();
    state.getNames().add("cake");
    state.setGuessType(GuessType.TYPE);

    State result = fixture.guess(state);
    assertNotNull(result);

    assertEquals(ravioli.getType(), result.getGuess());
    assertEquals(GameMachine.GuessType.TYPE, result.getGuessType());
  }

  @Test(expected = GameException.class)
  public void shouldRunOutOfGuessesForType() {
    State state = new State();
    state.getNames().addAll(Arrays.asList(cake.getType(), ravioli.getType()));
    state.setGuessType(GuessType.TYPE);

    fixture.guess(state);
    fail();
  }

  @Test
  public void shouldGuessNextPlateAfterSuccessfulGuessOnType() {
    State state = new State();
    state.setGuess(cake.getType());
    state.getNames().add(cake.getType());
    state.setGuessType(GuessType.TYPE);
    state.typeMatched();

    State result = fixture.guess(state);
    assertNotNull(result);

    assertEquals(cake.getName(), result.getGuess());
    assertEquals(GuessType.PLATE, result.getGuessType());
  }

  @Test
  public void shouldGuessNextPlate() {
    State state = new State();
    state.setType(ravioli.getType());
    state.setGuessType(GuessType.PLATE);

    State result = fixture.guess(state);
    assertNotNull(result);

    assertEquals(ravioli.getName(), result.getGuess());
    assertEquals(GameMachine.GuessType.PLATE, result.getGuessType());
  }

  @Test(expected = GameException.class)
  public void shouldRunOutOfGuessesForPlate() {
    State state = new State();
    state.setType("pasta");
    state.getNames().add(ravioli.getName());

    state.setGuessType(GuessType.PLATE);

    fixture.guess(state);
    fail();
  }


}
