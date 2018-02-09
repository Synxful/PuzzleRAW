import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PuzzleEx extends JFrame {

    char img;
    private JPanel panel;

    public static int UIstartup = 1;

    private BufferedImage source;
    private BufferedImage resized;
    private Image image;
    private MyButton lastButton;
    private int width, height;

    private List<MyButton> buttons;
    private List<Point> solution;

    private Path picPath;

    private final int NUMBER_OF_BUTTONS = 12;
    private final int DESIRED_WIDTH = 900;

    /* CASE Variables <PictureChoose> */
    final char IMAGE1 = 'a';
    final char IMAGE2 = 'b';
    final char IMAGE3 = 'c';
    final char IMAGE4 = 'd';

    /* MAX Step Variables <Difficulty> */
    public static int rookieSteps = 10;
    public static int amateurSteps = 16;
    public static int profiSteps = 20;

    /* BOOLEAN Variables <Difficulty> */
    public boolean rookieTrue = false;
    public boolean amateurTrue = false;
    public boolean profiTrue = false;

    /* CREATE Menubar */
    JMenuBar menubar = new JMenuBar();
    JMenu gamemenu = new JMenu("Spielmenü");
    JMenu picture = new JMenu("Bildauswahl");
    JMenu difficulty = new JMenu("Schwierigkeitsgrad");

    /* CREATE Menu <Restart> */
    JMenuItem restart = new JMenuItem("Neustart");

    /* CREATE Menu <Exit>  */
    JMenuItem exit = new JMenuItem("Beenden");


    /* CREATE MenuItems <Pictures> */
    JMenuItem pic1 = new JMenuItem("Mary Jane");
    JMenuItem pic2 = new JMenuItem("Megan Fox");
    JMenuItem pic3 = new JMenuItem("Salma Hayek");

    /* CREATE MenuItems <Difficulty> */
    ButtonGroup diffGroup = new ButtonGroup();
    JRadioButtonMenuItem rookie = new JRadioButtonMenuItem("Rookie");
    JRadioButtonMenuItem amateur = new JRadioButtonMenuItem("Amateur");
    JRadioButtonMenuItem profi = new JRadioButtonMenuItem("Profi");


    public PuzzleEx() {
        super();
        initUI();
    }

    private void initUI() {

        /* STARTUP REMOVE */
        if (UIstartup == 0) {

            panel.removeAll();
            panel.repaint();
            panel.invalidate();
            panel.validate();
            remove(panel);

        }

        panel = new JPanel();

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


        /* STARTUP SCHLEIFE */
        if (UIstartup == 1) {

            /* Menu <Difficulty>  */

            // DEFAULT
            rookie.setSelected(true);
            rookieTrue = true;

            // ROOKIE
            rookie.addActionListener((ActionEvent event) -> {

                remove(panel);
                initUI();

                System.out.println("CHOOSEN 'ROOKIE' DIFFICULTY");
                rookie.setSelected(true);
                rookieTrue = true;
                amateurTrue = false;
                profiTrue = false;

                setMaxSteps();

                System.out.println("VERSUCHE : " + rookieSteps);


            });

            // AMATEUR
            amateur.addActionListener((ActionEvent event) -> {

                remove(panel);
                initUI();

                System.out.println("CHOOSEN 'AMATEUR' DIFFICULTY");
                amateur.setSelected(true);
                rookieTrue = false;
                amateurTrue = true;
                profiTrue = false;

                setMaxSteps();

                System.out.println("VERSUCHE : " + amateurSteps);


            });

            // PROFI
            profi.addActionListener((ActionEvent event) -> {

                remove(panel);
                initUI();

                System.out.println("CHOOSEN 'PROFI' DIFFICULTY");
                profi.setSelected(true);
                rookieTrue = false;
                amateurTrue = false;
                profiTrue = true;

                setMaxSteps();

                System.out.println("VERSUCHE : " + profiSteps);


            });



            // 1 Menu < Neustart >

            restart.setToolTipText("Startet das Spiel neu");
            restart.addActionListener((ActionEvent event) ->{

                UIrestart();

            } );

            /* Menu <Exit>  */

            exit.setToolTipText("Beendet das Spiel");
            exit.addActionListener((ActionEvent event) -> {

                UIexit();

            });

        /* Menu <Bildauswahl>  */

            pic1.addActionListener((ActionEvent event) -> {


                System.out.println("CHOOSEN 'MARY JANE' PICTURE");
                setMaxSteps();
                img = IMAGE1;
                remove(panel);
                initUI();


            });

            pic2.addActionListener((ActionEvent event) -> {

                System.out.println("CHOOSEN 'MEGAN FOX' PICTURE");
                setMaxSteps();
                img = IMAGE2;
                remove(panel);
                initUI();

            });

            pic3.addActionListener((ActionEvent event) -> {

                System.out.println("CHOOSEN 'SALMA HAYEK' PICTURE");
                setMaxSteps();
                img = IMAGE3;
                remove(panel);
                initUI();

            });



            /* ADDING MenuItems to FRAME */
            diffGroup.add(rookie);
            diffGroup.add(amateur);
            diffGroup.add(profi);

            gamemenu.add(restart);
            gamemenu.add(exit);

            picture.add(pic1);
            picture.add(pic2);
            picture.add(pic3);

            difficulty.add(rookie);
            difficulty.add(amateur);
            difficulty.add(profi);

            menubar.add(gamemenu);
            menubar.add(picture);
            menubar.add(difficulty);

            setJMenuBar(menubar);

        }



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


        /* ADDING Panel to FRAME */
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

        for (MyButton btn : buttons) {
            panel.add(btn);
            btn.setBorder(BorderFactory.createLineBorder(Color.gray));
            btn.addActionListener(new ClickAction(this));
        }


        pack();
        setTitle("Puzzle");
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /* STARTUP CLOSE */
        UIstartup = 0;
    }


    public Path getPicPath() {
        return picPath;
    }

    public List<MyButton> getButtons() {
        return buttons;
    }

    public JPanel getPanel() {
        return panel;
    }


    private int getNewHeight(int w, int h) {

        double ratio = DESIRED_WIDTH / (double) w;
        int newHeight = (int) (h * ratio);
        return newHeight;
    }

    private void gameOver() throws IOException {

        /*BufferedImage bimg = ImageIO.read(getClass().getClassLoader().getResourceAsStream("res/game_over.jpg"));
        JLabel picLabel = new JLabel(new ImageIcon(bimg));
        panel.add(picLabel);*/

        panel.repaint();
        panel.invalidate();
        panel.validate();
        remove(panel);


        /* GAME OVER */
        JOptionPane.showMessageDialog(null,"GAME OVER !");
        System.out.println("GAME OVER!");

        /* RESTART panel */
        UIrestart();
    }

    private BufferedImage loadImage() throws IOException {


        // START PIC
        BufferedImage bimg = null;

        panel.removeAll();
        panel.repaint();
        switch (img) {
            case IMAGE1: {

                /*URL url = getClass().getResource("res/pic1.jpg");
                String picPath = url.toString();
                picPath = picPath.replaceAll("\\\\", "/");
                picPath = picPath.replaceAll("file:/", "");
                System.out.println(picPath);*/

                bimg = ImageIO.read(getClass().getClassLoader().getResourceAsStream("res/mary_jane.jpg"));
                break;

            }
            case IMAGE2: {
                bimg = ImageIO.read(getClass().getClassLoader().getResourceAsStream("res/megan_fox.jpg"));
                break;

            }
            case IMAGE3: {
                bimg = ImageIO.read(getClass().getClassLoader().getResourceAsStream("res/salma_hayek.jpg"));
                break;

            }
            case IMAGE4: {

                /*String picPath = getPicPath().toString();
                picPath = picPath.replaceAll("\\\\", "/");
                URL url = getClass().getResource(picPath);
                File file = new File(url.getPath());
                bimg = ImageIO.read(file);*/

                System.out.println(getClass().getClassLoader().getResourceAsStream(getPicPath().toString()));
                bimg = ImageIO.read(getClass().getClassLoader().getResourceAsStream(getPicPath().toString()));
                break;
            }
            default: {

                /*URL url = getClass().getResource("/res/pic1.jpg");
                System.out.println("DEFAUL URL : " + url.getPath());
                File file = new File(url.getPath());
                System.out.println("FILE URL : " + url.getPath());
                bimg = ImageIO.read(file);*/


                bimg = ImageIO.read(getClass().getClassLoader().getResourceAsStream("res/diane_kruger.jpg"));
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

    public void UIrestart(){
        setMaxSteps();
        remove(panel);
        initUI();
        System.out.println("GAME RESTARTED!");
    }

    public void UIexit(){

        System.out.println("CLOSING THE GAME !");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);

    }

    public void setMaxSteps() {
        rookieSteps = 10;
        amateurSteps = 16;
        profiSteps = 20;
    }

    public void checkRadioBtn() {

        // CHECK RADIOBUTTON

        if (rookieTrue) {
            rookieSteps = rookieSteps - 1;
            System.out.println("Steps left for ROOKIE : " + rookieSteps);
        } else if (amateur.isSelected()) {
            amateurSteps = amateurSteps - 1;
            System.out.println("Steps left for AMATEUR : " + amateurSteps);

        } else if (profi.isSelected()) {
            profiSteps = profiSteps - 1;
            System.out.println("Steps left for PROFI : " + profiSteps);
        }

    }

    public void checkSteps() {


        // CHECK ZERO STEPS
        if (rookieSteps == 0 || amateurSteps == 0 || profiSteps == 0) {
            try {
                gameOver();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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