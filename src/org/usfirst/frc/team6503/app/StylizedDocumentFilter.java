package org.usfirst.frc.team6503.app;

import java.awt.Color;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class StylizedDocumentFilter extends DocumentFilter {

	private final JTextPane textPane;
	private final StyledDocument styledDocument;

	private final List<StyledPattern> patterns;

	private static final StyleContext styleContext = StyleContext.getDefaultStyleContext();
	private static final AttributeSet DEFAULT_BLACK = styleContext.addAttribute(styleContext.getEmptySet(),
			StyleConstants.Foreground, Color.BLACK);
	private static final AttributeSet DEFAULT_UNITALICIZED = styleContext.addAttribute(DEFAULT_BLACK,
			StyleConstants.Italic, false);
	private static final AttributeSet DEFAULT_ATTRIB = styleContext.addAttribute(DEFAULT_UNITALICIZED,
			StyleConstants.Bold, false);

	public StylizedDocumentFilter(JTextPane pane, List<StyledPattern> patterns) {
		this.textPane = pane;
		this.styledDocument = pane.getStyledDocument();
		this.patterns = patterns;
	}

	@Override
	public void insertString(FilterBypass fb, int offset, String text, AttributeSet attributeSet)
			throws BadLocationException {
		super.insertString(fb, offset, text, attributeSet);

		handleTextChanged();
	}

	@Override
	public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
		super.remove(fb, offset, length);

		handleTextChanged();
	}

	@Override
	public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attributeSet)
			throws BadLocationException {
		super.replace(fb, offset, length, text, attributeSet);

		handleTextChanged();
	}

	private void handleTextChanged() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				updateTextStyles();
			}
		});
	}

	private void updateTextStyles() {
		styledDocument.setCharacterAttributes(0, textPane.getText().length(), DEFAULT_ATTRIB, true);
		for (StyledPattern styledPattern : patterns) {
			Pattern pattern = styledPattern.pattern;
			AttributeSet style = styledPattern.style;

			int total = 0;
			String[] lines = textPane.getText().split("\r\n|\r|\n");
			for (String str : lines) {
				Matcher matcher = pattern.matcher(str);
				while (matcher.find()) {
					styledDocument.setCharacterAttributes(matcher.start() + total, matcher.end() - matcher.start(),
							style, false);
				}
				total += str.length() + 1;
			}
		}
	}

	public static class StyledPattern {

		public final Pattern pattern;
		public final AttributeSet style;

		public StyledPattern(String pattern, AttributeSet style) {
			this(Pattern.compile(pattern), style);
		}

		public StyledPattern(Pattern pattern, AttributeSet style) {
			this.pattern = pattern;
			this.style = style;
		}
	}
}
