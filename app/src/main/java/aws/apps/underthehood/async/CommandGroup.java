package aws.apps.underthehood.async;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Hashtable;

import androidx.annotation.StringRes;
import aws.apps.underthehood.R;

abstract class CommandGroup {
    private final CommandRunner commandRunner = new CommandRunner();
    private final Resources res;

    CommandGroup(Resources res) {
        this.res = res;
    }

    abstract ArrayList<String> execute(
            Hashtable<CharSequence, Boolean> action_list,
            boolean rooted);

    protected String getPromptSymbol(boolean root) {
        return root ? "#" : "$";
    }

    protected String getString(@StringRes int resId) {
        return res.getString(resId);
    }

    protected String execute(int commandString,
                             Hashtable<CharSequence, Boolean> action_list,
                             boolean isRooted) {
        final String cmd = getString(commandString);
        String result = "";

        if (action_list.get(cmd)) {
            if (isRooted) {
                result = commandRunner.executeAsRoot(cmd);
            } else {
                result = commandRunner.execute(cmd);
            }
        } else {
            result = getString(R.string.text_execution_skipped_by_user);
        }

        return result.trim();
    }

}
