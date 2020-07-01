package aws.apps.underthehood.async;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Hashtable;

import aws.apps.underthehood.R;

public class ProcCommands extends CommandGroup {

    ProcCommands(final Resources res) {
        super(res);
    }

    public ArrayList<String> execute(Hashtable<CharSequence, Boolean> action_list, boolean isRooted) {
        final ArrayList<String> l = new ArrayList<>();
        final String promptSymbol = getPromptSymbol(isRooted);

        l.add(getString(R.string.seperator_identifier) + "General Info");

        l.add(promptSymbol + getString(R.string.shell_cat_proc_version));
        l.add(execute(R.string.shell_cat_proc_version, action_list, isRooted));

        l.add(promptSymbol + getString(R.string.shell_cat_proc_meminfo));
        l.add(execute(R.string.shell_cat_proc_meminfo, action_list, isRooted));

        l.add(getString(R.string.seperator_identifier) + "Hardware Info");

        l.add(promptSymbol + getString(R.string.shell_cat_proc_devices));
        l.add(execute(R.string.shell_cat_proc_devices, action_list, isRooted));

        l.add(promptSymbol + getString(R.string.shell_cat_proc_mounts));
        l.add(execute(R.string.shell_cat_proc_mounts, action_list, isRooted));

        l.add(getString(R.string.seperator_identifier) + "Network Info");

        l.add(promptSymbol + getString(R.string.shell_cat_proc_net_arp));
        l.add(execute(R.string.shell_cat_proc_net_arp, action_list, isRooted));

        l.add(promptSymbol + getString(R.string.shell_cat_proc_net_route));
        l.add(execute(R.string.shell_cat_proc_net_route, action_list, isRooted));

        l.add(promptSymbol + getString(R.string.shell_cat_proc_net_wireless));
        l.add(execute(R.string.shell_cat_proc_net_wireless, action_list, isRooted));

        l.add(promptSymbol + getString(R.string.shell_cat_proc_net_if_inet6));
        l.add(execute(R.string.shell_cat_proc_net_if_inet6, action_list, isRooted));

        l.add(promptSymbol + getString(R.string.shell_cat_proc_net_ipv6_route));
        l.add(execute(R.string.shell_cat_proc_net_ipv6_route, action_list, isRooted));

        return l;
    }
}
