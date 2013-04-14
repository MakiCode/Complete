package com.maki.UnhappyDevil.Screens;

import java.awt.Graphics;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.maki.UnhappyDevil.Drawable;

public class ErrorScreen implements Drawable {

  private Throwable myError;
  private JTextArea textArea;
  private JPanel panel;

  public ErrorScreen(Throwable error, JPanel panel) {
    myError = error;
    this.panel = panel;
    initTextArea();
  }

  private void initTextArea() {
    textArea = new JTextArea(75, 75);
    textArea.setLineWrap(true);
    JScrollPane scrollPane = new JScrollPane(textArea);
    textArea.setEditable(false);
    panel.add(scrollPane);
  }

  @Override
  public void paint(Graphics g) {
    TextAreaPrintStream textAreaOutputStream = new TextAreaPrintStream(textArea, System.out);
    myError.printStackTrace(textAreaOutputStream);
  }

  private static class TextAreaPrintStream extends PrintStream {

    // The JTextArea to wich the output stream will be redirected.
    private JTextArea textArea;

    /**
     * Method TextAreaPrintStream The constructor of the class.
     * 
     * @param the
     *          JTextArea to wich the output stream will be redirected.
     * @param a
     *          standard output stream (needed by super method)
     **/
    public TextAreaPrintStream(JTextArea area, OutputStream out) {
      super(out);
      textArea = area;
    }

    /**
     * Method println
     * 
     * @param the
     *          String to be output in the JTextArea textArea (private attribute
     *          of the class). After having printed such a String, prints a new
     *          line.
     **/
    public void println(String string) {
      textArea.append(string + "\n");
    }

    /**
     * Method print
     * 
     * @param the
     *          String to be output in the JTextArea textArea (private attribute
     *          of the class).
     **/
    public void print(String string) {
      textArea.append(string);
    }
  }

}
