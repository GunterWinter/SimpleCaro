package ClientSocket;

import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;

public class Client extends JComponent {

    public static JFrame jframe;
    JButton[][] nut;
    boolean flat = true;
    boolean winner;

    JTextArea content;
    JTextField enterchat;
    JButton send;
    Timer thoigian;
    Integer second, minute;
    JLabel demthoigian;
    JLabel turn;
    TextField textField;
    JPanel p;
    JPanel Pimage;
    String temp = "";
    int x, y;
    int[][] matran; // ma trận của X tức là của địch
    int[][] matrandanh; // ma trận của mình
    BufferedImage myPicture;

    // Server Socket
    Socket socket;
    ObjectOutputStream os;// .........
    ObjectInputStream is;// 

    //MenuBar
    MenuBar menubar;

    public Client() {
        jframe = new JFrame();
        jframe.setTitle("Game Caro Client");
        jframe.setSize(750, 500);
        x = 25;
        y = 25;
        jframe.getContentPane().setLayout(null);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //f.setVisible(true);
        jframe.setResizable(false); // đóng phóng to nhỏ

        matran = new int[x][y];
        matrandanh = new int[x][y];
        menubar = new MenuBar();
        p = new JPanel();
        p.setBounds(10, 30, 400, 400); // bảng caro
        p.setLayout(new GridLayout(x, y));
        Pimage = new JPanel();
        Pimage.setBounds(480, 20, 200, 100);
        try {
            myPicture = ImageIO.read(new File("image\\logo.png"));
        } catch (IOException ex) {

        }
        JLabel picLabel = new JLabel(new ImageIcon(myPicture.getScaledInstance(190, 90, Image.SCALE_SMOOTH)));
        JLabel Title = new JLabel("Cờ Caro");
        Title.setFont(new Font("Times New Roman", Font.BOLD, 16));
        Title.setBounds(550, 76, 100, 100);
        JLabel Text = new JLabel("Bạn Là X");
        Text.setFont(new Font("Times New Roman", Font.BOLD, 16));
        Text.setForeground(Color.RED);
        Text.setBounds(150, 0, 100, 40);
        turn = new JLabel("Lượt O");
        turn.setFont(new Font("Times New Roman", Font.BOLD, 17));
        turn.setForeground(Color.GREEN);
        turn.setBounds(10, 0, 200, 40);

        Pimage.add(picLabel);
        jframe.add(p);
        jframe.add(Pimage);
        jframe.setMenuBar(menubar);// tao menubar cho frame
        jframe.add(Title);
        jframe.add(Text);
        jframe.add(turn);

        jframe.setMenuBar(menubar);// tao menubar cho frame
        Menu game = new Menu("Game");
        menubar.add(game);
        Menu help = new Menu("Help");
        menubar.add(help);
        MenuItem helpItem = new MenuItem("Help");
        help.add(helpItem);
        MenuItem about = new MenuItem("About ..");
        help.add(about);
        help.addSeparator();
        MenuItem newItem = new MenuItem("New Game");
        game.add(newItem);
        MenuItem exit = new MenuItem("Exit");
        game.add(exit);
        game.addSeparator();
        newItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                newgame();
                try {
                    os.writeObject("newgame,123");
                } catch (IOException ie) {
                    //
                }
            }

        });
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Object[] options = {"OK"};
                JOptionPane.showConfirmDialog(jframe,
                        "Nguyễn Quốc Thái", "Information",
                        JOptionPane.CLOSED_OPTION);
            }
        });
        help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Object[] options = {"OK"};
                JOptionPane.showConfirmDialog(jframe,
                        "Luật chơi rất đơn giản bạn chỉ cần 5 ô liên tiếp nhau\n"
                                + "Theo hàng ngang hoặc dọc hoặc chéo là bạn đã thắng", "Luật Chơi",
                        JOptionPane.CLOSED_OPTION);
            }
        });
        //khung chat
        Font fo = new Font("Arial", Font.BOLD, 15);
        content = new JTextArea();
        content.setFont(fo);
        content.setBackground(Color.white);

        content.setEditable(false);
        JScrollPane sp = new JScrollPane(content);
        sp.setBounds(430, 170, 300, 180);
        send = new JButton("Gửi");
        send.setBounds(640, 390, 70, 40);
        enterchat = new JTextField("");
        enterchat.setFont(fo);
        enterchat.setBounds(430, 400, 200, 30);
        enterchat.setBackground(Color.white);
        jframe.add(enterchat);
        jframe.add(send);
        jframe.add(sp);
        jframe.setVisible(true);
        //jframe.getRootPane().setDefaultButton(send); // trick enter
        enterchat.addKeyListener(new KeyAdapter() { // nếu nhấn enter
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        temp += "Tôi: " + enterchat.getText() + "\n";
                        content.setText(temp);
                        os.writeObject("chat," + enterchat.getText());
                        enterchat.setText("");
                        //temp = "";
                        enterchat.requestFocus();
                        content.setVisible(false);
                        content.setVisible(true);
                    } catch (Exception r) {
                        r.printStackTrace();
                    }
                }
            }
        });
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource().equals(send)) {
                    try {

                        temp += "Tôi: " + enterchat.getText() + "\n";
                        content.setText(temp);
                        os.writeObject("chat," + enterchat.getText());
                        enterchat.setText("");
                        //temp = "";
                        enterchat.requestFocus();
                        content.setVisible(false);
                        content.setVisible(true);

                    } catch (Exception r) {
                        r.printStackTrace();
                    }
                }
            }
        });
        demthoigian = new JLabel("Thời Gian:");
        demthoigian.setFont(new Font("TimesRoman", Font.ITALIC, 16));
        demthoigian.setForeground(Color.BLACK);
        jframe.add(demthoigian);
        demthoigian.setBounds(430, 120, 300, 50);
        second = 0;
        minute = 0;
        thoigian = new Timer(1000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String temp = minute.toString();
                String temp1 = second.toString();
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                if (temp1.length() == 1) {
                    temp1 = "0" + temp1;
                }
                if (second == 59) {
                    demthoigian.setText("Thời Gian:" + temp + ":" + temp1);
                    minute++;
                    second = 0;
                }
                if (minute == 1) {
                    thoigian.stop();
                    try {
                        os.writeObject("checkwin,123");
                    } catch (IOException ex) {

                    }
                    Object[] options = {"Ok luôn", "Nghỉ game"};

                    int m = JOptionPane.showOptionDialog(jframe, "Bạn đã thua game này \nBạn có muốn chơi lại không?", "Thông báo",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                            null, options, options[0]);
                    if (m == JOptionPane.YES_OPTION) {
                        try {
                            os.writeObject("message,123"); // nếu ok thì gửi bên kia để đồng ý điều kiện chơi lại
                        } catch (IOException ie) {
                            //
                        }
                    } else if (m == JOptionPane.NO_OPTION) {
                        JOptionPane.showMessageDialog(jframe,
                                "Thế thì cút",
                                "Bye",
                                JOptionPane.INFORMATION_MESSAGE);
                        setEnableButton(false);
                    }
                } else {
                    demthoigian.setText("Thời Gian:" + temp + ":" + temp1);
                    second++;
                }

            }

        });

        nut = new JButton[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                final int a = i, b = j;
                nut[a][b] = new JButton();
                nut[a][b].setBackground(Color.WHITE);
                nut[a][b].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        thoigian.start();

                        second = 0;
                        minute = 0;

                        matrandanh[a][b] = 1;
                        nut[a][b].setEnabled(false);
                        //nut[a][b].setContentAreaFilled(false);
                        nut[a][b].setIcon(new ImageIcon("image\\x.png"));
                        nut[a][b].setDisabledIcon(new ImageIcon("image\\x.png"));
                        try {
                            os.writeObject("caro," + a + "," + b); // truyền a và b vào caro
                            setEnableButton(false); // tắt các nút bên ngoài đều là false không cho nhập nữa
                        } catch (Exception ie) {
                            ie.printStackTrace();
                        }
                        turn.setText("Lượt O");
                        turn.setForeground(Color.GREEN);
                        thoigian.stop();
                    }

                });
                p.add(nut[a][b]);
                p.setVisible(false);
                p.setVisible(true);
            }
        }
        if (flat) {
            setEnableButton(false);
        }

        try {
            socket = new Socket("127.0.0.1", 1234);
            System.out.println("Da ket noi toi server!");
            os = new ObjectOutputStream(socket.getOutputStream());
            is = new ObjectInputStream(socket.getInputStream());
            while (true) {
                String stream = is.readObject().toString();
                String[] data = stream.split(",");
                if (data[0].equals("chat")) {
                    temp += "Khách:" + data[1] + '\n';
                    content.setText(temp);
                } else if (data[0].equals("caro")) {
                    thoigian.start();
                    second = 0;
                    minute = 0;
                    caro(data[1], data[2]);
                    setEnableButton(true);
                    if (winner == true)
                        setEnableButton(false);
                } else if (data[0].equals("newgame")) {
                    newgame();
                } else if (data[0].equals("checkwin")) {
                    thoigian.stop();
                    JOptionPane.showMessageDialog(jframe,
                            "Bạn đã thắng, giờ bạn đợi thằng thua kia có chịu đầu hàng hay không?",
                            "Thông báo",
                            JOptionPane.INFORMATION_MESSAGE);
                } else if (data[0].equals("message")) {
                    accept();
                }
            }
        } catch (Exception ie) {
            // ie.printStackTrace();
        } //finally {
        //      socket.close();
        //      serversocket.close();
        //}
        textField = new TextField();

    }

    public void accept() {
        flat=false;
        Object[] options = {"Ok luôn", "Thôi"};
        int m = JOptionPane.showOptionDialog(jframe, "Bên kia muốn chơi lại bạn có muốn chơi không", "Thông báo",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);
        if (m == JOptionPane.YES_OPTION) {
            newgame();
            try {
                os.writeObject("newgame,123");
            } catch (IOException ie) {
                //
            }
            turn.setText("Lượt X");
            turn.setForeground(Color.RED);
        } else if (m == JOptionPane.NO_OPTION) {
            JOptionPane.showMessageDialog(jframe,
                    "Thế thì cút",
                    "Bye",
                    JOptionPane.INFORMATION_MESSAGE);
            setEnableButton(false);
        }
    }

    public void newgame() {
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                nut[i][j].setIcon(new ImageIcon("image\\new.png"));
                //nut[i][j].setBackground(Color.LIGHT_GRAY);
                nut[i][j].setDisabledIcon(new ImageIcon("image\\new.png"));
                matran[i][j] = 0;
                matrandanh[i][j] = 0;
            }
        }
        setVisiblePanel(p);
        second = 0;
        minute = 0;
        thoigian.stop();
        if (!flat) {
            setEnableButton(true);
        } else {
            setEnableButton(false);
        }
    }

    public void setVisiblePanel(JPanel pHienthi) {
        jframe.add(pHienthi);
        pHienthi.setVisible(true);
        pHienthi.updateUI();// ......

    }

    public void setEnableButton(boolean b) { // set nút không cho nhập nút khác nữa
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (matrandanh[i][j] == 0)
                    nut[i][j].setEnabled(b);
            }
        }
    }

    //thuat toan tinh thang thua
    public int checkHang() {
        int hang = 0, n = 0, k = 0;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (matran[i][j] == 1) {
                    hang++;
                } else {
                    hang = 0;
                }
                if (hang >= 5)
                    return 1;
            }
            hang = 0;
        }
        return 0;
    }

    public int checkCot() {
        int cot = 0, n = 0, k = 0;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (matran[j][i] == 1) {
                    cot++;
                } else {
                    cot = 0;
                }
                if (cot >= 5)
                    return 1;
            }
            cot = 0;
        }
        return 0;
    }

    public int checkCheoPhai() {
        int j = 0;
        while (j < y) {
            int demo = 0;
            int tamj = j, tami = 0;
            while (tamj < y) {
                if (matran[tami][tamj] == 1)
                    demo++;
                else
                    demo = 0;
                tamj++;
                tami++;
                if (demo >= 5)
                    return 1;
            }
            j++;
        }
        int i = 0;
        while (i < x) {
            int demo = 0;
            int tami = i, tamj = 0;
            while (tami < x) {
                if (matran[tami][tamj] == 1)
                    demo++;
                else
                    demo = 0;
                tamj++;
                tami++;
                if (demo >= 5)
                    return 1;
            }
            i++;
        }
        return 0;
    }

    public int checkCheoTrai() {
        int j = y - 1;
        while (j >= 0) {
            int demo = 0;
            int tamj = j, tami = 0;
            while (tamj >= 0) {
                if (matran[tami][tamj] == 1)
                    demo++;
                else
                    demo = 0;
                tamj--;
                tami++;
                if (demo >= 5)
                    return 1;
            }
            j--;
        }
        int i = 0;
        while (i < x) {
            int demo = 0;
            int tami = i, tamj = x - 1;
            while (tami < x) {
                if (matran[tami][tamj] == 1)
                    demo++;
                else
                    demo = 0;
                tamj--;
                tami++;
                if (demo >= 5)
                    return 1;
            }
            i++;
        }
        return 0;
    }

    //chat game
    public void caro(String x, String y) {
        turn.setText("Lượt X");
        turn.setForeground(Color.RED);
        int xx = Integer.parseInt(x);
        int yy = Integer.parseInt(y);
        // danh dau vi tri danh
        matran[xx][yy] = 1;
        matrandanh[xx][yy] = 1;
        nut[xx][yy].setEnabled(false);
        nut[xx][yy].setIcon(new ImageIcon("image\\o.png"));
        nut[xx][yy].setDisabledIcon(new ImageIcon("image\\o.png"));
//        nut[xx][yy].setBackground(Color.BLACK);

        // Kiem tra thang hay chua
        winner = (checkHang() == 1 || checkCot() == 1 || checkCheoPhai() == 1 || checkCheoTrai() == 1);
        if (checkHang() == 1 || checkCot() == 1 || checkCheoPhai() == 1
                || checkCheoTrai() == 1) {
            flat = true; // lúc này O thắng X thua
            turn.setText("Lượt O");
            turn.setForeground(Color.GREEN);
            thoigian.stop();
            try {
                os.writeObject("checkwin,123");
            } catch (IOException ex) {

            }
            Object[] options = {"Đồng ý", "Nghỉ game"};
            int m = JOptionPane.showOptionDialog(jframe, "Bạn đã thua game này\nBạn có muốn chơi lại không?", "Thông báo",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, options, options[0]);
            if (m == JOptionPane.YES_OPTION) {
                try {
                    os.writeObject("message,123");
                } catch (IOException ie) {
                    //
                }
            } else if (m == JOptionPane.NO_OPTION) {
                thoigian.stop();
            }
        }

    }

    public static void main(String[] args) {
        new Client();
    }

}

