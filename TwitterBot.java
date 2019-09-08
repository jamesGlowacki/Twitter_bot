import java.util.*;
import java.io.*;
import java.util.ArrayList;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.util.Random;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.lang.Runtime;

//code found on stack overflow just used to display a random meme
class DisplayImage{

    public DisplayImage(String path){
        try{
        BufferedImage img=ImageIO.read(new File(path));
        ImageIcon icon=new ImageIcon(img);
        JFrame frame=new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(800,800);
        JLabel lbl=new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } catch (IOException e){
            System.out.println("shit");
        }
    }
}

//meme object contains information such as name, path
//as well as what catagory it is in, catagory is found by
//name of parent directory
class Meme{
    String path, name, catagory;
    Boolean repost;

    public Meme(String p, String n, String c){
        this.path = p;
        this.name = n;
        this.catagory = c;
        this.repost=false;

    }

    public void printSelf(){
        System.out.printf(" name:%s \n path:%s \n catagory:%s\n", name, path, catagory);

    }

    public void dispImg(){
        /*
        try{
            System.out.println("should display");
            Image picutre = ImageIO.read(new File(path));
        } catch (IOException e){
            System.out.println("shit");
        }*/
        DisplayImage d = new DisplayImage(path);

    }


}

//list of 'Memes' each one is an image
class MemeList{

    public Boolean debug = false;
    ArrayList<String> posted;
    ArrayList<Meme> primary;

    public MemeList(Boolean db, ArrayList<String> p){
        this.debug = db;
        this.posted = p;
        String dir = "\\Users\\james\\Pictures\\memes";
        //ArrayList<Meme> muh = new ArrayList<Meme>();
        this.primary = this.getMemes( new File(dir), new ArrayList<Meme>(), "Meme");
        
        /*this is doing nothing
        if(debug){
            Meme m = getRandom();
            m.printSelf();
            m.dispImg();
        }*/
        
    }

    //recursively searches the given file directory and creates 'Meme'
    //objects from the files found within
    public ArrayList<Meme> getMemes( File file, ArrayList<Meme> memes, String cat){
        File[] list = file.listFiles();
        
        if(list!=null){
            for(File f: list){
                (f.isDirectory() ? getMemes(f, memes, f.getName()) : memes).add(new Meme(f.getPath(), f.getName(), cat));

            }
            return(memes);
        }
        for(Meme m: memes){
        	if(this.posted.contains(m.path)){
        		m.repost = true;
        	}
        }
        return(memes);

    }

    public Meme getRandom(){
        Random rand = new Random();
        return this.primary.get(rand.nextInt(this.primary.size()));
    }

}



class TwitterBot{

	MemeList memes;
	ArrayList<String> posted;

	public TwitterBot(Boolean debug) throws Exception{
		this.posted = loadPosted();
		this.memes = new MemeList(debug, this.posted);
		
	}

    public Meme getRand(){
        Meme tmp = this.memes.getRandom();
        System.out.print(tmp.path+ "\n");
        if(this.posted.contains(tmp.path)){
        	return(getRand());
        }else{
        	this.posted.add(tmp.path);
        	posted();
        	return(tmp);
        }
    }
    
    //loads up a list of all previously selected images to avoid repost
    public ArrayList<String> loadPosted() throws Exception{
        File file = new File("posted.txt");
        Scanner sc = new Scanner(file);
        ArrayList<String> p = new ArrayList();

        while( sc.hasNextLine() ){
            p.add(sc.nextLine());
            //System.out.println(sc.nextLine());
        }
        return(p);
    }


    
    public void posted(){
        System.out.println("posted");
    }

	public static void main(String[] args) throws Exception{
		String homeDirectory = System.getProperty("user.home"); //setting home directory
		TwitterBot bot = new TwitterBot(true); //initializing new bot with debug set to true
		while(true){
			Meme tmp = bot.getRand(); //selecting a random image
			tmp.printSelf(); //test code
			//note the python bot is what actually post the content
			Runtime.getRuntime().exec("python bot.py " + "image " + tmp.path); //running the python bot
			Thread.sleep(1200000); //sleeps for 2 hours
		}
		//System.out.println("done");
	}


}

