import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class ByteWriter{

  private List<Byte> buffer;
  boolean sizeIncluded;

  public ByteWriter(){
    buffer = new ArrayList<>();
    sizeIncluded = false;
  }

  public ByteWriter(byte[] data){
    this();
    write(data);
  }

  public ByteWriter write(byte data){
    buffer.add(data);
    return this;
  }

  public ByteWriter write(byte[] data){
    for (byte d : data)
      buffer.add(d);
    return this;
  }

  public ByteWriter write(int data){
    byte[] d = ByteBuffer.allocate(4).putInt(data).array();
    return write(d);
  }

  public ByteWriter prependSize(){
    if (!sizeIncluded){
      byte[] data = ByteBuffer.allocate(4).putInt(buffer.size()).array();
      for (int i = 0; i < 4; i++)
        buffer.add(i, data[i]);
      sizeIncluded = true;
    }
    return this;
  }

  public byte[] getBuffer(){
    byte[] b = new byte[buffer.size()];
    for (int i=0; i<b.length;i++)
      b[i] = buffer.get(i);
    return b;
  }

}