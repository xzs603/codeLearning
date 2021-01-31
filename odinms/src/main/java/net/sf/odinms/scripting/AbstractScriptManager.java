
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.odinms.scripting;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import net.sf.odinms.client.MapleClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Matze
 */
public abstract class AbstractScriptManager {

    protected ScriptEngine engine;
    private ScriptEngineManager sem;
    protected static final Logger log = LoggerFactory.getLogger(AbstractScriptManager.class);

    protected AbstractScriptManager() {
        sem = new ScriptEngineManager();
    }

    protected Invocable getInvocable(String path, MapleClient c) {
        try {
            path = "scripts/" + path;
            engine = null;
            if (c != null) {
                engine = c.getScriptEngine(path);
            }
            if (engine == null) {
                File scriptFile = new File(path);
                if (!scriptFile.exists()) {
                    return null;
                }
                engine = sem.getEngineByName("javascript");
                if (c != null) {
                    c.setScriptEngine(path, engine);
                }
                FileReader fr = new FileReader(scriptFile);
                BufferedReader br=new BufferedReader(fr);
String line=br.readLine();
StringBuffer js=new StringBuffer();
while(line!=null){
    js.append(line);
    js.append("\r\n");
    line=br.readLine();
}

engine.eval(js.toString());
br.close();
fr.close();
            }
            return (Invocable) engine;
        } catch (Exception e) {
            log.error("Error executing script.", e);
            return null;
        }
    }

    protected void resetContext(String path, MapleClient c) {
        path = "scripts/" + path;
        c.removeScriptEngine(path);
    }
}
