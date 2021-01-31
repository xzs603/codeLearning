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
import java.util.Collection;
import java.util.Iterator;
import net.sf.odinms.client.*;
import net.sf.odinms.database.DatabaseConnection;
import net.sf.odinms.net.channel.ChannelServer;
import net.sf.odinms.net.login.LoginServer;
import net.sf.odinms.net.world.guild.MapleGuildSummary;
import net.sf.odinms.scripting.npc.NPCScriptManager;
import net.sf.odinms.server.MaplePortal;
import net.sf.odinms.server.maps.MapleMap;
import net.sf.odinms.server.maps.SavedLocationType;
import net.sf.odinms.tools.*;

public class PlayerCommand {

    public static boolean executePlayerCommand(MapleClient c, MessageCallback mc, String line) {
        MapleCharacter player = c.getPlayer();
        String[] splitted = line.split(" ");
        ChannelServer cserv = c.getChannelServer();
        if (splitted[0].equals("@version")) {
        mc.dropMessage("梦の岛Leehom版 版权勿去");
           } else if (splitted[0].equals("@emo")) {
                player.setHp(0);
                player.updateSingleStat(MapleStat.HP, 0);		
            } else if (splitted[0].equals("@help")) {
                    mc.dropMessage("____________________________");
                    mc.dropMessage(" 服务器玩家命令");
                    mc.dropMessage("____________________________");
                    mc.dropMessage("@save        -保存信息");
                    mc.dropMessage("$viphelp    -VIP指令);");
                    mc.dropMessage("@emo       -自杀");
                    mc.dropMessage("@zaixian        -查看在线情况要钱哦");
                    mc.dropMessage("@superhelp  -超级玩家指令);");
                    mc.dropMessage("@quanxian         -我的权限");
                    mc.dropMessage("@expfix       -经验清0");
                    mc.dropMessage("@zijiu [账号]      -卡号自救了哦");
                    mc.dropMessage("@jiasi         -NPC假死修复");
                    mc.dropMessage("@ll/mj/zl/yq      -加对应属性");
                    mc.dropMessage("@version       -版本查看");
                    mc.dropMessage("@rebirth       -转生咯--被封拉");
                    mc.dropMessage("@ring [戒指ID] [ID]- 送戒指,要5000W");
                    mc.dropMessage("@ziyou       -回自由市场");
                    mc.dropMessage("@wnnpc       -万能npc");
                    mc.dropMessage("@gm       -叫GM npc");
                    mc.dropMessage("@cknpc       -随时随地玩仓库");
                    mc.dropMessage("戒指代码清单如下：");
                    mc.dropMessage("1112001 - 恋人戒指");
                    mc.dropMessage("1112002 - 纯爱恋人戒指");
                    mc.dropMessage("1112003 - 丘比特戒指");
                    mc.dropMessage("1112005 - 多个大红心围绕");
                    mc.dropMessage("1112006 - 头上两把剑");
                    mc.dropMessage("1112800 - 四叶挚友戒指");
                    mc.dropMessage("1112801 - 雏菊挚友戒指");
                    mc.dropMessage("1112802 - 闪星挚友戒指");
                    } else if (splitted[0].equals("@cknpc")) {
		if(player.getVip()>=0){
		NPCScriptManager npc = NPCScriptManager.getInstance();
		npc.start(c, 1022005);
		}else{
                   mc.dropMessage("梦の岛提示你：您不是玩家，不能使用此命令.");
                }
        } else if (splitted[0].equals("@bot")) {
            mc.dropMessage("梦の岛Leehom版 版权勿去");
        } else if (splitted[0].equals("@save")) {
                player.saveToDB(true);
                mc.dropMessage("梦の岛提示你：保存成功，请随时记得保存.");
        } else if (splitted[0].equals("@ll") || splitted[0].equals("@zl") || splitted[0].equals("@yq") || splitted[0].equals("@mj")) {
            int amount = Integer.parseInt(splitted[1]);
            boolean str = splitted[0].equals("@ll");
            boolean Int = splitted[0].equals("@zl");
            boolean luk = splitted[0].equals("@yq");
            boolean dex = splitted[0].equals("@mj");
            if (amount > 0 && amount <= player.getRemainingAp() && amount <= 29996) {
                if (str && amount + player.getStr() <= c.getChannelServer().getMaxStat()) {
                    player.setStr(player.getStr() + amount);
                    player.updateSingleStat(MapleStat.STR, player.getStr());
                } else if (Int && amount + player.getInt() <= c.getChannelServer().getMaxStat()) {
                    player.setInt(player.getInt() + amount);
                    player.updateSingleStat(MapleStat.INT, player.getInt());
                } else if (luk && amount + player.getLuk() <= c.getChannelServer().getMaxStat()) {
                    player.setLuk(player.getLuk() + amount);
                    player.updateSingleStat(MapleStat.LUK, player.getLuk());
                } else if (dex && amount + player.getDex() <= c.getChannelServer().getMaxStat()) {
                    player.setDex(player.getDex() + amount);
                    player.updateSingleStat(MapleStat.DEX, player.getDex());
                } else {
                    mc.dropMessage("梦の岛提示你：请确保你当前的属性不超过限定值.");
                }
                player.setRemainingAp(player.getRemainingAp() - amount);
                player.updateSingleStat(MapleStat.AVAILABLEAP, player.getRemainingAp());
            } else {
                mc.dropMessage("梦の岛提示你：请确保你当前的属性不超过限定值，且你有足够的AP分配.");
            }

        } else if (splitted[0].equals("@jiasi")) {
            NPCScriptManager.getInstance().dispose(c);
            mc.dropMessage("梦の岛提示你：NPC假死已修复，现在试试看吧！");
            c.getSession().write(MaplePacketCreator.enableActions());
            	} else if (splitted[0].equals("@ring")) {
			int itemId = Integer.parseInt(splitted[1]);
                    int partnerId = Integer.parseInt(splitted[2]);
                    String partnerName = MapleCharacter.getNameById(partnerId, 0);
			int[] ret = MapleRing.createRing(c, itemId, player.getId(), player.getName(), partnerId, partnerName);
			if (ret[0] == -1 || ret[1] == -1) {
				mc.dropMessage("梦の岛提示你：发生未知的错误.");
				mc.dropMessage("梦の岛提示你：请确保你要赠送戒指的人跟你在同一频道并且在线.");
			}
        } else if (splitted[0].equals("@ziyou")) {
            if (!player.inJail()) {
            if ((c.getPlayer().getMapId() < 910000000) || (c.getPlayer().getMapId() > 910000022)){
               new ServernoticeMapleClientMessageCallback(5, c).dropMessage("梦の岛提示你：你已被传送到自由市场！.");
               c.getSession().write(MaplePacketCreator.enableActions());
                 MapleMap to;
                 MaplePortal pto;
                               to = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(910000000);
                               c.getPlayer().saveLocation(SavedLocationType.FREE_MARKET);
                               pto = to.getPortal("out00");
                                 c.getPlayer().changeMap(to, pto);
             } else {
                               new ServernoticeMapleClientMessageCallback(5, c).dropMessage("梦の岛提示你：你已经在自由市场了。");
               c.getSession().write(MaplePacketCreator.enableActions());
             }
            } else {
            mc.dropMessage("梦の岛提示你：对不起，你在监狱服刑中.");
            }
           } else if (splitted[0].equals("@quanxian")) {
                 if (player.isGM()) {
                  mc.dropMessage("梦の岛提示你：梦の岛提示你：您是尊贵的游戏管理员");
                  mc.dropMessage("梦の岛提示你：请使用!help命令获得帮助");
                                  }else if (c.getPlayer().vip >= 5) {
                 mc.dropMessage("梦の岛提示你：您是尊贵的VIP5，您的VIP等级level 5");
                 mc.dropMessage("梦の岛提示你：请使用$viphelp获得帮助");
                                 }else if (c.getPlayer().vip >= 4) {
                 mc.dropMessage("梦の岛提示你：您是尊贵的VIP4，您的VIP等级level 4");
                 mc.dropMessage("梦の岛提示你：请使用$viphelp获得帮助");
                }else if (c.getPlayer().vip >= 3) {
                 mc.dropMessage("梦の岛提示你：您是尊贵的VIP3，您的VIP等级level 3");
                 mc.dropMessage("梦の岛提示你：请使用$viphelp获得帮助");
           }else if (c.getPlayer().vip >= 2) {
                 mc.dropMessage("梦の岛提示你：您是尊贵的VIP2，您的VIP等级level 2");
                 mc.dropMessage("梦の岛提示你：请使用$viphelp获得帮助");
                  }else if (c.getPlayer().vip >= 1) {
                      mc.dropMessage("梦の岛提示你：您是尊贵的VIP1，您的VIP等级level 1");
                      mc.dropMessage("梦の岛提示你：请使用$viphelp获得帮助");
                                        }else if (player.getLevel() == 180 ) {
                      mc.dropMessage("梦の岛提示你：您是超级玩家");
                      mc.dropMessage("梦の岛提示你：请使用@superhelp获得帮助");
                       }else{
                          mc.dropMessage("梦の岛提示你：尊敬的玩家，欢迎来到梦の境.");
                          mc.dropMessage("梦の岛提示你：请使用@help获得帮助");
                 }
                    } else if (splitted[0].equals("@superhelp")) {
                                               if(player.getLevel() == 180){
                       mc.dropMessage("____________________________");
                    mc.dropMessage(" 服务器超级玩家命令");
                    mc.dropMessage("____________________________");
                            mc.dropMessage("梦の岛提示你： 您好,[" + c.getPlayer().getName() + "]欢迎来查看超级玩家指令");
                            mc.dropMessage("梦の岛提示你：@cjbx 超级玩家专用补血魔功能,要2000W冒险币);");
                        }
        } else if (splitted[0].equals("@changename")) {
            if ((player.getMeso() > 999999999 || player.getKarma() > 40) && MapleCharacterUtil.canCreateChar(splitted[1], 0)) {
                if (player.getKarma() > 40) {
                    player.downKarma();
                    mc.dropMessage("梦の岛提示你：你已经降低你的karma来更改一个新的名称.");
                } else {
                    player.gainMeso(-1000000000, false);
                }
                player.setName(splitted[1]);
                c.getSession().write(MaplePacketCreator.getCharInfo(player));
                player.getMap().removePlayer(player);
                player.getMap().addPlayer(player);
            } else {
                mc.dropMessage("梦の岛提示你：你必须有10E冒险币才可以更改名称，但是你的名称也必须是合法的.");
            }

        } else if (splitted[0].equals("@rebirth")) {
            int negexp;
            if (player.getLevel() > 199) {
              mc.dropMessage("梦の岛提示你：此功能禁止~请去自由2楼娜娜处使用啦.");
            } else {
                mc.dropMessage("梦の岛提示你：此功能禁止~请去自由2楼娜娜处使用啦.");
            }
        } else if (splitted[0].equals("@expfix")) {
            player.setExp(0);
            player.updateSingleStat(MapleStat.EXP, Integer.valueOf(0));
            mc.dropMessage("梦の岛提示你：你的经验值已经修复.");
        } else if (splitted[0].equals("@hideout")) {
            if (player.getGuildId() == 0) {
                mc.dropMessage("梦の岛提示你：你目前没有家族.");
            } else {
                MapleGuildSummary g = cserv.getGuildSummary(player.getGuildId());
                int mapid = g.getHideout();
                String guildname = g.getName();
                if (mapid == -1) {
                    mc.dropMessage("梦の岛提示你：你的家族没有领地");
                } else if (player.getClient().getChannel() == 1) {
                    if (player.getMapId() != mapid) {
                        player.saveLocation(SavedLocationType.HIDEOUT);
                        MapleMap target = cserv.getMapFactory().getMap(mapid);
                        MaplePortal targetPortal = target.getPortal(0);
                        player.changeMap(target, targetPortal);
                        mc.dropMessage("梦の岛提示你：欢迎来到 " + guildname + " 的领地");
                    } else if (player.getMapId() == mapid) {
                        int smap = player.getSavedLocation(SavedLocationType.HIDEOUT);
                        MapleMap target = cserv.getMapFactory().getMap(smap);
                        MaplePortal targetPortal = target.getPortal(0);
                        player.changeMap(target, targetPortal);
                        player.clearSavedLocation(SavedLocationType.HIDEOUT);
                    }
                } else {
                    mc.dropMessage("梦の岛提示你：家族领地在同一个频道");
                }
            }
        } else if (splitted[0].equals("@zijiu")) {
            try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("UPDATE ACCOUNTS SET loggedin = 0");
                ps.executeUpdate();
                ps.close();
                mc.dropMessage("梦の岛提示你：自救成功.");
            } catch (SQLException ex) {
                mc.dropMessage("梦の岛提示你：自救失败，过程发生了一些错误.");
            }
        } else if (splitted[0].equals("@wnnpc")) {
            if (!player.inJail()) {
                NPCScriptManager npc = NPCScriptManager.getInstance();
                npc.start(c, 9000020, null, null);
            } else {
                mc.dropMessage("梦の岛提示你：对不起，你在监狱中.胜胜吩咐不能把你传出去的");
            }
              } else if (splitted[0].equals("@gm")) {
            if (!player.inJail()) {
                NPCScriptManager npc = NPCScriptManager.getInstance();
                npc.start(c, 2100008, null, null);
            } else {
                mc.dropMessage("梦の岛提示你：对不起，你在监狱中.XX吩咐不能让你啰嗦");
            }
            } else if (splitted[0].equals("@zaixian")) {
            if (c.getPlayer().getMeso() < 100000000) {
                        mc.dropMessage("梦の岛提示你：每次执行该命令系统将自动扣除您的查看费用1亿游戏币！");
                    } else {
             c.getPlayer().gainMeso(-100000000, true);
			mc.dropMessage("梦の岛提示你：频道服务器 " + c.getChannel() + " 当前状态:");
			Collection<MapleCharacter> chrs = c.getChannelServer().getInstance(c.getChannel()).getPlayerStorage().getAllCharacters();
			for (MapleCharacter chr : chrs) {
				mc.dropMessage("梦の岛提示你： 角色ID：" + chr.getId() + " 角色名：" + chr.getName() + " :所在地图ID: " + chr.getMapId());
			}
			mc.dropMessage("梦の岛提示你： 频道服务器 " + c.getChannel() + "  当前总计： " + chrs.size() + "人在线.");
            }
             	} else if (splitted[0].equals("@cjbx") && player.getLevel() == 200 ) {
            if (c.getPlayer().getMeso() < 20000000) {
                        mc.dropMessage("梦の岛提示你： 每次执行该命令系统将自动扣除您的查看费用2000W游戏币！");
                    } else {
             c.getPlayer().gainMeso(-20000000, true);
			player.setHp(player.getMaxHp());
            player.updateSingleStat(MapleStat.HP, player.getMaxHp());
            player.setMp(player.getMaxMp());
            player.updateSingleStat(MapleStat.MP, player.getMaxMp());
            }
     
        } else {
            if (player.gmLevel() >= 0) {
                mc.dropMessage("梦の岛提示你：您输入的玩家命令 " + splitted[0] + " 不存在.");
            }
            return false;
        }
        return true;
    }
}