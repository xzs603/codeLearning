/*
Jiongorz. Src. Command.java
JiongorzBBs : http://bbs.jiongorz.com
AdminQQ:307586193
Command
*/
package net.sf.odinms.client.messages;

import java.lang.management.ManagementFactory;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import javax.management.*;
import net.sf.odinms.client.*;
import net.sf.odinms.database.DatabaseConnection;
import net.sf.odinms.net.channel.ChannelServer;
import net.sf.odinms.net.channel.handler.GeneralchatHandler;
import net.sf.odinms.scripting.npc.NPCScriptManager;
import net.sf.odinms.server.MapleShopFactory;
import net.sf.odinms.server.TimerManager;
import net.sf.odinms.server.life.MapleLifeFactory;
import org.slf4j.*;
import net.sf.odinms.server.maps.MapleMap;
import net.sf.odinms.tools.*;

public class CommandProcessor implements CommandProcessorMBean {

    private static CommandProcessor instance = new CommandProcessor();
    private static final Logger log = LoggerFactory.getLogger(GeneralchatHandler.class);
    private static List<Pair<MapleCharacter, String>> gmlog = new LinkedList<Pair<MapleCharacter, String>>();
    private static Runnable persister;
    private static String type;
    private static int monsterId,  quantity,  gmlvl;


    static {
        persister = new PersistingTask();
        TimerManager.getInstance().register(persister, 62000);
    }

    public static CommandProcessor getInstance() {
        return instance;
    }

    private CommandProcessor() {
    }

    public static class PersistingTask implements Runnable {

        @Override
        public void run() {
            synchronized (gmlog) {
                Connection con = DatabaseConnection.getConnection();
                try {
                    PreparedStatement ps = con.prepareStatement("INSERT INTO gmlog (cid, command) VALUES (?, ?)");
                    for (Pair<MapleCharacter, String> logentry : gmlog) {
                        ps.setInt(1, logentry.getLeft().getId());
                        ps.setString(2, logentry.getRight());
                        ps.executeUpdate();
                    }
                    ps.close();
                } catch (SQLException e) {
                    log.error("错误咯", e);
                }
                gmlog.clear();
            }
        }
    }

    public static void registerMBean() {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        try {
            mBeanServer.registerMBean(instance, new ObjectName("net.sf.odinms.client.messages:name=CommandProcessor"));
        } catch (Exception e) {
            log.error("错误");
        }
    }

    public static boolean processCommand(MapleClient c, String line) {
        return processCommandInternal(c, new ServernoticeMapleClientMessageCallback(c), line);
    }

    public String processCommandJMX(int cserver, int mapid, String command) {
        ChannelServer cserv = ChannelServer.getInstance(cserver);
        if (cserv == null) {
            return "好囧啊 一大堆乱七八糟的";
        }
        MapleClient c = new MapleClient(null, null, new MockIOSession());
        MapleCharacter chr = MapleCharacter.getDefault(c, 26023);
        c.setPlayer(chr);
        chr.setName("/---------分割线-------------\\");
        MapleMap map = cserv.getMapFactory().getMap(mapid);
        if (map != null) {
            chr.setMap(map);
            map.addPlayer(chr);
        }
        cserv.addPlayer(chr);
        MessageCallback mc = new StringMessageCallback();
        try {
            processCommandInternal(c, mc, command);
        } finally {
            if (map != null) {
                map.removePlayer(chr);
            }
            cserv.removePlayer(chr);
        }
        return mc.toString();
    }

    private static boolean processCommandInternal(MapleClient c, MessageCallback mc, String line) {
        MapleCharacter p = c.getPlayer();
        int vip = p.vip;
        int gm = p.gmLevel();
        if (line.charAt(0) == '!' && gm > 0) {
            if (isExist(line)) {
                if (makeCustomSummon(c, type, monsterId, quantity, gmlvl)) {
                    return true;
                }
            }
            if (gm > 0) {
                try {
                    if (DonatorCommand.executeDonatorCommand(c, mc, line)) {
                        return true;
                    }
                } catch (RemoteException ex) {
                    java.util.logging.Logger.getLogger(CommandProcessor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (gm > 1) {
                if (InternCommand.executeInternCommand(c, mc, line)) {
                    return true;
                }
            }
            if (gm > 2) {
                if (GMCommand.executeGMCommand(c, mc, line)) {
                    return true;
                }
            }
            if (gm > 3) {
                if (SuperCommand.executeSuperCommand(c, mc, line)) {
                    return true;
                }
            }
            if (gm > 4) {
                if (AdminCommand.executeAdminCommand(c, mc, line, log, gmlog, persister)) {
                    return true;
                }
            }
            return false;
        } else if (line.charAt(0) == '@') {
            if (gm > -1) {
                if (PlayerCommand.executePlayerCommand(c, mc, line)) {
                    return true;
                }
            }
            } else if (line.charAt(0) == '$') {
            if (vip>1) {
                if (VipCommand.executeVipCommand(c, mc, line)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private static boolean isExist(String name) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM newcommands WHERE name=\"" + name + "\"");
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return false;
            }
            type = rs.getString("type");
            monsterId = rs.getInt("monsterid");
            quantity = rs.getInt("quantity");
            gmlvl = rs.getInt("gmlvl");
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

    public static boolean makeCustomSummon(MapleClient c, String type, int monsterid, int quantity, int gmlvl) {
        if (c.getPlayer().gmLevel() >= gmlvl) {
            if (type.equals("spawn")) {
                for (int i = 0; i < quantity; i++) {
                    c.getPlayer().getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(monsterid), c.getPlayer().getPosition());
                }
            } else if (type.equals("npc")) {
                NPCScriptManager.getInstance().start(c, monsterid, null, null);
            } else if (type.equals("shop")) {
                MapleShopFactory.getInstance().getShop(monsterid).sendShop(c);
            } else {
                return false;
            }
        } else {
            c.getPlayer().dropMessage("梦の岛提示你：你的GM指令不够高，或者自定义命令失败.");
        }
        return true;
    }
}
