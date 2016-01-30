//Bare minimum imports.
import javax.swing.*;
import java.io.*;
import java.nio.file.*;
class l{
    //Declared here because a uses them.
    JFrame f=new JFrame();
    JTextPane t=new JTextPane();
    JFileChooser j=new JFileChooser();

    //Main method/constructor to avoid making the above variables static.
    static void main(){
        new l();
    }

    //Constructor
    l(){
        f.add(new JScrollPane(t));
        f.setSize(640,480);
        
        //Binds key characters to the action a.
        //Note that the values for VK_char were substituted with numbers to save space,
        //and that the "meta" mask was replaced with 4. This makes the program mac-exclusive.
        p(79); //o for open
        p(83); //s for save
        p(80); //p for print
        p(78); //n for new

        t.getActionMap().put("",new a());

        f.setVisible(true);
    }

    //Helper method that avoids repeated calls.
    //This method binds a given character (or rather, its ASCII value) to the action "a", defined below.
    void p(int a){t.getInputMap().put(KeyStroke.getKeyStroke(a,4),"");}

    //Note that a is here instead of being defined in-place because it needs to call the defined instance variables above.
    class a extends AbstractAction{
        public void actionPerformed(java.awt.event.ActionEvent a){
            //Massive try-catch block to swallow all errors.
            try{
                String s=a.getActionCommand();
                //We abuse JFileChooser's methods to make very short statements that prompt users and get results.
                if(s.equals("o")&&j.showOpenDialog(f)==0){
                    //The longest, because of the need to create a new temporary char array.
                    File i=j.getSelectedFile();
                    //char[]c=new char[(int)i.length()];
                    //new FileReader(i).read(c);
                    //t.setText(new String(c));
                    //f.setTitle(i.getName());
                    
                    //Shortened with nio
                    t.setText(new String(Files.readAllBytes(i.toPath())));f.setTitle(i.getName());
                }
                if(s.equals("s")&&j.showSaveDialog(f)==0){
                    //FileWriter instead of BufferedWriter because we need to close if we want to save.
                    //File i=j.getSelectedFile();FileWriter w=new FileWriter(i);w.write(t.getText());w.close();f.setTitle(i.getName());
                    
                    //nio is a little shorter
                    File i=j.getSelectedFile();
                    Files.write(i.toPath(),t.getText().getBytes());
                    f.setTitle(i.getName());
                }
                if(s.equals("p"))
                //Lets the user handle most things.
                    t.print();
                if(s.equals("n")&&new JOptionPane("Sure?",0,1).showConfirmDialog(f,"Clear?")==0){
                    //Confirmation for clearing display.
                    t.setText("");
                    f.setTitle("");
                }
            }catch(Exception e){}
        }
    }
}