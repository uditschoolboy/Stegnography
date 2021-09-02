package stegnography;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;

class Embedder {
    private String hiddenmessage;
    private String sourcefile, targetfile;
    private SecurityManager smgr;
    Embedder(String password, String hm, String sf, String tf) throws Exception
    {
        File f1 = new File(hm);
        File f2 = new File(sf);
        if(!f1.exists() || !f2.exists())
            throw new Exception("File doesn't exists");
        smgr = new SecurityManager(password);
        hiddenmessage = hm;
        sourcefile = sf;
        targetfile = tf;
    }
    void embed() throws Exception {
        File fileVessel = new File(sourcefile);
        BufferedImage buffVessel = ImageIO.read(fileVessel);

        //capacity checker
        int w, h, tot;
        w = buffVessel.getWidth();
        h = buffVessel.getHeight();
        tot = w * h;
        File fileToEmbed = new File(hiddenmessage);
        if (tot < fileToEmbed.length() + HeaderManager.HEADER_LENGTH)
            throw new Exception("File length greater than vessel");

        String hdr = HeaderManager.formHeader(fileToEmbed.getName(), fileToEmbed.length());
        WritableRaster wrstr = buffVessel.getRaster();
        FileInputStream srcfile = new FileInputStream(fileToEmbed);
        boolean keepEmbedding = true;
        int cnt = 0;
        int flag = smgr.getPermutation();
        for(int y = 0; y < h && keepEmbedding; y++)
        {
            for(int x = 0; x < w; x++)
            {
                int r =wrstr.getSample(x, y, 0);
                int g = wrstr.getSample(x, y, 1);
                int b = wrstr.getSample(x, y, 2);
                int data;
                if(cnt < HeaderManager.HEADER_LENGTH)
                {
                    data = hdr.charAt(cnt);
                }
                else
                {
                    data = srcfile.read();
                    if(data == -1)
                    {
                        keepEmbedding = false;
                        srcfile.close();
                        break;
                    }
                    data = smgr.primaryCrypto(data);
                }
                int arr[] = ByteProcessor.slice(data, flag);
                int result[] = ByteProcessor.merge(r, g, b, arr, flag);
                wrstr.setSample(x, y, 0, result[0]);
                wrstr.setSample(x, y, 1, result[1]);
                wrstr.setSample(x, y, 2, result[2]);
                cnt++;
                flag = (flag + 1) % 3 + 1;
            }
        }
        ImageIO.write(buffVessel, "PNG", new File(targetfile));
    }
}
