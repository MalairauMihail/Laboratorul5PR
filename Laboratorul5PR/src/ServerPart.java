import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ServerPart {

    static AudioInputStream ais;
    static AudioFormat format;
    static boolean status = true;
    static int port = 50005;
    static int sampleRate = 44100;

    static DataLine.Info dataLineInfo;
    static SourceDataLine sourceDataLine;

    public static void main(String args[]) throws Exception
    {
        System.out.println("Serverul porneste pe portul:"+port);

        DatagramSocket serverSocket = new DatagramSocket(port);

        byte[] receiveData = new byte[4096];

        format = new AudioFormat(sampleRate, 16, 2, true, false);
        dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
        sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
        sourceDataLine.open(format);
        sourceDataLine.start();

        DatagramPacket primitPacket = new DatagramPacket(receiveData, receiveData.length);

        ByteArrayInputStream baiss = new ByteArrayInputStream(primitPacket.getData());

        while (status == true)
        {
            serverSocket.receive(primitPacket);
            ais = new AudioInputStream(baiss, format, primitPacket.getLength());
            toSpeaker(primitPacket.getData());
        }

        sourceDataLine.drain();
        sourceDataLine.close();
    }

    public static void toSpeaker(byte soundbytes[]) {
        try
        {
            System.out.println("Microfunul functioneaza");
            sourceDataLine.write(soundbytes, 0, soundbytes.length);
        } catch (Exception e) {
            System.out.println("Microfunul nu functioneaza..");
            e.printStackTrace();
        }
    }
}