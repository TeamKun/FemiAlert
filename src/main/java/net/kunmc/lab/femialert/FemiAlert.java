package net.kunmc.lab.femialert;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class FemiAlert extends JavaPlugin implements Listener, CommandExecutor, TabCompleter {

    @Override
    public void onEnable() {
        // イベントの登録
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             String label,
                             String[] args) {
        /*
        ～～～～～～～～～～～～～～～～～～～～
        コマンド判定
        /f以外では動作しないように
        ～～～～～～～～～～～～～～～～～～～～
        */
        if (!label.equals("f") && !label.equals("femialert")) {
            return false;
        }

        /*
        ～～～～～～～～～～～～～～～～～～～～
        config.yml関連の初期動作
        ～～～～～～～～～～～～～～～～～～～～
        */
        // config.ymlが存在しない場合、ファイル出力
        saveDefaultConfig();
        // config.ymlを読み込む
        FileConfiguration config = getConfig();

        /*
        ～～～～～～～～～～～～～～～～～～～～
        コマンド処理
        ～～～～～～～～～～～～～～～～～～～～
        */
        // PATHの初期値を設定
        String path = "Player_name.";
        // プレイヤーの名前を取得
        String playername = sender.getName();

        // 引数なしのコマンドが投入された時の処理
        if (args.length < 1) {
            // configにプレイヤー名が登録されているか判定
            if (config.contains(path + playername)) {
                // Word用PATHの設定
                String wordpath = (path + playername + ".words");

                // 登録済みのワードを表示
                sender.sendMessage("■警告するワードを選択してください");
                List<String> wordlist = config.getStringList(wordpath);
                for (String keywords : wordlist) {
                    sendHoverText(sender, "【" + keywords + "】", keywords + "で警告を出します", "/f " + keywords);
                }

            } else {
                sender.sendMessage("あなたのデータは登録されていません");
            }
        } else {
            String[] nextArgs = Arrays.copyOfRange(args, 1, args.length);
            switch (args[0]) {
                case "addplayer":
                    ModConfig.addPlayer(sender, config, nextArgs);
                    saveConfig();
                    break;

                case "delplayer":
                    ModConfig.delPlayer(sender, config, nextArgs);
                    saveConfig();
                    break;

                case "addword":
                    ModConfig.addWord(sender, config, nextArgs);
                    saveConfig();
                    break;

                case "delword":
                    ModConfig.delWord(sender, config, nextArgs);
                    saveConfig();
                    break;

                case "settitle":
                    ModConfig.setTitle(sender, config, nextArgs);
                    saveConfig();
                    break;

                case "modsound":
                    ModConfig.modsound(sender, config, nextArgs);
                    saveConfig();
                    break;

                default:
                    // Sound用PATHの設定
                    String soundpath = (path + playername + ".sound");
                    // Title用PATHの設定
                    String titlepath = (path + playername + ".title");
                    // サウンド設定
                    Sound sound = Sound.valueOf(config.getString(soundpath));
                    // タイトル設定
                    String titledata = config.getString(titlepath);

                    // タイトルフォーマットのPATH設定
                    String fomatpath = "title_format";
                    // フォーマット設定
                    String fomat = config.getString(fomatpath);

                    // オンラインプレイヤー全員に対しtitle表示、音を鳴らす
                    for (Player target : Bukkit.getOnlinePlayers()) {
                        if (target != null) {
                            // タイトル表示
                            target.sendTitle(fomat + "§4" + titledata + fomat,
                                    "§7理由：" + "§1§l" + args[0], 10, 70, 20);
                            // 音を鳴らす
                            for (int cnt = 0; cnt < 3; ++cnt) {
                                target.playSound(target.getLocation(), sound, 1.0f, 8.0f);
                                try {
                                    Thread.sleep(1100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    break;
            }
        }
        return true;
    }

    public static void sendHoverText(CommandSender sender, String text, String hoverText, String command) {
        // ホバーテキストとイベントを作成
        HoverEvent hoverEvent = null;
        if (hoverText != null) {
            BaseComponent[] hover = new ComponentBuilder(hoverText).create();
            hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover);
        }

        // クリックイベントを作成
        ClickEvent clickEvent = null;
        if (command != null) {
            clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, command);
        }

        BaseComponent[] message = new ComponentBuilder(text).event(hoverEvent).event(clickEvent).create();
        sender.spigot().sendMessage(message);
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender,
                                      @NotNull Command command,
                                      @NotNull String alias,
                                      @NotNull String[] args) {

        if (args.length == 1) {
            return Stream.of("addplayer",
                    "delplayer",
                    "addword",
                    "delword",
                    "settitle",
                    "modsound").filter(parm -> parm.startsWith(args[0])).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
