package stegnography;

public class Main {
    public static void main(String args[])
    {
        try
        {
            Embedder emb = new Embedder("this is the password", "/Users/uditchaudhary/Desktop/temp/pdffile.pdf", "/Users/uditchaudhary/Desktop/temp/other.jpg", "/Users/uditchaudhary/Desktop/temp/otherout.jpg");
            emb.embed();
            Extracter ext = new Extracter("this is the password", "/Users/uditchaudhary/Desktop/temp/otherout.jpg", "/Users/uditchaudhary/Desktop/temp");
            ext.extract();
        }
        catch(Exception e)
        {
            System.out.println("Error : " + e);
        }
    }
}
