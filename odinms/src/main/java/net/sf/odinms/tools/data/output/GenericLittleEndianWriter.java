package net.sf.odinms.tools.data.output;

import java.nio.charset.Charset;

/**
 * Provides a generic writer of a little-endian sequence of bytes.
 * 
 * @author Frz
 * @version 1.0
 * @since Revision 323
 */
public class GenericLittleEndianWriter implements LittleEndianWriter {
	private static Charset GBK = Charset.forName("GBK");
	private ByteOutputStream bos;
    public  int  getlength(String  str){
        int  i,t=0;
        byte[]  bt  =  str.getBytes();
        for  (i=1;i<=bt.length;i++){
            if  (bt[i-1]<0)  {t=t+2;i++;}
            else  t=t+1;
        }
        return  t;
    }
	/**
	 * Class constructor - Protected to prevent instantiation with no arguments.
	 */
	protected GenericLittleEndianWriter() {
		// Blah!
	}

	/**
	 * Sets the byte-output stream for this instance of the object.
	 * 
	 * @param bos The new output stream to set.
	 */
	protected void setByteOutputStream(ByteOutputStream bos) {
		this.bos = bos;
	}

	/**
	 * Class constructor - only this one can be used.
	 * 
	 * @param bos The stream to wrap this objecr around.
	 */
	public GenericLittleEndianWriter(ByteOutputStream bos) {
		this.bos = bos;
	}

	/**
	 * Write an array of bytes to the stream.
	 * 
	 * @param b The bytes to write.
	 */
	@Override
	public void write(byte[] b) {
		for (int x = 0; x < b.length; x++) {
			bos.writeByte(b[x]);
		}
	}

	/**
	 * Write a byte to the stream.
	 * 
	 * @param b The byte to write.
	 */
	@Override
	public void write(byte b) {
		bos.writeByte(b);
	}

	/**
	 * Write a byte in integer form to the stream.
	 * 
	 * @param b The byte as an <code>Integer</code> to write.
	 */
	@Override
	public void write(int b) {
		bos.writeByte((byte) b);
	}

	/**
	 * Write a short integer to the stream.
	 * 
	 * @param i The short integer to write.
	 */
	@Override
	public void writeShort(int i) {
		bos.writeByte((byte) (i & 0xFF));
		bos.writeByte((byte) ((i >>> 8) & 0xFF));
	}

	/**
	 * Writes an integer to the stream.
	 * 
	 * @param i The integer to write.
	 */
	@Override
	public void writeInt(int i) {
		bos.writeByte((byte) (i & 0xFF));
		bos.writeByte((byte) ((i >>> 8) & 0xFF));
		bos.writeByte((byte) ((i >>> 16) & 0xFF));
		bos.writeByte((byte) ((i >>> 24) & 0xFF));
	}

	/**
	 * Writes an ASCII string the the stream.
	 * 
	 * @param s The ASCII string to write.
	 */
	@Override
	public void writeAsciiString(String s) {
		write(s.getBytes(GBK));
	}

	/**
	 * Writes a maple-convention ASCII string to the stream.
	 * 
	 * @param s The ASCII string to use maple-convention to write.
	 */
	@Override
	public void writeMapleAsciiString(String s) {
		writeShort((short)getlength(s));
		writeAsciiString(s);
	}

	/**
	 * Writes a null-terminated ASCII string to the stream.
	 * 
	 * @param s The ASCII string to write.
	 */
	@Override
	public void writeNullTerminatedAsciiString(String s) {
		writeAsciiString(s);
		write(0);
	}
	
	/**
	 * Write a long integer to the stream.
	 * @param l The long integer to write.
	 */
	@Override
	public void writeLong(long l) {
		bos.writeByte((byte) (l & 0xFF));
		bos.writeByte((byte) ((l >>> 8) & 0xFF));
		bos.writeByte((byte) ((l >>> 16) & 0xFF));
		bos.writeByte((byte) ((l >>> 24) & 0xFF));
		bos.writeByte((byte) ((l >>> 32) & 0xFF));
		bos.writeByte((byte) ((l >>> 40) & 0xFF));
		bos.writeByte((byte) ((l >>> 48) & 0xFF));
		bos.writeByte((byte) ((l >>> 56) & 0xFF));
	}
}
