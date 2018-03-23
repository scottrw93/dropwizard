package io.dropwizard.migrations;

import com.google.common.base.Joiner;
import liquibase.Liquibase;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class DbPrepareRollbackCommand extends AbstractLiquibaseCommand {
    public DbPrepareRollbackCommand() {
        super("prepare-rollback", "Generate rollback DDL scripts for pending change sets.");
    }

    @Override
    public void configure(Subparser subparser) {
        subparser.addArgument("-c", "--count")
                 .dest("count")
                 .type(Integer.class)
                 .help("limit script to the specified number of pending change sets");

        subparser.addArgument("-i", "--include")
                 .action(Arguments.append())
                 .dest("contexts")
                 .help("include change sets from the given context");
    }

    @Override
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void run(Namespace namespace, Liquibase liquibase) throws Exception {
        final String context = getContext(namespace);
        final Integer count = namespace.getInt("count");
        if (count != null) {
            liquibase.futureRollbackSQL(count, context, new OutputStreamWriter(System.out, StandardCharsets.UTF_8));
        } else {
            liquibase.futureRollbackSQL(context, new OutputStreamWriter(System.out, StandardCharsets.UTF_8));
        }
    }

    private String getContext(Namespace namespace) {
        final List<Object> contexts = namespace.getList("contexts");
        if (contexts == null) {
            return "";
        }
        return Joiner.on(',').join(contexts);
    }
}
