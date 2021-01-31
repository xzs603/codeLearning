package net.sf.odinms.server.maps;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;

import net.sf.odinms.server.MaplePortal;
import net.sf.odinms.server.life.MapleMonster;
import net.sf.odinms.net.channel.ChannelServer;


/**
 *
 * @author alex_soh
 */
public class SBossMapMonitor extends BossMapMonitor {

    protected int[] mobs;
    protected boolean deadCount[];
    protected ChannelServer cserv;
    protected boolean hasHappen;
    protected short type;
    protected int trigger;
    public static final short HORNTAIL=0;
    public static final short ZAKUM=1;

    public SBossMapMonitor(MapleMap map,MapleMap pMap,MaplePortal portal,int[] mobs,ChannelServer cserv)
    {
        super(map,pMap,portal);
        this.mobs=mobs;
        this.deadCount=new boolean[mobs.length];
        this.cserv=cserv;
        for(int i=0;i<deadCount.length;i++)
        {
            deadCount[i]=type!=SBossMapMonitor.HORNTAIL;
        }
        hasHappen=false;
        type=SBossMapMonitor.HORNTAIL;
    }
    
    public SBossMapMonitor(MapleMap map,MapleMap pMap,MaplePortal portal,int[] mobs,ChannelServer cserv,short type,int trigger)
    {
        super(map,pMap,portal);
        this.mobs=mobs;
        this.deadCount=new boolean[mobs.length];
        this.cserv=cserv;
        for(int i=0;i<deadCount.length;i++)
        {
            deadCount[i]=type!=SBossMapMonitor.HORNTAIL;
        }
        hasHappen=false;
        this.type=type;
        this.trigger=trigger;
    }    

    private boolean chkDeadCount()
    {
        boolean result=true;
        for(int i=0;i<deadCount.length;i++)
        {
            if(!deadCount[i] && type==SBossMapMonitor.HORNTAIL)
            {
                result=false;
                break;
            }
            else if(deadCount[i] && type==SBossMapMonitor.ZAKUM)
            {
                result=false;
                break;
            }
        }
        return result;
    }
    
    private MapleMonster getMonster(int id)
    {
        MapleMonster m=null;
        List<MapleMapObject> list=getAllMob();
        for(int i=0;i<list.size();i++)
        {
            MapleMonster monster = (MapleMonster) list.get(i);
            if(monster.getId()==id)
            {
                m=monster;
                break;
            }
        }        
        return m;        
    }

    private List<MapleMapObject> getAllMob()
    {
      return map.getMapObjectsInRange(new Point(0,0), Double.POSITIVE_INFINITY, Arrays
                    .asList(MapleMapObjectType.MONSTER));
    }

    private boolean chkSpecialBoss()
    {
        List<MapleMapObject> list=getAllMob();
        for(int j=0;j<deadCount.length;j++)
        {
            if(!deadCount[j] && type==SBossMapMonitor.HORNTAIL)
            {
                for(int i=0;i<list.size();i++)
                {
                    MapleMonster monster = (MapleMonster) list.get(i);
                    if(monster.getId()==mobs[j])
                    {
                        deadCount[j]=true;
                        break;
                    }
                }
            }
            else if(deadCount[j] && type==SBossMapMonitor.ZAKUM)
            {
                boolean found=false;
                for(int i=0;i<list.size();i++)
                {
                    MapleMonster monster = (MapleMonster) list.get(i);
                    if(monster.getId()==mobs[j])
                    {
                        found=true;
                        break;
                    }
                }                
                deadCount[j]=found;
            }
        }
        return chkDeadCount();
    }

    @Override
    public void run()
    {
        MapleMonster triggerMob=null;
        switch(type)
        {
            case SBossMapMonitor.HORNTAIL:                    
                break;
            case SBossMapMonitor.ZAKUM:
                while(triggerMob==null)
                {
                    triggerMob=getMonster(trigger);
                    try
                    {
                        Thread.sleep(500);
                    }
                    catch(InterruptedException e)
                    {
                    }
                }
                triggerMob.setLock(true);
                break;
        }
        while(map.playerCount()>0)
        {
            if(chkSpecialBoss() && !hasHappen)
            {                
                //special event happen
                switch(type)
                {
                    case SBossMapMonitor.HORNTAIL:
                            map.killAllmonster();
                            cserv.broadcastPacket(net.sf.odinms.tools.MaplePacketCreator.serverNotice(6, cserv.getChannel(), "【黑龙战况】经过无数次的战争，暗黑龙王终于被勇士们所消灭，它们才是真正的龙林的英雄!!"));
                            hasHappen=true;
                            break;
                    case SBossMapMonitor.ZAKUM:
                            triggerMob.setLock(false);
                            triggerMob.setHp(triggerMob.getMaxHp());
                            triggerMob.setMp(triggerMob.getMaxMp());
                            hasHappen=true;
                            break;
                }
            }
            try
            {
                switch(type)
                {
                    case SBossMapMonitor.HORNTAIL:
                        Thread.sleep(3000);//how often it chk for the existence of deads parts,increase to reduce server lag
                        break;
                    case SBossMapMonitor.ZAKUM:
                        Thread.sleep(3000);//how often it chk for the existence of arms,increase to reduce server lag
                        break;
                }
                
            }
            catch(InterruptedException e)
            {
                //e.printStackTrace();
            }
        }
        while(map.mobCount()>0)
        {
            map.killAllmonster();
            try
            {
                Thread.sleep(5000);
            }
            catch(InterruptedException e)
            {
                //e.printStackTrace();
            }
        }
        map.resetReactors();
        pMap.resetReactors();
        portal.setPortalState(MapleMapPortal.OPEN);
    }
}  
