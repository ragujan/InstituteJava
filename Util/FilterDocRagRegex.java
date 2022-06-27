/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;

import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 *
 * @author acer
 */
public class FilterDocRagRegex {

    String regex;
    JTextField jt;

    public FilterDocRagRegex(JTextField jt, String regex) {
        this.regex = regex;
        this.jt = jt;
        setDocFilter();
    }
    public FilterDocRagRegex(JTextField jt, String regex,int length) {
        this.regex = regex;
        this.jt = jt;
        setDocFilter(length);
    }
    public void setDocFilter() {
        if (jt != null) {

            AbstractDocument ab = (AbstractDocument) jt.getDocument();
            ab.setDocumentFilter(new DocumentFilter() {
                @Override
                public void remove(DocumentFilter.FilterBypass fb, int offset, int length) throws
                        BadLocationException {

                    fb.remove(offset, length);
                }

                @Override
                public void insertString(DocumentFilter.FilterBypass fb, int offset, String string,
                        AttributeSet attr) throws BadLocationException {
                    fb.insertString(offset, string, attr);
                }

                @Override
                public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text,
                        AttributeSet attrs) throws BadLocationException {
                    String jtText = fb.getDocument().getText(0, fb.getDocument().getLength());
                    String newText = jtText.substring(0, offset) + text;
                    //regex = "([1-9][0-9]+|[1-9])";
                    boolean match = Util.BasicValidator.regexMatcher(newText, regex);

                    if (match || newText.equals("")) {
                        fb.replace(offset, length, text, attrs);
                    } else {

                    }

                }
            });
        }
    }
        public void setDocFilter(int length) {
        if (jt != null) {

            AbstractDocument ab = (AbstractDocument) jt.getDocument();
            ab.setDocumentFilter(new DocumentFilter() {
                @Override
                public void remove(DocumentFilter.FilterBypass fb, int offset, int length) throws
                        BadLocationException {

                    fb.remove(offset, length);
                }

                @Override
                public void insertString(DocumentFilter.FilterBypass fb, int offset, String string,
                        AttributeSet attr) throws BadLocationException {
                    fb.insertString(offset, string, attr);
                }

                @Override
                public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text,
                        AttributeSet attrs) throws BadLocationException {
                    String jtText = fb.getDocument().getText(0, fb.getDocument().getLength());
                    String newText = jtText.substring(0, offset) + text;
                    //regex = "([1-9][0-9]+|[1-9])";
                    boolean match = Util.BasicValidator.regexMatcher(newText, regex);

                    if (match || newText.equals("") || offset <= length) {
                        fb.replace(offset, length, text, attrs);
                    } else {

                    }

                }
            });
        }
    }
}
