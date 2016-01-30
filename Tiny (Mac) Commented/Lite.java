/* Note that the limitations on this text editor are ideally <2kb.
 * Increased features over the pocket version:
 * -A proper title: "Lite"
 * -Support for find/replace
 * -Tabbed editing support
 * -A help menu?
 * -Font support
 * -Full editing support
 */

//Bare minimum imports.
import javax.swing.*;
import java.io.*;
import java.nio.file.*;
class Lite{
    //Declared here because a uses them.
    JFrame f=new JFrame(); //Main drawing frame
    JPanel p=new JPanel(); //Find bar
    JTextField g=new JTextField(),h=new JTextField(); //Textfields in find (used to determine find or replace)
    JLabel m=new JLabel(); //Amount of matches found label
    JTextPane t=new JTextPane(); //Main text frame
    JFileChooser j=new JFileChooser(); //A file chooser to save space
    boolean b; //Whether or not the find bar is displayed
    String v="",y=""; //Used to tell whether or not the find text changed
    int c=0,n=0,q=0; //The caret position to move to in find\the total number of matches\cur number of matches

    //Main method/constructor to avoid making the above variables static.
    static void main(){
        new Lite();
    }

    Lite(){
        //f.getRootPane().putClientProperty("apple.awt.draggableWindowBackground", Boolean.TRUE);//This is fun
        //Adds the center text field
        f.add(new JScrollPane(t),"Center");

        //Setup for the find bar
        p.setLayout(new BoxLayout(p,0));
        g.addActionListener(new a());
        h.addActionListener(new a());

        //Adds the main components
        d(new JLabel("Find:"));
        d(g);
        d(new JLabel("Replace:"));
        d(h);

        //Adds the "Number found" component
        d(m);

        //Sets it to invisible and adds it
        p.setVisible(b);
        f.add(p,"North");

        //Sets the size of the frame
        f.setSize(640,480);

        //Binds key characters to the action a.
        //Note that the values for VK_char were substituted with numbers to save space,
        //and that the "meta" mask was replaced with 4. This makes the program mac-exclusive.
        p(79); //o for open
        p(83); //s for save
        p(80); //p for print
        p(78); //n for new

        p(70); //f for find/replace

        //Binds the action to the one defined below.
        t.getActionMap().put("",new a());

        //Draw, and let's go.
        f.setVisible(true);
    }

    //Helper method that avoids repeated calls.
    //This binds a key to the AbstractAction "a", defined below.
    void p(int a){t.getInputMap().put(KeyStroke.getKeyStroke(a,4),"");}

    //ad[d] a component to the main JPanel
    void d(java.awt.Component o){p.add(o);}

    //Note that a is here instead of being defined in-place because it needs to call the defined instance variables above.
    class a extends AbstractAction{
        public void actionPerformed(java.awt.event.ActionEvent a){
            y=t.getText();
            //Massive try-catch block to swallow all errors.
            try{
                String s=a.getActionCommand();
                Object u=a.getSource();
                if(s.equals("o")&&j.showOpenDialog(f)==0){
                    //The longest, because of the need to create a new temporary char array.
                    File i=j.getSelectedFile();
                    //char[]c=new char[(int)i.length()];
                    //new FileReader(i).read(c);
                    //t.setText(new String(c));
                    //f.setTitle(i.getName());

                    //Not always safe, uses default encoding, but shortest
                    //t.setText(y=new java.util.Scanner(j.getSelectedFile()).useDelimiter("\\Z").next());
                    //f.setTitle(y);

                    //Safest way, using nio
                    t.setText(y=new String(Files.readAllBytes(i.toPath())));f.setTitle(i.getName());
                }
                if(s.equals("s")&&j.showSaveDialog(f)==0){
                    //FileWriter instead of BufferedWriter because we need to close if we want to save.
                    File i=j.getSelectedFile();Files.write(i.toPath(),y.getBytes());f.setTitle(i.getName());
                    //FileWriter w=new FileWriter(i);
                    //w.write(y);
                    //w.close();

                }
                if(s.equals("p"))
                //Lets the user handle most things.
                    t.print();
                if(s.equals("n")&&JOptionPane.showConfirmDialog(f,"Clear?")==0){
                    //Confirmation for clearing display.
                    t.setText("");
                    f.setTitle("");
                }
                if(s.equals("f")) {
                    //Find/replace.
                    p.setVisible(b=!b);
                }
                if(u.equals(g)) {
                    //Find method
                    //If we're tabbing through the found words
                    if(s.equals(v)){
                        int z=s.length();
                        while(c+z<=y.length()){
                            if(q!=n&&t.getText(c,z).equals(s)){
                                //Only highlights, doesn't use the highlighter to save space.
                                t.select(c,c+=z);
                                m.setText(++q+"/"+n);
                                return;
                            }
                            c++;
                        }
                        //Loops back if we're done searching the document 
                        //(this will only be reached when that's true)
                        m.setText("Last!");
                        c=q=0;
                    }
                    else
                    //Here we're just searching through for a number; used in above tabbing
                        m.setText("Found: "+
                            (n=((y+" ").split(v=s).length)-1));

                }
                if(u.equals(h)) {
                    //Replace (assumes there is text in find)
                    t.setText(y.replaceAll(g.getText(),s));
                }
            }catch(Exception e){}
        }
    }
}