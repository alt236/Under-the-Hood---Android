package aws.apps.underthehood.async;

import aws.apps.underthehood.util.ExecTerminal;

class CommandRunner {
    private static final String NO_OUTPUT = "result was blank";

    public String execute(String cmd) {
        final ExecTerminal et = new ExecTerminal();
        final String res = et.exec(cmd);

        return sanitise(res);
    }

    public String executeAsRoot(String cmd) {
        final ExecTerminal et = new ExecTerminal();
        final String res = et.execSu(cmd);

        return sanitise(res);
    }

    private String sanitise(String result) {
        if (result.trim().equals("")) {
            return NO_OUTPUT;
        } else {
            return result;
        }
    }
}
