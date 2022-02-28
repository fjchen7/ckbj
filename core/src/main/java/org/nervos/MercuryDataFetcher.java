package org.nervos;

import org.nervos.protocol.type.DetailedCell;

import java.io.IOException;
import java.util.List;

public class MercuryDataFetcher implements DataFetcher {
    private Mercury mercury;

    public MercuryDataFetcher(String url) {
    }

    @Override
    public List<DetailedCell> getLiveCellByAddress(String address) throws IOException {
        return mercury.getLiveCellsByAddress(address).send().getResult();
    }
}
