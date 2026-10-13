package cubemanager.queryoptimizer.selectivityestimation;

import cubemanager.cubebase.Dimension;
import cubemanager.cubebase.Level;
import cubemanager.cubebase.LinearHierarchy;

import java.util.List;

/**
 * Parses a sigma predicate (e.g. "account_dim.lvl2 = 'Prague'") into the physical
 * database column and join info needed to query it.
 */
public class SigmaParser {

	public static class ParsedSigma {
		final String filterCol;
		final String dimTable;
		final String factFK;
		final String dimPK;

		ParsedSigma(String filterCol, String dimTable, String factFK, String dimPK) {
			this.filterCol = filterCol;
			this.dimTable = dimTable;
			this.factFK = factFK;
			this.dimPK = dimPK;
		}
	}

	/**
	 * Resolves sigma[0] (e.g. "account_dim.lvl2") to the physical column and join info.
	 *
	 * @param sigma sigma expression: [0]=dimName.levelName, [1]=operator, [2]=value
	 * @param dimensions dimension list from the cube
	 * @param dimRefFields FK column list, parallel to dimensions
	 * @return resolved column info, or null if sigma cannot be matched
	 */
	static ParsedSigma parse(String[] sigma, List<Dimension> dimensions, List<String> dimRefFields) {
		if (sigma == null || sigma[0] == null) {
			return null;
		}

		String[] parts = sigma[0].split("\\.");
		if (parts.length < 2) return null;

		String dimName = parts[0].trim();
		String levelName = parts[1].trim();

		for (int i = 0; i < dimensions.size(); i++) {
			Dimension dimension = dimensions.get(i);
			if (!dimension.hasSameName(dimName)) continue;

			String dimTable = dimension.getTableName();
			String factFK = dimRefFields.get(i);
			String dimPK = dimTable + "."
					+ ((LinearHierarchy) dimension.getHierarchy().get(0)).getLevels().get(0).getAttributeName(0);

			for (Level level : ((LinearHierarchy) dimension.getHierarchy().get(0)).getLevels()) {
				if (level.getName().equals(levelName)) {
					return new ParsedSigma(dimTable + "." + level.getAttributeName(0), dimTable, factFK, dimPK);
				}
			}
			break; // dimension matched but level not found — no point checking others
		}
		return null;
	}
}
