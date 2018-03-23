package io.dropwizard.migrations;

import liquibase.Liquibase;
import liquibase.change.CheckSum;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbCalculateChecksumCommand extends AbstractLiquibaseCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger("liquibase");

    public DbCalculateChecksumCommand() {
        super("calculate-checksum", "Calculates and prints a checksum for a change set");
    }

    @Override
    public void configure(Subparser subparser) {
        subparser.addArgument("id").nargs(1).help("change set id");
        subparser.addArgument("author").nargs(1).help("author name");
    }

    @Override
    public void run(Namespace namespace,
                    Liquibase liquibase) throws Exception {
        final CheckSum checkSum = liquibase.calculateCheckSum("migrations.xml",
                                                              namespace.<String>getList("id").get(0),
                                                              namespace.<String>getList("author").get(0));
        LOGGER.info("checksum = {}", checkSum);
    }
}
