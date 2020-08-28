package net.ajkret.gourmet.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;
import net.ajkret.gourmet.domain.Plate;
import net.ajkret.gourmet.exception.RepositoryException;
import org.junit.Before;
import org.junit.Test;

public class PlateRepositoryTest {

  private PlateRepository fixture;

  @Before
  public void init() {
    fixture = new PlateRepository();

    Plate plate = new Plate();
    plate.setName("Ravioli");
    plate.setType("pasta");
    fixture.add(plate);

    plate = new Plate();
    plate.setName("Cannelloni");
    plate.setType("pasta");
    fixture.add(plate);

    plate = new Plate();
    plate.setName("Chocolate Cake");
    plate.setType("cake");
    fixture.add(plate);

  }

  @Test
  public void shouldAcceptNewPlates() {
    Plate plate = new Plate();
    plate.setName("Farfalle");
    plate.setType("pasta");

    assertEquals(3, fixture.count());

    fixture.add(plate);

    assertEquals(4, fixture.count());
  }

  @Test(expected = RepositoryException.class)
  public void shouldTryEnterNullPlateAndFail() {
    fixture.add(null);
    fail("Should fail trying to inserting a null Plate");
  }

  @Test(expected = RepositoryException.class)
  public void shouldTryEnterNullPlateNameAndFail() {
    Plate plate = new Plate();
    fixture.add(plate);
    fail("Should fail trying to inserting a null Plate name");
  }

  @Test(expected = RepositoryException.class)
  public void shouldTryEnterNullPlateCategoryAndFail() {
    Plate plate = new Plate();
    plate.setName("Ravioli");
    fixture.add(plate);
    fail("Should fail trying to inserting a null Plate type");
  }

  @Test
  public void shouldListTypes() {
    List<String> result = fixture.listTypes();
    assertNotNull(result);
    assertEquals(2, result.size());
    assertTrue(result.contains("cake"));
  }

  @Test
  public void shouldGroupPlatesByType() {
    Map<String, List<Plate>> result = fixture.groupPlatesByType();

    assertNotNull(result);
    assertEquals(2, result.keySet().size());
    assertTrue(result.keySet().contains("cake"));
    assertNotNull(result.get("pasta"));
    assertEquals(2, result.get("pasta").size());
  }

  @Test
  public void shouldFindPlateByName() {
    Plate result = fixture.findByName("Ravioli");
    assertNotNull(result);
  }

  @Test
  public void shouldFindPlateByType() {
    List<Plate> result = fixture.findByType("pasta");
    assertNotNull(result);
    assertEquals(2, result.size());
  }

}
