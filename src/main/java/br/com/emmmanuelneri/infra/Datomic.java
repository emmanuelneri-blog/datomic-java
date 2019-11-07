package br.com.emmmanuelneri.infra;

import datomic.Connection;
import datomic.Peer;
import datomic.Util;
import lombok.NoArgsConstructor;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.List;

@NoArgsConstructor(staticName = "create")
public class Datomic {

    private static final String DATABASE_URI = "datomic:free://localhost:4334/datomic-java-db?password=admin";

    public Connection connect() {
        return Peer.connect(DATABASE_URI);
    }

    public void createDatabase() {
        Peer.createDatabase(DATABASE_URI);
    }

    public void setupSchema(final Connection connection, final String schema) {
        transactAllFromResource(connection, schema);
    }

    private static void transactAllFromResource(final Connection connection, final String resource) {
        try {
            final URL url = resource(resource);

            final InputStreamReader reader = new InputStreamReader(url.openStream());

            @SuppressWarnings("unchecked") final List<List> transacts = getTransacts(reader);
            for (final List transact : transacts) {
                connection.transact(transact).get();
            }
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static List getTransacts(final Reader reader) {
        return Util.readAll(reader);
    }

    private static URL resource(final String name) {
        return Thread.currentThread().getContextClassLoader().getResource(name);
    }

}
