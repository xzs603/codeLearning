/*
Jiongorz. Src. Command.java
JiongorzBBs : http://bbs.jiongorz.com
AdminQQ:307586193
Command
*/
package net.sf.odinms.client.messages;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import net.sf.odinms.client.MapleCharacter;
import net.sf.odinms.client.MapleClient;
import net.sf.odinms.database.DatabaseConnection;
import net.sf.odinms.net.channel.ChannelServer;
import net.sf.odinms.server.TimerManager;
import net.sf.odinms.server.life.MapleLifeFactory;
import net.sf.odinms.server.life.MapleMonster;
import net.sf.odinms.server.life.MapleNPC;
import net.sf.odinms.tools.MaplePacketCreator;
import net.sf.odinms.tools.Pair;
import net.sf.odinms.tools.StringUtil;
public class AdminCommand {

    private static class ShutdownAnnouncer implements Runnable {

        private ChannelServer cserv;
        private long startTime,  time;

        public ShutdownAnnouncer(ChannelServer cs, long t) {
            cserv = cs;
            time = t;
            startTime = System.currentTimeMillis();
        }

        public void run() {
            cserv.broadcastPacket(MaplePacketCreator.serverNotice(0, "这世界会变的 " + ((time - System.currentTimeMillis() + startTime) / 60000) + " minutes, please log off safely."));
        }
    }

