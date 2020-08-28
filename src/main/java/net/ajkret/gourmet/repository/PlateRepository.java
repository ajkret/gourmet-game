package net.ajkret.gourmet.repository;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import net.ajkret.gourmet.Constants;
import net.ajkret.gourmet.domain.Plate;
import net.ajkret.gourmet.exception.RepositoryException;

public class PlateRepository {

  private final Map<String, Plate> plates = new ConcurrentHashMap<>();

  public Plate findByName(final String name) {
    return plates.get(name);
  }

  public void add(final Plate plate) {
    if (plate == null || plate.getName() == null) {
      throw new RepositoryException(Constants.INVALID_PLATE);
    }
    if (plate.getType() == null) {
      throw new RepositoryException(Constants.INVALID_TYPE);
    }
    plates.put(plate.getName(), plate);
  }

  public int count() {
    return plates.size();
  }

  public List<String> listTypes() {
    return plates.values()
                 .stream()
                 .map(Plate::getType)
                 .collect(Collectors.toSet())
                 .stream()
                 .collect(Collectors.toList());
  }

  public Map<String, List<Plate>> groupPlatesByType() {
    return plates.values().stream().collect(Collectors.groupingBy(Plate::getType));
  }

  public List<Plate> findByType(final String type) {
    return Optional.ofNullable(type)
                   .map(filter -> plates.values()
                                        .stream()
                                        .filter(plate -> plate.getType().equalsIgnoreCase(filter))
                                        .collect(Collectors.toList()))
                   .orElse(Arrays.asList());
  }
}
