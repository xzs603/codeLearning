/*
Jiongorz. Src. Command.java
JiongorzBBs : http://bbs.jiongorz.com
AdminQQ:307586193
Command
*/
package net.sf.odinms.client.messages;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.util.List;
import net.sf.odinms.client.*;
import net.sf.odinms.net.MaplePacket;
import net.sf.odinms.net.channel.ChannelServer;
import net.sf.odinms.net.world.remote.CheaterData;
import net.sf.odinms.net.world.remote.WorldLocation;
import net.sf.odinms.server.MaplePortal;
import net.sf.odinms.server.MapleShop;
import net.sf.odinms.server.MapleShopFactory;
import net.sf.odinms.server.MapleTrade;
import net.sf.odinms.server.maps.MapleMap;
import net.sf.odinms.tools.MaplePacketCreator;
import net.sf.odinms.tools.StringUtil;

public class InternCommand {
    public static boolean executeInternCommand(MapleClient c, MessageCallback mc, String line) {
        ChannelServer cserv = c.getChannelServer();
            MapleCharacter player = c.getPlayer();
        String[] splitted = line.split(" ");
        if (splitted[0].equals("!maple")) {
            MapleShopFactory sfact = MapleShopFactory.getInstance();
            MapleShop shop = sfact.getShop(1353);
            shop.sendShop(c);
        } else if (splitted[0].equals("!glimmer")) {
            MapleShopFactory sfact = MapleShopFactory.getInstance();
            MapleShop shop = sfact.getShop(1338);
            shop.sendShop(c);
        } else if (splitted[0].equals("!misc")) {
            MapleShopFactory sfact = MapleShopFactory.getInstance();
            MapleShop shop = sfact.getShop(1339);
            shop.sendShop(c);
        } else if (splitted[0].equals("!scroll")) {
            MapleShopFactory sfact = MapleShopFactory.getInstance();
            MapleShop shop = sfact.getShop(1340);
            shop.sendShop(c);
        } else if (splitted[0].equals("!sbag")) {
            MapleShopFactory sfact = MapleShopFactory.getInstance();
            MapleShop shop = sfact.getShop(1341);
            shop.sendShop(c);
        }  else if (splitted[0].equals("!ring")) {
            MapleShopFactory sfact = MapleShopFactory.getInstance();
            MapleShop shop = sfact.getShop(1346);
            shop.sendShop(c);
        } else if (splitted[0].equals("!gmshop")) {
            MapleShopFactory sfact = MapleShopFactory.getInstance();
            MapleShop shop = sfact.getShop(1337);
            shop.sendShop(c);
        } else if (splitted[0].equals("!map")) {
            int mapid = Integer.parseInt(splitted[1]);
            MapleMap target = cserv.getMapFactory().getMap(mapid);
            MaplePortal targetPortal = null;
            if (splitted.length > 2) {
                try {
                    targetPortal = target.getPortal(Integer.parseInt(splitted[2]));
                } catch (IndexOutOfBoundsException ioobe) {
                } catch (NumberFormatException nfe) {
                }
            }
            if (targetPortal == null) {
                targetPortal = target.getPortal(0);
            }
            c.getPlayer().changeMap(target, targetPortal);
            } else if (splitted[0].equals("!gm2")) {
            mc.dropMessage("您是巡逻GM你的等级<GMlevel>:2，可以使用以下命令.");
            mc.dropMessage("!gmzx - 查看在线玩家状态,有MOVE则为非正常状态.");
            mc.dropMessage("!gmshop - 巡逻GM商店(没时间了~下次出)");
            mc.dropMessage("!fuzhu - 隐身+巡逻START~~囧");
            mc.dropMessage("!zaixian - 查看在线玩家状态.");
            mc.dropMessage("!mapid - 你在地图的ID");
            mc.dropMessage("@ring [戒指id] [对方id]- 送戒指");
            mc.dropMessage("!banid [id] [reason]  -ID封号");
            mc.dropMessage("!map [id]  传送地图");
            mc.dropMessage("!warpid [id]  可跨线传送ID角色处");
            mc.dropMessage("其他功能待研发.");
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
							mc.dropMessage("要跨线呢！");
			
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
						mc.dropMessage("梦の岛提示你：出现了错误" + e.getMessage());
					}
				}
                    } else if (splitted[0].equals("!fuzhu")) {
            int[] array = {9001000, 9101002, 9101003, 9101004, 9101008, 2001002, 1101007, 1005, 2301003, 5121009, 4111001, 4111002, 4211003, 4211005, 1321000, 2321004, 3121002};
            for (int i = 0; i < array.length; i++) {
                SkillFactory.getSkill(array[i]).getEffect(SkillFactory.getSkill(array[i]).getMaxLevel()).applyTo(player);
            }
        } else if (splitted[0].equals("!gmzx")) {
			try {
				List<CheaterData> cheaters = c.getChannelServer().getWorldInterface().getCheaters();
				for (int x = cheaters.size() - 1; x >= 0; x--) {
					CheaterData cheater = cheaters.get(x);
					mc.dropMessage(cheater.getInfo());
				}
			} catch (RemoteException e) {
				c.getChannelServer().reconnectWorld();
			}

        } else {
            if (c.getPlayer().gmLevel() == 2) {
                mc.dropMessage("梦の岛提示你：您输入的实习者命令 " + splitted[0] + " 不存在.");
            }
            return false;
        }
        return true;
    }
}