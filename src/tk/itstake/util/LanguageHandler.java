package tk.itstake.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bexco on 2015-07-26.
 */
public class LanguageHandler {

    JSONObject language = null;
    Plugin plugin = null;
    public LanguageHandler() {
        // Get Language Folder
        plugin = Bukkit.getPluginManager().getPlugin("SteakGUI");
        if(plugin != null && plugin.getDataFolder() != null) {
            languageLoad();
        } else {
            language = getDefaultLanguage();
        }
    }

    public void languageLoad() {
        plugin = Bukkit.getPluginManager().getPlugin("SteakGUI");
        File langfolder = new File(plugin.getDataFolder().toString() + File.separator + "lang");
        if(!langfolder.exists()) {
            langfolder.mkdir();
        }
        // Get Current Selected Language
        final String sellang;
        if (ConfigHandler.hasConfig("language")) {
            sellang = ConfigHandler.getConfig("language").toString();
        } else {
            sellang = "ko_KR";
        }
        // Load Current Selected Language
        if (langfolder.exists() && langfolder.isDirectory()) {
            File[] langlist = langfolder.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    if (name.equals(sellang + ".json")) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            // If Detected Language
            if (langlist.length > 0) {
                // Load Language
                try {
                    FileReader fr = new FileReader(langlist[0]);
                    JSONParser jp = new JSONParser();
                    language = (JSONObject) jp.parse(fr);
                    boolean notupdated = false;
                    for(Object key:language.keySet()) {
                        if(!getDefaultLanguage().containsKey(key)) {
                            notupdated = true;
                        }
                    }
                    if(notupdated) {
                        updateFile();
                    }
                    fr.close();
                } catch (Exception e) {
                    language = getDefaultLanguage();
                    makeDefaultFile();
                }
            } else {
                // If Can't Detect Language File
                language = getDefaultLanguage();
                makeDefaultFile();
            }
        }
    }

