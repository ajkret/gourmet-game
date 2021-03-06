package net.ajkret.gourmet;

import javax.swing.UIManager;
import lombok.SneakyThrows;
import net.ajkret.gourmet.view.GameInterface;
import net.ajkret.gourmet.view.SystemInterface;

public class App {

  @SneakyThrows
  public static void main(String[] args) {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    new GameInterface(new SystemInterface()).run();
  }
}
