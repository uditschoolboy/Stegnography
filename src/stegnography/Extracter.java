package stegnography;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileOutputStream;

class Extracter {
    private String vessel;
    private String extractedFile;
    private SecurityManager smgr;
    private String targetFolder;
    Extracter(String password, String vessel, String targetFolder) throws Exception
    {
        File f1 = new File(vessel);
        if(!f1.exists()) throw new Exception("File dosen't exists");

        smgr = new SecurityManager(password);
        this.vessel = vessel;
        this.targetFolder = targetFolder;
    }
    void extract() throws Exception
    {
        File fileVessel = new File(vessel);
        BufferedImage buffVessel = ImageIO.read(fileVessel);

        Raster rstr = buffVessel.getData();

        int w = buffVessel.getWidth(), h = buffVessel.getHeight();
        int flag = smgr.getPermutation();
        int cnt = 0;
        boolean keepExtracting = true;
        String hdr = "";
        int fileSize = 0;
        FileOutputStream fout = null;
        for(int y = 0; y < h && keepExtracting; y++)
        {
            for(int x = 0; x < w; x++)
            {
                int r = rstr.getSample(x, y, 0);
                int g = rstr.getSample(x, y, 1);
                int b = rstr.getSample(x, y, 2);
                int arr[] = ByteProcessor.extract(r, g, b, flag);
                int data = ByteProcessor.combine(arr, flag);
                if(cnt < HeaderManager.HEADER_LENGTH)
                {
                    hdr = hdr + (char)data;
                    if(cnt == HeaderManager.HEADER_LENGTH - 1)
                    {
                        extractedFile = HeaderManager.getFileName(hdr);
                        fileSize = HeaderManager.getFileSize(hdr);
                        extractedFile = "newlycreated" + extractedFile;
                        fout = new FileOutputStream(targetFolder + "/" + extractedFile);
                    }
                }
                else
                {
                    data = smgr.primaryCrypto(data);
                    fout.write(data);
                    if(cnt == fileSize + HeaderManager.HEADER_LENGTH)
                    {
                        keepExtracting = false;
                        fout.close();
                        break;
                    }
                }
                cnt++;
                flag = (flag + 1) % 3 + 1;
            }
        }
    }
}
