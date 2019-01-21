package org.usfirst.frc.team6503.app;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.AttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import org.usfirst.frc.team6503.app.StylizedDocumentFilter.StyledPattern;

public class DefaultStyleFilter {
	private static final StyleContext styleContext = StyleContext.getDefaultStyleContext();

	public static List<StyledPattern> getInstance() {
		List<StyledPattern> list = new ArrayList<>();
		// Numbers
		final AttributeSet numberColorStyle = styleContext.addAttribute(styleContext.getEmptySet(),
				StyleConstants.Foreground, Color.ORANGE);
		final AttributeSet numberStyle = styleContext.addAttribute(numberColorStyle, StyleConstants.Bold, true);
		list.add(new StyledPattern("\\b*\\d\\b*", numberStyle));

		// Brackets
		final AttributeSet bracketColorStyle = styleContext.addAttribute(styleContext.getEmptySet(),
				StyleConstants.Foreground, Color.BLUE);
		final AttributeSet bracketStyle = styleContext.addAttribute(bracketColorStyle, StyleConstants.Bold, true);
		list.add(new StyledPattern("\\b*[{}\\[\\]]\\b*", bracketStyle));

		// Keywords
		final AttributeSet keywordColorStyle = styleContext.addAttribute(styleContext.getEmptySet(),
				StyleConstants.Foreground, Color.MAGENTA);
		final AttributeSet keywordStyle = styleContext.addAttribute(keywordColorStyle, StyleConstants.Bold, true);
		list.add(new StyledPattern("\\b*(joysticks|controllers|gyros|camera|diffdrive|teleOperated|autonomous|,)\\b*",
				keywordStyle));

		// Attributes
		final AttributeSet attributeColorStyle = styleContext.addAttribute(styleContext.getEmptySet(),
				StyleConstants.Foreground, Color.DARK_GRAY);
		final AttributeSet attributeStyle = styleContext.addAttribute(attributeColorStyle, StyleConstants.Italic, true);
		list.add(new StyledPattern("\\b*(resx|resy|port|name|sparks|init|periodic)\\b*", attributeStyle));
		return list;
	}
}
