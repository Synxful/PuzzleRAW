import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;



public class PuzzleEx extends JFrame {

    boolean bildEins;
    boolean bildZwei;
    boolean bildDrei;

    final char IMAGE1 = 'a';
    final char IMAGE2 = 'b';
    final char IMAGE3 = 'c';
    final char IMAGE4 = 'd';

    char img;
    private JPanel panel;
    private JPanel gameStat;




    private BufferedImage source;
    private BufferedImage resized;
    private Image image;
    private MyButton lastButton;
    private int width, height;

    public List<MyButton> getButtons() {
        return buttons;
    }

    private List<MyButton> buttons;
    private List<Point> solution;
    JMenuItem restart;
    JMenuItem exit;

    public Path getPicPath() {
        return picPath;
    }

    private Path picPath;

    private final int NUMBER_OF_BUTTONS = 12;
    private final int DESIRED_WIDTH = 900;

    public JPanel getPanel() {
        return panel;
    }


    public PuzzleEx() {

        super();
        initUI();

    }

    private void initUI() {
        // this.removeAll();
        panel = new JPanel();

        gameStat= new JPanel();



        for (Component jp : panel.getComponents())
        {
            int i = jp.hashCode();
            System.out.println(jp.hashCode() + jp.getClass().toString());
        }
        //this.add(panel);


        solution = new ArrayList<>();

        solution.add(new Point(0, 0));
        solution.add(new Point(0, 1));
        solution.add(new Point(0, 2));
        solution.add(new Point(1, 0));
        solution.add(new Point(1, 1));
        solution.add(new Point(1, 2));
        solution.add(new Point(2, 0));
        solution.add(new Point(2, 1));
        solution.add(new Point(2, 2));
        solution.add(new Point(3, 0));
        solution.add(new Point(3, 1));
        solution.add(new Point(3, 2));

        buttons = new ArrayList<>();


        panel.removeAll();
        panel.repaint();
        panel.invalidate();
        panel.validate();
        panel.setBorder(BorderFactory.createLineBorder(Color.gray));
        panel.setLayout(new GridLayout(4, 3, 0, 0));

        JMenuBar menubar = new JMenuBar();
        JMenu gamemenu = new JMenu("Spielmenü");
        JMenu picture = new JMenu("Bildauswahl");
        JMenu difficulty = new JMenu("Schwierigkeitsgrad");


        // 1 Menu Neustart
        restart = new JMenuItem("Neustart");
        restart.setToolTipText("Startet das Spiel neu");

        restart.addActionListener((ActionEvent event) ->{
            remove(panel);
            initUI();
        } );

        // 1 Menu Exit
        exit =  new JMenuItem("Beenden");
        exit.setToolTipText("Beendet das Spiel");
        exit.addActionListener((ActionEvent event) ->{
            System.exit(0);
        } );

        // 2 Menu Bildauswahl

        JMenuItem pic1 = new JMenuItem("Blondine");
        pic1.addActionListener((ActionEvent event) ->{


            img = IMAGE1;
            remove(panel);
            initUI();

        } );

        JMenuItem pic2 = new JMenuItem("Salma Hayek");
        pic2.addActionListener((ActionEvent event) ->{

            img = IMAGE2;
            remove(panel);
            initUI();

        } );

        JMenuItem pic3 = new JMenuItem("Megan Fox");
        pic3.addActionListener((ActionEvent event) ->{

            img = IMAGE3;
            remove(panel);
            initUI();

        } );
        JMenuItem choosePicture = new JMenuItem("Eigenes Bild laden");
        choosePicture.addActionListener((ActionEvent event) -> {


            img = IMAGE4;
            // Dialogfenster für Auswahl vom eigenem Bild
            JFileChooser fc = new JFileChooser();

            // Filter für JPG und PNG Bilder
            fc.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter imageFilefilter = new FileNameExtensionFilter("JPG files","jpg");
            FileNameExtensionFilter imageFilefilter2 = new FileNameExtensionFilter("PNG Files","png");
            fc.addChoosableFileFilter(imageFilefilter);
            fc.addChoosableFileFilter(imageFilefilter2);

            // Pfad im String auslesen
            int result = fc.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                String pathName = file.getPath();
                System.out.println(pathName);
                this.picPath = Paths.get(pathName);
                System.out.println(getPicPath().toString());
                remove(panel);
                initUI();
            }
        });


        // 3 Menu <Difficulty>
        ButtonGroup diffGroup = new ButtonGroup();

        JRadioButtonMenuItem rookie = new JRadioButtonMenuItem("Rookie");
        rookie.setSelected(true);
        JRadioButtonMenuItem amateur = new JRadioButtonMenuItem("Amateur");
        JRadioButtonMenuItem profi = new JRadioButtonMenuItem("Profi");

        diffGroup.add(rookie);
        diffGroup.add(amateur);
        diffGroup.add(profi);

