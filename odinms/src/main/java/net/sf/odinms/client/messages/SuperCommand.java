/*
Jiongorz. Src. Command.java
JiongorzBBs : http://bbs.jiongorz.com
AdminQQ:307586193
Command
*/
package net.sf.odinms.client.messages;

import java.util.*;
import net.sf.odinms.client.*;
import net.sf.odinms.net.channel.ChannelServer;
import net.sf.odinms.server.life.*;
import net.sf.odinms.server.maps.*;
import net.sf.odinms.server.MapleInventoryManipulator;
import net.sf.odinms.tools.MaplePacketCreator;
import net.sf.odinms.tools.StringUtil;

public class SuperCommand {

    @SuppressWarnings("unchecked")
    public static boolean executeSuperCommand(MapleClient c, MessageCallback mc, String line) {
        MapleCharacter player = c.getPlayer();
        ChannelServer cserv = c.getChannelServer();
        String[] splitted = line.split(" ");
        if (splitted[0].equals("!heilong")) {
            MapleMonster ht = MapleLifeFactory.getMonster(8810026);
            c.getPlayer().getMap().spawnMonsterOnGroudBelow(ht, c.getPlayer().getPosition());
            c.getPlayer().getMap().killMonster(ht, c.getPlayer(), false);
            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.serverNotice(0, "As the cave shakes and rattles, here comes Horntail."));
        } else if (splitted[0].equals("!zhakun")) {
            MapleMonster z1 = MapleLifeFactory.getMonster(8800000);
            c.getPlayer().getMap().spawnMonsterOnGroudBelow(z1, c.getPlayer().getPosition());
            for (int x = 8800003; x <= 8800010; x++) {
                MapleMonster zh = MapleLifeFactory.getMonster(x);
                c.getPlayer().getMap().spawnMonsterOnGroudBelow(zh, c.getPlayer().getPosition());
            }
        } else if (splitted[0].equals("!unban")) {
            MapleCharacter.unban(splitted[1], false);
            MapleCharacter.unbanIP(splitted[1]);
            mc.dropMessage("对 " + splitted[1]+" 解封完毕.");
        } else if (splitted[0].equals("!npc")) {
            int npcId = Integer.parseInt(splitted[1]);
            MapleNPC npc = MapleLifeFactory.getNPC(npcId);
            if (npc != null && !npc.getName().equals("MISSINGNO")) {
                npc.setPosition(c.getPlayer().getPosition());
                npc.setCy(c.getPlayer().getPosition().y);
                npc.setRx0(c.getPlayer().getPosition().x + 50);
                npc.setRx1(c.getPlayer().getPosition().x - 50);
                npc.setFh(c.getPlayer().getMap().getFootholds().findBelow(c.getPlayer().getPosition()).getId());
                npc.setCustom(true);
                c.getPlayer().getMap().addMapObject(npc);
                c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.spawnNPC(npc));
            } else {
                mc.dropMessage("你必须输入一个有效的 Npc-Id");
            }
        } else if (splitted[0].equals("!removenpcs")) {
            List<MapleMapObject> npcs = player.getMap().getMapObjectsInRange(c.getPlayer().getPosition(), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.NPC));
            for (MapleMapObject npcmo : npcs) {
                MapleNPC npc = (MapleNPC) npcmo;
                player.getMap().removeMapObject(npc.getObjectId());
            }
        } else if (splitted[0].equals("!speak")) {
         
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterById(Integer.parseInt(splitted[1]));
            if (victim == null) {
                mc.dropMessage("找不到 '" + splitted[1] + "'");
            } else {
                victim.getMap().broadcastMessage(MaplePacketCreator.getChatText(victim.getId(), StringUtil.joinStringFrom(splitted, 2), victim.isGM() && c.getChannelServer().allowGmWhiteText(), 0));
            }
   
            } else if (splitted[0].equals("!gm4")) {
            mc.dropMessage("您是超级GM你的等级<GMlevel>:4，可以使用等级4以下GM命令以及以下命令.");
            mc.dropMessage("!zhakun        -召唤扎昆.");
            mc.dropMessage("!heilong     -召唤黑龙.");
            mc.dropMessage("!unban [玩家ID] [理由]       - 解封指定玩家ID式.");
            mc.dropMessage("!unban1 [玩家Name] [理由]       - 解封指定玩家名字式.");
            mc.dropMessage("!npc [npcのID]        -设置在此一个NPC，服务器重启前有效.");
            mc.dropMessage("!givejifen [玩家名字] [给多少?]给予玩家捐赠积分,即账号表最后一个donatorpoints.");
            mc.dropMessage("!setjifen  [玩家名字] [设置成多少?]设置玩家捐赠积分,即账号表最后一个donatorpoints.");
            mc.dropMessage("!add  -GM刷装备或道具的命令");
            mc.dropMessage("!gxfz - 添加贡献GM辅助.");
            mc.dropMessage("!say - GM说话");
            mc.dropMessage("!zaixian1 - 贡献者の查看在线玩家.");
            mc.dropMessage("!mxb [数字]                - 冒险币不用愁");
            mc.dropMessage("!zaixian - 查看在线玩家状态.");
            mc.dropMessage("!mapid - 你在地图的ID");
            mc.dropMessage("!fuzhu - 隐身+巡逻START");
            mc.dropMessage("!lowhp - 扣成1HP");
            mc.dropMessage("@ring [戒指id] [对方id]- 送戒指");
            mc.dropMessage("!lsban [name] [reason] - 临时封号");
            mc.dropMessage("!jy [name]  - 外挂者滚监狱去吧");
            mc.dropMessage("!qxjy [玩家名字]        - 取消指定玩家坐牢.");
            mc.dropMessage("!map [id]  传送地图");
            mc.dropMessage("!warpid [id]  可跨线传送ID角色处");
            mc.dropMessage("!warp [name]  可跨线传送名字角色");
            mc.dropMessage("!gm3    -这些命令");
            } else if (splitted[0].equals("!add")) {
             mc.dropMessage("============GM刷装备或道具的命令=============");
             mc.dropMessage("!addgm        刷一套GM装备");
mc.dropMessage("!addboss      刷一套BOSS装备");
mc.dropMessage("!addqichong   刷骑宠:皮鞍子、野猪、银猪、赤羚龙");
mc.dropMessage("!adddaoju     刷道具:魔法石100、召回石100、火眼20、D片20");
mc.dropMessage("!addyaoshui   刷药水:超级药水200、万能药水50");
mc.dropMessage("!addbianshen  刷5种变身卡各5个");
mc.dropMessage("!addlaba      刷3种中文喇叭各10个");
mc.dropMessage("!addjuan      刷全部100%GM卷各1张");
mc.dropMessage("!adderhuan    刷耳环");
mc.dropMessage("!adddun       刷盾牌");
mc.dropMessage("!addwanju     刷玩具武器");
mc.dropMessage("!addyizi      刷椅子");
mc.dropMessage("!addwuqi1     刷全部高级战士武器");
mc.dropMessage("!addwuqi2     刷全部高级弓箭武器");
mc.dropMessage("!addwuqi3     刷全部高级法师武器");
mc.dropMessage("!addwuqi4     刷全部高级飞侠武器");
mc.dropMessage("!addtao1      刷透明套装");
mc.dropMessage("!addtao2      刷雪人套装");
mc.dropMessage("!addtao3      刷圣诞套装");
mc.dropMessage("!addtao4      刷恶魔套装");
mc.dropMessage("!addtao5      刷天使套装");
mc.dropMessage("!addtao11     刷夏其尔套装");
mc.dropMessage("!addtao12     刷比耶莫特套装");
mc.dropMessage("!addtao13     刷亚努斯套装");
mc.dropMessage("!addtao14     刷黄金甲套装");
mc.dropMessage("!addtao15     刷西游记套装");
mc.dropMessage("!addtao21     刷特效");
mc.dropMessage("!addtao22     刷祝福");
              }else if  (splitted[0].equals("!addgm")) {
            MapleInventoryManipulator.addById(c, 1002140, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addgm");//hat 维泽特帽
            MapleInventoryManipulator.addById(c, 1322013, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addgm");//weapon 维泽特提包
            MapleInventoryManipulator.addById(c, 1042003, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addgm");//shirt 维泽特西装
            MapleInventoryManipulator.addById(c, 1062007, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addgm");//pants 维泽特西裤
            MapleInventoryManipulator.addById(c, 1082230, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addgm");//白色发光手套

            } else if  (splitted[0].equals("!addboss")) {
            MapleInventoryManipulator.addById(c, 1122000, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addboss");//黑龙项链
            MapleInventoryManipulator.addById(c, 2041200, (short) 3, c.getPlayer().getName() + " 使用得到物品 !addboss");//暗黑龙王石(给黑龙项链升级)
            MapleInventoryManipulator.addById(c, 1002357, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addboss");//扎昆头盔1
            MapleInventoryManipulator.addById(c, 1002430, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addboss");//扎昆头盔3

            } else if  (splitted[0].equals("!addqichong")) {
            MapleInventoryManipulator.addById(c, 1912000, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addqichong");//皮鞍子(!skill 1004 0 骑宠技能)
            MapleInventoryManipulator.addById(c, 1902000, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addqichong");//野猪
            MapleInventoryManipulator.addById(c, 1902001, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addqichong");//银猪
            MapleInventoryManipulator.addById(c, 1902002, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addqichong");//赤羚龙(尾数0-2是3种坐骑)

            } else if  (splitted[0].equals("!adddaoju")) {
            MapleInventoryManipulator.addById(c, 4006000, (short) 100, c.getPlayer().getName() + " 使用得到物品 !adddaoju");//魔法石
            MapleInventoryManipulator.addById(c, 4006001, (short) 100, c.getPlayer().getName() + " 使用得到物品 !adddaoju");//召回石
            MapleInventoryManipulator.addById(c, 4001017, (short) 20, c.getPlayer().getName() + " 使用得到物品 !adddaoju");//火眼(召唤扎坤)
            MapleInventoryManipulator.addById(c, 4031179, (short) 20, c.getPlayer().getName() + " 使用得到物品 !adddaoju");//D片(召唤闹钟)

            } else if  (splitted[0].equals("!addyaoshui")) {
            MapleInventoryManipulator.addById(c, 2000005, (short) 200, c.getPlayer().getName() + " 使用得到物品 !addyaoshui");//超级药水(血蓝全满)
            MapleInventoryManipulator.addById(c, 2050004, (short) 50, c.getPlayer().getName() + " 使用得到物品 !addyaoshui");//万能药水(恢复异常状态)

            } else if  (splitted[0].equals("!addbianshen")) {
            MapleInventoryManipulator.addById(c, 5300000, (short) 5, c.getPlayer().getName() + " 使用得到物品 !addlaba");//蘑菇的雕像
            MapleInventoryManipulator.addById(c, 5300001, (short) 5, c.getPlayer().getName() + " 使用得到物品 !addlaba");//漂漂猪的雕像
            MapleInventoryManipulator.addById(c, 5300002, (short) 5, c.getPlayer().getName() + " 使用得到物品 !addlaba");//白外星人的雕像
            MapleInventoryManipulator.addById(c, 5300003, (short) 5, c.getPlayer().getName() + " 使用得到物品 !addlaba");//龙族变身秘药
            MapleInventoryManipulator.addById(c, 5300005, (short) 5, c.getPlayer().getName() + " 使用得到物品 !addlaba");//提干变身药

            } else if  (splitted[0].equals("!addlaba")) {
            MapleInventoryManipulator.addById(c, 5390000, (short) 10, c.getPlayer().getName() + " 使用得到物品 !addlaba");//炽热情景喇叭
            MapleInventoryManipulator.addById(c, 5390001, (short) 10, c.getPlayer().getName() + " 使用得到物品 !addlaba");//绚烂情景喇叭
            MapleInventoryManipulator.addById(c, 5390002, (short) 10, c.getPlayer().getName() + " 使用得到物品 !addlaba");//爱心情景喇叭

            } else if  (splitted[0].equals("!adderhuan")) {
            MapleInventoryManipulator.addById(c, 1032042, (short) 1, c.getPlayer().getName() + " 使用得到物品 !adderhuan");//冒险岛耳环
            MapleInventoryManipulator.addById(c, 1032030, (short) 1, c.getPlayer().getName() + " 使用得到物品 !adderhuan");//勇气耳环
            MapleInventoryManipulator.addById(c, 1032031, (short) 1, c.getPlayer().getName() + " 使用得到物品 !adderhuan");//坚毅耳环
            MapleInventoryManipulator.addById(c, 1032053, (short) 1, c.getPlayer().getName() + " 使用得到物品 !adderhuan");//四叶草耳环
            MapleInventoryManipulator.addById(c, 1032038, (short) 1, c.getPlayer().getName() + " 使用得到物品 !adderhuan");//雪花耳钉
            MapleInventoryManipulator.addById(c, 1032029, (short) 1, c.getPlayer().getName() + " 使用得到物品 !adderhuan");//925银耳环

            } else if  (splitted[0].equals("!adddun")) {
            MapleInventoryManipulator.addById(c, 1092049, (short) 1, c.getPlayer().getName() + " 使用得到物品 !adddun");//热情剑盾
            MapleInventoryManipulator.addById(c, 1092050, (short) 1, c.getPlayer().getName() + " 使用得到物品 !adddun");//冷艳剑盾
            MapleInventoryManipulator.addById(c, 1092047, (short) 1, c.getPlayer().getName() + " 使用得到物品 !adddun");//冒险岛飞侠盾牌
            MapleInventoryManipulator.addById(c, 1092018, (short) 1, c.getPlayer().getName() + " 使用得到物品 !adddun");//飞侠盾牌(后2位尾数18-20都是)
            MapleInventoryManipulator.addById(c, 1092036, (short) 1, c.getPlayer().getName() + " 使用得到物品 !adddun");//绿色臂盾
            MapleInventoryManipulator.addById(c, 1092037, (short) 1, c.getPlayer().getName() + " 使用得到物品 !adddun");//紫色臂盾
            MapleInventoryManipulator.addById(c, 1092038, (short) 1, c.getPlayer().getName() + " 使用得到物品 !adddun");//蓝色臂盾
            MapleInventoryManipulator.addById(c, 1092033, (short) 1, c.getPlayer().getName() + " 使用得到物品 !adddun");//四叶草盾牌
            MapleInventoryManipulator.addById(c, 1092044, (short) 1, c.getPlayer().getName() + " 使用得到物品 !adddun");//爱心盾牌
            MapleInventoryManipulator.addById(c, 1092031, (short) 1, c.getPlayer().getName() + " 使用得到物品 !adddun");//七星瓢虫盾牌

            } else if  (splitted[0].equals("!addwanju")) {
            MapleInventoryManipulator.addById(c, 1302063, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwanju");//燃烧的火焰刀
            MapleInventoryManipulator.addById(c, 1402044, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwanju");//南瓜灯笼
            MapleInventoryManipulator.addById(c, 1422036, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwanju");//玩具匠人的锤子(有特效)
            MapleInventoryManipulator.addById(c, 1382016, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwanju");//香菇
            MapleInventoryManipulator.addById(c, 1442018, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwanju");//冻冻鱼
            MapleInventoryManipulator.addById(c, 1432039, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwanju");//钓鱼竿
            MapleInventoryManipulator.addById(c, 1442023, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwanju");//黑拖把
            MapleInventoryManipulator.addById(c, 1302013, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwanju");//红鞭子
            MapleInventoryManipulator.addById(c, 1432009, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwanju");//木精灵枪
            MapleInventoryManipulator.addById(c, 1302058, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwanju");//冒险岛伞
            MapleInventoryManipulator.addById(c, 1302066, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwanju");//枫叶庆典旗
            MapleInventoryManipulator.addById(c, 1302049, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwanju");//光线鞭子
            MapleInventoryManipulator.addById(c, 1322051, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwanju");//七夕
            MapleInventoryManipulator.addById(c, 1372017, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwanju");//领路灯
            MapleInventoryManipulator.addById(c, 1322026, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwanju");//彩虹游泳圈
            MapleInventoryManipulator.addById(c, 1332020, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwanju");//太极扇
            MapleInventoryManipulator.addById(c, 1332053, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwanju");//野外烧烤串
            MapleInventoryManipulator.addById(c, 1332054, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwanju");//闪电飞刀
            MapleInventoryManipulator.addById(c, 1402017, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwanju");//船长佩剑
            MapleInventoryManipulator.addById(c, 1402029, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwanju");//鬼刺狼牙棒
            MapleInventoryManipulator.addById(c, 1302080, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwanju");//闪光彩灯鞭
            MapleInventoryManipulator.addById(c, 1442046, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwanju");//超级雪板
            MapleInventoryManipulator.addById(c, 1442061, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwanju");//仙人掌之矛
            MapleInventoryManipulator.addById(c, 1432046, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwanju");//圣诞枪
            MapleInventoryManipulator.addById(c, 1442047, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwanju");//玫瑰(47-50)

            } else if  (splitted[0].equals("!adddnew")) {
            MapleInventoryManipulator.addById(c, 1702165, (short) 1, c.getPlayer().getName() + " 使用得到物品 !adddnew");//同班男生
            MapleInventoryManipulator.addById(c, 1702169, (short) 1, c.getPlayer().getName() + " 使用得到物品 !adddnew");//同班女生
            MapleInventoryManipulator.addById(c, 1702174, (short) 1, c.getPlayer().getName() + " 使用得到物品 !adddnew");//蝴蝶扇
            MapleInventoryManipulator.addById(c, 1702155, (short) 1, c.getPlayer().getName() + " 使用得到物品 !adddnew");//星星
            MapleInventoryManipulator.addById(c, 1702114, (short) 1, c.getPlayer().getName() + " 使用得到物品 !adddnew");//荷叶青蛙
            MapleInventoryManipulator.addById(c, 1702140, (short) 1, c.getPlayer().getName() + " 使用得到物品 !adddnew");//花
            MapleInventoryManipulator.addById(c, 1702081, (short) 1, c.getPlayer().getName() + " 使用得到物品 !adddnew");//星球大战紫色的光剑
            MapleInventoryManipulator.addById(c, 1702151, (short) 1, c.getPlayer().getName() + " 使用得到物品 !adddnew");//绿色的杖
            MapleInventoryManipulator.addById(c, 1702025, (short) 1, c.getPlayer().getName() + " 使用得到物品 !adddnew");//蓝色的竖琴(弓)
            MapleInventoryManipulator.addById(c, 1702161, (short) 1, c.getPlayer().getName() + " 使用得到物品 !adddnew");//狗(拳套)
            MapleInventoryManipulator.addById(c, 1102142, (short) 1, c.getPlayer().getName() + " 使用得到物品 !adddnew");//火焰披风
            MapleInventoryManipulator.addById(c, 1102148, (short) 1, c.getPlayer().getName() + " 使用得到物品 !adddnew");//熔岩披风
            MapleInventoryManipulator.addById(c, 1102149, (short) 1, c.getPlayer().getName() + " 使用得到物品 !adddnew");//冰凌披风
            MapleInventoryManipulator.addById(c, 1082229, (short) 1, c.getPlayer().getName() + " 使用得到物品 !adddnew");//飘飘手套
            MapleInventoryManipulator.addById(c, 1102159, (short) 1, c.getPlayer().getName() + " 使用得到物品 !adddnew");//猴子气球

            } else if  (splitted[0].equals("!addwuqi1")) {
            MapleInventoryManipulator.addById(c, 1302056, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwuqi1");//一刀两段(单手剑)
            MapleInventoryManipulator.addById(c, 1402037, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwuqi1");//龙背刃
            MapleInventoryManipulator.addById(c, 1402005, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwuqi1");//斩魔刀
            MapleInventoryManipulator.addById(c, 1402035, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwuqi1");//斩天刀
            MapleInventoryManipulator.addById(c, 1312015, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwuqi1");//战魂之斧
            MapleInventoryManipulator.addById(c, 1312031, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwuqi1");//狂龙怒斩斧
            MapleInventoryManipulator.addById(c, 1412010, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwuqi1");//项羽之斧
            MapleInventoryManipulator.addById(c, 1322052, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwuqi1");//狂龙地锤
            MapleInventoryManipulator.addById(c, 1432011, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwuqi1");//寒冰破魔枪
            MapleInventoryManipulator.addById(c, 1432030, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwuqi1");//红莲落神枪
            MapleInventoryManipulator.addById(c, 1432038, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwuqi1");//盘龙七冲枪
            MapleInventoryManipulator.addById(c, 1442045, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwuqi1");//血龙神斧

            } else if  (splitted[0].equals("!addwuqi2")) {
            MapleInventoryManipulator.addById(c, 1452044, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwuqi2");//金龙震翅弓
            MapleInventoryManipulator.addById(c, 1462039, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwuqi2");//黄金飞龙弩
            MapleInventoryManipulator.addById(c, 1452019, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwuqi2");//天鹰弓白
            MapleInventoryManipulator.addById(c, 1462015, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwuqi2");//光圣鹞弩白
            MapleInventoryManipulator.addById(c, 1452056, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwuqi2");//鸟弓
            MapleInventoryManipulator.addById(c, 1462049, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwuqi2");//鸟弩

            } else if  (splitted[0].equals("!addwuqi3")) {
            MapleInventoryManipulator.addById(c, 1372031, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwuqi3");//圣贤短杖
            MapleInventoryManipulator.addById(c, 1382037, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwuqi3");//偃月之杖
            MapleInventoryManipulator.addById(c, 1382035, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwuqi3");//冰肌玲珑杖
            MapleInventoryManipulator.addById(c, 1382036, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwuqi3");//黑精灵王杖

            } else if (splitted[0].equals("!addwuqi4")) {
            MapleInventoryManipulator.addById(c, 1332050, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwuqi4");//半月龙鳞裂
            MapleInventoryManipulator.addById(c, 1472051, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwuqi4");//寒木升龙拳
            MapleInventoryManipulator.addById(c, 2070016, (short) 4000, c.getPlayer().getName() + " 使用得到物品 !addwuqi4");//水晶齿轮5组 //(2070006 齿轮镖，2070007 月牙镖)
            MapleInventoryManipulator.addById(c, 1472067, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addwuqi4");//鸟拳

            } else if (splitted[0].equals("!addjuan")) {
            MapleInventoryManipulator.addById(c, 2340000, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addjuan");//祝福卷轴
            MapleInventoryManipulator.addById(c, 2043303, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addjuan");//短剑攻击诅咒卷轴
            MapleInventoryManipulator.addById(c, 2040303, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addjuan");//耳环智力诅咒卷轴
            MapleInventoryManipulator.addById(c, 2040506, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addjuan");//全身铠甲敏捷诅咒卷轴
            MapleInventoryManipulator.addById(c, 2040710, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addjuan");//鞋子跳跃诅咒卷轴
            MapleInventoryManipulator.addById(c, 2040807, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addjuan");//手套攻击诅咒卷轴
            MapleInventoryManipulator.addById(c, 2043003, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addjuan");//单手剑攻击诅咒卷轴
            MapleInventoryManipulator.addById(c, 2043103, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addjuan");//单手斧攻击诅咒卷轴
            MapleInventoryManipulator.addById(c, 2043203, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addjuan");//单手钝器攻击诅咒卷轴
            MapleInventoryManipulator.addById(c, 2043703, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addjuan");//短杖魔力诅咒卷轴
            MapleInventoryManipulator.addById(c, 2043803, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addjuan");//长杖魔力诅咒卷轴
            MapleInventoryManipulator.addById(c, 2044003, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addjuan");//双手剑攻击诅咒卷轴
            MapleInventoryManipulator.addById(c, 2044103, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addjuan");//双手斧攻击诅咒卷轴
            MapleInventoryManipulator.addById(c, 2044303, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addjuan");//枪攻击诅咒卷轴
            MapleInventoryManipulator.addById(c, 2044403, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addjuan");//矛攻击诅咒卷轴
            MapleInventoryManipulator.addById(c, 2044503, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addjuan");//弓攻击诅咒卷轴
            MapleInventoryManipulator.addById(c, 2044603, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addjuan");//弩攻击诅咒卷轴
            MapleInventoryManipulator.addById(c, 2044703, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addjuan");//拳套攻击诅咒卷轴

            } else if (splitted[0].equals("!addyizi")) {
            MapleInventoryManipulator.addById(c, 3010000, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addyizi");//休闲椅
            MapleInventoryManipulator.addById(c, 3010001, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addyizi");//蓝色木椅
            MapleInventoryManipulator.addById(c, 3010003, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addyizi");//红色时尚转椅
            MapleInventoryManipulator.addById(c, 3010007, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addyizi");//粉色海狗靠垫
            MapleInventoryManipulator.addById(c, 3010008, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addyizi");//藍色海狗靠垫
            MapleInventoryManipulator.addById(c, 3010010, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addyizi");//白色海狗靠垫
            MapleInventoryManipulator.addById(c, 3010012, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addyizi");//蓝色高靠背椅
            MapleInventoryManipulator.addById(c, 3010014, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addyizi");//月亮弯弯
            MapleInventoryManipulator.addById(c, 3011000, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addyizi");//钓鱼椅
            MapleInventoryManipulator.addById(c, 3010009, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addyizi");//粉色爱心马桶

            } else if (splitted[0].equals("!addtao1")) {
            MapleInventoryManipulator.addById(c, 1002186, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao1");//透明帽
            MapleInventoryManipulator.addById(c, 1102039, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao1");//透明披风
            MapleInventoryManipulator.addById(c, 1082102, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao1");//透明手套
            MapleInventoryManipulator.addById(c, 1032024, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao1");//透明耳环
            MapleInventoryManipulator.addById(c, 1072153, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao1");//透明鞋
            MapleInventoryManipulator.addById(c, 1050040, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao1");//红色泳裤
            MapleInventoryManipulator.addById(c, 1050041, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao1");//蓝色泳裤
            MapleInventoryManipulator.addById(c, 1051028, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao1");//白色泳衣
            MapleInventoryManipulator.addById(c, 1051029, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao1");//红色泳衣
            MapleInventoryManipulator.addById(c, 1051118, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao1");//粉红可爱游泳服
            MapleInventoryManipulator.addById(c, 1051119, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao1");//蓝色可爱游泳服
            MapleInventoryManipulator.addById(c, 1052078, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao1");//泡泡泡
            MapleInventoryManipulator.addById(c, 1702088, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao1");//专用刷子
            MapleInventoryManipulator.addById(c, 1050100, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao1");//蓝浴巾
            MapleInventoryManipulator.addById(c, 1051098, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao1");//红浴巾

            } else if (splitted[0].equals("!addtao2")) {
            MapleInventoryManipulator.addById(c, 1002479, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao2");//雪人帽
            MapleInventoryManipulator.addById(c, 1052046, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao2");//雪人服
            MapleInventoryManipulator.addById(c, 1702047, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao2");//雪杖
            MapleInventoryManipulator.addById(c, 1702136, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao2");//雪之花
            MapleInventoryManipulator.addById(c, 1032038, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao2");//雪花耳钉
            MapleInventoryManipulator.addById(c, 1092040, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao2");//雪花盾牌
            MapleInventoryManipulator.addById(c, 1702049, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao2");//雪人手套

            } else if (splitted[0].equals("!addtao3")) {
            MapleInventoryManipulator.addById(c, 1000026, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao3");//圣诞男孩子帽
            MapleInventoryManipulator.addById(c, 1001036, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao3");//圣诞女孩子帽
            MapleInventoryManipulator.addById(c, 1002225, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao3");//圣诞节帽
            MapleInventoryManipulator.addById(c, 1702100, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao3");//圣诞钟
            MapleInventoryManipulator.addById(c, 1082101, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao3");//圣诞手套
            MapleInventoryManipulator.addById(c, 1102065, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao3");//圣诞披风
            MapleInventoryManipulator.addById(c, 1072253, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao3");//圣诞鞋
            MapleInventoryManipulator.addById(c, 1002368, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao3");//圣诞鹿帽
            MapleInventoryManipulator.addById(c, 1012011, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao3");//圣诞鹿的鼻子
            MapleInventoryManipulator.addById(c, 1072278, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao3");//圣诞鹿拖鞋

            } else if (splitted[0].equals("!addtao11")) {
            MapleInventoryManipulator.addById(c, 1000030, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao11");//夏其尔的假发(男)
            MapleInventoryManipulator.addById(c, 1001045, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao11");//夏其尔的假发(女)
            MapleInventoryManipulator.addById(c, 1702119, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao11");//夏其尔的剑
            MapleInventoryManipulator.addById(c, 1052091, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao11");//夏其尔的盔甲
            MapleInventoryManipulator.addById(c, 1102096, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao11");//夏其尔的翅膀
            MapleInventoryManipulator.addById(c, 1072281, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao11");//夏其尔的鞋

            } else if (splitted[0].equals("!addtao12")) {
            MapleInventoryManipulator.addById(c, 1000031, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao12");//比耶莫特的假发(男)
            MapleInventoryManipulator.addById(c, 1001046, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao12");//比耶莫特的假发(女)
            MapleInventoryManipulator.addById(c, 1702120, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao12");//比耶莫特的剑
            MapleInventoryManipulator.addById(c, 1052092, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao12");//比耶莫特的盔甲
            MapleInventoryManipulator.addById(c, 1102095, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao12");//比耶莫特的翅膀
            MapleInventoryManipulator.addById(c, 1072282, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao12");//比耶莫特的鞋

            } else if (splitted[0].equals("!addtao13")) {
            MapleInventoryManipulator.addById(c, 1000032, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao13");//亚努斯的假发(男)
            MapleInventoryManipulator.addById(c, 1001047, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao13");//亚努斯的假发(女)
            MapleInventoryManipulator.addById(c, 1702118, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao13");//亚努斯的剑
            MapleInventoryManipulator.addById(c, 1052093, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao13");//亚努斯的盔甲
            MapleInventoryManipulator.addById(c, 1102097, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao13");//亚努斯的翅膀
            MapleInventoryManipulator.addById(c, 1072283, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao13");//亚努斯的鞋

            } else if (splitted[0].equals("!addtao14")) {
            MapleInventoryManipulator.addById(c, 1002599, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao14");//黄金盔
            MapleInventoryManipulator.addById(c, 1052084, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao14");//黄金甲
            MapleInventoryManipulator.addById(c, 1072280, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao14");//黄金靴
            MapleInventoryManipulator.addById(c, 5010044, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao14");//幻影残像
            MapleInventoryManipulator.addById(c, 1702034, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao14");//关公大刀
            MapleInventoryManipulator.addById(c, 1012026, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao14");//关公胡子
            MapleInventoryManipulator.addById(c, 1010001, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao14");//黑胡子

            } else if (splitted[0].equals("!addtao15")) {
            MapleInventoryManipulator.addById(c, 1002607, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao15");//猪八戒帽子
            MapleInventoryManipulator.addById(c, 1052094, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao15");//猪八戒衣服
            MapleInventoryManipulator.addById(c, 1702123, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao15");//猪八戒的钉耙
            MapleInventoryManipulator.addById(c, 1072284, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao15");//猪八戒鞋子
            MapleInventoryManipulator.addById(c, 1002592, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao15");//孙悟空的帽子
            MapleInventoryManipulator.addById(c, 1052083, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao15");//孙悟空的衣服
            MapleInventoryManipulator.addById(c, 1702112, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao15");//孙悟空的棒子

            } else if (splitted[0].equals("!addtao4")) {
            MapleInventoryManipulator.addById(c, 1102098, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao4");//升官发财(棺材)
            MapleInventoryManipulator.addById(c, 1102108, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao4");//恶魔尾巴
            MapleInventoryManipulator.addById(c, 1102058, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao4");//恶魔之翼
            MapleInventoryManipulator.addById(c, 1002576, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao4");//恶魔发夹
            MapleInventoryManipulator.addById(c, 1102006, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao4");//小恶魔翅膀
            MapleInventoryManipulator.addById(c, 1102066, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao4");//邪魔披风
            MapleInventoryManipulator.addById(c, 1102068, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao4");//魔鬼翅膀
            MapleInventoryManipulator.addById(c, 1102075, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao4");//蝙蝠翅膀
            MapleInventoryManipulator.addById(c, 1102150, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao4");//吸血鬼披风
            MapleInventoryManipulator.addById(c, 1102154, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao4");//扎昆爪爪
            MapleInventoryManipulator.addById(c, 5010026, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao4");//蝙蝠效果

            } else if (splitted[0].equals("!addtao5")) {
            MapleInventoryManipulator.addById(c, 1002333, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao5");//天使之圈
            MapleInventoryManipulator.addById(c, 1002367, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao5");//天使之冠
            MapleInventoryManipulator.addById(c, 1002575, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao5");//天使发夹
            MapleInventoryManipulator.addById(c, 1052030, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao5");//天使服
            MapleInventoryManipulator.addById(c, 1702004, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao5");//天使星星棒
            MapleInventoryManipulator.addById(c, 1702142, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao5");//天使魔力棒
            MapleInventoryManipulator.addById(c, 1372021, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao5");//天使的杖
            MapleInventoryManipulator.addById(c, 1102138, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao5");//天使背包
            MapleInventoryManipulator.addById(c, 1102005, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao5");//小天使翅膀
            MapleInventoryManipulator.addById(c, 1102059, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao5");//圣天使之翼
            MapleInventoryManipulator.addById(c, 1102063, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao5");//堕天使之翼

            } else if (splitted[0].equals("!addtao21")) {
            MapleInventoryManipulator.addById(c, 5010023, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao21");//怦怦小红心
            MapleInventoryManipulator.addById(c, 5010041, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao21");//啦啦啦音符
            MapleInventoryManipulator.addById(c, 5010024, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao21");//玩具小鸭家族
            MapleInventoryManipulator.addById(c, 5010025, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao21");//恐怖的幽灵
            MapleInventoryManipulator.addById(c, 5010026, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao21");//蝙蝠效果
            MapleInventoryManipulator.addById(c, 5010027, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao21");//上火
            MapleInventoryManipulator.addById(c, 5010028, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao21");//寒冰斗魂
            MapleInventoryManipulator.addById(c, 5010029, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao21");//红焰斗魂
            MapleInventoryManipulator.addById(c, 5010030, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao21");//雷电斗魂
            MapleInventoryManipulator.addById(c, 5010031, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao21");//我的男友
            MapleInventoryManipulator.addById(c, 5010032, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao21");//我的女友
            MapleInventoryManipulator.addById(c, 5010034, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao21");//圣诞树
            MapleInventoryManipulator.addById(c, 5010035, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao21");//寒冬雪人人型立牌
            MapleInventoryManipulator.addById(c, 5010038, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao21");//瀑布
            MapleInventoryManipulator.addById(c, 5010039, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao21");//聚光
            MapleInventoryManipulator.addById(c, 5010043, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao21");//眼光
            MapleInventoryManipulator.addById(c, 5010044, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao21");//幻影残像
            MapleInventoryManipulator.addById(c, 5010045, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao21");//被雷劈
            MapleInventoryManipulator.addById(c, 5010051, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao21");//圣诞树光效
            MapleInventoryManipulator.addById(c, 5010052, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao21");//圣诞老公公
            MapleInventoryManipulator.addById(c, 5010055, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao21");//太空飞船
            MapleInventoryManipulator.addById(c, 5010056, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao21");//美丽的花草


            } else if (splitted[0].equals("!addtao22")) {
            MapleInventoryManipulator.addById(c, 5120000, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao22");//下雪了
            MapleInventoryManipulator.addById(c, 5120001, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao22");//浪漫樱花
            MapleInventoryManipulator.addById(c, 5120002, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao22");//泡泡飞舞
            MapleInventoryManipulator.addById(c, 5120003, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao22");//雪绒花
            MapleInventoryManipulator.addById(c, 5120004, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao22");//飘礼物
            MapleInventoryManipulator.addById(c, 5120005, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao22");//甜心
            MapleInventoryManipulator.addById(c, 5120006, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao22");//花瓣雨
            MapleInventoryManipulator.addById(c, 5120007, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao22");//棒棒糖
            MapleInventoryManipulator.addById(c, 5120008, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao22");//飘枫叶
            MapleInventoryManipulator.addById(c, 5120009, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao22");//鞭炮
            MapleInventoryManipulator.addById(c, 5120010, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao22");//漂可乐
            MapleInventoryManipulator.addById(c, 5120011, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao22");//飘恐惧
            MapleInventoryManipulator.addById(c, 5120012, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao22");//圣诞袜子
            MapleInventoryManipulator.addById(c, 5120015, (short) 1, c.getPlayer().getName() + " 使用得到物品 !addtao22");//放鞭炮


            } else if (splitted[0].equals("!ringme")) {
            int itemId = Integer.parseInt(splitted[1]);
            if (itemId < 111200 || itemId > 1120000 || (itemId > 1112006 && itemId < 1112800) || itemId == 1112808) {
                mc.dropMessage("无效的物品ID.");
            } else {
                String partnerName = splitted[2];
                int partnerId = MapleCharacter.getIdByName(partnerName, 0);
                int[] ret = MapleRing.createRing(c, itemId, c.getPlayer().getId(), c.getPlayer().getName(), partnerId, partnerName);
                if (ret[0] == -1 || ret[1] == -1) {
                    mc.dropMessage("请确保对方是在线的.");
                }
            }
        } else if (splitted[0].equals("!checkkarma")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            if (splitted.length == 1) {
                mc.dropMessage(victim + "'s Karma level is at: " + victim.getKarma());
                if (victim.getKarma() <= -50) {
                    mc.dropMessage("他的karma非常低. 你可以封掉 他/她");
                } else if (victim.getKarma() >= 50) {
                    mc.dropMessage("他的karma非常搞. 你可以把他/她 设置成一个实习GM.");
                } else {
                    mc.dropMessage("用法: !checkkarma [玩家名字]");
                }
            } else if (splitted.length > 1) {
                if (splitted[2].equals("ban")) {
                    if (victim.getKarma() <= -50) {
                        victim.ban("Low Karma " + victim.getKarma());
                    } else {
                        mc.dropMessage("Too much karma");
                    }
                } else if (splitted[2].equals("intern")) {
                    if (victim.getKarma() >= 50) {
                        victim.setGMLevel(2);
                    } else {
                        mc.dropMessage("Not enough karma");
                    }
                    mc.dropMessage("You have set " + victim + " as an intern.");
                } else {
                    mc.dropMessage("Syntax: !checkkarma [user] [ban/intern]");
                }
            } else {
                mc.dropMessage("Syntax: !checkkarma [user]");
            }
        } else if (splitted[0].equals("!reactorchange")) { 
            MapleMap map = cserv.getMapFactory().getMap(Integer.parseInt(splitted[3]));
            int reactorid = Integer.parseInt(splitted[1]);
            List<MapleMapObject> reactors = map.getMapObjectsInRange(c.getPlayer().getPosition(), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.REACTOR));
            for (MapleMapObject reactorsmo : reactors) {
                MapleReactor reactor = (MapleReactor) reactorsmo;
                if (reactor.getReactorId() == reactorid) {
                    reactor.setState((byte) Integer.parseInt(splitted[2]));
                    map.broadcastMessage(MaplePacketCreator.triggerReactor(reactor, Integer.parseInt(splitted[2])));
                }
            }
        } else if (splitted[0].equals("!givejifen")) {
            cserv.getPlayerStorage().getCharacterByName(splitted[1]).gainDonatorPoints(Integer.parseInt(splitted[2]));
            mc.dropMessage("您给予了 " + splitted[1] + " " + splitted[2] + " 的积分");
        } else if (splitted[0].equals("!setjifen")) {
            cserv.getPlayerStorage().getCharacterByName(splitted[1]).gainDonatorPoints(Integer.parseInt(splitted[2]));
            mc.dropMessage("您设置 " + splitted[1] + "的积分为 " + splitted[2] + ".");
        } else {
            if (c.getPlayer().gmLevel() == 4) {
                mc.dropMessage("您输入的超级GM命令 " + splitted[0] + " 不存在！");
            }
            return false;
        }
        return true;
    }
}