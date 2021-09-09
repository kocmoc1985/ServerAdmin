/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

/**
 *
 * @author KOCMOC
 */
public class MyHttpLinks {

    private static String build_request_example_b(String company, String alarm, String alarmDescr, String recipe, String order, String batch, String os, String user, String ipLocal, String version, boolean hidden) {
        return String.format(
                "http://www.mixcont.com/index.php?link=_npmc_alarm&log=true&company=%s&alarm=%s&alarmdescr=%s&recipe=%s&order=%s&batch=%s&os=%s&user=%s&iplocal=%s&ver=%s&hidden=%s",
                trim(company),
                alarm,
                trim(alarmDescr),
                trim(recipe),
                trim(order),
                trim(batch),
                trim(os),
                trim(user),
                trim(ipLocal),
                trim(version),
                trim_boolean(hidden));
    }

    public static String logIp(int clientId, String version, String os) {
        return String.format("http://www.mixcont.com/index.php?link=_http_com&client=%s&ipw=true&ver=%s&os=%s", clientId, version, trim(os));
    }

    public static String getIp(int clientId) {
        return "http://www.mixcont.com/index.php?link=_http_com&client=" + clientId + "&param=ip";
    }

    public static String getValid(int clientId) {
        return "http://www.mixcont.com/index.php?link=_http_com&client=" + clientId + "&param=valid";
    }

    public static String trim(String parameter) {
        return parameter.replaceAll(" ", "_");
    }

    public static String createClient(String clientId, String info) {
        return String.format("http://www.mixcont.com/index.php?link=_http_com&create=true&client=%s&info=%s",
                clientId, info);
    }

    /**
     *
     * @param clientId
     * @param valid = 0 or 1
     * @return
     */
    public static String setValidInvalid(String clientId, String valid) {
        return String.format("http://www.mixcont.com/index.php?link=_http_com&valid=%s&client=%s", valid, clientId);
    }

    public static String logRdpCom(int clientId, String os, String host, String version) {
        return String.format("http://www.mixcont.com/index.php?link=_http_com&log=true&client=%s&os=%s&host=%s&ver=%s",
                clientId, trim(os), trim(host), trim(version));
    }

    public static String logExecutorClient(String company, String ipLocal, String user, String os, String version) {
        return String.format("http://www.mixcont.com/index.php?link=_http_ex&c=%s&ip=%s&u=%s&os=%s&v=%s",
                trim(company), trim(ipLocal), trim(user), trim(os), trim(version));
    }

    public static String logExecutorClientAppLaunched(String company, String ipLocal, String user, String os, String version, String application) {
        return String.format("http://www.mixcont.com/index.php?link=_http_ex&c=%s&ip=%s&u=%s&os=%s&v=%s&app=%s",
                trim(company), trim(ipLocal), trim(user), trim(os), trim(version), trim(application));
    }

    public static String logExecutorServerOffline(String company, String ipLocal, String user, String os, String version) {
        return String.format("http://www.mixcont.com/index.php?link=_http_ex&c=%s&ip=%s&u=%s&os=%s&v=%s&npms=off",
                trim(company), trim(ipLocal), trim(user), trim(os), trim(version));
    }

    public static String logNpmcAlarm(String company, String alarm, String alarmDescr, String recipe, String order, String batch, String os, String user, String ipLocal, String version, boolean hidden) {
        return String.format("http://www.mixcont.com/index.php?link=_npmc_alarm&log=true&company=%s&alarm=%s&alarmdescr=%s&recipe=%s&order=%s&batch=%s&os=%s&user=%s&iplocal=%s&ver=%s&hidden=%s",
                trim(company), alarm, trim(alarmDescr), trim(recipe), trim(order), trim(batch), trim(os), trim(user), trim(ipLocal), trim(version), hidden ? 1 : 0);
    }

    public static int trim_boolean(boolean bool) {
        return bool ? 1 : 0;
    }
}
