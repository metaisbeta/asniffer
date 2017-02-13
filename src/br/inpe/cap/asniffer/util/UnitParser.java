package br.inpe.cap.asniffer.util;

import java.util.StringTokenizer;

import br.inpe.cap.asniffer.exceptions.FileFormatException;

public class UnitParser {

	public static Integer numberOfLinesOfCode(String source) throws FileFormatException {
		StringTokenizer lines = new StringTokenizer(source, "\n");
		int loc = 0, currentLine = 0;
		while (lines.hasMoreTokens()) {
			currentLine++;
			String trimmed = lines.nextToken().trim();
			if (trimmed.length() == 0)
				continue;
			if (trimmed.startsWith("/*")) {
				while (trimmed.indexOf("*/") == -1)
					if (lines.hasMoreTokens()) {
						trimmed = lines.nextToken().trim();
						currentLine++;
					} else
						throw new FileFormatException("BAD FORMATTED FILE", currentLine);
			}
			if (!(trimmed.startsWith("//")) && trimmed.indexOf("*/") == -1)
				++loc;
		}
		return loc;
	}
	
	
}
