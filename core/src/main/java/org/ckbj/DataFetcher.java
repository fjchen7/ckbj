package org.ckbj;

import org.ckbj.protocol.type.DetailedCell;

import java.io.IOException;
import java.util.List;

/**
 * Class to fetch data from CKB chain
 */
// TODO: find a better class name
public interface DataFetcher {
    List<DetailedCell> getLiveCellByAddress(String address) throws IOException;
}