    public static boolean executeAdminCommand(MapleClient c, MessageCallback mc, String line, org.slf4j.Logger log, List<Pair<MapleCharacter, String>> gmlog, Runnable persister) {
        ChannelServer cserv = c.getChannelServer();
        MapleCharacter player = c.getPlayer();
        String[] splitted = line.split(" ");
        if (splitted[0].equals("!setGMlevel")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            victim.setGMLevel(Integer.parseInt(splitted[2]));
            mc.dropMessage("成功.");
            } else if (splitted[0].equals("!vip1")) {
	MapleCharacter target = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
	target.setVip();
	mc.dropMessage(target + "被提升为VIP1 " ) ;
} else if (splitted[0].equals("!vip2")) {
	MapleCharacter target = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
	target.setVip2();
	mc.dropMessage(target + "被提升为VIP2 " ) ;
} else if (splitted[0].equals("!vip3")) {
	MapleCharacter target = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
	target.setVip3();
	mc.dropMessage(target + "被提升为VIP3") ;
    } else if (splitted[0].equals("!vip4")) {
	MapleCharacter target = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
	target.setVip4();
	mc.dropMessage(target + "被提升为VIP4") ;
    } else if (splitted[0].equals("!vip5")) {
	MapleCharacter target = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
	target.setVip5();
	mc.dropMessage(target + "被提升为VIP5") ;
} else if (splitted[0].equals("!unvip")) {
	MapleCharacter target = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
	target.unVip();
	mc.dropMessage( target+"被取消VIP资格" ) ;
         } else if (splitted[0].equals("!duanxian")) {
            for (MapleCharacter everyone : cserv.getPlayerStorage().getAllCharacters()) {
                if (everyone != c.getPlayer()) {
                    everyone.getClient().getSession().close();
                }
                everyone.saveToDB(true);
                cserv.removePlayer(everyone);
            }
        } else if (splitted[0].equals("!shutdown") || splitted[0].equals("!shutdownnow")) {
            for (MapleCharacter everyone : cserv.getPlayerStorage().getAllCharacters()) {
                everyone.saveToDB(true);
            }
            int time = 60000;
            if (splitted.length > 1) {
                time = Integer.parseInt(splitted[1]) * 60000;
                TimerManager.getInstance().register(new ShutdownAnnouncer(cserv, (long) time), 5 * 60000, 5 * 60000);
            }
            if (splitted[0].equals("!shutdownnow")) {
                time = 1;
            }
            persister.run();
            cserv.shutdown(time);
        } else if (splitted[0].equals("!sql")) {
            try {
                String sql = StringUtil.joinStringFrom(splitted, 1);
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ps.executeUpdate();
                ps.close();
                mc.dropMessage("进行 " + sql);
            } catch (SQLException e) {
                mc.dropMessage("一些错误的发生.");
            }
             } else if (splitted[0].equals("!shuoming")) {
            mc.dropMessage("5 = admin级超强管理员");
            mc.dropMessage("4 = 可以召唤BOSS的管理员");
            mc.dropMessage("3 = 普通GM");
            mc.dropMessage("2 = 实习/巡逻GM");
            mc.dropMessage("1 = 给RMB弄のGM");
            mc.dropMessage("0 = 冒险玩家");
             } else if (splitted[0].equals("!gm5")) {
            mc.dropMessage("您是admin级超强管理员你的等级<GMlevel>:5，可以使用全部GM命令以及以下命令.");
            mc.dropMessage("!pnpc  [npcid] - 创建一个NPC永久在那个点.");
            mc.dropMessage("!setGMLevel [人物] [等级] - 设置指定人物提升到指定GM等级.");
            mc.dropMessage("!shuoming   -查看GM等级说明.");
            mc.dropMessage("!shutdown    [时间] - 在指定时间后关闭服务器.");
            mc.dropMessage("!shutdownnow - 马上关闭服务器.");
            mc.dropMessage("!duanxian       - 断开服务器全部玩家连接.");
            mc.dropMessage("!vip1 [人物名字] - 提升人物VIP等级为1.");
            mc.dropMessage("!vip2 [人物名字] - 提升人物VIP等级为2.");
            mc.dropMessage("!vip3 [人物名字] - 提升人物VIP等级为3.");
            mc.dropMessage("!vip4 [人物名字] - 提升人物VIP等级为4.");
           mc.dropMessage("!vip5 [人物名字] - 提升人物VIP等级为5.");
            mc.dropMessage("!unvip[人物名字]] - 取消人物的VIP资格.");
        } else if (splitted[0].equals("!pnpc")) {
            int npcId = Integer.parseInt(splitted[1]);
            MapleNPC npc = MapleLifeFactory.getNPC(npcId);
            if (npc != null && !npc.getName().equals("MISSINGNO")) {
                npc.setPosition(player.getPosition());
                npc.setCy(player.getPosition().y);
                npc.setRx0(player.getPosition().x + 50);
                npc.setRx1(player.getPosition().x - 50);
                npc.setFh(player.getMap().getFootholds().findBelow(player.getPosition()).getId());
                npc.setCustom(false);
                try {
                    Connection con = DatabaseConnection.getConnection();
                    PreparedStatement ps = con.prepareStatement("INSERT INTO spawns ( idd, f, fh, cy, rx0, rx1, type, x, y, mid ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");
                    ps.setInt(1, npcId);
                    ps.setInt(2, 0);
                    ps.setInt(3, player.getMap().getFootholds().findBelow(player.getPosition()).getId());
                    ps.setInt(4, player.getPosition().y);
                    ps.setInt(5, player.getPosition().x + 50);
                    ps.setInt(6, player.getPosition().x - 50);
                    ps.setString(7, "n");
                    ps.setInt(8, player.getPosition().x);
                    ps.setInt(9, player.getPosition().y);
                    ps.setInt(10, player.getMapId());
                    ps.executeUpdate();
                } catch (SQLException e) {
                    mc.dropMessage("存入NPC数据库失败.");
                    return false;
                }
                player.getMap().addMapObject(npc);
                player.getMap().broadcastMessage(MaplePacketCreator.spawnNPC(npc));
            } else {
                mc.dropMessage("你必须输入一个有效的Npc-Id.");
            }
        } else if (splitted[0].equals("!pmob")) {
            int npcId = Integer.parseInt(splitted[1]);
            int mobTime = Integer.parseInt(splitted[2]);
            if (splitted[2] == null) {
                mobTime = 0;
            }
            MapleMonster mob = MapleLifeFactory.getMonster(npcId);
            if (mob != null && !mob.getName().equals("MISSINGNO")) {
                mob.setPosition(player.getPosition());
                mob.setCy(player.getPosition().y);
                mob.setRx0(player.getPosition().x + 50);
                mob.setRx1(player.getPosition().x - 50);
                mob.setFh(player.getMap().getFootholds().findBelow(player.getPosition()).getId());
                try {
                    Connection con = DatabaseConnection.getConnection();
                    PreparedStatement ps = con.prepareStatement("INSERT INTO spawns ( idd, f, fh, cy, rx0, rx1, type, x, y, mid, mobtime ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");
                    ps.setInt(1, npcId);
                    ps.setInt(2, 0);
                    ps.setInt(3, player.getMap().getFootholds().findBelow(player.getPosition()).getId());
                    ps.setInt(4, player.getPosition().y);
                    ps.setInt(5, player.getPosition().x + 50);
                    ps.setInt(6, player.getPosition().x - 50);
                    ps.setString(7, "m");
                    ps.setInt(8, player.getPosition().x);
                    ps.setInt(9, player.getPosition().y);
                    ps.setInt(10, player.getMapId());
                    ps.setInt(11, mobTime);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    mc.dropMessage("存入MOB数据失败。");
                    return false;
                }
                player.getMap().addMonsterSpawn(mob, mobTime);
            } else {
                mc.dropMessage("你必须输入一个有效的 MOB-Id");
            }
        } else if (splitted[0].equals("!addcommand")) {
            if (!splitted[1].equals("spawn") || !splitted[1].equals("npc") || !splitted[1].equals("shop")) {
                return false;
            }
            String type = splitted[1];
            int monsterId = Integer.parseInt(splitted[2]);
            int quantity = Integer.parseInt(splitted[3]); 
            String name = splitted[4];
            int gmlvl = Integer.parseInt(splitted[5]);
            try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("INSERT INTO newcommands (type, monsterId, quantity, name, gmlvl) VALUES (?, ?, ?, ?, ?)");
                ps.setString(1, type);
                ps.setInt(2, monsterId);
                ps.setInt(3, quantity);
                ps.setString(4, name);
                ps.setInt(5, gmlvl);
                ps.executeUpdate();
                mc.dropMessage("成功加入 " + type + " 命令.");
            } catch (SQLException e) {
                mc.dropMessage("一些错误的发生.");
                return false;
            }
        } else {
            mc.dropMessage("您输入的admin命令" + splitted[0] + "不存在!");
            return false;
        }
        return true;
    }
}