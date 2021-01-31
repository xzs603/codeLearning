

package net.sf.odinms.scripting.npc;

import java.util.HashMap;
import java.util.Map;
import javax.script.Invocable;
import net.sf.odinms.client.MapleClient;
import net.sf.odinms.client.MapleCharacter;
import net.sf.odinms.scripting.AbstractScriptManager;

/**
 *
 * @author Matze
 */
public class NPCScriptManager extends AbstractScriptManager {

	private Map<MapleClient,NPCConversationManager> cms = new HashMap<MapleClient,NPCConversationManager>();
	private Map<MapleClient,NPCScript> scripts = new HashMap<MapleClient,NPCScript>();
	private static NPCScriptManager instance = new NPCScriptManager();

	public synchronized static NPCScriptManager getInstance() {
		return instance;
	}

	public void start(MapleClient c, int npc) {
            start(c, npc, null, null);
	}
    public int getRandom(int start, int end) {
        return (int) Math.floor(Math.random() * end + start);
    }  
	public void start(MapleClient c, int i,String npc) {
		try {
			if (cms.containsKey(c)) {
				return;
                        } else {
                        }
		} catch (Exception e) {
			log.error("NPC脚本发生错误.请检查NPC编号:(" + npc + ")的JS.");
			dispose(c);
			cms.remove(c);
		}
	}

	public void start(MapleClient c, int npc, String filename, MapleCharacter chr) {
		try {
			NPCConversationManager cm = new NPCConversationManager(c, npc, chr);
			if (cms.containsKey(c)) {
				return;
			}
			cms.put(c, cm);
			Invocable iv = getInvocable("npc/" + npc + ".js", c);
                        NPCScriptManager npcsm = NPCScriptManager.getInstance();
			if (filename != null) {
				iv = getInvocable("npc/" + filename + ".js", c);
			}
                        if (iv == null || npcsm == null || NPCScriptManager.getInstance() == null) {
                            cm.sendSimple("5555，我无法为你服务哦，我编号: (" + npc + ")快去加脚本吧 ");
                            cm.dispose();
                            return;
                        }
			engine.put("cm", cm);
			NPCScript ns = iv.getInterface(NPCScript.class);
			scripts.put(c, ns);
			if (chr != null) {
				ns.start(chr);
			} else {
			ns.start();
                        }
		} catch (Exception e) {
			log.error("NPC脚本发生错误.请检查NPC编号:(" + npc + ")的JS.");
			dispose(c);
			cms.remove(c);
		}
	}

	public void start(MapleClient c, String filename, int npc, MapleCharacter cha) {
		try {
			NPCConversationManager cm = new NPCConversationManager(c, npc, cha);
			if (cms.containsKey(c)) {
				return;
			}
			cms.put(c, cm);
			Invocable iv = getInvocable("npc/" + filename + ".js", c);
			if (iv == null || NPCScriptManager.getInstance() == null) {
				cm.dispose();
				return;
			}
			engine.put("cm", cm);
			NPCScript ns = iv.getInterface(NPCScript.class);
			scripts.put(c, ns);
			ns.start(cha);
		} catch (Exception e) {
			log.error("NPC脚本发生错误.请检查NPC编号:(" + npc + ")的JS.");
			dispose(c);
			cms.remove(c);
		}
	}

	public void action(MapleClient c, byte mode, byte type, int selection) {
		NPCScript ns = scripts.get(c);
		if (ns != null) {
			try {
				ns.action(mode, type, selection);
			} catch (Exception e) {
				log.error("执行NPC脚本过程中发生错误.请检查NPC文件.");
				dispose(c);
			}
		}
	}

	public void dispose(NPCConversationManager cm) {
		cms.remove(cm.getC());
		scripts.remove(cm.getC());
		resetContext("npc/" + cm.getNpc() + ".js", cm.getC());
	}

	public void dispose(MapleClient c) {
		NPCConversationManager npccm = cms.get(c);
		if (npccm != null) {
			dispose (npccm);
		}
	}

	public NPCConversationManager getCM(MapleClient c) {
		return cms.get(c);
	}
}