        gamemenu.add(restart);
        gamemenu.add(exit);

        picture.add(pic1);
        picture.add(pic2);
        picture.add(pic3);
        picture.add(choosePicture);

        difficulty.add(rookie);
        difficulty.add(amateur);
        difficulty.add(profi);

        menubar.add(gamemenu);
        menubar.add(picture);
        menubar.add(difficulty);

        setJMenuBar(menubar);


        try {
            source = loadImage();
            int h = getNewHeight(source.getWidth(), source.getHeight());
            resized = resizeImage(source, DESIRED_WIDTH, h,
                    BufferedImage.TYPE_INT_ARGB);

        } catch (IOException ex) {
            Logger.getLogger(PuzzleEx.class.getName()).log(
                    Level.SEVERE, null, ex);
        }

        width = resized.getWidth(null);
        height = resized.getHeight(null);



        add(panel, BorderLayout.CENTER);

        for (int i = 0; i < 4; i++) {

            for (int j = 0; j < 3; j++) {

                image = createImage(new FilteredImageSource(resized.getSource(),
                        new CropImageFilter(j * width / 3, i * height / 4,
                                (width / 3), height / 4)));

                MyButton button = new MyButton(image);
                button.putClientProperty("position", new Point(i, j));

                if (i == 3 && j == 2) {
                    lastButton = new MyButton();
                    lastButton.setBorderPainted(false);
                    lastButton.setContentAreaFilled(false);
                    lastButton.setLastButton();
                    lastButton.putClientProperty("position", new Point(i, j));
                } else {
                    buttons.add(button);
                }
            }
        }

        buttons.add(lastButton);

        Collections.shuffle(buttons, new java.util.Random(System.currentTimeMillis()));

        for (   MyButton btn :  buttons){
            panel.add(btn);
            btn.setBorder(BorderFactory.createLineBorder(Color.gray));
            btn.addActionListener(new ClickAction(this));
        }


        pack();
        setTitle("Puzzle");
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }



    private int getNewHeight(int w, int h) {

        double ratio = DESIRED_WIDTH / (double) w;
        int newHeight = (int) (h * ratio);
        return newHeight;
    }


    private BufferedImage loadImage() throws IOException {


        // START PIC
        BufferedImage bimg = null;

        panel.removeAll();
        panel.repaint();
        switch (img)
        {
            case IMAGE1:  {
                System.out.println(getClass().getClassLoader().getResourceAsStream("res/pic1.jpg"));
                bimg = ImageIO.read(getClass().getClassLoader().getResourceAsStream("res/pic1.jpg"));
                break;
            }
            case IMAGE2:  {
                System.out.println(getClass().getClassLoader().getResourceAsStream("res/pic2.jpg"));
                bimg = ImageIO.read(getClass().getClassLoader().getResourceAsStream("res/pic2.jpg"));
                break;
            }
            case IMAGE3:  {
                System.out.println(getClass().getClassLoader().getResourceAsStream("res/pic3.jpg"));
                bimg = ImageIO.read(getClass().getClassLoader().getResourceAsStream("res/pic3.jpg"));
                break;
            }
            case IMAGE4: {

                System.out.println("------------------------");
                String picPath = getPicPath().toString();
                System.out.println(picPath + "  PIC PATH NO REPLACE");
                System.out.println("------------------------");
                picPath = picPath.replaceAll("\\\\", "/");
                System.out.println(picPath + "PIC PATH REPLACED");
                System.out.println("------------------------");


                URL url = getClass().getResource(picPath);
                System.out.println(url);

                File file = new File(url.getPath());
                bimg = ImageIO.read(file);


                /*System.out.println(getClass().getClassLoader().getResourceAsStream(getPicPath().toString()));
                bimg = ImageIO.read(getClass().getClassLoader().getResourceAsStream(getPicPath().toString()));*/
                break;
            }
            default:
            {

                URL url = getClass().getResource("/res/pic1.jpg");
                System.out.println(url.getPath());

                File file = new File(url.getPath());
                bimg = ImageIO.read(file);


                /*bimg = ImageIO.read(getClass().getClassLoader().getResourceAsStream("res/pic1.jpg"));*/
            }

        }
        return bimg;
    }



    private BufferedImage resizeImage(BufferedImage originalImage, int width,
                                      int height, int type) throws IOException {

        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();

        return resizedImage;
    }

    public void checkSolution() {

        List<Point> current = new ArrayList<>();

        for (JComponent btn : buttons) {
            current.add((Point) btn.getClientProperty("position"));
        }

        if (compareList(solution, current)) {
            JOptionPane.showMessageDialog(panel, "Finished",
                    "Congratulation", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static boolean compareList(List ls1, List ls2) {

        return ls1.toString().contentEquals(ls2.toString());
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                PuzzleEx puzzle = new PuzzleEx();
                puzzle.setVisible(true);
            }
        });
    }
}