    private void updateFile() {
        // If Can't Load Language File
        plugin = Bukkit.getPluginManager().getPlugin("SteakGUI");
        File langfolder = new File(plugin.getDataFolder().toString() + File.separator + "lang");
        File langfile = new File(langfolder.toString() + File.separator + "ko_KR.json");
        // Write New File
        try {
            FileWriter fw = new FileWriter(langfile);
            JSONObject defaultlang = getDefaultLanguage();
            for(Object key:defaultlang.keySet()) {
                if(language.containsKey(key)) {
                    defaultlang.put(key, language.get(key));
                }
            }
            fw.write(JSONUtil.getPretty(defaultlang.toJSONString()));
            language = defaultlang;
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void makeDefaultFile() {
        // If Can't Load Language File
        plugin = Bukkit.getPluginManager().getPlugin("SteakGUI");
        File langfolder = new File(plugin.getDataFolder().toString() + File.separator + "lang");
        File langfile = new File(langfolder.toString() + File.separator + "ko_KR.json");
        // Write New File
        try {
            FileWriter fw = new FileWriter(langfile);
            language = getDefaultLanguage();
            fw.write(JSONUtil.getPretty(language.toJSONString()));
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JSONObject getDefaultLanguage() {
        JSONObject defaultlang = new JSONObject();
        defaultlang.put("editorhelp", "언어 파일을 수정하시려고 하시나요? 도움말은 http://wiki.itstake.tk/index.php?title=SteakGUI/언어 에서 찾아보세요!");
        defaultlang.put("console.onenable", "&a소고기구이(SteakGUI) v%0% 이 활성화 되었습니다!");
        defaultlang.put("console.useblacklist", "&a소고기구이(SteakGUI) 는 Efe 님의 플러그인, horyu1234 님의 플러그인과 연동되는 블랙리스트를 사용합니다!");
        defaultlang.put("console.ondisable", "&a소고기구이(SteakGUI) v%0% 이 비활성화 되었습니다. 사용해 주셔서 감사합니다.");
        defaultlang.put("console.detectblacklist", "&c블랙리스트 사용자가 감지되었습니다. 차단을 실행합니다.");
        defaultlang.put("command.wronguse", "&c사용법:%0%");
        defaultlang.put("command.help.intro", "&b소고기구이(SteakGUI) %0% 도움말 -----");
        defaultlang.put("command.help.open", "&c/%0% open <메뉴 이름> - 메뉴를 엽니다.");
        defaultlang.put("command.wronghelp.arg.player", "플레이어");
        defaultlang.put("command.wronghelp.arg.menu", "메뉴 이름");
        defaultlang.put("command.help.open", "&c/%0% open <메뉴 이름> - 메뉴를 엽니다.");
        defaultlang.put("command.help.open.others", "&c/%0% open <메뉴 이름> [플레이어] - 메뉴를 엽니다. 플레이어 이름을 입력한 경우 그 플레이어에게 메뉴를 엽니다.");
        defaultlang.put("command.help.open.console", "&c/%0% open <메뉴 이름> <플레이어> - 메뉴를 엽니다. 플레이어 이름을 입력한 경우 그 플레이어에게 메뉴를 엽니다.");
        defaultlang.put("command.help.setting", "&c/%0% setting <메뉴 이름> - 메뉴를 수정합니다.");
        defaultlang.put("command.help.create", "&c/%0% create <메뉴 이름> - 메뉴를 만듭니다.");
        defaultlang.put("command.help.delete", "&c/%0% delete <메뉴 이름> - 메뉴를 삭제합니다.");
        defaultlang.put("command.help.list", "&c/%0% list - 메뉴의 목록을 봅니다.");
        defaultlang.put("command.help.reload", "&c/%0% reload - 메뉴 혹은 설정 파일을 리로드합니다.");
        defaultlang.put("command.list.intro", "&b메뉴 목록---------");
        defaultlang.put("command.list.format", "&c%0%");
        defaultlang.put("command.list.nomenu", "&c메뉴가 없습니다!");
        defaultlang.put("command.reloaded", "&c소고기구이가 리로드 되었습니다.");
        defaultlang.put("nopermission", "&c권한이 없습니다!");
        defaultlang.put("noconsole", "&c콘솔에서는 사용할 수 없는 명령어입니다!");
        defaultlang.put("menudeleted", "&cMenu %0% Deleted");
        defaultlang.put("menucreated", "&cMenu %0% Created");
        defaultlang.put("notexistmenu", "&cNot Exist Menu!");
        defaultlang.put("offlineplayer", "&cOffline Player!");
        defaultlang.put("command.help.author", "&6소고기구이(SteakGUI) v%0%, 제작자: ITSTAKE(http://itstake.tk), 번역자: ITSTAKE(http://itstake.tk)");
        defaultlang.put("menu.nodisplayname", "&c아이템 이름 없음, 설정해 주세요.");
        defaultlang.put("menu.nolore", "&c아이템 설명이 없습니다.\n설정해 주세요.");
        defaultlang.put("menu.noitemtask", "&c아이템 작업이 없습니다. \n&b/sg setting <메뉴 이름> &c을 이용해 수정하세요.");
        defaultlang.put("menufile.wannahelp", "파일을 직접 수정하고 싶으신가요?\n그렇다면 http://wiki.itstake.tk/index.php?title=SteakGUI/직접_설정 문서를 참고해 보세요.");
        defaultlang.put("existpermission", "&c이미 존재하는 펄미션입니다!");
        defaultlang.put("notmatchedformat", "&c매뉴 이름엔 한글을 사용할 수 없습니다! 일단 영어로 매뉴를 만드신 후, /sg setting 을 이용하여 매뉴 제목을 수정하세요!");
        // TO-DO: Language Insert
        return defaultlang;
    }

    public String getLanguage(String path, ArrayList<String> args) {
        return getLanguage(path, (String[])args.toArray());
    }

    public String getLanguage(String path) {
        return getLanguage(path, new String[]{});
    }

    public String getLanguage(String path, String[] args) {
        if(language != null) {
            if(language.containsKey(path)) {
                String lango = (String) language.get(path);
                Matcher mat = Pattern.compile("%*[0-9]%").matcher(lango);
                String lang = lango;
                while (mat.find()) {
                    String sen = mat.group();
                    if(sen.startsWith("%") && sen.endsWith("%") && isNum(sen.replace("%", "")) && args.length > Integer.parseInt(sen.replace("%", ""))) {
                        lang = lang.replace(mat.group(), args[Integer.parseInt(sen.replace("%", ""))]);
                    }
                }
                return lang;
            } else {
                return "Language file error! Please call admin(언어 파일 오류입니다. 관리자에게 연락해 주세요!)";
            }
        } else {
            return "Language file error! Please call admin(언어 파일 오류입니다. 관리자에게 연락해 주세요!)";
        }
    }

    public boolean isNum(String num) {
        try {
            Integer.parseInt(num);
            return true;
        } catch(Exception e) {
            return false;
        }
    }
}
