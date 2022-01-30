package net.kunmc.lab.femialert;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class ModConfig extends JavaPlugin {
    private static String defaultpath = "Player_name.";
    //Map Playermap = new HashMap();

    public static void addPlayer(CommandSender sender, FileConfiguration config, String[] args) {
        // 初期登録用のワードリスト作成
        List<String> defaultwordlist = new ArrayList<String>();

        // 初期設定用の各種PATH
        String playerpath = defaultpath + args[0];
        String soundpath = playerpath + ".sound";   // sound用
        String titlepath = playerpath + ".title";   // title用
        String wordspath = playerpath + ".words";   // words用

        // プレイヤーが登録できるか判定
        if (config.contains(playerpath)) {
            sender.sendMessage("プレイヤー：[" + args[0] + "]は登録済みです");
            return;
        }

        // ワードリストのデフォルト値を設定
        defaultwordlist.add("初期値");

        // 初期値の設定
        config.set(soundpath, "BLOCK_BELL_RESONATE");
        config.set(titlepath, "警告");
        config.set(wordspath, defaultwordlist);
        return;
    }

    public static void delPlayer(CommandSender sender, FileConfiguration config, String[] args) {
        // プレイヤー削除用PATH
        String playerpath = defaultpath + args[0];

        // プレイヤーが登録できるか判定
        if (!(config.contains(playerpath))) {
            sender.sendMessage("プレイヤー：[" + args[0] + "]は登録されていません");
            return;
        }

        // プレイヤーのデータを削除
        config.set(playerpath, null);
        return;
    }

    public static void addWord(CommandSender sender, FileConfiguration config, String[] args) {
        // コマンド投入者の名前を取得
        String username = sender.getName();

        // words設定用path
        String playerpath = defaultpath + username;
        String wordspath = playerpath + ".words";

        // プレイヤーの情報が登録できるか判定
        if (!(config.contains(playerpath))) {
            sender.sendMessage("プレイヤー：[" + username + "]は登録されていません");
            return;
        }

        // 登録済みのワードをリストで取得
        List<String> wordlist = config.getStringList(wordspath);

        // ワードがすでに登録済みか判定
        if (wordlist.contains(args[0])) {
            sender.sendMessage("ワード：【" + args[0] + "】はすでに登録されています");
            return;
        }

        //　リストにワードを追加
        wordlist.add(args[0]);
        // コンフィグに設定
        config.set(wordspath, wordlist);

        return;
    }

    public static void delWord(CommandSender sender, FileConfiguration config, String[] args) {
        // コマンド投入者の名前を取得
        String username = sender.getName();

        // words削除用path
        String playerpath = defaultpath + username;
        String wordspath = playerpath + ".words";

        // プレイヤーの情報が削除できるか判定
        if (!(config.contains(playerpath))) {
            sender.sendMessage("プレイヤー：[" + username + "]は登録されていません");
            return;
        }

        // 登録済みのワードをリストで取得
        List<String> wordlist = config.getStringList(wordspath);

        // ワードがすでに登録済みか判定
        if (!(wordlist.contains(args[0]))) {
            sender.sendMessage("ワード：【" + args[0] + "】は登録されていません");
            return;
        }

        //　リストのワードを削除
        wordlist.remove(args[0]);
        // コンフィグに設定
        config.set(wordspath, wordlist);

        return;
    }

    public static void setTitle(CommandSender sender, FileConfiguration config, String[] args) {
        // コマンド投入者の名前を取得
        String username = sender.getName();

        // title登録用path
        String playerpath = defaultpath + username;
        String titlespath = playerpath + ".title";

        // プレイヤーの情報が登録されているか判定
        if (!(config.contains(playerpath))) {
            sender.sendMessage("プレイヤー：[" + username + "]は登録されていません");
            return;
        }

        String test = config.getString(titlespath);
        // タイトルがすでに登録済みか判定
        if (config.contains(titlespath)) {
            if (config.getString(titlespath).equals(args[0])) {
                sender.sendMessage("タイトル：【" + args[0] + "】はすでに登録されています");
                return;
            }
        }

        // コンフィグに設定
        config.set(titlespath, args[0]);

        return;
    }

    public static void modsound(CommandSender sender, FileConfiguration config, String[] args) {
        // コマンド投入者の名前を取得
        String username = sender.getName();

        // sound変更用path
        String soundspath = defaultpath + username + ".sound";

        // プレイヤーの情報が登録されているか判定
        if (!(config.contains(soundspath))) {
            sender.sendMessage("プレイヤー：[" + username + "]は登録されていません");
            return;
        }

        // タイトルがすでに登録済みか判定
        if (config.contains(soundspath)) {
            if (config.getString(soundspath).equals(args[0])) {
                sender.sendMessage("サウンド：【" + args[0] + "】はすでに登録されています");
                return;
            }
        }

        // コンフィグに設定
        config.set(soundspath, args[0]);

        return;
    }

}
