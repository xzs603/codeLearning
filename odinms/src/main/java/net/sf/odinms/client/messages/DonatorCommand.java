/*
Jiongorz. Src. Command.java
JiongorzBBs : http://bbs.jiongorz.com
AdminQQ:307586193
Command
*/
package net.sf.odinms.client.messages;
import com.mysql.jdbc.PreparedStatement;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.util.Collection;
import java.util.Map;
import net.sf.odinms.client.MapleCharacter;
import net.sf.odinms.client.MapleClient;
import net.sf.odinms.client.MapleStat;
import net.sf.odinms.client.SkillFactory;
import net.sf.odinms.database.DatabaseConnection;
import net.sf.odinms.net.channel.ChannelServer;
import net.sf.odinms.server.life.MapleLifeFactory;
import net.sf.odinms.server.life.MapleMonster;
import net.sf.odinms.tools.MaplePacketCreator;
import net.sf.odinms.tools.StringUtil;

public class DonatorCommand {
    public static int getOptionalIntArg(String splitted[], int position, int def) {
        if (splitted.length > position) {
            try {
                return Integer.parseInt(splitted[position]);
            } catch (NumberFormatException nfe) {
                return def;
            }
        }
        return def;

}

    public static boolean executeDonatorCommand(MapleClient c, MessageCallback mc, String line) throws RemoteException {
        MapleCharacter player = c.getPlayer();
        ChannelServer cserv = c.getChannelServer();
        String[] splitted = line.split(" ");
        if (splitted[0].equals("!!!!!")) {

	} else if (splitted[0].equals("!zaixian")) {
            if (c.getPlayer().getMeso() < 0) {
                        mc.dropMessage("梦の岛提示你： 每次执行该命令系统将自动扣除您的查看费用0游戏币！");
                    } else {
			mc.dropMessage("梦の岛提示你： 频道服务器 " + c.getChannel() + " 当前状态:");
			Collection<MapleCharacter> chrs = c.getChannelServer().getInstance(c.getChannel()).getPlayerStorage().getAllCharacters();
			for (MapleCharacter chr : chrs) {
				mc.dropMessage("梦の岛提示你：角色ID：" + chr.getId() + " 角色名：" + chr.getName() + " :所在地图ID: " + chr.getMapId());
			}
			mc.dropMessage("梦の岛提示你：频道服务器 " + c.getChannel() + "  当前总计： " + chrs.size() + "人在线.");
            }
             } else if (splitted[0].equals("!help")) {
            mc.dropMessage("您是游戏GM，可以使用以下命令.");
            mc.dropMessage("!gm5 <admin> - 查看最高管理的命令.");
            mc.dropMessage("!gm4 <可召唤BOSS管理GM> - 查看超级管理的命令.");
            mc.dropMessage("!gm3 <管理GM> - 查看游戏管理的命令.");
            mc.dropMessage("!gm2 <巡逻GM> - 查看实习管理的命令.");
            mc.dropMessage("!gm1 <捐赠GM> - 查看捐赠者管理的命令.");
             } else if (splitted[0].equals("!gm1")) {
            mc.dropMessage("您是贡献のGM你的等级<GMlevel>:1，可以使用以下命令.");
            mc.dropMessage("!gxfz - 添加辅助.");
            mc.dropMessage("!zaixian - 查看在线玩家.");
            mc.dropMessage("!dianjuan [玩家名字] [数字]        - 给指定玩家指定数量的点卷.");
            mc.dropMessage("!mxb [数字]                - 贡献GM 冒险币不用愁");
            mc.dropMessage("!wd[on/off]  开启GM无敌/关闭");
            mc.dropMessage("!zhaohuan [id] [number]  - 召唤怪物");
            mc.dropMessage("!pindao  - 频道信息");
            mc.dropMessage("!mapid - 你在地图的ID");
            mc.dropMessage("!say - GM说话");
             mc.dropMessage("!notice - GM说话");
            mc.dropMessage("其他功能待研发.");
         

                    } else if (splitted[0].equals("!zhaohuan")) {
            for (int i = 0; i < Math.min(getOptionalIntArg(splitted, 2, 1), 500); i++) {
                MapleMonster mob = MapleLifeFactory.getMonster(Integer.parseInt(splitted[1]));
                player.getMap().spawnMonsterOnGroudBelow(mob, player.getPosition());
            }
                    } else if (splitted[0].equals("!notice") || (splitted[0].equals("!say"))) {
            String type;
            if (splitted[0].equals("!notice")) {
                type = "[消息] ";
            } else {
                type = "[" + player.getName() + "] ";
            }

            try {
                ChannelServer.getInstance(c.getChannel()).getWorldInterface().broadcastMessage(player.getName(), MaplePacketCreator.serverNotice(6, type + StringUtil.joinStringFrom(splitted, 1)).getBytes());
            } catch (RemoteException e) {
                cserv.reconnectWorld();
            }

                    } else if (splitted[0].equals("!mapid")) {
            mc.dropMessage("梦の岛提示你：你在地图 " + player.getMap().getId());
               }else if (splitted[0].equals("!wdon")) {
			player.setStr(32767);
			player.setDex(32767);
			player.setInt(32767);
			player.setLuk(32767);
			player.setLevel(255);
			player.setFame(10000);
			player.setMaxHp(30000);
			player.setMaxMp(30000);
			player.updateSingleStat(MapleStat.STR, 32767);
			player.updateSingleStat(MapleStat.DEX, 32767);
			player.updateSingleStat(MapleStat.INT, 32767);
			player.updateSingleStat(MapleStat.LUK, 32767);
			player.updateSingleStat(MapleStat.LEVEL, 255);
			player.updateSingleStat(MapleStat.FAME, 13337);
			player.updateSingleStat(MapleStat.MAXHP, 30000);
			player.updateSingleStat(MapleStat.MAXMP, 30000);
   } else if (splitted[0].equals("!wdoff")) {
			player.setStr(4);
			player.setDex(4);
			player.setInt(4);
			player.setLuk(4);
			player.setLevel(1);
			player.setFame(0);
			player.setMaxHp(500);
			player.setMaxMp(300);
			player.updateSingleStat(MapleStat.STR, 4);
			player.updateSingleStat(MapleStat.DEX, 4);
			player.updateSingleStat(MapleStat.INT, 4);
			player.updateSingleStat(MapleStat.LUK, 4);
			player.updateSingleStat(MapleStat.LEVEL, 1);
			player.updateSingleStat(MapleStat.FAME, 0);
			player.updateSingleStat(MapleStat.MAXHP, 500);
			player.updateSingleStat(MapleStat.MAXMP, 300);
                
              } else if (splitted[0].equals("!pindao")) {
            try {
                Map<Integer, Integer> connected = cserv.getWorldInterface().getConnected();
                StringBuilder conStr = new StringBuilder("连接的人: ");
                boolean first = true;
                for (int i : connected.keySet()) {
                    if (!first) {
                        conStr.append(", ");
                    } else {
                        first = false;
                    }
                    if (i == 0) {
                        conStr.append("梦の岛提示你：全部在线: "+ connected.get(i));
                        conStr.append(connected.get(i));
                    } else {
                        conStr.append("频道" + i + ": " + connected.get(i));
                        conStr.append(i);
                        conStr.append(": ");
                        conStr.append(connected.get(i));
                    }
                }
                new ServernoticeMapleClientMessageCallback(c).dropMessage(conStr.toString());
            } catch (RemoteException e) {
                cserv.reconnectWorld();
            }
        } else if (splitted[0].equals("!gxfz")) {
            int[] array = {9001000, 9101002, 9101003, 9101008, 2001002, 1101007, 1005, 2301003, 5121009, 4111001, 4111002, 4211003, 4211005, 1321000, 2321004, 3121002};
            for (int i = 0; i < array.length; i++) {
                SkillFactory.getSkill(array[i]).getEffect(SkillFactory.getSkill(array[i]).getMaxLevel()).applyTo(player);
            }
    } else if (splitted[0].equals("!dianjuan")) {
            for (int i = 1; i < 4; i++) {
                cserv.getPlayerStorage().getCharacterByName(splitted[1]).modifyCSPoints(i, Integer.parseInt(splitted[2]));
            }
            mc.dropMessage("成功啦~~");
               } else if (splitted[0].equals("!mxb")) {
            player.gainMeso(Integer.parseInt(splitted[1]), true);
        } else {
            if (c.getPlayer().gmLevel() == 1) {
                mc.dropMessage("梦の岛提示你：您输入的捐赠GM命令 " + splitted[0] + " 不存在");
            }
            return false;
        }
        return true;
    }
}
