package net.ajkret.gourmet.view;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import javax.swing.JOptionPane;
import net.ajkret.gourmet.Constants;

public class SystemInterface {

  private final Charset utf8charset = Charset.forName("UTF-8");
  private final Charset currentCharset = Charset.defaultCharset();

  private String toIso(final String message) {
    CharBuffer interim = utf8charset.decode(ByteBuffer.wrap(message.getBytes()));
    ByteBuffer result = currentCharset.encode(interim);
    return new String(result.array());
  }

  public void showMessage(final String message) {
    JOptionPane.showMessageDialog(null, toIso(message), Constants.GAME, JOptionPane.PLAIN_MESSAGE);
  }

  public int showYesOrNo(final String message) {
    return JOptionPane.showConfirmDialog(null, toIso(message), Constants.GAME, JOptionPane.YES_NO_OPTION);
  }

  public String ask(final String message) {
    return ask(message, null);
  }

  public String ask(final String message, String initialValue) {
    if (initialValue == null) {
      return JOptionPane.showInputDialog(null, toIso(message));
    }
    return JOptionPane.showInputDialog(null, toIso(message), initialValue);
  }

  public void showError(final String message) {
    JOptionPane.showMessageDialog(null, message, Constants.GAME, JOptionPane.ERROR_MESSAGE);
  }
}
