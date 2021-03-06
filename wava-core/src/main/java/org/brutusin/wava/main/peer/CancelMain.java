/*
 * Copyright 2016 Ignacio del Valle Alles idelvall@brutusin.org.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.brutusin.wava.main.peer;

import org.brutusin.wava.utils.CoreUtils;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.brutusin.wava.core.io.CommandLineRequestExecutor;
import org.brutusin.wava.input.CancelInput;
import org.brutusin.wava.io.OpName;
import org.brutusin.wava.io.RequestExecutor;
import org.brutusin.wava.io.RetCode;

/**
 *
 * @author Ignacio del Valle Alles idelvall@brutusin.org
 */
public class CancelMain {

    public static final String DESCRIPTION = "cancel a running or enqueued job";

    private static CancelInput getRequest(String[] args) {
        Options options = new Options();
        Option cOpt = Option.builder("c")
                .argName("job id")
                .hasArg()
                .required()
                .build();
        options.addOption(cOpt);

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cl = parser.parse(options, args);

            int id;
            try {
                id = Integer.valueOf(cl.getOptionValue(cOpt.getOpt()));
            } catch (NumberFormatException ex) {
                throw new ParseException("Invalid job id value");
            }
            CancelInput ci = new CancelInput();
            ci.setId(id);
            return ci;
        } catch (ParseException exp) {
            System.err.println("Parsing failed.  Reason: " + exp.getMessage() + "\n");
            showHelp(options);
            return null;
        }
    }

    private static void showHelp(Options options) {
        CoreUtils.showHelp(options, "wava -c <job-id>\n" + DESCRIPTION);
    }

    public static void main(String[] args) throws Exception {
        CoreUtils.validateCoreRunning();
        CancelInput ci = getRequest(args);
        Integer retCode = null;
        if (ci != null) {
            retCode = new CommandLineRequestExecutor().executeRequest(OpName.cancel, ci);
        }
        if (retCode == null) {
            retCode = RetCode.ERROR.getCode();
        }
        System.exit(retCode);
    }
}
