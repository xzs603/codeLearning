/*
Jiongorz. Src. Command.java
JiongorzBBs : http://bbs.jiongorz.com
AdminQQ:307586193
Command
*/
package net.sf.odinms.client.messages;

public interface CommandProcessorMBean {
	String processCommandJMX(int cserver, int mapid, String command);
}