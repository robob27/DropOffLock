/**
 * @author MrUnknown404
 * @source https://github.com/MrUnknown404/ItemFavorites
 * @license GNU General Public License v3.0
 * @modfiedBy robob27
 */

package robob27.mod.dropofflock.util.compat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import mrunknown404.unknownlibs.utils.MathUtils;
import robob27.mod.dropofflock.util.LockHandler;

public class InvTweaksCompat {
	public static final String START_LINE = "# DropOffLock Config ->";
	public static Path dir;
	
	public static void reload() {
		List<String> lockLines = new ArrayList<String>();
		for (int i = 0; i < 36; i++) {
			boolean has = LockHandler.isSlotLocked(i);
			
			String prefix = "null";
			if (has) {
				if (MathUtils.within(i, 0, 8)) {
					prefix = "D";
				} else if (MathUtils.within(i, 9, 17)) {
					prefix = "A";
				} else if (MathUtils.within(i, 18, 26)) {
					prefix = "B";
				} else if (MathUtils.within(i, 27, 35)) {
					prefix = "C";
				}
				
				lockLines.add(prefix + ((i % 9) + 1) + " LOCKED");
			}
		}
		
		try {
			List<String> lines = Files.readAllLines(dir, StandardCharsets.UTF_8);
			if (lines == null || lines.isEmpty()) {
				System.err.println("COULD NOT FIND 'InvTweaksRules.txt'!");
				return;
			}
			
      // ItemFavorites mod does not preserve changes from other mods here
      // if they appear after the ItemFavorites section so we need to
      // ensure our content appears before it consistently
			String itemFavoritesMarker = "# ItemFavorites Config (Warning! Everything past this line will be deleted!) ->";
			List<String> newLines = new ArrayList<>();
			int itemFavoritesIndex = lines.indexOf(itemFavoritesMarker);
			
			// Copy everything up to any special marker
			for (int i = 0; i < lines.size(); i++) {
				String line = lines.get(i);
				
				if (line.equals(START_LINE) || line.equals(itemFavoritesMarker)) {
					break;
				}
				newLines.add(line);
			}
			
			if (!newLines.isEmpty() && !newLines.get(newLines.size() - 1).isEmpty()) {
				newLines.add("");
			}
			
			// Add our section
			newLines.add(START_LINE);
			newLines.addAll(lockLines);
			
			// Add ItemFavorites section if it exists
			if (itemFavoritesIndex != -1) {
				newLines.add("");
				newLines.add(itemFavoritesMarker);
				
				// Get ItemFavorites content
				for (int i = itemFavoritesIndex + 1; i < lines.size(); i++) {
					if (lines.get(i).contains("Config") && lines.get(i).contains("->") 
						&& !lines.get(i).equals(itemFavoritesMarker)) {
						break;
					}
					newLines.add(lines.get(i));
				}
			}
			
			Files.write(dir, newLines, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
