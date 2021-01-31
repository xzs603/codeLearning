/*
Jiongorz. Src. Command.java
JiongorzBBs : http://bbs.jiongorz.com
AdminQQ:307586193
Command
*/
package net.sf.odinms.client.messages;


import java.rmi.RemoteException;
import java.util.*;
import net.sf.odinms.net.MaplePacket;
import net.sf.odinms.net.channel.ChannelServer;
import net.sf.odinms.net.world.remote.*;
import net.sf.odinms.server.*;
import net.sf.odinms.server.life.*;
import net.sf.odinms.server.maps.*;
import net.sf.odinms.tools.*;
import java.net.*;
import java.io.*;
import net.sf.odinms.client.*;
import java.util.HashMap;
import net.sf.odinms.net.world.PlayerCoolDownValueHolder;
import net.sf.odinms.server.maps.FakeCharacter;

public class GMCommand {

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
    public static boolean executeGMCommand(MapleClient c, MessageCallback mc, String line) {
        MapleCharacter player = c.getPlayer();
        ChannelServer cserv = c.getChannelServer();
        String[] splitted = line.split(" ");
        if (splitted[0].equals("!beijing")) {
            player.setGMChat(!player.getGMChat());
            mc.dropMessage("梦の岛提示你：开/关成功.");
        } else if (splitted[0].equals("!god")) {
            player.setGodMode(!player.isGodMode());
            mc.dropMessage("梦の岛提示你：OK啦.你已经无敌啦！");
        } else if (splitted[0].equals("!warpmap")) {
            for (MapleCharacter mch : player.getMap().getCharacters()) {
                if (mch != null) {
                    int mapid = Integer.parseInt(splitted[1]);
                    MapleMap target = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(Integer.parseInt(splitted[1]));
                    mch.changeMap(target, target.getPortal(0));
                }
            }
        } else if (splitted[0].equals("!lock")) {
            int slot = Integer.parseInt(splitted[1]);
            boolean lock = splitted[2].equalsIgnoreCase("true");
            player.lockitem(slot, lock);
        } else if (splitted[0].equals("!event")) {
            if (player.getClient().getChannelServer().eventOn == false) {
                int mapid = getOptionalIntArg(splitted, 1, c.getPlayer().getMapId());
                player.getClient().getChannelServer().eventOn = true;
                player.getClient().getChannelServer().eventMap = mapid;
                try {
                    cserv.getWorldInterface().broadcastMessage(null, MaplePacketCreator.serverNotice(6, c.getChannel(), "[活动] 活动开始在频道 " + c.getChannel() + " 的 " + c.getPlayer().getMap() + "地图!").getBytes());
                } catch (RemoteException e) {
                    cserv.reconnectWorld();
                }
            } else {
                player.getClient().getChannelServer().eventOn = false;
                try {
                    cserv.getWorldInterface().broadcastMessage(null, MaplePacketCreator.serverNotice(6, c.getChannel(), "[の活动] 活动开始在频道 " + c.getChannel() + " 的 " + c.getPlayer().getMap() + "地图!").getBytes());
                } catch (RemoteException e) {
                    cserv.reconnectWorld();
                }
            }
            }else if (splitted[0].equals("!warpid")) {
                                MapleCharacter victim = cserv.getPlayerStorage().getCharacterById(Integer.parseInt(splitted[1]));
				if (victim != null) {
					if (splitted.length == 2) {
						MapleMap target = victim.getMap();
						c.getPlayer().changeMap(target, target.findClosestSpawnpoint(victim.getPosition()));
					} else {
						int mapid = Integer.parseInt(splitted[2]);
						MapleMap target = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(mapid);
						victim.changeMap(target, target.getPortal(0));
					}
				} else {
					try {
						victim = c.getPlayer();
						WorldLocation loc = c.getChannelServer().getWorldInterface().getLocation(splitted[1]);
						if (loc != null) {
							mc.dropMessage("梦の岛提示你：你将跨渠道扭曲。这可能需要几秒钟。");
							MapleMap target = c.getChannelServer().getMapFactory().getMap(loc.map);
							c.getPlayer().cancelAllBuffs();
							String ip = c.getChannelServer().getIP(loc.channel);
							c.getPlayer().getMap().removePlayer(c.getPlayer());
							victim.setMap(target);
							String[] socket = ip.split(":");
							if (c.getPlayer().getTrade() != null) {
								MapleTrade.cancelTrade(c.getPlayer());
							}
							c.getPlayer().saveToDB(true);
							if (c.getPlayer().getCheatTracker() != null)
								c.getPlayer().getCheatTracker().dispose();
							ChannelServer.getInstance(c.getChannel()).removePlayer(c.getPlayer());
							c.updateLoginState(MapleClient.LOGIN_SERVER_TRANSITION);
							try {
								MaplePacket packet = MaplePacketCreator.getChannelChange(InetAddress.getByName(socket[0]), Integer.parseInt(socket[1]));
								c.getSession().write(packet);
							} catch (Exception e) {
								throw new RuntimeException(e);
							}
						} else {
							int map = Integer.parseInt(splitted[1]);
							MapleMap target = cserv.getMapFactory().getMap(map);
							player.changeMap(target, target.getPortal(0));
						}
					} catch (Exception e) {
						mc.dropMessage("Something went wrong " + e.getMessage());
					}
				}
                                } else if (splitted[0].equals("!banid")) {
				if (splitted.length < 3) {
					new ServernoticeMapleClientMessageCallback(2, c).dropMessage(" 梦の岛提示你：用法: !ban 玩家ID 封号理由");
					return true;
				}
				String originalReason = StringUtil.joinStringFrom(splitted, 2);
				String reason = c.getPlayer().getName() + " banned " + splitted[1] + ": " + originalReason;
                                MapleCharacter target = cserv.getPlayerStorage().getCharacterById(Integer.parseInt(splitted[1]));
				if (target != null && target.isGM() == false) {
					String readableTargetName = MapleCharacterUtil.makeMapleReadable(target.getName());
					String ip = target.getClient().getSession().getRemoteAddress().toString().split(":")[0];
					target.ban(reason);
					mc.dropMessage("Banned " + readableTargetName + " reason: " + originalReason);
				} else {
					if (target.isGM() == true) {
                                        mc.dropMessage("你不能封掉GM");
                                       } else if (MapleCharacter.ban(splitted[1], reason, false)) {
                                       mc.dropMessage("封号 " + splitted[1]);
                               } else {
                                        mc.dropMessage("封号失败 " + splitted[1]);
                                      }
                              }

} else if (splitted[0].equals("!idname")) {
mc.dropMessage("-----------------设置ID类命令方便GM对中文名角色的管理----------------- .");
mc.dropMessage("!zaixian                          - 查看在线人物和人物角色ID.");
mc.dropMessage("!pindao                        - 显示人物/ID/所在频道/状态.");
mc.dropMessage("!warpid [角色ID]                   - 用ID传送到指定ID身边.");
mc.dropMessage("!warp [玩家名字]                - 用名字传送到指定玩家身边.");
mc.dropMessage("!warphereid [角色ID]               - 用ID传送指定ID到自己身边.");
mc.dropMessage("!warphere [玩家名字]            - 用名字传送到自己身边.");
mc.dropMessage("!warpallhere             - 所有传送到自己身边.");
mc.dropMessage("!banid [角色ID] [理由]             - 对指定角色ID封号处理.");
mc.dropMessage("!ban[玩家名字] [理由]           - 对指定玩家封号处理.");
            } else if (splitted[0].equals("!gm3")) {
mc.dropMessage("您是游戏管理员，你的等级<GMlevel>:3.您可以使用等级3以下的命令，包括以下命令");
mc.dropMessage("梦の岛提醒你，请勿滥用您的权力.");
mc.dropMessage("注意：使用命令区分字母大小写.");
mc.dropMessage("!shuxing                          - 查看属性类命令.");
mc.dropMessage("!beilv                            - 查看倍率类命令.");
mc.dropMessage("!renwu                            - 查看人物类命令.");
mc.dropMessage("!ziji                             - 查看自身类命令.");
mc.dropMessage("!chuli                            - 查看处理类命令.");
mc.dropMessage("!xiaoxi                           - 查看消息类命令.");
mc.dropMessage("!wupin                            - 查看物品类命令.");
mc.dropMessage("!xianshi                          - 查看显示类命令.");
mc.dropMessage("!qita                             - 查看其它类命令.");
mc.dropMessage("!idname                        - 查看Id式命令.");
mc.dropMessage("!boss                             - 查看BOSS列表.");
mc.dropMessage("!goto                             - 快捷传送地图.");
mc.dropMessage("!daima                            - 查看常用代码");
} else if (splitted[0].equals("!shuxing")) {
mc.dropMessage("■■■■■■■■■■属性类命令■■■■■■■■■■.");
mc.dropMessage("!allocate [属性] [数字]         - 设置指定能力的数量<属性:str/dex/int/luk>.");
mc.dropMessage("!level [数字]                   - 提升指定等级.");
mc.dropMessage("!quanman                         - 设置属性全满<Lv255 F:10000 S/D/I/L:32767 Hp/Mp:30000>.");
mc.dropMessage("!setall [数字]                  - 添加全部能力为知道的数字.");
} else if (splitted[0].equals("!beilv")) {
mc.dropMessage("■■■■■■■■■■倍率类命令■■■■■■■■■■.");
mc.dropMessage("!exprate [倍数]              - 设置游戏经验倍数.");
mc.dropMessage("!mesorate [倍数]             - 设置游戏金钱倍数.");
mc.dropMessage("!droprate [倍数]             - 设置游戏掉宝倍数.");
mc.dropMessage("!bossdroprate [倍数]         - 设置游戏BOSS掉宝倍数.");
mc.dropMessage("!petexprate [倍数]           - 设置游戏宠物经验倍数.");
mc.dropMessage("!shopmesorate [倍数]       - 设置商店卖出物品获得金币的倍数.");
} else if (splitted[0].equals("!renwu")) {
mc.dropMessage("■■■■■■■■■■人物类命令■■■■■■■■■■.");
mc.dropMessage("!dc [玩家名字]                     - 断开指定玩家的游戏连接.");
mc.dropMessage("!dianjuan [玩家名字] [数字]        - 给指定玩家指定数量的点卷.");
mc.dropMessage("!healperson [玩家名字]             - 给指定玩家满上HP/MP.");
mc.dropMessage("!healmap                       - 给此地图所有玩家满上HP/MP.");
mc.dropMessage("!hurt [玩家名字]                   - 设置指定玩家HP为1.");
mc.dropMessage("!jobperson [玩家名字] [职业ID]    - 改变指定玩家职业为指定职业ID.");
mc.dropMessage("!kill [玩家名字]                   - 干掉指定玩家.");
mc.dropMessage("!killeveryone                  - 干掉服务器上所有玩家.");
mc.dropMessage("!killmap                       - 干掉此地图上的玩家.");
mc.dropMessage("!levelperson [玩家名字] [数字]    - 设置指定玩家为指定等级.");
mc.dropMessage("!mesoperson [玩家名字] [数字]     - 给予指定玩家指定数字的金币.");
mc.dropMessage("!rebornperson [玩家名字] [数字]     - 设置指定玩家已经转生的次数.");
mc.dropMessage("!slap [玩家名字] [伤害值]        - 给予指定玩家指定伤害值的伤害.");
mc.dropMessage("!warpid [角色ID]                   - 用ID传送到指定ID身边.");
mc.dropMessage("!warp [玩家名字]                - 用名字传送到指定玩家身边.");
mc.dropMessage("!warphereid [角色ID]               - 用ID传送指定ID到自己身边.");
mc.dropMessage("!warphere [玩家名字]            - 用名字传送到自己身边.");
mc.dropMessage("!warpallhere             - 所有传送到自己身边.");
} else if (splitted[0].equals("!ziji")) {
mc.dropMessage("■■■■■■■■■■自己的命令■■■■■■■■■■.");
mc.dropMessage("!beijing                      - 开启关闭聊天背景.");
mc.dropMessage("!fuzhi                      - 复制一个自己的影子(造成地图问题==禁止).");
mc.dropMessage("!heal                          - 满上自己的HP/MP.");
mc.dropMessage("!job [职业ID]                   - 改变你的职业.");
mc.dropMessage("!levelup                       - 提升等级，每次只能升高一级.");
mc.dropMessage("!quanman                        - 设置属性全满<Lv255 F:10000 S/D/I/L:32767 Hp/Mp:30000>.");
mc.dropMessage("!mxb [数字]                - 添加指定数量的金钱到你身上.");
mc.dropMessage("!pvpdeaths [数字]            - 设置PK死亡次数.");
mc.dropMessage("!pvpkills  [数字]            - 设置PK杀敌次数.");
mc.dropMessage("!skill [技能ID] [等级]         - 提升指定技能的技能等级.");
mc.dropMessage("!map [地图ID]                 - 传送自己到指定的地图.");
} else if (splitted[0].equals("!chuli")) {
mc.dropMessage("■■■■■■■■■■处理类命令■■■■■■■■■■.");
mc.dropMessage("!ban [玩家名字] [封号原因]           - 对指定玩家封号处理.");
mc.dropMessage("!jy [玩家名字]                     - 把指定玩家送到监狱.");
mc.dropMessage("!qxjy [玩家名字]                   - 取消指定玩家坐牢.");
} else if (splitted[0].equals("!xiaoxi")) {
mc.dropMessage("■■■■■■■■■■消息命令■■■■■■■■■■.");
mc.dropMessage("!notice [信息]                 - 发布公告.");
mc.dropMessage("!say [信息]                 - 发布一个自己的信息给整个服务器.");
mc.dropMessage("!servermessage [信息]       - 改变顶部黄色字体信息.");
mc.dropMessage("!gmsay [D盘gmsay.txt所需发布公告的行数]- 发布系统公告.");
mc.dropMessage("用法:!gmsay 1[发布gmsay.txt文本的第一行信息].");
} else if (splitted[0].equals("!wupin")) {
mc.dropMessage("■■■■■■■■■■物品命令■■■■■■■■■■.");
mc.dropMessage("!drop [物品ID] [数量]            - 丢出指定物品ID指定的数量件数.");
mc.dropMessage("!item [物品id] [数量]            - 刷物品/或刷指定物品的件数到自己的物品栏上.");
} else if (splitted[0].equals("!xianshi")) {
mc.dropMessage("■■■■■■■■■■显示命令■■■■■■■■■■.");
mc.dropMessage("!lianjie                    - 显示服务器的连接数.");
mc.dropMessage("!gm                       - 显示在线GM.");
mc.dropMessage("!mapid                       - 显示自己所在的地图的ID.");
mc.dropMessage("!whosthere                     - 显示在此地图的所有玩家.");
mc.dropMessage("!zaixian                        - 显示在线玩家和玩家ID.");
mc.dropMessage("!zaixian1                       - 显示在线玩家.");
} else if (splitted[0].equals("!qita")) {
mc.dropMessage("■■■■■■■■■■其它命令■■■■■■■■■■.");
mc.dropMessage("!cancelBuffs [玩家名字]         - 取消指定玩家的辅助技能.");
mc.dropMessage("!charinfo [玩家名字]            - 取得指定玩家的所有信息.");
mc.dropMessage("!qc                           - 清除地图上所有物品.");
mc.dropMessage("!clock [数字]                  - 创建一个时钟和时间限制.");
mc.dropMessage("!sha                       - 干掉你所在地图上的所有怪物.");
mc.dropMessage("!wz                           - 在当前地图显示您的位置.");
mc.dropMessage("!openshop [商店ID]             - 打开指定商店.");
mc.dropMessage("!saveall                       - 保存服务器数据.");
mc.dropMessage("!zhaohuan [怪物ID] [数量]          - 召唤指定数量的怪物.");
} else if (splitted[0].equals("!boss")) {
mc.dropMessage("■■■■■■■■■■BOSS/怪物的命令■■■■■■■■■■.");
mc.dropMessage("!anego                         - 召唤大巴掌女老板.");
mc.dropMessage("!balrog                        - 召唤蝙蝠魔群.");
mc.dropMessage("!blackcrow                     - 召唤鸟乌鸦.");
mc.dropMessage("!bob                           - 召唤小蜗牛群.");
mc.dropMessage("!bird                          - 召唤冰火烈焰.");
mc.dropMessage("!centipede                     - 召唤蜈蚣王.");
mc.dropMessage("!clone                         - 召唤转职教官.");
mc.dropMessage("!coke                          - 召唤可乐怪物群.");
mc.dropMessage("!cornian                       - 召唤蜥蜴怪物群.");
mc.dropMessage("!dragon                        - 召唤飞龙怪物群.");
mc.dropMessage("!ergoth                        - 召唤死灵法神.");
mc.dropMessage("!franken                       - 召唤四不像机器人.");
mc.dropMessage("!horseman                      - 召唤鬼马.");
mc.dropMessage("!leafreboss                    - 召唤肥龙/天鹰.");
mc.dropMessage("!loki                          - 召唤箱子.");
mc.dropMessage("!ludimini                      - 召唤门神群.");
mc.dropMessage("!mushmom                       - 召唤蘑菇王群.");
mc.dropMessage("!nx                            - 召唤黄水灵群.");
mc.dropMessage("!pap                           - 召唤闹钟.");
mc.dropMessage("!papapixie                     - 召唤星精灵老大.");
mc.dropMessage("!pianus                        - 召唤鱼王.");
mc.dropMessage("!pirate                        - 召唤海贼王.");
mc.dropMessage("!rock                          - 召唤无敌巨石人.");
mc.dropMessage("!shark                         - 召唤鲨鱼群.");
mc.dropMessage("!snackbar                      - 召唤黑轮王.");
mc.dropMessage("!theboss                       - 召唤老不死.");
} else if (splitted[0].equals("!daima")) {
mc.dropMessage("梦の岛");
mc.dropMessage("--------------职业代码--------------");
mc.dropMessage("0 新手，100 战士，200 法师，300 射手，400 飞侠，500 海盗 || 900 管理员，910 超级管理员 ");
mc.dropMessage("110-112 英雄，120-122 圣骑，130-132 黑骑 || 210-212 火毒，220-222 冰雷，230-232 主教 ");
mc.dropMessage("310-312 射手，320-322 游侠 || 410-412 隐士，420-422 侠盗 || 510-512 冲锋，520-522 船长");
mc.dropMessage("--------------地图代码--------------");
mc.dropMessage("10000 新手训练(老版)，2 新手训练(新版)");
mc.dropMessage("209000000 幸福村，209000001-15 幸福村隐藏地图");
mc.dropMessage("180000000 GM地图，200000301 家族中心，109030001 封号地图");
mc.dropMessage("--------------椅子代码--------------");
mc.dropMessage("3010009 粉色爱心马桶，3010007 粉色海狗靠垫，3010012  蓝色高靠背椅，3010003 红色時尚转椅");
mc.dropMessage("3011000 钓鱼椅，3010013 悠长假期，3010014 月亮弯弯");
mc.dropMessage("--------------装备代码--------------");
mc.dropMessage("1002140 维泽特帽，1322013 维泽特提包，1042003 维泽特西装，1062007 维泽特西裤");
mc.dropMessage("1002562 新手帽子，1052081 新手衣服，1082230 白色闪光手套");
mc.dropMessage("1122000 黑龙项链，  2041200 暗黑龙王石(给黑龙项链升级)");
mc.dropMessage("1002357 扎昆头盔1，  1002390 扎昆头盔2，1002430 扎昆头盔3");
mc.dropMessage("1032053 四叶草耳环，1032038 雪花耳钉，1032029 925银耳环，1102142 火焰披风");
mc.dropMessage("1092033 四叶草盾牌，1092044 爱慢速变化球，1092031 瓢虫盾，1092008 锅盖");
mc.dropMessage("1092021 光子盾，  1092029 电磁光盾，  1092045 冒险岛魔法师盾牌，");
mc.dropMessage("1092018-20 飞侠盾牌，1092049 热情剑盾，1092050 冷艳剑盾，1092047 冒险岛飞侠盾牌");
mc.dropMessage("1032019 水仙耳环，1032042 冒险岛耳环，1032030 勇气耳环，1032031 坚毅耳环");
mc.dropMessage("--------------玩具武器代码--------------");
mc.dropMessage("1302063 燃烧的火焰刀，1402044 南瓜灯笼，1422036 玩具匠人的锤子，1382016 香菇，1402029 鬼刺狼牙棒");
mc.dropMessage("1442018 冻冻鱼，1432039 钓鱼竿，1442023 黑拖把，1302013 红鞭子，1432009 木精灵枪，");
mc.dropMessage("1302058 冒险岛伞，1302066 枫叶庆典旗，1302049 光线鞭子，1322051 七夕，1372017 领路灯");
mc.dropMessage("1322026 彩虹游泳圈，1332020 太极扇，1332053 野外烧烤串，1332054 闪电飞刀，1402017 船长佩剑");
mc.dropMessage("1442046 超级雪板，1442061 仙人掌之矛，1432046 圣诞枪，1302080 闪光彩灯鞭");
mc.dropMessage("1452054-6 鸟弓，1462047-9 鸟弩，1472065-7 鸟拳，1442047-50 玫瑰");
mc.dropMessage("--------------现金武器代码--------------");
mc.dropMessage("1702165 同班男生，1702169 同班女生，1702174 蝴蝶扇，1702114 荷叶青蛙，1702140 花");
mc.dropMessage("1702179 云彩9枕头，1702000 双刃激光剑(短剑)，1702034 关公大刀，1702155 星星");
mc.dropMessage("1702081 紫色燃烧的剑，1702151 绿色的杖，1702025 蓝色的竖琴(弓)，1702161 狗(拳套)");
mc.dropMessage("1082229 飘飘手套，1082227 海盗手套，1102159 猴子气球");
mc.dropMessage("--------------职业武器代码--------------");
mc.dropMessage("1302056 一刀两段(单手剑)，1402037 龙背刃，1402005 斩魔刀，1402035 斩天刀");
mc.dropMessage("1312015 战魂之斧，  1312031 狂龙怒斩斧，1412010 项羽之斧    1322052 狂龙地锤");
mc.dropMessage("1432011 寒冰破魔枪，1432030 红莲落神枪，1432038 盘龙七冲枪，1442045 血龙神斧");
mc.dropMessage("1452044 金龙震翅弓，1462039 黄金飞龙弩，1452019 天鹰弓白，  1462015 光圣鹞弩白");
mc.dropMessage("1372031 圣贤短杖，1382037 偃月之杖，1382035 冰肌玲珑杖，1382036 黑精灵王杖");
mc.dropMessage("1332050 半月龙鳞裂，1472051 寒木升龙拳，2070006 齿轮镖，2070007 月牙镖，2070016 水晶齿轮");
mc.dropMessage("--------------物 品 代 码--------------");
mc.dropMessage("1912000 皮鞍子，1902000-2 骑宠，!skill 1004 0 骑宠技能, 1932000 海盗船(MFC060版)");
mc.dropMessage("4001017 火眼，4031179 D片，4006000 魔法石，4006001 召回石，4001129 纪念币");
mc.dropMessage("4030000-09 换取棋盘的材料, 4030010-16 记忆大考验, 4080000-11 五子棋");
mc.dropMessage("2000005 超级药水，2050004 万能药水，2022199 全恢复，2050006 恢复视野药水");
mc.dropMessage("5300000 蘑菇变身卡，5300001 漂漂猪变身卡，5300002 外星人变身卡");
mc.dropMessage("5390000 炽热情景喇叭，5390001 绚烂情景喇叭，5390002 爱心情景喇叭");
mc.dropMessage("VIP兑换道具代码：4001120(普通), 4001121(高级)");
mc.dropMessage("--------------060新代码--------------");
mc.dropMessage("1382060 红杖，1432048 沙僧，1442068 关羽，1302088-94 玩具蝙蝠 || 1022058 狸猫面具");
mc.dropMessage("1482023 指节，1492013 手枪，2330005 子弹，2331000 火方块，2332000 冰方块");
mc.dropMessage("2210000-03 && 2210005-12 变身药水");
mc.dropMessage("1702180 黑球（83 黄 84白 85橙 87彩 88紫）");
mc.dropMessage("============我很懒！就几个怪！后面几个版本出=============");
mc.dropMessage("100100 蜗牛 100101 蓝蜗牛 1110100 绿蘑菇 1110101 黑木妖 1120100 三眼章鱼 1130100 斧木妖 1140100 古木妖 120100 蘑菇仔 ");
        } else if (splitted[0].equals("!zaixian")) {
        mc.dropMessage("在の玩家:");
	Collection<MapleCharacter> chrs = c.getChannelServer().getInstance(c.getChannel()).getPlayerStorage().getAllCharacters();
	for (MapleCharacter chr : chrs) {
	mc.dropMessage("人名字: "+chr.getName()+" 人ID: "+chr.getId());
	}

            } else if (splitted[0].equals("!fuzhi")) {
            mc.dropMessage("梦の岛提示你：被胜胜禁用！Sorry~啦啦啦");
            } else if (splitted[0].equals("!goto")) {
if (splitted.length < 2){
    mc.dropMessage("用法: !goto <地图名字>");
     mc.dropMessage("指令用法: !goto <地图名字> <玩家名字>, 将指定玩家传送到指定地图(不输名字则是传送自己):");
    mc.dropMessage("gmmap<GM地图>, southperry<南港>, amherst<彩虹村>, henesys<射手村>, ellinia<魔法密林>,");
    mc.dropMessage("perion<勇士部落>, kerning<废气都市>, lith<明珠港>, sleepywood<林中之城>, florina<黄金海滩>,");
   mc.dropMessage("orbis<天空之城>, happy<幸福村>, elnath<冰封雪域>, ludi<玩具城>, aqua<海底世界>,");
   mc.dropMessage("leafre<神木村>, mulung<武陵村>, herb<百草堂>, omega<地球防卫总部>, korean<童话村>,");
   mc.dropMessage("nlc<新叶城>, excavation<考古发掘地>, pianus<鱼王>, horntail<暗黑龙王>, mushmom<蘑茹王>, ");
                       mc.dropMessage("griffey<胖凤>, manon<肥龙>, horseman<火马>, balrog<蝙蝠魔>, zakum<扎昆>, papu<闹钟>,");
                       mc.dropMessage("showa<昭和村>, guild<家族>, shrine<樱花村>, fm<自由市场>, skelegon<骨龙>, pvp<PK地图>");
}else{
    HashMap<String, Integer> gotomaps = new HashMap<String, Integer>();
    gotomaps.put("gmmap", 180000000);
    gotomaps.put("southperry", 60000);
    gotomaps.put("amherst", 1010000);
    gotomaps.put("henesys", 100000000);
    gotomaps.put("ellinia", 101000000);
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
    gotomaps.put("horntail", 240060200);
    gotomaps.put("mushmom", 100000005);
    gotomaps.put("griffey", 240020101);
    gotomaps.put("manon", 240020401);
    gotomaps.put("horseman", 682000001);
    gotomaps.put("balrog", 105090900);
    gotomaps.put("zakum", 280030000);
    gotomaps.put("papu", 220080001);
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
        } else if (splitted[0].equals("!resetcooldowns")) {
            for (PlayerCoolDownValueHolder i : player.getAllCooldowns()) {
                player.removeCooldown(i.skillId);
            }
            mc.dropMessage("梦の岛提示你：成功.");
        } else if (splitted[0].equals("!drop") || splitted[0].equals("!droprandomstatitem") || splitted[0].equals("!item")) {
            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            int itemId = Integer.parseInt(splitted[1]);
            short quantity = (short) getOptionalIntArg(splitted, 2, 1);
            IItem toDrop;
            if (splitted[0].equals("!drop")) {
                if (ii.getInventoryType(itemId) == MapleInventoryType.EQUIP) {
                    toDrop = ii.getEquipById(itemId);
                } else {
                    toDrop = new Item(itemId, (byte) 0, quantity);
                }
                toDrop.setOwner(player.getName());
                player.getMap().spawnItemDrop(player, player, toDrop, player.getPosition(), true, true);
            } else if (splitted[0].equals("!item")) {
                if (itemId >= 5000000 && itemId <= 5000045) {
                    MaplePet.createPet(itemId);
                    return true;
                }
                MapleInventoryManipulator.addById(c, itemId, quantity, "", player.getName());
            } else {
                if (!MapleItemInformationProvider.getInstance().getInventoryType(itemId).equals(MapleInventoryType.EQUIP)) {
                    mc.dropMessage("梦の岛提示你：该命令命令只能用于物品.");
                } else {
                    toDrop = MapleItemInformationProvider.getInstance().randomizeStats((Equip) MapleItemInformationProvider.getInstance().getEquipById(itemId));
                    player.getMap().spawnItemDrop(player, player, toDrop, player.getPosition(), true, true);
                }
            }
        } else if (splitted[0].equals("!jy")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            int mapid = 200090300;
            if (splitted.length > 2 && splitted[1].equals("2")) {
                mapid = 980000404;
                victim = cserv.getPlayerStorage().getCharacterByName(splitted[2]);
            }
            if (victim != null) {
                MapleMap target = cserv.getMapFactory().getMap(mapid);
                victim.changeMap(target, target.getPortal(0));
                mc.dropMessage(victim.getName() + " 被送到监狱!");
            } else {
                mc.dropMessage(splitted[1] + " 没找到!");
            }
        } else if (splitted[0].equals("!qxjy")) {
            MapleMap target = cserv.getMapFactory().getMap(100000000);
            cserv.getPlayerStorage().getCharacterByName(splitted[1]).changeMap(target, target.getPortal(0));
        } else if (splitted[0].equals("!quanman")) {
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
        } else if (splitted[0].equals("!mxb")) {
            player.gainMeso(Integer.parseInt(splitted[1]), true);
        } else if (splitted[0].equals("!job")) {
            player.changeJob(MapleJob.getById(Integer.parseInt(splitted[1])));
        } else if (splitted[0].equals("!allocate")) {
            int up = Integer.parseInt(splitted[2]);
            if (splitted[1].equals("str")) {
                player.setStr(player.getStr() + up);
                player.updateSingleStat(MapleStat.STR, player.getStr());
            } else if (splitted[1].equals("dex")) {
                player.setDex(player.getDex() + up);
                player.updateSingleStat(MapleStat.DEX, player.getDex());
            } else if (splitted[1].equals("int")) {
                player.setInt(player.getInt() + up);
                player.updateSingleStat(MapleStat.INT, player.getInt());
            } else if (splitted[1].equals("luk")) {
                player.setLuk(player.getLuk() + up);
                player.updateSingleStat(MapleStat.LUK, player.getLuk());
            } else if (splitted[1].equals("hp")) {
                player.setMaxHp(player.getMaxHp() + up);
                player.updateSingleStat(MapleStat.MAXHP, player.getMaxHp());
            } else if (splitted[1].equals("mp")) {
                player.setMaxMp(player.getMaxMp() + up);
                player.updateSingleStat(MapleStat.MAXMP, player.getMaxMp());
            } else {
                mc.dropMessage(splitted[1] + " 不是一个有效的属性.");
            }
        } else if (splitted[0].equals("!healmap")) {
            for (MapleCharacter mch : player.getMap().getCharacters()) {
                if (mch != null) {
                    mch.setHp(mch.getMaxHp());
                    mch.updateSingleStat(MapleStat.HP, mch.getMaxHp());
                    mch.setMp(mch.getMaxMp());
                    mch.updateSingleStat(MapleStat.MP, mch.getMaxMp());
                }
            }
        } else if (splitted[0].equals("!song")) {
            player.getMap().broadcastMessage(MaplePacketCreator.musicChange(splitted[1]));
        } else if (splitted[0].equals("!cancelbuffs")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            if (victim != null) {
                victim.cancelAllBuffs();
            }
        } else if (splitted[0].equals("!wz")) {
            mc.dropMessage("您的位置: x坐标 = " + player.getPosition().x + ", y坐标 = " + player.getPosition().y + ", fh = " + player.getMap().getFootholds().findBelow(player.getPosition()).getId());
        } else if (splitted[0].equals("!coke")) {
            int[] ids = {9500144, 9500151, 9500152, 9500153, 9500154, 9500143, 9500145, 9500149, 9500147};
            for (int a : ids) {
                player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(a), player.getPosition());
            }
        } else if (splitted[0].equals("!pap")) {
            MapleMonster mob0 = MapleLifeFactory.getMonster(8500001);
            player.getMap().spawnMonsterOnGroudBelow(mob0, player.getPosition());
        } else if (splitted[0].equals("!ergoth")) {
            MapleMonster mob0 = MapleLifeFactory.getMonster(9300028);
            player.getMap().spawnMonsterOnGroudBelow(mob0, player.getPosition());
        } else if (splitted[0].equals("!ludimini")) {
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(8160000), player.getPosition());
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(8170000), c.getPlayer().getPosition());
        } else if (splitted[0].equals("!cornian")) {
            c.getPlayer().getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(8150201), c.getPlayer().getPosition());
            c.getPlayer().getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(8150200), c.getPlayer().getPosition());
        } else if (splitted[0].equals("!balrog")) {
            int[] ids = {8130100, 8150000, 9400536};
            for (int a : ids) {
                c.getPlayer().getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(a), c.getPlayer().getPosition());
            }
        } else if (splitted[0].equals("!mushmom")) {
            int[] ids = {6130101, 6300005, 9400205};
            for (int a : ids) {
                c.getPlayer().getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(a), c.getPlayer().getPosition());
            }
        } else if (splitted[0].equals("!dragon")) {
            int[] ids = {8150300, 8150301, 8150302};
            for (int a : ids) {
                c.getPlayer().getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(a), c.getPlayer().getPosition());
            }
        } else if (splitted[0].equals("!pirate")) {
            int[] ids = {9300119, 9300107, 9300105, 9300106};
            for (int a : ids) {
                c.getPlayer().getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(a), c.getPlayer().getPosition());
            }
        } else if (splitted[0].equals("!clone")) {
            int[] ids = {9001002, 9001000, 9001003, 9001001};
            for (int a : ids) {
                c.getPlayer().getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(a), c.getPlayer().getPosition());
            }
        } else if (splitted[0].equals("!levelup")) {
            player.setLevel(Integer.parseInt(splitted[1]) - 1);
            player.gainExp(-player.getExp(), false, false);
        } else if (splitted[0].equals("!fame")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            int fame = Integer.parseInt(splitted[2]);
            victim.setFame(fame);
            victim.updateSingleStat(MapleStat.FAME, fame);
        } else if (splitted[0].equals("!killmap")) {
            for (MapleCharacter mch : c.getPlayer().getMap().getCharacters()) {
                if (mch != null) {
                    mch.setHp(0);
                    mch.updateSingleStat(MapleStat.HP, 0);
                }
            }
        } else if (splitted[0].equals("!killeveryone")) {
            for (MapleCharacter mch : cserv.getPlayerStorage().getAllCharacters()) {
                if (mch != null) {
                    mch.setHp(0);
                    mch.updateSingleStat(MapleStat.HP, 0);
                }
            }

        } else if (splitted[0].equals("!giveItemBuff")) {
        cserv.getPlayerStorage().getCharacterByName(splitted[1]).setItemEffect(Integer.parseInt(splitted[2]));
        } else if (splitted[0].equals("!str") || splitted[0].equals("!dex") || splitted[0].equals("!int") || splitted[0].equals("!luk")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            int up = Integer.parseInt(splitted[2]);
            if (splitted[0].equals("!str")) {
                victim.setStr(up);
                victim.updateSingleStat(MapleStat.STR, victim.getStr());
            } else if (splitted[0].equals("!dex")) {
                victim.setDex(up);
                victim.updateSingleStat(MapleStat.DEX, victim.getDex());
            } else if (splitted[0].equals("!luk")) {
                victim.setLuk(up);
                victim.updateSingleStat(MapleStat.LUK, victim.getLuk());
            } else {
                victim.setInt(up);
                victim.updateSingleStat(MapleStat.INT, victim.getInt());
            }
        } else if (splitted[0].equals("!lolhaha")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            mc.dropMessage("你转换了性别 " + victim + ".");
            victim.setGender(1 - victim.getGender());
        } else if (splitted[0].equals("!anego")) {
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9400121), player.getPosition());
        } else if (splitted[0].equals("!theboss")) {
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9400300), player.getPosition());
        } else if (splitted[0].equals("!snackbar")) {
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9500179), player.getPosition());
        } else if (splitted[0].equals("!papapixie")) {
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9300039), player.getPosition());
        } else if (splitted[0].equals("!horseman")) {
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9400549), player.getPosition());
        } else if (splitted[0].equals("!nx")) {
            for (int x = 0; x < 10; x++) {
                player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9400202), player.getPosition());
            }
        } else if (splitted[0].equals("!loki")) {
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9400567), player.getPosition());
        } else if (splitted[0].equals("!blackcrow")) {
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9400014), player.getPosition());
        } else if (splitted[0].equals("!leafreboss")) {
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(8180000), player.getPosition());
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(8180001), player.getPosition());
        } else if (splitted[0].equals("!shark")) {
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(8150101), player.getPosition());
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(8150100), player.getPosition());
        } else if (splitted[0].equals("!franken")) {
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9300139), player.getPosition());
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9300140), player.getPosition());
        } else if (splitted[0].equals("!bird")) {
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9300090), player.getPosition());
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9300089), player.getPosition());
        } else if (splitted[0].equals("!pianus")) {
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(8510000), player.getPosition());
        } else if (splitted[0].equals("!rock")) {
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9300091), player.getPosition());
        } else if (splitted[0].equals("!centipede")) {
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9500177), player.getPosition());
        } else if (splitted[0].equals("!slap")) {
            int loss = Integer.parseInt(splitted[2]);
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            victim.setHp(victim.getHp() - loss);
            victim.updateSingleStat(MapleStat.HP, victim.getHp() - loss);
            mc.dropMessage("You slapped " + victim.getName() + ".");
        } else if (splitted[0].equals("!exprate")) {
            int exp = Integer.parseInt(splitted[1]);
            cserv.setExpRate(exp);
            MaplePacket packet = MaplePacketCreator.serverNotice(6, "梦の岛提示你：现在的经验被调整为" + exp + "x.");
            ChannelServer.getInstance(c.getChannel()).broadcastPacket(packet);
        } else if (splitted[0].equals("!petexprate")) {
            int exp = Integer.parseInt(splitted[1]);
            cserv.setPetExpRate(exp);
            MaplePacket packet = MaplePacketCreator.serverNotice(6, "梦の岛提示你：宠物经验倍调整为" + exp + "x.");
            ChannelServer.getInstance(c.getChannel()).broadcastPacket(packet);
        } else if (splitted[0].equals("!mountexprate")) {
            int exp = Integer.parseInt(splitted[1]);
            cserv.setMountRate(exp);
            MaplePacket packet = MaplePacketCreator.serverNotice(6, "梦の岛提示你：Mount经验倍调整为" + exp + "x.");
            ChannelServer.getInstance(c.getChannel()).broadcastPacket(packet);
        } else if (splitted[0].equals("!mesorate")) {
            int meso = Integer.parseInt(splitted[1]);
            cserv.setMesoRate(meso);
            MaplePacket packet = MaplePacketCreator.serverNotice(6, "梦の岛提示你：怪物掉钱被调整为" + meso + "x.");
            ChannelServer.getInstance(c.getChannel()).broadcastPacket(packet);
        } else if (splitted[0].equals("!droprate")) {
            int drop = Integer.parseInt(splitted[1]);
            cserv.setDropRate(drop);
            MaplePacket packet = MaplePacketCreator.serverNotice(6, "梦の岛提示你：怪物掉物被调整为" + drop + "x.");
            ChannelServer.getInstance(c.getChannel()).broadcastPacket(packet);
        } else if (splitted[0].equals("!bossdroprate")) {
            int bossdrop = Integer.parseInt(splitted[1]);
            cserv.setBossDropRate(bossdrop);
            MaplePacket packet = MaplePacketCreator.serverNotice(6, "梦の岛提示你：Boss掉宝被调整为" + bossdrop + "x.");
            ChannelServer.getInstance(c.getChannel()).broadcastPacket(packet);
        } else if (splitted[0].equals("!shopmesorate")) {
            int rate = Integer.parseInt(splitted[1]);
            cserv.setShopMesoRate(rate);
            MaplePacket packet = MaplePacketCreator.serverNotice(6, "梦の岛提示你：商店卖物品获得钱数倍率被调整为" + rate + "x.");
            ChannelServer.getInstance(c.getChannel()).broadcastPacket(packet);
        } else if (splitted[0].equals("!warpallhere")) {
            for (MapleCharacter mch : cserv.getPlayerStorage().getAllCharacters()) {
                if (mch.getMapId() != player.getMapId()) {
                    mch.changeMap(player.getMap(), player.getPosition());
                }
            }
        } else if (splitted[0].equals("!hurt")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            victim.setHp(1);
            victim.updateSingleStat(MapleStat.HP, 1);
        } else if (splitted[0].equals("!bob")) {
            MapleMonster mob0 = MapleLifeFactory.getMonster(9400551);
            player.getMap().spawnMonsterOnGroudBelow(mob0, player.getPosition());
            player.getMap().broadcastMessage(MaplePacketCreator.serverNotice(0, "梦の岛提示你：可爱的小蜗牛回来了!"));
        } else if (splitted[0].equals("!healperson")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            victim.setHp(victim.getMaxHp());
            victim.updateSingleStat(MapleStat.HP, victim.getMaxHp());
            victim.setMp(victim.getMaxMp());
            victim.updateSingleStat(MapleStat.MP, victim.getMaxMp());
        } else if (splitted[0].equals("!lolcastle")) {
            MapleMap target = cserv.getEventSM().getEventManager("lolcastle").getInstance("lolcastle" + splitted[1]).getMapFactory().getMap(990000300, false, false);
            player.changeMap(target, target.getPortal(0));
      
        } else if (splitted[0].equals("!saveall")) {
            for (ChannelServer chan : ChannelServer.getAllInstances()) {
                for (MapleCharacter chr : chan.getPlayerStorage().getAllCharacters()) {
                    chr.saveToDB(true);
                }
            }
            mc.dropMessage("保存数据成功.");
        } else if (splitted[0].equals("!job")) {
            player.changeJob(MapleJob.getById(Integer.parseInt(splitted[1])));
        } else if (splitted[0].equals("!clock")) {
            player.getMap().broadcastMessage(MaplePacketCreator.getClock(getOptionalIntArg(splitted, 1, 60)));
        } else if (splitted[0].equals("!qc")) {
          MapleMap map = player.getMap();
            List<MapleMapObject> items = map.getMapObjectsInRange(player.getPosition(), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.ITEM));
            for (MapleMapObject i : items) {
                map.removeMapObject(i);
                map.broadcastMessage(MaplePacketCreator.removeItemFromMap(i.getObjectId(), 0, player.getId()));
            }
            mc.dropMessage("你清除了 " + items.size() + " 件地上的物品.");
       } else if (splitted[0].equals("!levelperson")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            victim.setLevel(Integer.parseInt(splitted[2]) - 1);
            victim.levelUp();
            victim.setExp(0);
            victim.updateSingleStat(MapleStat.EXP, 0);
        } else if (splitted[0].equalsIgnoreCase("!showguanwuID")) {
            MapleMap map = player.getMap();
            double range = Double.POSITIVE_INFINITY;
            List<MapleMapObject> monsters = map.getMapObjectsInRange(player.getPosition(), range, Arrays.asList(MapleMapObjectType.MONSTER));
            for (MapleMapObject monstermo : monsters) {
                MapleMonster monster = (MapleMonster) monstermo;
                String alive = "false";
                if (monster.isAlive()) {
                    alive = "true";
                }
                mc.dropMessage("怪物名字：" + monster.getName() + " ID=" + monster.getId() + " 活着：" + monster.isAlive());
            }
        } else if (splitted[0].equals("!openshop")) {
            MapleShopFactory.getInstance().getShop(Integer.parseInt(splitted[1])).sendShop(c);
        } else if (splitted[0].equals("!level")) {
            player.setExp(0);
            player.setLevel(Integer.parseInt(splitted[1]) - 1);
            player.levelUp();
            player.setExp(0);
            player.updateSingleStat(MapleStat.EXP, 0);
        } else if (splitted[0].equals("!reborn")) {
            player.setReborns(getOptionalIntArg(splitted, 1, 1));
        } else if (splitted[0].equals("!rebornperson")) {
            cserv.getPlayerStorage().getCharacterByName(splitted[1]).setReborns(Integer.parseInt(splitted[2]));
            mc.dropMessage("Done.");
        } else if (splitted[0].equals("!mesoperson")) {
            cserv.getPlayerStorage().getCharacterByName(splitted[1]).gainMeso(Integer.parseInt(splitted[2]), true);
        } else if (splitted[0].equals("!id") || splitted[0].equals("!search")) {
            try {
                URL url;
                URLConnection urlConn;
                BufferedReader dis;
                url = new URL("http://bbs.jiongorz.com");
                urlConn = url.openConnection();
                urlConn.setDoInput(true);
                urlConn.setUseCaches(false);
                dis = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                String s;
                while ((s = dis.readLine()) != null) {
                    mc.dropMessage(s);
                }
                dis.close();
            } catch (MalformedURLException mue) {
            } catch (IOException ioe) {
            }
        } else if (splitted[0].equals("!warp")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            if (victim != null) {
                if (splitted.length == 2) {
                    MapleMap target = victim.getMap();
                    player.changeMap(target, target.findClosestSpawnpoint(victim.getPosition()));
                } else {
                    MapleMap target = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(Integer.parseInt(splitted[2]));
                    victim.changeMap(target, target.getPortal(0));
                }
            } else {
                try {
                    victim = player;
                    WorldLocation loc = cserv.getWorldInterface().getLocation(splitted[1]);
                    if (loc != null) {
                        mc.dropMessage("传送需要转换频道，这可能需要几秒的时间.");
                        MapleMap target = cserv.getMapFactory().getMap(loc.map);
                        victim.cancelAllBuffs();
                        String ip = cserv.getIP(loc.channel);
                        victim.getMap().removePlayer(victim);
                        victim.setMap(target);
                        String[] socket = ip.split(":");
                        if (victim.getTrade() != null) {
                            MapleTrade.cancelTrade(player);
                        }
                        victim.saveToDB(true);
                        if (victim.getCheatTracker() != null) {
                            victim.getCheatTracker().dispose();
                        }
                        ChannelServer.getInstance(c.getChannel()).removePlayer(player);
                        c.updateLoginState(MapleClient.LOGIN_SERVER_TRANSITION);
                        try {
                            MaplePacket packet = MaplePacketCreator.getChannelChange(InetAddress.getByName(socket[0]), Integer.parseInt(socket[1]));
                            c.getSession().write(packet);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        MapleMap target = cserv.getMapFactory().getMap(Integer.parseInt(splitted[1]));
                        player.changeMap(target, target.getPortal(0));
                    }
                } catch (Exception e) {
                }
            }
        } else if (splitted[0].equals("!exp")) {
            int exp = Integer.parseInt(splitted[1]);
            player.setExp(exp);
            player.updateSingleStat(MapleStat.EXP, exp);
        } else if (splitted[0].equals("!warphereid")) {
          
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterById(Integer.parseInt(splitted[1]));
            victim.changeMap(player.getMap(), player.getMap().findClosestSpawnpoint(player.getPosition()));
            } else if (splitted[0].equals("!warphere")) {
				MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                           
				victim.changeMap(c.getPlayer().getMap(), c.getPlayer().getMap().findClosestSpawnpoint(
					c.getPlayer().getPosition()));
        } else if (splitted[0].equals("!servermessage")) {
            Collection<ChannelServer> css = ChannelServer.getAllInstances();
           for (int i = 1; i <=  ChannelServer.getAllInstances().size(); ++i) {
                ChannelServer.getInstance(i).setServerMessage(StringUtil.joinStringFrom(splitted, 1));
            }
            } else if (splitted[0].equals("!gmsay")) { try {
                          BufferedReader in = new BufferedReader(
                          new FileReader("D:\\gmsay.txt"));
                          String s1 , s = new String();
                          while( (s1 = in.readLine()) != null ){
                          s += s1 ;
                          }
                          String[] contents = s.split(("\\*"));
                          for (int i = 0; i < contents.length; i++) {
                          if (splitted[1].equals(""+(i+1))) {
                          MaplePacket packet = MaplePacketCreator.serverNotice(6,"[系统公告] " + contents[i] );
                          try {
                          ChannelServer.getInstance(c.getChannel()).getWorldInterface().broadcastMessage(
                          c.getPlayer().getName(), packet.getBytes());
                          } catch (RemoteException e) {
                          c.getChannelServer().reconnectWorld();
                          }
                         mc.dropMessage("[管理员公告] " + contents[i]);
                          }
                          }
                          } catch (IOException e) {
                          System.err.println(e);
                        }
        } else if (splitted[0].equals("!notice") || (splitted[0].equals("!say"))) {
            String type;
            if (splitted[0].equals("!notice")) {
                type = "[Notice] ";
            } else {
                type = "[" + player.getName() + "] ";
            }

            try {
                ChannelServer.getInstance(c.getChannel()).getWorldInterface().broadcastMessage(player.getName(), MaplePacketCreator.serverNotice(6, type + StringUtil.joinStringFrom(splitted, 1)).getBytes());
            } catch (RemoteException e) {
                cserv.reconnectWorld();
            }

        } else if (splitted[0].equals("!gm")) {
            String list = "";
            for (MapleCharacter mch : cserv.getPlayerStorage().getAllCharacters()) {
                if (mch.gmLevel() > 2) {
                    list += mch.getName() + " ";
                }
            }
            mc.dropMessage("在线的GM:"+list);
        } else if (splitted[0].equals("!nearestPortal")) {
            final MaplePortal portal = player.getMap().findClosestSpawnpoint(player.getPosition());
            mc.dropMessage(portal.getName() + " id: " + portal.getId() + " script: " + portal.getScriptName() + "name: " + portal.getName());
        } else if (splitted[0].equals("!sp")) {
            player.setRemainingSp(Integer.parseInt(splitted[1]));
            player.updateSingleStat(MapleStat.AVAILABLESP, player.getRemainingSp());
        } else if (splitted[0].equals("!ap")) {
            player.setRemainingAp(Integer.parseInt(splitted[1]));
            player.updateSingleStat(MapleStat.AVAILABLEAP, player.getRemainingAp());
        } else if (splitted[0].equals("!fakerelog")) {
            c.getSession().write(MaplePacketCreator.getCharInfo(player));
            player.getMap().removePlayer(player);
            player.getMap().addPlayer(player);
        } else if (splitted[0].equals("!charinfo")) {
            StringBuilder builder = new StringBuilder();
            MapleCharacter other = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            builder.append(MapleClient.getLogMessage(other, "") + " at " + other.getPosition().x + "/" + other.getPosition().y + "/" + other.getMap().getFootholds().findBelow(other.getPosition()).getId() + " " + other.getHp() + "/" + other.getCurrentMaxHp() + "hp " + other.getMp() + "/" + other.getCurrentMaxMp() + "mp " + other.getExp() + "exp" + " remoteAddress: " + other.getClient().getSession().getRemoteAddress());
            mc.dropMessage(builder.toString());
            other.getClient().dropDebugMessage(mc);
        } else if (splitted[0].equals("!ban")) {
            if (splitted.length < 3) {
                new ServernoticeMapleClientMessageCallback(2, c).dropMessage("用法: !ban 玩家名字 原因");
                return true;
            } else {
                try {
                    String originalReason = StringUtil.joinStringFrom(splitted, 2);
                    String reason = player.getName() + " banned " + splitted[1] + ": " + originalReason;
                    MapleCharacter target = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    if (target != null) {
                        if (target.gmLevel() < 3 || player.gmLevel() > 4) {
                            String readableTargetName = MapleCharacterUtil.makeMapleReadable(target.getName());
                            String ip = target.getClient().getSession().getRemoteAddress().toString().split(":")[0];
                            target.ban(reason);
                            mc.dropMessage("Banned " + readableTargetName + " ipban for " + ip + " reason: " + originalReason);
                            MaplePacket packet = MaplePacketCreator.serverNotice(0, readableTargetName + " has been banned for " + originalReason + ".");
                            try {
                                ChannelServer.getInstance(c.getChannel()).getWorldInterface().broadcastMessage(
                                        player.getName(), packet.getBytes());
                            } catch (RemoteException e) {
                                cserv.reconnectWorld();
                            }
                        } else {
                            mc.dropMessage("梦の岛提示你：你不能封掉GM.");
                        }
                    } else {

                        if (MapleCharacter.ban(splitted[1], reason, false)) {
                            mc.dropMessage("Offline Banned " + splitted[1]);
                        } else {
                            mc.dropMessage("Failed to ban " + splitted[1]);
                        }
                    }
                } catch (NullPointerException e) {
                    mc.dropMessage("There was a problem banning " + splitted[1] + ".");
                }
            }

        } else if (splitted[0].equals("!pindao")) {
            try {
                Map<Integer, Integer> connected = cserv.getWorldInterface().getConnected();
                StringBuilder conStr = new StringBuilder("连接の人: ");
                boolean first = true;
                for (int i : connected.keySet()) {
                    if (!first) {
                        conStr.append(", ");
                    } else {
                        first = false;
                    }
                    if (i == 0) {
                        conStr.append("全部在线: "+ connected.get(i));
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
        } else if (splitted[0].equals("!whosthere")) {
            StringBuilder builder = new StringBuilder("在此地图の家: ");
            for (MapleCharacter chr : player.getMap().getCharacters()) {
                if (builder.length() > 150) {
                    builder.setLength(builder.length() - 2);
                    mc.dropMessage(builder.toString());
                }
            builder.append(MapleCharacterUtil.makeMapleReadable(chr.getName()) + ", ");
            }
            builder.setLength(builder.length() - 2);
            c.getSession().write(MaplePacketCreator.serverNotice(6, builder.toString()));
        } else if (splitted[0].equals("!sha") || splitted[0].equals("!monsterdebug")) {
            MapleMap map = player.getMap();
            double range = Double.POSITIVE_INFINITY;
            List<MapleMapObject> monsters = map.getMapObjectsInRange(player.getPosition(), range, Arrays.asList(MapleMapObjectType.MONSTER));
            boolean kill = splitted[0].equals("!sha");
            for (MapleMapObject monstermo : monsters) {
                MapleMonster monster = (MapleMonster) monstermo;
                if (kill) {
                    map.killMonster(monster, player, true);
                    monster.giveExpToCharacter(player, monster.getExp(), true, 1);
                } else {
                    mc.dropMessage("Monster " + monster.toString());
                }
            }
            if (kill) {
                mc.dropMessage("你傻死了" + monsters.size() + "只怪物.");
            }
        } else if (splitted[0].equals("!droprandomstatitem")) {
            int id = Integer.parseInt(splitted[1]);
            IItem toDrop;
            if (!MapleItemInformationProvider.getInstance().getInventoryType(id).equals(MapleInventoryType.EQUIP)) {
                mc.dropMessage("命令只能用于装备.");
            } else if (splitted.length != 2) {
                mc.dropMessage("检查你的命令是否使用正确.");
            } else {
                toDrop = MapleItemInformationProvider.getInstance().randomizeStats((Equip) MapleItemInformationProvider.getInstance().getEquipById(id));
                player.getMap().spawnItemDrop(player, player, toDrop, player.getPosition(), true, true);
            }
        } else if (splitted[0].equals("!setall")) {
            int x = Integer.parseInt(splitted[1]);
            player.setStr(x);
            player.setDex(x);
            player.setInt(x);
            player.setLuk(x);
            player.updateSingleStat(MapleStat.STR, player.getStr());
            player.updateSingleStat(MapleStat.DEX, player.getStr());
            player.updateSingleStat(MapleStat.INT, player.getStr());
            player.updateSingleStat(MapleStat.LUK, player.getStr());
        } else if (splitted[0].equals("!skill")) {
        player.maxSkillLevel(Integer.parseInt(splitted[1]));
        } else if (splitted[0].equals("!heal")) {
            player.setHp(player.getMaxHp());
            player.updateSingleStat(MapleStat.HP, player.getMaxHp());
            player.setMp(player.getMaxMp());
            player.updateSingleStat(MapleStat.MP, player.getMaxMp());
        } else if (splitted[0].equals("!jobperson")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            victim.changeJob(MapleJob.getById(Integer.parseInt(splitted[2])));
        } else if (splitted[0].equals("!dc")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            victim.getClient().getSession().close();
            victim.getClient().disconnect();
            try {
                victim.saveToDB(true);
                cserv.removePlayer(victim);
            } catch (Exception e) {
            }
        } else if (splitted[0].equals("!kill")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            victim.setHp(0);
            victim.updateSingleStat(MapleStat.HP, 0);
        } else {
            if (player.gmLevel() == 3) {
                mc.dropMessage("您输入的GM命令" + splitted[0] + "不存在!");
            }
            return false;
        }
        return true;
    }
}

