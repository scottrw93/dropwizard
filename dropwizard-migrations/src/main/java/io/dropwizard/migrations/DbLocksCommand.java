package io.dropwizard.migrations;

import liquibase.Liquibase;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

public class DbLocksCommand extends AbstractLiquibaseCommand {
    public DbLocksCommand() {
        super("locks", "Manage database migration locks");
    }

    @Override
    public void configure(Subparser subparser) {
        subparser.addArgument("-l", "--list")
                 .dest("list")
                 .action(Arguments.storeTrue())
                 .setDefault(Boolean.FALSE)
                 .help("list all open locks");

        subparser.addArgument("-r", "--force-release")
                 .dest("release")
                 .action(Arguments.storeTrue())
                 .setDefault(Boolean.FALSE)
                 .help("forcibly release all open locks");
    }

    @Override
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void run(Namespace namespace, Liquibase liquibase) throws Exception {
        final Boolean list = namespace.getBoolean("list");
        final Boolean release = namespace.getBoolean("release");

        if (!list && !release) {
            throw new IllegalArgumentException("Must specify either --list or --force-release");
        } else if (list) {
            liquibase.reportLocks(System.out);
        } else {
            liquibase.forceReleaseLocks();
        }
    }
}
