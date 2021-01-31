/*
Jiongorz. Src. Command.java
JiongorzBBs : http://bbs.jiongorz.com
AdminQQ:307586193
Command
*/
package net.sf.odinms.client.messages;


import java.util.Collection;
import net.sf.odinms.client.*;
import net.sf.odinms.server.*;
import net.sf.odinms.net.channel.ChannelServer;
import net.sf.odinms.scripting.npc.NPCScriptManager;
import java.util.HashMap;
import net.sf.odinms.server.maps.*;



public class VipCommand {

    public static boolean executeVipCommand(MapleClient c, MessageCallback mc, String line) {
        String[] splitted = line.split(" ");
        ChannelServer cserv = c.getChannelServer();
        MapleCharacter player = c.getPlayer();
        if (splitted[0].equals("$goto")) {
if (splitted.length < 2){

     mc.dropMessage("指令用法: $goto <地图名字>");
    mc.dropMessage("nangang<南港>, caihong<彩虹村>, sheshou<射手村>, mofa<魔法密林>,");
    mc.dropMessage("yongshi<勇士部落>, kerning<废气都市>, lith<明珠港>, sleepywood<林中之城>, florina<黄金海滩>,");
   mc.dropMessage("orbis<天空之城>, happy<幸福村>, elnath<冰封雪域>, ludi<玩具城>, aqua<海底世界>,");
   mc.dropMessage("leafre<神木村>, mulung<武陵村>, herb<百草堂>, omega<地球防卫总部>, korean<童话村>,");
   mc.dropMessage("nlc<新叶城>, excavation<考古发掘地>, pianus<鱼王>, mushmom<蘑茹王>, ");
                       mc.dropMessage("griffey<胖凤>, manon<肥龙>, horseman<火马>, balrog<蝙蝠魔>, ");
                       mc.dropMessage("showa<昭和村>, guild<家族>, shrine<樱花村>, fm<自由市场>, skelegon<骨龙>, pvp<PK地图>");
}else{
    HashMap<String, Integer> gotomaps = new HashMap<String, Integer>();
    gotomaps.put("nangang", 60000);
    gotomaps.put("caihong", 1010000);
    gotomaps.put("sheshou", 100000000);
    gotomaps.put("mofa", 101000000);
    gotomaps.put("perion", 102000000);
    gotomaps.put("kerning", 103000000);
    gotomaps.put("lith", 104000000);
    gotomaps.put("sleepywood", 105040300);
    gotomaps.put("florina", 110000000);
    gotomaps.put("orbis", 200000000);
    gotomaps.put("happy", 209000000);
    gotomaps.put("elnath", 211000000);
    gotomaps.put("ludi", 220000000);
    gotomaps.put("aqua", 230000000);
    gotomaps.put("leafre", 240000000);
    gotomaps.put("mulung", 250000000);
    gotomaps.put("herb", 251000000);
    gotomaps.put("omega", 221000000);
    gotomaps.put("korean", 222000000);
    gotomaps.put("nlc", 600000000);
    gotomaps.put("excavation", 990000000);
    gotomaps.put("pianus", 230040420);
    gotomaps.put("mushmom", 100000005);
    gotomaps.put("griffey", 240020101);
    gotomaps.put("manon", 240020401);
    gotomaps.put("horseman", 682000001);
    gotomaps.put("balrog", 105090900);
    gotomaps.put("showa", 801000000);
    gotomaps.put("guild", 200000301);
    gotomaps.put("shrine", 800000000);
    gotomaps.put("fm", 910000000);
    gotomaps.put("skelegon", 240040511);
    gotomaps.put("pvp", 800020400);

    if (gotomaps.containsKey(splitted[1])){

        MapleMap target = cserv.getMapFactory().getMap(gotomaps.get(splitted[1]));
        MaplePortal targetPortal = target.getPortal(0);
        player.changeMap(target, targetPortal);

    }else{
        mc.dropMessage("没有这样的地方 !!!");
    }
    }

            } else if (splitted[0].equals("$vipmap1")) {
            if (c.getPlayer().vip >= 1) {
            MapleMap target = cserv.getMapFactory().getMap(209000000);
            MaplePortal targetPortal = target.getPortal(0);
            player.changeMap(target, targetPortal);
             }else{
            mc.dropMessage("您不是VIP2，不能使用此命令.");
            }
} else if (splitted[0].equals("$vipmap2")) {
            if (c.getPlayer().vip >= 2) {
            MapleMap target = cserv.getMapFactory().getMap(2);
            MaplePortal targetPortal = target.getPortal(0);
            player.changeMap(target, targetPortal);
             }else{
            mc.dropMessage("您不是VIP2，不能使用此命令.");
            }
            } else if (splitted[0].equals("$vipmap4")) {
            if (c.getPlayer().vip >= 4) {
            MapleMap target = cserv.getMapFactory().getMap(922020300);
            MaplePortal targetPortal = target.getPortal(0);
            player.changeMap(target, targetPortal);
             }else{
            mc.dropMessage("您不是VIP4，不能使用此命令.");
            }
                        } else if (splitted[0].equals("$vipmap5")) {
            if (c.getPlayer().vip >= 5) {
            MapleMap target = cserv.getMapFactory().getMap(801040101);
            MaplePortal targetPortal = target.getPortal(0);
            player.changeMap(target, targetPortal);
             }else{
            mc.dropMessage("您不是VIP5，不能使用此命令.");
            }
} else if (splitted[0].equals("$vipmap3")) {
            if (c.getPlayer().vip >= 3) {
            MapleMap target = cserv.getMapFactory().getMap(920010000);
            MaplePortal targetPortal = target.getPortal(0);
            player.changeMap(target, targetPortal);
            }else{
            mc.dropMessage("您不是VIP3，不能使用此命令.");
            }
            } else if (splitted[0].equals("$vipzt")) {
            if (c.getPlayer().vip >= 5) {
            int[] array = {2001002, 1101007, 1005, 2301003, 5121009, 4111001, 4111002, 4211003, 4211005, 1321000, 2321004, 3121002};
            for (int i = 0; i < array.length; i++) {
                SkillFactory.getSkill(array[i]).getEffect(SkillFactory.getSkill(array[i]).getMaxLevel()).applyTo(player);
                 }
            }else{
            mc.dropMessage("您不是VIP5，不能使用此命令.嘎嘎鄙视你");
            }
   } else if (splitted[0].equals("$heal")) {
            player.setHp(player.getMaxHp());
            player.updateSingleStat(MapleStat.HP, player.getMaxHp());
            player.setMp(player.getMaxMp());
            player.updateSingleStat(MapleStat.MP, player.getMaxMp());
} else if (splitted[0].equals("$viphelp")) {
     mc.dropMessage("尊贵的VIP，以下是您的专属命令帮助.");
     mc.dropMessage("$vipmap1   -----传送到VIP1地图.");
     mc.dropMessage("$vipmap2   -----传送到VIP2地图");
     mc.dropMessage("$vipmap3   -----传送到VIP3地图");
    mc.dropMessage("$vipmap4   -----传送到VIP4地图");
   mc.dropMessage("$vipmap5   -----传送到VIP5地图");
     mc.dropMessage("$goto      -----快捷传送.");
     mc.dropMessage("$heal      -----VIP补满血魔(VIP4即用).");
     mc.dropMessage("$vipzt      -----给自己添加辅助技能.(VIP5专属)");
     mc.dropMessage("$vipzaixian     -----VIP免费查在线.");
	} else if (splitted[0].equals("$vipzaixian") && c.getPlayer().vip >= 1) {
            if (c.getPlayer().getMeso() < 0) {
                        mc.dropMessage("{ VIP指令 } 每次执行该命令系统将自动扣除您的查看费用0游戏币！");
                    } else {
			mc.dropMessage("[系统信息] 频道服务器 " + c.getChannel() + " 当前状态:");
			Collection<MapleCharacter> chrs = c.getChannelServer().getInstance(c.getChannel()).getPlayerStorage().getAllCharacters();
			for (MapleCharacter chr : chrs) {
				mc.dropMessage("[系统信息] 角色ID：" + chr.getId() + " 角色名：" + chr.getName() + " :所在地图ID: " + chr.getMapId());
			}
			mc.dropMessage("[系统信息] 频道服务器 " + c.getChannel() + "  当前总计： " + chrs.size() + "人在线.");
            }
        } else {
            if (player.vip >= 1) {
                mc.dropMessage("您输入的vip命令 " + splitted[0] + " 不存在.");
            }
            return false;
        }
        return true;
    }
}