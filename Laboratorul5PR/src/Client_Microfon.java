import javax.sound.sampled.*;
import java.io.IOException;
import java.net.*;

public class Client_Microfon
{
    public byte[] buffer;
    private int port;
    static AudioInputStream ais;

    public static void main(String[] args)
    {
        TargetDataLine line;
        DatagramPacket dgp;

        AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
        float rate = 44100.0f;
        int channels = 2;
        int sampleSize = 16;
        boolean bigEndian = false;
        InetAddress addr;


        AudioFormat format = new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8) * channels, rate, bigEndian);

        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("Line matching " + info + " not supported.");
            return;
        }

        try
        {
            line = (TargetDataLine) AudioSystem.getLine(info);

            int buffsize = 4096;

            line.open(format);

            line.start();

            int numBytesRead;
            byte[] data = new byte[buffsize];

            addr = InetAddress.getByName("127.0.0.1");
            DatagramSocket socket = new DatagramSocket();
            while (true) {
                numBytesRead =  line.read(data, 0, data.length);

                dgp = new DatagramPacket (data,data.length,addr,50005);

                socket.send(dgp);
            }

        }catch (LineUnavailableException e) {
            e.printStackTrace();
        }catch (UnknownHostException e) {
            // TODO: handle exception
        } catch (SocketException e) {
            // TODO: handle exception
        } catch (IOException e2) {
            // TODO: handle exception
        }
    }
}