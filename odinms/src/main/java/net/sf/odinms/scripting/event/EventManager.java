package net.sf.odinms.scripting.event;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import net.sf.odinms.tools.MaplePacketCreator;
import java.sql.*;
import java.sql.ResultSet;
import net.sf.odinms.database.DatabaseConnection;
import net.sf.odinms.client.MapleCharacter;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.Invocable;
import javax.script.ScriptException;
import net.sf.odinms.net.channel.ChannelServer;
import net.sf.odinms.net.world.MapleParty;
import net.sf.odinms.server.TimerManager;
import net.sf.odinms.server.maps.MapleMap;

/**
 *
 * @author Matze
 */
public class EventManager {

	private Invocable iv;
	private ChannelServer cserv;
	private Map<String,EventInstanceManager> instances = new HashMap<String,EventInstanceManager>();
	private Properties props = new Properties();
	private String name;
	
	public EventManager(ChannelServer cserv, Invocable iv, String name) {
		this.iv = iv;
		this.cserv = cserv;
		this.name = name;
	}
	
	public void cancel() {
		try {
			iv.invokeFunction("cancelSchedule", (Object) null);
		} catch (ScriptException ex) {
			Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
		} catch (NoSuchMethodException ex) {
			Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void schedule(final String methodName, long delay) {
		TimerManager.getInstance().schedule(new Runnable() {

			public void run() {
				try {
					iv.invokeFunction(methodName, (Object) null);
				} catch (ScriptException ex) {
					Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
				} catch (NoSuchMethodException ex) {
					Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
			
		}, delay);
	}
	
	public ScheduledFuture<?> scheduleAtTimestamp(final String methodName, long timestamp) {
		return TimerManager.getInstance().scheduleAtTimestamp(new Runnable() {

			public void run() {
				try {
					iv.invokeFunction(methodName, (Object) null);
				} catch (ScriptException ex) {
					Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
				} catch (NoSuchMethodException ex) {
					Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
			
		}, timestamp);		
	}
	
	public ChannelServer getChannelServer() {
		return cserv;
	}
	
	public EventInstanceManager getInstance(String name) {
		return instances.get(name);
	}
	
	public Collection<EventInstanceManager> getInstances() {
		return Collections.unmodifiableCollection(instances.values());
	}
	
	public EventInstanceManager newInstance(String name) {
		EventInstanceManager ret = new EventInstanceManager(this, name);
		instances.put(name, ret);
		return ret;
	}
	
	public void disposeInstance(String name) {
		instances.remove(name);
	}

	public Invocable getIv() {
		return iv;
	}
	
	public void setProperty(String key, String value) {
		props.setProperty(key, value);
	}
	
	public String getProperty(String key) {
		return props.getProperty(key);
	}

	public String getName() {
		return name;
	}
	
	//PQ method: starts a PQ
	public void startInstance(MapleParty party, MapleMap map) {
		try {
			EventInstanceManager eim = (EventInstanceManager)(iv.invokeFunction("setup", (Object) null));
			eim.registerParty(party, map);
		} catch (ScriptException ex) {
			Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
		} catch (NoSuchMethodException ex) {
			Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	//non-PQ method for starting instance
	public void startInstance(EventInstanceManager eim, String leader) {
		try {
			iv.invokeFunction("setup", eim);
			eim.setProperty("leader", leader);
		} catch (ScriptException ex) {
			Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
		} catch (NoSuchMethodException ex) {
			Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
    public void autoExp(int amount){
            Collection<MapleCharacter> chrs = cserv.getPlayerStorage().getAllCharacters();
            for (MapleCharacter chr : chrs) {
            int giveExp = chr.getLevel()*chr.getLevel()*cserv.getExpRate();
            int giveMeso= chr.getLevel()*chr.getLevel()*cserv.getMesoRate();
            int giveExp1 = (int)(giveExp*1.5);
            int giveExp2 = (int)(giveExp*2.5);
            int giveExp3 = giveExp*3;
                        int giveExpcj = (int)(giveExp*1.3);
            int giveExp4 = giveExp*4;
            int giveExp5 = giveExp*5;
            int giveMeso1 = (int)(giveMeso*1.5);
            int giveMeso2 = (int)(giveMeso*2.5);
            int giveMeso3 = giveMeso*3;
                        int giveMesocj = (int)(giveMeso*1.3);
            int giveMeso4 = giveMeso*4;
            int giveMeso5 = giveMeso*5;

            if((chr.getMapId()==910000000) && (chr.getLevel()>=0& chr.getLevel()<=179) && chr.getVip() == 0) {
            chr.gainExp (giveExp, true,true);
            chr.gainMeso(giveMeso, true);
            chr.getClient().getSession().write(MaplePacketCreator.serverNotice(5, "你是普通游戏玩家,市场泡点获得"+giveExp+"经验" +giveMeso+"金钱"));
            chr.saveToDB(true);
            } else if((chr.getMapId()==910000000) && (chr.getLevel()>=180& chr.getLevel()<=200) && chr.getVip() == 0) {
            chr.gainExp (giveExp1, true,true);
            chr.gainMeso(giveMeso1, true);
            chr.getClient().getSession().write(MaplePacketCreator.serverNotice(5, "你是超级玩家,市场泡点额外获得"+giveExp1+"经验" +giveMeso1+"金钱"));
            chr.saveToDB(true);
            
            } else if (chr.getVip() == 1) {
            chr.gainExp (giveExp1, true,true);
            chr.gainMeso(giveMeso1, true);
            chr.getClient().getSession().write(MaplePacketCreator.serverNotice(5, "你是本服的VIP1,全世界泡点获得"+giveExp1+"经验" +giveMeso1+"金钱"));
            chr.saveToDB(true);
            chr.getClient().getSession().write(MaplePacketCreator.serverNotice(5, "服务器系统自动存档."));
             } else if (chr.getVip() == 2) {
            chr.gainExp (giveExp2, true,true);
            chr.gainMeso(giveMeso2, true);
            chr.getClient().getSession().write(MaplePacketCreator.serverNotice(5, "你是本服的VIP2,全世界泡点获得"+giveExp2+"经验" +giveMeso2+"金钱"));
            chr.saveToDB(true);
            chr.getClient().getSession().write(MaplePacketCreator.serverNotice(5, "服务器系统自动存档."));
            } else if (chr.getVip() == 3) {
            chr.gainExp (giveExp3, true,true);
            chr.gainMeso(giveMeso3, true);
            chr.getClient().getSession().write(MaplePacketCreator.serverNotice(5, "梦の岛提示你：你是本服VIP3,全世界泡点获得"+giveExp3+"经验" +giveMeso3+"金钱"));
            chr.saveToDB(true);
            chr.getClient().getSession().write(MaplePacketCreator.serverNotice(5, "梦の岛提示你：服务器系统自动存档."));
            } else if (chr.getVip() == 4) {
            chr.gainExp (giveExp4, true,true);
            chr.gainMeso(giveMeso4, true);
            chr.getClient().getSession().write(MaplePacketCreator.serverNotice(5, "梦の岛提示你：你是本服的VIP4,全世界泡点获得"+giveExp1+"经验" +giveMeso1+"金钱"));
            chr.saveToDB(true);
            chr.getClient().getSession().write(MaplePacketCreator.serverNotice(5, "梦の岛提示你：服务器系统自动存档."));
                        } else if (chr.getVip() == 5) {
            chr.gainExp (giveExp5, true,true);
            chr.gainMeso(giveMeso5, true);
            chr.getClient().getSession().write(MaplePacketCreator.serverNotice(5, "梦の岛提示你：你是本服的VIP5,全世界泡点获得"+giveExp1+"经验" +giveMeso1+"金钱"));
            chr.saveToDB(true);
            chr.getClient().getSession().write(MaplePacketCreator.serverNotice(5, "梦の岛提示你：服务器系统自动存档."));

            } else if (chr.getVip() == 1) {
            chr.gainExp (giveExp1, true,true);
            chr.gainMeso(giveMeso1, true);
            chr.getClient().getSession().write(MaplePacketCreator.serverNotice(5, "梦の岛提示你：你是VIP1会员,全世界泡点获得"+giveExp1+"经验"+giveMeso1+"金钱"));
            chr.saveToDB(true);
            chr.getClient().getSession().write(MaplePacketCreator.serverNotice(5, "梦の岛提示你：系统自动存档."));
             } else if (chr.getVip() == 2) {
            chr.gainExp (giveExp2, true,true);
            chr.gainMeso(giveMeso2, true);
            chr.getClient().getSession().write(MaplePacketCreator.serverNotice(5, "梦の岛提示你：你是VIP2会员,全世界泡点获得"+giveExp2+"经验"+giveMeso2+"金钱"));
            chr.saveToDB(true);
            chr.getClient().getSession().write(MaplePacketCreator.serverNotice(5, "梦の岛提示你：系统自动存档."));
            } else if (chr.getVip() == 3) {
            chr.gainExp (giveExp3, true,true);
            chr.gainMeso(giveMeso3, true);
            chr.getClient().getSession().write(MaplePacketCreator.serverNotice(5, "梦の岛提示你：你是VIP3会员,全世界泡点获得"+giveExp3+"经验" +giveMeso3+"金钱"));
            chr.saveToDB(true);
            chr.getClient().getSession().write(MaplePacketCreator.serverNotice(5, "梦の岛提示你：系统自动存档."));
            } else if (chr.getVip() == 4) {
            chr.gainExp (giveExp4, true,true);
            chr.gainMeso(giveMeso4, true);
            chr.getClient().getSession().write(MaplePacketCreator.serverNotice(5, "梦の岛提示你：你是本服的VIP4,全世界泡点获得"+giveExp1+"经验" +giveMeso1+"金钱"));
            chr.saveToDB(true);
            chr.getClient().getSession().write(MaplePacketCreator.serverNotice(5, "梦の岛提示你：服务器系统自动存档."));
                        } else if (chr.getVip() == 5) {
            chr.gainExp (giveExp5, true,true);
            chr.gainMeso(giveMeso5, true);
            chr.getClient().getSession().write(MaplePacketCreator.serverNotice(5, "梦の岛提示你：你是本服的VIP5,全世界泡点获得"+giveExp1+"经验" +giveMeso1+"金钱"));
            chr.saveToDB(true);
            chr.getClient().getSession().write(MaplePacketCreator.serverNotice(5, "梦の岛提示你：服务器系统自动存档."));
             }
          }
        }
}
