package intentional.assess.fetch;

import java.util.List;

import intentional.assess.CubeManagerAdapter;

/**
 * Materializes the cubes an assess query needs — the target and one benchmark per AGAINST entry — from the
 * adapter's query template. Implementations differ only in how many scans they spend; the cubes they
 * return are identical.
 */
public interface AssessFetcher {

    String name();

    FetchedCubes fetch(CubeManagerAdapter adapter, List<List<String>> benchmarkDetails);
}
